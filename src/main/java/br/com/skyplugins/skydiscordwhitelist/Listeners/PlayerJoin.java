package br.com.skyplugins.skydiscordwhitelist.Listeners;

import br.com.skyplugins.skydiscordwhitelist.SkyPlugins;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) throws IOException, ParseException {
        Player player = e.getPlayer();
        UUID uuid = getRealUUID(e.getPlayer().getName());

        if (SkyPlugins.whitelist.size() > 0 && !SkyPlugins.whitelist.contains(player.getUniqueId().toString())) {
            player.kickPlayer(ChatColor.RED + "Este servidor possui uma whitelist via Discord." +
                    "\n\nEntre no Discord abaixo e envie o seu UUID: " + uuid + ".\n\nDiscord: ");
        }
    }

    public UUID getRealUUID(String playerName) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
        Scanner scanner = new Scanner(url.openStream());
        String json = scanner.useDelimiter("\\Z").next();
        scanner.close();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        if (obj instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) obj;
            String uuidStr = (String) jsonObj.get("id");
            uuidStr = uuidStr.substring(0, 8) + "-" +
                    uuidStr.substring(8, 12) + "-" +
                    uuidStr.substring(12, 16) + "-" +
                    uuidStr.substring(16, 20) + "-" +
                    uuidStr.substring(20);
            return UUID.fromString(uuidStr);
        }

        return null;
    }

}
