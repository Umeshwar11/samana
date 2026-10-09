# 🎯 SAMANA PROJECT - PRIORITY ACTION ITEMS

**Status:** Ready for Implementation  
**Overall Completion:** ~70% (need 30% more to reach 100%)

---

## 🔴 CRITICAL - MUST FIX (Block Public Launch)

### 1. **Experience Packages System** 🔴
**Impact:** HIGH - Users cannot book different package durations  
**Requirement:** Must Have for MVP  
**Time Estimate:** 4-6 hours

#### Problem
Currently:
- Each experience has ONE price, ONE duration
- Cannot offer 1-day, 3-day, 7-day packages
- Users cannot compare package options

#### Solution Needed
Create a Package system with:
```
New Entity: Package
├── id (Long)
├── name (String) - "3-Day Experience"
├── description (String)
├── price (Double)
├── duration (Integer)
├── maxGuests (Integer)
├── included (TEXT)
├── notIncluded (TEXT)
└── experience_id (FK to Experience)

Relationship: Experience 1→N Packages
```

#### Implementation Steps
1. Create `Package.java` entity with JPA annotations
2. Add `List<Package> packages` to `Experience.java`
3. Create `PackageRepository.java`
4. Create `PackageController.java` (CRUD endpoints)
5. Create `PackageService.java` (business logic)
6. Update `ExperienceResponse.java` DTO to include packages
7. Update booking flow to select package
8. Update frontend to show package options

#### Acceptance Criteria
- [ ] Experience detail page shows 3+ package options
- [ ] Users can select different package durations
- [ ] Booking shows selected package details
- [ ] Admin can manage packages per experience
- [ ] Package pricing is correctly calculated

---

### 2. **Search & Filters System** 🔴
**Impact:** HIGH - Users cannot discover experiences  
**Requirement:** Must Have for MVP  
**Time Estimate:** 6-8 hours

#### Problem
Currently:
- Only shows all experiences
- Basic sorting (price, duration) only
- NO filtering by:
  - Festival/experience name
  - Location/state/city
  - Experience category
  - Price range
  - Duration range
  - Number of guests

#### Solution Needed

**Backend Changes:**
```
1. Update ExperienceRepository with new query methods:
   - findByLocationContainingIgnoreCase(String location)
   - findByStateContainingIgnoreCase(String state)
   - findByPriceBetween(Double min, Double max)
   - findByDurationBetween(Integer min, Integer max)
   - findByMaxGuestsGreaterThanOrEqual(Integer guests)
   - findByTitleContainingIgnoreCase(String title)
   - findByCategoryIdIn(List<Long> categoryIds)

2. Update ExperienceController with new endpoint:
   GET /api/experiences/search?
     festival={name}&
     state={state}&
     location={location}&
     category={categoryId}&
     price_min={min}&
     price_max={max}&
     duration_min={min}&
     duration_max={max}&
     guests={count}&
     sort={field}&
     order={asc|desc}

3. Create ExperienceFilterService for complex queries
```

**Frontend Changes:**
```
1. Add Filter Panel to explore.html with:
   - Festival name search input
   - Location dropdown/autocomplete
   - State dropdown
   - Category checkboxes
   - Price range slider
   - Duration range slider
   - Guest count selector

2. Update explore.js:
   - Read filter values
   - Build query string
   - Call new /api/experiences/search endpoint
   - Display filtered results
   - Update result count

3. Persist filter selections in browser history
```

#### Implementation Steps
1. Add query methods to `ExperienceRepository.java`
2. Create `ExperienceSearchService.java`
3. Add `/search` endpoint to `ExperienceController.java`
4. Add filter UI to `explore.html`
5. Update `explore.js` to capture & send filter values
6. Test all filter combinations
7. Add clear filters button

#### Acceptance Criteria
- [ ] Filter by festival name works
- [ ] Filter by state/location works
- [ ] Filter by category works
- [ ] Filter by price range works
- [ ] Filter by duration works
- [ ] Multiple filters work together
- [ ] Results update real-time
- [ ] Filter panel responsive on mobile

---

### 3. **Festival Calendar** 🔴
**Impact:** MEDIUM-HIGH - Important for festival discovery  
**Requirement:** Should Have for MVP  
**Time Estimate:** 8-10 hours

#### Problem
Currently:
- No festival/event dates stored
- No calendar view
- No way to see upcoming festivals by month
- No availability dates per experience

#### Solution Needed

**Backend Changes:**
```
1. Update Experience entity with:
   - festivalDate (LocalDate) - When festival occurs
   - festivalMonth (Integer) - 1-12 for calendar grouping
   - isRecurringAnnually (Boolean)

2. Create AvailableDate entity:
   - id (Long)
   - experienceId (Long)
   - date (LocalDate)
   - available_slots (Integer)

3. Create CalendarController with endpoints:
   GET /api/calendar/month/{month}
   - Returns all festivals for a month
   
   GET /api/experiences/{id}/calendar
   - Returns available dates for an experience

4. Add repository methods for date queries
```

**Frontend Changes:**
```
1. Create calendar.html page with:
   - Month navigation (Jan-Dec)
   - Festival list for selected month
   - Click to view experience details
   - Mobile-friendly design

2. Update explore.html with:
   - Date picker for filtering
   - "View on Calendar" button

3. Create calendar.js with:
   - Fetch festivals for selected month
   - Display in calendar grid
   - Handle month navigation
```

#### Implementation Steps
1. Add date fields to `Experience.java`
2. Create `AvailableDate.java` entity
3. Create `CalendarController.java`
4. Create `CalendarService.java`
5. Add date query methods to repositories
6. Create `calendar.html` page
7. Create `calendar.js` for logic
8. Add calendar link to navigation
9. Integrate with search filters

