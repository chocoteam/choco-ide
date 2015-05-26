// Global variable for Choco samples
var samples;

window.onload = function () {
    // Fullfill drilldown list with Choco samples
    updateSamples();

    // Initializing Ace editor
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.getSession().setMode("ace/mode/choco");
    editor.$blockScrolling = Infinity;

    // Event handler when selected value has changed
    $('#samples').change(function () {
        var filenameSelectedSample = $('#samples option:selected').val();

        var jsonSamples = jQuery.parseJSON(samples);
        jsonSamples.forEach(function (jsonSample) {
            if (filenameSelectedSample == jsonSample.filename) {
                editor.getSession().setValue(jsonSample.content);
            }
        });
    });
}

function updateSamples() {
    var select = $('#samples');

    // Fire the HTTP GET request
    var request = $.ajax({
        url: "/getCodeSampleList",
        type: "get"
    });

    request.done(function (response, textStatus, jqXHR) {
        // Updating the model
        samples = response;

        // Updating the initial code in Ace editor
        ace.edit("editor").getSession().setValue(jQuery.parseJSON(samples)[0].content);
        var jsonSamples = jQuery.parseJSON(response);

        // Updating the drilldown
        jsonSamples.forEach(function (jsonSample) {
            var option = document.createElement("option");
            option.text = jsonSample.name;
            option.value = jsonSample.filename;
            select.append(option);
        });
    });

    request.fail(function (jqXHR, textStatus, errorThrown) {
        // Log the error to the console
        console.error(
            "The following error occurred: " +
            textStatus, errorThrown
        );
    });
}

function defineAceKeywords(){

}


function compile() {
    var editor = ace.edit("editor");
    var code = editor.getSession().getValue();
    var console = document.getElementById('console');

    // Fire the HTTP POST request
    var request = $.ajax({
        url: "/compile",
        type: "post",
        data: {body: code}
    });

    // Callback handler that will be called on success - HTTP 200 OK
    request.done(function (response, textStatus, jqXHR) {
        var jsonObject = jQuery.parseJSON(response);
        var compilationMessage = jsonObject.Errors;
        var runtimeEvents = jsonObject.Events;

        if (compilationMessage != "") {
            console.innerHTML = "<p class='error'>Error during compilation : " + compilationMessage + "</p>";
        }

        runtimeEvents.forEach(function (runtimeEvent) {
            var textColorClass = "success";
            if (runtimeEvent.Kind == "stdout") {

            }
            else if (runtimeEvent.Kind == "stderr") {
                textColorClass = "error"
            }
            else {
                // ????
            }
            console.innerHTML += "<p class='"+textColor+"'>" + runtimeEvent.Message + "</p>";
        });
    });


    // Callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown) {
        // Log the error to the console
        console.error(
            "The following error occurred: " +
            textStatus, errorThrown
        );
        console.innerHTML="<p class='error'>Server Error, please try again</p>";

    });

    // Callback handler that will always be called
    request.always(function () {
    });
}