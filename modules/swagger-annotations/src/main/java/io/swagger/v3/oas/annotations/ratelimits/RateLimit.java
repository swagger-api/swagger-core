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

package io.swagger.v3.oas.annotations.ratelimits;

/**
 * The annotation may be used to define a custom rate limit
 */
public @interface RateLimit {
  
  /**
   * The name of the rate limit
   *
   * @return the name of the rate limit
   */
  String name();
  
  /**
   * The maximum number of times the operation can be called within the interval secs. The
   * default of -1 means there is no rate limit on the operation
   *
   * @return the rate limit
   */
  int callLimit() default -1;
  
  /**
   * The maximum total number of seconds the operation can be processed. The default of -1 means
   * there is no rate limit on the operation
   *
   * @return the rate limit seconds
   */
  int intervalLimitSecs() default -1;
  
  /**
   * The interval seconds for the entire rate limit on the operation
   *
   * @return the interval seconds
   */
  int intervalSecs() default -1;
  
}