package com.codeweb.ssa.model;

import java.util.ArrayList;
import java.util.Collection;

public class ProjectStructure
{
  private String projName;
  private final Collection<ProjectPackage> topPackages = new ArrayList<>();

  public ProjectStructure(String name)
  {
    this.projName = name;
  }

  public String getProjName()
  {
    return projName;
  }

  public void addTopPackage(ProjectPackage pkg)
  {
    if (pkg != null)
    {
      this.topPackages.add(pkg);
    }
  }

  public Collection<ProjectPackage> getTopPackages()
  {
    return topPackages;
  }
}
