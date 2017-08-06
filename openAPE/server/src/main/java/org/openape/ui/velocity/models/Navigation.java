package org.openape.ui.velocity.models;

import java.util.ArrayList;

public class Navigation {
    private ArrayList<String> listNavigationLinks;
    private ArrayList<String> listNavigationIDs;

    public ArrayList<String> getListNavigationLinks() {
        return this.listNavigationLinks;
    }

    public void setListNavigationLinks(final ArrayList<String> listNavigationLinks) {
        this.listNavigationLinks = listNavigationLinks;
    }

    public ArrayList<String> getListNavigationIDs() {
        return this.listNavigationIDs;
    }

    public void setListNavigationIDs(final ArrayList<String> listNavigationIDs) {
        this.listNavigationIDs = listNavigationIDs;
    }

}
