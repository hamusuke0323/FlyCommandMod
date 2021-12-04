package com.hamusuke.flycommand.mixin;

import com.hamusuke.flycommand.FlyCommandMod;
import com.hamusuke.flycommand.invoker.LivingEntityInvoker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInvoker {
    private static final EntityDataAccessor<Boolean> NO_FALL_DAMAGE_MARKED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(NO_FALL_DAMAGE_MARKED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag p_21145_, CallbackInfo ci) {
        p_21145_.putBoolean("noFallDamageMarked", FlyCommandMod.isMarked((LivingEntity) (Object) this));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag p_21096_, CallbackInfo ci) {
        FlyCommandMod.mark((LivingEntity) (Object) this, p_21096_.getBoolean("noFallDamageMarked"));
    }

    @Override
    public void mark(boolean flag) {
        this.entityData.set(NO_FALL_DAMAGE_MARKED, flag);
    }

    @Override
    public boolean isMarked() {
        return this.entityData.get(NO_FALL_DAMAGE_MARKED);
    }
}
