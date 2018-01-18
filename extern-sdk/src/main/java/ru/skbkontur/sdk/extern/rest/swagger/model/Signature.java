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
import java.util.UUID;
import ru.skbkontur.sdk.extern.rest.swagger.model.Link;

/**
 * Signature
 */

public class Signature {
  @SerializedName("id")
  private UUID id = null;

  @SerializedName("content-data")
  private byte[] contentData = null;

  @SerializedName("content-link")
  private Link contentLink = null;

  public Signature id(UUID id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(example = "00000000-0000-0000-0000-000000000000", value = "")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Signature contentData(byte[] contentData) {
    this.contentData = contentData;
    return this;
  }

   /**
   * Get contentData
   * @return contentData
  **/
  @ApiModelProperty(value = "")
  public byte[] getContentData() {
    return contentData;
  }

  public void setContentData(byte[] contentData) {
    this.contentData = contentData;
  }

  public Signature contentLink(Link contentLink) {
    this.contentLink = contentLink;
    return this;
  }

   /**
   * Get contentLink
   * @return contentLink
  **/
  @ApiModelProperty(value = "")
  public Link getContentLink() {
    return contentLink;
  }

  public void setContentLink(Link contentLink) {
    this.contentLink = contentLink;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Signature signature = (Signature) o;
    return Objects.equals(this.id, signature.id) &&
        Objects.equals(this.contentData, signature.contentData) &&
        Objects.equals(this.contentLink, signature.contentLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, contentData, contentLink);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Signature {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    contentData: ").append(toIndentedString(contentData)).append("\n");
    sb.append("    contentLink: ").append(toIndentedString(contentLink)).append("\n");
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

