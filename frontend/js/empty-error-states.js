// =========================================================
// EMPTY & ERROR STATE COMPONENTS
// =========================================================

class StateComponent {
  static createEmptyState(title, message, action = null) {
    let actionHtml = '';
    if (action) {
      actionHtml = `
        <a href="${action.url}" class="btn btn-success mt-3">
          <i class="fas fa-${action.icon}"></i> ${action.text}
        </a>
      `;
    }

    return `
      <div class="empty-state text-center py-5">
        <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
        <h3>${this.escapeHtml(title)}</h3>
        <p class="text-muted">${this.escapeHtml(message)}</p>
        ${actionHtml}
      </div>
    `;
  }

  static createErrorState(title, message, retryCallback = null) {
    let actionHtml = '';
    if (retryCallback) {
      actionHtml = `
        <button class="btn btn-success mt-3" onclick="window.retryAction && window.retryAction()">
          <i class="fas fa-redo"></i> Try Again
        </button>
      `;
    }

    return `
      <div class="error-state text-center py-5">
        <i class="fas fa-exclamation-circle fa-3x text-danger mb-3"></i>
        <h3>${this.escapeHtml(title)}</h3>
        <p class="text-muted">${this.escapeHtml(message)}</p>
        ${actionHtml}
      </div>
    `;
  }

  static createNoResultsState(searchTerm = null) {
    let message = 'No results found';
    if (searchTerm) {
      message += ` for "${this.escapeHtml(searchTerm)}"`;
    }

    return `
      <div class="empty-state text-center py-5">
        <i class="fas fa-search fa-3x text-muted mb-3"></i>
        <h3>No Results</h3>
        <p class="text-muted">${message}</p>
        <a href="explore.html" class="btn btn-success mt-3">
          <i class="fas fa-arrow-left"></i> Back to Browse
        </a>
      </div>
    `;
  }

  static createLoadingState(message = 'Loading...') {
    return `
      <div class="loading-state text-center py-5">
        <div class="spinner-border text-success mb-3" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p class="text-muted">${this.escapeHtml(message)}</p>
      </div>
    `;
  }

  static showEmptyState(containerId, title, message, action = null) {
    const container = document.getElementById(containerId);
    if (container) {
      container.innerHTML = this.createEmptyState(title, message, action);
    }
  }

  static showErrorState(containerId, title, message, retryCallback = null) {
    const container = document.getElementById(containerId);
    if (container) {
      container.innerHTML = this.createErrorState(title, message, retryCallback);
      if (retryCallback) {
        window.retryAction = retryCallback;
      }
    }
  }

  static showNoResults(containerId, searchTerm = null) {
    const container = document.getElementById(containerId);
    if (container) {
      container.innerHTML = this.createNoResultsState(searchTerm);
    }
  }

  static showLoading(containerId, message = 'Loading...') {
    const container = document.getElementById(containerId);
    if (container) {
      container.innerHTML = this.createLoadingState(message);
    }
  }

  static escapeHtml(text) {
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

// Export for use
window.StateComponent = StateComponent;
