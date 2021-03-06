package cc.crypticcraft.percentsleep;

import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPlugin;

import java.util.HashMap;
import java.util.List;

public class PercentSleep extends JavaPlugin {

    private HashMap<String, PercentSleepWorld> worlds = new HashMap<String, PercentSleepWorld>();

    public static IEssentials essentials;
    public static VanishPlugin vanish;
    public static PercentSleep plugin;
    public static boolean anyPluginsHooked = false;


    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new PercentSleepListener(this), this);
        plugin = this;
        PluginManager pm = Bukkit.getServer().getPluginManager();

        // Hook Vanish/Essentials listeners if the plugins exist
        essentials = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        if (essentials != null) {
            pm.registerEvents(new PercentSleepEssentialsListener(this), this);
            anyPluginsHooked = true;
            this.getLogger().info("Hooked into Essentials");
        }

        vanish = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");
        if (vanish != null) {
            pm.registerEvents(new PercentSleepVanishListener(this), this);
            anyPluginsHooked = true;
            this.getLogger().info("Hooked into VanishNoPacket");
        }
        if (pm.getPlugin("SuperVanish") != null || pm.getPlugin("PremiumVanish") != null) {
            pm.registerEvents(new SVListener(this), this);
            anyPluginsHooked = true;
            this.getLogger().info("Hooked into SuperVanish/PremiumVanish");
        }

        final List<World> worlds = Bukkit.getWorlds();
        final List<String> ignored = this.getConfig().getStringList("ignored-worlds");

        for (World w: worlds) {
            if (w.getEnvironment().equals(World.Environment.NORMAL) && !ignored.contains(w.getName())) {
                this.worlds.put(w.getName(), new PercentSleepWorld(w));
            }
        }
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public HashMap<String, PercentSleepWorld> getWorlds() {
        return this.worlds;
    }
}
