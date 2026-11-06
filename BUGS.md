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


### BUG-002 : HTTP 500 Internal Server Error When Invalid Check-in Value Is Passed

**API:** GetBookingIds  
- **Severity:** Major
- **Priority:** High
- **Method:** GET
- **Expected Format:** `YYYY-MM-DD`

**Request:**

https://restful-booker.herokuapp.com/booking?checkin=TestUserString

**Expected:**
1. When an invalid string value is passed in the `checkin` query parameter instead of a valid date, the system/API must reject the request gracefully.
2. HTTP 400 Bad Request with a proper validation error message instead of an internal server error.

**Actual:**
API returns **HTTP 500 Internal Server Error** when a string value (`TestUserString`) is passed in the `checkin` query parameter.

**Scenarios:**
2. Invalid string: `TestUserString`
3. Empty value: `checkin=`
4. Add special char: `#@#$`


BUG-003: HTTP 500 Internal Server Error for Impossible Calendar Date
- API: GetBookingIds
- Severity: Major
- Priority: High
- Method: GET
- Request: https://restful-booker.herokuapp.com/booking?checkin=2026-13-45
- Expected: HTTP 400 Bad Request with a proper validation error.
- Actual: HTTP 500 Internal Server Error for an impossible calendar date.

BUG-004: Unsupported Query Parameters Are Silently Ignored
- API: GetBookingIds
- Severity: Minor
- Priority: Medium
- Method: GET
- Request: https://restful-booker.herokuapp.com/booking?age=25&gender=male
- Expected: HTTP 400 Bad Request with a validation error for unsupported query parameters.
- Actual: API ignores unsupported parameters and returns all booking IDs with HTTP 200 OK.

### BUG-005: Invalid Data Type Accepted for Total Price

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** POST
- **Field:** `totalprice` (Expected: Numeric)

**Request:**
`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": "Test Check",
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-11-01",
    "checkout": "2026-11-10"
  },
  "additionalneeds": "Breakfast"
}
```

**Expected:**
1. API should reject a string value in the `totalprice` field because it expects numeric data.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**
API accepts the invalid string value and returns HTTP 200 OK with `totalprice: null` in the response. This behavior is not documented in the API documentation.

### BUG-006: Invalid Data Type Accepted for Deposit Paid

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** POST
- **Field:** `depositpaid` (Expected: Boolean)

**Request:**

`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 500,
  "depositpaid": "Test",
  "bookingdates": {
    "checkin": "2026-11-01",
    "checkout": "2026-11-10"
  },
  "additionalneeds": "Breakfast"
}
```

**Expected:**
1. API should reject a string value in the `depositpaid` field because it expects a Boolean (`true` or `false`).
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid string value `"Test"` and returns HTTP 200 OK, automatically setting `depositpaid` to `true`. This behavior is not documented in the API documentation.


### BUG-007: Invalid Check-in Date Format Accepted During Booking Creation

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** POST
- **Field:** `bookingdates.checkin` (Expected Format: `YYYY-MM-DD`)

**Request:**

`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 500,
  "depositpaid": "Test",
  "bookingdates": {
    "checkin": "Test invalid",
    "checkout": "2026-11-10"
  },
  "additionalneeds": "Breakfast"
}
```

**Expected:**
1. API should reject invalid or blank check-in date values.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid check-in date and returns HTTP 200 OK, converting the date into `"0NaN-aN-aN"` in the response. This behavior is not documented in the API documentation.

### BUG-008: Invalid Checkout Date Format Accepted During Booking Creation

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** POST
- **Field:** `bookingdates.checkout` (Expected Format: `YYYY-MM-DD`)

**Request:**

`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 500,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-11-01",
    "checkout": "Test User"
  },
  "additionalneeds": "Breakfast"
}
```

**Expected:**
1. API should reject invalid or blank checkout date values.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid checkout date and returns HTTP 200 OK, converting the date into `"0NaN-aN-aN"` in the response. This behavior is not documented in the API documentation.

