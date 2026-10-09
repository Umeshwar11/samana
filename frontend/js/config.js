// Configuration file for API endpoints
// This allows switching between local and cloud deployments

(function() {
  // Detect environment and set API URL
  const getBackendUrl = () => {
    // If running on localhost
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
      return 'http://localhost:8080';
    }
    
    // If running on Cloud Run, use the same domain structure
    // Replace 'frontend' with 'backend' in the URL
    const currentUrl = window.location.origin;
    if (currentUrl.includes('samana-frontend')) {
      return currentUrl.replace('samana-frontend', 'samana-backend');
    }
    
    // Default fallback
    return 'https://samana-backend-82716904797.asia-south1.run.app';
  };

  // Set global API configuration
  window.API_CONFIG = {
    BACKEND_URL: getBackendUrl(),
    BASE_URL: getBackendUrl() + '/api',
    timeout: 30000,
    
    // Helper function to make API calls
    async fetch(endpoint, options = {}) {
      const url = this.BASE_URL + endpoint;
      try {
        const response = await fetch(url, {
          ...options,
          headers: {
            'Content-Type': 'application/json',
            ...options.headers
          }
        });
        
        if (!response.ok) {
          throw new Error(`API Error: ${response.status}`);
        }
        
        return await response.json();
      } catch (error) {
        console.error('API call failed:', error);
        throw error;
      }
    }
  };

  console.log('API Configuration loaded');
  console.log('Backend URL:', window.API_CONFIG.BACKEND_URL);
})();
