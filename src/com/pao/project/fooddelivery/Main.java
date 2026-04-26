package com.pao.project.fooddelivery;

import com.pao.project.fooddelivery.exception.DriverNotFoundException;
import com.pao.project.fooddelivery.exception.DriverUnavailableException;
import com.pao.project.fooddelivery.exception.DuplicateIdException;
import com.pao.project.fooddelivery.exception.InvalidDataException;
import com.pao.project.fooddelivery.exception.InvalidOrderException;
import com.pao.project.fooddelivery.exception.OrderNotFoundException;
import com.pao.project.fooddelivery.exception.RestaurantNotFoundException;
import com.pao.project.fooddelivery.model.*;
import com.pao.project.fooddelivery.service.OrderService;
import com.pao.project.fooddelivery.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        RestaurantService restaurantService = RestaurantService.getInstance();
        OrderService orderService = OrderService.getInstance();

        preloadDate(restaurantService, orderService);

        Scanner scanner = new Scanner(System.in);
        runMenu(restaurantService, orderService, scanner);
        scanner.close();
    }

    // Date initiale pentru demo
    private static void preloadDate(RestaurantService rs, OrderService os) {
        System.out.println("========================================");
        System.out.println("   Platforma Food Delivery");
        System.out.println("========================================");
        System.out.println("[INFO] Se incarca datele initiale...\n");

        Restaurant r1 = new Restaurant("R1", "Pizza Palace", new Address("Str. Victoriei 10", "Cluj", "400000"), "Pizza");
        Restaurant r2 = new Restaurant("R2", "Burger House", new Address("Str. Eroilor 5", "Cluj", "400001"), "Fast Food");
        Restaurant r3 = new Restaurant("R3", "Sushi Garden", new Address("Bd. Muncii 22", "Cluj", "400002"), "Sushi");
        rs.addRestaurant(r1);
        rs.addRestaurant(r2);
        rs.addRestaurant(r3);

        Client c1 = new Client("C1", "Ion Popescu", "ion@email.com", "0722111222");
        Client c2 = new Client("C2", "Maria Ionescu", "maria@email.com", "0733222333");
        c1.addAddress(new DeliveryAddress("A1", "Str. Florilor 3", "Cluj-Napoca", "400001", "Etaj 2, Ap 5"));
        c2.addAddress(new DeliveryAddress("A2", "Str. Libertatii 7", "Cluj-Napoca", "400002", ""));
        rs.addClient(c1);
        rs.addClient(c2);

        Driver d1 = new Driver("D1", "Andrei Curier", "andrei@delivery.com", "0744333444");
        Driver d2 = new Driver("D2", "Bogdan Rapid", "bogdan@delivery.com", "0755444555");
        os.registerDriver(d1);
        os.registerDriver(d2);

        try {
            rs.addProductToMenu("R1", new Product("P1", "Pizza Margherita", 35.00, "Pizza clasica cu mozzarella", "Pizza"));
            rs.addProductToMenu("R1", new Product("P2", "Pizza Quattro Formaggi", 42.00, "Pizza cu 4 tipuri de branza", "Pizza"));
            rs.addProductToMenu("R2", new Product("P3", "BigBurger", 28.50, "Burger cu carne de vita", "Burger"));
            rs.addProductToMenu("R2", new Product("P4", "CrispyChicken", 24.00, "Burger cu pui crocant", "Burger"));
            rs.addProductToMenu("R3", new Product("P5", "Salmon Roll (8 buc)", 38.00, "Rulouri cu somon proaspat", "Sushi"));
        } catch (RestaurantNotFoundException e) {
            System.err.println("Eroare preload: " + e.getMessage());
        }

        System.out.println("\n[INFO] Date incarcate. Poti incepe!\n");
    }

    private static void runMenu(RestaurantService rs, OrderService os, Scanner scanner) {
        int addrCounter = 10; // contor pentru ID-uri adrese generate

        while (true) {
            System.out.println("\n===== Meniu principal =====");
            System.out.println("1.  Adauga restaurant");
            System.out.println("2.  Adauga client");
            System.out.println("3.  Inregistreaza sofer");
            System.out.println("4.  Adauga produs la meniu");
            System.out.println("5.  Plaseaza comanda");
            System.out.println("6.  Atribuie sofer la comanda");
            System.out.println("7.  Finalizeaza comanda");
            System.out.println("8.  Cauta restaurante dupa categorie");
            System.out.println("9.  Comenzile unui client");
            System.out.println("10. Comenzi active");
            System.out.println("11. Afiseaza toate restaurantele");
            System.out.println("12. Afiseaza toti soferii");
            System.out.println("0.  Iesire");
            System.out.print("Alege optiunea: ");

            int optiune;
            try {
                optiune = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Optiune invalida. Introdu un numar.");
                continue;
            }

            switch (optiune) {

                case 1: // Adauga restaurant
                    try {
                        System.out.print("ID restaurant: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Nume: ");
                        String nume = scanner.nextLine().trim();
                        System.out.print("Strada: ");
                        String strada = scanner.nextLine().trim();
                        System.out.print("Oras: ");
                        String oras = scanner.nextLine().trim();
                        System.out.print("Cod postal: ");
                        String cod = scanner.nextLine().trim();
                        System.out.print("Categorie: ");
                        String categorie = scanner.nextLine().trim();
                        rs.addRestaurant(new Restaurant(id, nume, new Address(strada, oras, cod), categorie));
                    } catch (InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 2: // Adauga client
                    try {
                        System.out.print("ID client: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Nume: ");
                        String nume = scanner.nextLine().trim();
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Telefon (optional, Enter pentru a sari): ");
                        String telefon = scanner.nextLine().trim();
                        rs.addClient(new Client(id, nume, email, telefon.isEmpty() ? null : telefon));
                    } catch (InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 3: // Inregistreaza sofer
                    try {
                        System.out.print("ID sofer: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Nume: ");
                        String nume = scanner.nextLine().trim();
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Telefon: ");
                        String telefon = scanner.nextLine().trim();
                        os.registerDriver(new Driver(id, nume, email, telefon));
                    } catch (InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 4: // Adauga produs la meniu
                    try {
                        System.out.print("ID restaurant: ");
                        String restId = scanner.nextLine().trim();
                        System.out.print("ID produs: ");
                        String prodId = scanner.nextLine().trim();
                        System.out.print("Nume produs: ");
                        String numeProd = scanner.nextLine().trim();
                        System.out.print("Pret: ");
                        double pret = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Categorie: ");
                        String catProd = scanner.nextLine().trim();
                        System.out.print("Descriere (optional, Enter pentru a sari): ");
                        String descriere = scanner.nextLine().trim();
                        rs.addProductToMenu(restId, new Product(prodId, numeProd, pret, descriere.isEmpty() ? null : descriere, catProd));
                    } catch (RestaurantNotFoundException | InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Pret invalid.");
                    }
                    break;

                case 5: // Plaseaza comanda
                    try {
                        System.out.print("ID client: ");
                        String clientId = scanner.nextLine().trim();
                        System.out.print("ID restaurant: ");
                        String restId = scanner.nextLine().trim();
                        System.out.print("ID-uri produse (separate prin virgula, ex: P1,P2): ");
                        String[] produse = scanner.nextLine().trim().split(",");
                        System.out.print("Strada livrare: ");
                        String strada = scanner.nextLine().trim();
                        System.out.print("Oras livrare: ");
                        String oras = scanner.nextLine().trim();
                        System.out.print("Cod postal livrare: ");
                        String cod = scanner.nextLine().trim();
                        System.out.print("Detalii livrare (optional, Enter pentru a sari): ");
                        String detalii = scanner.nextLine().trim();

                        Client client = rs.findClientById(clientId);
                        if (client == null) {
                            System.out.println("Eroare: Client cu id=" + clientId + " nu a fost gasit.");
                            break;
                        }
                        Restaurant restaurant = rs.findRestaurantById(restId);
                        DeliveryAddress adresa = new DeliveryAddress("A" + (addrCounter++), strada, oras, cod, detalii.isEmpty() ? null : detalii);
                        List<String> idProduse = new ArrayList<>();
                        for (String produs : produse) {
                            idProduse.add(produs.trim());
                        }
                        os.placeOrder(client, restaurant, idProduse, adresa);
                    } catch (RestaurantNotFoundException | InvalidDataException | InvalidOrderException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 6: // Atribuie sofer la comanda
                    try {
                        System.out.print("ID comanda: ");
                        String orderId = scanner.nextLine().trim();
                        System.out.print("ID sofer: ");
                        String driverId = scanner.nextLine().trim();
                        os.assignDriver(orderId, driverId);
                    } catch (OrderNotFoundException | DriverNotFoundException | DriverUnavailableException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 7: // Finalizeaza comanda
                    try {
                        System.out.print("ID comanda: ");
                        String orderId = scanner.nextLine().trim();
                        os.completeOrder(orderId);
                    } catch (OrderNotFoundException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 8: // Cauta restaurante dupa categorie
                    try {
                        System.out.print("Categorie: ");
                        String categorie = scanner.nextLine().trim();
                        List<Restaurant> rezultate = rs.findByCategory(categorie);
                        if (rezultate.isEmpty()) {
                            System.out.println("Niciun restaurant gasit pentru categoria: " + categorie);
                        } else {
                            rezultate.forEach(r -> System.out.println("  " + r));
                        }
                    } catch (InvalidDataException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 9: // Comenzile unui client
                    try {
                        System.out.print("ID client: ");
                        String clientId = scanner.nextLine().trim();
                        List<Order> comenziClient = os.getClientOrders(clientId);
                        if (comenziClient.isEmpty()) {
                            System.out.println("Nicio comanda pentru clientul cu id=" + clientId);
                        } else {
                            comenziClient.forEach(o -> System.out.println("  " + o));
                        }
                    } catch (InvalidDataException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 10: // Comenzi active
                    List<Order> active = os.getActiveOrders();
                    if (active.isEmpty()) {
                        System.out.println("Nu exista comenzi active.");
                    } else {
                        active.forEach(o -> System.out.println("  " + o));
                    }
                    break;

                case 11: // Toate restaurantele
                    rs.getAllRestaurants().values().forEach(r -> System.out.println("  " + r));
                    break;

                case 12: // Toti soferii
                    os.getAllDrivers().forEach(d -> System.out.println("  " + d));
                    break;

                case 0:
                    System.out.println("La revedere!");
                    return;

                default:
                    System.out.println("Optiune invalida. Introdu un numar intre 0 si 12.");
            }
        }
    }
}
