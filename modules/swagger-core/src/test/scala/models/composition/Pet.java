package models.composition;

public interface Pet {
  String getType();
  void setType(String type);

  String getName();
  void setName(String name);

  Boolean getIsDomestic();
  void setIsDomestic(Boolean isDomestic);
}