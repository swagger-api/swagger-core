package converter.models;

import com.wordnik.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import java.util.Date;

@JsonTypeInfo( use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type" )
@JsonSubTypes( { @Type( value = JWildAnimal.class, name = "wild" ), 
                             @Type( value = JDomesticAnimal.class, name = "domestic" ) } )
public class JAnimal {
  private String type;
  private Date date;

  @ApiModelProperty (value = "type of animal", position = 1)
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @ApiModelProperty (value = "Date added to the zoo", position = 2)
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
