# 🤝 Guia de Contribuição

Obrigado por considerar contribuir com este projeto! Este documento fornece diretrizes para contribuições.

## 📋 Índice

- [Como Contribuir](#como-contribuir)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Padrões de Código](#padrões-de-código)
- [Testes](#testes)
- [Documentação](#documentação)
- [Processo de Pull Request](#processo-de-pull-request)

## 🚀 Como Contribuir

### 1. **Fork e Clone**
```bash
# Fork o repositório no GitHub
# Clone seu fork
git clone https://github.com/SEU_USUARIO/demo.git
cd demo
```

### 2. **Configure o Remote**
```bash
# Adicione o repositório original como upstream
git remote add upstream https://github.com/REPOSITORIO_ORIGINAL/demo.git
```

### 3. **Crie uma Branch**
```bash
# Para nova funcionalidade
git checkout -b feature/nome-da-funcionalidade

# Para correção de bug
git checkout -b fix/nome-do-bug

# Para documentação
git checkout -b docs/nome-da-doc
```

## 🛠️ Configuração do Ambiente

### **Pré-requisitos**
- Java 21+
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### **Configuração**
```bash
# Clone o repositório
git clone <url-do-fork>
cd demo

# Instale as dependências
mvn clean install

# Execute os testes
mvn test

# Execute a aplicação
mvn spring-boot:run
```

### **Verificações**
- ✅ Aplicação inicia sem erros
- ✅ Todos os testes passam
- ✅ Cobertura de testes mantida (80%+)
- ✅ Código compila sem warnings

## 📝 Padrões de Código

### **Java**
- **Java 21** features quando apropriado
- **Lombok** para redução de boilerplate
- **Clean Code** principles
- **SOLID** principles
- **Naming conventions** em português para domínio

### **Spring Boot**
- **RESTful** endpoints
- **DTOs** para transferência de dados
- **Service layer** para lógica de negócio
- **Repository pattern** para persistência
- **Global exception handling**

### **Estrutura de Classes**
```java
// 1. Imports
// 2. Anotações
// 3. Declaração da classe
// 4. Campos
// 5. Construtores
// 6. Métodos públicos
// 7. Métodos privados
```

### **Exemplo de Classe**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExemploDto {
    private String campo;
    
    public ExemploDto(String campo) {
        this.campo = processarCampo(campo);
    }
    
    private String processarCampo(String campo) {
        // Lógica específica
        return campo;
    }
}
```

## 🧪 Testes

### **Obrigatório**
- **TDD**: Testes escritos antes da implementação
- **Cobertura mínima**: 80%
- **Todos os testes passando**: 100%

### **Tipos de Teste**
- **Unitários**: Testam classes isoladamente
- **Integração**: Testam fluxo completo
- **Controller**: Testam endpoints REST
- **Service**: Testam lógica de negócio
- **Exception**: Testam tratamento de erros

### **Estrutura de Teste**
```java
@SpringBootTest
class ExemploTest {
    
    @Test
    @DisplayName("Deve fazer algo quando condição")
    void deveFazerAlgoQuandoCondicao() {
        // Given
        // When
        // Then
    }
}
```

### **Comandos de Teste**
```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=NomeDoTeste

# Cobertura
mvn jacoco:report

# Teste com relatório
mvn test jacoco:report
```

## 📚 Documentação

### **README.md**
- Atualizar para novas funcionalidades
- Adicionar novos endpoints
- Atualizar exemplos de uso
- Manter estrutura organizada

### **CHANGELOG.md**
- Documentar todas as mudanças
- Seguir formato [Keep a Changelog](https://keepachangelog.com/)
- Usar emojis para categorias
- Manter ordem cronológica

### **Código**
- **JavaDoc** para métodos públicos
- **Comentários** para lógica complexa
- **Nomes descritivos** para variáveis e métodos
- **README** em cada módulo se necessário

### **Swagger/OpenAPI**
- **Anotações** quando necessário
- **Exemplos** nos DTOs
- **Descrições** claras dos endpoints
- **Códigos de resposta** documentados

## 🔄 Processo de Pull Request

### **1. Preparação**
```bash
# Atualize sua branch
git fetch upstream
git checkout main
git merge upstream/main

# Atualize sua feature branch
git checkout feature/sua-feature
git merge main
```

### **2. Desenvolvimento**
- ✅ Código segue padrões estabelecidos
- ✅ Testes escritos e passando
- ✅ Cobertura de testes mantida
- ✅ Documentação atualizada
- ✅ Commits com mensagens claras

### **3. Commit Messages**
```
feat: adiciona validação de CPF
fix: corrige mascaramento de email
docs: atualiza README com novos endpoints
test: adiciona testes para validação
refactor: melhora estrutura do service
```

### **4. Pull Request**
- **Título**: Descritivo e claro
- **Descrição**: O que foi implementado
- **Checklist**: Itens verificados
- **Screenshots**: Se aplicável
- **Testes**: Evidência de funcionamento

### **Template de PR**
```markdown
## 📝 Descrição
Breve descrição das mudanças implementadas.

## 🎯 Tipo de Mudança
- [ ] Bug fix
- [ ] Nova funcionalidade
- [ ] Breaking change
- [ ] Documentação
- [ ] Refatoração

## ✅ Checklist
- [ ] Código segue padrões estabelecidos
- [ ] Testes escritos e passando
- [ ] Cobertura de testes mantida (80%+)
- [ ] Documentação atualizada
- [ ] README atualizado se necessário
- [ ] CHANGELOG atualizado

## 🧪 Como Testar
1. Passo 1
2. Passo 2
3. Passo 3

## 📸 Screenshots
Se aplicável, adicione screenshots.

## 🔗 Issues Relacionadas
Closes #123
```

## 📋 Checklist de Contribuição

### **Antes de Enviar**
- [ ] Código compila sem erros
- [ ] Todos os testes passam
- [ ] Cobertura de testes mantida
- [ ] Código segue padrões estabelecidos
- [ ] Documentação atualizada
- [ ] Commits com mensagens claras
- [ ] Branch atualizada com main

### **Durante o Review**
- [ ] Responder a comentários
- [ ] Fazer ajustes solicitados
- [ ] Manter histórico de commits limpo
- [ ] Testar mudanças localmente

### **Após Aprovação**
- [ ] Merge para main
- [ ] Deletar branch de feature
- [ ] Atualizar CHANGELOG
- [ ] Criar release se necessário

## 🐛 Reportando Bugs

### **Template de Bug Report**
```markdown
## 🐛 Descrição do Bug
Descrição clara e concisa do bug.

## 🔄 Passos para Reproduzir
1. Vá para '...'
2. Clique em '...'
3. Veja o erro

## ✅ Comportamento Esperado
O que deveria acontecer.

## 📸 Screenshots
Se aplicável, adicione screenshots.

## 🖥️ Ambiente
- OS: [e.g. Windows 10]
- Java: [e.g. 21.0.1]
- Maven: [e.g. 3.9.0]

## 📝 Logs
Se aplicável, adicione logs de erro.
```

## 💡 Sugerindo Melhorias

### **Template de Feature Request**
```markdown
## 💡 Descrição da Melhoria
Descrição clara da funcionalidade desejada.

## 🎯 Problema que Resolve
Qual problema esta funcionalidade resolve?

## 💭 Solução Proposta
Como você gostaria que funcionasse?

## 🔄 Alternativas Consideradas
Outras soluções que você considerou?

## 📚 Contexto Adicional
Qualquer outro contexto sobre a solicitação.
```

## 📞 Suporte

- **Issues**: Use o sistema de issues do GitHub
- **Discussões**: Use as discussões do GitHub
- **Email**: [seu-email@exemplo.com]

## 📄 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a mesma licença do projeto.

---

## 🙏 Agradecimentos

Obrigado por contribuir com este projeto! Sua contribuição é muito valorizada.

