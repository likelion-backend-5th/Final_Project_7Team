// qna삭제
async function deleteConsultation() {
    let selectedItems = [];

    let id = $("#consulId").val();
    selectedItems.push({id: id});

    if (selectedItems.length > 0) {
        const accessToken = localStorage.getItem("accessToken");
        const headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        };
        try {
            const response = await fetch('/admin/consultations/delete-list', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(selectedItems),
            });
            if (response.ok) {
                alert('삭제되었습니다.');
                window.location.href = "/admin/consultations";
            } else if (response.status === 401 || response.status === 403) {
                const reissueResponse = await fetch('/reissue', {
                    method: 'POST',
                    credentials: 'same-origin' // 쿠키를 보내기 위해 필수
                });
                if (reissueResponse.ok) {
                    const reissuedData = await reissueResponse.json();
                    localStorage.setItem('accessToken', reissuedData.accessToken);
                    await deleteConsultation(); // 원래 요청을 다시 시도
                } else {
                    alert('토큰 재발급 실패');
                }
            }
        } catch (error) {
            alert("오류 발생!!");
        }
    }
}

async function updateAnswer() {
    let consulId = parseInt($('#consulId').val());
    let answer = $('.answer').val();

    if(answer) {
        const requestBody = {
            answer: answer
        };
        const accessToken = localStorage.getItem("accessToken");
        const headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        };
        try {
            const response = await fetch(`/admin/consultations/${consulId}/update-answer`, {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(requestBody)
            });
            if (response.ok) {
                alert('등록되었습니다.');
                window.location.href = "/admin/consultations";
            } else if (response.status === 401 || response.status === 403) {
                const reissueResponse = await fetch('/reissue', {
                    method: 'POST',
                    credentials: 'same-origin' // 쿠키를 보내기 위해 필수
                });
                if (reissueResponse.ok) {
                    const reissuedData = await reissueResponse.json();
                    localStorage.setItem('accessToken', reissuedData.accessToken);
                    await updateAnswer(); // 원래 요청을 다시 시도
                } else {
                    alert('토큰 재발급 실패');
                }
            }
        } catch (error) {
            alert("오류 발생!!");
        }
    } else {
        alert("답변이 작성되지 않았습니다. 다시 작성해주세요.")
    }
}