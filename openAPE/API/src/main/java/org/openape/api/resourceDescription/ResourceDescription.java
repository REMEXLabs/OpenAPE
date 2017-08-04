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

package org.openape.api.resourceDescription;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openape.api.Property;
import org.openape.api.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Resource description object defined in 7.7.1
 */
@XmlRootElement
public class ResourceDescription extends Resource {
	private static final long serialVersionUID = -3341210067495347309L;

	/**
	 * Checks if a compare resource description has the same properties as a
	 * base context. Does return true if it has MORE contexts.
	 *
	 * @param base
	 * @param compare
	 * @return true, if compare has the same properties as base, false if not.
	 */
	private static boolean hasResourceDescriptionTheSameProperties(final ResourceDescription base,
			final ResourceDescription compare) {
		for (final Property baseProperty : base.getPropertys()) {
			// Match checks if for each property in this there is one in
			// compare.
			boolean match = false;
			for (final Property compareContext : compare.getPropertys()) {
				// if id fits check if property fits.
				if (baseProperty.getName().equals(compareContext.getName())) {
					if (baseProperty.equals(compareContext)) {
						match = true;
					}
				}
			}
			// no matching property
			if (match != true) {
				return false;
			}
		}
		return true;
	}

	private List<Property> propertys = new ArrayList<Property>();

	public ResourceDescription() {
		this.propertys = new ArrayList<Property>();
	}

	public void addProperty(final Property property) {
		this.propertys.add(property);

	}

	/**
	 * Checks if resource descriptions are equal in field values.
	 *
	 * @param compare
	 *            resource description to compare with.
	 * @return true if contexts are equal in field values, false else.
	 */
	@JsonIgnore
	public boolean equals(final ResourceDescription compare) {
		return (ResourceDescription.hasResourceDescriptionTheSameProperties(compare, this)
				&& ResourceDescription.hasResourceDescriptionTheSameProperties(this, compare));

	}

	@XmlElement(name = "property")
	public List<Property> getPropertys() {
		return this.propertys;
	}

	@Override
	@JsonIgnore
	public boolean isValid() {
		return true;
	}

	public void setPropertys(final List<Property> propertys) {
		this.propertys = propertys;
	}

}
