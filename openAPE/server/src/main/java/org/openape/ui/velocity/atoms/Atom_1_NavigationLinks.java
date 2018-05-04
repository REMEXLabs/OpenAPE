package org.openape.ui.velocity.atoms;

import java.util.LinkedHashMap;

public class Atom_1_NavigationLinks {
    public LinkedHashMap<String, String> generateSubNavigationLinks() {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("myProfile", "My Profile");
        map.put("myContexts", "My Contexts");
        map.put("myResources", "My Resources");
        map.put("myGroups", "My Groups");
        map.put("administration", "Administration");

        return map;
    }

    public LinkedHashMap<String, String> generateTopNavigationLinks() {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("Home", "home");
        map.put("gettingStarted", "Getting Started");
        map.put("tutorials", "Tutorials");
        map.put("downloads", "Downloads");
        map.put("contexts", "Contexts");
        map.put("contact", "Contact");

        return map;
    }

    public LinkedHashMap<String, String> generateUserSectionLinksLoggedOut() {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("signin", "<div class='glyphicon glyphicon-log-in'></div>&nbsp; Sign in");
        map.put("signup", "<img src='img/signup.png' width='20px' height='20px'>&nbsp; Sign up");

        return map;
    }
}
