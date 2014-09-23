package net.ccaper.graffitiTracker.dao;

import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

/**
 * @author ccaper
 * 
 *         The Interface ChicagoCityServicesServerDao.
 */
public interface ChicagoCityServicesServerDao {
  // TODO(ccaper): javadoc
  List<ChicagoCityServiceGraffiti> getGraffiti(Date startDate, Date endDate,
      int page);
}
