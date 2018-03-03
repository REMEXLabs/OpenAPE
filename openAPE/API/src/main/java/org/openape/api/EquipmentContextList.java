package org.openape.api;

import java.net.URI;
import java.util.List;

import org.openape.api.equipmentcontext.EquipmentContext;

import com.fasterxml.jackson.annotation.JsonSetter;

public class EquipmentContextList extends ContextList<EquipmentContext> {

    public EquipmentContextList(List<EquipmentContext> contexts, String url) {
        super(contexts, url, "equipment-context-uri");
    }
    public EquipmentContextList() {
        super();
    }
    
    public List<URI> getEquipmentContextUris() {
        return getContextUris();
    }

    @JsonSetter("equipment-context-uris")
    public void setEquipmentContextUris(final List<URI> equipmentContextUris) {
        setContextUris(equipmentContextUris);
    }

}
