package br.com.skyplugins.skydiscordwhitelist;

import br.com.skyplugins.skydiscordwhitelist.Listeners.PlayerJoin;
import br.com.skyplugins.skydiscordwhitelist.Listeners.WhitelistListener;
import com.google.common.io.Resources;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class SkyPlugins extends JavaPlugin {

    public static SkyPlugins plugin;
    private JDA jda;
    public static List<String> whitelist = new ArrayList<>();

    @Override
    public void onEnable() {
        send(" ");
        send("&a[SkyDiscordWhitelist]: &aIniciando SkyDiscordWhitelist...");
        send("&a  ___________          ________  .__                              ._____      __.__    .__  __         .__  .__         __   ");
        send("&a /   _____|  | _____.__\\______ \\ |__| ______ ____  ___________  __| _/  \\    /  |  |__ |___/  |_  ____ |  | |__| ______/  |_ ");
        send("&a \\_____  \\|  |/ <   |  ||    |  \\|  |/  ____/ ___\\/  _ \\_  __ \\/ __ |\\   \\/\\/   |  |  \\|  \\   ___/ __ \\|  | |  |/  ___\\   __\\");
        send("&a /        |    < \\___  ||    `   |  |\\___ \\\\  \\__(  <_> |  | \\/ /_/ | \\        /|   Y  |  ||  | \\  ___/|  |_|  |\\___ \\ |  |  ");
        send("&a/_______  |__|_ \\/ ____/_______  |__/____  >\\___  \\____/|__|  \\____ |  \\__/\\  / |___|  |__||__|  \\___  |____|__/____  >|__|  ");
        send("&a        \\/     \\/\\/            \\/        \\/     \\/                 \\/       \\/       \\/              \\/             \\/       ");
        send("&a Copyright SkyPlugins - www.skyplugins.com.br (Todos os direitos reservados)");
        send(" ");
        plugin = this;
        saveDefaultConfig();
        whitelist = loadWhitelist();

        try {
            File file = new File(getDataFolder(), "config.yml");
            String allText = Resources.toString(file.toURI().toURL(), StandardCharsets.UTF_8);
            getConfig().loadFromString(allText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerEvents();
        startBot();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    private void startBot() {
        String botToken = getConfig().getString("Discord.bot.token");

        if (botToken == null || botToken.isEmpty()) {
            getLogger().severe("Error: Discord bot token is missing or empty in the config file.");
            return;
        }

        JDABuilder builder = JDABuilder.createDefault(botToken);
        builder.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.addEventListeners(new WhitelistListener());

        String activityStr = getConfig().getString("Discord.bot.activity");
        Activity activity = null;

        // Set the bot's activity
        if(activityStr != null && !activityStr.isEmpty()) {
            activity = Activity.of(Activity.ActivityType.valueOf(activityStr), getConfig().getString("Discord.bot.info"));
        }
        if (activity == null) {
            // If activity is not set or invalid, default to watching the website
            activity = Activity.watching("SKYPLUGINS.COM.BR");
        }
        builder.setActivity(activity);

        String statusStr = getConfig().getString("Discord.bot.status");
        OnlineStatus status = OnlineStatus.valueOf(statusStr);

        builder.setStatus(status);

        try {
            jda = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<String> loadWhitelist() {
        List<String> whitelist = new ArrayList<>();

            try {
                Path whitelistFile = getDataFolder().toPath().resolve("whitelist.txt");
                if (!Files.exists(whitelistFile)) {
                    Files.createFile(whitelistFile);
                }

                List<String> lines = Files.readAllLines(whitelistFile, StandardCharsets.UTF_8);

                for (String line : lines) {
                    whitelist.add(line.toLowerCase());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return whitelist;
    }

    public void saveWhitelist() {
            try {
                File whitelistFile = new File(getDataFolder(), "whitelist.txt");
                if (!whitelistFile.exists()) {
                    whitelistFile.createNewFile();
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(whitelistFile));
                for (String name : whitelist) {
                    writer.write(name);
                    writer.newLine();
                }
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void send(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));
    }

    @Override
    public void onDisable() {
        send(" ");
        send("&c[SkyDiscordWhitelist]: Plugin desabilitado com sucesso.");
        send("&c  ___________          ________  .__                              ._____      __.__    .__  __         .__  .__         __   ");
        send("&c /   _____|  | _____.__\\______ \\ |__| ______ ____  ___________  __| _/  \\    /  |  |__ |___/  |_  ____ |  | |__| ______/  |_ ");
        send("&c \\_____  \\|  |/ <   |  ||    |  \\|  |/  ____/ ___\\/  _ \\_  __ \\/ __ |\\   \\/\\/   |  |  \\|  \\   ___/ __ \\|  | |  |/  ___\\   __\\");
        send("&c /        |    < \\___  ||    `   |  |\\___ \\\\  \\__(  <_> |  | \\/ /_/ | \\        /|   Y  |  ||  | \\  ___/|  |_|  |\\___ \\ |  |  ");
        send("&c/_______  |__|_ \\/ ____/_______  |__/____  >\\___  \\____/|__|  \\____ |  \\__/\\  / |___|  |__||__|  \\___  |____|__/____  >|__|  ");
        send("&c        \\/     \\/\\/            \\/        \\/     \\/                 \\/       \\/       \\/              \\/             \\/       ");
        send("&c Copyright SkyPlugins - www.skyplugins.com.br (Todos os direitos reservados)");
        send(" ");
        saveWhitelist();
    }


}
