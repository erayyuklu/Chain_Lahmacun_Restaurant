// Class representing a City
public class City {
    HashMap<String, Branch> branches; // Stores branches of the city using branch names as keys
    String cityName; // Name of the city

    // Constructor to initialize a City with a name and an empty HashMap of branches
    City(String cityName) {
        this.cityName = cityName; // Assign the provided city name
        this.branches = new HashMap<>(); // Initialize the HashMap to store branches
    }

    // Method to set up an initial branch for the city if it doesn't exist
    public void setBranchInitial(String branchName) {
        branches.putIfAbsent(branchName, new Branch(branchName)); // Adds a new branch if not present
    }

    // Method to retrieve a branch from the city based on the district
    public Branch getBranchInitial(String district) {
        return branches.get(district); // Retrieve the branch using the district as a key
    }
}