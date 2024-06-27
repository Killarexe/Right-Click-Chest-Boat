package github.killarexe.right_click_chest_boat.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Collectors;

@Mixin(Boat.class)
public abstract class BoatInteractMixin extends VehicleEntity implements Leashable, VariantHolder<Boat.Type> {

    @Shadow public abstract Boat.Type getVariant();

    public BoatInteractMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    public void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        InteractionResult result = super.interact(player, hand);
        if (result != InteractionResult.PASS) {
            callbackInfo.setReturnValue(result);
        }
        ItemStack stack = player.getItemInHand(hand);
        if((!stack.getTags().map(itemTagKey -> itemTagKey.location().getPath().contains("chest")).collect(Collectors.toSet()).isEmpty() || stack.is(Items.CHEST)) && player.isShiftKeyDown()) {
            stack.shrink(1);
            Level level = player.level();
            ChestBoat newBoat = new ChestBoat(level, xo, yo, zo);
            newBoat.setXRot(getXRot());
            newBoat.setYRot(getYRot());
            newBoat.setVariant(getVariant());
            level.addFreshEntity(newBoat);
            kill();
            callbackInfo.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
