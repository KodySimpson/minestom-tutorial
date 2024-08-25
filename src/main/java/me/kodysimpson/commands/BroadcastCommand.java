package me.kodysimpson.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

public class BroadcastCommand extends Command {

    public BroadcastCommand() {
        super("broadcast");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You need to specify a message to broadcast!");
        });

        //get all the arguments passed to the command and concatenate them into a single string
        var message = ArgumentType.StringArray("message");

        addSyntax((sender, context) -> {
            String[] messageArray = context.get(message);
            StringBuilder messageBuilder = new StringBuilder();
            for (String s : messageArray) {
                messageBuilder.append(s).append(" ");
            }
            String finalMessage = messageBuilder.toString();

            //Send the message to all players
            Audiences.players().sendMessage(Component.text(finalMessage).color(NamedTextColor.GREEN));

            //All players who meet a condition
            Audiences.players(player -> {
                if (player.getGameMode() == GameMode.SURVIVAL){{
                    return true;
                }}
                return false;
            }).sendMessage(Component.text(finalMessage).color(NamedTextColor.GREEN));

            //The entire server including console
            Audiences.all().sendMessage(Component.text(finalMessage).color(NamedTextColor.GREEN));

            //Everyone in the instance that the sender is in
            if (sender instanceof Player player) {
                player.getInstance().sendMessage(Component.text(finalMessage).color(NamedTextColor.GREEN));
            }

        }, message);
    }
}
