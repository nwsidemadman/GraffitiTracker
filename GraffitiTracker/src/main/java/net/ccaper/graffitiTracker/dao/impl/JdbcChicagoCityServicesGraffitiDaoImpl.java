package net.ccaper.graffitiTracker.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesGraffitiDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

@Repository("chicagoCityServicesGraffitiDao")
public class JdbcChicagoCityServicesGraffitiDaoImpl extends NamedParameterJdbcDaoSupport implements
    ChicagoCityServicesGraffitiDao {
  private static final String TABLE_NAME = "chicago_city_service_data_graffiti";
  private static final String SERVICE_REQUEST_ID_COL = "service_request_id";
  private static final String ID_COL = "id_col";
  private static final String STATUS_COL = "status";
  private static final String STATUS_NOTES_COL = "status_notes";
  private static final String REQUESTED_DATETIME_COL = "requested_datetime";
  private static final String UPDATED_DATETIME_COL = "updated_datetime";
  private static final String ADDRESS_COL = "address";
  private static final String LATITUDE_COL = "lat";
  private static final String LONGITUDE_COL = "long";
  private static final String MEDIA_URL_COL = "media_url";
  private static final String SYSTEM_CREATED_TIMESTAMP_COL = "system_created_timestamp";
  private static final String SYSTEM_UPDATED_TIMESTAMP_COL = "system_updated_timestamp";
  
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

  @Override
  public void storeChicagoCityServicesGraffiti(
      ChicagoCityServiceGraffiti graffiti) {
    // TODO Auto-generated method stub

  }
}
