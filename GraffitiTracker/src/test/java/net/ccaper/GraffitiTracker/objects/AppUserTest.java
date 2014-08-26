package net.ccaper.GraffitiTracker.objects;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.enums.RoleEnum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppUserTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetRolesAsTimestampToRoleEnumMap_nullRoles() {
    AppUser user = new AppUser();
    assertNull(user.getRolesAsTimestampToRoleEnumMap());
  }
  
  @Test
  public void testGetRolesAsTimestampToRoleEnumMap_empty() {
    AppUser user = new AppUser();
    user.setRoles(new ArrayList<Role>(0));
    assertEquals(new HashMap<RoleEnum, Timestamp>(0), user.getRolesAsTimestampToRoleEnumMap());
  }
  
  @Test
  public void testGetRolesAsTimestampToRoleEnumMap_happyPath() {
    AppUser user = new AppUser();
    List<Role> roles = new ArrayList<Role>(2);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    role1.setGrantedTimestamp(new Timestamp(1L));
    roles.add(role1);
    Role role2 = new Role();
    role2.setRole(RoleEnum.ADMIN);
    role2.setGrantedTimestamp(new Timestamp(2L));
    roles.add(role2);
    user.setRoles(roles);
    Map<RoleEnum, Timestamp> rolesMap = new HashMap<RoleEnum, Timestamp>(
        roles.size());
    for (Role role : roles) {
      rolesMap.put(role.getRole(), role.getGrantedTimestamp());
    }
    assertEquals(rolesMap, user.getRolesAsTimestampToRoleEnumMap());
  }
}
