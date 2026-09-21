# Backlog de issues simuladas

Estas issues representam o quadro de trabalho que sera mantido no repositorio durante a primeira entrega. Cada issue tem um commit principal previsto para que o historico do Git explique o progresso do projeto.

| ID | Titulo | Prioridade | Dependencia | Commit principal previsto | Estado |
| --- | --- | --- | --- | --- | --- |
| CC-01 | Estruturar o prototipo e a documentacao inicial | Alta | Nenhuma | `chore: inicia prototipo de reserva concorrente` | Planejada |
| CC-02 | Modelar sessao, assento e reserva | Alta | CC-01 | `feat: modela dominio de sessoes e assentos` | Concluida |
| CC-03 | Implementar reserva atomica de assentos | Alta | CC-02 | `feat: protege reserva concorrente de assentos` | Concluida |
| CC-04 | Expor protocolo HTTP para consulta e reserva | Media | CC-03 | `feat: expoe api de consulta e reserva` | Concluida |
| CC-05 | Demonstrar concorrencia com testes simultaneos | Alta | CC-03, CC-04 | `test: valida reservas simultaneas` | Concluida |
| CC-06 | Documentar arquitetura, riscos e sincronizacao | Alta | CC-03 | `docs: explica arquitetura e controle de concorrencia` | Concluida |
| CC-07 | Documentar protocolo e contrato de dados | Media | CC-04 | `docs: descreve protocolo http e dados trocados` | Planejada |
| CC-08 | Preparar roteiro de demonstracao e diario de IA | Media | CC-05, CC-06, CC-07 | `docs: prepara demonstracao e registro de IA` | Planejada |

## CC-01 Estruturar o prototipo e a documentacao inicial

**Objetivo:** criar o esqueleto Java, os padroes de pastas e os documentos base da entrega.

**Criterios de aceite:** o projeto inicia localmente; ha uma instrucao de execucao; as decisoes de escopo e arquitetura estao registradas.

## CC-02 Modelar sessao, assento e reserva

**Objetivo:** representar a capacidade de uma sessao e os estados de seus assentos.

**Criterios de aceite:** uma sessao pode ter assentos livres e reservados; uma reserva referencia cliente, sessao e assentos; nao existe duplicidade de assento na mesma sessao.

## CC-03 Implementar reserva atomica de assentos

**Objetivo:** assegurar que a reserva seja uma operacao indivisivel.

**Criterios de aceite:** duas tentativas concorrentes pelo mesmo assento resultam em uma unica confirmacao; falhas nao deixam reserva parcial; os bloqueios seguem ordem estavel.

## CC-04 Expor protocolo HTTP para consulta e reserva

**Objetivo:** permitir demonstrar o prototipo por requisicoes HTTP e JSON.

**Criterios de aceite:** a API lista assentos por sessao; a API recebe pedido de reserva; as respostas distinguem confirmacao, indisponibilidade e dados invalidos.

## CC-05 Demonstrar concorrencia com testes simultaneos

**Objetivo:** comprovar a correcao em execucoes concorrentes.

**Criterios de aceite:** um teste dispara multiplas tentativas simultaneas; apenas uma reserva do mesmo assento vence; reservas de assentos diferentes nao se bloqueiam desnecessariamente.

## CC-06 Documentar arquitetura, riscos e sincronizacao

**Objetivo:** atender aos criterios de escopo, mapa da arquitetura e identificacao dos riscos de concorrencia.

**Criterios de aceite:** documento inclui o mapa dos componentes, justificativas tecnicas, condicao de corrida, estrategia de sincronizacao, prevencao de impasse e tratamento de falhas.

## CC-07 Documentar protocolo e contrato de dados

**Objetivo:** deixar explicito como as partes se comunicam e quais dados trafegam.

**Criterios de aceite:** endpoints, metodos, exemplos JSON, codigos de resposta e regras de validacao estao descritos.

## CC-08 Preparar roteiro de demonstracao e diario de IA

**Objetivo:** organizar a apresentacao e registrar o uso de IA de forma transparente.

**Criterios de aceite:** roteiro mostra a disputa por assento; cada integrante tem uma parte de fala; o diario explica pedidos feitos, respostas aproveitadas e alteracoes manuais.
