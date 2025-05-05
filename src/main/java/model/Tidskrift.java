package model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Tidskrift\"", schema = "bibliotekssystem")
public class Tidskrift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tidskrift_id", nullable = false)
    private Integer id;

    @Column(name = "namn", nullable = false, length = 25)
    private String namn;

    @OneToMany(mappedBy = "tidskrift")
    private Set<Upplaga> upplagas = new LinkedHashSet<>();

    public Set<Upplaga> getUpplagas() {
        return upplagas;
    }

    public void setUpplagas(Set<Upplaga> upplagas) {
        this.upplagas = upplagas;
    }

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