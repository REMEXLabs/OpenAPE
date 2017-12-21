package org.openape.api;

import java.util.List;

import org.openape.api.equipmentcontext.EquipmentContext;

public class EquipmentContextList extends ContextList<EquipmentContext> {

    public EquipmentContextList(List<EquipmentContext> contexts, String url
            ) {
        super(contexts, url, "equipment-context-uris");
    }
}
