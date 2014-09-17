package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.ccaper.graffitiTracker.enums.ChicagoCityServiceStatusEnum;

// TODO(ccaper): javadoc
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChicagoCityServiceGraffiti {
  @JsonIgnore
  private int id;
  @JsonProperty("service_request_id")
  private String serviceRequestId;
  @JsonProperty("status")
  private ChicagoCityServiceStatusEnum status;
  @JsonProperty("status_notes")
  private String statusNotes;
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
  @JsonIgnore
  private Timestamp systemCreatedTimestamp;
  @JsonIgnore
  private Timestamp systemUpdatedTimestamp;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getServiceRequestId() {
    return serviceRequestId;
  }

  public void setServiceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
  }

  public ChicagoCityServiceStatusEnum getStatus() {
    return status;
  }

  @JsonIgnore
  public void setStatus(ChicagoCityServiceStatusEnum status) {
    this.status = status;
  }

  public void setStatus(String statusString) {
    try {
    status = ChicagoCityServiceStatusEnum
        .getChicagoCityServiceStatusEnumFromDbOrServerString(statusString);
    } catch (IllegalArgumentException e) {
      System.err.println(String.format("Service request '%s' contains invalid status of '%s'", serviceRequestId, status));
      status = null;
    }
  }

  public String getStatusNotes() {
    return statusNotes;
  }

  public void setStatusNotes(String statusNotes) {
    this.statusNotes = statusNotes;
  }

  public Timestamp getRequestedDateTime() {
    return requestedDateTime;
  }

  public void setRequestedDateTime(Timestamp requestedDateTime) {
    this.requestedDateTime = requestedDateTime;
  }

  public Timestamp getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(Timestamp updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getMediaUrl() {
    return mediaUrl;
  }

  public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }

  public Timestamp getSystemCreatedTimestamp() {
    return systemCreatedTimestamp;
  }

  public void setSystemCreatedTimestamp(Timestamp systemCreatedTimestamp) {
    this.systemCreatedTimestamp = systemCreatedTimestamp;
  }

  public Timestamp getSystemUpdatedTimestamp() {
    return systemUpdatedTimestamp;
  }

  public void setSystemUpdatedTimestamp(Timestamp systemUpdatedTimestamp) {
    this.systemUpdatedTimestamp = systemUpdatedTimestamp;
  }

  @Override
  public String toString() {
    return "ChicagoCityServiceGraffiti [id=" + id + ", serviceRequestId="
        + serviceRequestId + ", status=" + status.getDisplayString()
        + ", statusNotes=" + statusNotes + ", requestedDateTime="
        + requestedDateTime + ", updatedDateTime=" + updatedDateTime
        + ", address=" + address + ", lat=" + latitude + ", longitude=" + longitude
        + ", mediaUrl=" + mediaUrl + ", systemCreatedTimestamp="
        + systemCreatedTimestamp + ", systemUpdatedTimestamp="
        + systemUpdatedTimestamp + "]";
  }
}
