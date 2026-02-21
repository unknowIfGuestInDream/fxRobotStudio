package com.tlcsdm.fxrobotstudio;

import com.tlcsdm.fxrobotstudio.preferences.AppPreferences;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AppPreferencesTest {

    @Test
    void testSingletonInstance() {
        AppPreferences prefs1 = AppPreferences.getInstance();
        AppPreferences prefs2 = AppPreferences.getInstance();
        assertSame(prefs1, prefs2);
    }

    @Test
    void testDefaultTheme() {
        AppPreferences prefs = AppPreferences.getInstance();
        assertNotNull(prefs.getTheme());
    }

    @Test
    void testGetLocale() {
        AppPreferences prefs = AppPreferences.getInstance();
        Locale locale = prefs.getLocale();
        assertNotNull(locale);
    }

    @Test
    void testLanguageProperty() {
        AppPreferences prefs = AppPreferences.getInstance();
        assertNotNull(prefs.languageProperty());
        assertNotNull(prefs.getLanguage());
    }

    @Test
    void testThemeProperty() {
        AppPreferences prefs = AppPreferences.getInstance();
        assertNotNull(prefs.themeProperty());
    }

}
