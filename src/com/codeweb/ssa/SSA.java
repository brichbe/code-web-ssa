package com.codeweb.ssa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.codeweb.ssa.model.ProjectPackage;
import com.codeweb.ssa.model.ProjectSrcFile;
import com.codeweb.ssa.model.ProjectStructure;
import com.codeweb.ssa.util.CountSLOC;
import com.codeweb.ssa.util.FileIO;
import com.codeweb.ssa.util.PackageFileFilter;
import com.codeweb.ssa.util.Printer;
import com.codeweb.ssa.util.SourceFilenameFilter;

public class SSA
{
  private static final PackageFileFilter PACKAGE_FILTER = new PackageFileFilter();
  private static final SourceFilenameFilter SOURCE_FILTER = new SourceFilenameFilter();

  public static void main(String[] args) throws IOException
  {
    String projName = "CodeWeb SSA";
    String srcTopDir = "C:\\dev\\checkout\\CodeWeb-SSA\\src";
    if (args.length > 0)
    {
      projName = args[0];
    }
    if (args.length > 1)
    {
      srcTopDir = args[1];
    }

    ProjectStructure projStructure = createProject(projName, srcTopDir);
    Printer.print(projStructure);

    FileIO.write(projStructure);
  }

  private static ProjectStructure createProject(String projName, String srcTopDir)
  {
    ProjectStructure projStructure = new ProjectStructure(projName);
    Collection<ProjectPackage> projPackages = buildPackageStructure(srcTopDir);
    for (ProjectPackage pkg : projPackages)
    {
      projStructure.addTopPackage(pkg);
    }
    return projStructure;
  }

  static Collection<ProjectPackage> buildPackageStructure(String srcTopDir)
  {
    Collection<ProjectPackage> topPackages = new ArrayList<>();
    File file = new File(srcTopDir);
    if (file.exists() && file.canRead())
    {
      File[] subDirs = file.listFiles(PACKAGE_FILTER);
      if (subDirs != null && subDirs.length > 0)
      {
        for (File f : subDirs)
        {
          topPackages.addAll(buildPackageStructure(null, f.getAbsolutePath()));
        }
      }
    }
    return topPackages;
  }

  static Collection<ProjectPackage> buildPackageStructure(ProjectPackage parentPkg, String dirPath)
  {
    Collection<ProjectPackage> packages = new ArrayList<>();

    File file = new File(dirPath);
    if (file.exists() && file.canRead())
    {
      File[] dirFiles = file.listFiles(SOURCE_FILTER);
      File[] subDirs = file.listFiles(PACKAGE_FILTER);
      if ((dirFiles != null && dirFiles.length > 0) || (subDirs != null && subDirs.length > 0))
      {
        String pkgName = (parentPkg != null ? parentPkg.getName() + "." : "") + file.getName();
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
            Collection<ProjectPackage> subPkgs = buildPackageStructure(curPackage, subDir.getAbsolutePath());
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
