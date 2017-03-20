/**
 Copyright 2016 Hochschule der Medien - Stuttgart Media University

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.openape.api.usercontext;

import java.util.List;

public class Condition {
    /**
     * must be 'not', 'eq', 'ne', 'lt', 'le', 'gt', 'ge', and or 'or'. <br>
     * If type is "not", operands shall have exactly one element. <br>
     * If type is "eq", "ne", "lt", "le", "gt", or "ge", operands shall have
     * exactly two elements. <br>
     * If type is "and" or "or", operands shall have at least two elements.
     */
    String type;
    /**
     * Either Condition or Map<String, int>.
     */
    List<Object> operands;

    
}
