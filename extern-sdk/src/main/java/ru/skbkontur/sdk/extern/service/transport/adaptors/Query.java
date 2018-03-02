/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

/**
 *
 * @author AlexS
 */
@FunctionalInterface
public interface Query<R> {
	QueryContext<R> apply(QueryContext<R> context);
}
