/**
 * (c) Copyright 2017 Hewlett Packard Enterprise Development LP
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
package io.swagger.oas.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonValue;

@XmlRootElement
public class JsonValueModel {
    public ValueBean value;
    public InnerBean inner;
    public ListBean list;
    
    public static class ValueBean {
        @JsonValue
        public String getString(){
            return "string";
        }
        
        public int getInt() {
            // Should be ignored due to JsonValue annotation on other method.
            return 0;
        }
    }

    public static class InnerBean {
        @JsonValue
        public ComplexBean getComplexBean() {
            return new ComplexBean();
        }
    }
    
    public static class ComplexBean {
        public int count;
        public String name;
    }
    
    public static class ListBean {
        @JsonValue
        List<String> getList() {
            return new ArrayList<String>();
        }
    }
}
