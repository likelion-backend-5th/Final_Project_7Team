document.addEventListener('DOMContentLoaded', async function loadPage() {
    const body = document.getElementsByTagName("body");
    body[0].style.display = "none";

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };

    fetch('/mypage/order-list', {
        method: 'GET',
        headers: headers,
    })
        .then(response => {
        if (response.ok) {
            return response.json(); // JSON 데이터 파싱
        } else {
            alert("데이터를 가져오지 못했습니다.");
        }
    })
        .then(data => {
            // JSON 데이터를 기반으로 화면 동적 업데이트
            const orderListTableBody = document.getElementById('orderListTableBody');
            orderListTableBody.innerHTML = ''; // 기존 데이터 초기화

            data.forEach(order => {
                const row = document.createElement('tr');
                // 여기에서 필요한 데이터를 가져와서 테이블 행을 구성합니다.
                // 예를 들어, row.innerHTML = '<td>' + order.orderAt + '</td>' 와 같은 방식으로 데이터를 추가할 수 있습니다.
                // 이 과정을 반복하여 모든 주문 데이터를 추가합니다.
                orderListTableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('오류 발생:', error);
        });
})