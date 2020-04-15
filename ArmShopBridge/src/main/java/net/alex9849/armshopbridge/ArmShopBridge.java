package net.alex9849.armshopbridge;

import net.alex9849.arm.events.ResetBlocksEvent;
import net.alex9849.arm.events.UnsellRegionEvent;
import net.alex9849.armshopbridge.adapters.QuickShopAdapter;
import net.alex9849.armshopbridge.adapters.ShopChestAdapter;
import net.alex9849.armshopbridge.adapters.UltimateShopsAdapter;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import net.alex9849.armshopbridge.listener.RestoreRegionListener;
import net.alex9849.armshopbridge.listener.UnsellRegionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class ArmShopBridge extends JavaPlugin {
    private static ArmShopBridge instance;
    private Set<IShopPluginAdapter> shopPluginAdapter = new HashSet<>();

    @Override
    public void onEnable() {
        super.onEnable();
        this.saveDefaultConfig();
        if(this.getConfig().getBoolean("Settings.deleteShopsOnRegionRestore")) {
            Bukkit.getServer().getPluginManager().registerEvents(new RestoreRegionListener(), this);
        }
        if(this.getConfig().getBoolean("Settings.deleteShopsOnRegionUnsell")) {
            Bukkit.getServer().getPluginManager().registerEvents(new UnsellRegionListener(), this);
        }
        this.shopPluginAdapter = getInstalledAdapters();
        ArmShopBridge.instance = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ResetBlocksEvent.getHandlerList().unregister(this);
        UnsellRegionEvent.getHandlerList().unregister(this);
        ArmShopBridge.instance = null;
    }

    private Set<IShopPluginAdapter> getInstalledAdapters() {
        Set<IShopPluginAdapter> adapters = new HashSet<>();

        if(Bukkit.getPluginManager().getPlugin("QuickShop") != null) {
            adapters.add(new QuickShopAdapter());
        }
        if(Bukkit.getPluginManager().getPlugin("ShopChest") != null) {
            adapters.add(new ShopChestAdapter());
        }
        if(Bukkit.getPluginManager().getPlugin("UltimateShops") != null) {
            adapters.add(new UltimateShopsAdapter());
        }

        return adapters;
    }

    public static ArmShopBridge getInstance() {
        return ArmShopBridge.instance;
    }

    public Set<IShopPluginAdapter> getShopPluginAdapters() {
        return this.shopPluginAdapter;
    }
}
