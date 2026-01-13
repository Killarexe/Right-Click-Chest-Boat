package github.killarexe.right_click_chest_boat.mixin;

import github.killarexe.right_click_chest_boat.RCCBMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.boat.AbstractChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(AbstractBoat.class)
public abstract class BoatInteractMixin extends VehicleEntity implements Leashable {
  public BoatInteractMixin(EntityType<?> entityType, Level level) {
      super(entityType, level);
  }

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
    Optional<EntityType<? extends @NotNull AbstractChestBoat>> type = Optional.ofNullable(RCCBMap.ITEM_ENTITY_MAP.get(getType()));
    if (type.isEmpty()) {
      callbackInfo.setReturnValue(InteractionResult.PASS);
      return;
    }

    InteractionResult result = super.interact(player, hand);
    if (!getPassengers().isEmpty()) {
      player.displayClientMessage(Component.translatable("message.right_click_chest_boat.passengers"), true);
      callbackInfo.setReturnValue(result);
      return;
    }
    if (result != InteractionResult.PASS) {
      callbackInfo.setReturnValue(result);
      return;
    }

    Level level = player.level();
    if (level instanceof ServerLevel serverLevel) {
      ItemStack stack = player.getItemInHand(hand);
      if ((!stack.getTags().map(itemTagKey -> itemTagKey.location().getPath().contains("chest")).collect(Collectors.toSet()).isEmpty() || stack.is(Items.CHEST)) && player.isShiftKeyDown()) {
        stack.shrink(1);
        AbstractChestBoat newBoat = type.get().create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);
        EntityType.createDefaultStackConfig(serverLevel, getDropItem().getDefaultInstance(), player).accept(newBoat);
        newBoat.setXRot(getXRot());
        newBoat.setYRot(getYRot());
        newBoat.setPos(position());
        level.addFreshEntity(newBoat);
        kill(serverLevel);
        callbackInfo.setReturnValue(InteractionResult.SUCCESS);
      }
    }
  }
}
