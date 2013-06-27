package testresources;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

public class Handle {
  @XmlElement
  public String name;

  @XmlElement
  public int screwCount;
}
