# 🎨 FRONTEND INTEGRATION GUIDE - 3 Critical Features

**Status:** Backend Implementation Complete  
**Next Step:** Frontend Integration  
**Estimated Time:** 12-16 hours (3-4 developers)

---

## 📋 INTEGRATION CHECKLIST

### Phase 1: Search & Filters (6-8 hours)
- [ ] Add filter panel to explore.html
- [ ] Implement filter dropdowns (state, location, category)
- [ ] Add price and duration sliders
- [ ] Connect to search API
- [ ] Display filtered results
- [ ] Add "Clear Filters" button
- [ ] Test on desktop & mobile

### Phase 2: Experience Packages (4-6 hours)
- [ ] Update experience detail page
- [ ] Display package cards
- [ ] Add package selection to booking form
- [ ] Update booking summary
- [ ] Test package pricing calculation
- [ ] Mobile responsive design

### Phase 3: Festival Calendar (4-6 hours)
- [ ] Create calendar.html page
- [ ] Add month navigation
- [ ] Display festivals by month
- [ ] Add state filter
- [ ] Link to experience details
- [ ] Mobile responsive design

---

## 🔌 API INTEGRATION DETAILS

### 1. SEARCH & FILTERS API

#### Get Filter Options (Load on Page Init)
```javascript
// Get all states for dropdown
fetch('/api/experiences/filter-options/states')
  .then(res => res.json())
  .then(data => {
    // data.states = ["Rajasthan", "Uttar Pradesh", ...]
    populateStateDropdown(data.states);
  });

// Get all locations for dropdown
fetch('/api/experiences/filter-options/locations')
  .then(res => res.json())
  .then(data => {
    // data.locations = ["Jaipur", "Mathura", ...]
    populateLocationDropdown(data.locations);
  });
```

#### Advanced Search with Filters
```javascript
function applyFilters() {
  const params = new URLSearchParams();
  
  if (titleInput.value) params.append('title', titleInput.value);
  if (stateSelect.value) params.append('state', stateSelect.value);
  if (locationSelect.value) params.append('location', locationSelect.value);
  if (categorySelect.value) params.append('categoryId', categorySelect.value);
  if (minPrice) params.append('minPrice', minPrice);
  if (maxPrice) params.append('maxPrice', maxPrice);
  if (minDuration) params.append('minDuration', minDuration);
  if (maxDuration) params.append('maxDuration', maxDuration);
  if (minGuests) params.append('minGuests', minGuests);
  
  fetch(`/api/experiences/search?${params}`)
    .then(res => res.json())
    .then(data => {
      // data.total = number of results
      // data.results = [{ id, title, price, duration, ... }]
      displayResults(data.results);
      updateResultCount(data.total);
    });
}
```

#### Quick Search
```javascript
function quickSearch(query) {
  fetch(`/api/experiences/quick-search?q=${encodeURIComponent(query)}`)
    .then(res => res.json())
    .then(data => {
      // data.results = matching experiences
      displayResults(data.results);
    });
}
```

#### HTML Structure
```html
<!-- Filter Panel -->
<div class="filter-panel">
  <h3>Filters</h3>
  
  <!-- Search Input -->
  <div class="filter-group">
    <label>Search Festival/Experience</label>
    <input type="text" id="titleSearch" placeholder="e.g., Holi">
  </div>
  
  <!-- State Filter -->
  <div class="filter-group">
    <label>State</label>
    <select id="stateFilter">
      <option value="">All States</option>
      <!-- Options loaded from API -->
    </select>
  </div>
  
  <!-- Location Filter -->
  <div class="filter-group">
    <label>Location</label>
    <select id="locationFilter">
      <option value="">All Locations</option>
      <!-- Options loaded from API -->
    </select>
  </div>
  
  <!-- Category Filter -->
  <div class="filter-group">
    <label>Category</label>
    <select id="categoryFilter">
      <option value="">All Categories</option>
      <!-- Categories already exist -->
    </select>
  </div>
  
  <!-- Price Range -->
  <div class="filter-group">
    <label>Price Range: ₹<span id="priceDisplay">0</span> - ₹<span id="maxPriceDisplay">100000</span></label>
    <input type="range" id="minPrice" min="0" max="50000" value="0">
    <input type="range" id="maxPrice" min="0" max="100000" value="100000">
  </div>
  
  <!-- Duration Range -->
  <div class="filter-group">
    <label>Duration: <span id="durationDisplay">1</span> - <span id="maxDurationDisplay">30</span> days</label>
    <input type="range" id="minDuration" min="1" max="15" value="1">
    <input type="range" id="maxDuration" min="1" max="30" value="30">
  </div>
  
  <!-- Guest Capacity -->
  <div class="filter-group">
    <label>Minimum Guests</label>
    <input type="number" id="minGuests" min="1" value="1">
  </div>
  
  <!-- Buttons -->
  <button onclick="applyFilters()" class="btn-primary">Apply Filters</button>
  <button onclick="clearFilters()" class="btn-secondary">Clear Filters</button>
</div>

<!-- Results Area -->
<div class="results-area">
  <div class="result-count">
    Showing <span id="resultCount">0</span> experiences
  </div>
  <div id="resultsList" class="results-grid">
    <!-- Results displayed here -->
  </div>
</div>
```

