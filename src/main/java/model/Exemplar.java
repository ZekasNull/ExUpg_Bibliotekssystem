package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Exemplar\"", schema = "bibliotekssystem")
public class Exemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streckkod", nullable = false)
    private Integer streckkod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id")
    private Film film_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bok_id")
    private Bok bok;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "\"låntyp\"", nullable = false)
    private Låneperiod låntyp;

    @Column(name = "\"tillgänglig\"", nullable = false)
    private Boolean tillgänglig = true;

    public Boolean getTillgänglig() {
        return tillgänglig;
    }

    public void setTillgänglig(Boolean tillgänglig) {
        this.tillgänglig = tillgänglig;
    }

    public Integer getStreckkod() {
        return streckkod;
    }

    public void setStreckkod(Integer id) {
        this.streckkod = id;
    }

    public Film getFilm_id() {
        return film_id;
    }

    public void setFilm_id(Film film) {
        this.film_id = film;
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

    public String getTitle() {
        if (bok != null) {
            return bok.getTitel();
        } else if (film_id != null) {
            return film_id.getTitel();
        }
        return "";
    }

    /**
     * Alternativ som "skapar" låntypen direkt från string.
     * TODO implementera som enum eller liknande för bättre funktion
     * @param låntyp String med en av låntyperna
     */
    public void setNewLåntyp(String låntyp) {
        Låneperiod period = new Låneperiod();
        period.setLåntyp(låntyp.toLowerCase());
        this.låntyp = period;
    }

    @Override
    public String toString() {
        return "Exemplar{" +
                "id=" + streckkod +
                ", låntyp=" + låntyp +
                ", tillgänglig=" + tillgänglig +
                '}';
    }
}