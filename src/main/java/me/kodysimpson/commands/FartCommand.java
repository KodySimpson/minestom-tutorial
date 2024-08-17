package me.kodysimpson.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

//extend Command
public class FartCommand extends Command {

    public FartCommand() {
        //The command name followed by an optional list of aliases
        super("fart", "toot", "oof");

        //Code that will be executed when either no args are provided or nothing matches
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You farted! Stinky!");
        });

        //Define the arguments that can be extracted from the command
        var fartAmountArg = ArgumentType.Integer("fartAmount");
        var fartTarget = ArgumentType.String("fartTarget");

        //The code that will be run when the fart amount is provided
        //Only matches to: /fart <fartAmount> (not /fart or /fart <fartAmount> <something>)
        addSyntax((sender, context) -> {
            //extract the fart amount from the context
            int fartAmount = context.get(fartAmountArg);
            for (int i = 0; i < fartAmount; i++) {
                sender.sendMessage("You farted! Stinky! " + i);
            }
        }, fartAmountArg);

        //Fart on someone else: /fart <target name>
        addSyntax((sender, context) -> {
            //extract the target name from the context
            String targetName = context.get(fartTarget);
            sender.sendMessage("You farted on " + targetName + "! Very rude!");
        }, fartTarget);
    }

}
