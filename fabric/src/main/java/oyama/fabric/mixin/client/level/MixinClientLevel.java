package oyama.fabric.mixin.client.level;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import oyama.event.core.EventManager;
import oyama.event.tick.EntityTickEvent;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(ClientLevel.class)
public abstract class MixinClientLevel {
    @WrapOperation(
        method = "tickNonPassenger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;tick()V"
        )
    )
    private void fireEntityTickEvent(Entity entity, Operation<Void> original) {
        if (!EventManager.fire(new EntityTickEvent.Start(entity))) {
            original.call(entity);
            EventManager.fire(new EntityTickEvent.End(entity));
        }
    }
}
