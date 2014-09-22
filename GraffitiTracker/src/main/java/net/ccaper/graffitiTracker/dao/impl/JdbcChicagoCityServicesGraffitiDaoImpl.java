package net.ccaper.graffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesGraffitiDao;
import net.ccaper.graffitiTracker.enums.ChicagoCityServiceStatusEnum;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

/**
 * 
 * @author ccaper
 * 
 *         JDBC implementation for ChicagoCityServicesGraffitiDao.
 * 
 */
@Repository("chicagoCityServicesGraffitiDao")
public class JdbcChicagoCityServicesGraffitiDaoImpl extends
    NamedParameterJdbcDaoSupport implements ChicagoCityServicesGraffitiDao {
  private static final String CHICAGO_CITY_SERVICE_GRAFFITI_TABLE = "chicago_city_service_data_graffiti";
  private static final String SERVICE_REQUEST_ID_COL = "service_request_id";
  private static final String ID_COL = "id";
  private static final String STATUS_COL = "status";
  private static final String STATUS_NOTES_COL = "status_notes";
  private static final String REQUESTED_DATETIME_COL = "requested_datetime";
  private static final String UPDATED_DATETIME_COL = "updated_datetime";
  private static final String ADDRESS_COL = "address";
  private static final String LATITUDE_COL = "latitude";
  private static final String LONGITUDE_COL = "longitude";
  private static final String MEDIA_URL_COL = "media_url";
  private static final String SYSTEM_CREATED_TIMESTAMP_COL = "system_created_timestamp";
  private static final String SYSTEM_UPDATED_TIMESTAMP_COL = "system_updated_timestamp";

  private static final String SQL_INSERT_GRAFFITI = String
      .format(
          "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES "
              + "(:%s, :%s, :%s, :%s, :%s, :%s, :%s, :%s, :%s) "
              + "ON DUPLICATE KEY UPDATE %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s",
          CHICAGO_CITY_SERVICE_GRAFFITI_TABLE, SERVICE_REQUEST_ID_COL,
          STATUS_COL, STATUS_NOTES_COL, REQUESTED_DATETIME_COL,
          UPDATED_DATETIME_COL, ADDRESS_COL, LATITUDE_COL, LONGITUDE_COL,
          MEDIA_URL_COL, SERVICE_REQUEST_ID_COL, STATUS_COL, STATUS_NOTES_COL,
          REQUESTED_DATETIME_COL, UPDATED_DATETIME_COL, ADDRESS_COL,
          LATITUDE_COL, LONGITUDE_COL, MEDIA_URL_COL, STATUS_COL, STATUS_COL,
          STATUS_NOTES_COL, STATUS_NOTES_COL, REQUESTED_DATETIME_COL,
          REQUESTED_DATETIME_COL, UPDATED_DATETIME_COL, UPDATED_DATETIME_COL,
          ADDRESS_COL, ADDRESS_COL, LATITUDE_COL, LATITUDE_COL, LONGITUDE_COL,
          LONGITUDE_COL, MEDIA_URL_COL, MEDIA_URL_COL).toLowerCase();
  private static final String SQL_GET_ALL_GRAFFITI = String.format(
      "SELECT %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s FROM %s",
      SERVICE_REQUEST_ID_COL, ID_COL, STATUS_COL, STATUS_NOTES_COL,
      REQUESTED_DATETIME_COL, UPDATED_DATETIME_COL, ADDRESS_COL, LATITUDE_COL,
      LONGITUDE_COL, MEDIA_URL_COL, SYSTEM_CREATED_TIMESTAMP_COL,
      SYSTEM_UPDATED_TIMESTAMP_COL, CHICAGO_CITY_SERVICE_GRAFFITI_TABLE)
      .toLowerCase();

  private RowMapper<ChicagoCityServiceGraffiti> chicagoCityServiceGraffitiRowMapper = new RowMapper<ChicagoCityServiceGraffiti>() {
    @Override
    public ChicagoCityServiceGraffiti mapRow(ResultSet rs, int rowNum)
        throws SQLException {
      ChicagoCityServiceGraffiti chicagoCityServiceGraffiti = new ChicagoCityServiceGraffiti();
      chicagoCityServiceGraffiti.setId(rs.getInt(ID_COL));
      chicagoCityServiceGraffiti.setServiceRequestId(rs
          .getString(SERVICE_REQUEST_ID_COL));
      chicagoCityServiceGraffiti.setStatus(ChicagoCityServiceStatusEnum
          .getChicagoCityServiceStatusEnumFromDbOrServerString(rs
              .getString(STATUS_COL)));
      chicagoCityServiceGraffiti.setStatusNotes(rs.getString(STATUS_NOTES_COL));
      chicagoCityServiceGraffiti.setRequestedDateTime(rs
          .getTimestamp(REQUESTED_DATETIME_COL));
      chicagoCityServiceGraffiti.setUpdatedDateTime(rs
          .getTimestamp(UPDATED_DATETIME_COL));
      chicagoCityServiceGraffiti.setAddress(rs.getString(ADDRESS_COL));
      chicagoCityServiceGraffiti.setLatitude(rs.getFloat(LATITUDE_COL));
      chicagoCityServiceGraffiti.setLongitude(rs.getFloat(LONGITUDE_COL));
      chicagoCityServiceGraffiti.setMediaUrl(rs.getString(MEDIA_URL_COL));
      chicagoCityServiceGraffiti.setSystemCreatedTimestamp(rs
          .getTimestamp(SYSTEM_CREATED_TIMESTAMP_COL));
      chicagoCityServiceGraffiti.setUpdatedDateTime(rs
          .getTimestamp(SYSTEM_UPDATED_TIMESTAMP_COL));
      return chicagoCityServiceGraffiti;
    }
  };

  /**
   * Sets the ds.
   * 
   * @param dataSource
   *          the new ds
   */
  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.ChicagoCityServicesGraffitiDao#
   * storeChicagoCityServicesGraffiti
   * (net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti)
   */
  @Override
  public void storeChicagoCityServicesGraffiti(
      ChicagoCityServiceGraffiti graffiti) {
    Map<String, Object> graffitiParamMap = new HashMap<String, Object>(9);
    graffitiParamMap
        .put(SERVICE_REQUEST_ID_COL, graffiti.getServiceRequestId());
    graffitiParamMap.put(STATUS_COL, graffiti.getStatus().getDbString());
    graffitiParamMap.put(STATUS_NOTES_COL, graffiti.getStatusNotes());
    graffitiParamMap.put(REQUESTED_DATETIME_COL,
        graffiti.getRequestedDateTime());
    graffitiParamMap.put(UPDATED_DATETIME_COL, graffiti.getUpdatedDateTime());
    graffitiParamMap.put(ADDRESS_COL, graffiti.getAddress());
    graffitiParamMap.put(LATITUDE_COL, graffiti.getLatitude());
    graffitiParamMap.put(LONGITUDE_COL, graffiti.getLongitude());
    graffitiParamMap.put(MEDIA_URL_COL, graffiti.getMediaUrl());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_GRAFFITI,
        graffitiParamMap);
  }

  // TODO(ccaper): javadoc
  @Override
  public List<ChicagoCityServiceGraffiti> getAllChicagoCityServicesGraffiti(
      List<String> status, Timestamp startDate, Timestamp endDate) {
    String sqlWithWhere = SQL_GET_ALL_GRAFFITI;
    sqlWithWhere += String.format(" WHERE DATE(%s) BETWEEN DATE('%s') AND DATE('%s')", REQUESTED_DATETIME_COL, startDate, endDate);
    if (status.size() > 0) {
      sqlWithWhere += String.format(" AND %s IN (", STATUS_COL);
      sqlWithWhere += StringUtils.join(status, ", ");
      sqlWithWhere += ")";
    }
    sqlWithWhere += " LIMIT 100";
    sqlWithWhere = sqlWithWhere.toLowerCase();
    return getNamedParameterJdbcTemplate().query(sqlWithWhere,
        chicagoCityServiceGraffitiRowMapper);
  }
}
