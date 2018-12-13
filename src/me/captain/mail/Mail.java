/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.mail;

/**
 *
 * @author andre
 */
public class Mail {
    private String player;
    private String sender;
    private String message;
    private Boolean read;
    
    public Mail(String p, String s, String msg, Boolean r) {
        player = p;
        sender = s;
        message = msg;
        read = r;
    }

    /**
     * @return the player
     */
    public String getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the read
     */
    public Boolean getRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(Boolean read) {
        this.read = read;
    }
}
