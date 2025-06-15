<p align="center">
  <img src="https://github.com/user-attachments/assets/1085e88e-267e-4ef9-b302-6224a76c4566" alt="Alt text for your image">
</p>


---

## **Flossx83 E2E Suite: Flow Explanation (2025 Edition)**

**Flossx83** is ISO 8583 Simulation Tool For Indie Devs its a  processing platform. Designed for **core banking**, **regulatory audits**, and **fintech developers**, it offers full-stack financial transaction simulation, tokenization, fraud scoring, and real-time visibility—all without vendor lock-in.

Inspired by **kernel-grade** software principles and **Level-1 PCI DSS architectures**, this suite provides a **transparent**, **composable**, and **self-hostable** card switch solution for BFSI engineers, security auditors, and compliance professionals.

<p align="center">
  <img src="https://github.com/user-attachments/assets/6da108ba-f38c-470f-9e9a-27d25cec6fee" alt="HLD Flossx83">
</p>

### 📚 Flossx83 Index


| Section                                                                     | Description                               |
| --------------------------------------------------------------------------- | ----------------------------------------- |
| 🏦 [Flossx83 Overview](#flossx83-e2e-suite-flow-explanation-2025-edition)   | What Flossx83 is and why it exists        |
| 🖼️ [System Diagrams](#flossx83-e2e-suite-flow-explanation-2025-edition)    | Visual architecture and system flow       |
| 🖥️ [POS Terminal Simulator](#flossx83-pos-terminal-simulator-2025-edition) | GUI tool for ISO 8583 message creation    |
| ✨ [At a Glance](#-flossx83-at-a-glance)                                     | One-line summary of each core component   |
| 🚀 [Project Roadmap](#project-roadmap)                             | Milestones, dates, and live status  🚀Where Do We Stand ?🚀      |
| 🎯 [Project Goals](#-project-goals)                                         | Objectives, philosophy, and scope         |
| 🛠️ [Key Features](#key-features)                                           | Deep dive into each component             |
| 🔐 [PCI & Security Practices](#-security--pci-principles)                   | Compliance and tokenization details       |
| 📦 [Getting Started](#-getting-started)                                     | Step-by-step local setup guide            |
| 🧪 [Testing Philosophy](#-test-philosophy)                                  | Design for auditors, fuzzers, regressions |
| 📜 [Legal Notice](#-legal-notice)                                           | Disclaimer and usage terms                |
| 🧭 [Roadmap](#-roadmap)                                                     | What’s done, what’s coming                |
| 🤝 [Contributing](#-contributing)                                           | How to participate                        |
| 📚 [Full Docs](#-documentation)                                             | Wiki, API, compliance                     |
| ⚙️ [Maintainers](#maintainers)                                             | Core team and contact                     |


---
## **Flossx83 POS Terminal Simulator (2025 Edition)**
<p align="center">
  <img src="https://github.com/user-attachments/assets/373e0236-4ff8-437f-8dc5-984a90e2690a" alt="HLD Flossx83">
</p>


---

## ✨ Flossx83 At a Glance

| Component               | Description                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| 🔄 **Card Switch Engine**  | ISO 8583 TCP/HTTP engine (Spring Boot), tokenizes & routes messages        |
| 🖥️ **POS/ATM Simulator**  | PyQt5 GUI to craft and send ISO 8583 messages with drag-and-drop interface |
| 🚨 **Fraud Detection**     | Python FastAPI microservice for real-time rule/ML scoring                  |
| 📊 **Admin Dashboard**     | React-based interface for real-time tracing, logs, and drilldowns          |
| 🔐 **HSM Tokenization**    | AES256-based PCI-safe tokenization module (Java crypto)                    |
| 🧾 **Audit Logger**        | Immutable JSON logs for every event (ingress, parse, fraud, persist)       |

---


---

## Project Roadmap

| # | Milestone (Q2 2025)                                | Target Date     | Status         | Description                                                                             |
| - | ---------------------------------------- | --------------- | -------------- | --------------------------------------------------------------------------------------- |
| 1 | **Audit Logger**                         | Mid June 2025   | 🚧 In Progress | Immutable, append-only event logging (JSONL & text). Core & fraud engine integration.   |
| 2 | **ML-Infused Fraud Engine**              | Late June 2025  | 🟢 Up Next     | Plug-in machine learning & rule-based fraud scoring for ISO8583 messages.               |
| 3 | **Stable Release v1.0**                  | Early July 2025 | 🟡 On Deck     | Complete, production-ready switch: core engine, fraud, logging, and admin modules.      |
| 4 | **TestOps: End-to-End Automation**       | Early July 2025 | 🟡 On Deck     | Automated E2E transaction, fraud, and audit test suite (CI-ready).                      |
| 5 | **DevOps Integration (CI/CD)**           | Early July 2025 | 🟡 On Deck     | Streamlined pipelines for build, test, release, and deploy.                             |
| 6 | **Documentation Suite**                  | Mid July 2025   | 🟢 Lining Up   | Enterprise-grade API docs, user guides, architecture diagrams, live usage examples.     |
| 7 | **IaC & Deployment Scripts**             | Mid July 2025   | 🟢 Lining Up   | Docker, Kubernetes, and Terraform scripts for one-click infra on cloud or local.        |
| 8 | **Cloud Testing & SRE/Observability**    | Late July 2025  | 🟢 Lining Up   | Cloud-native test flows, monitoring dashboards, and real-time logging/metrics.          |
| 9 | **Real-World Benchmarking & Perf Tests** | Late July 2025  | 🟢 Lining Up   | Throughput, latency, and reliability benchmarks simulating live card network workloads. |

---

> 🏗️ **This roadmap is dynamic—milestones and statuses are updated as we advance!
> Jump into [Issues](https://github.com/YOUR_REPO/issues) or [Projects](https://github.com/YOUR_REPO/projects) to track live progress or contribute.**

---


## 🎯 Project Goals

- Deliver a **complete ISO 8583 infrastructure** for message routing, testing, audit, and compliance.
- Provide **zero-black-box** observability into all stages of transaction lifecycle.
- Empower developers and auditors to **simulate**, **trace**, and **verify** payment flows E2E.
- **Tokenize and protect** sensitive card data with compliance-first defaults.
- Serve as a **reference-grade BFSI backend**—for QA, integration, and regulatory education.

---

## Key Features

### 🔄 Core Switch Engine
- Parses and routes ISO 8583 messages (MTI + Fxx).
- Supports both **TCP and HTTP** ingress with protocol detection.
- Integrates with:
  - Tokenization service (AES/CBC, HSM-style)
  - Fraud Engine (REST call)
  - Audit logs (immutable JSON)
  - PostgreSQL persistence

### 💳 POS/ATM Simulator (GUI)
- Dual-mode message sender (TCP + HTTP)
- Dynamic form: auto-randomize or enter ISO fields manually
- Supports all common Fxx fields (2, 3, 4, 7, 11, 52, etc.)
- View request/response with breakdown
- Easily replicates regression/fuzzing scenarios

### 🚨 Fraud Engine (Python)
- REST microservice for fraud analysis
- Scoring logic via rule or pluggable ML
- Outputs `fraud`, `suspicious`, `clean`, with metadata
- Returns JSON payloads for routing/audit tagging

### 🧾 Immutable Audit Logger
- Logs every system event as structured JSON
- Append-only, timestamped, traceable
- Easy to grep, visualize, and export

### 📊 Admin Dashboard (Upcoming)
- Web dashboard for:
  - Real-time ISO message tracing
  - Audit log drilldowns
  - Transaction visualizations
  - Searchable logs and fraud scores

---

## 🔐 Security & PCI Principles

| Category             | Practice                                                                 |
|----------------------|--------------------------------------------------------------------------|
| **PAN/CVV**          | Immediately tokenized using AES256 + salt                                |
| **Logs**             | Masked fields only, redact all sensitive content                         |
| **Persistence**      | Tokenized transactions persisted, never raw card data                    |
| **Audit Trail**      | Every component logs structured entries to immutable log file            |
| **Compliance Mode**  | Follows PCI-DSS Level 1 recommendations for dev/test infrastructure      |

---

## 📦 Getting Started

```bash
git clone https://github.com/gracemann365/flossx83.git
cd flossx83
````

### 🔁 Start the Card Switch

```bash
cd java-switch
./mvnw spring-boot:run
```

### 🖥️ Launch the POS Simulator

```bash
cd atm-pos-sim-TCP_CLIENT
python3 v3.py
```

### ⚠️ Optional: Run the Fraud Engine

```bash
cd fraud-engine
uvicorn main:app --reload
```

---

## 🧪 Test Philosophy

* **Regression-fuzz ready**: Randomizable fields
* **Auditor mode**: Full trace per transaction
* **Bank emulation**: Loopback approvals/rejections
* **See Everything**: Logs, traces, and flows fully transparent

---

## 📜 Legal Notice

> This software is distributed **"AS IS"**, for educational, demo, and financial test lab purposes only.
> **Do not** use with real card data or production environments unless you meet full PCI compliance independently.
> All responsibilities for regulatory usage lie solely with the deployer. See [LICENSE](LICENSE) for full legal disclaimer.

---

## 🧭 Roadmap

* [x] Core TCP ISO8583 Engine
* [x] HSM Tokenization
* [x] Audit Logging
* [x] Fraud Engine (REST)
* [x] POS Simulator (GUI)
* [ ] Admin Dashboard (React)
* [ ] Downstream Bank/Network Routing
* [ ] Compliance modules (SOX, PCI templates)

---

## 🤝 Contributing

This project follows a **review-first, audit-by-default** contribution model.
Please open a [Discussion](https://github.com/gracemann365/flossx83/discussions) or [Issue](https://github.com/gracemann365/flossx83/issues) before submitting PRs.

---

## 📚 Documentation

| Resource                | Link                                               |
| ----------------------- | -------------------------------------------------- |
| 📖 Wiki                 | [ISO 8583 Suite Wiki](https://github.com/.../wiki) |
| 🛠️ API Reference       | `api/README.md` (coming soon)                      |
| 🧭 Architecture         | `docs/arch-overview.md`                            |
| 🔐 PCI Compliance Notes | `docs/compliance.md`                               |

---

## Maintainers

> Maintained by the **FLOSSX83 Core Contributors** – for audit professionals, fintech infra engineers, and open-compliance researchers.
1. [David Grace](https://github.com/Gracemann365) - Owner & Chief Engineer
2. [Goutham Rajesh](https://github.com/gouthamrajesh) - Product Manager & Launch Strategy 
3. For critical issues, contact: `gracemann365@gmail.com`

---

**Flossx83** — because **auditable finance** shouldn't cost \$500,000 a year.
