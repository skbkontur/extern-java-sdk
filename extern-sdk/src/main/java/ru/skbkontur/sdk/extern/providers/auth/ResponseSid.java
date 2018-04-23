/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author AlexS
 */
class ResponseSid {

		@SerializedName("Sid")
		private String sid;

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}
}
