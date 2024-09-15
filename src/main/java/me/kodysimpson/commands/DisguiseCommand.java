package me.kodysimpson.commands;

import me.kodysimpson.managers.DisguiseManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class DisguiseCommand extends Command {

    public DisguiseCommand(DisguiseManager disguiseManager) {
        super("disguise");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /disguise <username>");
        });

        var usernameArg = ArgumentType.String("username");

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                String username = context.get(usernameArg);
                disguiseManager.disguisePlayer(player, username);
                sender.sendMessage(Component.text("You are now disguised as " + username));
            } else {
                sender.sendMessage("Only players can use this command!");
            }
        }, usernameArg);
    }
}
