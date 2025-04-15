package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Tidsskrift\"", schema = "bibliotekssystem")
public class Tidsskrift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tidsskrift_id", nullable = false)
    private Integer id;

    @Column(name = "namn", nullable = false, length = 25)
    private String namn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

}