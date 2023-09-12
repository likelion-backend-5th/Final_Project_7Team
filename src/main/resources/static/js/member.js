// 클릭 이벤트
$(document).ready(function() {
    // Enter key event handler
    $('#keyword').keypress(function(event) {
        if (event.which === 13) { // Enter key code
            searchMembers();
        }
    });
});

function searchMembers() {
    let filter = $('#filter').val();
    let keyword = $('#keyword').val();
    // Redirect to the new URL to reload the page
    window.location.href = '/admin/members?filter=' + encodeURIComponent(filter) + '&keyword=' + encodeURIComponent(keyword);
}

// 페이지 이동
function navigateToPage(page) {
    let filter = $('#filter').val();
    let keyword = $('#keyword').val();
    let hidden = $('#hidden').val();

    if(hidden != null) {
        filter = encodeURIComponent(filter);
        hidden = encodeURIComponent(hidden);
        location.href = '/admin/members?page=' + page + '&filter=' + filter + '&keyword=' + hidden;
    } else if(keyword != null){
        filter = encodeURIComponent(filter);
        keyword = encodeURIComponent(keyword);
        location.href = '/admin/members?page=' + page + '&filter=' + filter + '&keyword=' + keyword;
    } else {
        location.href = '/admin/members?page=' + page;
    }
}

// 회원 삭제
function deleteMember(memberId) {
    const accessToken = localStorage.getItem("accessToken");

    alert(memberId)
    if(confirm("삭제하시겠습니까?")) {
        $.ajax({
            url: `/admin/members/${memberId}/delete`,
            method: "POST",
            headers: {
                "Authorization": `Bearer ${accessToken}`
            },
            success: function (response) {
                alert("삭제되었습니다.")
                // 성공적으로 삭제된 경우 처리
                window.location.href = "/admin/members"; // 페이지 새로고침
            },
            error: function () {
                // 삭제 실패 또는 오류 발생 시 처리
                window.location.href = "/admin/error-page/500";
            }
        });
    }
}