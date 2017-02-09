package com.codeweb.ssa.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Printer
{
  public static void printJson(Object object)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(object);
    System.out.println(json);
  }
}
