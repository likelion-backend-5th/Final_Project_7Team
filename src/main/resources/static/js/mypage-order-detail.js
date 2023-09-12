document.addEventListener('DOMContentLoaded', async function loadPointPage() {

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };
    let data ={};

    try {
        const response = await fetch('/mypage/point/data', {
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
                await loadPointPage(); // 원래 요청을 다시 시도
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
    const totalPointElement = document.querySelector('.point-total span');
    totalPointElement.textContent = data.pointList.content[0].totalPoint;

    const pointList = data.pointList.content;
    const pointTable = document.querySelector('.point-table tbody');

    pointList.forEach((point) => {
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
          <td>${point.status}</td>
          <td>${point.point}</td>
          <td>${point.pointSource}</td>
          <td>${point.orderNumber}</td>
          <td>${formatDate(point.usedAt)}</td>
        `;

        pointTable.appendChild(newRow);
    })
}

function formatDate(inputDate) {
    const date = new Date(inputDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}