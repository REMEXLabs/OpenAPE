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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openape.api.DatabaseObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * User context object defined in 7.2.1
 */
@XmlRootElement
public class UserContext extends DatabaseObject {
    private static final long serialVersionUID = 5891055316807633786L;

    /**
     * Checks if a compare user context has the same contexts as a base context.
     * Does return true if it has MORE contexts.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same contexts as base, false if not.
     */
    private static boolean hasUserContextTheSameContexts(UserContext base, UserContext compare) {
        for (final Context baseContext : base.getContexts()) {
            // Match checks if for each context in this there is one in
            // compare.
            boolean match = false;
            for (final Context compareContext : compare.getContexts()) {
                // if id fits check if context fits.
                if (baseContext.getId().equals(compareContext.getId())) {
                    match = true;
                    if (!baseContext.equals(compareContext)) {
                        return false;
                    }
                }
            }
            // no matching context
            if (match != true) {
                return false;
            }
        }
        return true;
    }

    private List<Context> contexts;

    public UserContext() {
        this.contexts = new ArrayList<Context>();
    }

    public void addContext(Context c) {
        this.contexts.add(c);

    }

    /**
     * Checks if user contexts are equal in field values.
     *
     * @param compare
     *            user context to compare with.
     * @return true if contexts are equal in field values, false else.
     */
    @JsonIgnore
    public boolean equals(UserContext compare) {
        return (UserContext.hasUserContextTheSameContexts(compare, this) && UserContext
                .hasUserContextTheSameContexts(this, compare));

    }

    @XmlElement(name = "context")
    public List<Context> getContexts() {
        return this.contexts;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }

}
