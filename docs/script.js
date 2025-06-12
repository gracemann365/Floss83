/* ============================
   FLOSSX83 - INTERACTIVE SCRIPT
   Premium Financial Platform
   ============================ */

// Global state management
const FlossX83 = {
  state: {
    isNavScrolled: false,
    currentTerminalLine: 0,
    transactionCount: 0,
    demoActive: false,
    particles: [],
    shapes: [],
  },

  config: {
    terminalSpeed: 100,
    particleCount: 15,
    shapeCount: 8,
    matrixColumns: 50,
    transactionInterval: 3000,
  },
};

// Utility functions
const utils = {
  random: (min, max) => Math.random() * (max - min) + min,
  randomChoice: (arr) => arr[Math.floor(Math.random() * arr.length)],
  debounce: (func, wait) => {
    let timeout;
    return function executedFunction(...args) {
      const later = () => {
        clearTimeout(timeout);
        func(...args);
      };
      clearTimeout(timeout);
      timeout = setTimeout(later, wait);
    };
  },
  throttle: (func, limit) => {
    let inThrottle;
    return function () {
      const args = arguments;
      const context = this;
      if (!inThrottle) {
        func.apply(context, args);
        inThrottle = true;
        setTimeout(() => (inThrottle = false), limit);
      }
    };
  },
};

// Navigation functionality
const Navigation = {
  init() {
    this.bindEvents();
    this.handleScroll();
  },

  bindEvents() {
    // Smooth scroll for navigation links
    document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
      anchor.addEventListener("click", (e) => {
        e.preventDefault();
        const target = document.querySelector(anchor.getAttribute("href"));
        if (target) {
          target.scrollIntoView({
            behavior: "smooth",
            block: "start",
          });
        }
      });
    });

    // Mobile menu toggle
    const hamburger = document.getElementById("hamburger");
    const navMenu = document.getElementById("navMenu");

    if (hamburger && navMenu) {
      hamburger.addEventListener("click", () => {
        hamburger.classList.toggle("active");
        navMenu.classList.toggle("active");
      });
      // Keyboard accessibility
      hamburger.addEventListener("keydown", (e) => {
        if (e.key === "Enter" || e.key === " ") {
          e.preventDefault();
          hamburger.click();
        }
      });
    }

    // Scroll events
    window.addEventListener(
      "scroll",
      utils.throttle(() => {
        this.handleScroll();
      }, 16)
    );
  },

  handleScroll() {
    const navbar = document.querySelector(".navbar");
    const scrolled = window.pageYOffset > 100;

    if (scrolled !== FlossX83.state.isNavScrolled) {
      FlossX83.state.isNavScrolled = scrolled;
      navbar?.classList.toggle("scrolled", scrolled);
    }
  },
};

// Terminal animation
const Terminal = {
  commands: [
    "./flossx83 --init-audit-engine",
    "[INFO] Starting FLOSSX83 Enterprise Suite...",
    "[OK] Core Switch Engine initialized",
    "[OK] HSM Tokenization service active",
    "[OK] Fraud Detection AI models loaded",
    "[INFO] Processing transaction flow...",
    "[AUDIT] ISO 8583 message parser ready",
    "[SECURITY] PCI DSS compliance verified",
    "[OK] ATM/POS Simulator GUI launched",
    "[TRACE] Real-time message tracer online",
    "[SUCCESS] All systems operational ✓",
    "[MONITOR] Awaiting transactions...",
  ],

  init() {
    this.terminal = document.getElementById("terminalContent");
    if (this.terminal) {
      this.startAnimation();
    }
  },

  startAnimation() {
    const interval = setInterval(() => {
      if (FlossX83.state.currentTerminalLine < this.commands.length) {
        this.addLine(this.commands[FlossX83.state.currentTerminalLine]);
        FlossX83.state.currentTerminalLine++;
      } else {
        clearInterval(interval);
        this.startBlinkingCursor();
      }
    }, FlossX83.config.terminalSpeed * 15);
  },

  addLine(text) {
    const line = document.createElement("div");
    line.className = "terminal-line";

    const prompt = document.createElement("span");
    prompt.className = "prompt";
    prompt.textContent = "$";

    const command = document.createElement("span");
    command.className = "command";

    line.appendChild(prompt);
    line.appendChild(command);
    this.terminal.appendChild(line);

    this.typeText(command, text);
  },

  typeText(element, text) {
    let i = 0;
    const interval = setInterval(() => {
      if (i < text.length) {
        element.textContent += text.charAt(i);
        i++;
      } else {
        clearInterval(interval);
      }
    }, FlossX83.config.terminalSpeed);
  },

  startBlinkingCursor() {
    const cursor = document.createElement("span");
    cursor.className = "cursor";
    cursor.textContent = "█";
    cursor.style.animation = "blink 1s infinite";
    this.terminal
      .querySelector(".terminal-line:last-child .command")
      .appendChild(cursor);
  },
};

