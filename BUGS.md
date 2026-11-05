### BUG-001 : Invalid Check-in Date Format Accepted
API : GetBookingIds
- **Severity:** Major
- **Priority:** High
- **Method:** GET
- **Expected Format:** `YYYY-MM-DD`

**Request:**
https://restful-booker.herokuapp.com/booking?checkin=04-11-2026

**Expected:** 
1. When an invalid date format that is not specified in the documentation is entered, the system/API must reject it. In document CCYY-MM-DD is mentioned. 
2. Expected Response: HTTP 400 Bad Request with a validation error.

**Actual:** 
API accepts the invalid date format and returns booking IDs.

**Scenarios:**
1. Valid date: `2026-11-04`
2. Invalid format: `04-11-2026`
3. Invalid separator: `04/11/2026`
4. Invalid calendar date: `2026-02-30`