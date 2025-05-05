package model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Genre\"", schema = "bibliotekssystem")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", nullable = false)
    private Integer id;

    @Column(name = "genre_namn", nullable = false, length = 25)
    private String genreNamn;

    @ManyToMany(mappedBy = "genres")
    private Set<Film> films = new LinkedHashSet<>();

    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }

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

    @Override
    public String toString() {
        return "Genre{" +
                ", genreNamn='" + genreNamn + '\'' +
                '}';
    }
}