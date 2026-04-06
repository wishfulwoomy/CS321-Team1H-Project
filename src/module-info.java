module main.java.org.example {
    requires java.xml;
    // Require JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Export your main package (so JavaFX can access it)
    exports main.java.org.example;
}