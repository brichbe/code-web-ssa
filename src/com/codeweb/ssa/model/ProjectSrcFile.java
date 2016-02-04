package com.codeweb.ssa.model;

public class ProjectSrcFile
{
  private String name;
  private int slocCount;

  public ProjectSrcFile(String name, int sloc)
  {
    this.name = name;
    this.slocCount = sloc;
  }

  public String getName()
  {
    return name;
  }
  
  public int getSlocCount()
  {
    return slocCount;
  }
}
