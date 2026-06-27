package com.nosneakanimation.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique
    private static final double MIN_CROUCHING_EYE_HEIGHT = 1.27;

    @Inject(method = "getEyeHeight()F", at = @At("RETURN"), cancellable = true)
    private void getStandingEyeHeight(CallbackInfoReturnable<Float> cir) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof AbstractClientPlayer
                && entity.getPose() == Pose.CROUCHING
                && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            cir.setReturnValue((float) MIN_CROUCHING_EYE_HEIGHT);
        }
    }
}
