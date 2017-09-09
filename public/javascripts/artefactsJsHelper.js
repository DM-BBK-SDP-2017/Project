/**
 * Created by dannymadell on 02/07/2017.
 */




function edit_artefact(arg) {


    // SET UP MODAL


    $('#artefact').modal('show');
    $("[name='submission_button']").attr("id", "submitEditArtefactButton");

    $('#info').select();

    $.getJSON('api/artefacts/' + arg).success(function (data) {

        tinyMCE.get('info').setContent(htmlDecode(data[0].content));
        //console.log(htmlDecode(data[0].tags_ids_string).split(","));

        ms.clear();

        ms.setValue(htmlDecode(data[0].tags_ids_string).split(","));

        categoriesMS.clear();

        $.getJSON("api/categories").success(function(cats) {

            categoriesMS.setValue($.grep(cats, function(e){return e.id == data[0].category_id})[0].name);

        });

        //console.log(data[0].tags_ids_string.split(",").length);


    });


    // ON SAVE


    $("[id='submitEditArtefactButton']").unbind('click').on(
        'click',
        function (e) {
            e.preventDefault();
            //console.log("HERE");


            updateArtefact(arg);


        });
}

function updateArtefact(arg) {


    var tags_ids_String = ms.getValue().join();
    var category_id = categoriesMS.getValue();

    var user = $('#user').val();
    var info = htmlEncode(tinyMCE.get('info').getContent());
    //$( infofromtinyMCE ).serialize();
    //var json = '{ "id": 0, "content":"' + info + '", "tags_ids_string": "' + tags_ids_String + '", "creator": 0, "created": "1971-01-01T00:00:00.000" }'

    var json = '{ "id": '+ arg +
        ', "content":"' + info.replace(/[\r?\n]/g, " ") +
        '", "tags_ids_string": "' + tags_ids_String +
        '"category_id": '+ category_id +', ' +
        '", "creator": 0, ' +
        '"created": "1971-01-01T00:00:00.000" }'

    //console.log(json)
    $('#user').val('');
    tinyMCE.get('info').setContent('');
    $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        data: json,
        url: "/api/artefacts",
        success: function (data) {
            console.log(data);

            ms.clear();
            loadData();
        },
        error: function (response) {
            alert("error " + response.statusText + ":" + response.status + " - " + response.responseText)
        }
    });


}

function updateArtefactTags(arg) {




}

function postNewArtefact() {


    var tags_ids_String = ms.getValue().join();
    var category_id;
    if (categoriesMS.getValue().length == 0)
        {category_id = 0} else
        {category_id = categoriesMS.getValue()[0]};
    console.log(category_id);


    var user = $('#user').val();
    var info = htmlEncode(tinyMCE.get('info').getContent());
    //$( infofromtinyMCE ).serialize();
    //var json = '{ "id": 0, "content":"' + info + '", "tags_ids_string": "' + tags_ids_String + '", "creator": 0, "created": "1971-01-01T00:00:00.000" }'

    var json = '{ ' +
        '"id": 0, ' +
        '"content":"' + info.replace(/[\r?\n]/g, " ") + '", ' +
        '"tags_ids_string": "' + tags_ids_String + '", ' +
        '"category_id": '+ category_id +', ' +
        '"creator": 0, ' +
        '"created": "1971-01-01T00:00:00.000" }'

    //console.log(json)
    $('#user').val('')
    tinyMCE.get('info').setContent('');
    $.ajax({
        type : "POST",
        contentType : "application/json; charset=utf-8",
        data : json,
        url : "/api/artefacts",
        success : function(data) {
            console.log(data);

            ms.clear();
            loadData();
        },
        error : function(response) {
            alert("error " + response.statusText + ":"+ response.status+" - " +response.responseText)
        }
    });


}




function add() {
    console.log("START MODAL");
    $("[name='submission_button']").attr('id','submitArtefactButton');
    $('#artefact').modal('show');
    $('#info').select();

    $("[id='submitArtefactButton']").unbind('click').on(
        'click',
        function(e) {
            e.preventDefault();

            postTags(postNewArtefact);

        });

}




