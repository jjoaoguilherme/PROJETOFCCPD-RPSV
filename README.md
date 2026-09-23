# PROJETO-FCCPD---RPSV-

Protótipo web para demonstrar a reserva concorrente de assentos em sessões de cinema.

## Primeira entrega

O foco atual é impedir que duas solicitações simultâneas confirmem o mesmo assento. As funcionalidades completas de um sistema de cinema serão incorporadas posteriormente para a disciplina de Requisitos, projeto de software e validação.

## Tecnologias

- Java 17
- Spring Boot
- Maven
- HTTP e JSON

## Executar localmente

Com Java 17 instalado, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Na primeira execucao, o Maven Wrapper baixa automaticamente a versao de Maven usada pelo projeto.

Depois, confira se a aplicação está ativa em `http://localhost:8080/api/health`.

## Documentação

- [Arquitetura e sincronização](docs/concorrencia/arquitetura-e-sincronizacao.md)
- [Protocolo HTTP e dados](docs/concorrencia/protocolo-e-dados.md)
- [Registro de uso de IA](docs/ia/registro-de-uso.md)
