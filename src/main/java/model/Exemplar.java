package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Exemplar\"", schema = "bibliotekssystem")
public class Exemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streckkod", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bok_id")
    private Bok bok;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"låntyp\"", nullable = false)
    private Låneperiod låntyp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Bok getBok() {
        return bok;
    }

    public void setBok(Bok bok) {
        this.bok = bok;
    }

    public Låneperiod getLåntyp() {
        return låntyp;
    }

    public void setLåntyp(Låneperiod låntyp) {
        this.låntyp = låntyp;
    }

}