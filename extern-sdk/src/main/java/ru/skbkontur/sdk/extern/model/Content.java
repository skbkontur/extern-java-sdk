/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

/**
 * @author AlexS
 */
public class Content {

    private Link decrypted = null;
    private Link encrypted = null;

    public Content decrypted(Link decrypted) {
        this.decrypted = decrypted;
        return this;
    }

    /**
     * Get decrypted
     *
     * @return decrypted
     */
    public Link getDecrypted() {
        return decrypted;
    }

    public void setDecrypted(Link decrypted) {
        this.decrypted = decrypted;
    }

    public Content encrypted(Link encrypted) {
        this.encrypted = encrypted;
        return this;
    }

    /**
     * Get encrypted
     *
     * @return encrypted
     */
    public Link getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Link encrypted) {
        this.encrypted = encrypted;
    }
}
