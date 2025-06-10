import tkinter as tk
from tkinter import ttk, messagebox
import socket
import random

MANDATORY_FIELDS = [
    ("MTI", "0200"),
    ("Primary Bitmap", "7238000000000000"),
    ("Field 2 (PAN)", "1234567890123456"),
    ("Field 3 (Processing Code)", "000000"),
    ("Field 4 (Amount)", "000000010000"),
    ("Field 7 (Transmission Date/Time)", "0709163030"),
    ("Field 11 (STAN)", "123456"),
    ("Field 12 (Time)", "163030"),
    ("Field 13 (Date)", "0709"),
]

OPTIONAL_FIELDS = [
    ("Field 52 (CVV)", "123"),
    ("Field 14 (Exp Date)", "2509"),
    ("Field 22 (POS Entry Mode)", "021"),
    # Add more as needed
]

def random_pan():
    return "".join(str(random.randint(0,9)) for _ in range(16))

def random_amount():
    return "{:012d}".format(random.randint(1, 999999999999))

def random_cvv():
    return "{:03d}".format(random.randint(0,999))

class ISOSimulatorApp(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Floss83 ATM/POS ISO8583 Simulator")
        self.geometry("620x500")
        self.resizable(False, False)

        self.field_vars = {}
        self.optional_vars = {}
        self.build_gui()

    def build_gui(self):
        frm = ttk.Frame(self)
        frm.pack(fill="both", expand=True, padx=10, pady=10)

        row = 0
        ttk.Label(frm, text="Mandatory Fields", font=("Segoe UI", 12, "bold")).grid(column=0, row=row, sticky="w")
        row += 1
        for field, default in MANDATORY_FIELDS:
            ttk.Label(frm, text=field).grid(column=0, row=row, sticky="w")
            var = tk.StringVar(value=default)
            entry = ttk.Entry(frm, textvariable=var, width=30)
            entry.grid(column=1, row=row, sticky="w")
            self.field_vars[field] = var
            row += 1

        ttk.Button(frm, text="Randomize", command=self.randomize_fields).grid(column=0, row=row, pady=10)
        row += 1

        ttk.Label(frm, text="Optional Fields", font=("Segoe UI", 12, "bold")).grid(column=0, row=row, sticky="w")
        row += 1
        for field, default in OPTIONAL_FIELDS:
            var = tk.StringVar(value=default)
            check = tk.BooleanVar(value=False)
            cb = ttk.Checkbutton(frm, text=field, variable=check)
            cb.grid(column=0, row=row, sticky="w")
            entry = ttk.Entry(frm, textvariable=var, width=20)
            entry.grid(column=1, row=row, sticky="w")
            self.optional_vars[field] = (check, var)
            row += 1

        ttk.Label(frm, text="Host:").grid(column=0, row=row, sticky="w")
        self.host_var = tk.StringVar(value="localhost")
        ttk.Entry(frm, textvariable=self.host_var, width=16).grid(column=1, row=row, sticky="w")
        row += 1

        ttk.Label(frm, text="Port:").grid(column=0, row=row, sticky="w")
        self.port_var = tk.IntVar(value=5000)
        ttk.Entry(frm, textvariable=self.port_var, width=10).grid(column=1, row=row, sticky="w")
        row += 1

        ttk.Button(frm, text="Send", command=self.send_iso).grid(column=0, row=row, pady=16)
        row += 1

        ttk.Label(frm, text="Server Response:").grid(column=0, row=row, sticky="w")
        row += 1

        self.response_text = tk.Text(frm, width=68, height=8)
        self.response_text.grid(column=0, row=row, columnspan=2, pady=4)

    def randomize_fields(self):
        self.field_vars["Field 2 (PAN)"].set(random_pan())
        self.field_vars["Field 4 (Amount)"].set(random_amount())
        self.optional_vars["Field 52 (CVV)"][1].set(random_cvv())
        # Add more as needed

    def build_iso_message(self):
        vals = []
        # Basic ISO message build (simplified, not binary/packed!)
        vals.append(self.field_vars["MTI"].get())
        vals.append(self.field_vars["Primary Bitmap"].get())
        pan = self.field_vars["Field 2 (PAN)"].get()
        vals.append(f"{len(pan):02d}{pan}")
        for field in [
            "Field 3 (Processing Code)", "Field 4 (Amount)",
            "Field 7 (Transmission Date/Time)", "Field 11 (STAN)",
            "Field 12 (Time)", "Field 13 (Date)"
        ]:
            vals.append(self.field_vars[field].get())

        # Optional fields
        for field, (check, var) in self.optional_vars.items():
            if check.get():
                val = var.get()
                # Simplified: append as is (add real encoding if needed)
                vals.append(val)
        return "".join(vals)

    def send_iso(self):
        msg = self.build_iso_message()
        host = self.host_var.get()
        port = self.port_var.get()
        self.response_text.delete(1.0, tk.END)
        try:
            with socket.create_connection((host, port), timeout=5) as s:
                s.sendall(msg.encode("ascii"))
                s.shutdown(socket.SHUT_WR)
                response = s.recv(4096)
                self.response_text.insert(tk.END, response.decode(errors="ignore"))
        except Exception as e:
            self.response_text.insert(tk.END, f"ERROR: {e}")
            messagebox.showerror("Send Error", str(e))

if __name__ == "__main__":
    app = ISOSimulatorApp()
    app.mainloop()
