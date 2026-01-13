package github.killarexe.right_click_chest_boat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.boat.AbstractChestBoat;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RCCBMap {
  public static final Map<EntityType<? extends @NotNull AbstractBoat>, EntityType<? extends @NotNull AbstractChestBoat>> ITEM_ENTITY_MAP = Map.of(
          EntityType.ACACIA_BOAT, EntityType.ACACIA_CHEST_BOAT,
          EntityType.BIRCH_BOAT, EntityType.BIRCH_CHEST_BOAT,
          EntityType.DARK_OAK_BOAT, EntityType.DARK_OAK_CHEST_BOAT,
          EntityType.CHERRY_BOAT, EntityType.CHERRY_CHEST_BOAT,
          EntityType.OAK_BOAT, EntityType.OAK_CHEST_BOAT,
          EntityType.JUNGLE_BOAT, EntityType.JUNGLE_CHEST_BOAT,
          EntityType.SPRUCE_BOAT, EntityType.SPRUCE_CHEST_BOAT,
          EntityType.MANGROVE_BOAT, EntityType.MANGROVE_CHEST_BOAT,
          EntityType.PALE_OAK_BOAT, EntityType.PALE_OAK_CHEST_BOAT,
          EntityType.BAMBOO_RAFT, EntityType.BAMBOO_CHEST_RAFT
  );
}
