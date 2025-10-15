# 🔐 API Auth Pless - Sistema de Autenticação Passwordless

API REST desenvolvida em Spring Boot para autenticação sem senha (passwordless) com tokens de 6 dígitos, validação de CPF, mascaramento de dados sensíveis e geração de JWT para sessões.

## 🚀 Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.0** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória/arquivo
- **Lombok** - Redução de código boilerplate
- **Maven** - Gerenciamento de dependências
- **JaCoCo** - Cobertura de testes
- **SpringDoc OpenAPI** - Documentação da API
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking para testes

## 📁 Estrutura do Projeto

```
src/
├── main/java/com/exemplo/
│   ├── App.java                          # Classe principal da aplicação
│   ├── controller/
│   │   └── RegistroController.java       # Controlador REST
│   ├── dto/
│   │   ├── IdentificadorRequest.java     # DTO para requisições de validação
│   │   ├── IdentificadorResponse.java    # DTO para respostas com dados mascarados
│   │   └── RegistroRequest.java          # DTO para requisições de registro
│   ├── model/
│   │   └── Registro.java                 # Entidade JPA
│   ├── repository/
│   │   └── RegistroRepository.java       # Repositório JPA
│   ├── service/
│   │   └── RegistroService.java          # Lógica de negócio
│   ├── exception/
│   │   └── GlobalExceptionHandler.java   # Tratamento global de exceções
│   └── config/
│       └── OpenApiConfig.java            # Configuração do Swagger/OpenAPI
├── main/resources/
│   ├── application.properties            # Configurações da aplicação
│   ├── messages.properties               # Mensagens em português
│   └── messages_en.properties            # Mensagens em inglês
└── test/java/com/exemplo/
    ├── AppTest.java                      # Teste da aplicação
    ├── controller/
    │   └── RegistroControllerTest.java   # Testes do controlador
    ├── dto/
    │   └── IdentificadorResponseTest.java # Testes do DTO
    ├── model/
    │   └── RegistroTest.java             # Testes da entidade
    ├── service/
    │   └── RegistroServiceTest.java     # Testes do serviço
    ├── exception/
    │   └── GlobalExceptionHandlerTest.java # Testes do tratamento de exceções
    └── RegistroIntegrationTest.java      # Testes de integração
```

## 🛠️ Funcionalidades

### 1. **CRUD de Registros**
- ✅ **Criar** registro de usuário
- ✅ **Listar** todos os registros
- ✅ **Buscar** por ID
- ✅ **Atualizar** registro existente
- ✅ **Excluir** registro

### 2. **Validação de CPF**
- ✅ **Endpoint**: `POST /api/registros/valida-cpf`
- ✅ **Funcionalidade**: Busca registro por CPF, vertical e jornada
- ✅ **Retorno**: Dados mascarados (email e telefone)
- ✅ **Tratamento**: Retorna `ProblemDetail` quando não encontrado

### 3. **Sistema de Autenticação**
- ✅ **Endpoint**: `POST /api/autenticacao`
- ✅ **Funcionalidade**: Gera token de 6 dígitos numéricos
- ✅ **Persistência**: Transações salvas no banco de dados
- ✅ **Validação**: Endpoint para validar tokens
- ✅ **Segurança**: Tokens únicos e não reutilizáveis

### 4. **Mascaramento de Dados Sensíveis**
- ✅ **Email**: `us***@exemplo.com`
- ✅ **Telefone**: `11*****99`

### 5. **Tratamento de Erros Padronizado**
- ✅ **Email duplicado**: Status 409 com mensagem amigável
- ✅ **CPF duplicado**: Status 409 com mensagem amigável
- ✅ **Registro não encontrado**: Status 404 com `ProblemDetail`
- ✅ **Internacionalização**: Suporte a português e inglês

### 6. **Documentação da API**
- ✅ **Swagger UI**: Disponível em `/swagger-ui.html`
- ✅ **OpenAPI 3**: Documentação automática
- ✅ **Configuração personalizada**: Informações do projeto

### 7. **Cobertura de Testes**
- ✅ **JaCoCo**: Cobertura mínima de 80%
- ✅ **Testes unitários**: Todas as classes testadas
- ✅ **Testes de integração**: Validação end-to-end
- ✅ **TDD**: Desenvolvimento orientado a testes

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.6+

### 1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd demo
```

### 2. **Execute a aplicação**
```bash
mvn spring-boot:run
```

### 3. **Acesse a aplicação**
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

## 📚 Endpoints da API

### **Registros**

#### `GET /api/registros`
Lista todos os registros
```json
Response: [
  {
    "id": 1,
    "cpf": "12345678901",
    "email": "us***@exemplo.com",
    "telefone": "11*****99",
    "vertical": "Tecnologia",
    "jornada": "Desenvolvimento",
    "ativo": true,
    "dataCriacao": "2025-10-14T10:30:00"
  }
]
```

#### `GET /api/registros/{id}`
Busca registro por ID
```json
Response: {
  "id": 1,
  "cpf": "12345678901",
  "email": "us***@exemplo.com",
  "telefone": "11*****99",
  "vertical": "Tecnologia",
  "jornada": "Desenvolvimento",
  "ativo": true,
  "dataCriacao": "2025-10-14T10:30:00"
}
```

#### `POST /api/registros`
Cria novo registro
```json
Request: {
  "cpf": "12345678901",
  "email": "usuario@exemplo.com",
  "telefone": "11999999999",
  "vertical": "Tecnologia",
  "jornada": "Desenvolvimento",
  "ativo": true
}

