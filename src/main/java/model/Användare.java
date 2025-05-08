package model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Användare\"", schema = "bibliotekssystem")
public class Användare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"användare_id\"", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "\"användartyp\"", nullable = false)
    private Användartyp användartyp;

    @Column(name = "\"användarnamn\"", nullable = false, length = 10)
    private String användarnamn;

    @Column(name = "pin", nullable = false, length = 4)
    private String pin;

    @Column(name = "fullt_namn", nullable = false, length = 50)
    private String fulltNamn;

    @OneToMany(mappedBy = "användare", fetch = FetchType.EAGER)
    private Set<Lån> låns = new LinkedHashSet<>();

    public Set<Lån> getLåns() {
        return låns;
    }

    public void setLåns(Set<Lån> låns) {
        this.låns = låns;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Användartyp getAnvändartyp() {
        return användartyp;
    }

    public void setAnvändartyp(Användartyp användartyp) {
        this.användartyp = användartyp;
    }

    public String getAnvändarnamn() {
        return användarnamn;
    }

    public void setAnvändarnamn(String användarnamn) {
        this.användarnamn = användarnamn;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFulltNamn() {
        return fulltNamn;
    }

    public void setFulltNamn(String fulltNamn) {
        this.fulltNamn = fulltNamn;
    }

    @Override
    public String toString() {
        return "Användare{" +
                "id=" + id +
                ", användartyp=" + användartyp +
                ", användarnamn='" + användarnamn + '\'' +
                ", pin='" + pin + '\'' +
                ", fulltNamn='" + fulltNamn + '\'' +
                ", låns=" + låns +
                '}';
    }
}