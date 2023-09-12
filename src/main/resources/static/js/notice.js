async function createNotice() {

    let title = $("#title").val();
    let content = $("#content").val();

    let param = {"title":title, "content":content}

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };
    try {
        const response = await fetch(`/admin/notices/create`, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(param),
        });
        if (response.ok) {
            alert("등록되었습니다.");
            window.location.href="/admin/notices";
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });
            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await createNotice(); // 원래 요청을 다시 시도
            } else {
                alert('토큰 재발급 실패');
            }
        }
    } catch (error) {
        alert("오류 발생!!");
    }
}

async function modifyNotice() {

    let noticeId = $("#noticeId").val();
    let title = $("#title").val();
    let content = $("#content").val();
    let param = {"title":title, "content":content}

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };
    try {
        const response = await fetch(`/admin/notices/${noticeId}/modify`, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(param),
        });
        if (response.ok) {
            alert("수정되었습니다.");
            window.location.href = "/admin/notices";
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });
            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await modifyNotice(); // 원래 요청을 다시 시도
            } else {
                alert('토큰 재발급 실패');
            }
        }
    } catch (error) {
        alert("오류 발생!!");
    }
}

async function deleteNotice() {
    let selectedItems = [];

    let id = $("#consulId").val();
    selectedItems.push({id: id});

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
                    await deleteNotice(); // 원래 요청을 다시 시도
                } else {
                    alert('토큰 재발급 실패');
                }
            }
        } catch (error) {
            alert("오류 발생!!");
        }
    }
}

