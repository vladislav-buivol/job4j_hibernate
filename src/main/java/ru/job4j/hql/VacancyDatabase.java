package ru.job4j.hql;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "vacancyDatabase")
public class VacancyDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vacancy> vacancy;

    private String name;

    public static VacancyDatabase of(String name, Set<Vacancy> vacancy) {
        return new VacancyDatabase()
                .setName(name)
                .setVacancy(vacancy);
    }

    public int getId() {
        return id;
    }

    public VacancyDatabase setId(int id) {
        this.id = id;
        return this;
    }

    public Set<Vacancy> getVacancy() {
        return vacancy;
    }

    public VacancyDatabase setVacancy(Set<Vacancy> vacancy) {
        this.vacancy = vacancy;
        return this;
    }

    public String getName() {
        return name;
    }

    public VacancyDatabase setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "VacancyDatabase{" +
                "id=" + id
                + ", vacancy=" + vacancy
                + ", name='" + name + '\''
                + '}';
    }
}
