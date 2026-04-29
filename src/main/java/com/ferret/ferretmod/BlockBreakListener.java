package com.ferret.ferretmod;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockBreakListener {

    // Could have a public list like "ControllerPos" and check if the pos of a block broken matches that of one of those blocks
    // and if it does, change a public str direction which obviously controls the snake direction. Also would have to immediately replace the block
    // and stuff. Also gonna have to do something when if you make a new game, it replaces the old controller pos list with a new one.

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState block = event.getState();

        if (player != null && !player.level().isClientSide()) {
            String blockName = block.getBlock().getName().getString();
            if (blockName.equals("Bedrock")) {
                ((ServerPlayer) player).connection.send(new ClientboundSetTitleTextPacket(
                        Component.literal("§eThat's §6 illegal")
                ));
            }

        }
    }
}
