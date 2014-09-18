package net.ccaper.graffitiTracker.service;

import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

public interface ChicagoCityServicesGraffitiService {
  List<ChicagoCityServiceGraffiti> getChicagoCityServiceDataFromServer(Date startDate, Date endDate);
  
  void storeChicagoCityServiceData(List<ChicagoCityServiceGraffiti> data);
}
