# Project Roadmap

This document outlines the planned features and improvements for the NannyTally application.

## V1 - Core Functionality

*   [x] Log time entries (date, start/end time, notes)
*   [x] Automatically calculate total hours per entry
*   [x] View a running total of hours for the week
*   [x] Delete individual entries
*   [x] Basic UI with HTMX for interactivity

## V2 - Upcoming Features

### 1. Edit Entries

*   **Goal:** Allow users to modify existing time entries without having to delete and recreate them.
*   **Implementation Ideas:**
    *   Add an "Edit" button to each entry row.
    *   Clicking "Edit" could replace the row with an inline form, pre-filled with the entry's data.
    *   Alternatively, it could open a modal window with the edit form.
    *   A `PUT` or `PATCH` request would be sent to an `/entries/:id` endpoint to update the data.

### 2. Weekly Pagination

*   **Goal:** Navigate between different weeks to view past and future entries.
*   **Implementation Ideas:**
    *   Add "Previous Week" and "Next Week" buttons to the main view.
    *   The backend will need to handle a date parameter to fetch entries for the requested week.
    *   The UI will update via HTMX to show the selected week's data.
    *   The current week should be clearly displayed.

### 3. CSV Export

*   **Goal:** Allow users to download the current week's time entries as a CSV file for record-keeping or sharing.
*   **Implementation Ideas:**
    *   Add a "Download CSV" button to the page.
    *   Create a new endpoint (e.g., `/entries/csv`) that generates a CSV file from the current week's data.
    *   The response should have the appropriate `Content-Type` (`text/csv`) and `Content-Disposition` headers to trigger a file download.

### 4. Comprehensive Testing

*   **Goal:** Increase test coverage to ensure the application is robust and reliable.
*   **Implementation Ideas:**
    *   **Unit Tests:** Write tests for individual functions, especially the business logic in the `models` and `handlers` namespaces (e.g., hour calculation, database interactions).
    *   **Integration Tests:** Test the full request/response cycle to ensure handlers and views work together correctly.
    *   **UI/End-to-End Tests:** Consider using a tool to simulate user interactions and verify that the frontend behaves as expected (this might be a lower priority for a simple app).
    *   Use the `kaocha` test runner for executing the tests.
