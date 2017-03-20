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

    public Condition() {
    }

    /**
     * @param type
     *            must be 'not', 'eq', 'ne', 'lt', 'le', 'gt', 'ge', 'and' or
     *            'or'. <br>
     *            If type is "not", operands shall have exactly one element. <br>
     *            If type is "eq", "ne", "lt", "le", "gt", or "ge", operands
     *            shall have exactly two elements. <br>
     *            If type is "and" or "or", operands shall have at least two
     *            elements.
     * @param operands
     *            must be a list of either conditions or a Map<String, int>.
     * @throws IllegalArgumentException
     */
    public Condition(String type, List<Object> operands) throws IllegalArgumentException {
        if (!(type.equals("not") || type.equals("eq") || type.equals("ne") || type.equals("lt")
                || type.equals("le") || type.equals("gt") || type.equals("ge")
                || type.equals("and") || type.equals("or"))) {
            throw new IllegalArgumentException(
                    "Type must be 'not', 'eq', 'ne', 'lt', 'le', 'gt', 'ge', 'and' or 'or'.");
        }
        switch (type) {
        case "not":
            if (operands.size() != 1) {
                throw new IllegalArgumentException(
                        "If type is 'not', operands shall have exactly one element.");
            }
            break;
        case "eq":
        case "ne":
        case "lt":
        case "le":
        case "gt":
        case "ge":
            if (operands.size() != 2) {
                throw new IllegalArgumentException(
                        "If type is 'eq', 'ne', 'lt', 'le', 'gt', or 'ge', operands shall have exactly two elements.");
            }
            break;
        case "and":
        case "or":
            if (operands.size() < 2) {
                throw new IllegalArgumentException(
                        "If type is 'and' or 'or', operands shall have at least two elements.");
            }
            break;
        default:
            break;
        }
        this.type = type;
        this.operands = operands;
    }

}
