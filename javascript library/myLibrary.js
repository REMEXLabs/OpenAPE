(function(window) {
    function defineOpenhab() {
        var openhab = {};

        openhab.getAllThings = function (thingtype) {
            getSource("http://localhost:8080/rest/things", function (resThings) {
                var allThings = [];
                for (var i = 0; i < resThings.length; i++) {
                    if (resThings[i].UID.split(":")[1] == thingtype) {
                        allThings.push(resThings[i].UID.split(":")[2]);
                    }
                }
                openhab = allThings;
            })
            this.things = openhab;
        }

        openhab.connect = function (thingtype, thing) {
            var allLinkedItems = [];
            getSource("http://localhost:8080/rest/things", function (resThingsNew) {
                var resThings = resThingsNew;
                if (typeof(thing) === 'undefined') {
                    console.log("Please specify the thing you want to get");
                } else if (typeof(thingtype) === 'undefined') {
                    console.log("Please specify the thingtype you want to get the thing from");
                } else {
                    for(var i=0; i < resThings.length; i++){
                        if(resThings[i].UID.split(":")[1] == thingtype && resThings[i].UID.split(":")[2] == thing){
                            for(var j=0; j < resThings[i].channels.length; j++){
                                if(resThings[i].channels[j].linkedItems.length != 0){
                                    for(var k=0; k < resThings[i].channels[j].linkedItems.length; k++){
                                        allLinkedItems.push(resThings[i].channels[j].linkedItems[k]);
                                    }
                                }
                            }
                        }
                    }
                }
            })

            getSource("http://localhost:8080/rest/items", function (resItems) {
                var urls = {};
                for(var i=0; i < allLinkedItems.length; i++){
                    for(var j=0; j < resItems.length; j++){
                        if(resItems[j].name == allLinkedItems[i]){
                            urls[allLinkedItems[i]] = resItems[j].link;
                        }
                    }
                }
                openhab = urls;
            })
            this.urls = openhab;
        }

        openhab.connect.prototype.setValue = function(key, value){
            var url = this.urls[key];

            var req = new XMLHttpRequest();
            req.open("POST", url, true);
            req.send(key, value);
        }

        openhab.connect.prototype.getValue = function(key){
            var url = this.urls[key];

            var req = new XMLHttpRequest();
            req.open("POST", url, true);
            req.send(key, value);
        }

        return openhab;
    }

    function getSource(url, callback) {
        var response;

        var req = new XMLHttpRequest;
        req.onreadystatechange = function () {
            if (req.readyState === 4 && req.status === 200 && callback) {
                callback(JSON.parse(req.responseText));
            }
        }

        req.open("GET", url, false);
        req.send();
    }

    if(typeof(playnhab) === 'undefined') {
        window.openhab = defineOpenhab();
    }
})(window);