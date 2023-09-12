// 클릭 이벤트
$(document).ready(function() {
    // Enter key event handler
    $('#keyword').keypress(function(event) {
        if (event.which === 13) { // Enter key code
            searchReport();
        }
    });

    $('#search-bnt').click(function() {
        searchReport();
    });

    let toDateValue = $(".toDate").val();
    if (toDateValue !== "") {
        $(".toDate").val(toDateValue);
    }

    let fromDateValue = $(".fromDate").val();
    if (fromDateValue !== "") {
        $(".fromDate").val(fromDateValue);
    }

    // 페이지 로딩 후 초기 체크
    if ($("#filter").val() === "classification") {
        $("#searchDiv").css("display", "none");
        $("#selectDiv").css("display", "block");
    } else {
        $("#selectDiv").css("display", "none");
        $("#searchDiv").css("display", "block");
    }

    // 셀렉트박스 생성
    $("#filter").on("change", function() {
        if ($(this).val() === "classification") {
            $("#searchDiv").css("display", "none");
            $("#selectDiv").css("display", "block");
        } else {
            $("#selectDiv").css("display", "none");
            $("#searchDiv").css("display", "block");
        }
    });

    // 조회 날짜
    $("#week").click(function () {
        setDateRange(7);  // 7일(1주일) 전
    });

    $("#month").click(function () {
        setDateRange(30);  // 30일(1달) 전
    });

    $("#3months").click(function () {
        setDateRange(90);  // 90일(3달) 전
    });

    function setDateRange(days) {
        let today = new Date();  // 오늘 날짜
        let pastDate = new Date(today.getTime() - days * 24 * 60 * 60 * 1000);  // 주어진 일수 전 날짜

        let toDate = formatDate(today);  // 오늘 날짜를 yyyy-MM-dd 형식으로 포맷
        let fromDate = formatDate(pastDate);  // 주어진 일수 전 날짜를 yyyy-MM-dd 형식으로 포맷

        $(".toDate").val(toDate);  // toDate 필드에 값을 설정
        $(".fromDate").val(fromDate);  // fromDate 필드에 값을 설정
    }

    function formatDate(date) {
        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, '0');
        let day = String(date.getDate()).padStart(2, '0');

        return `${year}-${month}-${day}`;
    }
});
function searchReport() {
    let filter = encodeURIComponent($('#filter').val());
    let keyword = encodeURIComponent($('#keyword').val());
    let toDate = encodeURIComponent($('.toDate').val());
    let fromDate = encodeURIComponent($('.fromDate').val());

    window.location.href = '/admin/reports?filter=' + filter + '&keyword=' + keyword
        + '&fromDate=' + fromDate + '&toDate=' + toDate;
}

// 페이지 이동
function navigateToPage(page) {
    let filter = $('#filter').val();
    let keyword = $('#keyword').val();
    let toDate = $('.toDate').val();
    let fromDate = $('.fromDate').val();
    let hidden = $('#hidden').val();

    if(hidden != null) {
        filter = encodeURIComponent(filter);
        hidden = encodeURIComponent(hidden);
        toDate = encodeURIComponent(toDate);
        fromDate = encodeURIComponent(fromDate);
        location.href = '/admin/reports?page=' + page + '&filter=' + filter + '&keyword=' + hidden + '&fromDate=' + fromDate + '&toDate=' + toDate;
    } else if(keyword != null){
        filter = encodeURIComponent(filter);
        keyword = encodeURIComponent(keyword);
        location.href = '/admin/reports?page=' + page + '&filter=' + filter + '&keyword=' + keyword + '&fromDate=' + fromDate + '&toDate=' + toDate;
    } else {
        location.href = '/admin/reports?page=' + page;
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
// qna삭제
function deleteReportList() {
    let selectedItems = [];
    // 동작중일때 중복으로 동작하지 않도록
    if(isRun === true) {
        return;
    }
    isRun = true;

    $(".checkbox:checked").each(function () {
        let id = $(this).val();
        selectedItems.push({id: id});
    });

    if (confirm("삭제하시겠습니까??") && selectedItems.length > 0) {
        const accessToken = localStorage.getItem("accessToken");
        tokenCheck(accessToken);
        $.ajax({
            url: "/admin/reports/delete-list",
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