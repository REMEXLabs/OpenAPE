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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class holding attributes of contexts and resource descriptions that are used
 * for storage and authorization.
 */
public class ImplementationParameters {

    private static final String FALSE = "false";

    private static final String PUBLIC = "public";

    private static final String IMPLEMENTATION_PARAMETERS = "implementation-parameters";

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
    public static String addPublicAttributeIfMissing(final String xml)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        // Create document to work on.
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
        // set implementation-parameters element and public to false if not set
        final Element root = document.getDocumentElement();
        final NodeList children = root.getChildNodes();
        boolean found = false;
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equals(ImplementationParameters.IMPLEMENTATION_PARAMETERS)) {
                found = true;
            }
        }
        if (!found) {
            final Element implemParams = document.createElement(ImplementationParameters.IMPLEMENTATION_PARAMETERS);
            root.insertBefore(implemParams, root.getFirstChild());
            implemParams.setAttribute(ImplementationParameters.PUBLIC, ImplementationParameters.FALSE);
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

    @DefaultValue(ImplementationParameters.FALSE)
    private boolean isPublic = false;

    @XmlTransient
    public String getOwner() {
        return this.owner;
    }

    @XmlAttribute(name = ImplementationParameters.PUBLIC)
    public boolean isPublic() {
        return this.isPublic;
    }

    /**
     * Remove implementation Parameters from generated xml string to make it standard conform.
     *
     * @param xml
     * @return corrected xml
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    @JsonIgnore
    public String removeImplemParams(final String xml)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        // Create document to work on.
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
        // remove implementation-parameters element
        final Element root = document.getDocumentElement();
        final NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equals(ImplementationParameters.IMPLEMENTATION_PARAMETERS)) {
                root.removeChild(children.item(i));
            }
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

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setPublic(final boolean aPublic) {
        this.isPublic = aPublic;
    }

}
