package io.swagger.models.issue2121;

public class OrderDto {
    public static class Summary {
      private long id;
      private String name;

      public long getId() {
        return id;
      }

      public void setId(long id) {
        this.id = id;
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }
    }
    public static class Detail {
      private long id;
      private String name;
      private String description;

      public long getId() {
        return id;
      }

      public void setId(long id) {
        this.id = id;
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public String getDescription() {
        return description;
      }

      public void setDescription(String description) {
        this.description = description;
      }
    }
  }