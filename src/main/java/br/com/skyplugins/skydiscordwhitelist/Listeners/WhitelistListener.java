package br.com.skyplugins.skydiscordwhitelist.Listeners;

import br.com.skyplugins.skydiscordwhitelist.SkyPlugins;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.UUID;

import static br.com.skyplugins.skydiscordwhitelist.SkyPlugins.whitelist;

public class WhitelistListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        User user = event.getAuthor();
        String messageContent = event.getMessage().getContentRaw();

        if (!event.isFromType(ChannelType.PRIVATE)) {
            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage("Por favor, envie seu UUID em uma mensagem privada para mim.").queue();
            });
            return;
        }

        if (isValidUUID(messageContent)) {
            if(!whitelist.contains(messageContent)) {
                whitelist.add(messageContent);

                user.openPrivateChannel().queue(channel -> {
                    channel.sendMessage("Thanks for sending your UUID! You have been whitelisted on the server.").queue();
                });
            } else if (whitelist.contains(messageContent)) {
                user.openPrivateChannel().queue(channel -> {
                    channel.sendMessage("You are already whitelisted.").queue();
                });
            }
        } else {
            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage("Invalid UUID format. Please try again.").queue();
            });
        }
    }

    private boolean isValidUUID(String uuidString) {
        try {
            UUID uuid = UUID.fromString(uuidString);
            return (uuid != null);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
