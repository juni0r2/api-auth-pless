# 📝 Changelog - API Auth Pless

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2025-10-14

### 🎉 Adicionado
- **CRUD completo** de registros de usuários
- **Validação de CPF** com endpoint `/api/registros/valida-cpf`
- **Sistema de Autenticação** com endpoint `/api/autenticacao`
- **Geração de tokens** de 6 dígitos numéricos
- **Persistência de transações** de autenticação
- **Validação de tokens** com endpoint `/api/autenticacao/validar`
- **Mascaramento de dados sensíveis** (email e telefone)
- **Tratamento de erros padronizado** com `ProblemDetail`
- **Internacionalização** (português e inglês)
- **Documentação Swagger/OpenAPI** automática
- **Cobertura de testes** com JaCoCo (80%+)
- **Testes unitários e de integração** completos
- **Integração com Lombok** para redução de código
- **Spring Boot DevTools** para hot reload
- **H2 Database** com console web
- **Mensagens centralizadas** em `messages.properties`

### 🔧 Funcionalidades Técnicas
- **Spring Boot 3.5.0** com Java 21
- **Spring Data JPA** para persistência
- **H2 Database** para desenvolvimento
- **Lombok** para redução de boilerplate
- **JaCoCo** para cobertura de testes
- **SpringDoc OpenAPI** para documentação
- **JUnit 5** e **Mockito** para testes
- **Maven** para gerenciamento de dependências

### 🛠️ Endpoints Implementados
- `GET /api/registros` - Listar todos os registros
- `GET /api/registros/{id}` - Buscar por ID
- `POST /api/registros` - Criar novo registro
- `PUT /api/registros/{id}` - Atualizar registro
- `DELETE /api/registros/{id}` - Excluir registro
- `POST /api/registros/valida-cpf` - Validar CPF
- `POST /api/autenticacao` - Gerar token de autenticação
- `POST /api/autenticacao/validar` - Validar token

### 🧪 Testes Implementados
- **RegistroControllerTest** - 11 testes
- **RegistroServiceTest** - 12 testes
- **RegistroTest** - 11 testes
- **IdentificadorResponseTest** - 9 testes
- **GlobalExceptionHandlerTest** - 3 testes
- **RegistroIntegrationTest** - 3 testes
- **AutenticacaoControllerTest** - 4 testes
- **AutenticacaoServiceTest** - 5 testes
- **AppTest** - 1 teste
- **Total**: 59 testes (100% passando)

### 📚 Documentação
- **README.md** completo com instruções
- **Swagger UI** disponível em `/swagger-ui.html`
- **H2 Console** disponível em `/h2-console`
- **Mensagens de erro** padronizadas e localizadas

### 🔒 Segurança e Qualidade
- **Validação de dados** com Bean Validation
- **Tratamento de exceções** global e padronizado
- **Mascaramento de dados** sensíveis
- **Cobertura de testes** acima de 80%
- **Código limpo** com Lombok

### 🌐 Internacionalização
- **Português** (padrão): `messages.properties`
- **Inglês**: `messages_en.properties`
- **Suporte** via header `Accept-Language`

### 📊 Métricas de Qualidade
- ✅ **59 testes passando** (100% de sucesso)
- ✅ **Cobertura JaCoCo** acima de 80%
- ✅ **Código limpo** com Lombok
- ✅ **Documentação completa** (README + Swagger)
- ✅ **Tratamento de erros** padronizado
- ✅ **Internacionalização** PT/EN
- ✅ **Sistema de autenticação** seguro

---

## 📋 Template para Futuras Versões

### [X.Y.Z] - YYYY-MM-DD

#### 🎉 Adicionado
- Nova funcionalidade
- Novo endpoint
- Nova validação

#### 🔧 Alterado
- Mudança em funcionalidade existente
- Refatoração de código
- Atualização de dependências

#### 🐛 Corrigido
- Correção de bug
- Melhoria de performance
- Correção de validação

#### 🗑️ Removido
- Funcionalidade removida
- Endpoint descontinuado
- Dependência removida

#### 🔒 Segurança
- Correção de vulnerabilidade
- Melhoria de segurança
- Nova validação de segurança

#### 📚 Documentação
- Atualização do README
- Nova documentação
- Correção de exemplos

#### 🧪 Testes
- Novo teste
- Correção de teste
- Melhoria de cobertura

---

## 📝 Notas de Desenvolvimento

### Padrões Seguidos
- **TDD**: Testes escritos antes da implementação
- **Clean Code**: Código limpo e bem documentado
- **SOLID**: Princípios de design aplicados
- **REST**: Padrões RESTful seguidos
- **RFC 7807**: ProblemDetail para tratamento de erros

### Convenções de Commit
- `feat:` nova funcionalidade
- `fix:` correção de bug
- `docs:` documentação
- `test:` testes
- `refactor:` refatoração
- `style:` formatação
- `chore:` tarefas de manutenção

### Estrutura de Branches
- `main` - branch principal
- `develop` - branch de desenvolvimento
- `feature/nome-da-feature` - novas funcionalidades
- `fix/nome-do-bug` - correções
- `docs/nome-da-doc` - documentação