// Matrix rain effect
const MatrixRain = {
  init() {
    this.container = document.getElementById("matrixRain");
    if (this.container) {
      this.createColumns();
    }
  },

  createColumns() {
    const chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    const columns = Math.floor(window.innerWidth / 20);

    for (let i = 0; i < Math.min(columns, FlossX83.config.matrixColumns); i++) {
      setTimeout(() => {
        this.createColumn(i, chars);
      }, utils.random(0, 5000));
    }
  },

  createColumn(index, chars) {
    const column = document.createElement("div");
    column.className = "matrix-column";
    column.style.left = `${index * 20}px`;
    column.style.animationDuration = `${utils.random(8, 15)}s`;
    column.style.animationDelay = `${utils.random(0, 5)}s`;

    // Generate random characters
    let text = "";
    for (let i = 0; i < utils.random(10, 30); i++) {
      text += chars.charAt(Math.floor(Math.random() * chars.length)) + "\n";
    }
    column.textContent = text;

    this.container.appendChild(column);

    // Remove and recreate after animation
    setTimeout(() => {
      if (this.container.contains(column)) {
        this.container.removeChild(column);
        this.createColumn(index, chars);
      }
    }, 15000);
  },
};

// Floating shapes
const FloatingShapes = {
  init() {
    this.container = document.getElementById("floatingShapes");
    if (this.container) {
      this.createShapes();
    }
  },

  createShapes() {
    const shapeTypes = ["triangle", "circle", "square", "hexagon"];

    for (let i = 0; i < FlossX83.config.shapeCount; i++) {
      setTimeout(() => {
        this.createShape(shapeTypes);
      }, utils.random(0, 3000));
    }
  },

  createShape(types) {
    const shape = document.createElement("div");
    shape.className = `shape ${utils.randomChoice(types)}`;
    shape.style.left = `${utils.random(0, window.innerWidth)}px`;
    shape.style.top = `${utils.random(0, window.innerHeight)}px`;
    shape.style.animationDuration = `${utils.random(8, 20)}s`;
    shape.style.animationDelay = `${utils.random(0, 5)}s`;

    this.container.appendChild(shape);
    FlossX83.state.shapes.push(shape);

    // Clean up after animation
    setTimeout(() => {
      if (this.container.contains(shape)) {
        this.container.removeChild(shape);
        const index = FlossX83.state.shapes.indexOf(shape);
        if (index > -1) FlossX83.state.shapes.splice(index, 1);
        this.createShape(types);
      }
    }, 25000);
  },
};

