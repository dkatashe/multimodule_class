package com.tsystems.oop.debtcollector;

import java.util.Scanner;

public class ConsoleUi
{
  Scanner sc;
  public ConsoleUi()
  {
    sc=new Scanner(System.in);
  }

    private String getCommandFromConsole()
  {
    String command=sc.nextLine();
    return command;
  }

  public void greetUser()
  {
    System.out.println("Hello, Program DebtCollector is running");
    System.out.println("Available commands: ");
    System.out.println("load somefile.csv - to load file with name somefile.csv");
    System.out.println("startCall         - to start to call debtors");
    System.out.println("getAll            - to get all debtors");
    System.out.println("exit              - to exit");
  }
}
