package github.killarexe.right_click_chest_boat.fabric;

import net.fabricmc.api.ModInitializer;
import org.slf4j.LoggerFactory;

public class RCCBFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LoggerFactory.getLogger(this.getClass()).info("Right Click Chest Boat initialized!");
    }
}
