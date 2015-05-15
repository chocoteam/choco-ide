window.onload = function() {
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.getSession().setMode("ace/mode/java");
}

function compile() {
    var editor = ace.edit("editor");
    var code = editor.getSession().getValue();
    var console = document.getElementById('console');

    // Fire the HTTP POST request
    var request = $.ajax({
        url: "/compile",
        type: "post",
        data: { body: code }
    });

    // Callback handler that will be called on success - HTTP 200 OK
    request.done(function (response, textStatus, jqXHR){

    });

    // Callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown){
        // bouchonnage / code dans request.fail en attendant que le service soit d�velopp�
        // a d�placer dans request.success
        var response = '{"Errors":"Duplicate import of package fr.toto","Events":[{"Message":"Hello guys","Kind":"stdout","Delay":0},{"Message":"2009/11/10 23:00:00 Error..","Kind":"stderr","Delay":0}]}';

        var jsonObject = jQuery.parseJSON(response);
        var compilationMessage = jsonObject.Errors;
        var runtimeEvents = jsonObject.Events;

        if(compilationMessage != "") {
            console.innerHTML += "<p style=\"color:red; background-color: black\">Error during compilation : " + compilationMessage + "</p>";
        }

        runtimeEvents.forEach(function(runtimeEvent) {
            var textColor = "blue";
            if(runtimeEvent.Kind == "stdout") {

            }
            else if(runtimeEvent.Kind == "stderr") {
                textColor = "red"
            }
            else {
                // ????
            }
            console.innerHTML += "<p style=\"color:"+textColor+";\">" + runtimeEvent.Message + "</p>";
        });

        //////////////////////////////////////

        // Log the error to the console
        //console.error(
        //    "The following error occurred: "+
        //    textStatus, errorThrown
        //);
    });

    // Callback handler that will always be called
    request.always(function () {
    });
}