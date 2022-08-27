package com.hamusuke.flycommand.network;

import com.hamusuke.flycommand.FlyCommandMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class Network {
    private static final String PROTOCOL = "1";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(FlyCommandMod.MOD_ID, "main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    static {
        INSTANCE.registerMessage(0, SyncS2CPacket.class, SyncS2CPacket::write, SyncS2CPacket::new, SyncS2CPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void load() {
    }

    public static void send2C(Object msg, ServerPlayer serverPlayer) {
        INSTANCE.sendTo(msg, serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
