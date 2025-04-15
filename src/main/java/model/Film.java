package model;

import javax.persistence.*;

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

    @Column(name = "\"åldersgräns\"", nullable = false)
    private Integer åldersgräns;

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

}