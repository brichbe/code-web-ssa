package com.codeweb.ssa.util;

import java.util.Collection;

import com.codeweb.ssa.model.ProjectPackage;
import com.codeweb.ssa.model.ProjectSrcFile;
import com.codeweb.ssa.model.ProjectStructure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Printer
{
  public static void print(ProjectStructure projStructure, boolean asJson)
  {
    if (asJson)
    {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String json = gson.toJson(projStructure);
      System.out.println(json);
    }
    else
    {
      System.out.println("Proj Structure for: " + projStructure.getProjName());
      print(projStructure.getTopPackages());
    }
  }

  static void print(Collection<ProjectPackage> pkgs)
  {
    for (ProjectPackage pkg : pkgs)
    {
      System.out.println(pkg.getName());
      if (!pkg.getSrcFiles().isEmpty())
      {
        for (ProjectSrcFile src : pkg.getSrcFiles())
        {
          System.out.println("\t" + src.getName() + "  (" + src.getSlocCount() + ")");
        }
      }
      print(pkg.getSubPackages());
    }
  }
}
