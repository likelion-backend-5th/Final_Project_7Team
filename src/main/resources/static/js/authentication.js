document.addEventListener('DOMContentLoaded', async function loadPage() {
    const body = document.getElementsByTagName("body");
    body[0].style.display = "none";

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };

    try {
        const response = await fetch('/authorize', {
            method: 'GET',
            headers: headers,
        });

        if (response.ok) {
            const responseData = await response.json();
            if(responseData.role === "ADMIN") {
                body[0].style.display = "block";
            } else {
                alert('권한이 없습니다.');
                window.location.href = "/";
            }
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await loadPage(); // 원래 요청을 다시 시도
            } else {
                alert('권한이 없습니다.');
                window.location.href = "/";
            }
        } else {
            alert('권한이 없습니다.');
            window.location.href = "/";
        }
    } catch (error) {
        console.error('오류:', error);
        alert('권한이 없습니다.');
        window.location.href = "/";
    }
});

async function logout() {
    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };

    try {
        const response = await fetch('/signout', {
            method: 'GET',
            headers: headers,
        });

        if (response.ok) {
            localStorage.removeItem('accessToken');
            window.location.href = '/';
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });
            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await logout(); // 원래 요청을 다시 시도
            } else {
                localStorage.removeItem('accessToken');
                window.location.href = '/';
            }
        } else {
            localStorage.removeItem('accessToken');
            window.location.href = '/';
        }
    } catch (error) {
        console.error('오류:', error);
        localStorage.removeItem('accessToken');
        window.location.href = '/';
    }
}
