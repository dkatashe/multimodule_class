package com.tsystems.oop.debtcollector;

import com.tsystems.oop.autocall.Caller;
import com.tsystems.oop.csvutil.CsvLoader;
import com.tsystems.oop.dbutil.DbUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application
{
  ConsoleUi ui;
  CsvLoader loader;
  DbUtil dbUtil=DbUtil.getInstance();
  Caller caller;

  public static void main(String[] args)
  {
    Application app=new Application();
    app.initApp();
    app.run();
  }

  private void initApp()
  {
    ui=new ConsoleUi();
  }

  private void run()
  {
    boolean isRunning=true;

    while (isRunning)
    {
      ConsoleUi.Command com=ui.getNextCommand();
      if (com != null)
      {
        // Temporary code below
        switch (com.type)
        {
          case LOAD:
            System.out.print(com.type);
            System.out.println(" " + com.arg);
            break;
          case GET_ALL:
          case START_CALL:
            System.out.println(com.type);
            break;
          case EXIT:
            isRunning=false;
            break;
        }
      }
    }
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

  // a lot of your code

}
