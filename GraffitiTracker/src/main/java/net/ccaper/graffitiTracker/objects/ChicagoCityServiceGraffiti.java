package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

import net.ccaper.graffitiTracker.enums.ChicagoCityServiceStatusEnum;

public class ChicagoCityServiceGraffiti {
  private int id;
  private String serviceRequestId;
  private ChicagoCityServiceStatusEnum chicagoCityServiceStatusEnum;
  private String statusNotes;
  // TODO(ccaper): this is different type than json
  private Timestamp requestedDateTime;
//TODO(ccaper): this is different type than json
  private Timestamp updatedDateTime;
  private String address;
  private float lat;
  // TODO(ccaper): this doesn't match json
  private float longitude;
  private String mediaUrl;
  private Timestamp systemCreatedTimestamp;
  private Timestamp systemUpdatedTimestamp;
}
