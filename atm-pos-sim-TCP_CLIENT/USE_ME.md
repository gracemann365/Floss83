

# 🏧 **ATM/POS ISO8583 Simulator GUI**

A professional developer and QA tool to craft, randomize, and send ISO 8583 payment messages over TCP to any card switch engine (e.g., Floss83 Java core).
Perfect for **auditable PCI-grade testing, onboarding, and regression/fuzzing scenarios**.

---

## **📦 Features**

- **User-Friendly GUI** (Tkinter; no CLI skills needed)
- **Mandatory field entry** (MTI, PAN, Amount, etc.)
- **Optional field selection** (enable/disable via checkbox)
- **Randomize values** for major fields (generate valid test data instantly)
- **Edit fields manually** for custom scenarios and negative testing
- **Send ISO8583 message** directly to any server/port (raw TCP, not HTTP)
- **View server responses** in real time
- **Error handling and clear logs** for network/test failures

---

## **🚦 How To Use This Simulator**

### 1. **Setup**

- **Pre-requisite:** Python 3.x (ships with Tkinter), no extra pip modules needed for basic GUI.
- Place `atm_pos_simulator.py` in your project root or `/FakePOS-GUI` folder.
- Start your Java TCP server (or any switch you want to test) and note its host/port.

### 2. **Run the Simulator**

```bash
python atm_pos_simulator.py
```

### 3. **Compose a Message**

- **Mandatory Fields:**
  Enter/verify values for fields like MTI, PAN, Amount, Date, etc.
- **Optional Fields:**
  Check the boxes for extra fields (CVV, Exp Date, etc.) and edit values as needed.

### 4. **Randomize**

- Click **“Randomize”** to auto-fill mandatory fields (PAN, Amount, CVV) with valid random data.

### 5. **Configure Target**

- Set **Host** (default: `localhost`) and **Port** (default: `5000`) to match your switch server.

### 6. **Send Message**

- Click **“Send”** to transmit the built ISO8583 message over TCP.
- **Server Response:** Output from your Java switch will be displayed in the bottom text box.

### 7. **Error Handling**

- Any socket/network error, message build error, or server-side failure will show up as an alert and in the response window.

---

## **👩‍💻 Developer/Auditor Documentation**

### **Purpose**

- Provide a **PCI-style, user-auditable, and repeatable** ISO8583 message simulator for integration, regression, and security testing.
- **No sensitive PAN/CVV stored**; all messages are ephemeral and local.
- **All logic, randomization, and message builds are transparent** and fully visible to testers, auditors, and reviewers.

### **Key Design Points**

- **Drag-and-drop/add/remove fields**: Mirrors real-world POS and ATM edge cases, not just happy path.
- **Randomization**: Enables fuzzing and high-volume regression testing.
- **Direct TCP transmission**: Simulates real payment hardware, not just web APIs.
- **No external dependencies**: Pure Python standard library, portable, easy to review.

### **Audit Checklist**

- [x] Can enter, edit, or randomize PAN, Amount, Date, CVV, etc.
- [x] Optional fields included/excluded on demand, for negative/edge testing.
- [x] All network operations are explicit; nothing is sent or stored without user trigger.
- [x] Works with any ISO8583 TCP server, including Floss83 and 3rd-party switches.
- [x] UI exposes all message fields, user controls everything.

---

## **📝 Example Workflow**

> **Goal:** Test tokenization/audit logging for a “decline” message

1. Open the simulator, enter a valid PAN, randomize Amount.
2. Add optional CVV/Exp Date.
3. Set Host/Port for your switch.
4. Click “Send.”
5. Review server response—look for tokenization logs, masking, audit proof.
6. Adjust fields for failed transaction or negative value.
7. Repeat as needed—export, document, or screenshot for audit trail.

---

## **ASCII Flow Diagram**

```text
+--------------------------+
|   ATM/POS Simulator GUI  |
|--------------------------|
| [MTI] [PAN] [Amount] ... |
| [CVV] [Exp Date] ...     |
| [Randomize] [Send]       |
+--------------------------+
           |
           v    (ISO8583 over TCP)
+-----------------------------+
|   Java TCP Card Switch      |
|   (Floss83, Spring Boot)    |
+-----------------------------+
           |
           v
+-----------------------------+
|     Logs/Audit/Processing   |
+-----------------------------+
```

---

## **🔐 Security & Compliance Notes**

- **No cardholder data at rest:** All field values live in memory only.
- **All randomization is local** and compliant with PCI “test data” norms.
- **No real card numbers should be used in production labs.**
- Tool is for test/dev environments only.

---

**For next-level usage:**

- Extend with message save/load, field-level validators, drag-and-drop UI, batch send, etc.

---
