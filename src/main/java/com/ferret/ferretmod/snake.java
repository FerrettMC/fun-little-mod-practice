package com.ferret.ferretmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class snake {

    private static String direction = "north";
    private static int tickCounter = 0;
    private static ServerPlayer loopPlayer = null;
    private static BlockPos snakePos = null;
    private static boolean snakeActive = false;
    record Point(int x, int z, String type) {}

    private static List<Point> snake = new ArrayList<>();


    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();

    }
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(0.42);
        }
    }


    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            String blockName = event.getPlacedBlock().getBlock().getDescriptionId();
            ItemStack stack = serverPlayer.getMainHandItem();
            String name = stack.getComponents().has(DataComponents.CUSTOM_NAME)
                    ? stack.getHoverName().getString()
                    : event.getPlacedBlock().getBlock().getName().getString();

            if (blockName.equals("block.minecraft.white_wool") && name.equalsIgnoreCase("snake head")) {
                // just set state and return, let the tick event do the work
                BlockPos pos = event.getPos();
                loopPlayer = serverPlayer;
                snakePos = event.getPos();
                snakeActive = true;
                serverPlayer.sendSystemMessage(Component.literal("Placed snake head"));
                snake.add(new Point(pos.getX(), pos.getZ(), "head"));
                snake.add(new Point(pos.getX() - 1, pos.getZ(), "body"));
            }
        }
    }


    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!snakeActive || loopPlayer == null || snakePos == null) return;

        tickCounter++;
        if (tickCounter % 10 == 0) {
            for (Point snake_block : snake) {

            }


//            if (loopPlayer.level() instanceof ServerLevel serverLevel) {
//                serverLevel.setBlock(snakePos.offset(snakeStep, 0, 0), Blocks.WHITE_WOOL.defaultBlockState(), 3);
//                if (snakeStep > 1)
//                    serverLevel.setBlock(snakePos.offset(snakeStep - 2, 0, 0), Blocks.AIR.defaultBlockState(), 3);
//
//            }
//
//            snakeStep++;
        }
    }

}

