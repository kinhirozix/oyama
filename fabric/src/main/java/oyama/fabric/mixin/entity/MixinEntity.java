package oyama.fabric.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import oyama.event.core.EventManager;
import oyama.event.tick.EntityTickEvent;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(Entity.class)
public abstract class MixinEntity {
    @WrapOperation(
        method = "rideTick",
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
