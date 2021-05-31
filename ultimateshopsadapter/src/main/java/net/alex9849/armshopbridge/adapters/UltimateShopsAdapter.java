package net.alex9849.armshopbridge.adapters;

import net.alex9849.arm.regions.Region;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.Location;
import thirtyvirus.ultimateshops.UltimateShops;
import thirtyvirus.ultimateshops.UShop;

public class UltimateShopsAdapter implements IShopPluginAdapter {
    @Override
    public void deleteShops(Region region) {
        for(UShop uShop : UltimateShops.shopsList) {
            Location shopLoc = uShop.getLocation();
            if(region.getRegion().contains(shopLoc.getBlockX(), shopLoc.getBlockY(), shopLoc.getBlockZ())) {
                uShop.destroy(false);
            }
        }
    }
}
