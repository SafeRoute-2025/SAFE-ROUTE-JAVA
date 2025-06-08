# 📍 SafeRoute - Java Spring Boot Application

Sistema completo para **gestão de emergências e riscos** em ambientes urbanos, construído com **Spring Boot** e integrado com IA, mensageria assíncrona e arquitetura RESTful.  
O sistema permite o cadastro de usuários, eventos, alertas, locais seguros e seus recursos, além de fornecer uma interface web para visualização e gerenciamento dos dados.  

## Futuras Implementações
- Integração com APIs de geolocalização para mapear eventos e locais seguros
- Notificações em tempo real via WebSocket
- Dashboard de monitoramento de eventos e alertas
- Geração de rotas seguras com base em eventos, alertas, locais seguros e localização do usuário.

---

## 📦 Tecnologias Utilizadas
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security (com OAuth2 Google)
- Thymeleaf (para renderização web)
- RabbitMQ (mensageria)
- Spring AI + Ollama (`tinyllama`) 🌟
- Docker & Docker Compose
- Oracle Database
- Swagger/OpenAPI 3
- JUnit 5 & Mockito (testes)

---

## 🚀 Como Executar o Projeto

### 🔧 Rodando Localmente

**Pré-requisitos:**
- Java 17
- Docker

**Passos:**
```bash
# 1. Clonar o repositório
https://github.com/SafeRoute-2025/SAFE-ROUTE-JAVA
# 2. Navegar até a pasta do projeto
cd SAFE-ROUTE-JAVA
# 3. Crie um arquivo `.env` na raiz do projeto seguindo o modelo do `.env.example` e preencha com suas credenciais do Oracle e google
# 4. Inicie o Docker e execute o comando para construir e iniciar os containers
docker compose up --build
# 5. Entre no terminal do container quando estiver rodando e execute:
docker exec -it ollama ollama pull tinyllama
# 6. Acesse a aplicação em http://localhost:8080
```

## 🔐 Autenticação
- Login com Google OAuth2
- Login com usuário registrado (banco Oracle)

---

## 📘 Endpoints REST (Principais)

### 🔹 Usuários `/api/users`
- `GET /api/users` → lista paginada
- `GET /api/users/{id}` → busca por ID
- `POST /api/users` → cria usuário
- `PUT /api/users/{id}` → atualiza
- `DELETE /api/users/{id}` → exclui

### 🔹 Eventos `/api/events`
- `GET /api/events` → lista paginada
- `GET /api/events/{id}`
- `POST /api/events`
- `PUT /api/events/{id}`
- `DELETE /api/events/{id}`
- `GET /api/events/names` → nomes simples (string formatada)
- `GET /api/events/list` → lista flat (sem paginação)

### 🔹 Alertas `/api/alerts`
- `GET /api/alerts` → todos os alertas
- `GET /api/alerts/{id}` → por ID
- `GET /api/alerts/page` → paginado
- `POST /api/alerts` → criar
- `PUT /api/alerts/{id}` → atualizar
- `DELETE /api/alerts/{id}` → remover
- `DELETE /api/alerts/older-than-7-days` → excluir antigos

### 🔹 Locais Seguros `/api/safe-places`
- `GET /api/safe-places`
- `GET /api/safe-places/{id}`
- `POST /api/safe-places`
- `PUT /api/safe-places/{id}`
- `DELETE /api/safe-places/{id}`

### 🔹 Recursos `/api/resources`
- `GET /api/resources`
- `GET /api/resources/{id}`
- `POST /api/resources`
- `PUT /api/resources/{id}`
- `DELETE /api/resources/{id}`

### 🔹 Dica de IA `/api/dica`
- `GET /api/dica` → retorna dica de segurança gerada com IA

---

## 🧠 Inteligência Artificial
- Usa **Spring AI** com modelo local **Ollama - TinyLlama**
- Geração de dicas de segurança em `/api/dica`

---

## ✉️ Mensageria
- RabbitMQ envia/consome mensagens de alertas e eventos
- Fila assíncrona para comunicação entre serviços

---

## 🌐 Interface Web (Thymeleaf)
- `/` → Página inicial
- `/events` → Eventos (listagem + modal)
- `/alerts` → Alertas (com botão excluir antigos)
- `/safe-places` → Locais Seguros
- `/resources` → Recursos disponíveis
- `/users` → Lista e edição de usuários

---

## ✅ Testes
- Testes unitários: `UserServiceTest`, `EventServiceTest`, `AlertServiceTest`
- Testes REST Controller: `UserControllerTest`, `EventControllerTest`, `AlertControllerTest`
- Execução com:
```bash
./mvnw test
```

---

## 📂 Estrutura de Pastas (src/main/java)
```
com.fiap.safe_route
├── config
├── controller
│   ├── api
│   └── web
├── dto
├── exception
├── model
├── repository
├── security
├── service
│   └── messaging
```

---
## Link dos Videos
[Vídeo de Demonstração](https://youtu.be/OTyvS78hBKQ)  

## Pitch
O documento do pitch está disponível na pasta `docs` do repositório.
---
## 👥 Autores
- Mauricio Pereira - RM553748 - [GitHub](https://github.com/Mauricio-Pereira)
- Luiz Otávio Leitão Silva - RM553542 - [GitHub](https://github.com/Luiz1614)
- Vitor de Melo - RM553483 - [GitHub](https://github.com/vitor52a1)

---

## 📄 Licença
Projeto acadêmico - FIAP Global Solution 2025
