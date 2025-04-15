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
    @JoinColumn(name = "tidsskrift_id", nullable = false)
    private Tidsskrift tidsskrift;

    @Column(name = "upplaga_nr", nullable = false)
    private Integer upplagaNr;

    @Column(name = "\"år\"", nullable = false)
    private Integer år;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tidsskrift getTidsskrift() {
        return tidsskrift;
    }

    public void setTidsskrift(Tidsskrift tidsskrift) {
        this.tidsskrift = tidsskrift;
    }

    public Integer getUpplagaNr() {
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