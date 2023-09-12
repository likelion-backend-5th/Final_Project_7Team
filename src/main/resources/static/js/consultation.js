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
            body[0].style.display = "block";
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

    consultationPage(0)

});

async function tokenCheck(accessToken) {

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
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await tokenCheck(reissuedData.accessToken); // 원래 요청을 다시 시도
            } else {
                alert('권한이 없습니다.2');
                window.location.href = "/login";
            }
        } else {
            alert('권한이 없습니다.3');
            window.location.href = "/login";
        }
    } catch (error) {
        console.error('오류:', error);
        alert('권한이 없습니다.4');
        window.location.href = "/login";
    }
}

function consultationPage(pageNumber, filter, keyword) {
    // Ajax 요청을 보낼 URL 설정
    let url = '/notices/consultations?page=' + pageNumber;

    const accessToken = localStorage.getItem("accessToken");

    // 검색 조건이 있을 경우 추가
    if (filter) {
        url += '&filter=' + filter;
    }
    if (keyword) {
        url += '&keyword=' + keyword;
    }

    $.ajax({
        url: url,
        method: 'GET',
        contentType: "application/json",
        headers: {
            "Authorization": `Bearer ${accessToken}`
        },
        success: function (data) {
            // 성공적으로 데이터를 받아왔을 때 페이지 업데이트
            updatePage(data);
            createPagination(data)
        },
        error: function () {
            alert('데이터를 불러오는 중 오류가 발생했습니다.');
        }
    });
}

function updatePage(data) {
    if(data.content) {
        // 테이블의 tbody 선택
        let tbody = $('.notice-table tbody');

        // tbody 초기화 (기존 데이터 삭제)
        tbody.empty();

        let tr = $('<tr>');
        tr.append('<th style="width: 5%">'+ '번호' + '</th>');
        tr.append('<th style="width: 10%">'+ '분류' + '</th>');
        tr.append('<th style="width: 50%">'+ '제목' + '</th>');
        tr.append('<th style="width: 10%">'+ '작성자' + '</th>');
        tr.append('<th style="width: 10%">'+ '상태' + '</th>');
        tr.append('<th style="width: 15%">'+ '작성일' + '</th>');

        // tbody에 행 추가
        tbody.append(tr);

        // consultationList의 각 항목을 테이블에 추가
        data.content.forEach(function (consultation) {
            const formattedCreatedAt = formatDateTime(consultation.createdAt);
            let classificationLabel = '';
            switch (consultation.classification) {
                case 'DELIVERY_INQUIRY':
                    classificationLabel = '배송문의';
                    break;
                case 'ORDER_INQUIRY':
                    classificationLabel = '주문문의';
                    break;
                case 'EXCHANGE_REQUESTED':
                    classificationLabel = '교환신청';
                    break;
                case 'REFUND_REQUESTED':
                    classificationLabel = '환불신청';
                    break;
                default:
                    classificationLabel = '기타문의';
            }

            let tr = $('<tr>');
            tr.append('<td>' + consultation.id + '</td>');
            tr.append('<td>' + classificationLabel + '</td>');
            tr.append('<td><a href="/notices/consultations/' + consultation.id + '"><span>' + consultation.subject + '</span></a></td>');
            tr.append('<td>' + consultation.writer + '</td>');
            if (consultation.answeredAt) {
                tr.append('<td>답변완료</td>');
            } else {
                tr.append('<td>답변대기</td>');
            }
            tr.append('<td>' + formattedCreatedAt + '</td>');

            // tbody에 행 추가
            tbody.append(tr);
        });
    } else {
        let tbody = $('.notice-table tbody');
        let tr = $('<tr>');
        tr.append('<td>' + '내역이 존재하지 않습니다.' + '</td>');
        tbody.append(tr);
    }
}

function createPagination(page) {
    let pageNationContainer = $("#pagination");

    // 페이지네이션 컨테이너에 추가
    pageNationContainer.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur+1)/10.0)*10; // 1~10
    let startPage = endPage-9; // 1~10
    if(endPage>page.totalPages-1) // totalPage는 1부터 센다 그래서 1을 빼줌
        endPage=page.totalPages;

    if(cur > 0) // 이전 버튼
        pageNationContainer.append(`<li class="mr-3"><a onclick="consultationPage(${cur - 1})"> < </a></li>`);

    for(let i=startPage; i <= endPage; i++) { // 페이지네이션
        pageNationContainer.append(`<li class="mr-3"><a onclick="consultationPage(${i - 1})"> ${i} </a></li>`);
    }
    if(cur + 1 <page.totalPages) // 다음 버튼
        pageNationContainer.append(`<li class="mr-3"><a onclick="consultationPage(${cur + 1})"> > </a></li>`);
    pageNationContainer.append(`<input type="hidden" id="cur" value="${cur}">`)

}

function searchNotice() {
    // let filter = document.getElementById('filter').value;
    let keyword = document.getElementById('keyword').value;
    consultationPage(0,"title", keyword);
}

function createConsultation() {
    const accessToken = localStorage.getItem("accessToken");

    // 폼 데이터 수집
    const subject = $("#subject").val();
    const classification = $("input[name='classification']:checked").val();
    const content = $("#content").val();

    // JSON 데이터 생성
    const requestData = {
        subject: subject,
        classification: classification,
        content: content
    };

    if(subject && classification && content) {
            if(confirm("등록하시겠습니까??")) {
                tokenCheck(accessToken);
                $.ajax({
                    url: '/notices/consultations',
                    method: 'POST',
                    contentType: "application/json",
                    headers: {
                        "Authorization": `Bearer ${accessToken}`
                    },
                    data: JSON.stringify(requestData),
                    success: function (data) {
                        alert("등록되었습니다.")
                        window.location.href = '/notices/consultations-form';
                    },
                    error: function () {
                        alert("오류가 발생했습니다.")
                    }
                });
        }
    } else {
        alert("입력되지 않은 필드가 있습니다. 다시 확인해주세요.")
    }
}

function formatDateTime(dateTimeString) {
    const dateTime = new Date(dateTimeString);
    const year = dateTime.getFullYear();
    const month = String(dateTime.getMonth() + 1).padStart(2, '0');
    const day = String(dateTime.getDate()).padStart(2, '0');
    const hours = String(dateTime.getHours()).padStart(2, '0');
    const minutes = String(dateTime.getMinutes()).padStart(2, '0');
    const seconds = String(dateTime.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
