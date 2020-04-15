package net.alex9849.armshopbridge.adapters;

import de.epiceric.shopchest.ShopChest;
import de.epiceric.shopchest.shop.Shop;
import de.epiceric.shopchest.utils.ShopUtils;
import net.alex9849.arm.regions.Region;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import org.bukkit.Location;

public class ShopChestAdapter implements IShopPluginAdapter {
    @Override
    public void deleteShops(Region region) {
        ShopUtils shopUtils = ShopChest.getInstance().getShopUtils();
        Shop[] shops = shopUtils.getShops().clone();

        for(Shop shop : shops) {
            Location loc = shop.getLocation();
            if(region.getRegion().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                shopUtils.removeShop(shop, true);
            }
        }
    }
}