---

### 2. EXPERIENCE PACKAGES API

#### Get Packages for Experience
```javascript
// On experience detail page
function loadPackages(experienceId) {
  fetch(`/api/experiences/${experienceId}/packages`)
    .then(res => res.json())
    .then(data => {
      // data.total = number of packages
      // data.packages = [
      //   {
      //     id: 1,
      //     name: "3-Day Experience",
      //     description: "...",
      //     price: 5000,
      //     duration: 3,
      //     maxGuests: 8,
      //     included: "...",
      //     notIncluded: "...",
      //     createdAt: "2026-10-09T..."
      //   }
      // ]
      displayPackages(data.packages);
    });
}
```

#### Display Packages
```html
<!-- Experience Detail Page -->
<div class="packages-section">
  <h2>Choose Your Package</h2>
  <div class="packages-grid" id="packagesContainer">
    <!-- Packages loaded here -->
  </div>
</div>

<!-- Package Card Template (JavaScript) -->
<script>
function createPackageCard(pkg) {
  return `
    <div class="package-card">
      <div class="package-header">
        <h3>${pkg.name}</h3>
        <span class="duration">${pkg.duration} days</span>
      </div>
      
      <p class="description">${pkg.description}</p>
      
      <div class="package-details">
        <div class="detail-item">
          <span class="label">Duration:</span>
          <span class="value">${pkg.duration} days</span>
        </div>
        <div class="detail-item">
          <span class="label">Max Guests:</span>
          <span class="value">${pkg.maxGuests}</span>
        </div>
        <div class="detail-item">
          <span class="label">Price per person:</span>
          <span class="value price">₹${pkg.price.toLocaleString()}</span>
        </div>
      </div>
      
      <div class="inclusions">
        <h4>Included:</h4>
        <ul>
          ${pkg.included.split('\n').map(item => `<li>${item}</li>`).join('')}
        </ul>
      </div>
      
      <div class="exclusions">
        <h4>Not Included:</h4>
        <ul>
          ${pkg.notIncluded.split('\n').map(item => `<li>${item}</li>`).join('')}
        </ul>
      </div>
      
      <button onclick="selectPackage(${pkg.id}, '${pkg.name}', ${pkg.price}, ${pkg.duration})" class="btn-primary">
        Select This Package
      </button>
    </div>
  `;
}
</script>
```

#### Update Booking Form
```javascript
function selectPackage(packageId, packageName, price, duration) {
  // Store selected package
  selectedPackage = {
    id: packageId,
    name: packageName,
    price: price,
    duration: duration
  };
  
  // Update booking form
  document.getElementById('selectedPackage').value = packageName;
  document.getElementById('packagePrice').value = price;
  document.getElementById('bookingDuration').value = duration;
  
  // Update total price calculation
  updateTotalPrice();
  
  // Highlight selected package
  document.querySelectorAll('.package-card').forEach(card => {
    card.classList.remove('selected');
  });
  event.target.closest('.package-card').classList.add('selected');
}

function updateTotalPrice() {
  const packagePrice = selectedPackage.price;
  const numberOfPeople = parseInt(document.getElementById('numberOfPeople').value) || 1;
  const totalPrice = packagePrice * numberOfPeople;
  
  document.getElementById('totalPrice').textContent = totalPrice.toLocaleString();
}
```

#### Update Booking API Call
```javascript
function submitBooking() {
  const bookingData = {
    experienceId: experienceId,
    packageId: selectedPackage.id,  // NEW: Send selected package
    numberOfPeople: document.getElementById('numberOfPeople').value,
    bookingDate: document.getElementById('bookingDate').value,
    specialRequests: document.getElementById('specialRequests').value
  };
  
  fetch('/api/bookings', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(bookingData)
  })
  .then(res => res.json())
  .then(data => {
    // Show booking confirmation
    showBookingConfirmation(data);
  });
}
```

---

### 3. FESTIVAL CALENDAR API

#### Get Festivals by Month
```javascript
function loadCalendarMonth(month, year) {
  fetch(`/api/calendar/festivals/month/${month}/year/${year}`)
    .then(res => res.json())
    .then(data => {
      // data.total = number of festivals
      // data.festivals = [
      //   {
      //     id: 1,
      //     experienceTitle: "Holi in Mathura",
      //     date: "2026-03-25",
      //     dayOfMonth: 25,
      //     monthValue: 3,
      //     year: 2026,
      //     dayOfWeek: "WEDNESDAY",
      //     isFestivalDate: true,
      //     festivalName: "Holi",
      //     availableSlots: 10,
      //     totalSlots: 10
      //   }
      // ]
      displayCalendarMonth(data.festivals);
    });
}
```

