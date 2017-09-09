/**
 *
 *
 * Created by dannymadell on 22/07/2017.
 */


function updateCategories(categories) {

    $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        data: categories,
        url: "/api/categories",
        success: function (data) {
            console.log(data);

        },
        error: function (response) {
            alert("error " + response.statusText + ":" + response.status + " - " + response.responseText)
        }
    });
}


function addGroup(groupName) {


    var json = JSON.stringify({"id": 0, "group_name": groupName, "created_by": 0, "created_timestamp": "1971-01-01T00:00:00.000"});
    console.log(json);

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

function addComment(artefact_id, comment_content) {

    var json = JSON.stringify({ "comment_id": 0, "from_user": 0, "artefact_id": new Number(artefact_id), "comment_content": comment_content, "comment_timestamp": "1971-01-01T00:00:00.000" });

    console.log(json);



    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: json,
        url: "/api/comment",
        success: function (data) {
            console.log(data);

        },
        error: function (response) {
            alert("error " + response.statusText + ":" + response.status + " - " + response.responseText)
        }
    })


    //     comment_id: Int,
    //   from_user: Int,
    //   artefact_id: Int,
    //   comment_content: String,
    //   comment_timestamp: Timestamp



}

function addCategory() {

   // category_id
   // category_name
   // parent

    var catName = $('#newCategoryName').val();
    var parentId = categoriesMS.getValue()[0];


    var json = JSON.stringify({
        "category_id": 0,
        "category_name": catName,
        "parent": parentId});


    console.log(json);


    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: json,
        url: "/api/categories",
        success: function (data) {
            console.log(data);
            window.location.reload();

        },
        error: function (response) {
            alert("error " + response.statusText + ":" + response.status + " - " + response.responseText)
        }
    })


}

function editCategory(cat) {

    var categoriesEdit;
    var categoriesEditMS;

            $('#ddCategory' + cat).modal('show');
            categoriesEdit = $.getJSON("api/categories").success(function categoriesMagicSuggest(cats) {
                categoriesEditMS = $('#editCategoryChooser').magicSuggest({
                    allowFreeEntries: false,
                    placeholder: 'Begin typing to search for categories',
                    maxSelection: 1,
                    data: cats,
                    value: cat

                })});

}