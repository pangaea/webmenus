$.extend({
    getUrlVars: function () {
        var vars = [], hash;
        try {
            var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
            for (var i = 0; i < hashes.length; i++) {
                hash = hashes[i].split('=');
                vars.push(hash[0]);
                temp = hash[1].replace(/[^a-zA-Z 0-9_-]+/g, '');
                vars[hash[0]] = temp;
            }
        }
        catch(e){}
        return vars;
    },
    getUrlVar: function (name) {
        return $.getUrlVars()[name];
    }
});