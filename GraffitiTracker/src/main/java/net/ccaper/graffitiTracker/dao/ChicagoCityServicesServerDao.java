package net.ccaper.graffitiTracker.dao;

import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

public interface ChicagoCityServicesServerDao {
  List<ChicagoCityServiceGraffiti> getGraffiti(Date startDate, Date endDate);
}
