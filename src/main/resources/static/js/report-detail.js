async function deleteReport() {
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
            const response = await fetch("/admin/reports/delete-list", {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(selectedItems),
            });
            if (response.ok) {
                alert("삭제되었습니다.")
                window.location.href = "/admin/reports";
            } else if (response.status === 401 || response.status === 403) {
                const reissueResponse = await fetch('/reissue', {
                    method: 'POST',
                    credentials: 'same-origin' // 쿠키를 보내기 위해 필수
                });
                if (reissueResponse.ok) {
                    const reissuedData = await reissueResponse.json();
                    localStorage.setItem('accessToken', reissuedData.accessToken);
                    await deleteReport(); // 원래 요청을 다시 시도
                } else {
                    alert('토큰 재발급 실패');
                }
            }
        } catch (error) {
            alert("오류 발생!!");
        }
    }
}

async function processReport() {
    let reportId = parseInt($('#reportId').val());

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };
    try {
        const response = await fetch(`/admin/reports/${reportId}/processed`, {
            method: 'POST',
            headers: headers,
        });
        if (response.ok) {
            alert("처리되었습니다.")
            window.location.href = "/admin/reports";
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });
            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await processReport(); // 원래 요청을 다시 시도
            } else {
                alert('토큰 재발급 실패');
            }
        }
    } catch (error) {
        alert("오류 발생!!");
    }
}
