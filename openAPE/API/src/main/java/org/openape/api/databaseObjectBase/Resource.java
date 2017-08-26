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

package org.openape.api.databaseObjectBase;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.DefaultValue;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A database object that is owned by a user and can be public (viewable by
 * other users).
 */

public class Resource extends DatabaseObject {

    private static final long serialVersionUID = 4077081454613480332L;

    /**
     * If input xml is missing the public attribute, it is set with value false.
     * 
     * @param xml
     * @return corrected xml
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    @JsonIgnore
    static protected String addPublicAttributeIfMissing(final String xml)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        // Create document to work on.
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
        // set public to false if not set
        final Element root = document.getDocumentElement();
        if (!root.hasAttribute("public")) {
            root.setAttribute("public", "false");
        }
        // Create String from document
        final DOMSource domSource = new DOMSource(document);
        final StringWriter writer = new StringWriter();
        final StreamResult result = new StreamResult(writer);
        final TransformerFactory tf = TransformerFactory.newInstance();
        final Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        // Update xml string.
        return writer.toString();
    }

    private String owner;

    @DefaultValue("false")
    private boolean isPublic;

    @XmlTransient
    public String getOwner() {
        return this.owner;
    }

    @XmlAttribute(name = "public")
    public boolean isPublic() {
        return this.isPublic;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setPublic(final boolean aPublic) {
        this.isPublic = aPublic;
    }

}
