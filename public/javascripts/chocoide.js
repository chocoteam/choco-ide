window.onload = function() {
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.getSession().setMode("ace/mode/java");
}

function compile() {
    window.alert("Button clicked!")
}