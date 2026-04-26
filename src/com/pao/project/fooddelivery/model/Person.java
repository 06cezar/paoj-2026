package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

public abstract class Person {
    private final String id;
    private String name;
    private String email;
    private String phone; // optional

    public Person(String id, String name, String email, String phone) {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul este obligatoriu.");
        if (name == null || name.isEmpty()) throw new InvalidDataException("Numele este obligatoriu.");
        if (email == null || email.isEmpty()) throw new InvalidDataException("Email-ul este obligatoriu.");

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public abstract String getRole();

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setName(String name) { 
        if (name == null || name.isEmpty()) throw new InvalidDataException("Numele este obligatoriu.");
        this.name = name; 
    }

    public void setEmail(String email) { 
        if (email == null || email.isEmpty()) throw new InvalidDataException("Email-ul este obligatoriu.");
        this.email = email;
    }
    
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return getRole() + "[id=" + id + ", nume=" + name + ", email=" + email + ", telefon=" + phone;
        // nu inchid ] intrucat e clasa abstracta; o sa inchid dupa ce afisez si restul
        // campurilor in subclase
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person p = (Person) o;
        return id.equals(p.id); // equals din cls string - id nu poate fi null
                                // din constructor
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
