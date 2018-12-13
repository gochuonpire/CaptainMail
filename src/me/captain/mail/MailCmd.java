/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.mail;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class MailCmd implements CommandExecutor {

    private final CaptainMail plugin;

    public MailCmd(CaptainMail instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player player = (Player) cs;
            switch (args.length) {
                case 0:
                    player.sendMessage(ChatColor.GRAY + "=== " + ChatColor.GREEN + "CaptainMail " + ChatColor.GRAY + "===");
                    if (player.hasPermission("captainmail.send")) {
                        player.sendMessage(ChatColor.GRAY + "/mail open - Reads all new mail");
                        player.sendMessage(ChatColor.GRAY + "/mail read player - Reads all the mail you have sent/received from that player");
                        player.sendMessage(ChatColor.GRAY + "/mail read sent - Reads all sent mail");
                        player.sendMessage(ChatColor.GRAY + "/mail send player message text here - Send the player a message");
                        player.sendMessage(ChatColor.GRAY + "/mail clear - Delete all your mail");
                        player.sendMessage(ChatColor.GRAY + "/mail clear sent - Delete all your sent mail (both mailboxes)");
                        player.sendMessage(ChatColor.GRAY + "/mail clear player - Delete mail sent/received from player");
                    }
                    break;
                case 1:
                    if (args[0].equals("open")) {
                        plugin.getMailHandler().readAllNew(player);
                    }
                    if (args[0].equals("clear")) {
                        plugin.getMailHandler().clearMail(player, false);
                    }
                    break;
                case 2:
                    if (args[0].equals("read")) {
                        if (args[1].equals("sent")) {
                            plugin.getMailHandler().readAllSent(player);
                        } else {
                            String sent = args[1];
                            plugin.getMailHandler().readAllPlayerMail(player, sent);
                        }
                    } else if (args[0].equals("clear")) {
                        if (args[1].equals("sent")) {
                            plugin.getMailHandler().clearMail(player, true);
                        } else {
                            String sent = args[1];
                            plugin.getMailHandler().clearMail(player, sent);
                        }
                    }
                    break;
            }
            if (args.length > 2) {
                if (args[0].equals("send")) {
                    String sent = args[1];
                    int i = 0;
                    StringBuilder builder = new StringBuilder();
                    for (String s : args) {
                        if (i >= 2) {
                            builder.append(s);
                            builder.append(" ");
                        }
                        i += 1;
                    }
                    String m = builder.toString();
                    String msg = m.replaceAll("(&([a-f0-9]))", "\u00A7$2");
                    Mail mail = new Mail(sent, player.getName(), msg, false);
                    plugin.getMailHandler().sendMail(mail);
                }
            }
        } else {

        }
        return true;
    }

}
