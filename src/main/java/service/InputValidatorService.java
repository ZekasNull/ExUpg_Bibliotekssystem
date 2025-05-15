package service;

import model.*;

public class InputValidatorService {
    // BOK
    public static boolean isValidBok(Bok bok) {
        return bok != null &&
                isValidIsbn(bok.getIsbn13()) &&
                isValidTitel(bok.getTitel());
    }

    private static boolean isValidIsbn(String isbn) {
        return isbn != null && isbn.matches("\\d{13}");
    }

    private static boolean isValidTitel(String titel) {
        return titel != null && titel.length() <= 50;
    }

    // ÄMNESORD
    public static boolean isValidAmnesord(Ämnesord ord) {
        return ord != null && ord.getOrd() != null && ord.getOrd().length() <= 25;
    }

    // FÖRFATTARE
    public static boolean isValidForfattare(Författare f) {
        return f != null &&
                isValidNamn(f.getFörnamn()) &&
                isValidNamn(f.getEfternamn());
    }

    private static boolean isValidNamn(String namn) {
        return namn != null && namn.length() <= 25;
    }

    // SKÅDESPELARE
    public static boolean isValidSkadespelare(Skådespelare s) {
        return s != null &&
                isValidNamn(s.getFörnamn()) &&
                isValidNamn(s.getEfternamn());
    }

    // FILM
    public static boolean isValidFilm(Film f) {
        return f != null &&
                isValidTitel(f.getTitel()) &&
                f.getProduktionsland() != null && f.getProduktionsland().length() <= 30 &&
                f.getÅldersgräns() >= 0;
    }

    // REGISSÖR
    public static boolean isValidRegissor(Regissör r) {
        return r != null &&
                isValidNamn(r.getFörnamn()) &&
                isValidNamn(r.getEfternamn());
    }

    // GENRE
    public static boolean isValidGenre(Genre g) {
        return g != null &&
                g.getGenreNamn() != null && g.getGenreNamn().length() <= 25;
    }

    // ANVÄNDARTYP
    public static boolean isValidAnvandartyp(Användartyp a) {
        return a != null &&
                a.getAnvändartyp() != null && a.getAnvändartyp().length() <= 20 &&
                a.getMaxLån() >= 0;
    }

    // ANVÄNDARE
    public static boolean isValidAnvandare(Användare a) {
        return a != null &&
                isValidUsername(a.getAnvändarnamn()) &&
                isValidPin(a.getPin()) &&
                a.getFulltNamn() != null && a.getFulltNamn().length() <= 50 &&
                a.getAnvändartyp() != null; // FK constraint
    }

    private static boolean isValidUsername(String username) {
        return username != null && username.length() <= 10;
    }

    private static boolean isValidPin(String pin) {
        return pin != null && pin.matches("\\d{4}");
    }

    // LÅNEPERIOD
    public static boolean isValidLanetyp(Låneperiod l) {
        return l != null &&
                l.getLåntyp() != null && l.getLåntyp().length() <= 20 &&
                l.getLånperiod() != null;
    }

    // EXEMPLAR
    public static boolean isValidExemplar(Exemplar e) {
        return e != null &&
                e.getLåntyp() != null &&
                ((e.getBok() == null && e.getFilm_id() != null) ||
                        (e.getBok() != null && e.getFilm_id() == null));
    }

    // LÅN
    public static boolean isValidLan(Lån lan) {
        return lan != null &&
                lan.getStreckkod() != null &&
                lan.getAnvändare() != null &&
                lan.getLånedatum() != null;
    }

    // TIDSKRIFT
    public static boolean isValidTidskrift(Tidskrift t) {
        return t != null &&
                t.getNamn() != null && t.getNamn().length() <= 25;
    }

    // UPPLAGA
    public static boolean isValidUpplaga(Upplaga u) {
        return u != null &&
                u.getTidskrift() != null &&
                u.getUpplagaNr() > 0 &&
                u.getÅr() >= 0;
    }

}
