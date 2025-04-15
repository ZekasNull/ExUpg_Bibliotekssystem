package model;

import javax.persistence.*;

@Entity
@Table(name = "\"Genre\"", schema = "bibliotekssystem")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", nullable = false)
    private Integer id;

    @Column(name = "genre_namn", nullable = false, length = 25)
    private String genreNamn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGenreNamn() {
        return genreNamn;
    }

    public void setGenreNamn(String genreNamn) {
        this.genreNamn = genreNamn;
    }

}