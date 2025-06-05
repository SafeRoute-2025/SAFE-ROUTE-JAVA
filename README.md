# SAFE ROUTE JAVA

Este projeto é um serviço backend em Java, utilizando Spring Boot, que fornece dicas rápidas de segurança para situações de desastres naturais, integrando-se com modelos de linguagem via Ollama.

## Funcionalidades

- Geração de dicas de segurança em português e inglês.
- Integração com modelos de linguagem (LLM) usando LangChain4j e Ollama.
- API pronta para ser consumida por aplicações web ou mobile.

## Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Maven
- LangChain4j
- Ollama

## Como Executar

1. Clone o repositório:
   ```
git clone https://github.com/SafeRoute-2025/SAFE-ROUTE-JAVA.git
   ```
2. Configure a URL do Ollama no arquivo `application.properties`:
   ```
spring.ai.ollama.base-url=http://localhost:11434
   ```
3. Compile e execute o projeto:
   ```
mvn spring-boot:run
   ```

## Exemplo de Uso

O serviço expõe um endpoint (a ser implementado) que retorna uma dica de segurança baseada no idioma informado.

## Estrutura do Projeto

- `src/main/java/com/fiap/safe_route/service/AiService.java`: Serviço responsável pela integração com o modelo de linguagem.

## Contribuição

Sinta-se à vontade para abrir issues ou pull requests.

## Licença

Este projeto está sob a licença MIT.