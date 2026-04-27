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
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber
public class snake {

    private static String direction = "north";
    private static int tickCounter = 0;
    private static ServerPlayer loopPlayer = null;

    // This should probably be a class instead of a record, when I remake this I should refactor that. 
    record Point(int x, int y, int z, String type) {}

    private static LinkedList<Point> snake = new LinkedList<>();



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
                snake.clear();

                serverPlayer.sendSystemMessage(Component.literal("Placed snake head"));
                snake.add(new Point(pos.getX(), pos.getY(), pos.getZ(), "head"));
                snake.add(new Point(pos.getX() - 1, pos.getY(), pos.getZ(), "body"));
                if (loopPlayer.level() instanceof ServerLevel serverLevel) {
                    BlockPos onePos = new BlockPos(snake.get(1).x, snake.get(1).y, snake.get(1).z);
                    serverLevel.setBlock(onePos, Blocks.BLACK_WOOL.defaultBlockState(), 3);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (loopPlayer == null || snake.isEmpty()) return;

        tickCounter++;
        if (tickCounter % 10 == 0) {
            if (loopPlayer.level() instanceof ServerLevel serverLevel) {

                // 1. Compute new head position
                Point head = snake.getFirst();
                Point newHead = new Point(head.x + 1, head.y, head.z, "head");

                // 2. Place new head block
                BlockPos newHeadPos = new BlockPos(newHead.x, newHead.y, newHead.z);
                serverLevel.setBlock(newHeadPos, Blocks.WHITE_WOOL.defaultBlockState(), 3);

                // 3. Add new head to front
                snake.addFirst(newHead);

                Point oldHead = snake.get(1);
                snake.set(1, new Point(oldHead.x, oldHead.y, oldHead.z, "body"));

                // 4. Remove tail block
                Point tail = snake.removeLast();
                BlockPos tailPos = new BlockPos(tail.x, tail.y, tail.z);
                serverLevel.setBlock(tailPos, Blocks.AIR.defaultBlockState(), 3);
                for (Point snake_block : snake) {
                    if (snake_block.type.equals("head"))
                        continue;
                    BlockPos blockPos = new BlockPos(snake_block.x, snake_block.y, snake_block.z);
                    serverLevel.setBlock(blockPos, Blocks.BLACK_WOOL.defaultBlockState(), 3);
                }
            }
        }

    }

}

