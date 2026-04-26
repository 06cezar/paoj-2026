# Proiect Individual — Platformă Food Delivery

**Tema:** Platformă food delivery (restaurante, meniuri, comenzi, șoferi, clienți)  
**Pachet:** `com.pao.project.fooddelivery`

---

## 1. Tipuri de obiecte (≥8)

| # | Clasă | Descriere |
|---|-------|-----------|
| 1 | `Address` | Adresă fizică (stradă, oraș, cod poștal) — clasă de bază pentru adrese |
| 2 | `DeliveryAddress` | Adresa de livrare a clientului (extends Address, adaugă id și detalii) — imutabilă |
| 3 | `Restaurant` | Localul care oferă mâncare, are un meniu propriu și o adresă de tip Address |
| 4 | `Product` | Un item din meniu — imutabil (id, name, price, category, description opțională) |
| 5 | `Menu` | Colecția de produse a unui restaurant, cu PriceComparator pentru sortare |
| 6 | `Person` | Clasă abstractă — baza ierarhiei de persoane |
| 7 | `Client` | Utilizatorul care plasează comenzi (extends Person) |
| 8 | `Driver` | Curierii care livrează comenzile (extends Person) |
| 9 | `Order` | O comandă plasată de un client la un restaurant (Comparable după dată) |
| 10 | `OrderStatus` | Enum: PLACED, IN_PREPARATION, IN_DELIVERY, DELIVERED, CANCELLED |

---

## 2. Acțiuni / interogări posibile în sistem (≥10)

1. **Adaugă un restaurant** — înregistrează un restaurant nou în platformă
2. **Adaugă un client** — înregistrează un utilizator nou
3. **Înregistrează un șofer** — adaugă un curier disponibil pentru livrări
4. **Adaugă produs la meniu** — adaugă un produs în meniul unui restaurant
5. **Plasează o comandă** — un client selectează produse dintr-un restaurant
6. **Atribuie șofer la comandă** — asignează un curier disponibil comenzii
7. **Finalizează o comandă** — marchează comanda ca livrată, eliberează șoferul
8. **Caută restaurante după categorie** — filtrează restaurantele după tipul de mâncare
9. **Listează comenzile unui client** — afișează istoricul comenzilor unui client (sortate după dată)
10. **Afișează comenzile active** — toate comenzile care nu sunt livrate sau anulate
11. **Listează toate restaurantele** — afișează toate restaurantele înregistrate în platformă
12. **Listează toți șoferii** — afișează toți șoferii cu statusul și ratingul curent

---

## 3. Ierarhii și design

### Ierarhie persoane
```
Person (abstractă, getRole() abstract)
├── Client  — are listă de DeliveryAddress
└── Driver  — are rating (0-5) și disponibilitate
```

### Ierarhie adrese
```
Address  — street, city, postalCode (final, validări, toString/equals/hashCode)
└── DeliveryAddress  — adaugă id și details opțional (imutabilă, equals după id)
```

### Clase imutabile
- **`Product`** — toate câmpurile `final`, fără setteri
- **`DeliveryAddress`** — toate câmpurile `final`, fără setteri
- **`Address`** — toate câmpurile `final`, fără setteri

### Colecții folosite
- `Map<String, Restaurant>` (LinkedHashMap) — indexare rapidă după id în `RestaurantService`
- `List<Order>` — sortabilă prin `Comparable<Order>` după dată
- `List<Driver>`, `List<Client>`, `List<Product>` — colecții standard

### Sortare
- `Order implements Comparable<Order>` — sortare după `orderDate`
- `PriceComparator implements Comparator<Product>` — sortare produse după preț

---

## 4. Excepții custom

| Excepție | Tip | Când se aruncă |
|----------|-----|----------------|
| `InvalidDataException` | unchecked | câmpuri null/goale, date invalide |
| `DuplicateIdException` | unchecked | ID duplicat la adăugare |
| `InvalidOrderException` | unchecked | comandă fără produse, produs negăsit în meniu |
| `RestaurantNotFoundException` | checked | restaurant negăsit după id |
| `DriverNotFoundException` | checked | șofer negăsit după id |
| `OrderNotFoundException` | checked | comandă negăsită după id |
| `DriverUnavailableException` | checked | șofer indisponibil la atribuire |

---

## 5. Servicii Singleton

### `RestaurantService`
- `addRestaurant`, `findRestaurantById`, `getAllRestaurants`, `updateRestaurant`, `deleteRestaurant`
- `addClient`, `findClientById`, `getAllClients`, `deleteClient`
- `addProductToMenu`, `findByCategory`

### `OrderService`
- `placeOrder`, `findOrderById`, `getAllOrdersSorted`, `deleteOrder`
- `registerDriver`, `findDriverById`, `getAllDrivers`, `deleteDriver`
- `assignDriver`, `completeOrder`
- `getClientOrders`, `getActiveOrders`

---

## 6. Rulare

`Main.java` încarcă automat date inițiale (3 restaurante, 2 clienți, 2 șoferi, 5 produse), apoi pornește un meniu interactiv cu Scanner care acoperă toate cele 10 acțiuni:

```
===== Meniu principal =====
1.  Adauga restaurant
2.  Adauga client
3.  Inregistreaza sofer
4.  Adauga produs la meniu
5.  Plaseaza comanda
6.  Atribuie sofer la comanda
7.  Finalizeaza comanda
8.  Cauta restaurante dupa categorie
9.  Comenzile unui client
10. Comenzi active
11. Afiseaza toate restaurantele
12. Afiseaza toti soferii
0.  Iesire
```

---

## 7. Structura pachetelor

```
com.pao.project.fooddelivery/
├── model/
│   ├── Address.java
│   ├── DeliveryAddress.java
│   ├── Person.java
│   ├── Client.java
│   ├── Driver.java
│   ├── Restaurant.java
│   ├── Product.java
│   ├── Menu.java
│   ├── PriceComparator.java
│   ├── Order.java
│   └── OrderStatus.java
├── service/
│   ├── RestaurantService.java
│   └── OrderService.java
├── exception/
│   ├── InvalidDataException.java
│   ├── DuplicateIdException.java
│   ├── InvalidOrderException.java
│   ├── RestaurantNotFoundException.java
│   ├── DriverNotFoundException.java
│   ├── OrderNotFoundException.java
│   └── DriverUnavailableException.java
└── Main.java
```
