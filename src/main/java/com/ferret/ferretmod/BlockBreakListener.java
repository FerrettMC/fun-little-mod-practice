package com.ferret.ferretmod;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockBreakListener {

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
