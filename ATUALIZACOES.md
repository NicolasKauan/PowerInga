# Atualizações do projeto

Registro das mudanças feitas na finalização do front-end, em ordem cronológica (a mais recente no topo).
Cada entrada informa o que mudou, por quê e o que pode afetar outras partes do sistema.

Branch de trabalho: `feature/add-front-end`.

## Etapas planejadas

| Etapa | Descrição | Status |
|---|---|---|
| 0 | Ajustes na API para o front (`/users/me`, CORS) | Concluída |
| 1 | Base do front: token, autenticação, rotas protegidas, header | Concluída |
| 2 | Correções do Login e tela de Cadastro | Em andamento |
| 3 | Home do usuário com saldo real | Pendente |
| 4 | Lojinha e compra de produtos | Pendente |
| 5 | Poste sua ação | Pendente |
| 6 | Consultar andamento das solicitações | Pendente |
| 7 | Painel do gestor (solicitações e usuários) | Pendente |

## Decisões tomadas

- O cadastro usa os campos da API (e-mail, nome e senha). Os campos do protótipo (CPF, nome de usuário) não serão usados.
- O campo `descricao` do usuário é exibido como **Nome**.
- O envio de foto ou vídeo em "Poste sua ação" fica de fora, porque a API não suporta upload.
- As telas de Ajuda e a Home do gestor terão uma versão simples, já que não estão no protótipo.
- O cadastro público sempre cria usuários `CLIENTE`. Gestores são criados direto no banco.

---

## 12/11/2026

### Etapa 2: tela de Cadastro (sem commit; falta o teste no navegador)

- Nova página `RegisterPage` na rota pública `/cadastro`, com o mesmo layout do Login (`AuthLayout` + `FormField`).
- Campos: Nome (`descricao`), E-mail, Senha e Confirmar senha. A confirmação é validada só no front e não é enviada para a API.
- O corpo da requisição é montado de forma explícita (`{ descricao, email, senha }`).
- Respostas da API:
  - sucesso: o usuário vai para o login, com o aviso "Conta criada!";
  - 409: "Já existe uma conta com esse e-mail.";
  - 400: as mensagens de validação da API;
  - sem resposta: a mensagem de falha de conexão;
  - qualquer outro erro: uma mensagem genérica.
- O formato do e-mail e as regras de senha não são validados no front, para não divergirem da API, que é a fonte da regra.
- Verificado via HTTP: cadastro (200), login com a conta nova (200), e-mail repetido (409), e-mail inválido (400) e nome vazio (400). Os acentos chegam em UTF-8.
- **Problema já existente, fora do escopo:** `npm run lint` acusa `React` importado e não usado em `UserStorePage.jsx`.

### Testes: pastas corrigidas (sem commit)

- Cinco testes estavam na pasta `test/.../services/`, mas declaravam outro pacote. Foram movidos com `git mv` para a pasta do pacote que declaram:
  - `AuthControllerTest`, `ProdutosControllerTest`, `SolicitacaoControllerTest` e `UserControllerTest` foram para `controllers/`;
  - `SecurityFilterTest` foi para `security/`.
  - **Motivo:** o editor compilava cópias quebradas desses testes em `target/`, e o Maven rodava essas cópias, gerando 5 erros `NoClassDefFound` falsos. Nenhuma linha de código mudou.
- Resultado: `mvnw clean test` passou com 67 testes, 0 falhas e 0 erros.

### Etapa 2: Nome obrigatório no cadastro (sem commit)

- O campo `descricao` (exibido como **Nome**) do `RegisterRequestDTO` passou a ter `@NotBlank`, com a mensagem `"ERRO: Nome é obrigatório!"`.
  - **Motivo:** sem isso, era possível criar um usuário sem nome pela API (Postman, Swagger), mesmo com o front exigindo o campo.
  - **Impacto:** `POST /auth/register` sem `descricao`, ou com `descricao` vazia, agora responde **400**. Os usuários que já estão no banco não são afetados.
- Novo teste `RegisterRequestDTOValidationTest`, que valida o DTO direto pelo `Validator`. Ele falhou antes da anotação e passou depois dela.

### Documentação: AGENTS.md (sem commit)

