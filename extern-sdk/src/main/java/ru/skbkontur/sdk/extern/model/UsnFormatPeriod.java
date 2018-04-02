/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

/**
 *
 * @author alexs
 */
public class UsnFormatPeriod {

	public enum PeriodModifiersEnum {
    NONE("none"),
    
    LIQUIDATIONREORGANIZATION("liquidationReorganization"),
    
    TAXREGIMECHANGE("taxRegimeChange"),
    
    LASTPERIODFORTAXREGIME("lastPeriodForTaxRegime");

    private final String value;

    PeriodModifiersEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static PeriodModifiersEnum fromValue(String text) {
      for (PeriodModifiersEnum b : PeriodModifiersEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  private PeriodModifiersEnum periodModifiers = null;
	
  private Integer year = null;

	public PeriodModifiersEnum getPeriodModifiers() {
		return periodModifiers;
	}

	public void setPeriodModifiers(PeriodModifiersEnum periodModifiers) {
		this.periodModifiers = periodModifiers;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
}