// Particle system
const ParticleSystem = {
  init() {
    this.container = document.getElementById("particleSystem");
    if (this.container) {
      this.createParticles();
    }
  },

  createParticles() {
    for (let i = 0; i < FlossX83.config.particleCount; i++) {
      setTimeout(() => {
        this.createParticle();
      }, utils.random(0, 2000));
    }
  },

  createParticle() {
    const particle = document.createElement("div");
    particle.className = "particle";
    particle.style.left = `${utils.random(0, window.innerWidth)}px`;
    particle.style.animationDuration = `${utils.random(6, 12)}s`;
    particle.style.animationDelay = `${utils.random(0, 3)}s`;

    this.container.appendChild(particle);
    FlossX83.state.particles.push(particle);

    // Clean up and recreate
    setTimeout(() => {
      if (this.container.contains(particle)) {
        this.container.removeChild(particle);
        const index = FlossX83.state.particles.indexOf(particle);
        if (index > -1) FlossX83.state.particles.splice(index, 1);
        this.createParticle();
      }
    }, 14000);
  },
};

// Interactive stats
const StatsAnimator = {
  init() {
    this.observeStats();
  },

  observeStats() {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            this.animateCard(entry.target);
          }
        });
      },
      { threshold: 0.3 }
    );

    document.querySelectorAll(".stat-card").forEach((card) => {
      observer.observe(card);
    });
  },

  animateCard(card) {
    card.style.transform = "translateY(-8px)";
    card.style.transition = "all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94)";

    setTimeout(() => {
      card.style.transform = "translateY(0)";
    }, 300);
  },
};

// Transaction demo
const TransactionDemo = {
  init() {
    this.feed = document.getElementById("transactionFeed");
    if (this.feed) {
      this.startDemo();
    }
  },

  transactionTypes: [
    { type: "ATM_WITHDRAWAL", icon: "🏧", status: "APPROVED" },
    { type: "POS_PURCHASE", icon: "💳", status: "APPROVED" },
    { type: "ONLINE_TRANSFER", icon: "💻", status: "APPROVED" },
    { type: "CONTACTLESS_PAY", icon: "📱", status: "APPROVED" },
    { type: "CARD_VALIDATION", icon: "✅", status: "VERIFIED" },
    { type: "FRAUD_CHECK", icon: "🛡️", status: "BLOCKED" },
  ],

  startDemo() {
    this.createTransaction();
    setInterval(() => {
      this.createTransaction();
    }, FlossX83.config.transactionInterval);
  },

  createTransaction() {
    const transaction = utils.randomChoice(this.transactionTypes);
    const amount = utils.random(10, 9999).toFixed(2);
    const id = `TXN${Date.now().toString().slice(-6)}`;

    const element = document.createElement("div");
    element.className = "transaction-item";
    element.innerHTML = `
            <div class="transaction-header">
                <span class="transaction-icon">${transaction.icon}</span>
                <span class="transaction-id">${id}</span>
                <span class="transaction-status ${transaction.status.toLowerCase()}">${
      transaction.status
    }</span>
            </div>
            <div class="transaction-details">
                <span class="transaction-type">${transaction.type}</span>
                <span class="transaction-amount">$${amount}</span>
            </div>
            <div class="transaction-time">${new Date().toLocaleTimeString()}</div>
        `;

    this.feed.insertBefore(element, this.feed.firstChild);

    // Animate in
    requestAnimationFrame(() => {
      element.style.opacity = "1";
      element.style.transform = "translateX(0)";
    });

    // Remove old transactions
    const items = this.feed.querySelectorAll(".transaction-item");
    if (items.length > 8) {
      items[items.length - 1].remove();
    }

    FlossX83.state.transactionCount++;
  },
};

// Intersection Observer for animations
const ScrollAnimations = {
  init() {
    this.createObserver();
  },

  createObserver() {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("animate-in");
          }
        });
      },
      {
        threshold: 0.1,
        rootMargin: "0px 0px -50px 0px",
      }
    );

    // Observe elements for animation
    document
      .querySelectorAll(".feature-card, .flow-step, .security-feature")
      .forEach((el) => {
        observer.observe(el);
      });
  },
};