- A seção "Estado atual do projeto" foi reescrita para refletir o código de hoje: pacote `com.poweringa.api`, MongoDB (sem JPA), JWT, Swagger, camadas existentes e o front `poweringa-web`.
  - **Motivo:** o texto antigo descrevia o projeto de agosto (pacote `nika`, persistência indefinida, sem endpoints) e podia levar ferramentas de IA a decisões erradas.
- Na seção "Convenções", o pacote-base passou para `com.poweringa.api`, e a regra "confirme se usará JPA ou MongoDB" virou "a persistência é MongoDB; não adicione outra tecnologia de banco sem aprovação".

## 11/11/2026

### Etapa 2: Login e Cadastro (em andamento, sem commit)

**API**
- `cargo` foi removido do `RegisterRequestDTO`. O `AuthService.register` agora sempre cria o usuário como `CLIENTE`.
  - **Motivo:** antes, qualquer pessoa podia se cadastrar como `GESTOR` enviando `"cargo": "GESTOR"`.
  - **Impacto:** quem chama `POST /auth/register` (front, Postman, Swagger) não deve mais enviar `cargo`.
- O teste de cadastro passou a verificar que o usuário é salvo como `CLIENTE` e com a senha criptografada.
- O exemplo de cadastro no README foi atualizado.
- Foi removido um `System.out.println` do `AuthService.register` que escrevia o e-mail de cada cadastro no console.
  - **Motivo:** era código de depuração e expunha um dado pessoal no log.

**Front**
- Componente `AuthLayout`: painel verde com o logo, compartilhado entre Login e Cadastro.
- Componente `FormField`: rótulo, campo e mensagem de erro, para não repetir o mesmo bloco em cada formulário.
- Login:
  - o botão "Criar uma conta" enviava o formulário; agora é um link para `/cadastro`;
  - os erros aparecem embaixo de cada campo;
  - o alerta de sucesso foi removido, e o usuário entra direto;
  - quando o servidor não responde, aparece uma mensagem de falha de conexão em vez de "senha incorreta".

**Falta:** criar a tela `RegisterPage` e a rota `/cadastro`. Até lá, o link "Criar uma conta" leva a uma página vazia.

### Etapa 1: base do front (commit `c626a57`)

- O axios passou a enviar `Authorization: Bearer <token>` em todas as requisições. Antes, o token era salvo mas nunca enviado.
- `AuthProvider` / `useAuth` guardam o usuário logado (busca em `GET /users/me`). Ao abrir o app com um token inválido ou expirado, o usuário é deslogado.
- `ProtectedRoute` libera as rotas conforme o cargo. É só usabilidade: a segurança de verdade continua na API.
- O `Header` virou um componente reutilizável, com a opção **Sair** ao clicar na foto. No celular, a foto agora aparece, para o "Sair" ficar acessível.
- O login redireciona conforme o cargo: `CLIENTE` vai para `/home` e `GESTOR` vai para `/gestor`. A tela `/gestor` só será criada na etapa 7.

**Limitação conhecida:** a API responde `403` tanto para token inválido quanto para falta de permissão. Por isso, se o token expirar durante o uso (o token dura 2h), a tela mostra erro em vez de voltar para o login.

### Etapa 0: ajustes na API (commit `2abebbb`)

- Novo endpoint `GET /users/me`, que devolve o usuário logado (`id`, `email`, `descricao`, `cargo`, `pontos`). Qualquer usuário autenticado pode chamar.
- O CORS passou a aceitar `PUT`, `PATCH` e `DELETE`, além de `GET` e `POST`.
  - **Motivo:** sem isso, o navegador bloqueava aprovar/reprovar solicitações e editar/remover produtos.
  - Nenhuma permissão nova foi aberta: quem pode fazer cada operação continua definido no `SecurityConfiguration`.

---

## Observações para o grupo

- **Rodar os testes da API:** é preciso JDK 17. Com Java 8 aparece o erro `No compiler is provided in this environment`.
  ```powershell
  $env:JAVA_HOME="<caminho do JDK 17>"; .\mvnw.cmd test
  ```
- O `AGENTS.md` está desatualizado: cita o pacote `com.poweringa.nika` e diz que não há endpoints.
