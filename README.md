<p align="center">
  <img src="https://github.com/user-attachments/assets/b85e77d8-59d4-417b-b673-78c5ccf4d8cc" alt="Alt text for your image">
</p>

---
## **Flossx83 E2E Suite: Flow Explanation (2025 Edition)**

```
                        +---------------------------+
                        |  (1) POS/ATM Simulator    |<----------------------+
                        |   [GUI Client]            |                       |
                        | - Drag & drop ISO fields  |                       |
                        | - Dual mode: TCP/HTTP     |                       |
                        | - Randomize/test flows    |                       |
                        +------------+--------------+                       |
                                     | (a) ISO 8583 Payment Msg (TCP/HTTP)  |
                                     v                                      |
                   +----------------------------+            +-----------------------------+
                   |   Floss83 Card Switch      |<-----------|      (2) Admin Dashboard    |
                   |        Engine (SpringBoot) |            |       [GUI, Login, Docs]    |
                   | - Parses & routes ISO      |            | - Visualize flows/logs      |
                   | - Tokenizes PAN/CVV        |            | - Drilldown/ISO docs        |
                   | - PCI audit logs           |            | - View DB (transactions)    |
                   | - Calls Fraud Engine (REST)|            +-----------------------------+
                   | - Persists to Postgres     |
                   +-----+-------------+--------+
                         |             |
         (b) Fraud Check |             | (c) ISO8583 to Bank/Network (Sim)
             (REST)      |             v
                         |     +------------------------------+
         +---------------+     | Simulated Bank/Card Network  |
         |                     |   (Approves/Declines Txn)    |
+---------------------+        +------------------------------+
| Fraud Check Engine  |
|   (FastAPI, Python) |
| - ML/Rule scoring   |
| - Standalone        |
+---------------------+

Legend:

[1] POS/ATM Simulator: GUI tool (customize, send, and see responses; TCP/HTTP switchable)
[2] Admin Dashboard: Login, visualize transactions, see ISO docs, audit logs, real-time updates
    Main Engine: Parses, tokenizes, logs, routes, persists, calls fraud, exposes APIs for tools
     Everything is pluggable, see-through, and auditable for QA, audit, and learning


_+ POS/ATM Simulator [GUI]: Dual-mode, randomization, and full ISO field control—massively improved dev UX.

_+ Admin Dashboard: Graphical, with search, drilldown, and real-time visualization—makes audit/training accessible.

_+ Fraud Engine: Clean, modular REST microservice (pluggable for ML/rule engines).

_+ Core Switch: Still the heart—now positioned as part of a suite, not a lonely engine.

_+ Database Layer: Persistence in Postgres for queries, history, and regression/audit trails.

_+ End-to-End Visibility: Every flow is “seeable” and interactive for users, not just a backend protocol pipe.

```
---



### 1. **(a) POS/ATM Simulator (GUI or Real Device) sends ISO 8583 Message (TCP or HTTP)**

* *Can be a physical terminal, a Python/Java FakePOS, or your drag-and-drop GUI Simulator.*
* **Dual Mode:**

  * Sends payment or reversal requests as raw ISO 8583 over **TCP** (realistic)
  * Or as **HTTP** (for developer/test automation).

---

### 2. **Floss83 Card Switch Engine (Spring Boot / Java) receives the message**

* **Parses** full ISO 8583 structure (MTI, all fields).
* **Tokenizes PAN/CVV immediately** with TokenizationService;
  **no raw PAN/CVV ever appears in logs or leaves this layer.**
* **PCI audit logs** capture every transformation and access, but always masked.
* **Transaction is persisted to Postgres** for traceability, regression, and audit history.

---

### 3. **Fraud Check Engine (Python FastAPI Microservice)**

* **Switch engine makes a REST call** (tokenized payload) to the fraud engine.
* **Fraud engine scores**: rule/ML logic returns risk score, “fraud”, or “clean.”
* **Fraud results are logged** and attached to the transaction.

---

### 4. **Admin Dashboard & Real-time Flow Visualization**

* **Web-based dashboard**:

  * Admin login, view/search all ISO 8583 messages
  * Drilldown on every field with human-friendly ISO docs
  * See audit logs, risk flags, and tokens per transaction
  * Real-time graphical flow: watch live traffic and “packet trace” animations of end-to-end journey
* **Side-by-side or multi-window UX:**

  * Launch POS simulator and dashboard together for “see everything” mode

---

### 5. **Downstream Bank/Card Network (Simulated Endpoint)**

* **Switch forwards approved ISO 8583 messages** to a real or dummy downstream (bank/network simulator, test harness, or sandbox).
* **Simulated bank returns approve/decline,** for a true E2E roundtrip (all fully logged).

