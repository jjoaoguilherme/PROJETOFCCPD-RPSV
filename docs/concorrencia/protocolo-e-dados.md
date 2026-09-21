# Protocolo de comunicacao e dados

## Escolha do protocolo

O prototipo usa HTTP com JSON. HTTP permite que varios clientes independentes enviem pedidos de reserva ao mesmo tempo; JSON torna os dados pequenos, legiveis e simples de verificar durante a demonstracao.

Todas as respostas da API usam JSON e a aplicacao local inicia, por padrao, em `http://localhost:8080`.

## Consulta de assentos

### `GET /api/sessions/{sessionId}/seats`

Retorna os dados de uma sessao e seus assentos ordenados pelo codigo.

**Exemplo de resposta `200 OK`:**

```json
{
  "id": 1,
  "movieTitle": "Cidade em Movimento",
  "startsAt": "2026-10-10T19:30:00",
  "seats": [
    { "code": "A1", "status": "AVAILABLE" },
    { "code": "A2", "status": "RESERVED" }
  ]
}
```

Se a sessao nao existir, a API retorna `404 Not Found`.

## Pedido de reserva

### `POST /api/sessions/{sessionId}/reservations`

O corpo deve ter o nome do cliente e a lista de assentos desejados.

**Exemplo de pedido:**

```json
{
  "customerName": "Ana",
  "seatCodes": ["A1", "A2"]
}
```

**Exemplo de resposta `201 Created`:**

```json
{
  "reservationId": 12,
  "status": "CONFIRMED",
  "seatCodes": ["A1", "A2"]
}
```

## Respostas de erro

As falhas esperadas pela regra de negocio retornam o mesmo formato simples:

```json
{
  "message": "Um ou mais assentos solicitados nao estao disponiveis."
}
```

| Situacao | Codigo | Significado |
| --- | --- | --- |
| Sessao inexistente | `404 Not Found` | O identificador da sessao nao foi encontrado. |
| Nome ausente, lista vazia, nula ou com assentos repetidos | `400 Bad Request` | O pedido nao respeita o contrato de dados. |
| Assento inexistente ou ja reservado | `409 Conflict` | O estado atual da sessao impede a confirmacao. |

## Regras de dados

- `sessionId` identifica a sessao que contem os assentos.
- `customerName` e obrigatorio e nao pode conter apenas espacos.
- `seatCodes` deve conter pelo menos um assento e nao pode repetir codigos.
- Os codigos sao tratados sem espacos nas pontas e em ordem estavel durante a reserva.
- Uma reserva e confirmada apenas se todos os assentos solicitados existirem e estiverem livres.

## Comunicacao durante uma disputa concorrente

Quando dois clientes enviam `POST` para A1 praticamente ao mesmo tempo, ambos usam o mesmo contrato HTTP. A diferenca aparece no resultado:

```text
Cliente A -> POST A1 -> 201 Created
Cliente B -> POST A1 -> 409 Conflict
```

O segundo cliente nao recebe uma confirmacao enganosa: ele recebe um conflito porque a transacao da primeira reserva alterou o estado compartilhado de A1 para `RESERVED`.

## Como reproduzir localmente

1. Inicie com `./mvnw.cmd spring-boot:run` no Windows.
2. Consulte `GET /api/sessions/1/seats`.
3. Envie o pedido de reserva para A1.
4. Repita o mesmo pedido e observe `409 Conflict`.

Os testes automatizados tambem exercitam a consulta, a confirmacao e o conflito da API.
