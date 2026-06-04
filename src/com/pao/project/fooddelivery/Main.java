package com.pao.project.fooddelivery;

import com.pao.project.fooddelivery.exception.ClientNotFoundException;
import com.pao.project.fooddelivery.exception.DriverNotFoundException;
import com.pao.project.fooddelivery.exception.DriverUnavailableException;
import com.pao.project.fooddelivery.exception.DuplicateIdException;
import com.pao.project.fooddelivery.exception.InvalidDataException;
import com.pao.project.fooddelivery.exception.InvalidOrderException;
import com.pao.project.fooddelivery.exception.OrderNotFoundException;
import com.pao.project.fooddelivery.exception.RestaurantNotFoundException;
import com.pao.project.fooddelivery.model.*;
import com.pao.project.fooddelivery.repository.ClientRepository;
import com.pao.project.fooddelivery.repository.DriverRepository;
import com.pao.project.fooddelivery.repository.OrderRepository;
import com.pao.project.fooddelivery.repository.RestaurantRepository;
import com.pao.project.fooddelivery.service.OrderService;
import com.pao.project.fooddelivery.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        RestaurantService restaurantService = RestaurantService.getInstance();
        OrderService orderService = OrderService.getInstance();

        preloadDate(restaurantService, orderService);

        Scanner scanner = new Scanner(System.in);
        runMenu(restaurantService, orderService, scanner);

        demonstratieBazaDeDate(restaurantService, orderService);

        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Date initiale pentru demo
    // -------------------------------------------------------------------------
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
            rs.addProductToMenu("R1", new Product("P1", "Pizza Margherita",        35.00, "Pizza clasica cu mozzarella",      "Pizza"));
            rs.addProductToMenu("R1", new Product("P2", "Pizza Quattro Formaggi",  42.00, "Pizza cu 4 tipuri de branza",      "Pizza"));
            rs.addProductToMenu("R2", new Product("P3", "BigBurger",               28.50, "Burger cu carne de vita",          "Burger"));
            rs.addProductToMenu("R2", new Product("P4", "CrispyChicken",           24.00, "Burger cu pui crocant",            "Burger"));
            rs.addProductToMenu("R3", new Product("P5", "Salmon Roll (8 buc)",     38.00, "Rulouri cu somon proaspat",        "Sushi"));
        } catch (RestaurantNotFoundException e) {
            System.err.println("Eroare preload: " + e.getMessage());
        }

        System.out.println("\n[INFO] Date incarcate. Poti incepe!\n");
    }

    // -------------------------------------------------------------------------
    // Meniu interactiv
    // -------------------------------------------------------------------------
    private static void runMenu(RestaurantService rs, OrderService os, Scanner scanner) {
        int addrCounter = 10;

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
            System.out.println("13. Anuleaza comanda");
            System.out.println("0.  Iesire (si demo baza de date)");
            System.out.print("Alege optiunea: ");

            int optiune;
            try {
                optiune = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Optiune invalida. Introdu un numar.");
                continue;
            }

            switch (optiune) {

                case 1:
                    try {
                        System.out.print("ID restaurant: ");   String id   = scanner.nextLine().trim();
                        System.out.print("Nume: ");             String nume = scanner.nextLine().trim();
                        System.out.print("Strada: ");           String str  = scanner.nextLine().trim();
                        System.out.print("Oras: ");             String oras = scanner.nextLine().trim();
                        System.out.print("Cod postal: ");       String cod  = scanner.nextLine().trim();
                        System.out.print("Categorie: ");        String cat  = scanner.nextLine().trim();
                        rs.addRestaurant(new Restaurant(id, nume, new Address(str, oras, cod), cat));
                    } catch (InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("ID client: ");  String id    = scanner.nextLine().trim();
                        System.out.print("Nume: ");        String nume  = scanner.nextLine().trim();
                        System.out.print("Email: ");       String email = scanner.nextLine().trim();
                        System.out.print("Telefon (Enter pt a sari): ");
                        String tel = scanner.nextLine().trim();
                        rs.addClient(new Client(id, nume, email, tel.isEmpty() ? null : tel));
                    } catch (InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.print("ID sofer: ");  String id    = scanner.nextLine().trim();
                        System.out.print("Nume: ");       String nume  = scanner.nextLine().trim();
                        System.out.print("Email: ");      String email = scanner.nextLine().trim();
                        System.out.print("Telefon: ");    String tel   = scanner.nextLine().trim();
                        os.registerDriver(new Driver(id, nume, email, tel));
                    } catch (InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        System.out.print("ID restaurant: ");         String restId   = scanner.nextLine().trim();
                        System.out.print("ID produs: ");              String prodId   = scanner.nextLine().trim();
                        System.out.print("Nume produs: ");            String numeProd = scanner.nextLine().trim();
                        System.out.print("Pret: ");                   double pret     = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Categorie: ");              String catProd  = scanner.nextLine().trim();
                        System.out.print("Descriere (Enter pt sari):"); String desc   = scanner.nextLine().trim();
                        rs.addProductToMenu(restId, new Product(prodId, numeProd, pret, desc.isEmpty() ? null : desc, catProd));
                    } catch (RestaurantNotFoundException | InvalidDataException | DuplicateIdException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Pret invalid.");
                    }
                    break;

                case 5:
                    try {
                        System.out.print("ID client: ");     String clientId = scanner.nextLine().trim();
                        System.out.print("ID restaurant: "); String restId   = scanner.nextLine().trim();
                        System.out.print("ID-uri produse (ex: P1,P2): ");
                        String[] produse = scanner.nextLine().trim().split(",");
                        System.out.print("Strada livrare: ");     String str   = scanner.nextLine().trim();
                        System.out.print("Oras livrare: ");       String oras  = scanner.nextLine().trim();
                        System.out.print("Cod postal livrare: "); String cod   = scanner.nextLine().trim();
                        System.out.print("Detalii (Enter pt sari): ");
                        String det = scanner.nextLine().trim();

                        Client client = rs.findClientById(clientId);
                        Restaurant restaurant = rs.findRestaurantById(restId);
                        DeliveryAddress adresa = new DeliveryAddress("A" + (addrCounter++), str, oras, cod, det.isEmpty() ? null : det);
                        List<String> ids = new ArrayList<>();
                        for (String p : produse) ids.add(p.trim());
                        os.placeOrder(client, restaurant, ids, adresa);
                    } catch (ClientNotFoundException | RestaurantNotFoundException |
                             InvalidDataException | InvalidOrderException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        System.out.print("ID comanda: "); String orderId  = scanner.nextLine().trim();
                        System.out.print("ID sofer: ");   String driverId = scanner.nextLine().trim();
                        os.assignDriver(orderId, driverId);
                    } catch (OrderNotFoundException | DriverNotFoundException | DriverUnavailableException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 7:
                    try {
                        System.out.print("ID comanda: ");
                        os.completeOrder(scanner.nextLine().trim());
                    } catch (OrderNotFoundException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 8:
                    try {
                        System.out.print("Categorie: ");
                        List<Restaurant> rez = rs.findByCategory(scanner.nextLine().trim());
                        if (rez.isEmpty()) System.out.println("Niciun restaurant gasit.");
                        else rez.forEach(r -> System.out.println("  " + r));
                    } catch (InvalidDataException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 9:
                    try {
                        System.out.print("ID client: ");
                        List<Order> comenzi = os.getClientOrders(scanner.nextLine().trim());
                        if (comenzi.isEmpty()) System.out.println("Nicio comanda.");
                        else comenzi.forEach(o -> System.out.println("  " + o));
                    } catch (InvalidDataException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 10:
                    List<Order> active = os.getActiveOrders();
                    if (active.isEmpty()) System.out.println("Nu exista comenzi active.");
                    else active.forEach(o -> System.out.println("  " + o));
                    break;

                case 11:
                    rs.getAllRestaurants().values().forEach(r -> System.out.println("  " + r));
                    break;

                case 12:
                    os.getAllDrivers().forEach(d -> System.out.println("  " + d));
                    break;

                case 13:
                    try {
                        System.out.print("ID comanda: ");
                        os.cancelOrder(scanner.nextLine().trim());
                    } catch (OrderNotFoundException | InvalidOrderException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Iesire din meniu.");
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Demonstratie persistenta JDBC
    // -------------------------------------------------------------------------
    private static void demonstratieBazaDeDate(RestaurantService rs, OrderService os) {
        System.out.println("\n\n========================================");
        System.out.println("   DEMONSTRATIE BAZA DE DATE (JDBC)");
        System.out.println("========================================");

        RestaurantRepository restaurantRepo = RestaurantRepository.getInstance();
        ClientRepository     clientRepo     = ClientRepository.getInstance();
        DriverRepository     driverRepo     = DriverRepository.getInstance();
        OrderRepository      orderRepo      = OrderRepository.getInstance();

        // ----- SAVE -----
        System.out.println("\n--- Salvare date in baza de date ---");
        try {
            for (Restaurant r : rs.getAllRestaurants().values()) {
                restaurantRepo.save(r);
                for (Product p : r.getMenu().getProducts()) {
                    restaurantRepo.saveProduct(p, r.getId());
                }
            }
            for (Client c : rs.getAllClients()) {
                clientRepo.save(c);
            }
            for (Driver d : os.getAllDrivers()) {
                driverRepo.save(d);
            }
            for (Order o : os.getAllOrdersSorted()) {
                // adresa de livrare trebuie sa existe deja in delivery_addresses
                // (salvata odata cu clientul) sau o salvam acum daca e noua
                clientRepo.saveAddress(o.getDeliveryAddress(), o.getClient().getId());
                orderRepo.save(o);
            }
            System.out.println("[OK] Toate datele au fost salvate in baza de date.");
        } catch (Exception e) {
            System.out.println("[ERR] Eroare la salvare: " + e.getMessage());
        }

        // ----- FIND ALL -----
        System.out.println("\n--- Restaurante din baza de date (findAll) ---");
        restaurantRepo.findAll().forEach(r -> System.out.println("  " + r));

        System.out.println("\n--- Clienti din baza de date (findAll) ---");
        clientRepo.findAll().forEach(c -> System.out.println("  " + c));

        System.out.println("\n--- Soferi din baza de date (findAll) ---");
        driverRepo.findAll().forEach(d -> System.out.println("  " + d));

        System.out.println("\n--- Comenzi din baza de date (findAll) ---");
        orderRepo.findAll().forEach(o -> System.out.println("  " + o));

        // ----- FIND BY ID -----
        System.out.println("\n--- findById: Restaurant R1 ---");
        Optional<Restaurant> r1 = restaurantRepo.findById("R1");
        r1.ifPresent(r -> System.out.println("  " + r));

        // ----- UPDATE -----
        System.out.println("\n--- update: Restaurant R1 -> schimbare categorie ---");
        r1.ifPresent(r -> {
            r.setCategory("Pizza & Paste");
            restaurantRepo.update(r);
            restaurantRepo.findById("R1").ifPresent(updated -> System.out.println("  Dupa update: " + updated));
        });

        // ----- DELETE -----
        System.out.println("\n--- delete: Driver D2 ---");
        try {
            driverRepo.delete("D2");
            System.out.println("  Soferi ramasi: " + driverRepo.findAll().size());
        } catch (RuntimeException e) {
            System.out.println("  [WARN] Nu s-a putut sterge D2: " + e.getMessage());
        }

        // ----- JOIN-URI -----
        System.out.println("\n--- JOIN 1: Toate comenzile cu client si restaurant ---");
        orderRepo.findOrdersWithClientAndRestaurant().forEach(row -> System.out.println("  " + row));

        System.out.println("\n--- JOIN 2: Comenzile clientului C1 ---");
        orderRepo.findOrdersByClientId("C1").forEach(row -> System.out.println("  " + row));

        System.out.println("\n--- JOIN 3: Comenzi active cu sofer ---");
        List<String> active = orderRepo.findActiveOrdersWithDriver();
        if (active.isEmpty()) System.out.println("  (nicio comanda activa)");
        else active.forEach(row -> System.out.println("  " + row));

        System.out.println("\n[INFO] Fisierul audit.csv a fost actualizat cu toate actiunile executate.");
        System.out.println("========================================\n");
    }
}
