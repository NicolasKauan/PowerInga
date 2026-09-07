# PowerInga API

API REST para gerenciamento de usuários, produtos, solicitações de recompensas e vendas com base em pontos. O sistema foi pensado para permitir que clientes acumulem pontos por meio de solicitações aprovadas por gestores e utilizem esses pontos para comprar produtos.

## Visão geral

O PowerInga é uma aplicação backend em Java com Spring Boot, usando MongoDB como banco de dados e autenticação via JWT. A aplicação expõe endpoints para:

- cadastro e autenticação de usuários
- consulta e gestão de produtos
- criação e análise de solicitações de recompensa
- aprovação ou reprovação por gestores
- registro de vendas e atualização de saldo em pontos

## Funcionalidades principais

- Autenticação com JWT
- Controle de acesso por perfil (`GESTOR` e `CLIENTE`)
- Gestão de produtos
- Solicitações de recompensa com categoria e status
- Cálculo de pontos por categoria
- Registro de vendas por usuário
- Documentação da API com Swagger/OpenAPI

## Tecnologias utilizadas

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Security
- Spring Data MongoDB
- JWT (Auth0)
- Lombok
- Swagger / Springdoc OpenAPI
- Maven Wrapper

## Estrutura do projeto

```text
PowerInga/
├── AGENTS.md
├── README.md
└── poweringa-api/
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com/poweringa/api/
        │   │       ├── config/
        │   │       ├── controllers/
        │   │       ├── dtos/
        │   │       ├── enums/
        │   │       ├── exceptions/
        │   │       ├── handlers/
        │   │       ├── models/
        │   │       ├── repositories/
        │   │       ├── security/
        │   │       ├── services/
        │   │       └── PowerIngaApplication.java
        │   └── resources/
        │       └── application.properties
        └── test/
```

## Arquitetura da API

A aplicação segue uma estrutura simples em camadas:

- `controllers`: expõe os endpoints HTTP
- `services`: contém a lógica de negócio
- `repositories`: acesso ao MongoDB
- `models`: entidades do domínio
- `dtos`: objetos de entrada/saída da API
- `security`: autenticação, autorização e filtros JWT
- `exceptions` e `handlers`: tratamento centralizado de erros

## Principais domínios

### Usuário

Representado por `User`, com os campos principais:

- `email`
- `senha`
- `cargo` (`GESTOR` ou `CLIENTE`)
- `descricao`
- `pontos`

### Produto

Representado por `ProdutosModel`, contendo:

- `descricao`
- `valorPontos`

### Solicitação

Representado por `Solicitacao`, com:

- `descricao`
- `categoria`
- `status` (`PENDENTE`, `APROVADO`, `REPROVADO`)
- `usuario`

### Venda

Representado por `Venda`, incluindo:

- `usuario`
- `itens`
- `totalPontosVenda`
- `created_at`

## Regras de negócio

### Segurança

- A autenticação é feita via JWT.
- O token é validado em cada requisição pelo filtro de segurança.
- A autorização é controlada por roles.

### Perfis de usuário

- `CLIENTE`: pode consultar produtos, criar solicitações e realizar compras com pontos
- `GESTOR`: pode aprovar/reprovar solicitações, consultar usuários e gerenciar produtos

### Solicitações

Quando uma solicitação é aprovada:

- os pontos da categoria são somados ao saldo do usuário

Quando é reprovada:

- o status da solicitação muda para `REPROVADO`
- o saldo do usuário não é alterado

### Vendas

Ao registrar uma venda:

- a API calcula o total de pontos da compra
- verifica se o usuário tem saldo suficiente
- debita os pontos do cliente
- registra a venda

## Configuração

O arquivo de configuração está em:

- [poweringa-api/src/main/resources/application.properties](poweringa-api/src/main/resources/application.properties)

### Variáveis principais

```properties
spring.application.name=api
spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=poweringa_db

api.security.token.secret-key=my-secret-key
```

### Pré-requisitos

- Java 17+
- MongoDB rodando localmente em `localhost:27017`
- A database `poweringa_db` deve existir ou ser criada automaticamente conforme a configuração do Spring Data

## Como executar

A partir da pasta do projeto:

```bash
cd poweringa-api
```

### Linux/macOS

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Windows

```powershell
cd poweringa-api
.
\mvnw.cmd clean install
.
\mvnw.cmd spring-boot:run
```

Se preferir, também pode usar:

```bash
./mvnw test
```

## Swagger

A aplicação possui documentação OpenAPI configurada com Swagger UI. Após subir a API, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

## Endpoints principais

### Autenticação

- `POST /auth/register` — cadastro de usuário
- `POST /auth/login` — autenticação e retorno de JWT

### Produtos

- `GET /produtos` — lista todos os produtos
- `GET /produtos/{id}` — busca produto por ID
- `POST /produtos` — cria produto
- `PUT /produtos/{id}` — atualiza produto
- `DELETE /produtos/{id}` — remove produto

### Solicitações

- `GET /solicitacoes` — lista solicitações do usuário ou de todos, conforme perfil
- `GET /solicitacoes/{id}` — busca solicitação por ID
- `POST /solicitacoes` — cria solicitação
- `PATCH /solicitacoes/{id}/aprovar` — aprova solicitação
- `PATCH /solicitacoes/{id}/reprovar` — reprova solicitação

### Vendas

- `GET /vendas` — lista vendas do usuário ou geral para gestor
- `GET /vendas/{id}` — busca venda por ID
- `POST /vendas` — registra nova venda

### Usuários

- `GET /users` — lista usuários (somente gestor)

## Exemplo de autenticação

Após registrar e fazer login, o retorno será um token JWT. Em requisições autenticadas, envie no header:

```http
Authorization: Bearer <token>
```

## Exemplos de payload

### Registro de usuário

```json
{
  "email": "cliente@email.com",
  "senha": "123456",
  "descricao": "Cliente da plataforma",
  "cargo": "CLIENTE"
}
```

### Login

```json
{
  "email": "cliente@email.com",
  "senha": "123456"
}
```

### Criação de solicitação

```json
{
  "descricao": "Participei da limpeza do bairro",
  "categoria": "MICRO_ACAO"
}
```

### Criação de venda

```json
{
  "itens": [
    {
      "idProduto": "64f2c7d5a1b2c9d3e4f5a6b7",
      "quantidade": 2
    }
  ]
}
```
