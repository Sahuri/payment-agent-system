# 💳 Payment Agent System — AI-Powered Payment Assistant

A monorepo containing 3 Spring Boot services that together let a large language model query and reason over payment/transaction data through tool calling — instead of writing manual REST endpoints for every use case.

Instead of a human calling `GET /api/payments`, the agent lets an LLM decide *when* and *how* to fetch payment data based on natural language, using OpenRouter as the LLM gateway.

---

## 🎯 Problem This Solves

Traditional payment integrations require a new REST endpoint (and matching client code) for every possible query — "get all transactions," "get transactions by date," "get failed transactions," and so on. Each variation means more boilerplate and more surface area to maintain.

This project explores a different approach: expose payment operations as **tools** an LLM can call directly, and let the model decide which tool(s) to invoke based on a natural-language request. The result is a system where new capabilities can be added by writing one small `@Tool`-annotated method — no new endpoint, no new client integration, no rigid request schema.

It's a small, focused case study in **agentic backend design**: using [Spring AI](https://spring.io/projects/spring-ai) and the [Model Context Protocol](https://modelcontextprotocol.io) to connect an LLM (via [OpenRouter](https://openrouter.ai)) to real backend services in a modular, swappable way.

---

## ✨ Features

- 🧠 **LLM-driven tool calling** — the agent exposes backend operations (e.g. fetching payment transactions) as `@Tool`-annotated functions that a language model can invoke autonomously.
- 🔌 **MCP (Model Context Protocol) integration** — tools are exposed via MCP, making them reusable across any MCP-compatible client, not just this app.
- 🌐 **Model-agnostic via OpenRouter** — swap between dozens of LLMs (free or paid, from providers like Qwen, GLM, Kimi, etc.) with a single config change, no code changes required.
- 🧩 **Built on Spring AI** — leverages Spring's `ChatModel` abstraction, advisors, and observability (Micrometer) out of the box.
- 🔒 **Config-driven, secrets via environment variables** — no API keys hardcoded in source.

---

## 🏗️ Architecture

```
┌─────────────┐      natural language       ┌─────────────────────────┐
│   Client /  │ ──────────────────────────► │   payment-agent :8010    │
│   User      │                              │   (Spring Boot + AI)     │
└─────────────┘                              │                          │
                                              │  ┌────────────────────┐  │
                                              │  │ Spring AI          │  │
                                              │  │ ChatModel          │  │
                                              │  └─────────┬──────────┘  │
                                              │            │ tool call    │
                                              │  ┌─────────▼──────────┐  │
                                              │  │ MCP Client          │  │
                                              │  └─────────┬──────────┘  │
                                              └────────────┼─────────────┘
                                                            │ MCP protocol
                                                 ┌──────────▼───────────┐
                                                 │  payment-mcp :8090    │
                                                 │  MCP Server           │
                                                 │  (exposes @Tool ops)  │
                                                 └──────────┬───────────┘
                                                            │ REST call
                                                 ┌──────────▼───────────┐
                                                 │  payment-api :8080    │
                                                 │  Core payment service │
                                                 └────────────────────────┘

                    ┌────────────────────────────────────┐
                    │      OpenRouter (LLM Gateway)       │
                    │      Qwen / GLM / Kimi / etc.       │
                    └──────────────────┬───────────────────┘
                                       │ chat completions
                                       ▼
                              payment-agent :8010
```

---

## 🛠️ Tech Stack

