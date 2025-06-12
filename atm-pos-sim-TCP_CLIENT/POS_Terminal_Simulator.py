"""
Flossx83 Enterprise ATM/POS ISO 8583 Simulator – Full-Field Edition
===================================================================
*All 64 standard ISO 8583:1987 fields supported, dynamic bitmap, live per-field preview, and polished light/dark modes.*

### Key Add-ons
1. **Complete Field Registry**: 64 fields (1–64) from ISO 8583:1987, auto-split into Mandatory & Optional.
2. **Searchable Optional Tab**: Instant filter by field number or label.
3. **Live Wire & Structured Preview**: Hex wire message plus `Fnn = value` lines.
4. **Dynamic Bitmap**: Primary & secondary bits—secondary only if fields >64 are included.
5. **Randomize Selected Fields**: Field-specific and generic random data generators.
6. **Inline Validation**: Required, type, length checks with red highlights.
7. **Light/Dark Themes**: True palette switching for all UI elements.
8. **Save/Open Templates**: JSON-based import/export of field sets.
9. **Protocol Extensions**: TPDU header & length prefix toggles.
10. **TCP Send & Audit Log**: One-click send, request/response log, status bar feedback.
11. **Color-Coded Fields**: Each field has unique color for easy identification.

Author: Gracemann365 • Flossx83 Team  •  June 2025
"""

import sys
import json
import random
import socket
import pathlib
import requests
from datetime import datetime
from typing import Dict, Callable, List, Tuple

from PyQt5.QtWidgets import (
    QApplication, QWidget, QVBoxLayout, QHBoxLayout, QFormLayout, QLabel, QLineEdit,
    QCheckBox, QPushButton, QTextEdit, QScrollArea, QTabWidget, QMessageBox, QSpinBox,
    QFileDialog, QStatusBar, QMenuBar
)
from PyQt5.QtGui import QPalette, QColor, QPixmap   
from PyQt5.QtCore import Qt, pyqtSignal
from PyQt5.QtGui import QIcon

def generate_field_colors():
    """Generate unique colors for each field number"""
    colors = {}
    for i in range(1, 65):
        hue = (i * 137.5) % 360
        colors[i] = f"hsl({hue:.0f}, 70%, 60%)"
    return colors

FIELD_COLORS = generate_field_colors()

