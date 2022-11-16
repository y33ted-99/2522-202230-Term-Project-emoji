module ca.bcit.comp2522.termproject.emoji {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
//    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens ca.bcit.comp2522.termproject.emoji to javafx.fxml;
    exports ca.bcit.comp2522.termproject.emoji;
}