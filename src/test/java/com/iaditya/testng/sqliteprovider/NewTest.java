package com.iaditya.testng.sqliteprovider;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import redis.clients.jedis.Jedis;

/**
 * Sample test class to demonstrate use of sqlite as the data source of the test data.
 * 
 * @author adityai
 *
 */
public class NewTest {


	/**
	 * Sample test case that pulls test data from sqlite database
	 * 
	 * @param a
	 * @param b
	 */
  @Test(dataProvider="redisDataProvider")
  public void testCase001(String a, String b) {
	  Assert.assertEquals(a, "testData_001_1");
	  Assert.assertEquals(b, "testData_001_2");
  }

  /**
   * Sample test case that pulls test data from sqlite database
   * 
   * @param a
   * @param b
   */
  @Test(dataProvider="redisDataProvider")
  public void testCase002(String a, String b) {
	  Assert.assertEquals(a, "testData_002_1");
	  Assert.assertEquals(b, "testData_002_2");
  }
  
  @AfterSuite
  private void cleanUp() {
	  Jedis jedis = new Jedis("0.0.0.0", 6379);
	  jedis.connect();
	  jedis.del("testCase001");
	  jedis.del("testCase002");
	  jedis.close();
  }
  

  /**
   * Before suite, create redis database instance and insert data
   */
  @BeforeSuite
  private void initRedis() {
	  Jedis jedis = new Jedis("0.0.0.0", 6379);
	  jedis.connect();
	  Map<String, String> hash = new HashMap<String, String>();
	  hash.put("key1", "testData_001_1");
	  hash.put("key2", "testData_001_2");
	  jedis.hset("testCase001", hash);
	  
	  hash = new HashMap<String, String>();
	  hash.put("key1", "testData_002_1");
	  hash.put("key2", "testData_002_2");
	  jedis.hset("testCase002", hash);
	  jedis.close();
	  
  }
  
  @DataProvider(name="redisDataProvider")
  private Object[][] getData(Method method) {
	  Object[][] data = null;
	  Jedis jedis = new Jedis("0.0.0.0", 6379);
	  jedis.connect();
	  Map<String, String> testCaseMap = jedis.hgetAll(method.getName());
	  data = new Object[][] {{ testCaseMap.get("key1"), testCaseMap.get("key2") }};
	  jedis.close();
	  return data;
  }
  
}
  
