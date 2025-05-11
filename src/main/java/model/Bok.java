package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "\"Bok\"", schema = "bibliotekssystem")
public class Bok {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bok_id", nullable = false)
    private Integer id;

    @Column(name = "isbn_13", nullable = false, length = 13)
    private String isbn13;

    @Column(name = "titel", nullable = false, length = 50)
    private String titel;

    public void setFörfattare(Set<Författare> författare) {
        Författare = författare;
    }
    public Set<Författare> getFörfattare() {
        return Författare;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "\"Bok_Författare\"",
            schema = "bibliotekssystem",
            joinColumns = @JoinColumn(name = "bok_jc_id"),
            inverseJoinColumns = @JoinColumn(name = "författare_jc_id")
    )
    private Set<Författare> Författare;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "\"Bok_Ämnesord\"",
            schema = "bibliotekssystem",
            joinColumns = @JoinColumn(name = "bok_jc_id"),
            inverseJoinColumns = @JoinColumn(name = "ord_jc_id")
    )
    private Set<Ämnesord> Ämnesord;

    //Getters för båda så sökningen kan skriva ut de i deras separata kolumner
    public Set<Ämnesord> getÄmnesord() {
        return Ämnesord;
    }



    @OneToMany(mappedBy = "bok", fetch = FetchType.EAGER)
    private Set<Exemplar> exemplars = new LinkedHashSet<>();

    public Set<Exemplar> getExemplars() {
        return exemplars;
    }

    public void setExemplars(Set<Exemplar> exemplars) {
        this.exemplars = exemplars;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Bok() {
        this.Författare = new HashSet<>();
        this.Ämnesord = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Bok{" +
                "id=" + id +
                ", isbn13='" + isbn13 + '\'' +
                ", titel='" + titel + '\'' +
                ", Författare=" + Författare +
                ", Ämnesord=" + Ämnesord +
                ", exemplars=" + exemplars +
                '}';
    }
}