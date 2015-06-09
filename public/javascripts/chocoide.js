// Global variable for Choco samples
var samples;
var chocoLangClasses = "Object";

window.onload = function () {
    // Fullfill drilldown list with Choco samples
    updateSamples();

    // Initializing Ace editor
    ace.require("ace/ext/language_tools");
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");

    editor.$blockScrolling = Infinity;
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableLiveAutocompletion: false
    });

    var request = $.ajax({
        url: "/getKeywords",
        type: "get"
    });

    request.success(function (response, textStatus, jqXHR) {
        var responseObject = JSON.parse(response);
        chocoLangClasses += "|"+responseObject;
        editor.getSession().setMode("ace/mode/choco");
    });

    request.fail(function (jqXHR, textStatus, errorThrown) {
        console.log("Error while getting /getKeywords url");
    });

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

    // Event handler when modal popped up
    $('.modal').on('shown.bs.modal', function (e) {
        var myModal = $(this);
        setTimeout(function () {
            myModal.modal('hide');
        }, 5000);
    })

    // Event handler when submitting a report
    $("#reportForm").submit(function(event) {
        event.preventDefault();
        var email = $('#emailReport').val();
        var comment = $('#commentReport').val();
        sendReport(email, comment); // call the report service
        $('#reportModal').modal('hide'); // hide the report form
        $('#reportForm')[0].reset(); // clear the report form
        return false; // avoiding event to be processed
    });

    settingDragNDrop();
}

// Pick up all availables Java/Choco samples
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

        // Cleaning the drilldown
        $('#samples').removeAttr("disabled");
        $('#samples').empty();

        // Updating the drilldown
        jsonSamples.forEach(function (jsonSample) {
            var option = document.createElement("option");
            option.text = jsonSample.name;
            option.value = jsonSample.filename;
            select.append(option);
        });
    });

    request.fail(function (jqXHR, textStatus, errorThrown) {
        // Lock the samples drilldown
        $('#samples').attr("disabled", "true");
        var option = document.createElement("option");
        option.text = "No samples available";
        option.value = "0";
        select.append(option);
    });
}

// Compile the source code
function compile() {
    // Changing the 'Run' button to 'Loading'
    $('#runButton').removeClass("btn-success");
    $('#runButton').addClass("btn-warning");
    $('#runButton span').removeClass("glyphicon-play-circle");
    $('#runButton span').addClass("glyphicon-refresh");
    $('#runButton span').addClass("glyphicon-refresh-animate");

    var editor = ace.edit("editor");
    var code = editor.getSession().getValue();

    var consoleCode = document.getElementById('console');

    // Fire the HTTP POST request
    var request = $.ajax({
        url: "/compile",
        type: "post",
        data: {body: code}
    });

    // Callback handler that will be called on success - HTTP 200 OK
    request.done(function (response, textStatus, jqXHR){
        var compilationEvents = response.errors;
        var runtimeEvents = response.events;

        consoleCode.innerHTML = "";

        compilationEvents.forEach(function(compilationEvent) {
            consoleCode.innerHTML += "<pre class=\"compilationErr\">" + "Error during datas.compilation : " + compilationEvent + "</pre>";

        });

        runtimeEvents.forEach(function(runtimeEvent) {
            var className = "stdOut";
            if(runtimeEvent.kind == "stderr") {
                className = "stdErr"
            }

            consoleCode.innerHTML += "<pre class=\""+className+"\">" + runtimeEvent.message + "</pre>";

        });
    });


    // Callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown) {
        if(jqXHR.responseText == "TIMEOUT") {
            $('#alertCompileTimeout').modal('show');
        }
        else {
            $('#alertCompileFailure').modal('show');
        }
    });

    request.always(function (jqXHR, textStatus, errorThrown) {
        // Changing the 'Loading' button to 'Run'
        $('#runButton').removeClass("btn-warning");
        $('#runButton').addClass("btn-success");
        $('#runButton span').removeClass("glyphicon-refresh");
        $('#runButton span').removeClass("glyphicon-refresh-animate");
        $('#runButton span').addClass("glyphicon-play-circle");
    });
}


