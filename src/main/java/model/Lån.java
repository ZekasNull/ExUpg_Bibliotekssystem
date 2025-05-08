package model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "\"Lån\"", schema = "bibliotekssystem")
public class Lån {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"lån_id\"", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "streckkod", nullable = false)
    private Exemplar streckkod;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "\"användare_id\"", nullable = false)
    private Användare användare;

    @Column(name = "\"lånedatum\"", nullable = false, insertable = false, updatable = false)
    private Instant lånedatum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Exemplar getStreckkod() {
        return streckkod;
    }

    public void setStreckkod(Exemplar streckkod) {
        this.streckkod = streckkod;
    }

    public Användare getAnvändare() {
        return användare;
    }

    public void setAnvändare(Användare användare) {
        this.användare = användare;
    }

    public Instant getLånedatum() {
        return lånedatum;
    }

    public void setLånedatum(Instant lånedatum) {
        this.lånedatum = lånedatum;
    }

    @Override
    public String toString() {
        return "Lån{" +
                "id=" + id +
                ", streckkod=" + streckkod +
                ", lånedatum=" + lånedatum +
                '}';
    }
}