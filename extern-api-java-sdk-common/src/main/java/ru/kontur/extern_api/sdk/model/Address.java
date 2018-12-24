/*
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
 *
 */

package ru.kontur.extern_api.sdk.model;

public class Address {

    /** Индекс */
    private String index;

    /** Код региона (единственный обязательный) */
    private String region;

    /** Район */
    private String district;

    /** Корпус */
    private String city;

    /** Населенный пункт */
    private String settlement;

    /** Корпус */
    private String street;

    /** Дом */
    private String house;

    /** Корпус */
    private String building;

    /** Квартира */
    private String flat;

    public Address setIndex(String index) {
        this.index = index;
        return this;
    }

    public Address setRegion(String region) {
        this.region = region;
        return this;
    }

    public Address setDistrict(String district) {
        this.district = district;
        return this;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public Address setSettlement(String settlement) {
        this.settlement = settlement;
        return this;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public Address setHouse(String house) {
        this.house = house;
        return this;
    }

    public Address setBuilding(String building) {
        this.building = building;
        return this;
    }

    public Address setFlat(String flat) {
        this.flat = flat;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public String getRegion() {
        return region;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getSettlement() {
        return settlement;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public String getBuilding() {
        return building;
    }

    public String getFlat() {
        return flat;
    }
}
