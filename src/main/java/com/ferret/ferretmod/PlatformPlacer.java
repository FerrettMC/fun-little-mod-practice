package com.ferret.ferretmod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber

public class PlatformPlacer {
    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        String message = event.getMessage().getString();
        ServerPlayer player = event.getPlayer();

        if (message.equalsIgnoreCase("platform") && player.level() instanceof ServerLevel serverLevel) {
            BlockPos pos = player.blockPosition();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            for (int i = -5; i < 6; i++) {
                for (int q = -5; q < 6; q++) {
                    if (Math.abs(i % 2) == 1) {
                        if (Math.abs(q % 2) == 1) {
                            serverLevel.setBlock(new BlockPos(x + i, y-1, z + q), Blocks.LIME_CONCRETE.defaultBlockState(), 3);
                        } else {
                            serverLevel.setBlock(new BlockPos(x + i, y-1, z + q), Blocks.GREEN_CONCRETE.defaultBlockState(), 3);
                        }
                    } else {
                        if (Math.abs(q % 2) == 1) {
                            serverLevel.setBlock(new BlockPos(x + i, y-1, z + q), Blocks.GREEN_CONCRETE.defaultBlockState(), 3);
                        } else {
                            serverLevel.setBlock(new BlockPos(x + i, y-1, z + q), Blocks.LIME_CONCRETE.defaultBlockState(), 3);
                        }
                    }

                }
            }
            player.sendSystemMessage(Component.literal("Platform set."));

            // Cancel the message (stops it from being sent to chat)
            event.setCanceled(true);

        }


    }

}
