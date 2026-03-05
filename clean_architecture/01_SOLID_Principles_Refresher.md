# 1. The SOLID Principles Refresher

The SOLID principles are the foundation of the Clean Architecture. They tell us how to arrange our functions and data structures into classes, and how those classes should be interconnected.

## 1. Single Responsibility Principle (SRP)
*   **Definition:** A module should be responsible to one, and only one, *actor* (a group requiring a change).
*   **Interview Token:** It's not just that a class should "do one thing". It means a class should only have one *reason to change*.
*   **Violation:** An `Employee` class that contains `calculatePay()` (used by Accounting) and `saveToDb()` (used by DBAs). If DBAs require a schema change, Accounting's logic might accidentally break.
*   **Solution:** Separate the data (`EmployeeData`) from the functions (`PayCalculator`, `EmployeeRepository`).

## 2. Open-Closed Principle (OCP)
*   **Definition:** A software artifact should be open for extension but closed for modification.
*   **Interview Token:** You should be able to make the system behave in new ways *without* changing existing code (which could introduce bugs).
*   **Violation:** A `ReportGenerator` class with a massive `switch` statement: `if (type == PDF) { ... } else if (type == HTML) { ... }`. Adding a CSV type requires modifying this existing, tested class.
*   **Solution:** Create an interface `ReportFormatter`. Create concrete classes `PdfFormatter` and `HtmlFormatter`. The `ReportGenerator` relies on the interface. To add CSV, just create a new `CsvFormatter` class; the generator code is untouched.

## 3. Liskov Substitution Principle (LSP)
*   **Definition:** Objects of a superclass shall be replaceable with objects of its subclasses without breaking the application.
*   **Interview Token:** Subclasses must honor the *contracts* (preconditions and postconditions) of the parent class.
*   **Violation:** The classic Square/Rectangle problem. If `Square` inherits from `Rectangle`, and a function sets the width to 5 and height to 2, it expects an area of 10. But a `Square` forces width and height to be equal, returning an area of 4, breaking the caller's assumption.
*   **Solution:** Ensure inherited behaviors do not fundamentally alter expected outcomes. Favor composition over inheritance if behaviors diverge.

## 4. Interface Segregation Principle (ISP)
*   **Definition:** No client should be forced to depend on methods it does not use.
*   **Interview Token:** Keep interfaces small and specific. Fat interfaces cause unnecessary coupling.
*   **Violation:** An `IMachine` interface with `print()`, `scan()`, and `fax()`. A simple desk printer must implement `IMachine` but throw `NotSupportedException` for `scan()` and `fax()`.
*   **Solution:** Break it down into `IPrinter`, `IScanner`, and `IFax`. A complex machine implements all three; a simple printer only implements `IPrinter`.

## 5. Dependency Inversion Principle (DIP)
*   **Definition:** High-level modules (business rules) should not depend on low-level modules (DB, UI). Both should depend on abstractions (interfaces). Abstractions should not depend on details; details should depend on abstractions.
*   **Interview Token:** This is the absolute core of Clean Architecture. You invert the flow of control using interfaces.
*   **Violation:** A `CreateUserUseCase` directly instantiates and calls a `PostgresUserRepository`. The business logic is coupled to PostgreSQL.
*   **Solution:** `CreateUserUseCase` depends on an interface `IUserRepository`. The `PostgresUserRepository` *implements* `IUserRepository`. The dependency arrow is flipped—the DB layer now depends on an interface defined by the Domain layer.
