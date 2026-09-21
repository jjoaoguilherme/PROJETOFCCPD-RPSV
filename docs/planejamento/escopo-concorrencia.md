# Escopo da entrega de Computacao Concorrente

## Objetivo da primeira entrega

Construir um prototipo web de um unico no para reserva de assentos em sessoes de cinema. O sistema deve permitir que varios clientes tentem reservar assentos ao mesmo tempo sem que o mesmo assento seja vendido duas vezes.

O prototipo sera deliberadamente pequeno. Ele nao incluira, nesta fase, catalogo completo de filmes, comentarios, precificacao avancada ou gestao administrativa completa. Esses modulos poderao ser adicionados posteriormente para a disciplina de Requisitos, projeto de software e validacao.

## Fluxo demonstravel

1. Uma sessao possui uma lista de assentos.
2. Dois ou mais clientes fazem requisicoes de reserva simultaneas para o mesmo assento.
3. Somente a primeira reserva valida e confirmada.
4. As demais recebem uma resposta clara de indisponibilidade.
5. Reservas para assentos diferentes podem ser processadas em paralelo.

## Arquitetura inicial

```text
Cliente de demonstracao
        |
        | HTTP + JSON
        v
API REST Spring Boot (um unico no)
        |
        | transacao de reserva
        v
Servico de Reserva
        |
        | bloqueio dos assentos em ordem estavel
        v
Persistencia JPA / banco relacional
```

## Decisoes tecnicas

| Decisao | Motivo |
| --- | --- |
| Java 17 e Spring Boot | Usa a versao Java disponivel no ambiente e mantem a base compativel com a outra disciplina. |
| JPA e banco relacional | Permitem transacoes e restricoes de unicidade para proteger a reserva. |
| HTTP e JSON | Sao simples de demonstrar, testar e documentar como protocolo de comunicacao. |
| Bloqueio transacional por assento | Evita que duas tarefas confirmem o mesmo assento. |
| Teste com requisicoes simultaneas | Prova que o comportamento correto nao depende de sorte ou de execucao sequencial. |

## Regra de consistencia

Uma reserva somente sera confirmada se todos os assentos solicitados ainda estiverem livres dentro da mesma transacao. Os assentos serao bloqueados em ordem crescente de identificador para reduzir o risco de impasse. O banco tambem tera uma restricao unica por sessao e assento como ultima camada de defesa.

## Caminho para a segunda disciplina

Esta entrega cria os elementos centrais do futuro sistema de cinema: Sessao, Assento, Cliente e Reserva. Na continuacao, serao encaixados autenticacao, compra, ingresso, filme, sala, cinema, genero e precificacao sem substituir a regra de reserva concorrente.
