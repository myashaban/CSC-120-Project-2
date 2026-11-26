/**
 * Fleet Management Program.
 * Manages an inventory of boats for the Coconut Grove Sailing Club.
 * For CSC-120 Project 2.
 *
 * @author Muhemmed Yusuf Ali Shaban
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FleetManagement {

  /**
   * Constant for the serialized database filename.
   */
  private static final String DB_FILENAME = "FleetData.db";

  /**
   * The main entry point for the application. Handles command line arguments,
   * initializes the fleet, and runs the main menu loop.
   *
   * @param args Command line arguments (expects a CSV filename on initial run)
   */


  public static void main(String[] args) {

    // as per Geoff's Law: All local variables declared at the start of the method
    ArrayList<Boat> fleet;
    String menuInput;
    char choice;

    fleet = new ArrayList<>();
    Scanner keyboard = new Scanner(System.in);

    System.out.println("Welcome to the Fleet Management System");

    if (args.length > 0) {
      loadFromCSV(args[0], fleet);
    } else {
      loadFromDB(fleet);
    }

    do {
      System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
      menuInput = keyboard.nextLine().trim().toUpperCase();

      if (menuInput.isEmpty()) {
        choice = ' ';
      } else {
        choice = menuInput.charAt(0);
      }

      if (choice == 'P') {
        printFleet(fleet);
      } else if (choice == 'A') {
        addBoat(keyboard, fleet);
      } else if (choice == 'R') {
        removeBoat(keyboard, fleet);
      } else if (choice == 'E') {
        expenseBoat(keyboard, fleet);
      } else if (choice == 'X') {
        System.out.println("Exiting the Fleet Management System");
        saveToDB(fleet);
      } else {
        System.out.println("Invalid menu option, try again");
      }
    } while (choice != 'X');

    keyboard.close();
  } // end of method main

  /**
   * Loads boat data from a comma-separated values file into the fleet.
   *
   * @param filename The name of the CSV file to read.
   * @param fleet The ArrayList storing the club's boats.
   */
  private static void loadFromCSV(String filename, ArrayList<Boat> fleet) {

    // as per Geoff's Law in marks rubric: local variables at the top
    File file;
    String line;
    Boat newBoat;

    try {
      file = new File(filename);
      Scanner keyboard = new Scanner(file);

      while (keyboard.hasNextLine()) {
        line = keyboard.nextLine();
        if (line.length() > 0) {
          newBoat = new Boat(line);
          fleet.add(newBoat);
        }
      }
      keyboard.close();
    } catch (IOException e) {
      System.out.println("Error reading CSV file: " + e.getMessage());
    }
  } // end of method loadFromCSV

  /**
   * Deserializes the fleet ArrayList from the database file.
   *
   * @param fleet The ArrayList to populate with saved boats.
   */
  private static void loadFromDB(ArrayList<Boat> fleet) {
    // Geoff's Law: Local variables at the top
    FileInputStream fileInput;
    ObjectInputStream objectInput;
    ArrayList<Boat> savedFleet;

    try {
      fileInput = new FileInputStream(DB_FILENAME);
      objectInput = new ObjectInputStream(fileInput);

      savedFleet = (ArrayList<Boat>) objectInput.readObject();
      fleet.addAll(savedFleet);

      objectInput.close();
      fileInput.close();
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Error loading from DB: " + e.getMessage());
    }
  } // end of method loadFromDB



  /**
   * Serializes the current fleet ArrayList to the database file.
   *
   * @param fleet The ArrayList containing the club's boats to be saved.
   */


  private static void saveToDB(ArrayList<Boat> fleet) {
    // Geoff's Law: Local variables at the top
    FileOutputStream fileOutput;
    ObjectOutputStream objectOutput;

    try {
      fileOutput = new FileOutputStream(DB_FILENAME);
      objectOutput = new ObjectOutputStream(fileOutput);

      objectOutput.writeObject(fleet);

      objectOutput.close();
      fileOutput.close();
    } catch (IOException e) {
      System.out.println("Error saving to DB: " + e.getMessage());
    }
  } // end of method saveToDB



  /**
   * Prints the details of every boat in the fleet and the total financials.
   *
   * @param fleet The ArrayList containing the club's boats.
   */
  private static void printFleet(ArrayList<Boat> fleet) {

    // as per Geoff's Law: Local variables at the top
    int index;
    double totalPaid;
    double totalSpent;
    Boat currentBoat;

    totalPaid = 0.0;
    totalSpent = 0.0;

    System.out.println("Fleet report:");
    for (index = 0; index < fleet.size(); index++) {
      currentBoat = fleet.get(index);
      System.out.println(currentBoat.toString());
      totalPaid += currentBoat.getPurchasePrice();
      totalSpent += currentBoat.getExpenses();
    }

    System.out.printf("%-52s : Paid $ %9.2f : Spent $ %9.2f\n", "Total", totalPaid, totalSpent);
  } // end of method printFleet



  /**
   * Prompts the user for a CSV string and adds the parsed boat to the fleet.
   *
   * @param keyboard The Scanner object for user input.
   * @param fleet The ArrayList containing the club's boats.
   */
  private static void addBoat(Scanner keyboard, ArrayList<Boat> fleet) {
    // Geoff's Law: Local variables at the top
    String csvData;
    Boat newBoat;

    System.out.print("Please enter the new boat CSV data          : ");
    csvData = keyboard.nextLine();

    try {
      newBoat = new Boat(csvData);
      fleet.add(newBoat);
    } catch (Exception e) {
      System.out.println("Invalid boat data format.");
    }
  } // end of method addBoat


  /**
   * Prompts the user for a boat name and removes it from the fleet if found.
   *
   * @param keyboard The Scanner object for user input.
   * @param fleet The ArrayList containing the club's boats.
   */
  private static void removeBoat(Scanner keyboard, ArrayList<Boat> fleet) {
    // Geoff's Law: Local variables at the top
    String targetName;
    int index;
    boolean found;
    Boat currentBoat;

    found = false;

    System.out.print("Which boat do you want to remove?           : ");
    targetName = keyboard.nextLine().trim();

    for (index = 0; index < fleet.size(); index++) {
      currentBoat = fleet.get(index);
      if (currentBoat.getName().equalsIgnoreCase(targetName)) {
        fleet.remove(index);
        found = true;
        break;
      }
    }

    if (!found) {
      System.out.println("Cannot find boat " + targetName);
    }
  } // end of method removeBoat




  /**
   * Prompts the user for a boat name and an expense amount, then checks if
   * the expense is permitted under the Commodore's policy.
   *
   * @param keyboard The Scanner object for user input.
   * @param fleet The ArrayList containing the club's boats.
   */

  private static void expenseBoat(Scanner keyboard, ArrayList<Boat> fleet) {
    // Geoff's Law: Local variables at the top
    String targetName;
    double amount;
    int index;
    boolean found;
    Boat currentBoat;

    found = false;

    System.out.print("Which boat do you want to spend on?         : ");
    targetName = keyboard.nextLine().trim();

    for (index = 0; index < fleet.size(); index++) {
      currentBoat = fleet.get(index);
      if (currentBoat.getName().equalsIgnoreCase(targetName)) {
        found = true;
        System.out.print("How much do you want to spend?              : ");

        if (keyboard.hasNextDouble()) {
          amount = keyboard.nextDouble();
          keyboard.nextLine(); // consume the newline

          if (currentBoat.spendMoney(amount)) {
            System.out.printf("Expense authorized, $%.2f spent.\n", currentBoat.getExpenses());
          } else {
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n",
                    currentBoat.getRemainingBudget());
          }
        } else {
          System.out.println("Invalid amount entered.");
          keyboard.nextLine(); // consume invalid input
        }
        break;
      }
    }

    if (!found) {
      System.out.println("Cannot find boat " + targetName);
    }
  } // end of method expenseBoat
} // end of public class FleetManagement