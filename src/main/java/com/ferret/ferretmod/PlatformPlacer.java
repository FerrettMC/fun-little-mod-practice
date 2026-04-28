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
        String[] words = message.split("\\s+");

        if (message.equalsIgnoreCase("platform")) {
            player.sendSystemMessage(Component.literal("Enter an integer to specify platform side length (e.g. platform 12)."));
            event.setCanceled(true);
            return;
        }

        if (words[0].equalsIgnoreCase("platform") && words.length == 2 && player.level() instanceof ServerLevel serverLevel) {
            int length;
            if (words[1].matches("\\d+")) {
                length = Integer.parseInt(words[1]);
            } else {
                player.sendSystemMessage(Component.literal("Not a valid length. (Invalid format)"));
                event.setCanceled(true);
                return;
            }
            if (length < 3 || length > 45) {
                player.sendSystemMessage(Component.literal("Not a valid length."));
                event.setCanceled(true);
                return;
            }
            int firstNum = (length / 2) * -1;
            int secondNum = ((length + 1) / 2);



            BlockPos pos = player.blockPosition();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            for (int i = firstNum; i < secondNum; i++) {
                for (int q = firstNum; q < secondNum; q++) {
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