FIELD_REGISTRY: List[Tuple[int, str, str, int, str, bool]] = [
    (1,  "Secondary Bitmap (internal)",               "",          0,  "b", False),
    (2,  "Primary Account Number (PAN)",             "4000001234567899",19,"n", True),
    (3,  "Processing Code",                          "000000",     6,  "n", False),
    (4,  "Transaction Amount",                       "000000010000",12,"n", False),
    (5,  "Settlement Amount",                        "000000010000",12,"n", False),
    (6,  "Cardholder Billing Amount",                "000000010000",12,"n", False),
    (7,  "Transmission Date & Time",                 datetime.now().strftime("%m%d%H%M%S"),10,"n",False),
    (8,  "Billing Fee Amount",                       "00000001",   8,  "n", False),
    (9,  "Settlement Conversion Rate",               "00001000",   8,  "n", False),
    (10, "Billing Conversion Rate",                  "00001000",   8,  "n", False),
    (11, "System Trace Audit Number (STAN)",         "123456",     6,  "n", False),
    (12, "Local Transaction Time",                   datetime.now().strftime("%H%M%S"),6,"n",False),
    (13, "Local Transaction Date",                   datetime.now().strftime("%m%d"),4,"n",False),
    (14, "Expiration Date",                          "2612",       4,  "n", False),
    (15, "Settlement Date",                          "0710",       4,  "n", False),
    (16, "Conversion Date",                          "0710",       4,  "n", False),
    (17, "Capture Date",                             "0710",       4,  "n", False),
    (18, "Merchant Type (MCC)",                      "6011",       4,  "n", False),
    (19, "Acquiring Institution Country Code",       "356",        3,  "n", False),
    (20, "PAN Extended Country Code",                "356",        3,  "n", False),
    (21, "Forwarding Institution Country Code",      "356",        3,  "n", False),
    (22, "Point of Service Entry Mode",              "051",        3,  "n", False),
    (23, "Card Sequence Number",                     "001",        3,  "n", False),
    (24, "Function Code (Network ID)",               "0001",       4,  "n", False),
    (25, "POS Condition Code",                       "00",         2,  "n", False),
    (26, "POS Capture Code",                         "12",         2,  "n", False),
    (27, "Authorization ID Response Length",         "1",          1,  "n", False),
    (28, "Amount, Transaction Fee",                  "000000001",  9,  "xn",False),
    (29, "Amount, Settlement Fee",                   "000000001",  9,  "xn",False),
    (30, "Amount, Transaction Processing Fee",       "000000001",  9,  "xn",False),
    (31, "Amount, Settlement Processing Fee",        "000000001",  9,  "xn",False),
    (32, "Acquiring Institution ID",                 "12345678901",11,"n", False),
    (33, "Forwarding Institution ID",                "12345678901",11,"n", False),
    (34, "PAN Extended",                             "12345678901234567890",20,"z",True),
    (35, "Track 2 Data",                             "4000001234567899=26122010000012345678",37,"z",True),
    (36, "Track 3 Data",                             "1234567890123456789012345678901234567890",104,"z",True),
    (37, "Retrieval Reference Number",               "ABC123456789",12,"an",False),
    (38, "Authorization ID Response",                "A1B2C3",      6,  "an",False),
    (39, "Response Code",                            "00",          2,  "an",False),
    (40, "Service Restriction Code",                "201",        3,  "n", False),
    (41, "Card Acceptor Terminal ID",               "TERM01",      8,  "ans",False),
    (42, "Card Acceptor ID Code",                   "MERCH12345", 15, "ans",False),
    (43, "Card Acceptor Name/Location",              "TEST MERCHANT     BLR     IN",40,"ans",False),
    (44, "Additional Response Data",                "OK",          25, "an",False),
    (45, "Track 1 Data",                             "B4000001234567899^DOE/JOHN^26122010000000000000",76,"ans",True),
    (46, "Additional Data – ISO",               "ISOEXTRA",    999,"an",False),
    (47, "Additional Data – National",          "NATEXTRA",    999,"an",False),
    (48, "Additional Data – Private",           "PRIVATEEXTRA",999,"an",False),
    (49, "Currency Code, Transaction",              "356",         3,  "n", False),
    (50, "Currency Code, Settlement",               "356",         3,  "n", False),
    (51, "Currency Code, Cardholder Billing",       "356",         3,  "n", False),
    (52, "PIN / CVV (16 bytes)",                    "1234567890123456",16,"b",True),
    (53, "Security Related Control Info",           "SECUREINFO",  48, "an",False),
    (54, "Additional Amounts",                      "000000001",   120,"an",False),
    (55, "Reserved ISO (EMV Data)",                "EMVDATA",     255,"b",True),
    (56, "Reserved National",                      "NATIONAL",    255,"an",False),
    (57, "Reserved Private",                       "PRIVATE",     255,"an",False),
    (58, "Authentication Code",                    "ABC123",       6,  "an",False),
    (59, "Response Indicator",                     "RESPIND",     999,"an",False),
    (60, "Payment Information",                    "PAYINFO",     999,"an",False),
    (61, "Reserved for POS",                       "POSDATA",     999,"an",False),
    (62, "Reserved for Network",                   "NETDATA",     999,"an",False),
    (63, "Reserved for Issuer",                    "ISSUERDATA",  999,"an",False),
    (64, "Message Authentication Code (MAC)",       "ABCDEF1234567890",16,"b",True)
]

