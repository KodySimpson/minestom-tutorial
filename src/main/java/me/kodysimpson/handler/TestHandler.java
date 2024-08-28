package me.kodysimpson.handler;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class TestHandler implements BlockHandler {
    @Override
    public void onPlace(@NotNull Placement placement) {
        System.out.println("Test block placed at " + placement.getBlockPosition());
    }

    @Override
    public void onDestroy(@NotNull Destroy destroy) {
        if (destroy instanceof PlayerDestroy playerDestroy) {
            playerDestroy.getPlayer().sendMessage("You broke the test block!");
        }
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("minestom_tutorial:test_handler");
    }
}