### BUG-009: API Accepts Check-in Date Later Than Checkout Date

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** POST
- **Fields:** `bookingdates.checkin` and `bookingdates.checkout`

**Request:**

`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 500,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2028-11-01",
    "checkout": "2026-11-01"
  },
  "additionalneeds": "Breakfast"
}
```

**Expected:**
1. API should reject the booking when the check-in date is later than the checkout date.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid date sequence and returns HTTP 200 OK, successfully creating a booking where the check-in date is later than the checkout date.


### BUG-012: Invalid Numeric Value Accepted for Additional Needs

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** Medium
- **Method:** POST
- **Field:** `additionalneeds` (Expected: String)

**Request:**

`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 500,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-01-01",
    "checkout": "2026-01-01"
  },
  "additionalneeds": 503265
}
```

**Expected:**
1. API should reject numeric values in `additionalneeds` instead of accepting non-string data.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the numeric value and returns HTTP 200 OK, storing `additionalneeds` as a number instead of a string.

### BUG-013: API Accepts Invalid Checkout Date Sequence

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** POST
- **Field:** `bookingdates.checkout` (Expected Format: `YYYY-MM-DD`)

**Request:**

`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 500,
  "depositpaid": true,
  "bookingdates": {
    "checkout": "2026-01-01",
    "checkin": "2026-01-01"
  },
  "additionalneeds": "Breakfast"
}
```

**Expected:**
1. API should validate the checkout date and ensure it is later than the check-in date.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API returns HTTP 200 OK and accepts the booking even when the checkout date is equal to the check-in date. The date-validation behavior is not clearly specified in the API documentation.

### BUG-014: API Accepts Checkout Date Earlier Than Check-in Date

- **API:** CreateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** POST
- **Fields:** `bookingdates.checkin` and `bookingdates.checkout`

**Request:**

`POST https://restful-booker.herokuapp.com/booking`

```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 111,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2030-01-05",
    "checkout": "2026-01-01"
  },
  "additionalneeds": "Yes Pass"
}
```

**Expected:**
1. API should reject the booking when the checkout date is earlier than the check-in date.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API returns HTTP 200 OK and accepts the booking with an invalid date sequence. This validation behavior is not mentioned in the API documentation.

### BUG-015: API Accepts Request Without Content-Type Header

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PUT
- **Header:** `Content-Type: application/json`

**Request:**

`PUT https://restful-booker.herokuapp.com/booking/15`

**Request Body:**
```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 111,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2030-01-01",
    "checkout": "2026-01-01"
  },
  "additionalneeds": "Yes Pass"
}
```

**Expected:**
1. API documentation should clearly specify whether the `Content-Type` header is mandatory or optional.
2. If mandatory, the API should reject requests without it with HTTP 400 Bad Request or an appropriate 415 Unsupported Media Type response.

**Actual:**

API accepts the request without the `Content-Type` header and returns HTTP 200 OK. The optional/mandatory behavior of this header is not clearly specified in the API documentation.

### BUG-016: API Accepts Request Without Accept Header

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PUT
- **Header:** `Accept: application/json`

**Request:**

`PUT https://restful-booker.herokuapp.com/booking/15`

**Request Body:**
```json
{
  "firstname": "Akshay",
  "lastname": "Zankat",
  "totalprice": 111,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2030-01-01",
    "checkout": "2026-01-01"
  },
  "additionalneeds": "Yes Pass"
}
```

**Expected:**
1. API documentation should clearly specify whether the `Accept` header is mandatory or optional.
2. If mandatory, the API should return an appropriate error when the header is missing.

**Actual:**

API accepts the request without the `Accept` header and returns HTTP 200 OK. The mandatory/optional behavior of this header is not clearly specified in the API documentation.

### BUG-017: API Accepts Invalid Numeric Value for First Name

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Field:** `firstname` (Expected: String)

**Request:**

