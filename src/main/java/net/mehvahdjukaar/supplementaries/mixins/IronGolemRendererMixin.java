package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.common.Textures;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemRenderer.class)
public abstract class IronGolemRendererMixin {

    @Inject(method = "getEntityTexture", at = @At("HEAD"), cancellable = true)
    public void getEntityTexture(IronGolemEntity entity, CallbackInfoReturnable<ResourceLocation> info) {
        if(entity.getUniqueID().getLeastSignificantBits()%420==0)
            info.setReturnValue(Textures.THICK_GOLEM);
    }
}