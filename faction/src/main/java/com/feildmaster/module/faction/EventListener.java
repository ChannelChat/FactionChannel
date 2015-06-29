package com.feildmaster.module.faction;

import com.feildmaster.channelchat.event.channel.*;
import org.bukkit.event.*;

public class EventListener implements Listener {

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onReload(ReloadEvent event) {
        Module.getplugin().reloadChannel();
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelDelete(ChannelDeleteEvent event) {
        if(event.isCancelled()) return;

        if(event.getChannel() instanceof FactionChannel) {
            event.setCancelled(true);
            event.setCancelReason("You can't delete FactonChannel!");
        }
    }
}
