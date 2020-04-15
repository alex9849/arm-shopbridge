package net.alex9849.armshopbridge.adapters;

import net.alex9849.arm.regions.Region;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.Location;
import thirtyvirus.ultimateshops.UltimateShops;
import thirtyvirus.ultimateshops.shops.UShop;

import java.util.HashSet;
import java.util.Set;

public class UltimateShopsAdapter implements IShopPluginAdapter {
    @Override
    public void deleteShops(Region region) {
        Set<UShop> allShops = new HashSet<>(UltimateShops.getPlugin(UltimateShops.class).getShops().values());

        for(UShop uShop : allShops) {
            Location shopLoc = uShop.getLocation();
            if(region.getRegion().contains(shopLoc.getBlockX(), shopLoc.getBlockY(), shopLoc.getBlockZ())) {
                uShop.destroy(false);
            }
        }
    }
}
