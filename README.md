# SocialConnect API

API REST para gestão de beneficiários, doadores, doações e produtos de uma ONG.

## Executar

Requer Java 21+ e Maven 3.9+. Execute `mvn clean compile` e `mvn spring-boot:run`.
O ambiente local usa H2 em memória; Flyway cria o esquema ao iniciar.
Swagger UI: http://localhost:8080/swagger-ui.html.

## Produtos

| Método | Rota | Uso |
| --- | --- | --- |
| GET | `/api/v1/produtos` | Lista paginada; filtros `nome` e `categoria` |
| GET | `/api/v1/produtos/{id_produto}` | Consulta por ID |
| POST | `/api/v1/produtos` | Cadastro (201 e `Location`) |
| PUT | `/api/v1/produtos/{id_produto}` | Substituição total |
| DELETE | `/api/v1/produtos/{id_produto}` | Exclusão (204) |

Exemplo de corpo para POST e PUT:

```json
{"nome":"Arroz 5kg","categoria":"ALIMENTO","estoqueAtual":3,"estoqueMinimo":10,"unidadeMedida":"unidade"}
```

`estoqueBaixo` é calculado quando `estoqueAtual < estoqueMinimo`. Nome duplicado retorna 409; estoque atual negativo retorna 422; ID ausente retorna 404. Os erros usam Problem Details.

## Testes

Execute `mvn test` para rodar os testes unitários de serviço e validação.
