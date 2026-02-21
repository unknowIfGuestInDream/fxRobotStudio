package com.tlcsdm.fxrobotstudio;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.tlcsdm.fxrobotstudio.preferences.AppPreferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public class FxRobotStudio extends Application {

    private static final Logger log = LoggerFactory.getLogger(FxRobotStudio.class);

    public static final String BUNDLE_BASE_NAME = "com.tlcsdm.fxrobotstudio.i18n.messages";

    private static ResourceBundle resourceBundle;

    public static ResourceBundle getBundle() {
        return resourceBundle;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final AppPreferences preferences = AppPreferences.getInstance();
        final Locale locale = preferences.getLocale();
        Locale.setDefault(locale);

        applyTheme(preferences.getTheme());

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
        resourceBundle = bundle;
        final FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("fxml/main.fxml"), bundle);
        final Parent root = loader.load();

        final Scene scene = new Scene(root);
        scene.getStylesheets().add("css/style.css");
        primaryStage.setScene(scene);

        InputStream iconStream = getClass().getClassLoader().getResourceAsStream("icons/logo.png");
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        }

        primaryStage.setTitle(bundle.getString("app.title"));
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        log.info("fxRobotStudio started");
    }

    public static void applyTheme(String themeName) {
        String stylesheet = switch (themeName) {
            case "Default" -> null;
            case "Primer Dark" -> new PrimerDark().getUserAgentStylesheet();
            case "Nord Light" -> new NordLight().getUserAgentStylesheet();
            case "Nord Dark" -> new NordDark().getUserAgentStylesheet();
            case "Cupertino Light" -> new CupertinoLight().getUserAgentStylesheet();
            case "Cupertino Dark" -> new CupertinoDark().getUserAgentStylesheet();
            case "Dracula" -> new Dracula().getUserAgentStylesheet();
            default -> new PrimerLight().getUserAgentStylesheet();
        };
        Application.setUserAgentStylesheet(stylesheet);
    }

}
