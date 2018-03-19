/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author alexs
 */
public class ResponseLink {

		@SerializedName("Link")
		private Link link;
		@SerializedName("Key")
		private String key;

		public ResponseLink() {
		}

		public Link getLink() {
			return link;
		}

		public void setLink(Link link) {
			this.link = link;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
}
