package com.tsystems.oop.autocall;

import java.util.Random;

public class Caller
{
  Random rand;
  int probabilityOfLoss = 30;

  public Caller()
  {
    this.rand=new Random();
  }

  public boolean makeCallTo(String name, String lastname, String phone, String debt)
  {
    System.out.println("Calling to " + phone + " ... ... ...");
    if (rand.nextInt(100) > probabilityOfLoss)
    {
      System.out.println("Hey you, " + name + " " + lastname + ", you owe an " + debt + " bucks to us");
      return true;
    }
    return false;
  }
}
