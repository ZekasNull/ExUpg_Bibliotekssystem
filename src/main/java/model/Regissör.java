package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Regissör\"", schema = "bibliotekssystem")
public class Regissör {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"regissör_id\"", nullable = false)
    private Integer id;

    @Column(name = "\"förnamn\"", nullable = false, length = 25)
    private String förnamn;

    @Column(name = "efternamn", nullable = false, length = 25)
    private String efternamn;

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

}