`PATCH https://restful-booker.herokuapp.com/booking/12`

```json
{
  "firstname": 500
}
```

**Expected:**
1. API should reject numeric values in the `firstname` field instead of accepting non-string data.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the numeric value `500` and returns HTTP 200 OK, updating the booking's first name with a number instead of a string.

### BUG-018: API Accepts Invalid Numeric Value for Last Name

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Field:** `lastname` (Expected: String)

**Request Body:**
```json
{
  "lastname": 26535
}
```

**Expected:**
1. API should reject numeric values in the `lastname` field instead of accepting non-string data.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the numeric value `26535` and returns HTTP 200 OK, updating the booking's last name with a number instead of a string.

### BUG-019: Invalid String Value Accepted for Total Price and Converted to Null

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Field:** `totalprice` (Expected: Numeric)

**Request Body:**
```json
{
  "totalprice": "Akshay"
}
```

**Expected:**
1. API should reject string values in the `totalprice` field instead of accepting non-numeric data.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API returns HTTP 200 OK and converts the invalid string value into `null` in the response instead of rejecting the request. This behavior is not clearly documented in the API documentation.

### BUG-020: Invalid String Value Accepted for Deposit Paid and Converted to True

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Field:** `depositpaid` (Expected: Boolean)

**Request Body:**
```json
{
  "depositpaid": "Akshay"
}
```

**Expected:**
1. API should reject string values in the `depositpaid` field instead of accepting non-Boolean data.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid string value `"Akshay"` and returns HTTP 200 OK, automatically converting it to `true`. This behavior is not clearly documented in the API documentation.

### BUG-021: Invalid Check-in Date Accepted During Booking Update

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Field:** `bookingdates.checkin` (Expected Format: `YYYY-MM-DD`)

**Request Body:**
```json
{
  "bookingdates": {
    "checkin": "Akshay",
    "checkout": "2026-01-01"
  }
}
```

**Expected:**
1. API should reject invalid check-in date values instead of accepting non-date strings.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid check-in value and returns HTTP 200 OK, converting it into `"0NaN-aN-aN"` in the response. This behavior is not documented in the API documentation.

### BUG-022: Invalid Checkout Date Accepted During Booking Update

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Field:** `bookingdates.checkout` (Expected Format: `YYYY-MM-DD`)

**Request Body:**
```json
{
  "bookingdates": {
    "checkin": "2026-01-01",
    "checkout": "Akshay"
  }
}
```

**Expected:**
1. API should reject invalid checkout date values instead of accepting non-date strings.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid checkout value and returns HTTP 200 OK, converting it into `"0NaN-aN-aN"` in the response. This behavior is not documented in the API documentation.

### BUG-023: Invalid Checkout Date Format Automatically Converted to Valid Date

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Field:** `bookingdates.checkout` (Expected Format: `YYYY-MM-DD`)

**Request Body:**
```json
{
  "bookingdates": {
    "checkin": "2026-01-01",
    "checkout": "2026@05/14"
  }
}
```

**Expected:**
1. API should reject invalid checkout date formats instead of automatically converting them.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid date format and returns HTTP 200 OK, automatically converting `"2026@05/14"` into `"2026-05-14"` in the response. This behavior is not clearly documented in the API documentation.

### BUG-024: API Accepts Checkout Date Earlier Than Check-in Date During Booking Update

- **API:** UpdateBooking
- **Severity:** Major
- **Priority:** High
- **Method:** PATCH
- **Fields:** `bookingdates.checkin` and `bookingdates.checkout`

**Request Body:**
```json
{
  "bookingdates": {
    "checkin": "2027-01-01",
    "checkout": "2026-01-01"
  }
}
```

**Expected:**
1. API should reject the booking update when the checkout date is earlier than the check-in date.
2. Expected Response: HTTP 400 Bad Request with a proper validation error.

**Actual:**

API accepts the invalid date sequence and returns HTTP 200 OK, allowing the checkout date to be earlier than the check-in date.