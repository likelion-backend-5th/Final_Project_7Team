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


// notice 삭제
async function deleteNoticeList() {
    let selectedItems = [];

    $(".checkbox:checked").each(function () {
        let id = $(this).val();
        selectedItems.push({id: id});
    });

    if(selectedItems.length > 0) {
        const accessToken = localStorage.getItem("accessToken");
        const headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        };
        try {
            const response = await fetch("/admin/notices/delete-list", {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(selectedItems),
            });
            if (response.ok) {
                alert("삭제되었습니다.")
                window.location.href = "/admin/notices";
            } else if (response.status === 401 || response.status === 403) {
                const reissueResponse = await fetch('/reissue', {
                    method: 'POST',
                    credentials: 'same-origin' // 쿠키를 보내기 위해 필수
                });
                if (reissueResponse.ok) {
                    const reissuedData = await reissueResponse.json();
                    localStorage.setItem('accessToken', reissuedData.accessToken);
                    await deleteNoticeList(); // 원래 요청을 다시 시도
                } else {
                    alert('토큰 재발급 실패');
                }
            }
        } catch (error) {
            alert("오류 발생!!");
        }
    }
}