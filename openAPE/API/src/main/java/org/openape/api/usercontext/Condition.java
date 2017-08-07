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

import java.util.LinkedHashMap;
import java.util.List;

import org.openape.api.Messages;

public class Condition {
	/**
	 * must be 'not', 'eq', 'ne', 'lt', 'le', 'gt', 'ge', and or 'or'. <br>
	 * If type is "not", operands shall have exactly one element. <br>
	 * If type is "eq", "ne", "lt", "le", "gt", or "ge", operands shall have
	 * exactly two elements. <br>
	 * If type is "and" or "or", operands shall have at least two elements.
	 */
	private String type = null;
	/**
	 * Either Condition or String.
	 */
	private List<Object> operands = null;

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
	 *            must be a list of either conditions or strings.
	 * @throws IllegalArgumentException
	 *             if this is not the case.
	 */
	public Condition(final String type, final List<Object> operands) throws IllegalArgumentException {
		this.checkType(type);
		this.checkOpernadListLength(type, operands);
		this.checkOperandClasses(operands);
		this.type = type;
		this.operands = operands;
	}

	/**
	 * @param operands
	 *            must be a list of either conditions or strings.
	 * @throws IllegalArgumentException
	 *             if this is not the case.
	 */
	private void checkOperandClasses(final List<Object> operands) {
		for (final Object operand : operands) {
			if (!(operand instanceof Condition) && !(operand instanceof String)
					&& !(operand instanceof LinkedHashMap<?, ?>)) {// LinkedHashMap
				// is used by
				// json to
				// store sub
				// results.
				throw new IllegalArgumentException(Messages.getString("Condition.wrongOperandTypesErrorMsg")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * If type is "not", operands shall have exactly one element. <br>
	 * If type is "eq", "ne", "lt", "le", "gt", or "ge", operands shall have
	 * exactly two elements. <br>
	 * If type is "and" or "or", operands shall have at least two elements.
	 *
	 * @throws IllegalArgumentException
	 *             if this is not the case.
	 */
	private void checkOpernadListLength(final String type, final List<Object> operands)
			throws IllegalArgumentException {
		if (type.equals(Messages.getString("Condition.not"))) { //$NON-NLS-1$
			if (operands.size() != 1) {
				throw new IllegalArgumentException(Messages.getString("Condition.wrongNumberOfOperandsTypeNot")); //$NON-NLS-1$
			}
		} else if (type.equals(Messages.getString("Condition.equal")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.notEqual")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.lessThen")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.lessThenOrEqual")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.greaterThen")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.greaterThenOrEqual"))) { //$NON-NLS-1$
			if (operands.size() != 2) {
				throw new IllegalArgumentException(Messages.getString("Condition.wrongNumberOfOperands")); //$NON-NLS-1$
			}
		} else if (type.equals(Messages.getString("Condition.and")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.or"))) { //$NON-NLS-1$
			if (operands.size() < 2) {
				throw new IllegalArgumentException(Messages.getString("Condition.wrongNumberOfOperandsTypeAndOrOr")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @param type
	 *            must be 'not', 'eq', 'ne', 'lt', 'le', 'gt', 'ge', 'and' or
	 *            'or'. <br>
	 * @throws IllegalArgumentException
	 *             if this is not the case.
	 */
	private void checkType(final String type) throws IllegalArgumentException {

		if (!(type.equals(Messages.getString("Condition.not")) || type.equals(Messages.getString("Condition.equal")) //$NON-NLS-1$ //$NON-NLS-2$
				|| type.equals(Messages.getString("Condition.notEqual")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.lessThen")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.lessThenOrEqual")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.greaterThen")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.greaterThenOrEqual")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.and")) //$NON-NLS-1$
				|| type.equals(Messages.getString("Condition.or")))) { //$NON-NLS-1$
			throw new IllegalArgumentException(Messages.getString("Condition.wrongTypeErrorMsg")); //$NON-NLS-1$
		}
	}

	public List<Object> getOperands() {
		return this.operands;
	}

	public String getType() {
		return this.type;
	}

	/**
	 * If type is "not", operands shall have exactly one element. <br>
	 * If type is "eq", "ne", "lt", "le", "gt", or "ge", operands shall have
	 * exactly two elements. <br>
	 * If type is "and" or "or", operands shall have at least two elements.
	 *
	 * @param operands
	 *            must be a list of either conditions or strings.
	 * @throws IllegalArgumentException
	 *             if this is not the case.
	 */
	public void setOperands(final List<Object> operands) throws IllegalArgumentException {
		this.checkOperandClasses(operands);
		// Check the operands list length if type is already set.
		if (this.getType() != null) {
			this.checkOpernadListLength(this.getType(), operands);
		}
		this.operands = operands;
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
	 * @throws IllegalArgumentException
	 *             if this is not the case.
	 */
	public void setType(final String type) {
		this.checkType(type);
		// Check the operands list length if operands are already set.
		if (this.getOperands() != null) {
			this.checkOpernadListLength(type, this.getOperands());
		}
		this.type = type;
	}

}
