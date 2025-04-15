package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"Låneperiod\"", schema = "bibliotekssystem")
public class Låneperiod {
    @Id
    @Column(name = "\"låntyp\"", nullable = false, length = 20)
    private String låntyp;

    public String getLåntyp() {
        return låntyp;
    }

    public void setLåntyp(String låntyp) {
        this.låntyp = låntyp;
    }

/*
Då låneperiod är ett intervall så kunde det inte läggas in direkt. Det finns två val:
1. Använd någon sorts hårdkodad enum då vi har begränsad antal typer av perioder
2. Koda logik att hämta intervallet i ett format Java kan hantera (stödjer då att databasen ändrar sig)
 TODO [Reverse Engineering] create field to map the '\"lånperiod\"' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "\"lånperiod\"", columnDefinition = "interval not null")
    private Object lånperiod;
*/
}