let isRun = false;
// qna삭제
function deleteConsultation() {
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
            url: "/admin/consultations/delete-list",
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
                window.location.href = "/admin/consultations"; // 페이지 새로고침
            },
            error: function (error) {
                // 오류 처리 동작
                window.location.href = "/admin/error-page/500";
            }
        });
    }
}

function updateAnswer() {
    let consulId = parseInt($('#consulId').val());
    let answer = encodeURIComponent($('.answer').val());
    const accessToken = localStorage.getItem("accessToken");


    if(answer) {
        if (confirm("등록하시겠습니까??")) {
            $.ajax({
                url: `/admin/consultations/${consulId}/update-answer`,
                type: "POST",
                dataType: "text",
                headers: {
                    "Authorization": `Bearer ${accessToken}`
                },
                data: "answer=" + answer, // 인코딩된 데이터를 전송
                success: function (response) {
                    // 성공적으로 처리된 경우의 동작
                    alert("등록되었습니다.")
                    window.location.href = "/admin/consultations";
                },
                error: function (error) {
                    // 오류 처리 동작
                    window.location.href = "/admin/error-page/500";
                }
            })
        }
    } else {
        alert("답변이 작성되지 않았습니다. 다시 작성해주세요.")
    }
}