function loadData() {

    $.getJSON("api/categories").success(function categoriesMagicSuggest(cats) {


        $.getJSON(
            "api/artefacts",
            function (data) {
                $("#artefacts").empty();
                $.each(data, function (key, j) {

                    var dropup = '<div class="btn-group pull-right">\n' +
                        '<button class="btn btn-primary toggle-dropdown" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"> <span class="glyphicon glyphicon-cog"></span>\n' +
                        '</button>\n' +
                        '<ul class="dropdown-menu dropdown-menu-right">\n' +
                        '<li onclick="edit_artefact(' + j.id + ')"><a href="#">Edit</a>\n' +
                        '</li>\n' +
                        '<li><a href="#">Delete</a>\n' +
                        '</li>\n' +
                        '</ul>\n' +
                        '</div>'
                    var head = '<div class="panel panel-primary"><div class="panel-heading clearfix"><h3 class="panel-title pull-left" style="padding-top: 7.5px;">' + j.created + '</h3>' + dropup
                    head = head + '</div><div class="panel-body">'


                    var newone = htmlDecode(j.content);
                    var newoneTag = j.tags_ids_string.toString().replace(/,/g, ", ")
                    var newoneCat = $.grep(cats, function(e){return e.id == j.category_id})[0].name;
                    var tail = '</div><div class="panel-footer"><div class="container"><div class="col-md-2">Tags: <mark style="background-color: olivedrab;color: white;padding: 5px; border-radius: 5px">' + newoneTag + '</mark></div> Category: <mark style="background-color: olivedrab;color: white;padding: 5px;border-radius: 5px">' + newoneCat + '</mark></div></div></div></div>'

                    //console.log(newone)
                    $("#artefacts").append(
                        head + newone + tail);
                    //test);
                    $("#artefact")
                    tinyMCE.get('info').setContent('');

                });

            }).fail(function (response) {
            console.log("error " + response.statusText + ":" + response.status + " - " + response.responseText)
        });
    });
}



function postArtefactTags(tag) {

    var tagjson = '{ "artefact_Tag_Id": 0, "artefact_Tag":"' + tag.replace(/[\r?\n]/g, " ") + '", "creator_id": 0, "created": "1971-01-01T00:00:00.000" }'
    $.ajax({
        type : "POST",
        contentType : "application/json; charset=utf-8",
        data : tagjson,
        url : "/api/artefacttag",
        success : function(data) {
            console.log('Posted ' + data);
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

function postTags(func) {
    for (var i = 0; i < ms.getValue().length; i++) {

        if (tags.indexOf(ms.getValue()[i]) == -1) {

            //postArtefactTags(ms.getValue()[i]);
            tags.push(ms.getValue()[i]);

        }
    }

    func();

}

function search(arg) {

    if (arg == '') {loadData()}
    else {

        $.getJSON(
            "api/artefacts/search/" + arg,
            function (data) {
                $("#artefacts").empty();
                $.each(data, function (key, j) {

                    var newone = htmlDecode(j.content);
                    var newoneTag = j.tags_ids_string.toString().replace(/,/g, ", ")

                    var dropup = '<div class="btn-group pull-right">\n' +
                        '<button class="btn btn-primary toggle-dropdown" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"> <span class="glyphicon glyphicon-cog"></span>\n' +
                        '</button>\n' +
                        '<ul class="dropdown-menu dropdown-menu-right">\n' +
                        '<li onclick="edit_artefact(' + j.id + ')"><a href="#">Edit</a>\n' +
                        '</li>\n' +
                        '<li><a href="#">Delete</a>\n' +
                        '</li>\n' +
                        '</ul>\n' +
                        '</div>'
                    var head = '<div class="panel panel-primary" id="artefact"><div class="panel-heading clearfix"><h3 class="panel-title pull-left" style="padding-top: 7.5px;">' + j.created + '</h3>' + dropup
                    head = head + '</div><div class="panel-body">'


                    var tail = '</div><div class="panel-footer">' + newoneTag + '</div></div>'

                    //console.log(newone)
                    $("#artefacts").append(
                        head + newone + tail).fadeIn();

                    //test);
                    tinyMCE.get('info').setContent('');

                });

            }).fail(function (response) {
            console.log("error " + response.statusText + ":" + response.status + " - " + response.responseText)
        })
    }
}




function htmlEncode(value){
    //create a in-memory div, set it's inner text(which jQuery automatically encodes)
    //then grab the encoded contents back out.  The div never exists on the page.
    //return $('<div/>').text(value).html();
    return value.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
}

function htmlDecode(value){
    return $('<div/>').html(value).text();

}

htmlEncode('<b>test</b>')
// result"&lt;b&gt;test&lt;/b&gt;"

//return mystring.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
