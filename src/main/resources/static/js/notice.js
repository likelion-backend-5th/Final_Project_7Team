let isRun = false;

function createNotice() {
    // 동작중일때 중복으로 동작하지 않도록
    if(isRun === true) {
        return;
    }
    isRun = true;

    let title = $("#title").val();
    let content = $("#content").val();
    const accessToken = localStorage.getItem("accessToken");

    let param = {"title":title, "content":content}

    if(confirm("등록하시겠습니까??")){
        $.ajax({
            type:'POST',
            url:"/admin/notices/create",
            contentType: "application/json",
            dataType: "text",
            data: JSON.stringify(param),
            headers: {
                "Authorization": `Bearer ${accessToken}`
            },
            success : function(data) {
                alert("등록되었습니다.");
                window.location.href="/admin/notices";
                isRun = false;
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("ERROR : " + textStatus + " : " + errorThrown);
                window.location.href = "/admin/error-page/500";
            }
        });
    }
}

function modifyNotice() {
    // 동작중일때 중복으로 동작하지 않도록
    if(isRun === true) {
        return;
    }
    isRun = true;

    let noticeId = $("#noticeId").val();
    let title = $("#title").val();
    let content = $("#content").val();
    const accessToken = localStorage.getItem("accessToken");

    let param = {"title":title, "content":content}

    if (confirm("수정하시겠습니까??")) {
        $.ajax({
            type: 'POST',
            url: `/admin/notices/${noticeId}/modify`,
            contentType: "application/json",
            dataType: "text",
            data: JSON.stringify(param),
            headers: {
                "Authorization": `Bearer ${accessToken}`
            },
            success: function (data) {
                alert("수정되었습니다.");
                window.location.href = "/admin/notices";
                isRun = false;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("ERROR : " + textStatus + " : " + errorThrown);
                window.location.href = "/admin/error-page/500";
            }
        });
    }
}

function deleteNotice() {
    let selectedItems = [];
    const accessToken = localStorage.getItem("accessToken");

    // 동작중일때 중복으로 동작하지 않도록
    if(isRun === true) {
        return;
    }
    isRun = true;

    let id = $("#consulId").val();
    selectedItems.push({id: id});

    if (confirm("삭제하시겠습니까??") && selectedItems.length > 0) {
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
                window.location.href = "/admin/notices";
                isRun = false;
            },
            error: function (error) {
                // 오류 처리 동작
                window.location.href = "/admin/error-page/500";
            }
        });
    }
}

