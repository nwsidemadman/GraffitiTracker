package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.ccaper.graffitiTracker.enums.ChicagoCityServiceStatusEnum;

/**
 * 
 * @author ccaper
 * 
 *         POJO representing a Chicago city services request for graffiti
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChicagoCityServiceGraffiti {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public class ExtendedAttributes {
    @JsonProperty("ward")
    private int ward;
    @JsonProperty("police")
    private int policeDistrict;
    @JsonProperty("zip")
    private int zipcode;

    public ExtendedAttributes() {
    }

    public int getWard() {
      return ward;
    }

    public void setWard(int ward) {
      this.ward = ward;
    }

    public int getPoliceDistrict() {
      return policeDistrict;
    }

    public void setPoliceDistrict(int policeDistrict) {
      this.policeDistrict = policeDistrict;
    }

    public int getZipcode() {
      return zipcode;
    }

    public void setZipcode(int zip) {
      this.zipcode = zip;
    }
  }

  @JsonIgnore
  private int id;
  @JsonProperty("service_request_id")
  private String serviceRequestId;
  @JsonProperty("status")
  private ChicagoCityServiceStatusEnum status;
  @JsonProperty("requested_datetime")
  private Timestamp requestedDateTime;
  @JsonProperty("updated_datetime")
  private Timestamp updatedDateTime;
  @JsonProperty("address")
  private String address;
  @JsonProperty("lat")
  private double latitude;
  @JsonProperty("long")
  private double longitude;
  @JsonProperty("media_url")
  private String mediaUrl;
  @JsonProperty("extended_attributes")
  private ExtendedAttributes extendedAttributes;
  @JsonIgnore
  private Timestamp systemCreatedTimestamp;
  @JsonIgnore
  private Timestamp systemUpdatedTimestamp;

  /**
   * Gets the id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id
   *          the new id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the service request id.
   *
   * @return the service request id
   */
  public String getServiceRequestId() {
    return serviceRequestId;
  }

  /**
   * Sets the service request id.
   *
   * @param serviceRequestId
   *          the new service request id
   */
  public void setServiceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
  }

  /**
   * Gets the status.
   *
   * @return the status
   */
  public ChicagoCityServiceStatusEnum getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status
   *          the new status
   */
  @JsonIgnore
  public void setStatus(ChicagoCityServiceStatusEnum status) {
    this.status = status;
  }

  /**
   * Sets the status.
   *
   * @param statusString
   *          the new status
   */
  public void setStatus(String statusString) {
    status = ChicagoCityServiceStatusEnum
        .getChicagoCityServiceStatusEnumFromDbOrServerString(statusString);
  }

  /**
   * Gets the requested date time.
   *
   * @return the requested date time
   */
  public Timestamp getRequestedDateTime() {
    return requestedDateTime;
  }

  /**
   * Sets the requested date time.
   *
   * @param requestedDateTime
   *          the new requested date time
   */
  public void setRequestedDateTime(Timestamp requestedDateTime) {
    this.requestedDateTime = requestedDateTime;
  }

  /**
   * Gets the updated date time.
   *
   * @return the updated date time
   */
  public Timestamp getUpdatedDateTime() {
    return updatedDateTime;
  }

  /**
   * Sets the updated date time.
   *
   * @param updatedDateTime
   *          the new updated date time
   */
  public void setUpdatedDateTime(Timestamp updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  /**
   * Gets the address.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address.
   *
   * @param address
   *          the new address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Gets the latitude.
   *
   * @return the latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude.
   *
   * @param latitude
   *          the new latitude
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  /**
   * Gets the longitude.
   *
   * @return the longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude.
   *
   * @param longitude
   *          the new longitude
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  /**
   * Gets the media url.
   *
   * @return the media url
   */
  public String getMediaUrl() {
    return mediaUrl;
  }

  /**
   * Sets the media url.
   *
   * @param mediaUrl
   *          the new media url
   */
  public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }

  public ExtendedAttributes getExtendedAttributes() {
    return extendedAttributes;
  }

  public void setExtendedAttributes(ExtendedAttributes extendedAttributes) {
    this.extendedAttributes = extendedAttributes;
  }

  /**
   * Gets the system created timestamp. This is the timestamp of the data when
   * it was brought into the GraffitiTracker system.
   *
   * @return the system created timestamp
   */
  public Timestamp getSystemCreatedTimestamp() {
    return systemCreatedTimestamp;
  }

  /**
   * Sets the system created timestamp. This is the timestamp of the data when
   * it was brought into the GraffitiTracker system.
   *
   * @param systemCreatedTimestamp
   *          the new system created timestamp
   */
  public void setSystemCreatedTimestamp(Timestamp systemCreatedTimestamp) {
    this.systemCreatedTimestamp = systemCreatedTimestamp;
  }

  /**
   * Gets the system updated timestamp. This is the timestamp of when the data
   * was updated in the graffiti tracker system.
   *
   * @return the system updated timestamp
   */
  public Timestamp getSystemUpdatedTimestamp() {
    return systemUpdatedTimestamp;
  }

  /**
   * Sets the system updated timestamp. This is the timestamp of when the data
   * was updated in the graffiti tracker system.
   *
   * @param systemUpdatedTimestamp
   *          the new system updated timestamp
   */
  public void setSystemUpdatedTimestamp(Timestamp systemUpdatedTimestamp) {
    this.systemUpdatedTimestamp = systemUpdatedTimestamp;
  }
}
