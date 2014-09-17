package net.ccaper.graffitiTracker.dao;

import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

public interface ChicagoCityServiceServerDao {
  List<ChicagoCityServiceGraffiti> getGraffiti();
}
