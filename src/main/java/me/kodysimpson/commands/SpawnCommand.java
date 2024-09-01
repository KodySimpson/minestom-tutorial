package me.kodysimpson.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.metadata.monster.zombie.ZombieMeta;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;

import java.util.List;

public class SpawnCommand extends Command {
    public SpawnCommand() {
        super("spawn");

        var entityNameArg = ArgumentType.EntityType("entityType");
        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                EntityType entityType = context.get(entityNameArg);

                if (entityType == EntityType.ZOMBIE) {
                    var zombie = new EntityCreature(EntityType.ZOMBIE);

                    //Setting metadata for the zombie
                    ZombieMeta zombieMeta = (ZombieMeta) zombie.getEntityMeta();
                    zombieMeta.setBaby(true);
                    zombieMeta.setAggressive(true);
                    zombieMeta.setCustomName(Component.text("Zombie Grandpa"));
                    zombieMeta.setCustomNameVisible(true);
                    zombie.setHelmet(ItemStack.of(Material.GOLDEN_HELMET));

                    //make him chase you
                    zombie.addAIGroup(
                            List.of(new MeleeAttackGoal(zombie, 1.2, 20, TimeUnit.SERVER_TICK)),
                            List.of(new ClosestEntityTarget(zombie, 32, entity -> entity instanceof Player))
                    );

                    zombie.setInstance(player.getInstance(), player.getPosition());

                    var entityNode = EventNode.type("entity_node", EventFilter.ENTITY);
                    entityNode.addListener(PlayerEntityInteractEvent.class, event -> {
                        if (event.getHand() != Player.Hand.MAIN || event.getTarget() != zombie) return;
                        var target = event.getTarget();
                        if (target instanceof LivingEntity livingEntity) {
                            event.getPlayer().sendMessage(Component.text("You killed the zombie!"));
                            livingEntity.kill();
                        }
                    });
                    MinecraftServer.getGlobalEventHandler().addChild(entityNode);

                    zombie.eventNode().addListener(EntityDeathEvent.class, event -> {
                        player.sendMessage(Component.text("The zombie has been killed!"));
                    });
                } else {
                    Entity entity = new Entity(entityType);
                    entity.setInstance(player.getInstance(), player.getPosition());
                }
            }
        }, entityNameArg);

    }
}
