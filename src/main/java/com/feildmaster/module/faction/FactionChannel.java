package com.feildmaster.module.faction;

import com.feildmaster.channelchat.Chat;
import com.feildmaster.channelchat.channel.CustomChannel;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

public class FactionChannel extends CustomChannel {
    private Faction faction = null;

    protected FactionChannel(String name) {
        super(name);
    }

    private void setFaction(Player player) {
        if(player == null) {
            faction = null;
            return;
        }
        FPlayer p = FPlayers.i.get(player);

        if(!p.hasFaction()) {
            faction = null;
            return;
        }

        faction = p.getFaction();
    }

    public Set<String> getMembers(Player player) {
        Set<String> members = super.getMembers(player);
        setFaction(player);

        for(String p : new HashSet<String>(members))
            if(faction != null) {
                FPlayer fp = FPlayers.i.get(Bukkit.getPlayer(p));
                if(!fp.hasFaction() || !fp.getFaction().equals(faction))
                    members.remove(p);
            } else
                members.remove(p);

        setFaction(null);
        return members;
    }

    public void sendJoinMessage(Player player) {
        setFaction(player);
        if(faction == null) {
            player.sendMessage(Chat.info("You have joined \""+getName()+"\""));
            return;
        }
        super.sendJoinMessage(player);
        setFaction(null);
    }

    public void sendLeaveMessage(Player player) {
        setFaction(player);
        if(faction == null) {
            player.sendMessage(Chat.info("You have left \""+getName()+"\""));
            return;
        }
        super.sendLeaveMessage(player);
        setFaction(null);
    }

    public Boolean isMember(Player player) {
        if(faction == null) return super.isMember(player);
        FPlayer fplayer = FPlayers.i.get(player);
        return super.isMember(player) && fplayer.hasFaction() && fplayer.getFaction().equals(faction);
    }

    public void handleEvent(PlayerChatEvent event) {
        setFaction(event.getPlayer());
        if(faction == null) {
            event.getPlayer().sendMessage(Chat.info("You do not have a faction."));
            event.setCancelled(true);
            return;
        }
        super.handleEvent(event);
        setFaction(null);
    }
}
