# Plano de commits

Os commits abaixo serao pequenos, independentes e diretamente associados ao backlog. Nenhum commit misturara funcionalidade, teste e documentacao sem necessidade.

| Ordem | Mensagem prevista | Conteudo | Issue |
| --- | --- | --- | --- |
| 1 | `docs: planeja entrega de reserva concorrente` | Escopo, backlog e sequencia de trabalho. | Planejamento |
| 2 | `chore: inicia prototipo de reserva concorrente` | Projeto Spring Boot, configuracao local e instrucoes de execucao. | CC-01 |
| 3 | `feat: modela dominio de sessoes e assentos` | Entidades, repositorios e dados de demonstracao. | CC-02 |
| 4 | `feat: protege reserva concorrente de assentos` | Transacao, bloqueio e regra atomica de reserva. | CC-03 |
| 5 | `feat: expoe api de consulta e reserva` | Endpoints HTTP, contratos JSON e tratamento de erros. | CC-04 |
| 6 | `test: valida reservas simultaneas` | Testes com tentativas paralelas e verificacao de consistencia. | CC-05 |
| 7 | `docs: explica arquitetura e controle de concorrencia` | Diagrama, riscos, sincronizacao e decisoes. | CC-06 |
| 8 | `docs: descreve protocolo http e dados trocados` | Contratos de comunicacao e exemplos de requisicao e resposta. | CC-07 |
| 9 | `docs: prepara demonstracao e registro de IA` | Roteiro da apresentacao, divisao de fala e diario de IA. | CC-08 |

## Convencoes

- Cada commit sera verificado antes de ser enviado ao GitHub.
- Toda issue so muda para concluida quando seus criterios de aceite estiverem cobertos por codigo, teste ou documento verificavel.
- Alteracoes de escopo serao registradas no backlog antes de serem implementadas.
- A mensagem de commit usara o mesmo vocabulario da issue para manter rastreabilidade.
