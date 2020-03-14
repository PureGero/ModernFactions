package com.modernfactions;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.soap.Text;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class MFTranslationManager {

    private static MFTranslationManager translationManager;

    public static MFTranslationManager get() {
        return translationManager;
    }

    private final JavaPlugin plugin;
    private final File translationsFolder;

    private final Map<String, MFTranslation> translations = new HashMap<>();
    private MFTranslation english = null;

    public MFTranslationManager(JavaPlugin plugin) {
        translationManager = this;

        this.plugin = plugin;
        this.translationsFolder = new File(plugin.getDataFolder(), "translations");

        copyEnglishIfNotExists();

        loadEnglish();

        loadTranslations();
    }

    private InputStream getEnglishResourceAsStream() {
        return getClass().getClassLoader().getResourceAsStream("en_us.properties");
    }

    private void copyEnglishIfNotExists() {
        translationsFolder.mkdirs();

        File en_us = new File(translationsFolder, "en_us.properties");
        if (!en_us.exists()) {
            try (InputStream in = getEnglishResourceAsStream()) {
                Files.copy(in, en_us.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadEnglish() {
        try (InputStream in = getEnglishResourceAsStream()) {
            english = loadTranslation("en_us", in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTranslations() {
        for (File potentialTranslationFile : translationsFolder.listFiles()) {
            String fileName = potentialTranslationFile.getName();

            if (!fileName.endsWith(".properties")) {
                continue;
            }

            try (FileInputStream in = new FileInputStream(potentialTranslationFile)) {
                loadTranslation(fileName, in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private MFTranslation loadTranslation(String fileName, InputStream in) throws IOException {
        String languageName = fileName.toLowerCase();

        if (languageName.indexOf('.') > 0) {
            languageName = languageName.substring(0, languageName.indexOf('.'));
        }

        MFTranslation translation = MFTranslation.load(in);

        translations.put(languageName, translation);

        return translation;
    }

    public BaseComponent[] getMessage(CommandSender sender, String name, Object... args) {
        String languageName = getLanguageName(sender);

        return getMessage(languageName, name, args);
    }

    private BaseComponent[] getMessage(String languageName, String name, Object[] args) {
        if (translations.containsKey(languageName)) {
            return getMessage(translations.get(languageName), name, args);
        } else {
            return getMessage(english, name, args);
        }
    }

    private BaseComponent[] getMessage(MFTranslation translation, String name, Object[] args) {
        if (translation.get(name) != null) {
            // Get from translation file

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof BaseComponent) {
                    args[i] = TextComponent.toPlainText((BaseComponent) args[i]);
                } else if (args[i] instanceof BaseComponent[]) {
                    args[i] = TextComponent.toPlainText((BaseComponent[]) args[i]);
                }
            }

            return TextComponent.fromLegacyText(String.format(translation.get(name), args));

        } else if (translation == english) {
            // This is the default translation, and it doesn't have it, must be a mojang translation
            TranslatableComponent component = new TranslatableComponent(name);
            for (Object arg : args) {
                component.addWith(arg.toString());
            }

            return new BaseComponent[] {component};

        } else if (translation.get("parent") != null) {
            // Has a parent, try getting it from there
            return getMessage(translation.get("parent"), name, args);

        } else {
            // No parent, try the default translations
            return getMessage(english, name, args);

        }
    }

    public String getLanguageName(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return player.getLocale().toLowerCase();
        } else {
            return null;
        }
    }
}
