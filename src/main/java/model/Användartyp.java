package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"Användartyp\"", schema = "bibliotekssystem")
public class Användartyp {
    @Id
    @Column(name = "\"användartyp\"", nullable = false, length = 20)
    private String användartyp;

    @Column(name = "\"max_lån\"", nullable = false)
    private Short maxLån;

    public String getAnvändartyp() {
        return användartyp;
    }

    public void setAnvändartyp(String användartyp) {
        this.användartyp = användartyp;
    }

    public Short getMaxLån() {
        return maxLån;
    }

    public void setMaxLån(Short maxLån) {
        this.maxLån = maxLån;
    }

}