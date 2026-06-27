package com.nosneakanimation.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.objectweb.asm.Opcodes;

@Mixin(Camera.class)
public abstract class CameraClientMixin {

    @Unique
    private boolean nosneakanimation$wasSneaking = false;

    @Shadow
    private float eyeHeight;
    @Shadow private Entity entity;

    @Shadow private float eyeHeightOld;

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Camera;eyeHeight:F", opcode = Opcodes.PUTFIELD))
    public void nosneakanimation(Camera obj, float value) {
        if (entity instanceof Player) {
            if (entity.getPose() == Pose.CROUCHING) {
                nosneakanimation$wasSneaking = true;
                eyeHeightOld = eyeHeight = entity.getEyeHeight();
                return;
            } else if (nosneakanimation$wasSneaking) {
                nosneakanimation$wasSneaking = false;
                eyeHeightOld = eyeHeight = entity.getEyeHeight();
                return;
            }
        }
        // Since we want instant sneaking, we don't apply smoothing when crouching/uncrouching.
        // We still apply smoothing for other eye height changes (like swimming maybe).
        eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * 0.5f;
    }
}