#### Get Festivals by State & Month
```javascript
function loadFestivalsByState(state, month) {
  fetch(`/api/calendar/festivals/state/${state}/month/${month}`)
    .then(res => res.json())
    .then(data => {
      displayFestivals(data.festivals);
    });
}
```

#### Get All Festival States
```javascript
function loadFestivalStates() {
  fetch('/api/calendar/festival-states')
    .then(res => res.json())
    .then(data => {
      // data.states = ["Rajasthan", "Uttar Pradesh", ...]
      populateStateFilter(data.states);
    });
}
```

#### Calendar HTML Structure
```html
<!-- Calendar Page -->
<div class="calendar-container">
  <h1>Festival Calendar</h1>
  
  <!-- Month Navigation -->
  <div class="calendar-header">
    <button onclick="previousMonth()" class="btn-nav">← Previous</button>
    <h2 id="currentMonth">March 2026</h2>
    <button onclick="nextMonth()" class="btn-nav">Next →</button>
  </div>
  
  <!-- State Filter -->
  <div class="state-filter">
    <label>Filter by State:</label>
    <select id="stateFilter" onchange="filterByState()">
      <option value="">All States</option>
      <!-- Options loaded from API -->
    </select>
  </div>
  
  <!-- Festivals List -->
  <div class="festivals-list" id="festivalsList">
    <!-- Festivals loaded here -->
  </div>
</div>

<!-- Festival Card Template -->
<div class="festival-card">
  <div class="festival-date">
    <div class="day">25</div>
    <div class="month">MAR</div>
  </div>
  
  <div class="festival-info">
    <h3>Holi in Mathura</h3>
    <p class="festival-name">Festival: <strong>Holi</strong></p>
    <p class="state">Location: <strong>Mathura, Uttar Pradesh</strong></p>
    <p class="availability">
      Slots: <strong id="slots">10</strong> available
    </p>
  </div>
  
  <div class="festival-actions">
    <a href="/experience.html?id=1" class="btn-primary">View Details</a>
    <button onclick="bookFestival(1)" class="btn-secondary">Book Now</button>
  </div>
</div>
```

#### JavaScript Implementation
```javascript
let currentMonth = new Date().getMonth() + 1;
let currentYear = new Date().getFullYear();
let filteredState = '';

function loadCalendar() {
  loadFestivalStates();
  displayMonth();
}

function displayMonth() {
  const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
                     'July', 'August', 'September', 'October', 'November', 'December'];
  document.getElementById('currentMonth').textContent = 
    `${monthNames[currentMonth - 1]} ${currentYear}`;
  
  // Load festivals for this month
  if (filteredState) {
    loadFestivalsByState(filteredState, currentMonth);
  } else {
    loadCalendarMonth(currentMonth, currentYear);
  }
}

function previousMonth() {
  currentMonth--;
  if (currentMonth < 1) {
    currentMonth = 12;
    currentYear--;
  }
  displayMonth();
}

function nextMonth() {
  currentMonth++;
  if (currentMonth > 12) {
    currentMonth = 1;
    currentYear++;
  }
  displayMonth();
}

function filterByState() {
  filteredState = document.getElementById('stateFilter').value;
  displayMonth();
}

function displayFestivals(festivals) {
  const container = document.getElementById('festivalsList');
  container.innerHTML = '';
  
  if (festivals.length === 0) {
    container.innerHTML = '<p class="no-results">No festivals found for this month.</p>';
    return;
  }
  
  festivals.forEach(festival => {
    const card = document.createElement('div');
    card.className = 'festival-card';
    card.innerHTML = `
      <div class="festival-date">
        <div class="day">${festival.dayOfMonth}</div>
        <div class="month">${getMonthShort(festival.monthValue)}</div>
      </div>
      
      <div class="festival-info">
        <h3>${festival.experienceTitle}</h3>
        <p class="festival-name">Festival: <strong>${festival.festivalName || 'N/A'}</strong></p>
        <p class="day-of-week">${festival.dayOfWeek}</p>
        <p class="availability">
          ${festival.availableSlots > 0 
            ? `<span class="available">✓ ${festival.availableSlots} slots available</span>` 
            : '<span class="full">Full</span>'}
        </p>
      </div>
      
      <div class="festival-actions">
        <a href="/experience.html?id=${festival.experienceId}" class="btn-primary">View Details</a>
        ${festival.availableSlots > 0 
          ? `<button onclick="bookFestival(${festival.experienceId})" class="btn-secondary">Book Now</button>`
          : '<button disabled class="btn-disabled">Fully Booked</button>'}
      </div>
    `;
    container.appendChild(card);
  });
}

function getMonthShort(month) {
  const months = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN',
                  'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];
  return months[month - 1];
}
```

