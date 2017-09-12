function removeWarning() {
     document.getElementById("error").style.visibility = "hidden";
}
function validateEmail (){
    document.getElementById("error").style.visibility = "hidden";
    var input = document.getElementById("textInput").value;
    if (input.length == 0){
        document.getElementById("error").style.visibility = "visible";
    }
    if (!input.includes("@")){
        document.getElementById("error").style.visibility = "visible";
    }
    atValue = input.indexOf("@")
    if (!input.includes(".", atValue)){
        document.getElementById("error").style.visibility = "visible";
    }
}