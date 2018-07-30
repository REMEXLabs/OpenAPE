package org.openape.api.listing;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.resourceDescription.ResourceDescription;

@XmlRootElement(name="response")
public class ListingResponse extends DatabaseObject {
private int start;
private int count;
@XmlElement(name="resource-description-uris")
private List<URI> resource;
public int getStart() {
	return start;
}
public void setStart(int start) {
	this.start = start;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
public List<URI> getResource() {
	return resource;
}
public void setResource(List<URI> resource) {
	this.resource = resource;
}

public ResourceDescription	getResourceDescription(URI resourceDescriptionUri) {
	String id = resourceDescriptionUri.getPath();
	return getResourceDescription(id); 
}

public ResourceDescription	getResourceDescription(String ResourceDescriptionId) {
	return null;
}
}
