package picerija;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Definējam picas pasūtījuma klasi
class PizzaOrder {
    String name;
    String address;
    String phone;
    String size;
    List<String> toppings;
    List<String> sauces;
    boolean delivery;
    double price;

    public PizzaOrder(String name, String address, String phone, String size, List<String> toppings, List<String> sauces, boolean delivery, double price) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.size = size;
        this.toppings = toppings;
        this.sauces = sauces;
        this.delivery = delivery;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nAddress: " + address + "\nPhone: " + phone + "\nSize: " + size +
                "\nToppings: " + String.join(", ", toppings) + "\nSauces: " + String.join(", ", sauces) +
                "\nDelivery: " + (delivery ? "Yes" : "No") + "\nPrice: " + price + "\n";
    }
}

// Galvenā programma
public class Main {
    private static final String ORDER_FILE = "pizza_orders.txt"; // Faila nosaukums
    private static List<PizzaOrder> orders = new ArrayList<>(); // Pasūtījumu saraksts

    public static void main(String[] args) {
        loadOrders(); // Ielādējam iepriekšējos pasūtījumus
        while (true) {
            // Izvēlamies darbību
            String[] options = {"New Order", "View Orders", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Pizza Ordering System",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            // Atkarībā no izvēles izpildām attiecīgo darbību
            if (choice == 0) {
                createNewOrder(); // Jauns pasūtījums
            } else if (choice == 1) {
                viewOrders(); // Skatīt pasūtījumus
            } else {
                saveOrders(); // Saglabāt pasūtījumus
                System.exit(0); // Iziet
            }
        }
    }

    private static void createNewOrder() {
        // Pieprasām klienta informāciju
        String name = JOptionPane.showInputDialog("Enter your name:");
        if (name == null) return; // Ja lietotājs atceļ
        String address = JOptionPane.showInputDialog("Enter your address:");
        if (address == null) return;
        String phone = JOptionPane.showInputDialog("Enter your phone number:");
        if (phone == null) return;

        // Izvēle starp picas izmēriem
        String[] sizeOptions = {"Small", "Medium", "Large"};
        int sizeChoice = JOptionPane.showOptionDialog(null, "Choose the pizza size:", "Pizza Size",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, sizeOptions, sizeOptions[0]);
        if (sizeChoice == -1) return;
        String size = sizeOptions[sizeChoice];

        // Pieprasām piedevas
        List<String> toppings = new ArrayList<>();
        String[] availableToppings = {"Pepperoni", "Mushrooms", "Onions", "Sausage", "Bacon"};
        for (String topping : availableToppings) {
            int toppingChoice = JOptionPane.showConfirmDialog(null, "Add " + topping + "?", "Toppings",
                    JOptionPane.YES_NO_OPTION);
            if (toppingChoice == JOptionPane.YES_OPTION) {
                toppings.add(topping);
            }
        }

        // Pieprasām mērces
        List<String> sauces = new ArrayList<>();
        String[] availableSauces = {"Tomato", "BBQ", "Garlic"};
        for (String sauce : availableSauces) {
            int sauceChoice = JOptionPane.showConfirmDialog(null, "Add " + sauce + " sauce?", "Sauces",
                    JOptionPane.YES_NO_OPTION);
            if (sauceChoice == JOptionPane.YES_OPTION) {
                sauces.add(sauce);
            }
        }

        // Vai nepieciešama piegāde?
        boolean delivery = JOptionPane.showConfirmDialog(null, "Would you like delivery?", "Delivery",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        // Aprēķinām cenu
        double price = calculatePrice(size, toppings.size(), sauces.size(), delivery);

        // Izveidojam jaunu pasūtījumu un pievienojam sarakstam
        PizzaOrder newOrder = new PizzaOrder(name, address, phone, size, toppings, sauces, delivery, price);
        orders.add(newOrder);
        
        // Ziņojums par veiksmīgu pasūtījumu
        JOptionPane.showMessageDialog(null, "Order created successfully!\n" + newOrder.toString());
    }

    private static double calculatePrice(String size, int numToppings, int numSauces, boolean delivery) {
        // Pamata cena atkarībā no picas izmēra
        double basePrice = switch (size) {
            case "Small" -> 5.0;
            case "Medium" -> 7.0;
            case "Large" -> 9.0;
            default -> 0;
        };

        // Cena par piedevām un mērci
        double toppingPrice = 0.5 * numToppings;
        double saucePrice = 0.5 * numSauces;
        double deliveryFee = delivery ? 2.0 : 0;

        // Kopējā cena
        return basePrice + toppingPrice + saucePrice + deliveryFee;
    }

    private static void viewOrders() {
        // Ja nav pasūtījumu
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No previous orders.");
            return;
        }

        // Parāda visus pasūtījumus
        StringBuilder sb = new StringBuilder();
        for (PizzaOrder order : orders) {
            sb.append(order.toString()).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Previous Orders", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void loadOrders() {
        File file = new File(ORDER_FILE); // Faila objekts
        if (!file.exists()) return; // Ja fails neeksistē, iziet

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String name = line.split(":")[1].trim();
                String address = br.readLine().split(":")[1].trim();
                String phone = br.readLine().split(":")[1].trim();
                String size = br.readLine().split(":")[1].trim();
                String[] toppings = br.readLine().split(":")[1].trim().split(", ");
                String[] sauces = br.readLine().split(":")[1].trim().split(", ");
                boolean delivery = br.readLine().split(":")[1].trim().equals("Yes");
                double price = Double.parseDouble(br.readLine().split(":")[1].trim());

                // Izveidojam un pievienojam pasūtījumu sarakstam
                List<String> toppingList = new ArrayList<>();
                for (String topping : toppings) {
                    if (!topping.isBlank()) {
                        toppingList.add(topping);
                    }
                }

                List<String> sauceList = new ArrayList<>();
                for (String sauce : sauces) {
                    if (!sauce.isBlank()) {
                        sauceList.add(sauce);
                    }
                }

                orders.add(new PizzaOrder(name, address, phone, size, toppingList, sauceList, delivery, price));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading previous orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void saveOrders() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (PizzaOrder order : orders) {
                bw.write(order.toString());
                bw.write("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
