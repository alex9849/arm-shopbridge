package net.alex9849.armshopbridge.adapters;

import java.util.concurrent.Semaphore;

import net.alex9849.arm.regions.Region;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.Location;
import thirtyvirus.ultimateshops.UltimateShops;
import thirtyvirus.ultimateshops.UShop;

public class UltimateShopsAdapter implements IShopPluginAdapter {
    private final Semaphore mutex = new Semaphore(1, true);

    @Override
    public void deleteShops(Region region) {
        try{
            for(UShop uShop : UltimateShops.shopsList) {
                mutex.acquire();
                Location shopLoc = uShop.getLocation();
                if(region.getRegion().contains(shopLoc.getBlockX(), shopLoc.getBlockY(), shopLoc.getBlockZ())) {
                    uShop.destroy(false);
                }
                mutex.release();
            }
        } catch(InterruptedException ie) {
            mutex.release();
        }
    }
}

