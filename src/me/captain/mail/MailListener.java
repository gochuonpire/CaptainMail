package me.captain.mail;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


/**
 *
 * @author andre
 */
public class MailListener implements Listener {
    public CaptainMail plugin;
    
    public MailListener(CaptainMail instance) {
        plugin = instance;
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        plugin.getMailHandler().welcome(event.getPlayer());
    }
    
}
