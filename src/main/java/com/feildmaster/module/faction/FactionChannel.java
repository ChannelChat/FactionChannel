package feildmaster.Modules.ChanChat.Faction;

import com.feildmaster.chanchat.Chan.CustomChannel;
import com.feildmaster.chanchat.Chat;
import com.massivecraft.factions.FPlayer;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

public class FactionChannel extends CustomChannel {
    private Set<String> members = null;

    protected FactionChannel(String name) {
        super(name);
    }

    public Set<String> getMembers(Player player) {
        members = new HashSet<String>();

        if(FPlayer.get(player).hasFaction())
            for(FPlayer p : FPlayer.get(player).getFaction().getFPlayers())
                members.add(p.getPlayer().getName());

        return members;
    }

    public void sendJoinMessage(Player player) { // Directly set active
        Chat.getChannelManager().setActiveChan(player, getName());
        player.sendMessage("Now talking in \""+getName()+".\"");
    }

    public void sendLeaveMessage(Player player) { // Do nothing
    }

    public Boolean isSenderMember(Player player) {
        return super.isMember(player) && FPlayer.get(player).hasFaction();
    }

    public Boolean isMember(Player player) {
        if(members == null) return super.isMember(player);
        return super.isMember(player) && members.contains(player.getName());
    }

    public void handleEvent(PlayerChatEvent event) {
        members = getMembers(event.getPlayer());

        super.handleEvent(event);

        members = null;
    }

    public void callReload() {
        factions.getplugin().reloadChannel();
    }
}
