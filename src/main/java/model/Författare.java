package model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Författare\"", schema = "bibliotekssystem")
public class Författare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "författare_id", nullable = false)
    private Integer id;

    @Column(name = "förnamn", nullable = false, length = 25)
    private String förnamn;

    @Column(name = "efternamn", nullable = false, length = 25)
    private String efternamn;

    @ManyToMany(mappedBy = "Författare")
    private Set<Bok> boks = new LinkedHashSet<>();

    public Set<Bok> getBoks() {
        return boks;
    }

    public void setBoks(Set<Bok> boks) {
        this.boks = boks;
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
        return "Författare{" +
                "efternamn='" + efternamn + '\'' +
                ", förnamn='" + förnamn + '\'' +
                '}';
    }
}