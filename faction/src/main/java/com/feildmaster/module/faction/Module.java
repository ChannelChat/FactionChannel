package com.feildmaster.module.faction;

import static com.feildmaster.channelchat.channel.ChannelManager.getManager;

public class Module extends com.feildmaster.channelchat.Module {
    private static Module plugin;
    private FactionChannel channel;

    public void onDisable() {
        removeChannel();
    }

    public void onEnable() {
        plugin = this;

        reloadChannel();
        registerEvents(new EventListener());
    }

    public void reloadChannel() {
        getConfig().load();

        if(getConfig().needsUpdate()) {
            saveDefaultConfig();
        }

        if(channel == null) {
            channel = new FactionChannel(getConfig().getString("name"));
        }

        channel.setTag(getConfig().getString("tag"));
        channel.setListed(getConfig().getBoolean("listed"));
        channel.setAuto(getConfig().getBoolean("auto"));
        if(!channel.setAlias(getConfig().getString("alias"))) {
            getServer().getLogger().info(channel.getName()+" - Alias "+getConfig().getString("alias")+" is taken.");
        }

        if(getConfig().getBoolean("enabled")) addChannel();
        else removeChannel();
    }

    private void addChannel() {
        if(!getManager().addChannel(channel)) {
            getServer().getLogger().info("Channel could not be added! (Name {"+channel.getName()+"} taken)");
            channel = null;
        }
    }

    private void removeChannel() {
        channel.sendMessage("FactionChannel has disabled, or is reloading.");
        getManager().deleteChannel(channel);
        channel = null;
    }

    public static Module getplugin() {
        return plugin;
    }
}