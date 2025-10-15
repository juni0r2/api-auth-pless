# 🐳 Dockerfile para Demo Application

# Multi-stage build para otimização
FROM maven:3.9.6-openjdk-21-slim AS builder

# 📁 Definir diretório de trabalho
WORKDIR /app

# 📦 Copiar arquivos de dependências
COPY pom.xml .
COPY src ./src

# 🔧 Instalar dependências e compilar
RUN mvn clean package -DskipTests

# 🚀 Imagem final
FROM openjdk:21-jre-slim

# 👤 Criar usuário não-root para segurança
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 📁 Definir diretório de trabalho
WORKDIR /app

# 📦 Copiar JAR da aplicação
COPY --from=builder /app/target/demo-*.jar app.jar

# 👤 Mudar para usuário não-root
USER appuser

# 🌐 Expor porta
EXPOSE 8080

# 🏥 Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 🚀 Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]

# 📝 Labels para metadados
LABEL maintainer="seu-email@exemplo.com"
LABEL version="1.0.0"
LABEL description="Sistema de Registro de Usuários"
LABEL org.opencontainers.image.title="Demo Application"
LABEL org.opencontainers.image.description="Sistema REST API para gerenciamento de registros"
LABEL org.opencontainers.image.version="1.0.0"
LABEL org.opencontainers.image.authors="Seu Nome <seu-email@exemplo.com>"

