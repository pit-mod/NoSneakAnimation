package com.nosneakanimation.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {

    @Shadow
    private ClientLevel level;

    protected ClientPacketListenerMixin(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Redirect(method = "handleSetEntityData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;packedItems()Ljava/util/List;"))
    public List<SynchedEntityData.DataValue<?>> nosneakanimation(ClientboundSetEntityDataPacket packet) {
        if (level.getEntity(packet.id()) == minecraft.player) {
            packet.packedItems().removeIf(entry -> {
                return entry.serializer().equals(EntityDataSerializers.POSE) && entry.value() == Pose.CROUCHING;
            });
        }
        return packet.packedItems();
    }
}
