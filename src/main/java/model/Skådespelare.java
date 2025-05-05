package model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Skådespelare\"", schema = "bibliotekssystem")
public class Skådespelare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skådespelare_id", nullable = false)
    private Integer id;

    @Column(name = "förnamn", nullable = false, length = 25)
    private String förnamn;

    @Column(name = "efternamn", nullable = false, length = 25)
    private String efternamn;

    @ManyToMany(mappedBy = "skådespelares")
    private Set<Film> films = new LinkedHashSet<>();

    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFörnamn() {
        return förnamn;
    }

    public void setFörnamn(String förnamn) {
        this.förnamn = förnamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public void setEfternamn(String efternamn) {
        this.efternamn = efternamn;
    }

    @Override
    public String toString() {
        return "Skådespelare{" +
                "efternamn='" + efternamn + '\'' +
                ", förnamn='" + förnamn + '\'' +
                '}';
    }
}