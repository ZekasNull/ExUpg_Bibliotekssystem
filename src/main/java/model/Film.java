package model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Film\"", schema = "bibliotekssystem")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", nullable = false)
    private Integer id;

    @Column(name = "titel", nullable = false, length = 50)
    private String titel;

    @Column(name = "produktionsland", nullable = false, length = 30)
    private String produktionsland;

    @Column(name = "åldersgräns", nullable = false)
    private Integer åldersgräns;

    @OneToMany(mappedBy = "film")
    private Set<Exemplar> exemplars = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "\"Film_Genre\"",
            schema = "bibliotekssystem",
            joinColumns = @JoinColumn(name = "film_jc_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_jc_id"))
    private Set<Genre> genres = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "\"Film_Regissör\"",
            schema = "bibliotekssystem",
            joinColumns = @JoinColumn(name = "film_jc_id", referencedColumnName = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "regissör_jc_id", referencedColumnName = "regissör_id"))
    private Set<Regissör> regissörs = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "\"Film_Skådespelare\"",
            schema = "bibliotekssystem",
            joinColumns = @JoinColumn(name = "film_jc_id", referencedColumnName = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "skådespelare_jc_id", referencedColumnName = "skådespelare_id"))
    private Set<Skådespelare> skådespelares = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getProduktionsland() {
        return produktionsland;
    }

    public void setProduktionsland(String produktionsland) {
        this.produktionsland = produktionsland;
    }

    public Integer getÅldersgräns() {
        return åldersgräns;
    }

    public void setÅldersgräns(Integer åldersgräns) {
        this.åldersgräns = åldersgräns;
    }

    public Set<Exemplar> getExemplars() {
        return exemplars;
    }

    public void setExemplars(Set<Exemplar> exemplars) {
        this.exemplars = exemplars;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Regissör> getRegissörs() {
        return regissörs;
    }

    public void setRegissörs(Set<Regissör> regissörs) {
        this.regissörs = regissörs;
    }

    public Set<Skådespelare> getSkådespelares() {
        return skådespelares;
    }

    public void setSkådespelares(Set<Skådespelare> skådespelares) {
        this.skådespelares = skådespelares;
    }

    @Override
    public String toString() {
        return "Film{" +
                "\nid=" + id +
                ",\n titel='" + titel + '\'' +
                ",\n produktionsland='" + produktionsland + '\'' +
                ",\n åldersgräns=" + åldersgräns +
                ",\n exemplars=" + exemplars +
                ",\n genres=" + genres +
                ",\n regissörs=" + regissörs +
                ",\n skådespelares=" + skådespelares +
                '}';
    }
}