$(document).ready(function() {
    $("#logoutLink").on("click", function(e) {
        e.preventDefault();
        document.logoutForm.submit();
    });

    $(".link-delete").on("click", function(e) {
        e.preventDefault();
        $("#yesButton").attr("href", $(this).attr("href"));
        $("#confirmModal").modal();
    });

});