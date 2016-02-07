package com.codeweb.ssa.util;

import java.util.Collection;

import com.codeweb.ssa.model.ProjectPackage;
import com.codeweb.ssa.model.ProjectSrcFile;
import com.codeweb.ssa.model.ProjectStructure;

public class Printer
{
  public static void print(ProjectStructure projStructure)
  {
    System.out.println("Proj Structure for: " + projStructure.getProjName());
    print(projStructure.getTopPackages());
  }

  public static void print(Collection<ProjectPackage> pkgs)
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
