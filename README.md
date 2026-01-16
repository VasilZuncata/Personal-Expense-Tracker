# Personal Expense Tracker (Java Swing with FlatLaf)

Personal Expense Tracker is a Java desktop application for managing personal expenses through a graphical user interface.
The application is built using **Java Swing**, enhanced with **FlatLaf** to provide a modern and clean look and feel.

The project was developed as a **team-based Java application**, focusing on clean architecture, separation of responsibilities, and basic object-oriented programming principles.

---

## Features

* Add new expenses (category, amount, note)
* View all expenses in a list
* Delete selected expenses
* Automatically calculate:

  * Total expenses
  * Average expense
* Category-based expense summary
* Input validation for incorrect values
* Save and load expenses from files
* Modern graphical desktop interface (no terminal interaction)

---

## Technologies Used

* Java
* Java Swing
* FlatLaf (modern Look & Feel)
* Collections (ArrayList)
* VS Code

---

## Project Structure

* **Expense** – data model representing a single expense
* **ExpenseTrackerGUI** – main application window and user interaction
* **ExpenseStorage** – save and load functionality (file persistence)
* **CategorySummaryPanel** – category-based expense statistics

---

## How to Run

1. Make sure Java (version 11 or newer) is installed
2. Open the project in VS Code
3. Configure the FlatLaf library:

   * Open **Java: Configure Classpath** 
   * Go to **Libraries**
   * Select **Add Library**
   * Navigate to the project **lib** folder
   * Add the **FlatLaf `.jar` file**
4. Run the `ExpenseTrackerGUI.java` file
5. The application window will open

---

## Notes

* The application runs locally
* No database or external services are required
* FlatLaf is included as an external library via the project classpath
* Designed for educational and academic purposes
---
## Conclusion

This projecdemonstrates a well-structured Java Swing desktop application with persistent storage, basic data analysis, and a modern user interface using FlatLaf.
