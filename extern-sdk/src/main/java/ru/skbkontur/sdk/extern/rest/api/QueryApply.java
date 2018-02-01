/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.rest.api;

import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;

/**
 * @author AlexS
 * API REST Query
 * @param <R> some return type
 */
@FunctionalInterface
public interface QueryApply<R> {
	QueryContext<R> apply(QueryContext<R> context) throws ApiException;
}
