
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
