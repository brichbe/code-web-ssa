package com.codeweb.ssa.model;

import java.util.ArrayList;
import java.util.Collection;

public class ProjectPackage
{
  private String name;
  private final Collection<ProjectPackage> subPackages = new ArrayList<>();
  private final Collection<ProjectSrcFile> srcFiles = new ArrayList<>();

  public ProjectPackage(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void addSubPackage(ProjectPackage pkg)
  {
    this.subPackages.add(pkg);
  }

  public Collection<ProjectPackage> getSubPackages()
  {
    return subPackages;
  }

  public void addSrcFile(ProjectSrcFile src)
  {
    this.srcFiles.add(src);
  }

  public Collection<ProjectSrcFile> getSrcFiles()
  {
    return srcFiles;
  }
}
