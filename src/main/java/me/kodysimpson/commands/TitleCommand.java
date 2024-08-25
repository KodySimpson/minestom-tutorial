package me.kodysimpson.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.time.Duration;

public class TitleCommand extends Command {

    public TitleCommand() {
        super("title");

        var titleTimes = Title.Times.times(Duration.ofSeconds(2), Duration.ofSeconds(5), Duration.ofMillis(700));
        Title title = Title.title(Component.text("Hello, World!", NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD), Component.text("Subtitle goes here", NamedTextColor.GRAY), titleTimes);

        setDefaultExecutor((sender, context) -> {
            sender.showTitle(title);
        });

        var titleText = ArgumentType.String("titleText");
        var subtitleText = ArgumentType.String("subtitleText");

        addSyntax((sender, context) -> {
            String titleString = context.get(titleText);
            String subtitleString = context.get(subtitleText);
            Title newTitle = Title.title(Component.text(titleString, NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD), Component.text(subtitleString, NamedTextColor.GRAY), titleTimes);
            sender.showTitle(newTitle);
        }, titleText, subtitleText);
    }

}
