package com.wordnik.swagger.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.wordnik.swagger.util.Yaml;
import org.junit.Test;

public class ModelImplTest {

  @Test
  public void requiredFlag() throws IOException {
    Model model = Yaml.mapper().readValue("id: SchemaObject\n"
      + "properties:\n"
      + "  required_prop:\n"
      + "    type: string\n"
      + "  optional_prop:\n"
      + "    type: string\n"
      + "required: [required_prop]", Model.class);
    assertEquals(2, model.getProperties().size());
    assertTrue(model.getProperties().get("required_prop").getRequired());
    assertFalse(model.getProperties().get("optional_prop").getRequired());
  }
}