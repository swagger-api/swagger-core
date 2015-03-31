package com.wordnik.swagger.config;

import com.wordnik.swagger.config.Scanner;

public class ScannerFactory {
  private static Scanner SCANNER;

  public static void setScanner(Scanner scanner) {
    SCANNER = scanner;
  }

  public static Scanner getScanner() {
    return SCANNER;
  }
}