package com.iaditya.testng.sqliteprovider;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
  @Test(dataProvider="sqliteDataProvider")
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
  @Test(dataProvider="sqliteDataProvider")
  public void testCase002(String a, String b) {
	  Assert.assertEquals(a, "testData_002_1");
	  Assert.assertEquals(b, "testData_002_2");
  }
  

  /**
   * Before suite, create sqlite database instance and insert data
   */
  @BeforeSuite
  private void initSqlite() {
		    Connection connection = null;
            Statement statement = null;
            String createTableSql = "CREATE TABLE IF NOT EXISTS testdata ("
                    + "	TestCaseName text PRIMARY KEY,"
                    + "	TestMethod text NOT NULL,"
                    + "	data1 text,"
                    + "	data2 text"
                    + ")";    
            String url = "jdbc:sqlite:src/test/resources/testdata.db";
            try {
				connection = DriverManager.getConnection(url);
	            System.out.println("Connection to SQLite has been established.");
				statement = connection.createStatement();
				statement.execute(createTableSql);
				statement.executeUpdate("delete from testdata;");
				statement.executeUpdate("INSERT INTO testdata VALUES ('TEST_CASE_001', 'testCase001', 'testData_001_1', 'testData_001_2' );");
				statement.executeUpdate("INSERT INTO testdata VALUES ('TEST_CASE_002', 'testCase002', 'testData_002_1', 'testData_002_2' );");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	            System.out.println("Connection to SQLite has NOT been established.");
			} finally {
				try {
				    statement.close();
				    connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
  }

  /**
   * After test suite, stop sqlite
   */
  @AfterSuite
  private void stopSqlite(){

  }

  /**
   * Dataprovider for reading data from sqlite database
   * 
   * @param method
   * @return object array with test data
   */
  @DataProvider(name="sqliteDataProvider")
  private Object[][] getData(Method method) {
	    Object[][] data = null;
	    Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        String url = "jdbc:sqlite:src/test/resources/testdata.db";
        try {
			connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from testdata where TestMethod = '" + method.getName() + "'");
			resultSet.next();
			data = new Object[][] {{ resultSet.getString("data1"), resultSet.getString("data2") }};
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            System.out.println("Connection to SQLite has NOT been established.");
		} finally {
			try {
				resultSet.close();
			    statement.close();
			    connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
	  return data;
  }
  
}
  
