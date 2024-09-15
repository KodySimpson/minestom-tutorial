package me.kodysimpson.listeners;

import me.kodysimpson.managers.DisguiseManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;

public class PlayerEventListener {

    public static EventNode<?> getEventNode(Instance spawnInstance, DisguiseManager disguiseManager) {
        var eventNode = EventNode.type("players-node", EventFilter.PLAYER);

        // Set the player's skin during PlayerSkinInitEvent
        eventNode.addListener(PlayerSkinInitEvent.class, event -> {
            Player player = event.getPlayer();
            if (disguiseManager.isDisguised(player)) {
                var disguiseUsername = disguiseManager.getDisguiseUsername(player);
                event.setSkin(PlayerSkin.fromUsername(disguiseUsername));
            }
        });

        // Set the player's display name during PlayerSpawnEvent
        //since if we do it in the PlayerSkinInitEvent, it will be overwritten by the player's actual username
        eventNode.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            if (disguiseManager.isDisguised(player)) {
                String disguiseUsername = disguiseManager.getDisguiseUsername(player);
                player.setDisplayName(Component.text(disguiseUsername));
            }
        });

        // Set the player's spawning instance and respawn point
        eventNode.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(spawnInstance);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        return eventNode;
    }
}
