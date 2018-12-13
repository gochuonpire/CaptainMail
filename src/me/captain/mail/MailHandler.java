/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.mail;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class MailHandler {

    private final CaptainMail plugin;

    private final ArrayList<Mail> mail;

    public MailHandler(CaptainMail instance) {
        plugin = instance;
        mail = new ArrayList();
    }

    public void loadMail() {
        try {
            File f = new File(plugin.getDataFolder(), "mail.yml");
            YamlConfiguration mailf = new YamlConfiguration();
            mailf.load(f);
            for (String s : mailf.getKeys(false)) {
                String p = mailf.getString(s + ".player");
                String send = mailf.getString(s + ".sender");
                String msg = mailf.getString(s + ".msg");
                Boolean read = mailf.getBoolean(s + ".read");
                Mail m = new Mail(p, send, msg, read);
                mail.add(m);
            }
            System.out.println("[CaptainMail] Mail loaded.");
        } catch (Exception e) {
            System.out.println("[CaptainMail] Error while loading mail.yml");
        }
    }

    public void saveMail() {
        try {
            File f = new File(plugin.getDataFolder(), "mail.yml");
            YamlConfiguration mailf = new YamlConfiguration();
            for (Mail m : mail) {
                mailf.set(m.getPlayer() + mail.indexOf(m) + ".player", m.getPlayer());
                mailf.set(m.getPlayer() + mail.indexOf(m) + ".sender", m.getSender());
                mailf.set(m.getPlayer() + mail.indexOf(m) + ".msg", m.getMessage());
                mailf.set(m.getPlayer() + mail.indexOf(m) + ".read", m.getRead());
            }
            mailf.save(f);
            System.out.println("[CaptainMail] Mail saved.");
        } catch (Exception e) {
            System.out.println("[CaptainMail] Error saving mail.yml");
        }
    }

    public void sendMail(Mail m) {
        mail.add(m);
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.getName().equals(m.getPlayer())) {
                p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] You received a message from " + m.getSender());
                if (isNew(p)) {
                    p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] To read your new mail, type /mail open");
                }
            } else if (p.getName().equals(m.getSender())) {
                p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] You sent a message to " + m.getPlayer());
                if (isNewSend(p)) {
                    p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] To read sent mail, type /mail read sent");
                }
            }
        }
    }

    public boolean isNew(Player p) {
        int i = 0;
        for (Mail m : mail) {
            if (m.getPlayer().equals(p.getName())) {
                i++;
            }
        }
        return i <= 1;
    }

    public boolean isNewSend(Player p) {
        int i = 0;
        for (Mail m : mail) {
            if (m.getSender().equals(p.getName())) {
                i++;
            }
        }
        return i <= 1;
    }

    public void readAllNew(Player p) {
        int i = 0;
        for (Mail m : mail) {
            if (m.getPlayer().equals(p.getName()) && !m.getRead()) {
                readMail(m);
                m.setRead(true);
                i++;
            }
        }
        if (i == 0) {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] No new mail");
        }
    }

    public void readAllSent(Player p) {
        int i = 0;
        for (Mail m : mail) {
            if (m.getSender().equals(p.getName())) {
                readSent(m);
                i++;
            }
        }
        if (i == 0) {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] No sent mail");
        }
    }

    public boolean readMail(Mail m) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.getName().equals(m.getPlayer())) {
                p.sendMessage(ChatColor.GRAY + "[Mail]" + ChatColor.GREEN + m.getSender() + ChatColor.GRAY + "->" + ChatColor.GREEN + "You" + ChatColor.GRAY + ":" + m.getMessage());
                return true;
            }
        }
        return false;
    }

    public boolean readSent(Mail m) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.getName().equals(m.getSender())) {
                p.sendMessage(ChatColor.GRAY + "[Mail]" + ChatColor.GREEN + "You" + ChatColor.GRAY + "-> " + ChatColor.GREEN + m.getPlayer() + ChatColor.GRAY + ":" + m.getMessage());
                return true;
            }
        }
        return false;
    }

    public void clearMail(Player p, boolean sent) {
        ArrayList<Mail> todelete = new ArrayList();
        for (Mail m : mail) {
            if (m.getPlayer().equals(p.getName()) && !sent) {
                todelete.add(m);
            } else if (m.getSender().equals(p.getName()) && sent) {
                todelete.add(m);
            }
        }
        todelete.forEach((m) -> {
            mail.remove(m);
        });
    }

    public void readAllPlayerMail(Player p, String sent) {
        int i = 0;
        for (Mail m : mail) {
            if (m.getPlayer().equals(sent) && m.getSender().equals(p.getName())) {
                readSent(m);
                i++;
            } else if (m.getSender().equals(sent) && m.getPlayer().equals(p.getName())) {
                readMail(m);
                i++;
            }
        }
        if (i == 0) {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] No mail sent to or recieved from " + sent);
        }
    }

    public void clearMail(Player p, String sent) {
        int i = 0;
        ArrayList<Mail> todelete = new ArrayList();
        for (Mail m : mail) {
            if (m.getPlayer().equals(sent) && m.getSender().equals(p.getName())) {
                todelete.add(m);
                i++;
            } else if (m.getPlayer().equals(p.getName()) && m.getSender().equals(sent)) {
                todelete.add(m);
                i++;
            }
        }
        todelete.forEach((m) -> {
            mail.remove(m);
        });
        if (i == 0) {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] No mail sent/received to " + sent);
        } else {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] Mail sent/received to " + sent + " ");
        }
    }
    
    public void welcome(Player p) {
        int h = hasMail(p.getName());
        if(h==0) {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] No new mail");
        } else if(h==1) {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] One new message");
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] To read your new mail, type /mail open");
        } else if(h>1) {
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] " + h + " new messages");
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Mail" + ChatColor.GRAY + "] To read your new mail, type /mail open");
        }
    }
    
    public int hasMail(String p) {
        int i = 0;
        for(Mail m : mail) {
            if(m.getPlayer().equals(p)) {
                i++;
            }
        }
        return i;
    }

}
