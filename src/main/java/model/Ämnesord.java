package model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Ämnesord\"", schema = "bibliotekssystem")
public class Ämnesord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ord_id", nullable = false)
    private Integer id;

    @Column(name = "ord", nullable = false, length = 25)
    private String ord;

    @ManyToMany(mappedBy = "Ämnesord")
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

    public String getOrd() {
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }

    @Override
    public String toString() {
        return "Ämnesord{" +
                "ord='" + ord + '\'' +
                '}';
    }
}