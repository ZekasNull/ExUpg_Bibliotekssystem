package model;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "bok_författare",
            joinColumns = @JoinColumn(name = "bok_id"),
            inverseJoinColumns = @JoinColumn(name = "författare_id")
    )
    private List<Författare> Författare;

    @ManyToMany
    @JoinTable(
            name = "bok_ämnesord",
            joinColumns = @JoinColumn(name = "bok_id"),
            inverseJoinColumns = @JoinColumn(name = "ord_id")
    )
    private List<Ämnesord> Ämnesord;

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

}