# AGENTS.md

## Escopo

Estas instruções valem para todo o repositório PowerInga. O projeto é acadêmico: além de entregar código funcional, ajude o desenvolvedor a compreender as decisões e os conceitos envolvidos.

## Estado atual do projeto

- A aplicação está em `poweringa-api/`.
- Backend em Java 17 com Spring Boot 4.1.0 e Maven Wrapper.
- Pacote-base: `com.poweringa.api`.
- A configuração atual contém `spring.application.name=api`, a conexão com o MongoDB (`localhost:27017`, banco `poweringa_db`) e a chave do JWT (`api.security.token.secret-key`).
- O `pom.xml` inclui Spring MVC, Security, Validation, MongoDB, JWT (`java-jwt`), Springdoc OpenAPI (Swagger) e Lombok.
- A persistência é feita com MongoDB (Spring Data MongoDB). Não use JPA nem adicione outro banco sem antes confirmar a decisão.
- O projeto já possui domínio (usuários, produtos, solicitações e vendas), endpoints e arquitetura em camadas (`controllers`, `services`, `repositories`, `models`, `dtos`, `enums`, `security`, `config`, `exceptions` e `handlers`). Siga essa estrutura e não invente padrões complexos antecipadamente.
- A autenticação é feita com JWT e o acesso é controlado por perfil (`GESTOR` e `CLIENTE`).
- Os testes são unitários (JUnit 5 e Mockito) e o JaCoCo exige no mínimo 70% de cobertura de linhas no pacote `services`.

## Forma de trabalho

Para tarefas de desenvolvimento com impacto relevante, siga esta ordem:

1. Entenda a solicitação e identifique requisitos e dúvidas.
2. Inspecione o código relacionado antes de propor alterações.
3. Apresente um plano curto, indicando etapas e arquivos afetados.
4. Explique o motivo e o funcionamento das mudanças propostas.
5. Aguarde aprovação antes de mudanças significativas ou decisões arquiteturais.
6. Implemente somente o escopo aprovado.
7. Execute as verificações adequadas e informe os resultados.
8. Explique como o usuário pode validar o comportamento.

Não exija plano ou aprovação formal para respostas conceituais, inspeções, correções triviais ou tarefas cujo pedido já autorize claramente uma alteração pequena e bem delimitada.

Em trabalhos maiores, divida a implementação em partes compreensíveis e informe a conclusão de cada etapa importante. A comunicação deve ser em português, clara, direta e didática.

## Controle de escopo

- Faça exatamente o que foi solicitado.
- Não adicione funcionalidades, dependências, abstrações ou refatorações fora do escopo.
- Quando identificar uma melhoria útil, apenas a sugira. Use a regra: **sugira primeiro, implemente depois**.
- Se faltar uma decisão que altere significativamente a solução, explique a dúvida e peça orientação.
- Quando uma suposição simples permitir avançar sem risco relevante, declare-a explicitamente.
- Preserve o código e as alterações existentes do usuário. Não reverta nem sobrescreva trabalho não relacionado.
- Prefira mudanças pequenas e localizadas; não reescreva arquivos inteiros quando um patch menor for suficiente.

## Convenções de implementação

- Siga os padrões já presentes no projeto e mantenha o código compatível com Java 17.
- Mantenha classes sob o pacote-base `com.poweringa.api`, organizando subpacotes por responsabilidade ou funcionalidade conforme a estrutura evoluir.
- Use injeção por construtor. Evite injeção direta em campos.
- Mantenha controllers focados no protocolo HTTP, regras de negócio fora dos controllers e acesso a dados isolado em repositories.
- Use DTOs nos limites da API quando houver entrada ou saída HTTP; não exponha entidades de persistência automaticamente.
- Trate validação e erros de forma consistente, mas não crie uma infraestrutura genérica antes de ela ser necessária.
- Nunca inclua senhas, tokens, chaves ou credenciais no repositório. Use variáveis de ambiente ou configuração externa e forneça exemplos seguros quando necessário.
- Não altere versões ou adicione bibliotecas sem justificar a necessidade e obter aprovação quando houver impacto relevante.
- Lombok está disponível, mas use-o apenas quando melhorar a legibilidade; não o aplique indiscriminadamente.
- A persistência do projeto é MongoDB. Antes de usar outra tecnologia de persistência (como JPA), confirme a decisão com o usuário.

## Testes e validação

Execute comandos a partir de `poweringa-api/`, usando o Maven Wrapper do repositório:

```powershell
.\mvnw.cmd test
```

Para iniciar a aplicação localmente:

```powershell
.\mvnw.cmd spring-boot:run
```

Em ambientes Unix:

```bash
./mvnw test
./mvnw spring-boot:run
```

- Para correções pequenas, execute ao menos os testes diretamente relacionados.
- Para alterações de configuração, integração, segurança ou persistência, prefira executar a suíte completa.
- Não afirme que algo funciona sem informar qual verificação foi executada.
- Se um teste não puder ser executado por falta de serviço externo, credencial ou configuração, explique a limitação e forneça os passos exatos para validação manual.
- Ao criar endpoints, informe método, rota, corpo ou parâmetros, resposta esperada e principais cenários de erro. Inclua um exemplo pequeno de requisição quando útil.

## Diagnóstico de erros

Antes de editar código para corrigir um problema:

1. Reproduza ou examine a mensagem completa do erro.
2. Identifique a causa provável com evidências do código ou dos logs.
3. Explique por que o erro ocorre.
4. Faça a menor correção necessária.
5. Verifique a correção e descreva como reproduzir o teste.

Não altere várias áreas por tentativa e erro. Se faltarem dados essenciais, solicite o stack trace, a requisição, a configuração ou os arquivos relevantes.

## Comunicação da entrega

Ao concluir uma alteração, informe de forma objetiva:

- o que foi alterado;
- as decisões ou suposições adotadas;
- quais testes foram executados e seus resultados;
- como validar manualmente, quando aplicável;
- riscos, limitações ou próximos passos que permaneçam fora do escopo.

Explique conceitos importantes na medida da complexidade da tarefa. Evite jargão sem explicação, textos longos sem necessidade e grandes blocos de implementação sem contexto.
