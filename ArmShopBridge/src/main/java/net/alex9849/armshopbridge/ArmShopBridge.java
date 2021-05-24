package net.alex9849.armshopbridge;

import net.alex9849.arm.events.RestoreRegionEvent;
import net.alex9849.arm.events.UnsellRegionEvent;
import net.alex9849.armshopbridge.interfaces.IShopPluginAdapter;
import net.alex9849.armshopbridge.listener.RestoreRegionListener;
import net.alex9849.armshopbridge.listener.UnsellRegionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        RestoreRegionEvent.getHandlerList().unregister(this);
        UnsellRegionEvent.getHandlerList().unregister(this);
        ArmShopBridge.instance = null;
    }

    private Set<IShopPluginAdapter> getInstalledAdapters() {
        Set<IShopPluginAdapter> adapters = new HashSet<>();
        Set<String> ignorePlugins = new HashSet<>(this.getConfig().getStringList("Settings.ignorePlugins"));
        if(ignorePlugins.stream().noneMatch(x -> x.equalsIgnoreCase("QuickShop"))
                && Bukkit.getPluginManager().getPlugin("QuickShop") != null) {
            addQuickshopAdapter(adapters);
        }
        if(ignorePlugins.stream().noneMatch(x -> x.equalsIgnoreCase("ShopChest"))
                && Bukkit.getPluginManager().getPlugin("ShopChest") != null) {
            addAdapter(adapters, "ShopChest", "net.alex9849.armshopbridge.adapters.ShopChestAdapter");
        }
        if(ignorePlugins.stream().noneMatch(x -> x.equalsIgnoreCase("UltimateShops"))
                && Bukkit.getPluginManager().getPlugin("UltimateShops") != null) {
            addAdapter(adapters, "UltimateShops", "net.alex9849.armshopbridge.adapters.UltimateShopsAdapter");
        }
        if(ignorePlugins.stream().noneMatch(x -> x.equalsIgnoreCase("Shopkeepers"))
                && Bukkit.getPluginManager().getPlugin("Shopkeepers") != null) {
            addAdapter(adapters, "Shopkeepers", "net.alex9849.armshopbridge.adapters.ShopkeepersAdapter");
        }
        if(ignorePlugins.stream().noneMatch(x -> x.equalsIgnoreCase("DisplayShops"))
                && Bukkit.getPluginManager().getPlugin("DisplayShops") != null) {
            addAdapter(adapters, "DisplayShops", "ru.nosooqua.armshopbridge.adapters.DisplayShopsAdapter");
        }

        return adapters;
    }

    private void addQuickshopAdapter(Set<IShopPluginAdapter> adapters) {
        Plugin qsPlugin = Bukkit.getPluginManager().getPlugin("QuickShop");
        if(qsPlugin == null) {
            return;
        }
        String qsVersion = qsPlugin.getDescription().getVersion();
        Pattern pattern = Pattern.compile("\\d(\\d)*(\\.\\d(\\d)*)*");
        Matcher matcher = pattern.matcher(qsVersion);
        if(!matcher.find()) {
            return;
        }
        String qsVersionNumbersString = qsVersion.substring(matcher.start(0), matcher.end(0));
        String[] qsVersionNumberStringSplitted = qsVersionNumbersString.split("\\.");
        List<Integer> versionNumbers = Arrays.stream(qsVersionNumberStringSplitted).map(Integer::parseInt).collect(Collectors.toList());
        if(versionNumbers.size() == 0) {
            return;
        }
        if(versionNumbers.get(0) >= 4) {
            addAdapter(adapters, "QuickShop", "net.alex9849.armshopbridge.adapters.QuickShop4Adapter");
        } else {
            addAdapter(adapters, "QuickShop", "net.alex9849.armshopbridge.adapters.QuickShopAdapter");
        }
    }

    private Set<IShopPluginAdapter> addAdapter(Set<IShopPluginAdapter> adapterSet, String pluginName, String classPath) {
        try {
            Class qsAdapterClass = Class.forName(classPath);
            adapterSet.add((IShopPluginAdapter) qsAdapterClass.newInstance());
            getLogger().log(Level.INFO, "Successfully hooked into " + pluginName);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            getLogger().log(Level.WARNING, "Could not activate " + pluginName + " integration! Ignoring it!");
        }
        return adapterSet;
    }

    public static ArmShopBridge getInstance() {
        return ArmShopBridge.instance;
    }

    public Set<IShopPluginAdapter> getShopPluginAdapters() {
        return this.shopPluginAdapter;
    }
}
