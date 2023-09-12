let isRun = false;
function deleteReport() {
    let selectedItems = [];

    // 동작중일때 중복으로 동작하지 않도록
    if(isRun === true) {
        return;
    }
    isRun = true;

    let id = $("#consulId").val();
    selectedItems.push({id: id});

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
                window.location.href = "/admin/reports"; // 페이지 새로고침
            },
            error: function (error) {
                // 오류 처리 동작
                window.location.href = "/admin/error-page/500";
            }
        });
    }
}

function processReport() {
    let reportId = parseInt($('#reportId').val());

    if(confirm("처리하시겠습니까??")) {
        const accessToken = localStorage.getItem("accessToken");
        tokenCheck(accessToken);
        $.ajax({
            url: `/admin/reports/${reportId}/processed`,
            type: "POST",
            dataType: "text",
            headers: {
                "Authorization": `Bearer ${accessToken}`
            },
            success: function (response) {
                // 성공적으로 처리된 경우의 동작
                alert("처리되었습니다.")
                window.location.href = "/admin/reports";
            },
            error: function (error) {
                // 오류 처리 동작
                window.location.href = "/admin/error-page/500";
            }
        })
    }
}
