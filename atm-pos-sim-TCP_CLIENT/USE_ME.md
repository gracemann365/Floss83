# 🚀 Flossx83 Enterprise ATM/POS ISO 8583 Simulator

**Full-Field Edition** • Modern PCI-Grade Fintech Testing Tool

---

## 🟢 **How to Use This Simulator**

_(In 3 easy steps!)_

---

### 1. **Launch the Simulator**

- Install [Python 3.x](https://www.python.org/) and run:

  ```bash
  python v3.py
  ```

- _(Requires PyQt5: `pip install pyqt5 requests`)_

### 2. **Craft & Preview Your ISO 8583 Message**

- **Enter field values**: Use the GUI to edit any of the 64 ISO 8583 fields.
- **Toggle mandatory/optional fields**: Instantly add, remove, or search for any field.
- **Randomize**: One-click test data generation for major fields.
- **Live wire preview**: See the message hex + per-field breakdown in real time.
- **Switch between Light/Dark mode**: True banking UI vibe.

### 3. **Send, Log, and Analyze**

- **Set TCP or HTTP target** (host/port or HTTP endpoint).
- **Send message**: Press “Send TCP” or “Send HTTP”—the message is delivered in raw ISO8583 or as HTTP POST.
- **Audit logs**: All requests/responses are logged in the GUI—exportable and clear for compliance/testing.
- **Save/Load templates**: Work faster, document tests, and repeat easily.

---

## ✨ **Why Use Flossx83 Simulator?**

- **Designed for real fintech QA/devs**—not just hobby use.
- **Instant message composition**: No code, no scripts—just fill fields and fire!
- **100% PCI-style safety**: No PAN/CVV leaks; all tokenization, masking, and logs ready for audit trails.
- **Perfect for onboarding, regression, fuzz, and negative testing** of ANY card switch or ISO8583 core.
- **Enterprise features**: Custom branding, animated UI, per-field coloring, and deep validation.

---

## 🎬 **Quick Demo Workflow**

1. **Fill in fields**: MTI, PAN, Amount, CVV… or use “Randomise.”
2. **Select destination**: TCP (Java card switch) or HTTP (Spring Boot endpoint).
3. **Send!**

   - Check logs for parsed server response, masking, and PCI audit style.
   - Switch to dark mode for _banker_ vibes.
   - Save your template to repeat tomorrow.

---

## 🖼️ **Branded Experience**

- **Custom logo** and window icon
- Optional: **Animated GIF** for card swipe, beep, or EMV insert on send (see docs)
- Top-bar: “Flossx83 ATM/POS Simulator” for pro presentation

---

## ⚡️ **Power Features**

- 64 ISO fields (mandatory/optional, searchable)
- Instant field validation and error highlighting
- Save/load templates as JSON
- All logs, all flows, **auditable** and exportable

---

## 🛡️ **Security & Best Practice**

- **No real PAN/CVV stored** or sent unless you want (always use test data!)
- Messages are _not_ kept on disk unless you export template
- All randomization done locally

---

# **Start Testing Like a Pro.**

**No more Excel. No more CLI. No more excuses.**

---

## 🔗 **Run it now**

```bash
python v3.py
```

_(Requires `pyqt5` and `requests` modules. For animated logo, use `QMovie` and a GIF in your project folder.)_
