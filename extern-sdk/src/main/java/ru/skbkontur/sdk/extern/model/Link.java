/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.Objects;

/**
 *
 * @author AlexS
 */
public class Link {

    private String href = null;
    private String rel = null;
    private String name = null;
    private String title = null;
    private String profile = null;
    private Boolean templated = null;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Boolean getTemplated() {
        return templated;
    }

    public void setTemplated(Boolean templated) {
        this.templated = templated;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(this.href, link.href)
            && Objects.equals(this.rel, link.rel)
            && Objects.equals(this.name, link.name)
            && Objects.equals(this.title, link.title)
            && Objects.equals(this.profile, link.profile)
            && Objects.equals(this.templated, link.templated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, rel, name, title, profile, templated);
    }

}
