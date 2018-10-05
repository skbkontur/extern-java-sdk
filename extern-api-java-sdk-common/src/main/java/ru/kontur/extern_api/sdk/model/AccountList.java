/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Класс реализует постраничное чтение списка учетных записей
 * Используется в группе запросов {@code AccountService}
 * </p>
 * @author Aleksey Sukhorukov
 */
public class AccountList {

	private Long skip = null;

	private Long take = null;

	@SerializedName("total-count")
	private Long totalCount = null;

	private List<Account> accounts = new ArrayList<>();

	/**
	 * Метод возвращает список объектов класса Account
	 * @return список объектов класса Account
	 * @see Account
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

	/**
	 * Метод устанавливает список объектов класса Account
	 * @param accounts список объектов класса
	 * @see Account
	 */
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	/**
	 * Метод возвращает общее количество учетных записей
	 * @return общее количество учетных записей
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * Метод устанавливает общее количество учетных записей
	 * @param totalCount общее количество учетных записей
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Метод возвращает порядковый номер первой записи в коллекции accounts
	 * @return порядковый номер первой записи в коллекции accounts
	 */
	public Long getSkip() {
		return skip;
	}

	/**
	 * Метод устанавливает порядковый номер первой записи в коллекции accounts
	 * @param skip порядковый номер первой записи в коллекции accounts
	 */
	public void setSkip(Long skip) {
		this.skip = skip;
	}

	/**
	 * Метод возврвщвет максимальное количество элементов в возвращаемой коллекции accounts
	 * @return максимальное количество элементов в возвращаемой коллекции accounts
	 */
	public Long getTake() {
		return take;
	}

	/**
	 * Метод устанавливает максимальное количество элементов в возвращаемой коллекции accounts
	 * @param take максимальное количество элементов в возвращаемой коллекции accounts
	 */
	public void setTake(Long take) {
		this.take = take;
	}
}
