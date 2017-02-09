package com.codeweb.ssa.util;

import java.io.File;
import java.io.FilenameFilter;

public final class SourceFilenameFilter implements FilenameFilter
{
  private final String fileExt;

  public SourceFilenameFilter(String ext)
  {
    this.fileExt = ext;
  }

  @Override
  public boolean accept(File dir, String name)
  {
    return name.toLowerCase().endsWith(fileExt);
  }
}