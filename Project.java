import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class HouseConstructionMaterialCalculator {
    // Global variables
    private static final double tmtPerSqft = 0.0033; // tons
    private static final double cementPerSqft = 0.426; // bags
    private static final double mSandPerSqft = 0.065; // tons
    private static final double concentrateMSandPerSqft = 0.029; // tons
    private static final double jelly40mmPerSqft = 0.23; // cubic feet
    private static final double jelly20mmPerSqft = 1.33; // cubic feet
    private static final double brickPerSqft = 42; // blocks
    private static final double marlaToSqft = 250.0; // Conversion factor for marla to square feet
    private static final String userCredentialsFile = "user_credentials.txt";
    private static final String adminCredentialsFile = "admin_credentials.txt";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        show();
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("Please log in:");
            System.out.println("1. User Login");
            System.out.println("2. Admin Login");
            System.out.print("Enter your choice: ");
            int choice;
            while (!input.hasNextInt()) {
                System.out.print("Invalid input. Please enter a valid integer: ");
                input.next();
            }
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    if (userLogin(input)) {
                        loggedIn = true;
                        System.out.println("User login successful.");
                    } else {
                        System.out.println("User login failed. Please try again.");
                    }
                    break;
                case 2:
                    if (adminLogin(input)) {
                        loggedIn = true;
                        System.out.println("Admin login successful.");
                    } else {
                        System.out.println("Admin login failed. Please try again.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option (1-2).");
                    break;
            }
        }

        String unit;
        do {
            unit = getAreaUnit(input);
        } while (!unit.equalsIgnoreCase("f") && !unit.equalsIgnoreCase("m"));

        // If user has selected marla
        double area;
        if (unit.equalsIgnoreCase("m")) {
            System.out.print("Enter area in marla: ");
            double marla;
            while (!input.hasNextDouble()) {
                System.out.print("Invalid input. Please enter a valid number: ");
                input.next();
            }
            marla = input.nextDouble();

            area = convertMarlaToSqft(marla);

        } else {

            System.out.print("Enter length in feet: ");
            double length;
            while (!input.hasNextDouble()) {
                System.out.print("Invalid input. Please enter a valid number: ");
                input.next();
            }

            length = input.nextDouble();
            // Input width
            System.out.print("Enter width in feet: ");
            double width;
            while (!input.hasNextDouble()) {
                System.out.print("Invalid input. Please enter a valid number: ");
                input.next();
            }
            width = input.nextDouble();

            area = calculateArea(length, width);
        }

        // Input number of floors,rooms, bathrooms, kitchens.......................
        System.out.print("Enter the number of floors: ");
        int floors;
        while (!input.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid integer: ");
            input.next();
        }
        floors = input.nextInt();

        System.out.print("Enter the number of rooms: ");
        int rooms;
        while (!input.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid integer: ");
            input.next();
        }
        rooms = input.nextInt();

        System.out.print("Enter the number of kitchens: ");
        int kitchens;
        while (!input.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid integer: ");
            input.next();
        }
        kitchens = input.nextInt();

        System.out.print("Enter the number of bathrooms: ");
        int bathrooms;
        while (!input.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid integer: ");
            input.next();
        }
        bathrooms = input.nextInt();

        calculateHouseConstructionMaterials(area, rooms, kitchens, floors, bathrooms);

        // Cost estimation...........................................Second feature
        String choice = "";
        while (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no")) {
            System.out.print("Do you want to find the total cost of materials? (yes/no): ");
            choice = input.next();
        }

        if (choice.equalsIgnoreCase("yes")) {
            double cementPrice = getCementPrice(input);
            double tmtPrice = getTMTPrice(input);
            double plasteringMSandPrice = getPlasteringMSandPrice(input);
            double concreteMSandPrice = getConcreteMSandPrice(input);
            double constructionCrushPrice = getConstructionCrushPrice(input);
            double brickPrice = getBrickPrice(input);
            double totalCost = calculateTotalCost(area, floors, cementPrice, tmtPrice, plasteringMSandPrice,
                    concreteMSandPrice, constructionCrushPrice, brickPrice, rooms, bathrooms, kitchens);
            System.out.println("Total cost of materials: Rs " + totalCost);

        }

        // Prompt for order placement
        String placeOrderChoice = "";
        while (!placeOrderChoice.equalsIgnoreCase("yes") && !placeOrderChoice.equalsIgnoreCase("no")) {
            System.out.print("Do you want to place an order? (yes/no): ");
            placeOrderChoice = input.next();
        }

        if (placeOrderChoice.equalsIgnoreCase("yes")) {
            // Get order details
            System.out.print("Enter the number of items: ");
            int itemCount = input.nextInt();
            input.nextLine();

            System.out.println("Enter the item details:");
            List<String> items = new ArrayList<>();
            for (int i = 0; i < itemCount; i++) {
                System.out.print("Item " + (i + 1) + ": ");
                String item = input.nextLine();
                items.add(item);
            }
            System.out.print("Enter your address: ");
            String address = input.nextLine();

            String paymentMethod = "";
            while (!paymentMethod.equalsIgnoreCase("online") && !paymentMethod.equalsIgnoreCase("delivery")) {
                System.out.print("Choose payment method ('online' or 'delivery'): ");
                paymentMethod = input.next();
            }

            if (paymentMethod.equalsIgnoreCase("online")) {
                // Online payment
                System.out.print("Enter your credit card number: ");
                String creditCardNumber = input.next();

                System.out.println("\n--- Order Details ---");
                System.out.println("Items:");
                for (String item : items) {
                    System.out.println("- " + item);
                }
                System.out.println("Address: " + address);
                System.out.println("Payment Method: Online");
                System.out.println("Credit Card Number: " + creditCardNumber);
            } else {
                // Delivery payment
                // Place order logic can be added here
                System.out.println("\n--- Order Details ---");
                System.out.println("Items:");
                for (String item : items) {
                    System.out.println("- " + item);
                }
                System.out.println("Address: " + address);
                System.out.println("Payment Method: Delivery");
            }
        } else {
            System.out.println("No order placed. Exiting the program.");
            System.exit(0);
        }

        input.close();
    }

    public static void show() {
        System.out.println(".");
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("..");
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("...");
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("....................................................................");
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("......................3-D Model Generator...........................");
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("....................................................................");
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println(".....................Material and Cost Estimator....................");
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("....................................................................");

    }

    private static boolean userLogin(Scanner input) {
        System.out.print("Enter username: ");
        String username = input.next();

        String password;
        int passLength;
        do {
            System.out.println("Enter password: ");
            System.out.println("Password must be equal to 8 digits");
            password = input.next();
            passLength = password.length();
        } while (passLength < 8);

        try {
            File file = new File("user_credentials.txt");
            if (file.createNewFile()) {
                System.out.println("File created ");
            }

            FileWriter fw = new FileWriter("user_credentials.txt");
            fw.write(username + "," + password);

            fw.close();
        } catch (Exception IO) {
            System.out.println("Cannot create a new file");
        }

        return checkCredentials(username, password, userCredentialsFile);
    }

    private static boolean adminLogin(Scanner input) {
        System.out.print("Enter username: ");
        String username = input.next();
        System.out.print("Enter password: ");
        String password;
        int passLength;
        do {
            System.out.println("Enter password: ");
            System.out.println("Password must be equal to 8 digits");
            password = input.next();
            passLength = password.length();
        } while (passLength < 8);
        try {
            File file = new File("admin_credentials.txt");
            if (file.createNewFile()) {
                System.out.println("File created ");
            }

            FileWriter fw = new FileWriter("admin_credentials.txt");
            fw.write(username + "," + password);

            fw.close();
        } catch (Exception IO) {
            System.out.println("Cannot create a new file");
        }

        return checkCredentials(username, password, adminCredentialsFile);
    }

    private static boolean checkCredentials(String username, String password, String credentialsFile) {
        try {
            File file = new File(credentialsFile);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to read the credentials file.");
        }
        return false;
    }

    private static String getAreaUnit(Scanner input) {
        String unit;
        do {
            System.out.print("Select area unit ('f' for square feet or 'm' for marla): ");
            unit = input.next();
        } while (!unit.equalsIgnoreCase("f") && !unit.equalsIgnoreCase("m"));
        return unit;
    }

    // Calculate Area...................
    private static double calculateArea(double length, double width) {
        return length * width;
    }

    // Convert marla to square feet............
    private static double convertMarlaToSqft(double marla) {
        return marla * marlaToSqft;
    }

    // Calculate construction materials................... First
    // feature......................
    private static void calculateHouseConstructionMaterials(double area, int rooms, int kitchens, int floors,
            int bathrooms) {
        // Total area with number of floors
        double builtupSqft = Math.ceil(area * floors - area / 2);
        // Tmt steel used in construction
        double tmt = Math.ceil((tmtPerSqft * builtupSqft * 10) / 10.0);
        // Cement used in construction
        double cement = Math.ceil((cementPerSqft * builtupSqft * 10) / 10.0);
        // Manufactured (plastering) sand (mSand) used in construction
        double mSand = Math.ceil((mSandPerSqft * builtupSqft));
        // Manufactured Concrete sand used in construction
        double concreteMSand = Math.ceil((concentrateMSandPerSqft * builtupSqft));
        // Jelly estimations
        double jelly40mm = Math.ceil((jelly40mmPerSqft * builtupSqft));
        double jelly20mm = Math.ceil((jelly20mmPerSqft * builtupSqft));
        // Bricks
        double bricks = Math
                .ceil(((brickPerSqft * builtupSqft + rooms / 2 * 120 + bathrooms / 2 * 60 + kitchens / 2 * 90)));
        // Crush estimation in house
        double constructionCrush = Math.ceil((0.043 * builtupSqft));

        // Displaying outputs
        System.out.println("Total square footage: " + area + " sqft");
        System.out.println("Built-up area: " + builtupSqft + " sqft");
        System.out.println("TMT steel: " + tmt + " tons");
        System.out.println("Cement: " + cement + " bags");
        System.out.println("Plastering M Sand: " + mSand + " tons");
        System.out.println("Concrete M Sand: " + concreteMSand + " tons");
        System.out.println("4\" clay brick: " + bricks + " blocks");
        System.out.println("Construction crush: " + constructionCrush + " tons");
        System.out.println("40mm jelly: " + jelly40mm + " cubic feet");
        System.out.println("20mm jelly: " + jelly20mm + " cubic feet");
        System.out.println("  ");
        System.out.println("Here Is a model of your House");
        System.out.println("  ");
        System.out.println("      /\\      ");
        System.out.println("     /  \\     ");
        System.out.println("    /    \\    ");
        System.out.println("   /------\\   ");
        System.out.println("  / |    | \\  ");
        System.out.println(" /  |    |  \\ ");
        System.out.println("/---|----|---\\");
        System.out.println("|   |    |   |");
        System.out.println("|   |    |   |");
        System.out.println("|---|----|---|");
        System.out.println("  ");

    }
    // .........................

    // ..................................................Calculating prices
    // ....Second feature
    // Asking user to choose cement price
    private static double getCementPrice(Scanner input) {
        System.out.println("Choose the cement type:");
        System.out.println("1. Rs 1100 per bag");
        System.out.println("2. Rs 1000 per bag");
        System.out.println("3. Rs 900 per bag");
        System.out.print("Enter your choice: ");

        int choice = 0;
        // Checking choice
        while (choice < 1 || choice > 3) {
            if (input.hasNextInt()) {
                choice = input.nextInt();
            } else {
                input.next(); // Discard invalid input
            }
            // Range for choice
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter a valid option (1-3).");
            }
        }
        // Cases for choice
        switch (choice) {
            case 1:
                return 1100.0;
            case 2:
                return 1000.0;
            case 3:
                return 900.0;
            default:
                return 0.0; // Invalid choice, return 0 price
        }
    }

    // Asking user to choose tmt steel price
    private static double getTMTPrice(Scanner input) {
        System.out.println("Choose the TMT steel type:");
        System.out.println("1. Rs 250,000 per ton");
        System.out.println("2. Rs 245,000 per ton");
        System.out.println("3. Rs 240,000 per ton");
        System.out.print("Enter your choice: ");
        // Validity check
        int choice = 0;
        while (choice < 1 || choice > 3) {
            if (input.hasNextInt()) {
                choice = input.nextInt();
            } else {
                input.next(); // Discard invalid input
            }
            // Range for choice
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter a valid option (1-3).");
            }
        }
        // Choices
        switch (choice) {
            case 1:
                return 250000.0;
            case 2:
                return 245000.0;
            case 3:
                return 240000.0;
            default:
                return 0.0; // Invalid choice, return 0 price
        }
    }

    // method for choice of Plastering m sand price
    private static double getPlasteringMSandPrice(Scanner input) {
        System.out.println("Choose the plastering M sand type:");
        System.out.println("1. Rs 1365 per ton");
        System.out.println("2. Rs 1325 per ton");
        System.out.println("3. Rs 1275 per ton");
        System.out.print("Enter your choice: ");
        // Validity check
        int choice = 0;
        while (choice < 1 || choice > 3) {
            if (input.hasNextInt()) {
                choice = input.nextInt();
            } else {
                input.next(); // Discard invalid input
            }
            // Range check
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter a valid option (1-3).");
            }
        }
        // Choice
        switch (choice) {
            case 1:
                return 1365.0;
            case 2:
                return 1325.0;
            case 3:
                return 1275.0;
            default:
                return 0.0; // Invalid choice, return 0 price
        }
    }

    // method for price of concrete m sand
    private static double getConcreteMSandPrice(Scanner input) {
        System.out.println("Choose the concrete M sand type:");
        System.out.println("1. Rs 725 per ton");
        System.out.println("2. Rs 695 per ton");
        System.out.println("3. Rs 635 per ton");
        System.out.print("Enter your choice: ");
        // Validity check
        int choice = 0;
        while (choice < 1 || choice > 3) {
            if (input.hasNextInt()) {
                choice = input.nextInt();
            } else {
                input.next(); // Discard invalid input
            }
            // Range check
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter a valid option (1-3).");
            }
        }
        // Choices
        switch (choice) {
            case 1:
                return 725.0;
            case 2:
                return 695.0;
            case 3:
                return 635.0;
            default:
                return 0.0; // Invalid choice, return 0 price
        }
    }

    // Method for crush price
    private static double getConstructionCrushPrice(Scanner input) {
        System.out.println("Choose the construction crush type:");
        System.out.println("1. Rs 1500 per ton");
        System.out.println("2. Rs 1450 per ton");
        System.out.println("3. Rs 1400 per ton");
        System.out.print("Enter your choice: ");
        // Validity check
        int choice = 0;
        while (choice < 1 || choice > 3) {
            if (input.hasNextInt()) {
                choice = input.nextInt();
            } else {
                input.next(); // Discard invalid input
            }
            // Range check
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter a valid option (1-3).");
            }
        }
        // Choice
        switch (choice) {
            case 1:
                return 1500.0;
            case 2:
                return 1450.0;
            case 3:
                return 1400.0;
            default:
                return 0.0; // Invalid choice, return 0 price
        }
    }

    // method for choice of brick price
    private static double getBrickPrice(Scanner input) {
        System.out.println("Choose the brick type:");
        System.out.println("1. Rs 15 per block");
        System.out.println("2. Rs 13 per block");
        System.out.println("3. Rs 12 per block");
        System.out.print("Enter your choice: ");
        // Validity check
        int choice = 0;
        while (choice < 1 || choice > 3) {
            if (input.hasNextInt()) {
                choice = input.nextInt();
            } else {
                input.next(); // Discard invalid input
            }
            // Range check
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter a valid option (1-3).");
            }
        }
        // Choices
        switch (choice) {
            case 1:
                return 15;
            case 2:
                return 13;
            case 3:
                return 12;
            default:
                return 0.0; // Invalid choice, return 0 price
        }
    }

    //
    // Calculating total cost.....................
    private static double calculateTotalCost(double area, int floors, double cementPrice, double tmtPrice,
            double plasteringMSandPrice, double concreteMSandPrice,
            double constructionCrushPrice,
            double brickPrice, int rooms, int bathrooms, int kitchens) {
        // Calculating total square feet
        double builtupSqft = Math.ceil(area * floors - area / 2);
        // Calculating steel cost
        double tmtCost = Math.ceil(tmtPrice * ((tmtPerSqft * builtupSqft)));
        // calculating cement cost
        double cementCost = Math.ceil(cementPrice * ((cementPerSqft * builtupSqft)));
        // Calculating sand cost
        double mSandCost = Math.ceil(plasteringMSandPrice * ((mSandPerSqft * builtupSqft)));
        // Calculating concrete cement cost
        double concreteMSandCost = Math.ceil(concreteMSandPrice * ((concentrateMSandPerSqft * builtupSqft)));
        // Calculating brick cost
        double bricksCost = Math
                .ceil(brickPrice * (((brickPerSqft * builtupSqft + rooms / 2 * 120 + bathrooms / 2 * 60 +
                        kitchens / 2 * 90))));
        // Calculating crush cost
        double constructionCrushCost = Math.ceil(constructionCrushPrice * ((0.043 * builtupSqft)));

        // Calculating total price of house
        double totalCost = tmtCost + cementCost + mSandCost + concreteMSandCost +
                bricksCost + constructionCrushCost;
        return totalCost;

    }
}
