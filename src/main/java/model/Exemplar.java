package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Exemplar\"", schema = "bibliotekssystem")
public class Exemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streckkod", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bok_id")
    private Bok bok;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "\"låntyp\"", nullable = false)
    private Låneperiod låntyp;

    @Column(name = "\"tillgänglig\"", nullable = false)
    private Boolean tillgänglig = false;

    public Boolean getTillgänglig() {
        return tillgänglig;
    }

    public void setTillgänglig(Boolean tillgänglig) {
        this.tillgänglig = tillgänglig;
    }

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

    @Override
    public String toString() {
        return "Exemplar{" +
                "id=" + id +
                ", låntyp=" + låntyp +
                ", tillgänglig=" + tillgänglig +
                '}';
    }
}