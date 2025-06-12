<p align="center">
  <img src="https://github.com/user-attachments/assets/b85e77d8-59d4-417b-b673-78c5ccf4d8cc" alt="Alt text for your image">
</p>

---

## **Flossx83 E2E Suite: Flow Explanation (2025 Edition)**

**Flossx83** is an enterprise-grade, auditable ISO 8583 transaction processing platform. Designed for **core banking**, **regulatory audits**, and **fintech developers**, it offers full-stack financial transaction simulation, tokenization, fraud scoring, and real-time visibility—all without vendor lock-in.

Inspired by **kernel-grade** software principles and **Level-1 PCI DSS architectures**, this suite provides a **transparent**, **composable**, and **self-hostable** card switch solution for BFSI engineers, security auditors, and compliance professionals.

<p align="center">
  <img src="https://github.com/user-attachments/assets/6da108ba-f38c-470f-9e9a-27d25cec6fee" alt="HLD Flossx83">
</p>

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

## 🎯 Project Goals

- Deliver a **complete ISO 8583 infrastructure** for message routing, testing, audit, and compliance.
- Provide **zero-black-box** observability into all stages of transaction lifecycle.
- Empower developers and auditors to **simulate**, **trace**, and **verify** payment flows E2E.
- **Tokenize and protect** sensitive card data with compliance-first defaults.
- Serve as a **reference-grade BFSI backend**—for QA, integration, and regulatory education.

---

## 🛠️ Key Features

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

## ⚙️ Maintainers

> Maintained by the **FLOSSX83 Core Contributors** – for audit professionals, fintech infra engineers, and open-compliance researchers.

For critical issues, contact: `gracemann365@gmail.com`

---

**Flossx83** — because **auditable finance** shouldn't cost \$500,000 a year.
