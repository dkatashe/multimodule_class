package com.tsystems.oop.csvutil;

import java.util.*;

public interface CsvLoader
{

  List<Map<String, String>> readFile(String path);

}
