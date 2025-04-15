package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Ämnesord\"", schema = "bibliotekssystem")
public class Ämnesord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ord_id", nullable = false)
    private Integer id;

    @Column(name = "ord", nullable = false, length = 25)
    private String ord;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrd() {
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }

}