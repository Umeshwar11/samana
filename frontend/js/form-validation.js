// =========================================================
// FORM VALIDATION SYSTEM
// =========================================================

class FormValidator {
  constructor(formId) {
    this.form = document.getElementById(formId);
    this.isValid = true;
    if (this.form) {
      this.setupValidation();
    }
  }

  setupValidation() {
    const inputs = this.form.querySelectorAll('input, textarea, select');
    inputs.forEach(input => {
      input.addEventListener('blur', (e) => this.validateField(e.target));
      input.addEventListener('change', (e) => this.validateField(e.target));
    });

    this.form.addEventListener('submit', (e) => this.handleSubmit(e));
  }

  validateField(field) {
    const errorId = `${field.id}Error`;
    const errorEl = document.getElementById(errorId);

    let isValid = true;
    let errorMessage = '';

    // Required validation
    if (field.hasAttribute('required') && !field.value.trim()) {
      isValid = false;
      errorMessage = `${this.getFieldLabel(field)} is required`;
    }

    // Email validation
    if (field.type === 'email' && field.value && !this.isValidEmail(field.value)) {
      isValid = false;
      errorMessage = 'Please enter a valid email address';
    }

    // Tel validation
    if (field.type === 'tel' && field.value && !this.isValidPhone(field.value)) {
      isValid = false;
      errorMessage = 'Please enter a valid phone number';
    }

    // Min length
    if (field.hasAttribute('minlength') && field.value.length < field.minLength) {
      isValid = false;
      errorMessage = `Must be at least ${field.minLength} characters`;
    }

    // Password strength (if has class 'password-strength')
    if (field.classList.contains('password-strength') && field.value) {
      const strength = this.checkPasswordStrength(field.value);
      if (strength.score < 2) {
        isValid = false;
        errorMessage = strength.message;
      }
    }

    // Update field UI
    if (isValid) {
      field.classList.remove('is-invalid');
      field.classList.add('is-valid');
      if (errorEl) {
        errorEl.style.display = 'none';
      }
    } else {
      field.classList.remove('is-valid');
      field.classList.add('is-invalid');
      if (errorEl) {
        errorEl.textContent = errorMessage;
        errorEl.style.display = 'block';
      } else {
        this.createErrorElement(field, errorMessage);
      }
    }

    return isValid;
  }

  handleSubmit(e) {
    const inputs = this.form.querySelectorAll('input, textarea, select');
    let formIsValid = true;

    inputs.forEach(input => {
      if (!this.validateField(input)) {
        formIsValid = false;
      }
    });

    if (!formIsValid) {
      e.preventDefault();
      toast.error('Please fix the errors above');
    }
  }

  createErrorElement(field, message) {
    const errorId = `${field.id}Error`;
    if (!document.getElementById(errorId)) {
      const errorEl = document.createElement('div');
      errorEl.id = errorId;
      errorEl.className = 'invalid-feedback d-block';
      errorEl.textContent = message;
      field.parentNode.appendChild(errorEl);
    }
  }

  isValidEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  isValidPhone(phone) {
    const regex = /^[\d\s\-\+\(\)]{10,}$/;
    return regex.test(phone.replace(/\D/g, ''));
  }

  checkPasswordStrength(password) {
    let score = 0;
    const messages = [];

    if (password.length >= 8) score++;
    if (password.length >= 12) score++;
    if (/[a-z]/.test(password)) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    if (score < 2) {
      messages.push('Password is too weak');
      messages.push('Use at least 8 characters with mix of uppercase, lowercase, numbers, and symbols');
    } else if (score < 4) {
      messages.push('Password is weak');
      messages.push('Add more variety: uppercase, numbers, or symbols');
    }

    return {
      score,
      message: messages.join('. ')
    };
  }

  getFieldLabel(field) {
    const label = document.querySelector(`label[for="${field.id}"]`);
    return label ? label.textContent.trim().replace('*', '') : field.id;
  }
}

// Initialize validators on page load
document.addEventListener('DOMContentLoaded', () => {
  if (document.getElementById('loginForm')) {
    new FormValidator('loginForm');
  }
  if (document.getElementById('registerForm')) {
    new FormValidator('registerForm');
  }
  if (document.getElementById('contactForm')) {
    new FormValidator('contactForm');
  }
});