---

### **How each part connects:**

* **POS/ATM Simulator (GUI or script) → Card Switch Engine (TCP/HTTP) → Fraud Check Engine (REST) → Dashboard (Web) → Bank/Network Simulator**
* **No clear cardholder data leaves the switch at any point—only tokens.**
* **Every step (parse, tokenize, fraud, route, audit) is logged, visualized, and queryable.**

---

## **How Modern Fintech Infra Works (FLOSS83 Edition):**

* **Legacy rails (ISO8583 over TCP/HTTP) for full realism.**
* **New school fraud/risk scoring via Python microservice (REST, ML ready).**
* **Full persistence, E2E traceability, and zero-trust PCI masking.**
* **Everything wrapped in GUIs, dashboards, and visual tools for real adoption, onboarding, and education.**

---

**No black boxes. No more “WTF is happening.”
Just plug, play, see, and trust every message flow—*the modern way*.**

---






Update 10th June 2025 : How do I make sure Your PAN CVV doesnt get stolen by some dude in hoodie 

---

## 🚧 Branch 4: Tokenization & HSM Simulator (core/tokenization-hsm-sim) 🚧

> _“If you can’t explain it to your grandma or a summer intern, you don’t deserve to run prod.”_

### 🧩 **What’s New?**

- Plugged a PCI-style tokenization and ‘fake ass vault’ layer (HsmSimulator + TokenizationService) into the core JavaSwitch
- All PAN and CVV data now **locked up** with strong AES encryption before anything touches it downstream
- Every operation is **audit-logged** (timestamp, event type, input/output masked, status)
- Tampered tokens get you a loud FAIL in the logs (hacker? back to school, bitch)
- _No DB, no cloud, no prod secrets—dev only, but ready for a real HSM drop-in when it matters_

---

### 🤖 ASCII For Dummies: “What The F*** Is Happening Here !?”

```text
         +-------------------------------+
         |         Network Layer         |
         |   (TCP Handler, ISO Inbound)  |
         +---------------+---------------+
                         |
                         v
         +---------------+---------------+
         |    TokenizationService.java   |
         |   - Receives raw PAN/CVV      |
         |   - Validates, calls HSM      |
         |   - Only hands out tokens     |
         +---------------+---------------+
                         |
                         v
         +-------------------------------+
         |      HsmSimulator.java        |
         |   - Simulates a bank HSM      |
         |   - AES/CBC encryption        |
         |   - In-memory key (dev only)  |
         +---------------+---------------+
                         |
                         v
              (No DB or persistence)
                         |
                         v
         +-------------------------------+
         |      Downstream Switch/Flow   |
         |    (Sees only tokens)         |
         +-------------------------------+

**Log output on every call (PCI-style):**
    [AUDIT] TOKENIZE_PAN  | 2025-06-10T... | IN: ***5454 | OUT: *** | SUCCESS
    [AUDIT] DETOKENIZE_PAN| ...            | IN: ***    | OUT: ***5454 | SUCCESS
    [AUDIT] ...           | ...            | ...        | ...   | FAIL: reason

```
Update 9th june 2025 : How The Connection Works Incoming Raw ISO8583

```
                        +----------------------------+
                        |     Floss83 JavaSwitch     |
                        |   ISO 8583 Engine (core)   |
                        +----------------------------+
                                   /        \
                                  /          \
                   [TCP Door]   /              \   [HTTP Door]
                      |        /                \      |
                      v       v                  v     v
          +--------------------+        +-----------------------+
          |   TcpServer.java   |        | IsoMessageController  |
          | (port 5000)        |        | (@RestController)     |
          +--------------------+        +-----------------------+
                  |                               |
                  |   hands off raw ISO 8583 msg   |
                  |-------------------------------+
                  |
                  v
         +---------------------+
         |  Iso8583Parser.java |
         +---------------------+
                  |
      parses, logs, validates, returns
                  |
           +--------------+
           |  Console Log |
           |  & ACK/ERR   |
           +--------------+
                  ^
                  |
+-----------------+-------------------+
|                                     |
|         Test Clients                |
|                                     |
|  +---------------------+            |
|  | ConnectionTest      |            |
|  | TcpClient.py        |            |
|  +---------------------+            |
|        |                            |
|   TCP connect:5000                  |
|        v                            |
|    TcpServer.java                   |
|                                     |
|  +-------------------------+        |
|  | ConnectionTestHttpsClient.py |   |
|  +-------------------------+        |
|        |                            |
|   HTTP POST :8080/api/iso8583       |
|        v                            |
|   IsoMessageController.java          |
+-------------------------------------+


```
