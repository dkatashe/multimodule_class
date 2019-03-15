package com.tsystems.oop.debtcollector;

import java.util.Scanner;

public class ConsoleUi
{
  private Scanner sc;

  public ConsoleUi()
  {
    greetUser();
    sc=new Scanner(System.in);
  }

  private String[] getCommandFromConsole()
  {
    String command=sc.nextLine();

    return command.split(" ");
  }

  public Command getNextCommand()
  {
    Command command=null;
    System.out.println("Please enter a command:");
    String[] input=getCommandFromConsole();

    try
    {
      if (input.length > 0)
      {
        switch (input[0].toLowerCase())
        {
          case "load":
            if (input.length == 2)
            {
              command=new Command(input[0].toLowerCase(), input[1]);
            }
            else
            {
              throw new CommandParseException("Argument \"filepath\" is empty or invalid");
            }
            break;
          case "startcall":
          case "getall":
          case "exit":
            if (input.length == 1)
            {
              command=new Command(input[0].toLowerCase());
            }
            else
            {
              throw new CommandParseException("No arguments are acceptable for the command \"" + input[0] + "\"");
            }
            break;
          default:
            throw new CommandParseException("Unknown command \"" + input[0] + "\"");
        }
      }
      else
      {
        throw new CommandParseException("No command entered");
      }
    }
    catch (CommandParseException e)
    {
      System.err.println("Error: " + e.getMessage());
    }

    return command;
  }

  private void greetUser()
  {
    System.out.println("Hello, Program DebtCollector is running");
    System.out.println("Available commands: ");
    System.out.println("load somefile.csv - to load file with name somefile.csv");
    System.out.println("startCall         - to start to call debtors");
    System.out.println("getAll            - to get all debtors");
    System.out.println("exit              - to exit");
  }

  public class Command
  {
    CommandType type;
    String arg;

    Command(String com)
    {
      type=determineType(com);
    }

    Command(String com, String arg)
    {
      type=determineType(com);
      this.arg=arg;
    }

    private CommandType determineType(String type)
    {
      switch (type)
      {
        case "load":
          return CommandType.LOAD;
        case "startcall":
          return CommandType.START_CALL;
        case "getall":
          return CommandType.GET_ALL;
        case "exit":
          return CommandType.EXIT;
        default:
          return null;
      }
    }
  }

  public enum CommandType
  {
    LOAD,
    START_CALL,
    GET_ALL,
    EXIT
  }

  public class CommandParseException extends Exception
  {
    public CommandParseException(String message)
    {
      super(message);
    }
  }
}
