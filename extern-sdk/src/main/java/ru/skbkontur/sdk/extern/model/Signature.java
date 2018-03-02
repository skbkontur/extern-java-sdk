/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.List;
import java.util.UUID;
import ru.skbkontur.sdk.extern.model.Link;

/**
 *
 * @author AlexS
 */
public class Signature {

	private UUID id = null;
	private Link contentLink = null;
	private List<Link> links = null;

	public Signature id(UUID id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 *
	 * @return id
	 *
	 */
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Signature contentLink(Link contentLink) {
		this.contentLink = contentLink;
		return this;
	}

	/**
	 * Get contentLink
	 *
	 * @return contentLink
	 *
	 */
	public Link getContentLink() {
		return contentLink;
	}

	public void setContentLink(Link contentLink) {
		this.contentLink = contentLink;
	}

	public Signature links(List<Link> links) {
		this.links = links;
		return this;
	}

	/**
	 * Get links
	 *
	 * @return links
	 *
	 */
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
