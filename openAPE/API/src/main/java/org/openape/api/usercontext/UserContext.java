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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openape.api.Resource;

/**
 * User context object defined in 7.2.1
 */
@XmlRootElement
public class UserContext extends Resource {
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
        final Set<String> baseKeySet = base.getContexts().keySet();
        final Set<String> compareKeySet = compare.getContexts().keySet();
        for (final String baseKey : baseKeySet) {
            // Match checks if for each context in this there is one in
            // compare.
            boolean match = false;
            for (final String compareKey : compareKeySet) {
                // if id fits check if context fits.
                if (baseKey.equals(compareKey)) {
                    match = true;
                    if (!base.getContext(baseKey).equals(compare.getContext(compareKey))) {
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

    private Map<String, Context> contexts;

    public UserContext() {
        this.contexts = new HashMap<String, Context>();
    }

    public void addContext(String id, Context c) {
        this.contexts.put(id, c);

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

    /**
     * @param id
     * @return null if not found.
     */
    @JsonIgnore
    public Context getContext(String id) {
        return this.getContexts().get(id);
    }

    @XmlElement(name = "context")
    public Map<String, Context> getContexts() {
        return this.contexts;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    public void setContexts(Map<String, Context> contexts) {
        this.contexts = contexts;
    }

}
