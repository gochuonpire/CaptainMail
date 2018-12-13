package me.captain.mail;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author captainawesome7
 */
public class CaptainMail extends JavaPlugin {

    private MailHandler mh;
    
    @Override
    public void onEnable() {
        getCommand("mail").setExecutor(new MailCmd(this));
        this.getServer().getPluginManager().registerEvents(new MailListener(this), this);
        mh = new MailHandler(this);
        mh.loadMail();
    }
    
    @Override
    public void onDisable() {
        mh.saveMail();
    }
    
    public MailHandler getMailHandler() {
        return mh;
    }
}
