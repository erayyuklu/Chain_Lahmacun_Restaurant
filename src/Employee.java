// Class representing an Employee
public class Employee {
    String name; // Name of the employee
    String category; // Category of the employee (e.g., Cashier, Cook, Courier)
    int proPoint; // Performance points of the employee

    // Constructor to initialize an Employee with a name, category, and default performance points
    public Employee(String name, String category) {
        this.name = name; // Assign the provided name
        this.category = category; // Assign the provided category
        this.proPoint = 0; // Initialize performance points to zero
    }
}