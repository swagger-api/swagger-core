package testresources;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.*;

public class Window {
  @XmlElement
  public List<Handle> windows = new ArrayList<Handle>();
}