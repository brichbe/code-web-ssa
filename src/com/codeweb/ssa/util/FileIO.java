package com.codeweb.ssa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.codeweb.ssa.model.ProjectStructure;
import com.google.gson.Gson;

public class FileIO
{
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

  public static void write(ProjectStructure projStructure) throws IOException
  {
    Gson gson = new Gson();
    String json = gson.toJson(projStructure);

    String projName = projStructure.getProjName().replaceAll("[^a-zA-Z0-9.-]", "_");
    String dtgStr = dateFormat.format(new Date());
    File f = new File(projName + "_" + dtgStr + ".ssa");
    
    FileWriter writer = new FileWriter(f);
    writer.write(json);
    writer.close();
  }

  public static ProjectStructure read(File file) throws IOException
  {
    Gson gson = new Gson();
    try (BufferedReader br = new BufferedReader(new FileReader(file)))
    {
      return gson.fromJson(br, ProjectStructure.class);
    }
  }
}
