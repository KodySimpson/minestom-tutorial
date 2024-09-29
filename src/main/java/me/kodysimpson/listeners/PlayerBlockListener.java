package me.kodysimpson.listeners;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.permission.Permission;

public class PlayerBlockListener {

    public static EventNode<?> getEventNode() {
        var eventNode = EventNode.type("players-blocks-node", EventFilter.PLAYER,
                (playerEvent, player) -> {
                    return player.hasPermission(new Permission("block.*"));
                });

        eventNode.addListener(EventListener.builder(PlayerBlockBreakEvent.class)
                .handler(playerBlockBreakEvent -> {
                    playerBlockBreakEvent.getInstance().sendMessage(Component.text("Player " + playerBlockBreakEvent.getPlayer().getUsername() + " broke a block!"));
                })
                .build());

        return eventNode;
    }

}
