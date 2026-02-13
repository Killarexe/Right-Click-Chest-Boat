package github.killarexe.right_click_chest_boat.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(AbstractBoat.class)
public abstract class BoatInteractMixin extends VehicleEntity implements Leashable {

    @Unique
    private static final Map<Item, EntityType<? extends AbstractChestBoat>> ITEM_ENTITY_MAP = Map.of(
            Items.ACACIA_BOAT, EntityType.ACACIA_CHEST_BOAT,
            Items.BIRCH_BOAT, EntityType.BIRCH_CHEST_BOAT,
            Items.DARK_OAK_BOAT, EntityType.DARK_OAK_CHEST_BOAT,
            Items.CHERRY_BOAT, EntityType.CHERRY_CHEST_BOAT,
            Items.OAK_BOAT, EntityType.OAK_CHEST_BOAT,
            Items.JUNGLE_BOAT, EntityType.JUNGLE_CHEST_BOAT,
            Items.SPRUCE_BOAT, EntityType.SPRUCE_CHEST_BOAT,
            Items.MANGROVE_BOAT, EntityType.MANGROVE_CHEST_BOAT,
            Items.PALE_OAK_BOAT, EntityType.PALE_OAK_CHEST_BOAT,
            Items.BAMBOO_RAFT, EntityType.BAMBOO_CHEST_RAFT
    );

    @Shadow @Final private Supplier<Item> dropItem;

    public BoatInteractMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    public void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
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
            if ((stack.getTags().anyMatch(tag -> tag.location().getPath().toLowerCase().contains("chest"))|| stack.is(Items.CHEST)) && player.isShiftKeyDown()) {
                stack.shrink(1);
                Optional<EntityType<? extends AbstractChestBoat>> type = Optional.ofNullable(ITEM_ENTITY_MAP.get(dropItem.get()));
                if (type.isEmpty()) {
                    callbackInfo.setReturnValue(InteractionResult.PASS);
                    return;
                }
                AbstractChestBoat newBoat = type.get().create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);
                EntityType.createDefaultStackConfig(serverLevel, dropItem.get().getDefaultInstance(), player).accept(newBoat);
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
