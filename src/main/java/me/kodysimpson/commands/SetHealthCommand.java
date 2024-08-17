package me.kodysimpson.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class SetHealthCommand extends Command {
    public SetHealthCommand() {
        super("sethealth", "heal");

        //Set a condition for the command
        setCondition((sender, commandString) -> {
            return sender.hasPermission("sethealth");
        });

        //Hit if no arguments are provided or if the argument does not match correctly
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /sethealth <amount>");
        });

        //Command: /sethealth <amount>
        var healthAmount = ArgumentType.Integer("healthAmount");

        addSyntax((sender, context) -> {
            int newHealth = context.get(healthAmount);

            //make sure the health is between 1 and 20
            if (newHealth < 1 || newHealth > 20) {
                sender.sendMessage("Health must be between 1 and 20");
                return;
            }

            //A sender can be a player or the console or command block, so we need to check
            if (sender instanceof Player player) {
                player.setHealth(newHealth);
                sender.sendMessage("Health set to " + newHealth);
            }
        }, healthAmount);
    }
}
