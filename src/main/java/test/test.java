package test;

import javax.persistence.Persistence;

public class test {
    public static void main(String[] args) {
        Persistence.createEntityManagerFactory("default");
    }
}
