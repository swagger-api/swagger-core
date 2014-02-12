package converter.models;

import com.fasterxml.jackson.annotation.*;

public class ModelWithJacksonAnnotatedPrivateField {
  
  @JsonProperty("hiddenFieldLevelJacksonProp") private String hiddenFieldProp;
  @JsonProperty public String rawFieldProp;
  @JsonProperty("fieldLevelJacksonProp") public String renamableFieldProp;

  @JsonProperty("renamedJacksonProp") private String renamableJackonProp;
  @JsonProperty("renamedJacksonProp2") private String renamableJackonProp2;
  @JsonIgnore private String ignoredJacksonProp;

  public String getIgnoredJacksonProp() {
    return ignoredJacksonProp;
  }

  public String getRenamableJackonProp() { 
    return renamableJackonProp; 
  }

  @JsonProperty("renamedJacksonMethod") public String getRenamableJackonProp2() {
    return renamableJackonProp2;
  }

  @JsonIgnore public String getIgnoredJacksonMethod() {
    return "";
  }

}
