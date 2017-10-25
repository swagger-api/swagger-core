/**
 * Copyright 2017 SmartBear Software
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

package io.swagger.v3.oas.models.parameters;

import java.util.Objects;

/**
 * QueryParameter
 */


public class QueryParameter extends Parameter {
  private String in = "query";

  /**
   * returns the in property from a QueryParameter instance.
   *
   * @return String in
   **/

  public String getIn() {
    return in;
  }

  public void setIn(String in) {
    this.in = in;
  }

  public QueryParameter in(String in) {
    this.in = in;
    return this;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryParameter queryParameter = (QueryParameter) o;
    return Objects.equals(this.in, queryParameter.in) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(in, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryParameter {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    in: ").append(toIndentedString(in)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