// Grab all relevant information and send the report using the Java service
function sendReport(userEmail, comment) {
    var editor = ace.edit("editor");
    var sourceCode = editor.getSession().getValue();

    var compilationErrs ="", stdOuts="", stdErrs="";
    $('.compilationErr').each(function() {
        compilationErrs += $(this).text() + "\n"
    });

    $('.stdOut').each(function() {
        stdOuts += $(this).text() + "\n"
    });

    $('.stdErr').each(function() {
        stdErrs += $(this).text() + "\n"
    });

    // Fire the HTTP POST request
    var request = $.ajax({
        url: "/reportError",
        type: "post",
        data: {
            sourceCode: sourceCode,
            userEmail: userEmail,
            stdOut: stdOuts,
            stdErr: stdErrs,
            compilationErr: compilationErrs,
            comment: comment
        }
    });

    // Callback handler that will be called on success - HTTP 200 OK
    request.done(function (response, textStatus, jqXHR){
        $('#alertReportSuccess').modal('show');
    });

    // Callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown){
        $('#alertReportFailure').modal('show');
    });
}


function settingDragNDrop(){


    window.ondragover = function(e){
        e.preventDefault();
        document.getElementById("header").style.filter="blur(5px)";
        document.getElementById("footer").style.filter="blur(5px)";
        document.getElementById("not-editor").style.filter="blur(5px)";
        document.getElementById("console").style.backgroundColor="lightgrey";
        document.getElementById("console").style.filter="blur(5px)";
    };

    var unblur = function () {
        document.getElementById("header").style.filter="";
        document.getElementById("footer").style.filter="";
        document.getElementById("not-editor").style.filter="";
        document.getElementById("console").style.filter="";
        document.getElementById("console").style.backgroundColor="white";
    };

    window.ondragend = function(e){
        unblur();
    };

    window.ondragleave = function(e){
        e.preventDefault();
        unblur();
    };

    document.body.ondrop = function(e){
        e.preventDefault();
        unblur();
    };

    var dropZone = document.getElementById("editor");

    dropZone.ondragover = function(e){
        e.dataTransfer.effectAllowed = 'copy';
        e.dataTransfer.dropEffect = 'copy';
        e.preventDefault();
    };

    dropZone.ondrop = function(e){
        e.preventDefault();
        var data = e.dataTransfer;
        if ('files' in data && data.files[0].type.indexOf("text")!=-1){
            var reader = new FileReader();
            reader.readAsText(e.dataTransfer.files[0]);

            reader.onloadend=function() {
                if (reader.readyState==reader.DONE) {
                    ace.edit("editor").getSession().setValue(reader.result);
                }
            };
        }else{
            console.log("No valid element dropped");
        }
    };
}

/**
 * Little routine used for performance tests
 */
function performanceTest() {
    // Getting the console UI for the report
    var consoleCode = document.getElementById('console');
    consoleCode.innerHTML = ""; // cleaning up!

    // Fetching the samples...
    jQuery.parseJSON(samples).forEach(function (jsonSample) {
        var name = jsonSample.name;
        var filename = jsonSample.filename;
        var content = jsonSample.content;

        var compilationOK = "OK";
        var runtimeOK = "OK";

        var start = new Date().getTime(); // start the timer :-)

        // Run compilation on current sample
        var request = $.ajax({
            url: "/compile",
            type: "post",
            data: {body: content}
        });

        // Callback handler that will be called on success - HTTP 200 OK
        request.done(function (response, textStatus, jqXHR){
            var compilationEvents = response.errors;
            var runtimeEvents = response.events;

            // Compilation errors
            if( $.isArray(compilationEvents) &&  (compilationEvents.length > 0) ) {
                compilationOK = "KO";
                runtimeOK = "KO";
            }

            runtimeEvents.forEach(function(runtimeEvent) {
                if(runtimeEvent.kind == "stderr") {
                    runtimeOK = "KO";
                }
            });
        });

        request.fail(function (jqXHR, textStatus, errorThrown) {
            compilationOK = "KO";
            runtimeOK = "KO";
        });


        request.complete(function () {
            var end = new Date().getTime();
            var time = end - start;
            consoleCode.innerHTML += "<pre>" + filename + " processed in " + time/1000 + " seconds. Compilation : " + compilationOK + " - Runtime : " + runtimeOK + "</pre>";
        });
    });

}