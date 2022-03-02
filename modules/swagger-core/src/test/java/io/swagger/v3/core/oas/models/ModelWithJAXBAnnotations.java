/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "rootName")
public class ModelWithJAXBAnnotations {
    @XmlAttribute
    public String id;

    @XmlElement(name = "renamed")
    public String name;

    @JsonIgnore
    @XmlAttribute
    public String hidden;

    public List<String> list;

    @XmlElementWrapper(name = "wrappedListItems")
    @XmlElement(name = "wrappedList")
    public List<String> wrappedList;

    @XmlAttribute(name = "doNotUseMe")
    public List<String> forcedElement;
}
