import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MarketplaceClient {


    
    public static void main(String[] args) throws IOException {
        new MarketplaceGUI();

        Scanner scanner = new Scanner(System.in);

        BufferedReader reader;  //Buffered reader object to read server
        PrintWriter writer;  //Print Writer object to read server

        try {
            Socket socket = new Socket("localhost", 1111);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Connection Found", "Search",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Welcome to the Shop 180!");

        int choice;
        do {
            System.out.println("Please choose an option:");
            System.out.println("1. Sign in");
            System.out.println("2. Create an account as a buyer");
            System.out.println("3. Create an account as a seller");

            choice = scanner.nextInt();
            scanner.nextLine();
        } while (choice != 1 && choice != 2 && choice != 3);


        if (choice == 1) {
            writer.write("1\n");
            writer.flush();

            boolean valid;
            int selection = 10;
            do {
                valid = true;
                String email;
                do {
                    System.out.println("Please enter your email:");
                    email = scanner.nextLine();
                } while (!(email.contains("@") && email.contains(".")));

                System.out.println("Please enter your password:");
                String password = scanner.nextLine();

                writer.write(email);
                writer.println();
                writer.write(password);
                writer.println();
                writer.flush();

                boolean error;  //Whether the number is an integer, (makes sure there are returns)

                //Gets number of results
                int best = 0;
                do {
                    error = false;
                    try {
                        String readLine = reader.readLine();
                        selection = Integer.parseInt(readLine);
                    } catch (Exception e) {
                        error = true;
                        best++;
                    }
                } while (error && best != 10);

                if (selection == 2) {
                    valid = false;
                }

            } while(!valid);

            if (selection == 0) {
                try {
                    buyerLogic(reader, writer, scanner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (selection == 1) {
                try {
                    sellerLogic(reader, writer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("ERROR");
            }
        } else if (choice == 2) {
            writer.write("2\n");
            writer.flush();
            System.out.println("Please enter your name:");
            String name = scanner.nextLine();

            String email = "";
            do {
                System.out.println("Please enter your email:");
                email = scanner.nextLine();
                if (email.contains(",")) {
                    System.out.println("ERROR: Email can not contain comma\n");
                }
            } while (email.contains(","));

            System.out.println("Please enter your password: ");
            String password = scanner.nextLine();

            Customer buyer = new Customer(name, email, password);
            writer.write(buyer.toString());
            writer.println();
            writer.flush();

            System.out.println("Account created successfully!");
            System.out.println("Please log back in to begin!");
        } else if (choice == 3) {
            writer.write("3\n");
            writer.flush();
            System.out.println("Please enter your name:");
            String name = scanner.nextLine();

            String email = "";
            do {
                System.out.println("Please enter your email:");
                email = scanner.nextLine();
                if (email.contains(",")) {
                    System.out.println("ERROR: Email can not contain comma\n");
                }
            } while (email.contains(","));

            System.out.println("Please enter your password:");
            String password = scanner.nextLine();

            Seller seller = new Seller(name, email, password);
            writer.write(seller.toString());
            writer.println();
            writer.flush();

            System.out.println("Account created successfully!");
            System.out.println("Please log back in to begin!");
        } else {
            writer.write("10\n");
            writer.flush();
            System.out.println("Invalid choice. Please try again.");
        }
    }

    public static void buyerLogic(BufferedReader reader, PrintWriter writer, Scanner scanner) throws IOException {

        Customer buyer = new Customer(reader.readLine());
        System.out.println("Welcome, " + buyer.getName() + "!\n");

        boolean cont = true;

        do {
            int selection;
            buyer = new Customer(reader.readLine());

            do{
                System.out.println("Would you like to:\n" +
                        "1. View all products\n" +
                        "2. Search for specific product\n" +
                        "3. Sort Market on price\n" +
                        "4. Sort Market on Quantity\n" +
                        "5. View Purchase History\n" +
                        "6. View Current Cart\n" +
                        "7. Check out with current cart\n" +
                        "8. Close application");

                selection = scanner.nextInt();
                scanner.nextLine();

                if (selection > 9 || selection < 0) {
                    System.out.println("ERROR: Enter valid selection");
                }
            } while (selection > 9 || selection < 0);

            switch (selection) {
                case 1:
                    writer.write("1\n");
                    writer.flush();

                    String productString = reader.readLine();
                    String[] productStrings = productString.split(";");
                    Product[] productsArray = new Product[productStrings.length];

                    for (int i = 0; i < productStrings.length; i++) {
                        productsArray[i] = new Product(productStrings[i]);
                    }

                    displayProduct(productsArray, reader, writer, scanner);
                    break;
                case 2:
                    writer.write("1\n");
                    writer.flush();

                    String productString4 = reader.readLine();
                    String[] productStrings4 = productString4.split(";");
                    Product[] productsArray4 = new Product[productStrings4.length];

                    for (int i = 0; i < productStrings4.length; i++) {
                        productsArray4[i] = new Product(productStrings4[i]);
                    }

                    System.out.println("Enter Search: ");
                    String search = scanner.nextLine();

                    ArrayList<Product> list = new ArrayList<>();

                    for (Product product : productsArray4) {
                        if (product.getName().toLowerCase().contains(search.toLowerCase())) {
                            list.add(product);
                        }
                    }

                    Product[] searchArray = new Product[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        searchArray[i] = list.get(i);
                    }

                    displayProduct(searchArray, reader, writer, scanner);
                    break;
                case 3:
                    writer.write("1\n");
                    writer.flush();

                    String productString2 = reader.readLine();
                    String[] productStrings2 = productString2.split(";");
                    Product[] productsArray2 = new Product[productStrings2.length];

                    for (int i = 0; i < productStrings2.length; i++) {
                        productsArray2[i] = new Product(productStrings2[i]);
                    }

                    Arrays.sort(productsArray2, Product.productPriceComparator);

                    displayProduct(productsArray2, reader, writer, scanner);
                    break;
                case 4:
                    writer.write("1\n");
                    writer.flush();

                    String productString3 = reader.readLine();
                    String[] productStrings3 = productString3.split(";");
                    Product[] productsArray3 = new Product[productStrings3.length];

                    for (int i = 0; i < productStrings3.length; i++) {
                        productsArray3[i] = new Product(productStrings3[i]);
                    }

                    Arrays.sort(productsArray3, Product.productQuantityComparator);

                    displayProduct(productsArray3, reader, writer, scanner);
                    break;
                case 5:
                    writer.write("2\n");
                    writer.flush();
                    System.out.println("Viewing Purchase History...\n\n" +
                            " Previously Bought\n___________________");
                    for (Product previous : buyer.getPreviousBought()) {
                        System.out.printf("|Product| %-10s |Cost| %-8s |Store| %s \n",
                                previous.getName(),
                                previous.getPrice(),
                                previous.getStore());
                    }

                    System.out.println("Would you like to export purchase history? (y/n)");
                    if (scanner.nextLine().equals("y")) {
                        try {
                            File myObj = new File(buyer.getName() + "PreviousPurchases.txt");
                            if (myObj.createNewFile()) {
                                System.out.println("File created: " + myObj.getName());
                                System.out.println("File should be in program download folder");
                            } else {
                                System.out.println("File Exists Already, Previous File Updated");
                            }

                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(buyer.getName() + "PreviousPurchases.txt"))) {
                                for (Product previous : buyer.getPreviousBought()) {
                                    bw.write(previous.toStringBuyerOutput());
                                    bw.newLine();
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }
                    }
                    break;
                case 6:
                    writer.write("3\n");
                    writer.flush();
                    boolean conts = true;
                    String deleteString = null;

                    while (conts) {
                        System.out.println("Viewing Current Cart...\n\n Current Cart\n______________");
                        int k = 0;
                        for (Product previous : buyer.getCart().getProducts()) {
                            k++;
                            System.out.printf(k + ".  |Product| %-10s |Cost| %-8s |Store| %s \n",
                                    previous.getName(),
                                    previous.getPrice(),
                                    previous.getStore());
                        }
                        System.out.println("\nWould you like to delete an item? Enter its " +
                                "number if yes or 0 to save and exit.");
                        int delete = scanner.nextInt();
                        scanner.nextLine();

                        if (delete >= 1 && delete < buyer.getCart().getProducts().size() + 1){
                            Product remove = buyer.getCart().getProducts().get(delete - 1);
                            if (deleteString == null) {
                                deleteString = remove.toString();
                            } else {
                                deleteString += ";" + remove;
                            }

                            Cart tempCart = buyer.getCart();
                            tempCart.removeProduct(remove);
                            buyer.setCart(tempCart);
                        } else {
                            conts = false;
                            if (deleteString != null) {
                                writer.write(deleteString + "\n");
                            } else {
                                writer.write("none\n");
                            }
                            writer.flush();
                        }
                    }
                    break;
                case 7:
                    System.out.printf("The cost of your cart is %.2f, would you like to check out? (y/n)\n",
                            buyer.getCart().getTotalPrice());
                    if (scanner.nextLine().equals("y")) {
                        System.out.printf("Payment of $%.2f was successful!\n", buyer.getCart().getTotalPrice());
                        writer.write("4\n");
                        writer.flush();
                    } else {
                        System.out.println("Okay! Returning to options!");
                        writer.write("2\n");
                        writer.flush();
                    }
                    break;
                case 8:
                    cont = false;
                    System.out.println("EXITING");
                    writer.write("0");
                    break;
            }
        } while (cont);
    }
    public static void showPage(BufferedReader reader, PrintWriter writer, Scanner scanner, Product[] productArray,
                           int input) {
        System.out.println(productArray[input - 1].toStringPage());
        System.out.println("\nWould you like to add this to your cart? (y/n)");
        if (scanner.nextLine().equals("y")) {
            writer.write("1\n");
            writer.write(productArray[input - 1].toString() + "\n");
            writer.flush();
            System.out.println("Added to cart\n");
        } else {

            writer.write("0\n");
            writer.flush();
            System.out.println("Returning to options");
        }
    }

    public static void displayProduct(Product[] productsArray, BufferedReader reader, PrintWriter writer,
                                      Scanner scanner) {
        System.out.println("\nProducts for Sale:");
        for (int j = 0; j < productsArray.length; j++) {
            System.out.printf("%-5d.  |Product| %-10s |Cost| %-10s |Store| %s \n", (j + 1),
                    productsArray[j].getName(),
                    productsArray[j].getPrice(),
                    productsArray[j].getStore());
        }

        boolean valid;
        int input = 0;

        do {
            valid = true;
            System.out.println("\nWould you like to view a products page?" +
                    " (Enter product number to view its page or 0 to go back to options)");
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input < 0 || input > productsArray.length) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("\nError: Enter Valid Choice");
                valid = false;
            }
        } while (!valid);

        if (input == 0) {
            writer.write("0\n");
            writer.flush();
        } else {
            writer.write("1\n");
            writer.flush();
            showPage(reader, writer, scanner, productsArray, input);
        }
    }

    public static void sellerLogic (BufferedReader reader, PrintWriter writer) throws IOException {

    }
}