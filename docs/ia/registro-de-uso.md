# Registro de uso de IA

## Declaracao

Foi utilizada uma assistente de IA para apoiar o planejamento, a implementacao e a documentacao deste prototipo. As decisoes de escopo, a revisao do trabalho e a autorizacao de cada envio ao repositorio foram feitas pelo usuario responsavel pelo projeto.

Este registro resume as interacoes relevantes para que o grupo possa explicar o que a IA sugeriu, o que foi validado e como o codigo funciona.

## Interacoes e resultados

| Etapa | Pedido ou contexto fornecido | Ajuda da IA | Revisao ou decisao humana |
| --- | --- | --- | --- |
| Definicao do foco | Foi solicitado priorizar Fundamentos de Computacao Concorrente, Paralela e Distribuida e deixar base para a outra disciplina. | A IA sugeriu um sistema de cinema reduzido ao problema de reserva concorrente de assentos. | O usuario confirmou que esse seria o foco da primeira entrega. |
| Planejamento | Foi pedido um plano de commits e issues simuladas. | A IA criou backlog, dependencias e sequencia de commits pequenos. | O usuario pediu nomes de commit mais humanos e aprovou cada envio. |
| Ambiente | Foi verificado o ambiente de desenvolvimento. | A IA identificou Java 17 e ausencia de Maven global; estruturou o projeto Spring Boot com Maven Wrapper. | A versao Java do planejamento foi ajustada de 21 para 17 para coincidir com o ambiente real. |
| Modelagem | Foi solicitado iniciar o prototipo aos poucos. | A IA propôs as entidades Sessao, Assento por Sessao e Reserva, com banco relacional e restricao de unicidade. | O usuario aprovou o Commit 2 depois dos testes. |
| Concorrencia | O objetivo era impedir reserva duplicada. | A IA implementou transacao, bloqueio pessimista de escrita e testes simultaneos. | O usuario aprovou o Commit 3 e pediu que os testes fossem ampliados. |
| Correcao | A primeira execucao da nova reserva falhou na compilacao por imports posicionados fora da declaracao da interface do repositorio. | A IA leu o arquivo, reorganizou os imports e repetiu os testes. | A correcao so foi confirmada depois que a suite passou. |
| API e documentacao | Era necessario demonstrar como as partes trocam dados. | A IA criou os endpoints HTTP/JSON e os documentos de arquitetura, sincronizacao e protocolo. | O usuario aprovou cada commit antes do envio. |

## Como entender a solucao

1. A requisicao chega a `SessionController` por HTTP/JSON.
2. O controller transforma os dados em um `ReservationCommand` e chama `ReservationService`.
3. O servico abre uma transacao e bloqueia os assentos solicitados para escrita.
4. Se todos ainda estiverem livres, o servico muda o estado para `RESERVED` e cria a reserva.
5. Se outro pedido vier pelo mesmo assento, ele encontra o assento reservado e recebe conflito.
6. Os testes reproduzem essa disputa com varias tarefas concorrentes.

## Alteracoes e validacoes relevantes

- A proposta inicial mencionava Java 21, mas o ambiente disponivel usava Java 17. A configuracao foi alterada para Java 17, que atende ao Spring Boot utilizado.
- A primeira tentativa de compilar o repositório de assentos falhou por imports fora do lugar. O problema foi inspecionado e corrigido antes de qualquer commit.
- O teste foi ampliado de duas tentativas para oito tentativas simultaneas pelo mesmo assento, alem de reservas simultaneas para assentos diferentes.
- Cada parte foi testada antes do commit e cada push foi autorizado explicitamente pelo usuario.

## Limites do uso de IA

A IA nao substitui a compreensao do grupo. Antes da entrega, todos devem revisar o codigo, executar os testes e conseguir explicar o bloqueio, a transacao e as respostas da API com suas proprias palavras.
