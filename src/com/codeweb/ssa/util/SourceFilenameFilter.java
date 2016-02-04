package com.codeweb.ssa.util;

import java.io.File;
import java.io.FilenameFilter;

public final class SourceFilenameFilter implements FilenameFilter
{
  @Override
  public boolean accept(File dir, String name)
  {
    return name.toLowerCase().endsWith(".java");
  }
}