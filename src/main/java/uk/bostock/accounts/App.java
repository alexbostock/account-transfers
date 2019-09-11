package uk.bostock.accounts;

import static spark.Spark.*;

/**
 * Hello World!
 */
public class App {
      public static void main(String[] args) {
                get("/hello", (req, res) -> "Hello World");
      }
}
