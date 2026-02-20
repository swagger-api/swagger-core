package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonView;

public class JsonViewObject {
  public static class View {

    public interface Public {
    }

    public interface Protected {
    }

    public interface Private {
    }
  }

  public static class Person {

    @JsonView({View.Public.class, View.Protected.class, View.Private.class})
    public String id;

    @JsonView({View.Protected.class, View.Private.class})
    public String firstName;

    @JsonView({View.Protected.class, View.Private.class})
    public String lastName;

    @JsonView(View.Private.class)
    public String address;

    public String email;
  }
}
