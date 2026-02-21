module com.tlcsdm.fxrobotstudio {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.prefs;
    requires java.desktop;

    requires com.github.kwhat.jnativehook;
    requires org.controlsfx.controls;
    requires com.dlsc.preferencesfx;
    requires atlantafx.base;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material;
    requires org.kordamp.ikonli.materialdesign;

    requires jakarta.xml.bind;
    requires org.glassfish.jaxb.runtime;

    requires com.sun.jna;
    requires com.sun.jna.platform;

    requires org.bytedeco.javacv;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;

    opens com.tlcsdm.fxrobotstudio to javafx.fxml;
    opens com.tlcsdm.fxrobotstudio.controller to javafx.fxml;
    opens com.tlcsdm.fxrobotstudio.model to jakarta.xml.bind;

    exports com.tlcsdm.fxrobotstudio;
    exports com.tlcsdm.fxrobotstudio.model;
    exports com.tlcsdm.fxrobotstudio.controller;
    exports com.tlcsdm.fxrobotstudio.preferences;
    exports com.tlcsdm.fxrobotstudio.service;
    exports com.tlcsdm.fxrobotstudio.util;
}
