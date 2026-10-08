# 🎨 PREMIUM CSS IMPLEMENTATION GUIDE

**Quick Start Guide for Implementing Professional UI Enhancements**

---

## 📋 Table of Contents

1. [Typography CSS](#typography-css)
2. [Enhanced Color System](#enhanced-color-system)
3. [Premium Button System](#premium-button-system)
4. [Form Enhancements](#form-enhancements)
5. [Animations & Transitions](#animations--transitions)
6. [Dark Mode](#dark-mode)
7. [Accessibility CSS](#accessibility-css)
8. [Card Components](#card-components)
9. [Implementation Checklist](#implementation-checklist)

---

## TYPOGRAPHY CSS

**File: `css/typography.css`** (Create new file)

```css
/* =========================================================
   PREMIUM TYPOGRAPHY SYSTEM
   ========================================================= */

@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800;900&family=Playfair+Display:wght@700;800;900&family=Inter:wght@300;400;500;600;700;800&display=swap');

:root {
    /* Font Families */
    --font-heading: 'Playfair Display', serif;
    --font-body: 'Inter', sans-serif;
    --font-accent: 'Poppins', sans-serif;
    
    /* Font Sizes */
    --text-xs: 0.75rem;      /* 12px */
    --text-sm: 0.875rem;     /* 14px */
    --text-base: 1rem;       /* 16px */
    --text-lg: 1.125rem;     /* 18px */
    --text-xl: 1.25rem;      /* 20px */
    --text-2xl: 1.5rem;      /* 24px */
    --text-3xl: 1.875rem;    /* 30px */
    --text-4xl: 2.25rem;     /* 36px */
    --text-5xl: 3rem;        /* 48px */
    --text-6xl: 3.75rem;     /* 60px */
    
    /* Line Heights */
    --leading-tight: 1.2;
    --leading-normal: 1.5;
    --leading-relaxed: 1.75;
    
    /* Font Weights */
    --weight-light: 300;
    --weight-normal: 400;
    --weight-medium: 500;
    --weight-semibold: 600;
    --weight-bold: 700;
    --weight-extrabold: 800;
    --weight-black: 900;
}

/* Global Typography */
body {
    font-family: var(--font-body);
    font-size: var(--text-base);
    line-height: var(--leading-normal);
    letter-spacing: 0.3px;
    color: var(--text);
    background: var(--light-bg);
}

h1, h2, h3, h4, h5, h6 {
    font-family: var(--font-heading);
    font-weight: var(--weight-extrabold);
    letter-spacing: -0.5px;
    line-height: var(--leading-tight);
    margin: 0;
    padding: 0;
}

h1 {
    font-size: clamp(32px, 5vw, 56px);
}

h2 {
    font-size: clamp(24px, 4vw, 42px);
}

h3 {
    font-size: clamp(20px, 3vw, 32px);
}

h4 {
    font-size: var(--text-xl);
    font-weight: var(--weight-bold);
}

h5 {
    font-size: var(--text-lg);
    font-weight: var(--weight-bold);
}

h6 {
    font-size: var(--text-base);
    font-weight: var(--weight-semibold);
    text-transform: uppercase;
    letter-spacing: 1px;
}

p {
    margin: 0 0 1rem 0;
    line-height: var(--leading-relaxed);
}

.text-sm {
    font-size: var(--text-sm);
    letter-spacing: 0.2px;
}

.text-lg {
    font-size: var(--text-lg);
    letter-spacing: 0.3px;
}

.text-xl {
    font-size: var(--text-xl);
    letter-spacing: 0.4px;
}

.text-muted {
    color: var(--muted);
}

.text-secondary {
    color: var(--neutral-600);
}

.font-heading {
    font-family: var(--font-heading);
    font-weight: var(--weight-extrabold);
}

.font-body {
    font-family: var(--font-body);
}

.font-accent {
    font-family: var(--font-accent);
}

.font-light {
    font-weight: var(--weight-light);
}

.font-normal {
    font-weight: var(--weight-normal);
}

.font-medium {
    font-weight: var(--weight-medium);
}

.font-semibold {
    font-weight: var(--weight-semibold);
}

.font-bold {
    font-weight: var(--weight-bold);
}

.font-extrabold {
    font-weight: var(--weight-extrabold);
}

/* Text Transforms */
.uppercase {
    text-transform: uppercase;
    letter-spacing: 1.5px;
}

.lowercase {
    text-transform: lowercase;
}

.capitalize {
    text-transform: capitalize;
}

/* Text Alignment */
.text-center {
    text-align: center;
}

.text-left {
    text-align: left;
}

.text-right {
    text-align: right;
}

/* Links */
a {
    color: var(--primary);
    text-decoration: none;
    transition: all 0.3s ease;
    font-weight: 600;
}

a:hover {
    color: var(--primary-dark);
    text-decoration: underline;
}

/* Code */
code {
    font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
    background: var(--neutral-100);
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 0.9em;
}

pre {
    background: var(--neutral-900);
    color: white;
    padding: 16px;
    border-radius: 8px;
    overflow-x: auto;
}

/* Lists */
ul, ol {
    margin: 1rem 0;
    padding-left: 2rem;
}

li {
    margin-bottom: 0.5rem;
    line-height: var(--leading-relaxed);
}

/* Blockquote */
blockquote {
    border-left: 4px solid var(--primary);
    padding-left: 1rem;
    margin: 1.5rem 0;
    color: var(--muted);
    font-style: italic;
}
```

**Update: `css/style.css`** (Replace typography section)

```css
/* Replace the existing body/h1/h2 etc. styles with: */

@import url('css/typography.css');

body {
    font-family: var(--font-body);
    background: var(--light-bg);
    color: var(--text);
    line-height: var(--leading-normal);
    overflow-x: hidden;
}
```

---

## ENHANCED COLOR SYSTEM

**Update: `css/style.css`** (Add to :root)

```css
:root {
    /* ======= PRIMARY COLORS ======= */
    --primary: #1a7c4a;
    --primary-light: #eef9f3;
    --primary-dark: #0f5733;
    
    /* ======= SECONDARY COLORS ======= */
    --secondary: #d4a373;
    --secondary-light: #f5ede2;
    --secondary-dark: #a67c52;
    
    /* ======= ACCENT COLORS ======= */
    --accent-blue: #2563eb;
    --accent-red: #dc2626;
    --accent-orange: #ea580c;
    
    /* ======= NEUTRAL SCALE ======= */
    --neutral-50: #fafaf9;
    --neutral-100: #f5f5f4;
    --neutral-200: #e7e5e4;
    --neutral-300: #d6d3d1;
    --neutral-400: #a8a29d;
    --neutral-500: #78716b;
    --neutral-600: #57534e;
    --neutral-700: #44403c;
    --neutral-800: #292524;
    --neutral-900: #1c1917;
    
    /* ======= SEMANTIC COLORS ======= */
    --success: #16a34a;
    --warning: #ea8c55;
    --error: #dc2626;
    --info: #0284c7;
    
    /* ======= GRADIENTS ======= */
    --gradient-primary: linear-gradient(135deg, #1a7c4a 0%, #0f5733 100%);
    --gradient-warm: linear-gradient(135deg, #d4a373 0%, #a67c52 100%);
    --gradient-sunset: linear-gradient(135deg, #ea580c 0%, #d4a373 100%);
    --gradient-cool: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
    --gradient-vibrant: linear-gradient(135deg, #1a7c4a 0%, #2563eb 100%);
    
    /* ======= TEXT COLORS ======= */
    --text-primary: #1c1917;
    --text-secondary: #78716b;
    --text-tertiary: #a8a29d;
    
    /* ======= LEGACY (kept for compatibility) ======= */
    --samana-green: #1a7c4a;
    --samana-green-dark: #0f5733;
    --samana-green-light: #eef9f3;
    --samana-gold: #d4a373;
    --samana-gold-dark: #a67c52;
    --cream: #f5ede2;
    --light-bg: #fafaf9;
    --white: #ffffff;
    --dark: #1c1917;
    --text: #57534e;
    --muted: #78716b;
    --border: #e7e5e4;
}
```

---

## PREMIUM BUTTON SYSTEM

**File: `css/buttons.css`** (Create new file)

```css
/* =========================================================
   PREMIUM BUTTON SYSTEM
   ========================================================= */

/* Base Button Styles */
.btn,
button {
    font-family: var(--font-accent);
    font-weight: 600;
    font-size: var(--text-sm);
    border: none;
    border-radius: 8px;
    padding: 12px 24px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.23, 1, 0.320, 1);
    outline: none;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    white-space: nowrap;
}

/* ======= PRIMARY BUTTONS ======= */
.btn-primary,
.btn-success {
    background: var(--primary);
    color: white;
    box-shadow: 0 4px 12px rgba(26, 124, 74, 0.2);
}

.btn-primary:hover:not(:disabled),
.btn-success:hover:not(:disabled) {
    background: var(--primary-dark);
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(26, 124, 74, 0.35);
}

.btn-primary:active:not(:disabled),
.btn-success:active:not(:disabled) {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(26, 124, 74, 0.25);
}

/* ======= SECONDARY BUTTONS ======= */
.btn-secondary {
    background: transparent;
    color: var(--primary);
    border: 2px solid var(--primary);
    padding: 10px 22px;
}

.btn-secondary:hover:not(:disabled) {
    background: var(--primary-light);
    color: var(--primary-dark);
    border-color: var(--primary-dark);
}

/* ======= GHOST BUTTONS ======= */
.btn-ghost {
    background: transparent;
    color: var(--text-primary);
    border: none;
    padding: 8px 12px;
    text-decoration: underline;
}

.btn-ghost:hover {
    color: var(--primary);
    text-decoration-thickness: 2px;
}

/* ======= DANGER BUTTONS ======= */
.btn-danger {
    background: var(--error);
    color: white;
    box-shadow: 0 4px 12px rgba(220, 38, 38, 0.2);
}

.btn-danger:hover:not(:disabled) {
    background: #b91c1c;
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(220, 38, 38, 0.35);
}

/* ======= WARNING BUTTONS ======= */
.btn-warning {
    background: var(--warning);
    color: white;
    box-shadow: 0 4px 12px rgba(234, 140, 85, 0.2);
}

.btn-warning:hover:not(:disabled) {
    background: #d97706;
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(234, 140, 85, 0.35);
}

/* ======= INFO BUTTONS ======= */
.btn-info {
    background: var(--info);
    color: white;
    box-shadow: 0 4px 12px rgba(2, 132, 199, 0.2);
}

.btn-info:hover:not(:disabled) {
    background: #0c7ec7;
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(2, 132, 199, 0.35);
}

/* ======= BUTTON SIZES ======= */
.btn-sm {
    padding: 8px 16px;
    font-size: var(--text-xs);
}

.btn-md {
    padding: 12px 24px;
    font-size: var(--text-sm);
}

.btn-lg {
    padding: 14px 32px;
    font-size: var(--text-base);
    border-radius: 10px;
}

/* ======= FULL WIDTH ======= */
.btn-full-width {
    width: 100%;
}

/* ======= ICON BUTTONS ======= */
.btn-icon {
    width: 40px;
    height: 40px;
    padding: 0;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.btn-icon.btn-lg {
    width: 48px;
    height: 48px;
}

/* ======= DISABLED STATE ======= */
.btn:disabled,
button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    transform: none !important;
}

/* ======= LOADING STATE ======= */
.btn.loading {
    position: relative;
    color: transparent;
}

.btn.loading::after {
    content: '';
    position: absolute;
    width: 16px;
    height: 16px;
    top: 50%;
    left: 50%;
    margin-left: -8px;
    margin-top: -8px;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    border-top-color: white;
    animation: spinner 0.8s linear infinite;
}

@keyframes spinner {
    to { transform: rotate(360deg); }
}

/* ======= BUTTON GROUPS ======= */
.btn-group {
    display: inline-flex;
    gap: 8px;
    flex-wrap: wrap;
}

.btn-group-vertical {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

/* ======= BUTTON WITH ICON ======= */
.btn i,
button i {
    font-size: 1em;
}

.btn i:first-child:not(:last-child) {
    margin-right: 4px;
}

.btn i:last-child:not(:first-child) {
    margin-left: 4px;
}
```

**Update: `css/style.css`**

```css
/* Add this after typography import */
@import url('css/buttons.css');

/* Replace existing button CSS with reference to buttons.css */
```

---

## FORM ENHANCEMENTS

**File: `css/forms.css`** (Create new file)

```css
/* =========================================================
   PREMIUM FORM SYSTEM
   ========================================================= */

/* FORM GROUPS */
.form-group {
    margin-bottom: 24px;
}

.form-label {
    display: block;
    margin-bottom: 8px;
    font-weight: 600;
    font-size: var(--text-sm);
    color: var(--text-primary);
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.form-label .required {
    color: var(--error);
    margin-left: 4px;
}

.form-label .optional {
    color: var(--muted);
    font-weight: 400;
    text-transform: none;
    letter-spacing: normal;
}

/* FORM CONTROLS */
.form-control,
.form-select,
textarea,
input[type="text"],
input[type="email"],
input[type="password"],
input[type="number"],
input[type="date"],
input[type="time"] {
    border: 2px solid var(--border);
    border-radius: 8px;
    padding: 12px 14px;
    font-family: var(--font-body);
    font-size: var(--text-sm);
    transition: all 0.3s ease;
    background: white;
    color: var(--text-primary);
}

.form-control:focus,
.form-select:focus,
textarea:focus,
input[type="text"]:focus,
input[type="email"]:focus,
input[type="password"]:focus,
input[type="number"]:focus,
input[type="date"]:focus,
input[type="time"]:focus {
    border-color: var(--primary);
    outline: none;
    box-shadow: 0 0 0 3px rgba(26, 124, 74, 0.1);
    background: #fafaf9;
}

/* FORM STATES */
.form-control.is-valid,
.form-select.is-valid {
    border-color: var(--success);
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 8 8'%3e%3cpath fill='%2316a34a' d='M2.3 6.73L.6 4.53c-.4-1.04.46-1.4 1.1-.8l1.1 1.4 3.4-3.8c.6-.63 1.6-.27 1.2.7l-4 4.6c-.43.5-.8.4-1.1.1z'/%3e%3c/svg%3e");
    background-repeat: no-repeat;
    background-position: right 12px center;
    background-size: 16px 12px;
    padding-right: 38px;
}

.form-control.is-invalid,
.form-select.is-invalid {
    border-color: var(--error);
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 12 12' width='12' height='12' fill='none' stroke='%23dc2626' viewBox='0 0 12 12'%3e%3ccircle cx='6' cy='6' r='4.5'/%3e%3cpath stroke-linejoin='round' d='M5.8 3.6h.4L6 6.5z'/%3e%3ccircle cx='6' cy='8.2' r='.6' fill='%23dc2626' stroke='none'/%3e%3c/svg%3e");
    background-repeat: no-repeat;
    background-position: right 12px center;
    background-size: 16px 12px;
    padding-right: 38px;
}

.valid-feedback,
.invalid-feedback {
    display: block;
    margin-top: 6px;
    font-size: var(--text-xs);
    font-weight: 500;
}

.valid-feedback {
    color: var(--success);
}

.invalid-feedback {
    color: var(--error);
}

/* INPUT GROUP */
.input-group-text {
    background: var(--neutral-100);
    border: 2px solid var(--border);
    color: var(--neutral-600);
    border-radius: 8px;
    font-weight: 500;
    padding: 12px 14px;
}

.input-group-text + .form-control,
.form-control + .input-group-text {
    border-radius: 0 8px 8px 0;
}

.input-group-text:first-child + .form-control {
    border-radius: 0 8px 8px 0;
    border-left: none;
}

/* CHECKBOX & RADIO */
.form-check {
    margin-bottom: 12px;
    display: flex;
    align-items: center;
}

.form-check-input {
    width: 20px;
    height: 20px;
    margin: 2px 0 0 0;
    border: 2px solid var(--border);
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s ease;
    appearance: none;
    -webkit-appearance: none;
    background: white;
    flex-shrink: 0;
}

.form-check-input:checked {
    background: var(--primary);
    border-color: var(--primary);
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%23fff' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M2 8l4 4 8-8'/%3e%3c/svg%3e");
    background-repeat: no-repeat;
    background-position: center;
    background-size: 100%;
}

.form-check-input:focus {
    box-shadow: 0 0 0 3px rgba(26, 124, 74, 0.1);
    border-color: var(--primary);
}

.form-check-input:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.form-check-label {
    cursor: pointer;
    margin-left: 8px;
    user-select: none;
    margin-bottom: 0;
}

/* RADIO BUTTONS */
.form-check-input[type="radio"] {
    border-radius: 50%;
}

.form-check-input[type="radio"]:checked {
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3ccircle cx='8' cy='8' r='3' fill='%23fff'/%3e%3c/svg%3e");
}

/* TEXTAREA */
textarea,
.form-control[rows] {
    resize: vertical;
    min-height: 120px;
    font-family: var(--font-body);
}

textarea:focus {
    border-color: var(--primary);
}

/* SELECT */
.form-select {
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%231a7c4a' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M2 5l6 6 6-6'/%3e%3c/svg%3e");
    background-repeat: no-repeat;
    background-position: right 12px center;
    background-size: 16px 12px;
    padding-right: 38px;
    appearance: none;
    -webkit-appearance: none;
    -moz-appearance: none;
}

/* FORM HELP TEXT */
.form-text {
    display: block;
    margin-top: 4px;
    font-size: var(--text-xs);
    color: var(--muted);
}

/* FLOATING LABELS */
.form-floating > label {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    padding: 1rem 0.75rem;
    pointer-events: none;
    border: 1px solid transparent;
    transform-origin: 0 0;
    transition: opacity 0.1s ease-in-out, transform 0.1s ease-in-out;
}

.form-floating > .form-control:focus ~ label,
.form-floating > .form-control:not(:placeholder-shown) ~ label {
    opacity: 0.65;
    transform: scale(0.85) translateY(-0.5rem) translateX(0.15rem);
}
```

---

## ANIMATIONS & TRANSITIONS

**File: `css/animations.css`** (Create new file)

```css
/* =========================================================
   PREMIUM ANIMATIONS & TRANSITIONS
   ========================================================= */

/* FADE IN */
@keyframes fadeIn {
    from {
        opacity: 0;
    }
    to {
        opacity: 1;
    }
}

.fade-in {
    animation: fadeIn 0.6s ease forwards;
}

/* SLIDE UP */
@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.slide-up {
    animation: slideUp 0.6s ease forwards;
}

/* SLIDE DOWN */
@keyframes slideDown {
    from {
        opacity: 0;
        transform: translateY(-30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.slide-down {
    animation: slideDown 0.6s ease forwards;
}

/* SLIDE LEFT */
@keyframes slideLeft {
    from {
        opacity: 0;
        transform: translateX(30px);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

.slide-left {
    animation: slideLeft 0.6s ease forwards;
}

/* SLIDE RIGHT */
@keyframes slideRight {
    from {
        opacity: 0;
        transform: translateX(-30px);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

.slide-right {
    animation: slideRight 0.6s ease forwards;
}

/* SCALE IN */
@keyframes scaleIn {
    from {
        opacity: 0;
        transform: scale(0.95);
    }
    to {
        opacity: 1;
        transform: scale(1);
    }
}

.scale-in {
    animation: scaleIn 0.4s ease forwards;
}

/* BOUNCE */
@keyframes bounce {
    0%, 100% {
        transform: translateY(0);
    }
    50% {
        transform: translateY(-10px);
    }
}

.bounce {
    animation: bounce 1s ease-in-out;
}

/* PULSE */
@keyframes pulse {
    0%, 100% {
        opacity: 1;
    }
    50% {
        opacity: 0.5;
    }
}

.pulse {
    animation: pulse 2s ease-in-out infinite;
}

/* SPIN */
@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}

.spin {
    animation: spin 1s linear infinite;
}

/* HEARTBEAT */
@keyframes heartbeat {
    0%, 100% {
        transform: scale(1);
    }
    25% {
        transform: scale(1.1);
    }
    50% {
        transform: scale(1);
    }
}

.heartbeat {
    animation: heartbeat 1.3s ease-in-out infinite;
}

/* FLOAT */
@keyframes float {
    0%, 100% {
        transform: translateY(0px);
    }
    50% {
        transform: translateY(-10px);
    }
}

.float {
    animation: float 3s ease-in-out infinite;
}

/* SHIMMER/SKELETON LOADING */
@keyframes shimmer {
    0% {
        background-position: -1000px 0;
    }
    100% {
        background-position: 1000px 0;
    }
}

.skeleton {
    background: linear-gradient(
        90deg,
        rgba(255, 255, 255, 0.1) 25%,
        rgba(255, 255, 255, 0.2) 50%,
        rgba(255, 255, 255, 0.1) 75%
    );
    background-size: 1000px 100%;
    animation: shimmer 2s infinite;
    border-radius: 8px;
}

/* STAGGER ANIMATIONS */
.stagger-1 { animation-delay: 0.1s; }
.stagger-2 { animation-delay: 0.2s; }
.stagger-3 { animation-delay: 0.3s; }
.stagger-4 { animation-delay: 0.4s; }
.stagger-5 { animation-delay: 0.5s; }

/* DELAYED ANIMATIONS */
.delay-100 { animation-delay: 100ms; }
.delay-200 { animation-delay: 200ms; }
.delay-300 { animation-delay: 300ms; }
.delay-500 { animation-delay: 500ms; }

/* SMOOTH TRANSITIONS */
body {
    --transition: 0.3s cubic-bezier(0.23, 1, 0.320, 1);
}

.transition-all {
    transition: all var(--transition);
}

.transition-colors {
    transition: background-color var(--transition), color var(--transition), border-color var(--transition);
}

.transition-transform {
    transition: transform var(--transition);
}

.transition-opacity {
    transition: opacity var(--transition);
}

.transition-shadow {
    transition: box-shadow var(--transition);
}
```

---

## DARK MODE

**File: `css/dark-mode.css`** (Create new file)

```css
/* =========================================================
   DARK MODE SUPPORT
   ========================================================= */

@media (prefers-color-scheme: dark) {
    :root {
        --bg-primary: #1c1917;
        --bg-secondary: #292524;
        --text-primary: #f5f5f4;
        --text-secondary: #a8a29d;
        --border-color: #44403c;
        --light-bg: #1c1917;
        --white: #292524;
    }
    
    body {
        background: var(--bg-secondary);
        color: var(--text-primary);
    }
    
    .card,
    .modal-content,
    .dropdown-menu,
    .navbar {
        background: var(--bg-primary);
        color: var(--text-primary);
    }
    
    .form-control,
    .form-select,
    textarea,
    input[type="text"],
    input[type="email"],
    input[type="password"],
    input[type="number"],
    input[type="date"],
    input[type="time"] {
        background: var(--bg-secondary);
        color: var(--text-primary);
        border-color: var(--border-color);
    }
    
    .form-control:focus,
    .form-select:focus,
    textarea:focus {
        background: #3f3f3f;
        border-color: var(--primary);
        box-shadow: 0 0 0 3px rgba(26, 124, 74, 0.2);
    }
    
    .btn-secondary {
        color: var(--secondary);
        border-color: var(--secondary);
    }
    
    .btn-secondary:hover {
        background: rgba(212, 163, 115, 0.1);
    }
    
    .input-group-text {
        background: var(--bg-secondary);
        border-color: var(--border-color);
        color: var(--text-secondary);
    }
    
    hr {
        border-color: var(--border-color);
    }
    
    .text-muted {
        color: var(--text-secondary);
    }
    
    code {
        background: var(--bg-secondary);
        color: var(--secondary);
    }
    
    blockquote {
        color: var(--text-secondary);
        border-color: var(--primary);
    }
    
    /* Navbar in dark mode */
    .navbar-premium {
        background: rgba(28, 25, 23, 0.95);
        border-bottom-color: var(--border-color);
    }
    
    /* Table dark mode */
    table {
        color: var(--text-primary);
    }
    
    tbody tr {
        border-color: var(--border-color);
    }
    
    /* Alert dark mode */
    .alert {
        background: var(--bg-secondary);
        border-color: var(--border-color);
    }
}

/* Manual dark mode class */
body.dark-mode {
    --bg-primary: #1c1917;
    --bg-secondary: #292524;
    --text-primary: #f5f5f4;
    --text-secondary: #a8a29d;
    --border-color: #44403c;
    --light-bg: #1c1917;
    --white: #292524;
}
```

---

## ACCESSIBILITY CSS

**File: `css/accessibility.css`** (Create new file)

```css
/* =========================================================
   ACCESSIBILITY & INCLUSIVITY
   ========================================================= */

/* Skip to content link */
.skip-link {
    position: absolute;
    top: -40px;
    left: 0;
    background: var(--primary);
    color: white;
    padding: 8px 12px;
    text-decoration: none;
    z-index: 100;
    border-radius: 0 0 8px 0;
    font-weight: 600;
}

.skip-link:focus {
    top: 0;
}

/* Focus states for keyboard navigation */
a:focus,
button:focus,
input:focus,
select:focus,
textarea:focus,
[tabindex]:focus {
    outline: 3px solid var(--primary);
    outline-offset: 2px;
}

/* High contrast mode support */
@media (prefers-contrast: more) {
    :root {
        --border: #333;
        --text-primary: #000;
        --text-secondary: #333;
    }
    
    .btn {
        border: 2px solid currentColor;
    }
    
    a {
        text-decoration: underline;
    }
}

/* Reduced motion support */
@media (prefers-reduced-motion: reduce) {
    *,
    *::before,
    *::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        transition-duration: 0.01ms !important;
        scroll-behavior: auto !important;
    }
}

/* Color blind friendly alerts */
.alert {
    border-left: 4px solid currentColor;
    padding-left: 12px;
}

.alert-success {
    border-left-color: var(--success);
    background: rgba(22, 163, 74, 0.1);
}

.alert-error {
    border-left-color: var(--error);
    background: rgba(220, 38, 38, 0.1);
}

.alert-warning {
    border-left-color: var(--warning);
    background: rgba(234, 140, 85, 0.1);
}

.alert-info {
    border-left-color: var(--info);
    background: rgba(2, 132, 199, 0.1);
}

/* Visual focus indicator */
:focus-visible {
    outline: 3px solid var(--primary);
    outline-offset: 2px;
}

/* Improve form error visibility */
.form-control.is-invalid:focus {
    border-color: var(--error);
    box-shadow: 0 0 0 0.2rem rgba(220, 38, 38, 0.25);
}

.form-control.is-valid:focus {
    border-color: var(--success);
    box-shadow: 0 0 0 0.2rem rgba(22, 163, 74, 0.25);
}

/* Label association */
label {
    display: inline-block;
    margin-bottom: 0.5rem;
}

/* Heading levels for screen readers */
h1, h2, h3, h4, h5, h6 {
    margin-bottom: 1rem;
}

/* Icon descriptions */
.icon-only {
    font-size: 0;
}

.icon-only::before {
    content: attr(aria-label);
    font-size: 1rem;
}

/* Image captions */
figure {
    margin: 1.5rem 0;
    padding: 0;
}

figcaption {
    color: var(--muted);
    font-size: var(--text-sm);
    margin-top: 0.5rem;
    font-style: italic;
}

/* Table accessibility */
table {
    width: 100%;
    border-collapse: collapse;
}

th {
    text-align: left;
    font-weight: 600;
    background: var(--light-bg);
    padding: 12px;
    border-bottom: 2px solid var(--border);
}

td {
    padding: 12px;
    border-bottom: 1px solid var(--border);
}

/* List markers for screen readers */
ul, ol {
    list-style: revert;
}

/* Skip links for navigation */
nav a {
    display: block;
    padding: 8px 12px;
}

/* Breadcrumb navigation */
.breadcrumb {
    list-style: none;
    padding: 0;
    display: flex;
    gap: 8px;
    align-items: center;
}

.breadcrumb li::after {
    content: '/';
    margin-left: 8px;
    color: var(--muted);
}

.breadcrumb li:last-child::after {
    content: '';
}

/* Live region for announcements */
.sr-only,
.visually-hidden {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border-width: 0;
}

.sr-live {
    position: absolute;
    left: -10000px;
    width: 1px;
    height: 1px;
    overflow: hidden;
}
```

---

## CARD COMPONENTS

**File: `css/components.css`** (Create new file)

```css
/* =========================================================
   PREMIUM CARD COMPONENTS
   ========================================================= */

/* Base Card */
.card {
    background: white;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(0, 0, 0, 0.04);
    transition: all 0.3s cubic-bezier(0.23, 1, 0.320, 1);
    height: 100%;
}

.card:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}

/* Experience Card Premium */
.experience-card-premium {
    background: white;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(0, 0, 0, 0.04);
    transition: all 0.3s cubic-bezier(0.23, 1, 0.320, 1);
    height: 100%;
    position: relative;
    display: flex;
    flex-direction: column;
}

.experience-card-premium:hover {
    transform: translateY(-12px);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12);
}

.experience-card-premium .image-wrapper {
    position: relative;
    overflow: hidden;
    height: 280px;
    background: var(--light-bg);
}

.experience-card-premium .image-wrapper img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.6s cubic-bezier(0.23, 1, 0.320, 1);
}

.experience-card-premium:hover .image-wrapper img {
    transform: scale(1.08);
}

.category-badge {
    position: absolute;
    top: 12px;
    right: 12px;
    background: rgba(26, 124, 74, 0.9);
    color: white;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 11px;
    font-weight: 700;
    backdrop-filter: blur(10px);
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.rating-badge {
    position: absolute;
    bottom: 12px;
    left: 12px;
    background: white;
    padding: 8px 12px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    display: flex;
    align-items: center;
    gap: 6px;
}

.rating-badge .stars {
    color: #fbbf24;
}

.rating-badge .count {
    font-size: 12px;
    color: var(--muted);
}

.experience-card-premium .card-content {
    padding: 20px;
    flex: 1;
    display: flex;
    flex-direction: column;
}

.experience-card-premium h3 {
    font-size: 18px;
    font-weight: 700;
    margin-bottom: 8px;
    color: var(--dark);
}

.experience-card-premium .location {
    display: flex;
    align-items: center;
    gap: 6px;
    color: var(--muted);
    font-size: 13px;
    margin-bottom: 12px;
}

.experience-card-premium .location i {
    color: var(--primary);
}

.experience-card-premium .description {
    color: var(--muted);
    font-size: 14px;
    line-height: 1.5;
    margin-bottom: 14px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    flex: 1;
}

.experience-card-premium .footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 14px;
    border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.experience-card-premium .price {
    font-size: 20px;
    font-weight: 800;
    color: var(--primary);
}

.experience-card-premium .price .currency {
    font-size: 14px;
    font-weight: 600;
}

.experience-card-premium .wishlist {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: rgba(220, 38, 38, 0.1);
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    color: var(--muted);
    flex-shrink: 0;
}

.experience-card-premium .wishlist:hover {
    background: rgba(220, 38, 38, 0.2);
    color: var(--error);
    transform: scale(1.1);
}

.experience-card-premium .wishlist.active {
    background: rgba(220, 38, 38, 0.3);
    color: var(--error);
}
```

---

## IMPLEMENTATION CHECKLIST

### Quick Implementation Steps

- [ ] **Step 1:** Create new CSS files (typography, buttons, forms, animations, dark-mode, accessibility, components)
- [ ] **Step 2:** Import Google Fonts in style.css
- [ ] **Step 3:** Update :root variables with new color palette
- [ ] **Step 4:** Add @import statements for all new CSS files
- [ ] **Step 5:** Test buttons in all states (primary, secondary, hover, active, disabled, loading)
- [ ] **Step 6:** Test forms with validation states
- [ ] **Step 7:** Enable dark mode and test
- [ ] **Step 8:** Test accessibility features
- [ ] **Step 9:** Test on mobile devices
- [ ] **Step 10:** Performance optimization (minify CSS)

### File Structure After Implementation
```
frontend/
├── css/
│   ├── style.css (main - updated)
│   ├── responsive.css (existing)
│   ├── typography.css (NEW)
│   ├── buttons.css (NEW)
│   ├── forms.css (NEW)
│   ├── animations.css (NEW)
│   ├── dark-mode.css (NEW)
│   ├── accessibility.css (NEW)
│   └── components.css (NEW)
└── ... (other files)
```

---

**Document Version:** 1.0 | **Status:** Ready to Implement | **Estimated Time:** 6-8 hours

