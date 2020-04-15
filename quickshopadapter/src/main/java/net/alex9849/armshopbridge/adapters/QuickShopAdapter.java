package net.alex9849.armshopbridge.adapters;

import net.alex9849.arm.regions.Region;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.Shop.Shop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class QuickShopAdapter implements IShopPluginAdapter {

    @Override
    public void deleteShops(Region region) {
        Vector minPoint = region.getRegion().getMinPoint();
        Vector maxPoint = region.getRegion().getMinPoint();
        World world = region.getRegionworld();
        Set<Chunk> chuckLocations = new HashSet<>();

        for(int x = minPoint.getBlockX(); x <= maxPoint.getBlockX() + 16; x += 16) {
            for(int z = minPoint.getBlockZ(); z <= maxPoint.getBlockZ() + 16; z += 16) {
                chuckLocations.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }

        HashMap<Location, Shop> shopMap = new HashMap<>();

        for(Chunk chunk : chuckLocations) {
            HashMap<Location, Shop> shopsInChunk = QuickShop.instance.getShopManager().getShops(chunk);
            if(shopsInChunk != null) {
                shopMap.putAll(shopsInChunk);
            }
        }
        for(Location shopLocation : shopMap.keySet()) {
            if(region.getRegion().contains(shopLocation.getBlockX(), shopLocation.getBlockY(), shopLocation.getBlockZ())) {
                Shop shop = shopMap.get(shopLocation);
                if(shop != null) {
                    shop.onUnload();
                    shop.delete(false);
                }
            }
        }
    }
}
