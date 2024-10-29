import java.util.ArrayList;

// Class representing a Branch
public class Branch {
    // Fields for a branch including its employees, bonuses, counts for different roles, and manager details
    HashMap<String, Employee> employees; // Stores employees using their name as the key
    String branchName; // Name of the branch
    int monthlyBonus; // Total monthly bonus accumulated by the branch
    int overAllBonus; // Total overall bonus accumulated by the branch
    int numCashier; // Number of Cashiers in the branch
    int numCourier; // Number of Couriers in the branch
    int numCook; // Number of Cooks in the branch
    ArrayList<Employee> nextManager; // List of potential next managers
    Employee manager; // Current manager of the branch

    // Constructor to initialize the Branch with its name and default values
    Branch(String branchName) {
        this.branchName = branchName;
        this.employees = new HashMap<>(); // Initialize the employee map
        monthlyBonus = 0; // Initialize bonuses to zero
        numCashier = 0; // Initialize counts to zero
        numCourier = 0;
        numCook = 0;
        nextManager = new ArrayList<>(); // Initialize the list of potential next managers
    }

    // Method to add an employee to the branch with a specific category
    public void setEmployee(String employeeName, String category) {
        employees.put(employeeName, new Employee(employeeName, category));
    }

    // Method to retrieve an employee from the branch using the employee's name
    public Employee getEmployee(String employeeName) {
        return employees.get(employeeName);
    }

    // Method to check if an employee exists in the branch
    public boolean isEmployeeExist(String employeeName) {
        return employees.containsKey(employeeName);
    }

    // Method to determine if an employee can leave based on the branch's employee count for the category
    public boolean canLeave(Employee e) {
        String catg = e.category;
        if (catg.equals("COOK")) {
            return numCook > 1;
        } else if (catg.equals("CASHIER")) {
            return numCashier > 1;
        } else {
            return numCourier > 1;
        }
    }

    // Method to remove an employee from the branch
    public void leave(Employee e) {
        employees.remove(e.name);
    }

    // Methods to count the number of employees in each category
    public int numOfCashiers() {
        int cashier = 0;
        for (Employee e : employees.values()) {
            if (e.category.equals("CASHIER")) {
                cashier++;
            }
        }
        return cashier;
    }

    public int numOfCooks() {
        int cook = 0;
        for (Employee e : employees.values()) {
            if (e.category.equals("COOK")) {
                cook++;
            }
        }
        return cook;
    }

    public int numOfCouriers() {
        int courier = 0;
        for (Employee e : employees.values()) {
            if (e.category.equals("COURIER")) {
                courier++;
            }
        }
        return courier;
    }

    // Method to check if the manager should be dismissed based on their performance points
    public boolean isManagerWillDismiss() {
        return manager.proPoint <= -5;
    }
}