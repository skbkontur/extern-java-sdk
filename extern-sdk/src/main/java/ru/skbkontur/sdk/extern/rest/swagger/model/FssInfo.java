/*
 * Kontur.Extern.Api.Public
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package ru.skbkontur.sdk.extern.rest.swagger.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import ru.skbkontur.sdk.extern.rest.swagger.model.EmployeeInfo;

/**
 * FssInfo
 */

public class FssInfo {
  @SerializedName("registration-number")
  private String registrationNumber = null;

  @SerializedName("additional-code")
  private String additionalCode = null;

  @SerializedName("dependency-code")
  private String dependencyCode = null;

  @SerializedName("representative")
  private EmployeeInfo representative = null;

  public FssInfo registrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
    return this;
  }

   /**
   * Get registrationNumber
   * @return registrationNumber
  **/
  @ApiModelProperty(value = "")
  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public FssInfo additionalCode(String additionalCode) {
    this.additionalCode = additionalCode;
    return this;
  }

   /**
   * Get additionalCode
   * @return additionalCode
  **/
  @ApiModelProperty(value = "")
  public String getAdditionalCode() {
    return additionalCode;
  }

  public void setAdditionalCode(String additionalCode) {
    this.additionalCode = additionalCode;
  }

  public FssInfo dependencyCode(String dependencyCode) {
    this.dependencyCode = dependencyCode;
    return this;
  }

   /**
   * Get dependencyCode
   * @return dependencyCode
  **/
  @ApiModelProperty(value = "")
  public String getDependencyCode() {
    return dependencyCode;
  }

  public void setDependencyCode(String dependencyCode) {
    this.dependencyCode = dependencyCode;
  }

  public FssInfo representative(EmployeeInfo representative) {
    this.representative = representative;
    return this;
  }

   /**
   * Get representative
   * @return representative
  **/
  @ApiModelProperty(value = "")
  public EmployeeInfo getRepresentative() {
    return representative;
  }

  public void setRepresentative(EmployeeInfo representative) {
    this.representative = representative;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FssInfo fssInfo = (FssInfo) o;
    return Objects.equals(this.registrationNumber, fssInfo.registrationNumber) &&
        Objects.equals(this.additionalCode, fssInfo.additionalCode) &&
        Objects.equals(this.dependencyCode, fssInfo.dependencyCode) &&
        Objects.equals(this.representative, fssInfo.representative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(registrationNumber, additionalCode, dependencyCode, representative);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FssInfo {\n");
    
    sb.append("    registrationNumber: ").append(toIndentedString(registrationNumber)).append("\n");
    sb.append("    additionalCode: ").append(toIndentedString(additionalCode)).append("\n");
    sb.append("    dependencyCode: ").append(toIndentedString(dependencyCode)).append("\n");
    sb.append("    representative: ").append(toIndentedString(representative)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