// Performance monitoring
const Performance = {
  init() {
    this.monitorPerformance();
    this.optimizeAnimations();
  },

  monitorPerformance() {
    // Reduce animations on low-end devices
    if (navigator.hardwareConcurrency && navigator.hardwareConcurrency < 4) {
      document.body.classList.add("reduced-motion");
    }

    // Battery API for mobile optimization
    if ("getBattery" in navigator) {
      navigator.getBattery().then((battery) => {
        if (battery.level < 0.2) {
          document.body.classList.add("battery-save");
        }
      });
    }
  },

  optimizeAnimations() {
    // Pause animations when page is not visible
    document.addEventListener("visibilitychange", () => {
      if (document.hidden) {
        document.body.classList.add("paused-animations");
      } else {
        document.body.classList.remove("paused-animations");
      }
    });
  },
};

// Resize handler
const ResizeHandler = {
  init() {
    window.addEventListener(
      "resize",
      utils.debounce(() => {
        this.handleResize();
      }, 250)
    );
  },

  handleResize() {
    // Recreate effects on significant size changes
    if (Math.abs(window.innerWidth - this.lastWidth) > 200) {
      this.recreateEffects();
      this.lastWidth = window.innerWidth;
    }
  },

  recreateEffects() {
    // Clear existing effects
    const matrixContainer = document.getElementById("matrixRain");
    const shapesContainer = document.getElementById("floatingShapes");

    if (matrixContainer) matrixContainer.innerHTML = "";
    if (shapesContainer) shapesContainer.innerHTML = "";

    // Recreate with new dimensions
    setTimeout(() => {
      MatrixRain.init();
      FloatingShapes.init();
    }, 100);
  },
};

// Add CSS for animations
const AnimationStyles = {
  init() {
    const style = document.createElement("style");
    style.textContent = `
            .transaction-item {
                background: var(--glass-bg);
                border: 1px solid var(--glass-border);
                border-radius: 12px;
                padding: 1rem;
                margin-bottom: 0.5rem;
                opacity: 0;
                transform: translateX(-20px);
                transition: all 0.3s ease;
            }
            
            .transaction-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 0.5rem;
            }
            
            .transaction-status.approved { color: var(--neon-green); }
            .transaction-status.blocked { color: var(--hot-pink); }
            .transaction-status.verified { color: var(--cyber-blue); }
            
            .transaction-details {
                display: flex;
                justify-content: space-between;
                font-size: 0.9rem;
                color: var(--text-secondary);
            }
            
            .transaction-time {
                font-size: 0.8rem;
                color: var(--text-secondary);
                margin-top: 0.25rem;
            }
            
            .cursor {
                color: var(--neon-green);
                animation: blink 1s infinite;
            }
            
            @keyframes blink {
                0%, 50% { opacity: 1; }
                51%, 100% { opacity: 0; }
            }
            
            .animate-in {
                animation: slideInUp 0.8s ease forwards;
            }
            
            @keyframes slideInUp {
                from {
                    opacity: 0;
                    transform: translateY(30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            
            .paused-animations * {
                animation-play-state: paused !important;
            }
            
            .battery-save .matrix-rain,
            .battery-save .floating-shapes,
            .battery-save .particle-system {
                display: none;
            }
            
            .reduced-motion * {
                animation-duration: 0.1s !important;
                transition-duration: 0.1s !important;
            }
        `;
    document.head.appendChild(style);
  },
};

// Initialize everything when DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
  console.log("🚀 FLOSSX83 Enterprise Suite - Initializing...");

  // Initialize all modules
  AnimationStyles.init();
  Navigation.init();
  Terminal.init();
  MatrixRain.init();
  FloatingShapes.init();
  ParticleSystem.init();
  StatsAnimator.init();
  TransactionDemo.init();
  ScrollAnimations.init();
  Performance.init();
  ResizeHandler.init();

  console.log("✅ FLOSSX83 Enterprise Suite - Ready");
});

// Export for external access
window.FlossX83 = FlossX83;
