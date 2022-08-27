package com.hamusuke.flycommand.network;

import com.hamusuke.flycommand.FlyCommandMod;
import com.hamusuke.flycommand.command.FlyCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkManager {
    private static final String VERSION = "1";
    private static final SimpleChannel MAIN = NetworkRegistry.newSimpleChannel(new ResourceLocation(FlyCommandMod.MOD_ID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);

    static {
        MAIN.registerMessage(0, SyncS2CPacket.class, SyncS2CPacket::write, SyncS2CPacket::new, SyncS2CPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void load() {
    }

    public static void sendToClient(Object msg, ServerPlayer serverPlayer) {
        MAIN.sendTo(msg, serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
