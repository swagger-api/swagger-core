package converter.models;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum(String.class)
public enum TestEnum {
  @XmlEnumValue("PRIVATE")PRIVATE,
  @XmlEnumValue("PUBLIC")PUBLIC,
  @XmlEnumValue("SYSTEM")SYSTEM,
  @XmlEnumValue("INVITE-ONLY")INVITE_ONLY;
}
