package me.kodysimpson.commands;

import me.kodysimpson.managers.DisguiseManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class UndisguiseCommand extends Command {

    public UndisguiseCommand(DisguiseManager disguiseManager) {
        super("undisguise");

        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                if (disguiseManager.isDisguised(player)) {
                    disguiseManager.removeDisguise(player);
                    sender.sendMessage("You are no longer disguised!");
                } else {
                    sender.sendMessage("You are not disguised!");
                }
            } else {
                sender.sendMessage("Only players can use this command!");
            }
        });
    }
}
