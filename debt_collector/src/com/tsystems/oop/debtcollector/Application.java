package com.tsystems.oop.debtcollector;

import com.tsystems.oop.autocall.Caller;
import com.tsystems.oop.csvutil.CsvLoader;
import com.tsystems.oop.dbutil.DbUtil;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Application
{
  private ConsoleUi ui;
  private CsvLoader loader;
  private DbUtil dbUtil=DbUtil.getInstance();
  private Caller caller;

  public static void main(String[] args)
  {
    Application app=new Application();
    app.initApp();
    app.run();
  }

  private void initApp()
  {
    ui=new ConsoleUi();
    caller=new Caller();
    loader=path -> {
      List<Map<String, String>> list=new LinkedList<>();

      try (BufferedReader br=new BufferedReader(new FileReader(path)))
      {
        Map<String, String> debt;
        String line;
        String[] headers=null;

        while ((line=br.readLine()) != null)
        {
          String[] ar=line.split(",");

          if (headers == null)
          {
            headers=ar;
            continue;
          }

          debt=new HashMap<>();
          for (int i=0; i < ar.length; i++)
          {
            debt.put(headers[i], ar[i]);
          }
          list.add(debt);
        }
      }
      catch (IOException e)
      {
        System.err.println("Error: " + e.getMessage());
      }

      return list;
    };
  }

  private void run()
  {
    boolean isRunning=true;

    while (isRunning)
    {
      ConsoleUi.Command com=ui.getNextCommand();
      if (com != null)
      {
        switch (com.type)
        {
          case LOAD:
            load(com.arg);
            break;
          case GET_ALL:
            getAll();
            break;
          case START_CALL:
            startCall();
            break;
          case EXIT:
            dbUtil.destroyDb();
            isRunning=false;
            break;
        }
      }
    }
  }

  private void load(String path)
  {
    List<Map<String, String>> bankData=loader.readFile(path);
    bankData.forEach(e -> e.put("prio", "1"));
    dbUtil.store(bankData);
    System.out.println(bankData.size() + " records loaded");
  }

  private void getAll()
  {
    List<Map<String, String>> dataFromDb=dbUtil.getAll();
    dataFromDb.forEach(debtor -> System.out.println(debtor.get("name") + " " + debtor.get("last_name") + ", email: " + debtor.get("email") +
        ", phone: " + debtor.get("phone") + ", debt: " + debtor.get("debt") + ", loan date: " + debtor.get("loandate") +
        ", prio: " + debtor.get("prio")));
  }

  private void startCall()
  {
    List<Map<String, String>> dataFromDb=dbUtil.getAll();
    dataFromDb.forEach(debtor -> {
      String name=debtor.get("name");
      String lastname=debtor.get("last_name");
      String debt=debtor.get("debt");
      String phone=debtor.get("phone");
      if (caller.makeCallTo(name, lastname, phone, debt))
      {
        debtor.replace("debt", "0");
      }
    });

    dataFromDb.forEach(e -> {
      if (e.get("debt").equals("0"))
      {
        dbUtil.deleteById(Integer.parseInt(e.get("id")));
      }
    });
  }
}
