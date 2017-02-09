package com.codeweb.ssa.model;

public class ProjectSrcFile
{
  private String className;
  private String fileExt;
  private int slocCount;

  public ProjectSrcFile(String name, int sloc)
  {
    int sep = name.lastIndexOf('.');
    this.className = name.substring(0, sep);
    this.fileExt = name.substring(sep + 1);
    this.slocCount = sloc;
  }

  public String getFileExt()
  {
    return fileExt;
  }

  public String getClassName()
  {
    return className;
  }

  public int getSlocCount()
  {
    return slocCount;
  }
}
