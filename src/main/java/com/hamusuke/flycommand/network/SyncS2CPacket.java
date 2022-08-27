package com.hamusuke.flycommand.network;

import com.hamusuke.flycommand.invoker.LivingEntityInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncS2CPacket {
    private final int entityId;
    private final boolean flag;

    public SyncS2CPacket(int entityId, boolean flag) {
        this.entityId = entityId;
        this.flag = flag;
    }

    public SyncS2CPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.flag = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeBoolean(this.flag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.getEntity(this.entityId) instanceof LivingEntityInvoker invoker) {
                invoker.mark(this.flag);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
