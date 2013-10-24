package converter.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@JsonTypeInfo( use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type" )
@JsonSubTypes( { @Type( value = JWildAnimal.class, name = "wild" ),
                             @Type( value = JDomesticAnimal.class, name = "domestic" ) } )
@ApiModel("Animal desc")
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
