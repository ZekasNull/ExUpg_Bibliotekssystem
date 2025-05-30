package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Upplaga\"", schema = "bibliotekssystem")
public class Upplaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upplaga_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tidskrift_id", nullable = false)
    private Tidskrift tidskrift;

    @Column(name = "upplaga_nr", nullable = false)
    private Integer upplagaNr;

    @Column(name = "\"år\"", nullable = false)
    private Integer år;

    public Tidskrift getTidskrift() {
        return tidskrift;
    }

    public void setTidskrift(Tidskrift tidskrift) {
        this.tidskrift = tidskrift;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUpplaga_nr() {
        return upplagaNr;
    }

    public void setUpplagaNr(Integer upplagaNr) {
        this.upplagaNr = upplagaNr;
    }

    public Integer getÅr() {
        return år;
    }

    public void setÅr(Integer år) {
        this.år = år;
    }

}