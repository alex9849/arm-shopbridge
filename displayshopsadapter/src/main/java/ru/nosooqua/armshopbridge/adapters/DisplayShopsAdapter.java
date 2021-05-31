package ru.nosooqua.armshopbridge.adapters;

import net.alex9849.arm.regions.Region;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.Location;
import xzot1k.plugins.ds.DisplayShops;
import xzot1k.plugins.ds.api.objects.Shop;

public class DisplayShopsAdapter implements IShopPluginAdapter {
    @Override
    public void deleteShops(Region region) {
        DisplayShops ds = DisplayShops.getPluginInstance();

        for(Shop shop : ds.getManager().getShopMap().values()) {
            Location loc = shop.getBaseLocation().asBukkitLocation();
            if(region.getRegion().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                shop.purge(null, true);
            }
        }
    }
}
