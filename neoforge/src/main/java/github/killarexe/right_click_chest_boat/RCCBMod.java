package github.killarexe.right_click_chest_boat;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(RCCB.MOD_ID)
public class RCCBMod {
    public RCCBMod(IEventBus _bus) {
        RCCB.init();
    }
}