MANDATORY_FIELDS = FIELD_REGISTRY[:7]
OPTIONAL_FIELDS  = FIELD_REGISTRY[7:64]

rand_num = lambda n: "".join(str(random.randint(0,9)) for _ in range(n))
FIELD_RANDOM: Dict[int, Callable[[], str]] = {
    2: lambda: rand_num(16),
    4: lambda: f"{random.randint(1,999999999999):012d}",
    52: lambda: rand_num(16)
}

class IsoSim(QWidget):
    iso_preview_changed = pyqtSignal(str)

    def __init__(self):
        super().__init__()
        self.setWindowTitle("Flossx83 POS Simulator")
        self.setWindowIcon(QIcon("flossx83_logo.jpg"))
        self.widgets: Dict[int, Tuple[QCheckBox, QLineEdit, int, str, bool]] = {}
        self.dark_mode = False
        self.optional_items = []
        self.optional_form = None
        self.colored_msg_parts = []
        self._build_ui()
        self.apply_theme()
        self.iso_preview_changed.connect(self.update_wire_display)
        self.recompute_preview()

    def _build_ui(self):
        root = QVBoxLayout(self)
        header_layout = QHBoxLayout()
        logo_label = QLabel()
        logo_label.setPixmap(QPixmap("flossx83_logo.jpg").scaled(90, 90, Qt.KeepAspectRatio, Qt.SmoothTransformation))  # You’ll need atm_logo.png
        title_label = QLabel("<b>Flossx83 POS Simulator</b>")
        title_label.setStyleSheet("font-size: 22px; padding-left: 12px; color: #008000;")
        header_layout.addWidget(logo_label)
        header_layout.addWidget(title_label)
        header_layout.addStretch()
        root.addLayout(header_layout)

        menubar = QMenuBar()
        menubar.addAction("Dark Mode", self.toggle_theme)
        menubar.addAction("Save JSON Template", self.save_template)
        menubar.addAction("Load JSON Template", self.load_template)
        root.setMenuBar(menubar)

        tabs = QTabWidget()
        tabs.addTab(self._create_tab(MANDATORY_FIELDS, mandatory=True), "Mandatory")
        tabs.addTab(self._create_tab(OPTIONAL_FIELDS,  mandatory=False), "Optional")

        # HTTP tab for dev testing
        http_tab = QWidget()
        http_layout = QVBoxLayout(http_tab)
        url_layout = QHBoxLayout()
        self.http_url = QLineEdit("http://localhost:8080/api/iso8583")
        url_layout.addWidget(QLabel("URL:"))
        url_layout.addWidget(self.http_url)
        http_layout.addLayout(url_layout)
        send_http_btn = QPushButton("Send HTTP")
        send_http_btn.clicked.connect(self.send_http)
        http_layout.addWidget(send_http_btn, alignment=Qt.AlignLeft)
        self.http_log = QTextEdit()
        self.http_log.setReadOnly(True)
        http_layout.addWidget(QLabel("HTTP Log:"))
        http_layout.addWidget(self.http_log)
        tabs.addTab(http_tab, "HTTP")

        root.addWidget(tabs, stretch=7)

        self.message_wire = QTextEdit()
        self.message_wire.setMaximumHeight(60)
        self.message_wire.setReadOnly(True)
        self.message_structured = QTextEdit()
        self.message_structured.setReadOnly(True)
        self.message_structured.setMaximumHeight(180)
        root.addWidget(QLabel("Live Wire Message (hex):"))
        root.addWidget(self.message_wire)
        root.addWidget(QLabel("Per-Field Breakdown:"))
        root.addWidget(self.message_structured)

        ctrl = QHBoxLayout()
        self.host_in = QLineEdit("localhost")
        self.port_in = QSpinBox(); self.port_in.setRange(1, 65535); self.port_in.setValue(5000)
        self.tpdu_chk = QCheckBox("Include TPDU 6000000000")
        self.len_chk  = QCheckBox("Add 2-byte LEN header")
        rand_btn = QPushButton("Randomise Fields"); rand_btn.clicked.connect(self.randomise)
        send_btn = QPushButton("Send TCP");      send_btn.clicked.connect(self.send)
        for w in [QLabel("Host:"), self.host_in, QLabel("Port:"), self.port_in,
                  self.tpdu_chk, self.len_chk, rand_btn, send_btn]:
            ctrl.addWidget(w)
        root.addLayout(ctrl)

        self.log = QTextEdit(); self.log.setReadOnly(True)
        root.addWidget(self.log, stretch=3)
        self.status = QStatusBar(); root.addWidget(self.status)

    def _create_tab(self, fields, mandatory):
        container = QWidget()
        vlay = QVBoxLayout(container)
        if not mandatory:
            self.search_field = QLineEdit()
            self.search_field.setPlaceholderText("🔍 Search optional fields by number or name...")
            self.search_field.textChanged.connect(self._filter_optional)
            vlay.addWidget(self.search_field)
        scroll = QScrollArea()
        scroll.setWidgetResizable(True)
        vlay.addWidget(scroll)
        content = QWidget()
        form = QFormLayout(content)
        scroll.setWidget(content)
        if not mandatory:
            self.optional_form = form
        for num, label, default, maxlen, dtype, pci in fields:
            if num == 1:
                continue
            title = f"F{num}: {label}{' 🔒' if pci else ''}"
            inp = QLineEdit(default)
            inp.setMaxLength(maxlen)
            inp.textChanged.connect(self.recompute_preview)
            field_color = FIELD_COLORS.get(num, "#cccccc")
            base_style = f"QLineEdit {{ border-left: 4px solid {field_color}; padding-left: 8px; }}"
            inp.setStyleSheet(base_style)
            if dtype == 'n': inp.setPlaceholderText("digits only")
            elif dtype == 'an': inp.setPlaceholderText("alphanumeric")
            elif dtype == 'ans': inp.setPlaceholderText("ASCII printable")
            elif dtype == 'b': inp.setPlaceholderText("hex bytes")
            elif dtype == 'z': inp.setPlaceholderText("track data")
            if not mandatory:
                chk = QCheckBox()
                chk.stateChanged.connect(self.recompute_preview)
                row = QWidget()
                hl = QHBoxLayout(row); hl.setContentsMargins(0,0,0,0)
                hl.addWidget(chk); hl.addWidget(inp)
                self.optional_items.append(((num, label, title), (QLabel(title), row)))
                form.addRow(QLabel(title), row)
            else:
                chk = None
                form.addRow(QLabel(title), inp)
            self.widgets[num] = (chk, inp, maxlen, dtype, pci)
        return container

    def _filter_optional(self, search_text):
        if not hasattr(self, 'optional_items') or not self.optional_form:
            return
        search_text = search_text.lower().strip()
        while self.optional_form.count():
            child = self.optional_form.takeAt(0)
            if child.widget(): child.widget().setParent(None)
        matches, non_matches = [], []
        for field_data, (label_w, row_w) in self.optional_items:
            num, label, title = field_data
            target = f"f{num} {title} {label}".lower()
            if not search_text or search_text in target:
                matches.append((label_w, row_w))
            else:
                non_matches.append((label_w, row_w))
        for label_w, row_w in matches:
            self.optional_form.addRow(label_w, row_w)
            self._highlight_widget(row_w, highlight=bool(search_text), dim=False)
        for label_w, row_w in non_matches:
            self.optional_form.addRow(label_w, row_w)
            self._highlight_widget(row_w, highlight=False, dim=True)

    def _highlight_widget(self, widget, highlight, dim=False):
        if highlight:
            if self.dark_mode:
                widget.setStyleSheet("""
                    QWidget { 
                        background-color: #003300; 
                        border: 2px solid #00ff00; 
                        border-radius: 4px;
                        padding: 2px;
                    }
                    QLineEdit { 
                        background-color: #004400; 
                        border: 1px solid #00ff00; 
                        color: #00ffff;
                    }
                    QCheckBox {
                        color: #00ff00;
                    }
                """)
            else:
                widget.setStyleSheet("""
                    QWidget { 
                        background-color: #e8f5e8; 
                        border: 2px solid #4CAF50; 
                        border-radius: 4px;
                        padding: 2px;
                    }
                    QLineEdit { 
                        background-color: #f0fff0; 
                        border: 2px solid #4CAF50; 
                    }
                """)
        elif dim:
            if self.dark_mode:
                widget.setStyleSheet("""
                    QWidget { 
                        background-color: black; 
                    }
                    QLineEdit { 
                        background-color: #0a0a0a; 
                        color: #555555;
                        border: 1px solid #222;
                    }
                    QCheckBox {
                        color: #555555;
                    }
                """)
            else:
                widget.setStyleSheet("""
                    QWidget { 
                        background-color: #f8f8f8; 
                    }
                    QLineEdit { 
                        background-color: #f0f0f0; 
                        color: #999999;
                        border: 1px solid #ddd;
                    }
                    QCheckBox {
                        color: #999999;
                    }
                """)
        else:
            widget.setStyleSheet("")
    def apply_theme(self):
        pal = QPalette()
        if self.dark_mode:
            bg_black = QColor(0, 0, 0)
            bg_dark = QColor(20, 20, 20)
            text_green = QColor(0, 255, 0)
            text_cyan = QColor(0, 255, 255)
            text_yellow = QColor(255, 255, 0)
            highlight = QColor(0, 100, 0)
            
            pal.setColor(QPalette.Window, bg_black)
            pal.setColor(QPalette.WindowText, text_green)
            pal.setColor(QPalette.Base, bg_dark)
            pal.setColor(QPalette.AlternateBase, bg_black)
            pal.setColor(QPalette.Text, text_cyan)
            pal.setColor(QPalette.Button, bg_black)
            pal.setColor(QPalette.ButtonText, text_yellow)
            pal.setColor(QPalette.BrightText, text_green)
            pal.setColor(QPalette.Link, text_cyan)
            pal.setColor(QPalette.Highlight, highlight)
            pal.setColor(QPalette.HighlightedText, text_green)
            pal.setColor(QPalette.ToolTipBase, bg_black)
            pal.setColor(QPalette.ToolTipText, text_green)
            
            pal.setColor(QPalette.Dark, bg_black)
            pal.setColor(QPalette.Mid, bg_dark)
            pal.setColor(QPalette.Light, QColor(40, 40, 40))
            pal.setColor(QPalette.Midlight, QColor(30, 30, 30))
            pal.setColor(QPalette.Shadow, bg_black)
            
        else:
            pal.setColor(QPalette.Window, QColor(250, 250, 250))
            pal.setColor(QPalette.WindowText, Qt.black)
            pal.setColor(QPalette.Base, QColor(255, 255, 255))
            pal.setColor(QPalette.Text, Qt.black)
            pal.setColor(QPalette.Button, QColor(240, 240, 240))
            pal.setColor(QPalette.ButtonText, Qt.black)
            pal.setColor(QPalette.Highlight, QColor(0, 120, 215))
            pal.setColor(QPalette.HighlightedText, Qt.white)
            pal.setColor(QPalette.AlternateBase, QColor(245, 245, 245))
            
        QApplication.setPalette(pal)
        
        if self.dark_mode:
            self.setStyleSheet("""
                QWidget { 
                    background-color: black; 
                    color: #00ff00; 
                }
                QLineEdit { 
                    background-color: #141414; 
                    border: 1px solid #333; 
                    color: #00ffff; 
                    padding: 3px;
                }
                QTextEdit { 
                    background-color: #141414; 
                    border: 1px solid #333; 
                    color: #00ffff; 
                }
                QTabWidget::pane { 
                    background-color: black; 
                    border: 1px solid #333; 
                }
                QTabBar::tab { 
                    background-color: #141414; 
                    color: #ffff00; 
                    padding: 8px 16px; 
                    border: 1px solid #333; 
                }
                QTabBar::tab:selected { 
                    background-color: #333; 
                    color: #00ff00; 
                }
                QPushButton { 
                    background-color: #141414; 
                    color: #ffff00; 
                    border: 1px solid #555; 
                    padding: 5px 10px; 
                }
                QPushButton:hover { 
                    background-color: #333; 
                }
                QCheckBox { 
                    color: #00ff00; 
                }
                QSpinBox { 
                    background-color: #141414; 
                    color: #00ffff; 
                    border: 1px solid #333; 
                }
                QScrollArea { 
                    background-color: black; 
                }
                QScrollBar:vertical { 
                    background-color: #141414; 
                    border: 1px solid #333; 
                }
                QScrollBar::handle:vertical { 
                    background-color: #555; 
                }
                QMenuBar { 
                    background-color: black; 
                    color: #00ff00; 
                    border-bottom: 1px solid #333; 
                }
                QMenuBar::item { 
                    background-color: transparent; 
                    padding: 4px 8px; 
                }
                QMenuBar::item:selected { 
                    background-color: #333; 
                }
                QStatusBar { 
                    background-color: black; 
                    color: #00ff00; 
                    border-top: 1px solid #333; 
                }
            """)
        else:
            self.setStyleSheet("")


    def toggle_theme(self):
        self.dark_mode = not self.dark_mode
        self.apply_theme()
        if hasattr(self, 'search_field'):
            self._filter_optional(self.search_field.text())

    def save_template(self):
        path, _ = QFileDialog.getSaveFileName(self, "Save Template", "template.json", "JSON (*.json)")
        if not path: return
        data = {num: self.widgets[num][1].text() for num in self.widgets
                if not self.widgets[num][0] or self.widgets[num][0].isChecked()}
        pathlib.Path(path).write_text(json.dumps(data, indent=2))
        self.status.showMessage("Template saved")

    def load_template(self):
        path, _ = QFileDialog.getOpenFileName(self, "Open Template", "", "JSON (*.json)")
        if not path: return
        data = json.loads(pathlib.Path(path).read_text())
        for num_str, val in data.items():
            num = int(num_str)
            if num in self.widgets:
                chk, inp, *_ = self.widgets[num]
                if chk: chk.setChecked(True)
                inp.setText(val)
        self.status.showMessage("Template loaded")
        self.recompute_preview()

    def randomise(self):
        for num, (chk, inp, maxlen, dtype, _) in self.widgets.items():
            if chk and not chk.isChecked(): continue
            if num in FIELD_RANDOM:
                inp.setText(FIELD_RANDOM[num]())
            elif dtype == 'n':
                inp.setText(str(random.randint(0, 10**maxlen - 1)).zfill(maxlen))
        self.status.showMessage("Randomised selected fields")
        self.recompute_preview()

    def validate(self):
        ok = True
        for num, (chk, inp, maxlen, dtype, _) in self.widgets.items():
            if chk and not chk.isChecked():
                inp.setStyleSheet('')
                continue
            val = inp.text().strip()
            bad = not val or len(val) > maxlen or (dtype == 'n' and not val.isdigit())
            inp.setStyleSheet('background:#ffcccc' if bad else '')
            ok &= not bad
        if not ok:
            QMessageBox.warning(self, "Validation Error", "Please fix highlighted fields.")
        return ok

    def build(self):
        selected = [n for n, (chk, _, _, _, _) in self.widgets.items() 
                   if n != 1 and (not chk or chk.isChecked())]
        bits = ['0'] * 64
        for n in selected:
            if 1 <= n <= 64:
                bits[n-1] = '1'
        prim = int(''.join(bits), 2)
        bitmap_hex = f"{prim:016X}"
        msg = "0200" + bitmap_hex
        lines = []
        colored_msg_parts = []
        
        colored_msg_parts.append(("0200", "#888888"))
        colored_msg_parts.append((bitmap_hex, "#aaaaaa"))
        
        for n in selected:
            val = self.widgets[n][1].text().strip()
            field_color = FIELD_COLORS.get(n, "#cccccc")
            
            if n == 2:
                len_prefix = f"{len(val):02d}"
                msg += len_prefix + val
                colored_msg_parts.append((len_prefix, "#666666"))
                colored_msg_parts.append((val, field_color))
            else:
                msg += val
                colored_msg_parts.append((val, field_color))
                
            lines.append(f'<span style="color: {field_color}; font-weight: bold;">F{n} = {val}</span>')
        
        if self.tpdu_chk.isChecked(): 
            msg = "6000000000" + msg
            colored_msg_parts.insert(0, ("6000000000", "#444444"))
        if self.len_chk.isChecked(): 
            len_header = f"{len(msg)//2:04X}"
            msg = len_header + msg
            colored_msg_parts.insert(0, (len_header, "#444444"))
        
        self.message_structured.setHtml("<br>".join(lines))
        self.colored_msg_parts = colored_msg_parts
        return msg

    def update_wire_display(self, msg):
        if hasattr(self, 'colored_msg_parts') and self.colored_msg_parts:
            html_parts = []
            for part, color in self.colored_msg_parts:
                html_parts.append(f'<span style="color: {color}; font-weight: bold; font-family: monospace;">{part}</span>')
            
            html_msg = "".join(html_parts)
            self.message_wire.setHtml(html_msg)
        else:
            self.message_wire.setPlainText(msg)

    def recompute_preview(self):
        msg = self.build()
        self.update_wire_display(msg)


    def send(self):
        if not self.validate(): return
        iso = self.build()
        self.log.append(f"→ {iso}")
        try:
            with socket.create_connection((self.host_in.text(), self.port_in.value()), timeout=5) as s:
                s.sendall(iso.encode())
                s.shutdown(socket.SHUT_RDWR)
                resp = s.recv(4096).decode(errors='ignore')
                self.log.append(f"← {resp}")
                self.status.showMessage("Sent ✔")
        except Exception as e:
            self.log.append(f"ERROR: {e}")
            self.status.showMessage("[TCP] aborted")

    def send_http(self):
        if not self.validate():
            return
        iso = self.build()
        url = self.http_url.text().strip()
        headers = {"Content-Type": "text/plain"}
        self.http_log.append(f"→ POST {url}\n{iso}\n")
        try:
            resp = requests.post(url, data=iso, headers=headers, timeout=5)
            # Try JSON pretty-print
            try:
                resp_json = resp.json()
                pretty = json.dumps(resp_json, indent=2)
                self.http_log.append(f"← {resp.status_code} {resp.reason}\n{pretty}\n")
                # # Optional: update structured per-field breakdown if 'fields' present
                # if "fields" in resp_json:
                #     lines = []
                #     for k, v in resp_json["fields"].items():
                #         lines.append(f"<b>{k}:</b> {v}")
                #     self.message_structured.setHtml("<br>".join(lines))
            except Exception:
                # fallback to plain text
                self.http_log.append(f"← {resp.status_code} {resp.reason}\n{resp.text}\n")
            self.status.showMessage("[HTTP] Sent ✔")
        except Exception as e:
            self.http_log.append(f"ERROR: {e}\n")
            self.status.showMessage("[HTTP] aborted")
  


if __name__ == "__main__":
    app = QApplication(sys.argv)
    sim = IsoSim()
    sim.show()
    sys.exit(app.exec_())
