package feildmaster.Modules.ChanChat.Faction;

import com.massivecraft.factions.Factions;
import com.feildmaster.chanchat.Chat;
import com.feildmaster.chanchat.Util.ModuleConfiguration;
import org.blockface.bukkitstats.CallHome;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class factions extends JavaPlugin {
    public static Factions factionPlugin;
    private static factions plugin;

    private FactionChannel channel;

    private ModuleConfiguration config;

    // Configuration
    private boolean enabled;
    private boolean listed;
    private String name;
    private String tag;
    private String alias;
    private ChatColor color;


    public void onDisable() {
        Chat.getChannelManager().delChannel(channel.getName());
        getServer().getLogger().info(String.format("[%1$s] Disabled!", getDescription().getName()));
    }

    public void onEnable() {
        plugin = this;
        CallHome.load(this);

        setupConfig();

        try {
            factionPlugin = (Factions)getServer().getPluginManager().getPlugin("Factions");
        } catch (Exception e) {
            getServer().getLogger().info(String.format("[%1$s] %2$s", getDescription().getName(), "Factions not found, channel not created."));
        }

        reloadChannel();

        if(enabled)
            Chat.getChannelManager().addChannel(channel);

        getServer().getLogger().info(String.format("[%1$s] v%2$s Enabled!", getDescription().getName(), getDescription().getVersion()));
    }

    private void setupConfig() {
        config = new ModuleConfiguration(this);

        if(!config.exists())
            config.saveDefaults();

        loadConfig();
    }

    private void loadConfig() {
        config.load();

        enabled = config.getBoolean("enabled");
        name = config.getString("name");
        tag = config.getString("tag");
        alias = config.getString("alias");
        listed = config.getBoolean("listed");
        color = config.getChatColor("Town.color");
    }

    public void reloadChannel() {
        loadConfig();

        if(channel == null) channel = new FactionChannel(name);

        channel.setTag(tag);
        channel.setAlias(alias);
        channel.setListed(listed);
        channel.setChatColor(color);
    }

    public static factions getplugin() {
        return plugin;
    }
}
