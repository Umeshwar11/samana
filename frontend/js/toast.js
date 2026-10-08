// =========================================================
// TOAST NOTIFICATION SYSTEM
// =========================================================

class Toast {
  constructor() {
    this.container = null;
    this.init();
  }

  init() {
    if (!document.getElementById("toastContainer")) {
      const container = document.createElement("div");
      container.id = "toastContainer";
      container.className = "toast-container";
      document.body.appendChild(container);
      this.container = container;
    } else {
      this.container = document.getElementById("toastContainer");
    }
  }

  show(message, type = "info", duration = 3000) {
    const toastId = `toast-${Date.now()}`;
    const toastEl = document.createElement("div");
    toastEl.id = toastId;
    toastEl.className = `toast toast-${type}`;
    toastEl.setAttribute("role", "status");
    toastEl.setAttribute("aria-live", "polite");
    toastEl.setAttribute("aria-atomic", "true");

    const icon = this.getIcon(type);
    toastEl.innerHTML = `
      <div class="toast-content">
        <span class="toast-icon">${icon}</span>
        <span class="toast-message">${this.escapeHtml(message)}</span>
        <button type="button" class="toast-close" aria-label="Close notification">
          <i class="fas fa-times"></i>
        </button>
      </div>
    `;

    this.container.appendChild(toastEl);

    toastEl.querySelector(".toast-close").addEventListener("click", () => {
      this.remove(toastId);
    });

    // Auto-remove after duration
    if (duration > 0) {
      setTimeout(() => this.remove(toastId), duration);
    }

    // Trigger animation
    requestAnimationFrame(() => {
      toastEl.classList.add("show");
    });

    return toastId;
  }

  success(message, duration = 3000) {
    return this.show(message, "success", duration);
  }

  error(message, duration = 3000) {
    return this.show(message, "error", duration);
  }

  info(message, duration = 3000) {
    return this.show(message, "info", duration);
  }

  warning(message, duration = 3000) {
    return this.show(message, "warning", duration);
  }

  remove(toastId) {
    const toastEl = document.getElementById(toastId);
    if (toastEl) {
      toastEl.classList.remove("show");
      setTimeout(() => toastEl.remove(), 300);
    }
  }

  getIcon(type) {
    const icons = {
      success: '<i class="fas fa-check-circle"></i>',
      error: '<i class="fas fa-exclamation-circle"></i>',
      info: '<i class="fas fa-info-circle"></i>',
      warning: '<i class="fas fa-exclamation-triangle"></i>'
    };
    return icons[type] || icons.info;
  }

  escapeHtml(text) {
    const map = {
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      '"': "&quot;",
      "'": "&#039;"
    };
    return text.replace(/[&<>"']/g, (m) => map[m]);
  }
}

// Global toast instance
const toast = new Toast();
