$(document).ready(function () {
    // Enter key event handler
    $('#keyword').keypress(function (event) {
        if (event.which === 13) { // Enter key code
            searchNotice();
        }
    });

    $('#searchBtn').click(function () {
        searchNotice();
    });
});

function searchNotice() {
   // let filter = $("#filter").val();
    let keyword = $("#keyword").val();
    window.location.href = '/notices?filter=' + encodeURIComponent("title") + '&keyword=' + encodeURIComponent(keyword);
}