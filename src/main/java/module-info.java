module d0024e.exupg_bibliotekssystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jdk.compiler;
    requires java.persistence; //fixme ambiguous

    opens test to org.hibernate.orm.core;
    exports test;

    exports d0024e.exupg_bibliotekssystem;
    opens d0024e.exupg_bibliotekssystem to javafx.fxml, javafx.graphics;
}