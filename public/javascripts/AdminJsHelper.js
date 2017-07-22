/**
 *
 *
 * Created by dannymadell on 22/07/2017.
 */


function addGroup(groupName) {


    var json = '{ "id": 0, "group_name": "'+groupName+'", "created_by": 0, "created_timestamp": "1971-01-01T00:00:00.000" }'

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: json,
        url: "/api/group",
        success: function (data) {
            console.log(data);

        },
        error: function (response) {
            alert("error " + response.statusText + ":" + response.status + " - " + response.responseText)
        }
    })

}