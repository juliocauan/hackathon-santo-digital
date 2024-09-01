## Desafio Spring Boot com PostgreSQL

**Descrição:**

Este projeto é uma solução desenvolvida em Spring Boot para um desafio proposto. Utiliza PostgreSQL como banco de dados, JUnit para testes unitários e Jacoco para medir a cobertura de código. A documentação da API RESTful é gerada automaticamente com Swagger.

**Por que PostgreSQL?**

O PostgreSQL foi escolhido por sua curva de aprendizado relativamente suave e por sua comunidade ativa. A versão ALPINE, utilizada neste projeto, é leve e eficiente.

**Como executar:**

**Pré-requisitos:**

* Docker
* Maven
* Java 17

**Subir a API:**

1. **Construir o projeto:**

    ```bash
   mvn clean package -DskipTests
    ```
2. **Iniciar os containers:**

    ```bash
    docker compose -f docker-compose.yaml up -d --build
    ```

**Executar testes e gerar relatório:**

1. **Iniciar os containers de teste:**
    
    ```bash
    docker compose -f "docker-compose-test.yml" up -d --build
    ```

2. **Aguardar a inicialização do banco de testes: Aguarde cerca de 6-8 segundos.**

3. **Gerar o relatório:**

    ```bash
    mvn jacoco:prepare-agent clean package jacoco:report
    ```
    
4. O relatório será gerado em **target/site/jacoco/index.html**.