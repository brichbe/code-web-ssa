package com.codeweb.ssa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

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
  private static final SourceFilenameFilter JAVA_SOURCE_FILTER = new SourceFilenameFilter(".java");
  private static final String EXTRACT_DIR = "temp";

  private ProjectStructure projStructure;

  public static void main(String[] args) throws IOException
  {
    SSA ssa = null;
    if (args.length == 1)
    {
      ssa = fromArchive(args[0]);
    }
    else if (args.length == 2)
    {
      ssa = fromSrcTopDir(args[0], args[1]);
    }

    if (ssa == null)
    {
      printUsageAndExit();
    }

    ssa.printProject();
    ssa.writeProjectFile();
  }

  private static void printUsageAndExit()
  {
    System.out.println("Usage:");
    System.out.println("SSA <path to ZIP/JAR file>");
    System.out.println("- or -");
    System.out.println("SSA <project name> <path to project source root>");
    System.exit(1);
  }

  public static SSA fromSrcTopDir(String projName, String srcTopDir)
  {
    File dir = new File(srcTopDir);
    if (dir.exists() && dir.isDirectory())
    {
      return new SSA(projName, srcTopDir);
    }
    return null;
  }

  public static SSA fromArchive(String srcArchiveFilePath)
  {
    File srcArchiveFile = new File(srcArchiveFilePath);
    if (srcArchiveFile.exists() && srcArchiveFile.isFile())
    {
      try
      {
        ZipFile zipFile = new ZipFile(srcArchiveFile);
        zipFile.extractAll(EXTRACT_DIR);
        String filename = srcArchiveFile.getName();
        String projName = filename.substring(0, filename.lastIndexOf("."));
        return new SSA(projName, EXTRACT_DIR);
      }
      catch (ZipException e)
      {
        e.printStackTrace();
      }
      finally
      {
        FileIO.deleteDir(new File(EXTRACT_DIR));
      }
    }
    return null;
  }

  protected SSA(String projName, String srcTopDir)
  {
    this.projStructure = createProject(projName, srcTopDir);
  }

  public ProjectStructure getProjStructure()
  {
    return projStructure;
  }

  public void printProject()
  {
    Printer.printJson(projStructure);
  }

  public void writeProjectFile() throws IOException
  {
    FileIO.writeJson(projStructure, projStructure.getProjName(), System.currentTimeMillis(), ".ssa");
  }

  public static ProjectStructure createProject(String projName, String srcTopDir)
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
      File[] dirFiles = file.listFiles(JAVA_SOURCE_FILTER);
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
