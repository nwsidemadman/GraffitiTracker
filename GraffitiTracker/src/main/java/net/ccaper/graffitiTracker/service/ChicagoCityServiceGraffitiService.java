package net.ccaper.graffitiTracker.service;

import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

public interface ChicagoCityServiceGraffitiService {
  List<ChicagoCityServiceGraffiti> getChicagoCityServiceFromServer();
}
