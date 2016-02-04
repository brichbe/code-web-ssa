package com.codeweb.ssa.util;

import java.io.File;
import java.io.FileFilter;

public final class PackageFileFilter implements FileFilter
{
  @Override
  public boolean accept(File pathname)
  {
    return (pathname != null && pathname.isDirectory());
  }
}