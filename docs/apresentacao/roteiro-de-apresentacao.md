# Roteiro da apresentacao

## Objetivo

Demonstrar em 10 minutos que o prototipo lida corretamente com reservas simultaneas, explicando a arquitetura, o risco de venda dupla, a sincronizacao e os resultados dos testes.

## Roteiro sugerido para cinco integrantes

Os nomes e os tempos devem ser ajustados pelo grupo antes da apresentacao. A divisao abaixo existe para que todos participem e todos conhecam a visao completa do projeto.

| Tempo | Pessoa | Fala e demonstracao |
| --- | --- | --- |
| 0:00 - 1:30 | Cesar | Apresenta o problema: dois clientes podem tentar comprar o mesmo assento. Mostra o escopo de no unico e o mapa da arquitetura. |
| 1:30 - 3:00 | Pessoa 2 | Explica os dados compartilhados: sessao, assento por sessao e reserva. Mostra por que A1 nao pode ser duplicado na mesma sessao. |
| 3:00 - 5:00 | Joao | Explica a condicao de corrida e a solucao: transacao, bloqueio de escrita e confirmacao de todos os assentos ou de nenhum. |
| 5:00 - 7:00 | Pessoa 3 | Mostra a API: consulta de assentos e pedido de reserva. Explica as respostas `201 Created` e `409 Conflict`. |
| 7:00 - 8:30 | Pessoa 5 | Executa ou mostra o teste concorrente: oito clientes disputando A1 e duas reservas paralelas para assentos diferentes. |
| 8:30 - 10:00 | Todo o grupo | Recapitula o resultado, responde perguntas e explica o que cada integrante aprendeu. |

## Demonstracao pratica

Antes da apresentacao, execute a suite completa:

```powershell
.\mvnw.cmd clean test
```

Para iniciar a API localmente:

```powershell
.\mvnw.cmd spring-boot:run
```

Com a aplicacao ativa, consulte a sessao de demonstracao:

```powershell
Invoke-RestMethod http://localhost:8080/api/sessions/1/seats
```

Use este pedido para reservar A1:

```json
{
  "customerName": "Ana",
  "seatCodes": ["A1"]
}
```

O primeiro pedido deve receber `201 Created`. Ao enviar o mesmo pedido novamente, a resposta deve ser `409 Conflict`.

## Perguntas que todos devem saber responder

1. Qual e a condicao de corrida do sistema?
2. Por que a leitura simples de um assento livre nao e suficiente?
3. O que o bloqueio pessimista de escrita protege?
4. Por que a reserva precisa ser uma unica transacao?
5. Qual a diferenca entre `201 Created` e `409 Conflict`?
6. O que o teste de oito clientes prova?
7. Qual e o limite do prototipo de no unico?

## Checklist antes de apresentar

- Todos executaram o projeto pelo menos uma vez.
- Cada pessoa explicou sua parte sem ler o documento.
- O grupo sabe localizar o teste de concorrencia.
- O grupo sabe mostrar os documentos de arquitetura e protocolo.
- O grupo revisou o registro de uso de IA e consegue explicar as alteracoes humanas feitas no projeto.
