package net.alex9849.armshopbridge.adapters;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import net.alex9849.arm.regions.Region;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.Location;

import java.util.*;

public class ShopkeepersAdapter implements IShopPluginAdapter {
    @Override
    public void deleteShops(Region region) {

        Collection<? extends Shopkeeper> shopkeepers = ShopkeepersAPI.getPlugin().getShopkeeperRegistry().getAllShopkeepers();

        for(Shopkeeper shopkeeper : shopkeepers) {
            Location loc = shopkeeper.getLocation();
            if(region.getRegion().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                shopkeeper.delete();
            }
        }

    }
}
