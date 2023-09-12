// 클릭 이벤트
$(document).ready(function() {
    // Enter key event handler
    $('#keyword').keypress(function (event) {
        if (event.which === 13) { // Enter key code
            searchNotice();
        }
    });

    $('#search-bnt').click(function () {
        searchNotice();
    });
})

function searchNotice() {
    let filter = encodeURIComponent($('#filter').val());
    let keyword = encodeURIComponent($('#keyword').val());

    window.location.href = '/admin/notices?filter=' + filter + '&keyword=' + keyword;

}

// 페이지 이동
function navigateToPage(page) {
    let filter = $('#filter').val();
    let keyword = $('#keyword').val();
    let hidden = $('#hidden').val();

    if(hidden != null) {
        filter = encodeURIComponent(filter);
        hidden = encodeURIComponent(hidden);
        location.href = '/admin/notices?page=' + page + '&filter=' + filter + '&keyword=' + hidden;
    } else if(keyword != null){
        filter = encodeURIComponent(filter);
        keyword = encodeURIComponent(keyword);
        location.href = '/admin/notices?page=' + page + '&filter=' + filter + '&keyword=' + keyword;
    } else {
        location.href = '/admin/notices?page=' + page;
    }
}

// 체크 박스 선택
$("#selectAll").click(function () {
    if($("#selectAll").prop("checked")) {
        $(".checkbox").prop("checked", true)
    } else {
        $(".checkbox").prop("checked", false)
    }
});

$(".checkbox").click(function () {
    if($(".checkbox").prop("checked").length !== $(".checkbox").length) {
        $("#selectAll").prop("checked", false)
    } else {
        $("#selectAll").prop("checked", true)
    }
});


let isRun = false;
// notice 삭제
function deleteNoticeList() {
    let selectedItems = [];
    const accessToken = localStorage.getItem("accessToken");
    // 동작중일때 중복으로 동작하지 않도록
    if(isRun === true) {
        return;
    }
    isRun = true;

    $(".checkbox:checked").each(function () {
        let id = $(this).val();
        selectedItems.push({id: id});
    });

    if (confirm("선택된 글을 삭제하시겠습니까??") && selectedItems.length > 0) {
        $.ajax({
            url: "/admin/notices/delete-list",
            type: "POST",
            contentType: "application/json",
            dataType: "text",
            headers: {
                "Authorization": `Bearer ${accessToken}`
            },
            data: JSON.stringify(selectedItems),
            success: function (response) {
                // 성공적으로 처리된 경우의 동작
                alert("삭제되었습니다.")
                window.location.reload(); // 페이지 새로고침
            },
            error: function (error) {
                // 오류 처리 동작
                window.location.href = "/admin/error-page/500";
            }
        });
    }
}