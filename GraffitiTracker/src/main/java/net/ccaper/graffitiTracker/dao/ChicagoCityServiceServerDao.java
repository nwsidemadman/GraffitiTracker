package net.ccaper.graffitiTracker.dao;

import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

public interface ChicagoCityServiceServerDao {
  List<ChicagoCityServiceGraffiti> getGraffiti(Date startDate, Date endDate);
}
