
```text
++----------------------------------------------------------------++
++----------------------------------------------------------------++
||                                                                ||
||                                                                ||
||   ███████╗██╗      ██████╗ ███████╗███████╗ █████╗ ██████╗     ||
||   ██╔════╝██║     ██╔═══██╗██╔════╝██╔════╝██╔══██╗╚════██╗    ||
||   █████╗  ██║     ██║   ██║███████╗███████╗╚█████╔╝ █████╔╝    ||
||   ██╔══╝  ██║     ██║   ██║╚════██║╚════██║██╔══██╗ ╚═══██╗    ||
||   ██║     ███████╗╚██████╔╝███████║███████║╚█████╔╝██████╔╝    ||
||   ╚═╝     ╚══════╝ ╚═════╝ ╚══════╝╚══════╝ ╚════╝ ╚═════╝     ||
||                                                                ||
||                                                                ||
++----------------------------------------------------------------++
++----------------------------------------------------------------++

```

Open Source ISO 8583 Card Switch for Modern Fintech Auditing &amp; GTM Testing

- [Floss83](https://github.com/floss83)


```text
      (a) ISO 8583 Payment Msg (TCP)
+------------------+         +--------------------------+       (c) Fraud Check (REST/HTTP)
|  Card Machine /  | -------> |   Floss83 Card Switch    | <------------------------+        
|     POS / ATM    |         |      Engine (Java)       |                          |
|  (Sends ISO 8583 |         | - Parses & routes        |                          |
|   payment msg)   |         | - Tokenizes PAN/CVV      |                          |
+------------------+         | - PCI audit logs         |                          |
                             | - Calls fraud check      |       +------------------+
         ^                   | - Sends to downstream    |       |    Fraud Check   |
         |                   +--------------------------+       |     Service      |
         |                            |       ^                | (Python micro-   |
         |       (b) Downstream ISO   |       |                |   service, ML)   |
         +--------------------------->|       |                +------------------+
                                      |       |
                                      v       |
                          +-----------------------------+
                          |  Simulated Bank/Card Network|
                          |     Endpoint (ISO 8583)     |
                          | (Approves/Declines Txn)     |
                          +-----------------------------+
```

---

## **Flow Explanation:**

1. **(a) Card Machine/POS/ATM sends an ISO 8583 message over TCP**

   * Could be a real POS or a FakePOS script (Python/Java) sending a payment request.

2. **Floss83 Card Switch Engine (Java) receives the message via TCP**

   * **Parses the ISO 8583** message (gets PAN, CVV, amount, etc.)
   * **Tokenizes PAN/CVV immediately** (calls TokenizationService, logs every op)
   * **PCI audit logs**—no clear PAN/CVV ever in logs or output

3. **(c) Fraud Check (Python microservice)**

   * **Java engine calls out via REST/HTTP (or gRPC)** to the fraud service
   * Sends tokenized fields and other data for risk scoring/fraud analysis
   * Fraud engine responds: “fraud”/“no fraud” or a risk score

4. **(b) Downstream routing**

   * If transaction is clean, Java switch **forwards (maybe re-maps) the ISO 8583** to the next hop (bank, network, simulator, etc.)
   * If declined or flagged, logs/audits and may reject the transaction

5. **Simulated Bank/Card Network Endpoint**

   * Could be a dummy server or stub that “approves” or “declines” (for full E2E test/dev)

---

### **How each part connects:**

* **TCP client (POS) → Java TCP server (Switch) → Python REST Fraud → (optional) Downstream Bank**
* **No sensitive data leaks outside the Switch.**
* **Logs are always PCI-safe, tokens only.**

---
## modern fintech infra works:
**Old school rails (TCP/ISO), new school fraud (Python/REST), and audit-grade everything.**




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
