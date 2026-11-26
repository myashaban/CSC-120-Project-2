import java.io.Serializable;

/**
 * Represents a single boat in the fleet inventory.
 * Implements Serializable so it can be saved to an object database file.
 */
public class Boat implements Serializable {
    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int length;
    private double purchasePrice;
    private double expenses;

    /**
     * Constructor that parses a raw CSV string to create a Boat object.
     *
     * @param csvLine A comma-separated string containing boat data.
     */
    public Boat(String csvLine) {
        // Split the line by commas
        String[] parts = csvLine.split(",");

        // Parse and assign each part, using trim() to remove accidental spaces
        this.type = BoatType.valueOf(parts[0].trim().toUpperCase());
        this.name = parts[1].trim();
        this.year = Integer.parseInt(parts[2].trim());
        this.makeModel = parts[3].trim();
        this.length = Integer.parseInt(parts[4].trim());
        this.purchasePrice = Double.parseDouble(parts[5].trim());
        this.expenses = 0.00; // Expenses always start at 0
    }

    /**
     * Checks if the requested expense is allowed under the Commodore's policy.
     *
     * @param amount The requested maintenance cost.
     * @return true if authorized, false if the limit is exceeded.
     */
    public boolean spendMoney(double amount) {
        if ((this.expenses + amount) <= this.purchasePrice) {
            this.expenses += amount;
            return true;
        }
        return false;
    }

    // --- Accessor Methods (Getters) --- //

    public String getName() {
        return name;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getExpenses() {
        return expenses;
    }

    /**
     * Returns the remaining budget allowed for this boat.
     *
     * @return The difference between purchase price and current expenses.
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Formats the boat's data to perfectly match the Fleet report output.
     *
     * @return A formatted string of the boat's details.
     */
    @Override
    public String toString() {
        // %-7s left-aligns a 7-character string. %9.2f formats a float to 2 decimal places.
        return String.format("%-7s %-20s %4d %-12s %3d' : Paid $ %9.2f : Spent $ %9.2f",
                type, name, year, makeModel, length, purchasePrice, expenses);
    }
}