---

## 🎨 CSS STYLING RECOMMENDATIONS

### Filter Panel
```css
.filter-panel {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.filter-group {
  margin-bottom: 15px;
}

.filter-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.filter-group select,
.filter-group input[type="text"],
.filter-group input[type="number"],
.filter-group input[type="range"] {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
```

### Package Cards
```css
.packages-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin: 20px 0;
}

.package-card {
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.package-card:hover {
  border-color: #2196F3;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.package-card.selected {
  border-color: #4CAF50;
  background: #e8f5e9;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #2196F3;
}
```

### Calendar
```css
.festival-card {
  display: flex;
  gap: 20px;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.festival-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.festival-date {
  text-align: center;
  font-weight: bold;
  min-width: 70px;
}

.day {
  font-size: 28px;
  color: #2196F3;
}

.month {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.available {
  color: #4CAF50;
  font-weight: bold;
}

.full {
  color: #f44336;
  font-weight: bold;
}
```

---

## 📱 RESPONSIVE DESIGN

### Mobile Considerations
```css
@media (max-width: 768px) {
  /* Filter Panel */
  .filter-panel {
    padding: 15px;
  }
  
  /* Packages Grid */
  .packages-grid {
    grid-template-columns: 1fr;
  }
  
  /* Festival Card */
  .festival-card {
    flex-direction: column;
  }
  
  /* Calendar */
  .calendar-header button {
    width: 45px;
    padding: 8px;
  }
}
```

---

## 🧪 TESTING CHECKLIST

### Search & Filters
- [ ] All filters work independently
- [ ] Multiple filters work together
- [ ] Price range validation (min < max)
- [ ] Duration range validation
- [ ] Clear filters button works
- [ ] Responsive on mobile
- [ ] No JavaScript errors in console

### Packages
- [ ] Packages display correctly
- [ ] Can select different packages
- [ ] Price updates when package selected
- [ ] Booking form captures package ID
- [ ] Mobile responsive

### Calendar
- [ ] Month navigation works
- [ ] Festivals display by month
- [ ] State filter works
- [ ] Can click to view details
- [ ] Mobile responsive
- [ ] No date formatting issues

---

## ⚠️ COMMON ISSUES & SOLUTIONS

### Issue: Filters not updating results
**Solution:** Ensure apply filters button calls correct API endpoint with all parameters

### Issue: Package price not calculating correctly
**Solution:** Verify price is multiplied by number of people before displaying

### Issue: Calendar dates showing wrong month
**Solution:** Check month value is 1-12, not 0-11

### Issue: Mobile layout broken
**Solution:** Add CSS media queries, ensure flex/grid layouts are responsive

---

## 📞 BACKEND ENDPOINTS SUMMARY

### All Available Endpoints
```
SEARCH & FILTERS:
GET  /api/experiences/search
GET  /api/experiences/quick-search
GET  /api/experiences/filter/price
GET  /api/experiences/filter/duration
GET  /api/experiences/filter/state
GET  /api/experiences/filter/location
GET  /api/experiences/filter/categories
GET  /api/experiences/filter/guests
GET  /api/experiences/filter-options/states
GET  /api/experiences/filter-options/locations

PACKAGES:
GET  /api/packages/{id}
GET  /api/experiences/{id}/packages
POST /api/packages
PUT  /api/packages/{id}
DELETE /api/packages/{id}

CALENDAR:
POST /api/calendar/available-dates
GET  /api/calendar/experiences/{id}/available-dates
GET  /api/calendar/experiences/{id}/month/{m}/year/{y}
GET  /api/calendar/festivals/month/{month}
GET  /api/calendar/festivals/month/{m}/year/{y}
GET  /api/calendar/festivals/state/{state}/month/{m}
GET  /api/calendar/festivals/state/{state}
GET  /api/calendar/month/{month}/view
GET  /api/calendar/festivals/upcoming
GET  /api/calendar/festival-states
PUT  /api/calendar/available-dates/{id}
DELETE /api/calendar/available-dates/{id}
GET  /api/calendar/statistics
```

---

## 🚀 DEPLOYMENT STEPS

1. Backend: Deploy Spring Boot application
2. Database: Run migrations for new tables
3. Frontend: Update HTML pages with new sections
4. Frontend: Add JavaScript integration code
5. Frontend: Add CSS styling
6. Testing: Run full test suite
7. Launch: Deploy to production

---

**This guide provides everything needed for frontend developers to integrate all 3 features.**

**Questions? Check the API responses in the backend code - each endpoint returns detailed JSON.**