#### Acceptance Criteria
- [ ] Calendar shows all 12 months
- [ ] Can navigate between months
- [ ] Festivals grouped by month displayed
- [ ] Can click festival to see details
- [ ] Availability dates shown
- [ ] Responsive on mobile
- [ ] No JavaScript errors

---

## 🟡 IMPORTANT - SHOULD DO (High Value)

### 4. **Content Creator Dashboard** 🟡
**Impact:** MEDIUM - Enables content creators  
**Requirement:** Nice to Have for MVP  
**Time Estimate:** 10-15 hours

#### Problem
Currently:
- Hosts and creators use same system
- No creator-specific features
- No analytics for creators
- No content management tools

#### Needed Features
- Creator registration flow
- Creator verification badge
- Analytics dashboard (views, bookings, revenue)
- Content management tools
- Social media integration
- Creator earnings report

#### Implementation Steps
1. Add creator role to User entity
2. Create CreatorProfile entity
3. Update HostProfile for creator-specific fields
4. Create CreatorController
5. Create analytics service
6. Build creator dashboard page
7. Add creator verification flow

---

### 5. **Advanced Host Dashboard** 🟡
**Impact:** MEDIUM - Better host management  
**Requirement:** Enhancement  
**Time Estimate:** 6-8 hours

#### Improvements Needed
- Experience analytics
- Booking management interface
- Photo gallery management
- Profile completion percentage
- Reviews & ratings dashboard
- Earnings overview

---

## 🟢 NICE TO HAVE - LATER (Lower Priority)

### 6. **Product/Service Marketplace** 🟢
**Impact:** LOW - Future expansion  
**Requirement:** Out of Scope for MVP  
**Time Estimate:** 20-30 hours (Future Sprint)

#### This is future scope - do NOT implement now
- Focus on experience/festival marketplace first
- Product marketplace can be added in Phase 2
- Current architecture supports future expansion

---

## 📊 EFFORT & TIMELINE

### Quick Wins (Can do in parallel)
```
✅ Packages System:        4-6 hours  (Dev A)
✅ Search & Filters:       6-8 hours  (Dev B)
✅ Calendar System:        8-10 hours (Dev C)

TOTAL CRITICAL:            18-24 hours = 2-3 days
```

### Following Phase
```
⚠️ Creator Dashboard:      10-15 hours
⚠️ Advanced Host Tools:    6-8 hours

TOTAL ENHANCEMENT:         16-23 hours = 2-3 days
```

### Recommended Schedule
```
WEEK 1:
- Mon: Sprint planning & setup
- Tue-Thu: Implement 3 critical features (parallel)
- Fri: Testing & bug fixes
- Done by Friday EOD: Packages, Filters, Calendar

WEEK 2:
- Mon-Wed: Creator & Host enhancements
- Thu-Fri: Testing, deployment prep
- Done by Friday EOD: Analytics & Management tools

WEEK 3:
- Deploy to production
- User testing
- Gather feedback
```

---

## ✅ CURRENT STRENGTHS (Don't Break!)

✅ Core Features Working
- Experience discovery & booking ✅
- Host management system ✅
- User registration & auth ✅
- Review & rating system ✅
- Payment foundation ✅

✅ UI/UX Excellent
- Premium design system ✅
- Dark mode support ✅
- Responsive design ✅
- WCAG 2.1 AA accessibility ✅
- Professional appearance ✅

✅ Infrastructure Solid
- Spring Boot backend ✅
- PostgreSQL database ✅
- Docker support ✅
- Google Cloud deployment ✅

**Keep these working while adding new features!**

---

## 🚀 SUCCESS METRICS

After implementing all 3 critical features:

```
✅ Users can discover experiences by month (Calendar)
✅ Users can filter/search for specific experiences (Filters)
✅ Users can book different package durations (Packages)
✅ Complete MVP feature set (Requirements A-F fully met)
✅ Ready for public launch
```

---

## 📋 IMMEDIATE ACTION CHECKLIST

**This Week:**
- [ ] Review this action items list
- [ ] Assign developers to each feature
- [ ] Create Jira/GitHub tickets
- [ ] Setup feature branches
- [ ] Schedule standups
- [ ] Begin implementation Mon morning

**Dev Tasks:**
- [ ] Dev A: Experience Packages
- [ ] Dev B: Search & Filters  
- [ ] Dev C: Festival Calendar

**Testing:**
- [ ] QA: Create test cases for each feature
- [ ] QA: Manual testing on desktop/mobile
- [ ] QA: Security testing

**Deployment:**
- [ ] DevOps: Prepare staging environment
- [ ] DevOps: Database migration scripts
- [ ] DevOps: Deployment checklist

---

## 💬 QUESTIONS FOR TEAM LEADS

1. **Can we assign 2-3 developers for this?** → Parallel implementation needed
2. **What's the hard deadline for MVP?** → Affects sprint planning
3. **Should we launch with packages or without?** → Critical for MVP
4. **Do we need creator analytics immediately?** → Phase 2 or Phase 1?
5. **What's the deployment strategy?** → Blue-green, canary, or direct?

---

## 📞 SUPPORT NEEDED

- [ ] Design review for calendar UI
- [ ] Product feedback on package presentation
- [ ] Marketing input on creator features
- [ ] Legal review of content rights
- [ ] Security audit before launch

---

**Prepared By:** Claude Haiku 4.5  
**Date:** 2026-10-09  
**Status:** Ready for Implementation

