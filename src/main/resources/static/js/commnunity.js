// 클릭 이벤트
$(document).ready(function() {
    // Enter key event handler
    $('#keyword').keypress(function (event) {
        if (event.which === 13) { // Enter key code
            searchCommunities();
        }
    });

    $('#search-bnt').click(function () {
        searchCommunities();
    });

    function searchCommunities() {
        let filter = encodeURIComponent($('#filter').val());
        let keyword = encodeURIComponent($('#keyword').val());

        window.location.href = '/admin/communities?filter=' + filter + '&keyword=' + keyword;
    }

    // 페이지 이동
    function navigateToPage(page) {
        let filter = $('#filter').val();
        let keyword = $('#keyword').val();
        let hidden = $('#hidden').val();

        if (hidden != null) {
            filter = encodeURIComponent(filter);
            hidden = encodeURIComponent(hidden);
            location.href = '/admin/communities?page=' + page + '&filter=' + filter + '&keyword=' + hidden;
        } else if (keyword != null) {
            filter = encodeURIComponent(filter);
            keyword = encodeURIComponent(keyword);
            location.href = '/admin/communities?page=' + page + '&filter=' + filter + '&keyword=' + keyword;
        } else {
            location.href = '/admin/communities?page=' + page;
        }
    }

    // 체크 박스 선택
    $("#selectAll").click(function () {
        if ($("#selectAll").prop("checked")) {
            $(".checkbox").prop("checked", true)
        } else {
            $(".checkbox").prop("checked", false)
        }
    });

    $(".checkbox").click(function () {
        if ($(".checkbox").prop("checked").length !== $(".checkbox").length) {
            $("#selectAll").prop("checked", false)
        } else {
            $("#selectAll").prop("checked", true)
        }
    });

    // 상태 일괄변경
    function updateOrderStatus() {
        let selectedItems = [];

        $(".checkbox:checked").each(function () {
            let id = parseInt($(this).val());
            let status = $(this).closest("tr").find(".orderStatus").val();
            selectedItems.push({id: id, status: status});
        });

        if (selectedItems.length > 0) {
            // 선택된 상품의 상태값 변경 요청
            $.ajax({
                url: "/admin/orders/change-status",
                type: "POST",
                contentType: "application/json",
                dataType: "text",
                data: JSON.stringify(selectedItems),
                success: function (response) {
                    // 성공적으로 처리된 경우의 동작
                    alert("변경되었습니다.")
                    window.location.reload(); // 페이지 새로고침
                },
                error: function (error) {
                    // 오류 처리 동작
                    window.location.href = "/admin/error-page/500";
                }
            });
        }
    }
});