# Arquitetura e sincronizacao das reservas

## Objetivo do prototipo

O prototipo representa a reserva de assentos para uma sessao de cinema. O problema concorrente central e impedir que duas requisicoes confirmem o mesmo assento quando chegam praticamente no mesmo instante.

O sistema opera como um unico no: uma aplicacao Spring Boot recebe varias requisicoes HTTP ao mesmo tempo e usa um banco relacional para manter o estado compartilhado das reservas.

## Mapa da arquitetura

```text
Cliente 1 ─┐
           ├── HTTP + JSON ──> API REST ──> ReservationService ──> JPA ──> Banco relacional H2
Cliente 2 ─┘                         |                              |
                                      |                              └─ movie_sessions
                                      |                              └─ session_seats
                                      |                              └─ seat_reservations
                                      |
                                      └─ transacao + bloqueio de escrita por assento
```

| Componente | Responsabilidade | Motivo da escolha |
| --- | --- | --- |
| API REST | Receber consultas e pedidos de reserva. | HTTP e JSON sao simples de testar e permitem observar requisicoes simultaneas. |
| ReservationService | Validar o pedido, bloquear assentos, confirmar a reserva ou recusá-la. | Mantem a regra concorrente em um unico ponto. |
| JPA | Mapear os objetos Java para tabelas relacionais. | Evita SQL repetido e integra transacoes com a aplicacao. |
| Banco relacional H2 | Armazenar sessoes, assentos e reservas durante o prototipo. | Permite demonstrar bloqueios e restricoes sem infraestrutura externa. |
| Testes concorrentes | Executar varias tentativas ao mesmo tempo. | Produzem evidencia reproduzivel de que nao ocorre venda dupla. |

## Dados compartilhados

Cada linha de `session_seats` identifica um assento dentro de uma sessao. Ela possui o estado `AVAILABLE` ou `RESERVED`.

O banco tambem possui uma restricao unica para `(movie_session_id, seat_code)`. Assim, a mesma sessao nunca pode criar duas representacoes do assento A1, por exemplo.

## Risco de condicao de corrida

Sem sincronizacao, duas tarefas poderiam seguir esta sequencia:

```text
Tarefa A: le A1 como AVAILABLE
Tarefa B: le A1 como AVAILABLE
Tarefa A: confirma a reserva de A1
Tarefa B: confirma a reserva de A1
```

O resultado seria uma venda dupla: duas reservas apontando para um unico assento.

## Estrategia de sincronizacao

A reserva e executada dentro de uma transacao. Antes de conferir a disponibilidade, o repositorio busca os assentos solicitados com bloqueio pessimista de escrita (`PESSIMISTIC_WRITE`). As linhas sao solicitadas em ordem crescente de identificador.

```text
1. Receber sessao, cliente e lista de assentos.
2. Validar que a lista nao esta vazia e nao contem duplicatas.
3. Iniciar transacao.
4. Bloquear as linhas dos assentos solicitados.
5. Confirmar que todos existem e ainda estao AVAILABLE.
6. Marcar todos como RESERVED e criar uma unica reserva.
7. Confirmar a transacao.
```

Enquanto a primeira transacao mantem o bloqueio de A1, uma segunda tentativa pelo mesmo assento aguarda. Quando o bloqueio e liberado, ela encontra A1 como `RESERVED` e recebe uma resposta de indisponibilidade. Se um pedido tiver mais de um assento, todos sao confirmados juntos ou nenhum e confirmado.

## Prevencao de impasses e erros

- Os assentos de um pedido sao ordenados antes da busca para reduzir o risco de duas transacoes bloquearem os mesmos itens em sequencias diferentes.
- Dados invalidos, sessao inexistente ou assento indisponivel geram excecoes e nao criam uma reserva parcial.
- O estado do assento e alterado apenas dentro da mesma transacao que cria a reserva.
- A restricao unica no banco protege a identidade de cada assento dentro da sessao, mesmo se um erro de programacao tentar repeti-lo.

## Evidencias executaveis

O projeto possui os seguintes testes:

| Cenario | Resultado esperado |
| --- | --- |
| Oito clientes solicitam A1 ao mesmo tempo. | Apenas uma reserva e confirmada. |
| Dois clientes solicitam A1 e A2 ao mesmo tempo. | As duas reservas sao confirmadas. |
| O mesmo assento e cadastrado duas vezes na mesma sessao. | O banco rejeita a duplicacao. |
| Um cliente tenta reservar A1 apos uma reserva confirmada. | A API responde `409 Conflict`. |

Para executar toda a evidencia: `./mvnw test` no Linux/macOS ou `./mvnw.cmd test` no Windows.

## Limite do escopo atual

O prototipo demonstra concorrencia dentro de uma unica aplicacao e de um unico banco. Ele nao pretende resolver coordenacao entre varios servidores ou bancos distribuidos. Caso o sistema seja expandido depois, a regra de reserva e o banco transacional permanecem como base para evoluir a solucao.
