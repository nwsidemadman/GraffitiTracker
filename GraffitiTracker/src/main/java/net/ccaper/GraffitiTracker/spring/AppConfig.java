package net.ccaper.GraffitiTracker.spring;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.UserDao;
import net.ccaper.GraffitiTracker.service.UserService;
import net.ccaper.GraffitiTracker.serviceImpl.UserServiceImpl;
import net.ccaper.net.dao.impl.JdbcUserDaoImpl;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

@Configuration
@PropertySource("classpath:/net/ccaper/GraffitiTracker/${CLASSPATH_PROP_ENV:local}/GraffitiTracker.properties")
public class AppConfig {
  @Autowired
  private Environment properties;
  
  @Bean
  public ShaPasswordEncoder passwordEncoder() {
    return new ShaPasswordEncoder(256);
  }

  @Bean(destroyMethod = "close")
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(properties.getProperty("db.driverClassName"));
    dataSource.setUrl(properties.getProperty("db.url"));
    dataSource.setUsername(properties.getProperty("db.user"));
    dataSource.setPassword(properties.getProperty("db.password"));
    dataSource.setInitialSize(Integer.parseInt(properties
        .getProperty("db.pool.initialSize")));
    dataSource.setMaxActive(Integer.parseInt(properties
        .getProperty("db.pool.maxActive")));
    return dataSource;
  }
  
  @Bean
  public UserDao userDao() {
    JdbcUserDaoImpl userDao = new JdbcUserDaoImpl();
    userDao.setDataSource(dataSource());
    return userDao;
  }
  
  @Bean
  public UserService userService() {
    return new UserServiceImpl();
  }
}
