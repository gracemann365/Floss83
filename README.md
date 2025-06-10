
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
+------------------+         +--------------------------+        +----------------+
|  Card Machine /  |         |      Floss83 Card        |        |   Fraud Check  |
|     POS / ATM    |-------> |     Switch Engine        |<-----> |   Service      |
|  (Sends ISO 8583 |   (a)   |  (Parses, routes, logs,  |   (c)  |  (Python micro-|
|   payment msg)   |         |  audits card messages)   |        |    service)    |
+------------------+         +--------------------------+        +----------------+
        |                            |      ^      |
        |         (b)                |      |      |
        +--------------------------->|      |      |
                                      |      |      |
                                      |      |      |
                              +---------------------+
                              |  Simulated Bank/Card|
                              |  Network Endpoint   |
                              | (Approves/Declines) |
                              +---------------------+
```
LEGEND:
(a) Card machine or ATM sends a card payment message (ISO 8583) to the Switch.
(b) Floss83 parses the message, tokenizes card data, checks for fraud, logs/audits it.
(c) Switch consults a Python fraud engine for risk rules.
(d) Switch forwards the message to a simulated bank/card network for approval/decline.

What This Proves:
- **This is a “mini Visa/MasterCard in a box” for developer learning/testing.**
- Handles **real card message formats (ISO 8583)**.
- Audits, logs, and routes transactions for **security, compliance, and demo value**.
- **Open source:** Anyone can inspect/extend how real payment rails work.


Upcoming Connection Handler 
Audit Trails 
and More 


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

### 🤖 **ASCII For Dummies: “What The F*** Is Happening Here !?”**

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
