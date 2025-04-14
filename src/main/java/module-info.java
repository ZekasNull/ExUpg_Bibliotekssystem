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
    requires java.persistence;

    opens d0024e.exupg_bibliotekssystem to javafx.fxml;
    exports d0024e.exupg_bibliotekssystem;
}