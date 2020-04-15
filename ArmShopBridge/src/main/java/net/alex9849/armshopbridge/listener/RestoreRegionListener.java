package net.alex9849.armshopbridge.listener;

import net.alex9849.arm.events.ResetBlocksEvent;
import net.alex9849.armshopbridge.ArmShopBridge;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RestoreRegionListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleRegionReset(ResetBlocksEvent event) {
        for(IShopPluginAdapter adapter : ArmShopBridge.getInstance().getShopPluginAdapters()) {
            adapter.deleteShops(event.getRegion());
        }
    }

}
