/**
 * Created by dannymadell on 12/07/2017.
 */


function interactionSender(artefact_id, interaction_type) {

    console.log("Posting interaction type \""+interaction_type+"\" for artefact_id_"+artefact_id)

    var json = '{ "artefact_id": '+artefact_id+', "user_id": 0, "interaction_type": "'+interaction_type+'", "interaction_timestamp": "1971-01-01T00:00:00.000" }'

    $.ajax({
        type : "POST",
        contentType : "application/json; charset=utf-8",
        data : json,
        url : "/feed/interaction",
        success : function(data) {
            console.log(data);

        },
        error : function(response) {
            alert("error " + response.statusText + ":"+ response.status+" - " +response.responseText)
        }
    })
}

function messageSender(artefact_id, message_type, message, to_user) {
    console.log("Posting message type \""+message_type+"\" for artefact_id_"+artefact_id+": "+message)

    var json = '{ "message_id": 0, "message_type": "'+message_type+'", "from_user": 0, "to_user": '+to_user+', "artefact_id": '+artefact_id+', "message": "'+message+'", "timestamp": "1971-01-01T00:00:00.000" }'

    $.ajax({
        type : "POST",
        contentType : "application/json; charset=utf-8",
        data : json,
        url : "/feed/message",
        success : function(data) {
            console.log(data);

        },
        error : function(response) {
            alert("error " + response.statusText + ":"+ response.status+" - " +response.responseText)
        }
    });
}

function like(int) {


    interactionSender(int, "like");

    var button = '#likeButtons_' + int.toString();

    $(button).fadeOut('slow', function() {

        $(button).html('<div class="btn-group"><button class="btn btn-primary btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true" ></span></button></div>');
        $(button).fadeIn();


    });

}

function dislike(int) {

    interactionSender(int, "dislike");
    var art = '#artefact_' + int.toString();

    $(art).fadeOut();

}

function more(int) {

    interactionSender(int, "more");
    $('#more_button_'+int).removeAttr("onclick");
    $('#comment_button_'+int).removeAttr("onclick");

}

function suggest(artefact_id, message_to) {

    var content = $('#suggestion_content_'+artefact_id).val();
    messageSender(artefact_id,"suggestion",content,message_to);

    var button = '#suggest_' + artefact_id.toString();
    $('#suggest_slidedown_' + artefact_id.toString()).fadeOut("slow");

    $(button).fadeOut('slow', function() {

        var div = $('<button class="btn btn-primary btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true" ></span> Thanks for your suggestion!</button>');

        $(this).replaceWith(div);
        $(button).fadeIn("slow");


    });
}

function alertAdmin(artefact_id) {

    var content = $('#alertAdmin_content_'+artefact_id).val();
    messageSender(artefact_id,"alert",content,1);

    var button = '#alertAdmin_' + artefact_id.toString();
    $('#alertAdmin_slidedown_'+ artefact_id.toString()).fadeOut("slow");

    $(button).fadeOut('slow', function() {

        var div = $('<button class="btn btn-primary btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true" ></span> Thanks for your alert!</button>');
        $(this).replaceWith(div);
        $(button).fadeIn("slow");

    });

}

function recommend(artefact_id, arrayOfValues) {


    console.log("Posting recommendation for artefact_id_"+artefact_id)

   // (id: Int,
   //     recommended_by: Int,
   //     recommended_for: Int,
   //     recommendation_score: Double,
   //     recommended_timestamp: Timestamp)

    arrayOfValues.forEach(function(i) {


        var json = '{ "id": 0, ' +
            '"artefact_id": '+artefact_id+', ' +
            '"recommended_by": 0, ' +
            '"recommended_for": '+i+', ' +
            '"recommendation_score": 0.0, ' +
            '"recommended_timestamp": "1971-01-01T00:00:00.000" }'

        $.ajax({
            type : "POST",
            contentType : "application/json; charset=utf-8",
            data : json,
            url : "/api/recommendation",
            success : function(data) {
                console.log(data);

            },
            error : function(response) {
                alert("error " + response.statusText + ":"+ response.status+" - " +response.responseText)
            }
        })

        }


    )


    var button = '#recommend_' + artefact_id.toString();

    //console.log(arrayOfValues);

    $('#recommend_slidedown_'+ artefact_id.toString()).fadeOut("slow");

    $(button).fadeOut('slow', function() {

        var div = $('<button class="btn btn-primary btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true" ></span> Thanks for your recommendation!</button>');
        $(this).replaceWith(div);
        $(button).fadeIn("slow");

    });

}

function prepareRecData(artefact_id) {



    var groups = ["sf"];

    console.log("HERE");
    $('#recommend_chooser_'+artefact_id).magicSuggest({
        allowFreeEntries: false,
        placeholder: 'Begin typing to search for groups, you may select up to 3',
        maxSelection: 3,
        height: '90%',
        data: groups
    });

    //console.log('data tags size ' + ms.getData().length);
}

function addComment(artefact_id) {

//    comment_id: Int,
//        from_user: Int,
//        artefact_id: Int,
//        comment_content: String,
//        comment_timestamp: Timestamp

    var content = $('#addComment_content_'+artefact_id).val();
    console.log("Posting comment for artefact_id_"+artefact_id);

    var json = JSON.stringify({ "comment_id": 0, "from_user": 0, "artefact_id": artefact_id, "comment_content": content, "comment_timestamp": "1971-01-01T00:00:00.000"});

    $.ajax({
        type : "POST",
        contentType : "application/json; charset=utf-8",
        data : json,
        url : "/api/comment",
        success : function(data) {
            console.log(data);

        },
        error : function(response) {
            alert("error " + response.statusText + ":"+ response.status+" - " +response.responseText)
        }
    });






    //messageSender(artefact_id,"alert",content,1);

    var button = '#addComment_button_' + artefact_id.toString();
    $('#addComment_slidedown_'+ artefact_id.toString()).fadeOut("slow");

    $(button).fadeOut('slow', function() {

        var div = $('<button class="btn btn-default btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true" ></span> Thanks for your comment, it will appear when you reload this artefact</button>');
        $(this).replaceWith(div);
        $(button).fadeIn("slow");

    });





}





