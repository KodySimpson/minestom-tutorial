package me.kodysimpson.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class FartCommand extends Command {

    public FartCommand() {
        super("fart", "poop");

        setCondition((sender, commandString) -> {
            if(!sender.hasPermission("fart.permission")){
                sender.sendMessage("You do not have permission to fart!");
                return false;
            }
            return true;
        });

        var fartAmountArg = ArgumentType.Integer("fart-amount");
        //Validate the input
        fartAmountArg.setCallback((sender, exception) -> {
            final String input = exception.getInput();
            sender.sendMessage("The number " + input + " is invalid! You cannot fart that. :/");
        });

        //If none of the syntaxes are correct, this will be executed
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You need to specify how many times you want to fart!");
            sender.sendMessage("Correct usage: /fart <fart-amount>");
        });

        //A specific execution for a particular syntax with the arguments we defined
        addSyntax((sender, context) -> {
            final int fartAmount = context.get(fartAmountArg);

            for (int i = 0; i < fartAmount; i++) {
                sender.sendMessage("Farted! Ewwww stinky");
            }
        }, fartAmountArg);
    }



}
