/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.rest.api;

import com.google.gson.Gson;
import java.util.Map;

/**
 *
 * @author AlexS
 */
public class ApiWrap {

	protected <T> T jsonToDTO(Map<?, ?> response, Class<T> t) {
		Gson gson = new Gson();
		String json = gson.toJson(response);
		return gson.fromJson(json, t);
	}
}
