/**
 * Created by dannymadell on 02/07/2017.
 */







function loadData() {
    $.getJSON(
        "api/artefacts",
        function(data) {
            $("#artefacts").empty();
            $.each(	data,function(key, j) {
                var head = '<div class="panel panel-default"><div class="panel-heading"><h3 class="panel-title">The title</h3></div>'
                head = head	+ '<div class="panel-body">'

                var newone =  j.content
                var newoneTag = j.tags_ids_string
                var tail = '</div><div class="panel-footer">' + newoneTag + '</div></div>'
                //console.log(newone)
                $("#artefacts").append(
                    head + newone + tail);
            });

        }).fail(function(response) {
        console.log("error " + response.statusText + ":"+ response.status+" - " +response.responseText)
    })
}



function postArtefactTags(tag) {





    var tagjson = '{ "artefact_Tag_Id": 0, "artefact_Tag":"' + tag.replace(/[\r?\n]/g, " ") + '", "creator_id": 0, "created": "1971-01-01T00:00:00.000" }'
    $.ajax({
        type : "POST",
        contentType : "application/json; charset=utf-8",
        data : tagjson,
        url : "/api/artefacttag",
        success : function(data) {
            console.log(data);

            loadData();
        },
        error : function(response) {
            alert("error " + response.statusText + ":"+ response.status+" - " +response.responseText)
        }
    });
};



function getArtefactTags(func) {


    $.getJSON("api/artefacttags").success(function(data) {

        for (var i = 0; i < data.length; i++) {
            tags.push(data[i].artefact_Tag);
        }

        func();

    });



};