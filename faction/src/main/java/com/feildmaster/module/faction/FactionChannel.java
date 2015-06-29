package com.feildmaster.module.faction;

import com.feildmaster.channelchat.Chat;
import com.feildmaster.channelchat.channel.CustomChannel;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.Faction;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class FactionChannel extends CustomChannel {
    private Faction faction = null;
    private MPlayer mplayer;

    protected FactionChannel(String name)
    {
        super(name);
    }

    private void setFaction(Player player)
    {
    	player = player.getPlayer(); //get Bukkit player entity
        mplayer = MPlayer.get(player); //Set Massive player to Bukkit player
    	
        if(player == null) //Did a player execute the command?
        {
            faction = null; //No player, no faction
            return;
        }
        
        if(!mplayer.hasFaction()) //Did a massiveplayer execute the command?
        {
        	//This method seems a bit redundant seeing as mplayer is player.
        	//If player is no, mplayer will be no.
        	//But, perhaps there's a purpose for this. Leaving in.
            faction = null; 
            return;
        }

        faction = mplayer.getFaction(); //Everything checks out
    }

    public Set<String> getMembers(Player player)
    {
        Set<String> members = super.getMembers(player);
        setFaction(player);

        for(String p : new HashSet<String>(members))
            if(faction != null)
            {
                MPlayer fp = MPlayer.get(player);
                if(!fp.hasFaction() || !fp.getFaction().equals(faction))
                    members.remove(p);
            }
            else
            {	
                members.remove(p);
            }

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
        MPlayer mplayer = MPlayer.get(player);
        return super.isMember(player) && mplayer.hasFaction() && mplayer.getFaction().equals(faction);
    }

    public void handleEvent(AsyncPlayerChatEvent event) {
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