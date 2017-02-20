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

/**
 * User context object defined in 7.2.1
 */
@XmlRootElement
public class UserContext extends DatabaseObject {
    private static final long serialVersionUID = 5891055316807633786L;

    private List<Context> contexts;

    public UserContext() {
        this.contexts = new ArrayList<Context>();
    }

    public void addContext(Context c) {
        this.contexts.add(c);

    }

    @XmlElement(name = "context")
    public List<Context> getContexts() {
        return this.contexts;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }
}
