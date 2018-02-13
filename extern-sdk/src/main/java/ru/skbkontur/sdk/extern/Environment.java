/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import com.google.gson.Gson;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;

/**
 *
 * @author AlexS
 */
public class Environment {
	public final Gson gson = new Gson();

	public Configuration configuration;
	public String accessToken;
	public CryptoService cryptoService;
	public Key signKey;
	
	public Environment() {
		configuration = new Configuration();
	}
}
