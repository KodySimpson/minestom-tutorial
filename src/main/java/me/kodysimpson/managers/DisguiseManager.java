package me.kodysimpson.managers;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DisguiseManager {

    private final Map<UUID, String> disguisedPlayers = new ConcurrentHashMap<>();

    // Check if a player is disguised
    public boolean isDisguised(Player player) {
        return disguisedPlayers.containsKey(player.getUuid());
    }

    // Method to disguise a player
    public void disguisePlayer(Player player, String username) {
        disguisedPlayers.put(player.getUuid(), username);
        applyDisguise(player, username);
    }

    // Method to remove a player's disguise
    public void removeDisguise(Player player) {
        disguisedPlayers.remove(player.getUuid());
        // Reset to the player's original skin and display name
        player.setSkin(PlayerSkin.fromUuid(player.getUuid().toString()));
        player.setDisplayName(Component.text(player.getUsername()));
    }

    // Apply disguise to a player
    private void applyDisguise(Player player, String username) {
        player.setSkin(PlayerSkin.fromUsername(username));
        player.setDisplayName(Component.text(username));
    }

    public String getDisguiseUsername(Player player) {
        return disguisedPlayers.get(player.getUuid());
    }

}
