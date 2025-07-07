# NannyTally

NannyTally is a simple web application designed to track the hours worked by our nanny, Ju. It provides a clean interface to log daily start and end times, calculate total hours, and view a summary for the week.

## Features

*   **Log Time Entries:** Easily add new entries with date, start time, and end time.
*   **Automatic Calculation:** Automatically calculates the total hours for each entry and provides a running total for the week.
*   **Notes:** Add optional notes to any entry (e.g., "stayed late").
*   **Simple Interface:** A clean, minimalist UI for quick data entry and review.
*   **Delete Entries:** Remove entries with a single click.

## Tech Stack

*   **Backend:** Clojure
*   **Web Server:** Ring with Jetty
*   **Routing:** Compojure
*   **Database:** SQLite (via next.jdbc)
*   **HTML Generation:** Hiccup
*   **Frontend Interactivity:** HTMX
*   **Styling:** Pico.css
*   **Task Runner:** just

## Prerequisites

Before you begin, ensure you have the following installed:

*   [Clojure CLI tools](https://clojure.org/guides/getting_started)
*   [just](https://github.com/casey/just) (a command runner)

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url>
    cd nannytally
    ```

2.  **Install Dependencies:**
    Run the `prepare-deps` command to download the necessary Java and Clojure libraries.
    ```bash
    just prepare-deps
    ```

3.  **Run the Application:**
    Start the web server in development mode. This will enable features like automatic code reloading.
    ```bash
    just dev
    ```
    The application will be running at [http://localhost:3000](http://localhost:3000).

## Usage

1.  **Open the application** in your web browser.
2.  Use the **"Add New Entry"** form to input the date, start time, and end time.
3.  Add any relevant information in the **"Notes"** field.
4.  Click **"Add Entry"** to save it. The entries table will update automatically.
5.  To remove an entry, click the **"Delete"** button in the corresponding row.

## Running Tests

To run the test suite, use the `kaocha` test runner with the defined alias:

```bash
clj -A:kaocha
```
