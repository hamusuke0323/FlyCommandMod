package com.hamusuke.flycommand.network;

import com.hamusuke.flycommand.invoker.LivingEntityInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncS2CPacket {
    private final int id;
    private final boolean marked;

    public SyncS2CPacket(int id, boolean marked) {
        this.id = id;
        this.marked = marked;
    }

    public SyncS2CPacket(FriendlyByteBuf byteBuf) {
        this.id = byteBuf.readVarInt();
        this.marked = byteBuf.readBoolean();
    }

    public void write(FriendlyByteBuf byteBuf) {
        byteBuf.writeVarInt(this.id);
        byteBuf.writeBoolean(this.marked);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getEntity(this.id) instanceof LivingEntityInvoker invoker) {
                invoker.mark(this.marked);
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
