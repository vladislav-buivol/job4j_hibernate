package ru.job4j.hql;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "vacancy")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public static Vacancy of(String name) {
        return new Vacancy().setName(name);
    }

    public int getId() {
        return id;
    }

    public Vacancy setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Vacancy setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vacancy)) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return id == vacancy.id
                && Objects.equals(name, vacancy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Vacancy{"
                + "id=" + id
                + ", name='" + name
                + '\'' + '}';
    }
}
