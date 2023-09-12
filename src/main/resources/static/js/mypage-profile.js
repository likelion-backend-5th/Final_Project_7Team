document.addEventListener('DOMContentLoaded', async function loadProfilePage() {
    // const body = document.getElementsByTagName("body");
    // body[0].style.display = "none";

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };
    let data ={};

    try {
        const response = await fetch('/mypage/profile/data', {
            method: 'GET',
            headers: headers,
        });

        if (response.ok) {
            data =  await response.json();
            console.log("data",data)
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await loadProfilePage(); // 원래 요청을 다시 시도
            } else {
                alert('권한이 없습니다.');
                window.location.href = "/login";
            }
        } else {
            alert('권한이 없습니다.');
            window.location.href = "/login";
        }

    } catch (error) {
        console.error('오류:', error);
        alert('권한이 없습니다.');
        window.location.href = "/login";
    }

    dataOnPage(data);
});

function dataOnPage(data) {
    const profile = data['profile'];

    document.querySelector('.loginId').innerHTML = profile.loginId
    document.querySelector('.name').innerHTML = profile.name
    document.querySelector('.nickname').innerHTML = profile.nickname
    document.querySelector('.email').innerHTML = profile.email
    document.querySelector('.phone').innerHTML = profile.phone
}