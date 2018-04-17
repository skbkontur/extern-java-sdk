/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.List;
import java.util.UUID;


/**
 * @author AlexS
 */
public class Document {

    private UUID id = null;
    private DocumentDescription description = null;
    private Content content = null;
    private List<Signature> signatures = null;
    private List<Link> links = null;

    public Document id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Document description(DocumentDescription description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     */
    public DocumentDescription getDescription() {
        return description;
    }

    public void setDescription(DocumentDescription description) {
        this.description = description;
    }

    public Document content(Content content) {
        this.content = content;
        return this;
    }

    /**
     * Get content
     *
     * @return content
     */
    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Document signatures(List<Signature> signatures) {
        this.signatures = signatures;
        return this;
    }

    /**
     * Get signatures
     *
     * @return signatures
     */
    public List<Signature> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<Signature> signatures) {
        this.signatures = signatures;
    }

    public Document links(List<Link> links) {
        this.links = links;
        return this;
    }

    /**
     * Get links
     *
     * @return links
     */
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
