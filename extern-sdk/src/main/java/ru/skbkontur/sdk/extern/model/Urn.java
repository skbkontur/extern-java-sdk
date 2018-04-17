/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

/**
 * @author AlexS
 */
public class Urn {

    private String nid = null;
    private String nss = null;
    private String value = null;

    /**
     * Get nid
     *
     * @return nid
     */
    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    /**
     * Get nss
     *
     * @return nss
     */
    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    /**
     * Get value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
