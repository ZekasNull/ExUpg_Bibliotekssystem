package service;

import javafx.util.Pair;
import model.*;

public class InputValidatorService {
    // BOK
    public static Pair<Boolean, String> isValidBok(Bok bok) {
        if(bok.getTitel().length() > 50) return new Pair<>(false,
                "Bokens titel är för lång: " + bok.getTitel().length() + " av max 50 tecken.");
        if(!isValidIsbn(bok.getIsbn13())) return new Pair<>(false, "ISBN har ogiltigt format");

        for (Ämnesord a : bok.getÄmnesord()) {
            if(a.getOrd().length() > 25) return new Pair<>(false,
                    "Ordet " + a.getOrd() + " är för långt " + a.getOrd().length() + " av max 25 tecken.");
        }

        for (Författare f : bok.getFörfattare()) {
            if(!isValidName(f.getFörnamn()) || !isValidName(f.getEfternamn())) return new Pair<>(false,
                    f.getFörnamn() + " " + f.getEfternamn()+ ": För- eller efternamnet är för långt (max 25 tecken)");
        }

        //else true
        return new Pair<>(true, "");
    }

    public static Pair<Boolean, String> isValidFilm(Film film) {
        if(film.getTitel().length() > 50) return new Pair<>(false, "Filmens titel är för lång: " + film.getTitel().length() + " av max 50 tecken.");
        if(film.getÅldersgräns() <= 0) return new Pair<>(false, "Ogiltig åldersgräns");
        if (film.getProduktionsland().length() > 30) return new Pair<>(false, "Produktionslandet är för långt: " + film.getProduktionsland().length() + " av max 30 tecken.");

        for (Genre g : film.getGenres()) {
            if(g.getGenreNamn().length() > 25) return new Pair<>(false, "Genrenamnet " + g.getGenreNamn() + "är för långt: " + g.getGenreNamn().length() + " av max 25 tecken.");
        }

        for (Skådespelare s : film.getSkådespelares()) {
            if (!isValidName(s.getFörnamn()) || !isValidName(s.getEfternamn())) return new Pair<>(false, "Skådespelare " + s.getFörnamn() + " " +  s.getEfternamn() + ": För- eller efternamnet är för långt (max 25 tecken)");
        }

        for (Regissör r : film.getRegissörs()) {
            if (!isValidName(r.getFörnamn()) || !isValidName(r.getEfternamn())) return new Pair<>(false, "Regissör " + r.getFörnamn() + " " +  r.getEfternamn() + ": För- eller efternamnet är för långt (max 25 tecken)");
        }

        return new Pair<>(true, "");
    }

    private static boolean isValidName(String name) {
        return name.length() <= 25;
    }

    private static boolean isValidIsbn(String isbn) {
        return isbn.matches("\\d{13}");
    }

}
