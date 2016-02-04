package com.codeweb.ssa;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.codeweb.ssa.model.ProjectPackage;
import com.codeweb.ssa.model.ProjectSrcFile;
import com.codeweb.ssa.model.ProjectStructure;
import com.codeweb.ssa.util.CountSLOC;

public class SSA
{
  public static void main(String[] args)
  {
    String projName = "Sandbox";
    String srcTopDir = "C:\\dev\\projects\\Sandbox\\src";
    if (args.length > 0)
    {
      projName = args[0];
    }
    if (args.length > 1)
    {
      srcTopDir = args[1];
    }

    ProjectStructure projStructure = createProject(projName, srcTopDir);

    System.out.println("Proj Structure for: " + projStructure.getProjName());
    print(projStructure.getTopPackages());
  }

  private static ProjectStructure createProject(String projName, String srcTopDir)
  {
    ProjectStructure projStructure = new ProjectStructure(projName);
    Collection<ProjectPackage> projPackages = buildPackageStructure(null, srcTopDir);
    for (ProjectPackage pkg : projPackages)
    {
      projStructure.addTopPackage(pkg);
    }
    return projStructure;
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

  static Collection<ProjectPackage> buildPackageStructure(String parentPkg, String dirPath)
  {
    Collection<ProjectPackage> packages = new ArrayList<>();

    File file = new File(dirPath);
    if (file.exists() && file.canRead())
    {
      File[] dirFiles = file.listFiles(new FilenameFilter()
      {
        @Override
        public boolean accept(File dir, String name)
        {
          return name.toLowerCase().endsWith(".java");
        }
      });
      File[] subDirs = file.listFiles(new FileFilter()
      {
        @Override
        public boolean accept(File pathname)
        {
          return (pathname != null && pathname.isDirectory());
        }
      });

      if ((dirFiles != null && dirFiles.length > 0) || (subDirs != null && subDirs.length > 0))
      {
        String pkgName = (parentPkg != null ? parentPkg + "." : "") + file.getName();
        ProjectPackage curPackage = new ProjectPackage(pkgName);
        packages.add(curPackage);

        if (dirFiles != null)
        {
          for (File dirFile : dirFiles)
          {
            
            try
            {
              ProjectSrcFile srcFile = new ProjectSrcFile(dirFile.getName(), CountSLOC.getLines(dirFile));
              curPackage.addSrcFile(srcFile);
            }
            catch (IOException e)
            {
              e.printStackTrace();
            }
          }
        }

        if (subDirs != null)
        {
          for (File subDir : subDirs)
          {
            Collection<ProjectPackage> subPkgs = buildPackageStructure(curPackage.getName(), subDir.getAbsolutePath());
            if (!subPkgs.isEmpty())
            {
              for (ProjectPackage pkg : subPkgs)
              {
                curPackage.addSubPackage(pkg);
              }
            }
          }
        }
      }
    }

    return packages;
  }

}
