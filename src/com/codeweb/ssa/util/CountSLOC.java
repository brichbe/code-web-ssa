package com.codeweb.ssa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CountSLOC
{
  public static int getLines(File f) throws IOException
  {
    int count = 0;
    if (f.isFile() && f.canRead())
    {
      try (BufferedReader br = new BufferedReader(new FileReader(f)))
      {
        String line;
        while ((line = br.readLine()) != null)
        {
          line = line.replaceAll("\\n|\\t|\\s", "").trim();
          if (!line.isEmpty() && !line.startsWith("/") && !line.startsWith("*"))
          {
            count++;
          }
        }
      }
    }
    return count;
  }
}