| Layer            | Technology                          |
|------------------|--------------------------------------|
| Language         | Java                                  |
| Framework        | Spring Boot                           |
| AI Orchestration | Spring AI                             |
| LLM Gateway      | [OpenRouter](https://openrouter.ai)   |
| Agent Protocol   | Model Context Protocol (MCP)          |
| HTTP Client      | Spring `RestClient`                   |
| Observability    | Micrometer Observation                |

---

## 📁 Project Structure

This is a **monorepo** — all 3 services live together in this single repository for easy browsing, but each remains an independently runnable Spring Boot application:

```
payment-agent-system/
├── payment-api/       # Core payment REST service (port 8080)
├── payment-mcp/       # MCP server exposing payment operations as tools (port 8090)
├── payment-agent/     # AI agent connecting LLM ↔ MCP (port 8010)
└── README.md          # You are here
```

---

## 📋 Prerequisites

- Java 17+
- Maven
- An [OpenRouter](https://openrouter.ai) account and API key

---

## ⚙️ Configuration

Set the following in `application.properties` (or override via environment variables):

```properties
server.port=8010
spring.application.name=payment-agent

# OpenRouter (OpenAI-compatible) configuration
spring.ai.openai.api-key=${OPENROUTER_API_KEY}
spring.ai.openai.base-url=https://openrouter.ai/api/v1
spring.ai.openai.chat.options.model=${OPENROUTER_MODEL:openrouter/free}

# MCP
spring.ai.mcp.client.enabled=true
spring.ai.mcp.server.expose-mcp-client-tools=true
```

> ⚠️ **Important:** the base URL must include `/v1` — OpenRouter's OpenAI-compatible endpoint lives at `https://openrouter.ai/api/v1`, not `https://openrouter.ai/api`.

Set your secret via environment variable rather than committing it:

```bash
export OPENROUTER_API_KEY=sk-or-v1-your-key-here
export OPENROUTER_MODEL=z-ai/glm-5.2:free   # or any model slug from openrouter.ai/models
```

---

## 🚀 Running the App

Clone the repo once — it contains all 3 services:

```bash
git clone https://github.com/<your-username>/payment-agent-system.git
cd payment-agent-system
```

The 3 services must be started **in order**, since each one depends on the previous one being available:

1. **`payment-api`** — the core payment REST service (source of truth for transaction data)
2. **`payment-mcp`** — MCP server that exposes payment operations as tools
3. **`payment-agent`** — the AI agent that connects to an LLM via OpenRouter and calls tools through MCP

### 1. Start `payment-api` — port `8080`

```bash
cd payment-api
mvn spring-boot:run
```

Runs on `http://localhost:8080`.

> Open a new terminal (still in the `payment-agent-system` root) for each step below.

### 2. Start `payment-mcp` — port `8090`

```bash
cd payment-mcp
mvn spring-boot:run
```

Runs on `http://localhost:8090`. Make sure its config points to `payment-api` at `http://localhost:8080`, and that `payment-api` is fully up before starting this service.

### 3. Start `payment-agent` — port `8010`

```bash
cd payment-agent
mvn spring-boot:run
```

Runs on `http://localhost:8010`. Make sure its MCP client config points to `payment-mcp` at `http://localhost:8090`.

Once all three are running, the agent is ready to receive requests at `http://localhost:8010`.

| Service         | Port   | Role                                  |
|------------------|--------|----------------------------------------|
| `payment-api`    | `8080` | Core payment REST service              |
| `payment-mcp`    | `8090` | MCP server exposing payment tools      |
| `payment-agent`  | `8010` | AI agent (LLM + tool calling)          |

> 💡 Tip: for local development, run each service in its own terminal tab so you can watch logs independently, or use a process manager (e.g. `tmux`, `docker-compose`) to start them together in the correct order.

---

## 🧰 Available Tools

Tools exposed to the LLM via `@Tool` annotations:

| Tool name                  | Description                                      |
|-----------------------------|---------------------------------------------------|
| `get_payment_information`   | Retrieves a list of all payment transactions with their details from the payment service. |

Example implementation:

```java
@Tool(
    name = "get_payment_information",
    description = "Retrieve a list of all payment transactions with their details"
)
public List<PaymentResponse> getPaymentInfo() {
    return paymentRestClient
        .get()
        .uri("/api/payments/payment/info")
        .retrieve()
        .body(new ParameterizedTypeReference<List<PaymentResponse>>() {});
}
```

> More tools can be added simply by annotating new methods with `@Tool` — Spring AI + MCP automatically expose them to the LLM.

---

## 💬 Example Interaction

```
User: "Show me all payment transactions this month"
Agent: [invokes get_payment_information tool]
Agent: "Here are the transactions: ..."
```

---

## 🗺️ Roadmap

- [ ] Add tools for filtering payments by date range / status
- [ ] Add authentication/authorization layer
- [ ] Add unit & integration tests for tool methods
- [ ] Add streaming responses
- [ ] Dockerize for easier deployment

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

## 🙋 About

Built as a learning project to explore **agentic AI patterns** using Spring AI and the Model Context Protocol — turning traditional REST APIs into tools an LLM can reason about and call autonomously. Structured as a monorepo so the full system — data layer, tool layer, and AI layer — can be reviewed in one place.
