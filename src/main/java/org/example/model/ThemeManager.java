package main.java.org.example.model;

import javafx.scene.Scene;
import java.util.Objects;

public class ThemeManager 
{
    private static boolean isHighContrast = false;
    private static final String CSS_PATH = "/org.openjfx/highContrast.css";

    public static void setHighContrast(boolean enabled) 
    {
        isHighContrast = enabled;
    }

    public static boolean isHighContrast() 
    {
        return isHighContrast;
    }

    public static void applyTheme(Scene scene) 
    {
        scene.getStylesheets().removeIf(css -> css.endsWith("highContrast.css"));
        
        if (isHighContrast) 
        {
            String cssUrl = Objects.requireNonNull(ThemeManager.class.getResource(CSS_PATH)).toExternalForm();
            scene.getStylesheets().add(cssUrl);
        }
    }
}