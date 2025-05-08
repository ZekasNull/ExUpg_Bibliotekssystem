package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"Låneperiod\"", schema = "bibliotekssystem")
public class Låneperiod {
    @Id
    @Column(name = "\"låntyp\"", nullable = false, length = 20)
    private String låntyp;

    @Column(name = "\"lånperiod\"", columnDefinition = "interval not null")
    private String lånperiod;

    public String getLåntyp() {
        return låntyp;
    }

    public void setLåntyp(String låntyp) {
        this.låntyp = låntyp;
    }

    public String getLånperiod() {
        return lånperiod;
    }

    @Override
    public String toString() {
        return "Låneperiod{" +
                "låntyp='" + låntyp + '\'' +
                ", lånperiod='" + lånperiod + '\'' +
                '}';
    }

}