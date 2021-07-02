package org.pupperpals.pupperpals.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;


@Entity
public class Pupper {

    @Id
    @GeneratedValue (strategy= GenerationType.AUTO)
    private long id;
    private String name;
    private String breed;

    public Pupper() {
    }

    public Pupper(String name, String breed) {
        this.name = name;
        this.breed = breed;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        return "Pupper{" +
                "name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pupper pupper = (Pupper) o;
        return Objects.equals(name, pupper.name) && Objects.equals(breed, pupper.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, breed);
    }
}