Response: 201 Created
```

#### `PUT /api/registros/{id}`
Atualiza registro existente
```json
Request: {
  "cpf": "12345678901",
  "email": "usuario@exemplo.com",
  "telefone": "11999999999",
  "vertical": "Tecnologia",
  "jornada": "Desenvolvimento",
  "ativo": true
}

Response: 200 OK
```

#### `DELETE /api/registros/{id}`
Exclui registro
```json
Response: 204 No Content
```

### **Validação de CPF**

#### `POST /api/registros/valida-cpf`
Valida CPF e retorna dados mascarados
```json
Request: {
  "cpf": "12345678901",
  "vertical": "Tecnologia",
  "jornada": "Desenvolvimento"
}

Response: {
  "email": "us***@exemplo.com",
  "telefone": "11*****99"
}
```

### **Autenticação**

#### `POST /api/autenticacao`
Gera token de autenticação de 6 dígitos
```json
Request: {
  "vertical": "Tecnologia",
  "jornada": "Desenvolvimento",
  "cpf": "12345678901",
  "tipoContato": "EMAIL"
}

Response: {
  "identificador": "12345678901_TECNOLOGIA_DESENVOLVIMENTO",
  "token": "123456",
  "mensagem": "Token de autenticação gerado com sucesso"
}
```

#### `POST /api/autenticacao/validar`
Valida token de autenticação
```json
Request: GET /api/autenticacao/validar?identificador=12345678901_TECNOLOGIA_DESENVOLVIMENTO&token=123456

Response: "Token válido"
```

## 🔧 Configurações

### **Banco de Dados**
- **Tipo**: H2 (in-memory/file)
- **URL**: `jdbc:h2:file:./data/registrodb`
- **Console**: http://localhost:8080/h2-console
- **Credenciais**: 
  - Username: `SA`
  - Password: (vazio)

### **Mensagens de Erro**
- **Português**: `messages.properties`
- **Inglês**: `messages_en.properties`
- **Suporte**: Headers `Accept-Language`

### **Cobertura de Testes**
- **Mínima**: 80%
- **Relatório**: `target/site/jacoco/index.html`
- **Comando**: `mvn jacoco:report`

## 🧪 Executando Testes

### **Todos os testes**
```bash
mvn test
```

### **Testes específicos**
```bash
mvn test -Dtest=RegistroControllerTest
mvn test -Dtest=RegistroServiceTest
mvn test -Dtest=GlobalExceptionHandlerTest
```

### **Cobertura de testes**
```bash
mvn jacoco:report
```

## 📊 Exemplos de Uso

### **1. Criar um registro**
```bash
curl -X POST http://localhost:8080/api/registros \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678901",
    "email": "usuario@exemplo.com",
    "telefone": "11999999999",
    "vertical": "Tecnologia",
    "jornada": "Desenvolvimento",
    "ativo": true
  }'
```

### **2. Validar CPF**
```bash
curl -X POST http://localhost:8080/api/registros/valida-cpf \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678901",
    "vertical": "Tecnologia",
    "jornada": "Desenvolvimento"
  }'
```

### **3. Gerar Token de Autenticação**
```bash
curl -X POST http://localhost:8080/api/autenticacao \
  -H "Content-Type: application/json" \
  -d '{
    "vertical": "Tecnologia",
    "jornada": "Desenvolvimento",
    "cpf": "12345678901",
    "tipoContato": "EMAIL"
  }'
```

### **4. Validar Token**
```bash
curl -X POST "http://localhost:8080/api/autenticacao/validar?identificador=12345678901_TECNOLOGIA_DESENVOLVIMENTO&token=123456"
```

### **5. Tratamento de erro (email duplicado)**
```bash
curl -X POST http://localhost:8080/api/registros \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "99999999999",
    "email": "joao.silva@exemplo.com",
    "telefone": "11999999999",
    "vertical": "Teste",
    "jornada": "Teste",
    "ativo": true
  }'

# Response: 409 Conflict
{
  "type": "https://api.exemplo.com/errors/data-integrity-violation",
  "title": "Email já cadastrado",
  "status": 409,
  "detail": "O email fornecido já está sendo utilizado por outro registro. Por favor, utilize um email diferente.",
  "instance": "/api/registros"
}
```

## 🔄 Desenvolvimento

### **Hot Reload**
A aplicação possui Spring Boot DevTools configurado para hot reload automático durante o desenvolvimento.

### **Padrões de Código**
- **TDD**: Testes escritos antes da implementação
- **Clean Code**: Código limpo e bem documentado
- **SOLID**: Princípios de design aplicados
- **Lombok**: Redução de código boilerplate

### **Estrutura de Commits**
```
feat: nova funcionalidade
fix: correção de bug
docs: documentação
test: testes
refactor: refatoração
```

## 📈 Métricas de Qualidade

- ✅ **Cobertura de testes**: 80%+
- ✅ **Testes passando**: 50/50
- ✅ **Código limpo**: Lombok + Clean Code
- ✅ **Documentação**: README + Swagger
- ✅ **Tratamento de erros**: Padronizado
- ✅ **Internacionalização**: PT/EN

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Changelog

### **v1.0.0** - 2025-10-14
- ✅ CRUD completo de registros
- ✅ Validação de CPF com mascaramento
- ✅ Tratamento de erros padronizado
- ✅ Documentação Swagger
- ✅ Cobertura de testes 80%+
- ✅ Internacionalização PT/EN
- ✅ Integração com Lombok

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👥 Autores

- **Desenvolvedor** - *Desenvolvimento inicial* - [SeuNome](https://github.com/seuusuario)

## 🙏 Agradecimentos

- Spring Boot Team
- Lombok Project
- H2 Database
- JUnit Team
- Mockito Team
