package com.analog.service;

import com.analog.model.YamlConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

public class YamlConfigParser {

  public YamlConfig parse() throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

    return mapper
        .readValue(new File("src/test/resources/cect.yml"), YamlConfig.class);

  }

}
