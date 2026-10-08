// =========================================================
// SKELETON SCREEN LOADER
// =========================================================

class SkeletonLoader {
  static createCardSkeleton() {
    return `
      <div class="skeleton-card" aria-busy="true">
        <div class="skeleton-image"></div>
        <div class="skeleton-content">
          <div class="skeleton-line" style="width: 80%;"></div>
          <div class="skeleton-line" style="width: 60%;"></div>
          <div class="skeleton-line" style="width: 40%;"></div>
        </div>
      </div>
    `;
  }

  static createListSkeleton(count = 3) {
    let html = '';
    for (let i = 0; i < count; i++) {
      html += this.createCardSkeleton();
    }
    return html;
  }

  static createExperienceCardSkeleton() {
    return `
      <div class="skeleton-card" aria-busy="true">
        <div class="skeleton-image" style="height: 250px;"></div>
        <div class="skeleton-content">
          <div class="skeleton-line" style="width: 90%;"></div>
          <div class="skeleton-line" style="width: 70%;"></div>
          <div class="skeleton-badge-line">
            <div class="skeleton-badge"></div>
            <div class="skeleton-badge"></div>
          </div>
        </div>
      </div>
    `;
  }

  static showSkeletons(containerId, count = 3, type = 'card') {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = '';
    let skeletonHtml = '';

    if (type === 'experience') {
      for (let i = 0; i < count; i++) {
        skeletonHtml += this.createExperienceCardSkeleton();
      }
    } else {
      skeletonHtml = this.createListSkeleton(count);
    }

    container.innerHTML = `<div class="skeleton-grid">${skeletonHtml}</div>`;
  }

  static hideSkeletons(containerId) {
    const container = document.getElementById(containerId);
    if (container) {
      const skeletons = container.querySelector('.skeleton-grid');
      if (skeletons) {
        skeletons.style.opacity = '0';
        skeletons.style.transition = 'opacity 0.3s ease';
        setTimeout(() => {
          skeletons.remove();
        }, 300);
      }
    }
  }
}

// Export for use
window.SkeletonLoader = SkeletonLoader;
