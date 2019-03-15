package com.tsystems.oop.dbutil;

import java.sql.*;
import java.util.*;

public class DbUtil
{
  private static DbUtil instance;
  private Connection con=null;

  private DbUtil()
  {
    init();
  }

  public static DbUtil getInstance()
  {
    if (instance == null)
    {
      instance=new DbUtil();
    }
    return instance;
  }

  public void destroyDb()
  {
    try
    {
      con.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  private List<Map<String, String>> data;

  public void init()
  {
    final String initSQL=
        "CREATE TABLE DEBTORS(id INT PRIMARY KEY auto_increment, name VARCHAR(150), lastname VARCHAR(150), email VARCHAR(150), phone VARCHAR"
            + "(150), loandate VARCHAR(150), debt VARCHAR(150), address VARCHAR(150), prio INT);";

    try
    {
      con=DriverManager.getConnection("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1");
      Statement stmt=con.createStatement();
      stmt.executeUpdate(initSQL);
      stmt.close();
      con.commit();

    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }

  }

  public List<Map<String, String>> getAll()
  {
    String SelectQuery="select * from DEBTORS";
    PreparedStatement selectPreparedStatement=null;
    List<Map<String, String>> data=new LinkedList<>();
    try
    {
      selectPreparedStatement=con.prepareStatement(SelectQuery);
      ResultSet rs=selectPreparedStatement.executeQuery();
      while (rs.next())
      {
        Map<String, String> line=new TreeMap<>();
        line.put("id", String.valueOf(rs.getInt("id")));
        line.put("name", rs.getString("name"));
        line.put("last_name", rs.getString("lastname"));
        line.put("phone", rs.getString("phone"));
        line.put("debt", rs.getString("debt"));
        line.put("email", rs.getString("email"));
        line.put("loandate", rs.getString("loandate"));
        line.put("prio", rs.getString("prio"));
        data.add(line);
      }
      selectPreparedStatement.close();
      con.commit();
      return data;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }

    return data;
  }

  public void deleteById(int id)
  {
    try
    {
      Statement stmt=con.createStatement();
      stmt.executeUpdate("DELETE FROM DEBTORS WHERE id = " + id + ";");
      con.commit();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  public void store(List<Map<String, String>> data)
  {
    {
      PreparedStatement insertPreparedStatement=null;
      try
      {

        for (Map<String, String> map : data)
        {
          insertPreparedStatement=con.prepareStatement(prepareStatement());
          insertPreparedStatement.setString(1, map.get("first_name"));
          insertPreparedStatement.setString(2, map.get("last_name"));
          insertPreparedStatement.setString(3, map.get("email"));
          insertPreparedStatement.setString(4, map.get("phone"));
          insertPreparedStatement.setString(5, map.get("loan_date"));
          insertPreparedStatement.setString(6, map.get("debt"));
          insertPreparedStatement.setString(7, map.get("address"));
          insertPreparedStatement.setString(8, map.get("prio"));
          insertPreparedStatement.executeUpdate();
          insertPreparedStatement.close();
        }
        con.commit();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }

    }
  }

  public String prepareStatement()
  {
    StringBuilder stringBuilder=new StringBuilder();
    stringBuilder.append("INSERT INTO DEBTORS");
    stringBuilder.append("(name, lastname, email, phone, loandate, debt, address, prio) values");
    stringBuilder.append("(?,?,?,?,?,?,?,?)");
    return stringBuilder.toString();
  }

}
