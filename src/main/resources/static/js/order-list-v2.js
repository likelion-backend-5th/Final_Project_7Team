document.addEventListener('DOMContentLoaded', async function loadPage() {
    const body = document.getElementsByTagName("body");
    body[0].style.display = "none";

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };

    const params = new URLSearchParams(window.location.search);
    const orderStatus = params.get("orderStatus");
    let status= orderStatus === null ? "" : orderStatus;

    let data ={};

    try {
        const response = await fetch(`/mypage/order-list/data?orderStatus=${status}`, {
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

    dataOnPage(data);
})

function filterOrderStatus(orderStatus) {
    if (orderStatus === 'ALL') {
        location.href = "/mypage/order-list"
    } else {
        location.href = "/mypage/order-list?orderStatus=" + orderStatus;
    }
}
function dataOnPage(data) {
    const orderList = data.orderList.content;

    const orderStatusCount = data.orderStatusCount;

    const orderStatusList = document.getElementById('orderStatusList');
    // 반복문 사용하여 <li> 요소를 생성하고 추가
    for (const status in orderStatusCount) {
        const count = orderStatusCount[status];
        let statusName = ""
        switch (status) {
            case "RECEIVED":
                statusName = "주문접수"
                break;
            case "PREPARING_FOR_SHIPMENT":
                statusName = "배송준비"
                break;
            case "SHIPPED":
                statusName = "배송중"
                break;
            case "DELIVERED":
                statusName = "배송완료"
                break;
            case "PURCHASE_CONFIRMED":
                statusName = "구매확정"
                break;
            case "EXCHANGE_REQUESTED":
                statusName = "교환요청"
                break;
            case "EXCHANGE_COMPLETED":
                statusName = "교환완료"
                break;
            case "REFUND_REQUESTED":
                statusName = "환불요청"
                break;
            case "REFUND_COMPLETED":
                statusName = "환불완료"
                break;
        }
        const li = document.createElement('li');
        li.textContent = `${statusName} ${count}`;
        li.addEventListener('click', function () {
            filterOrderStatus(status);
        });
        orderStatusList.appendChild(li);
    }



    //테이블 생성
    const tbody = document.querySelector('tbody')

    for (let i = 0; i < orderList.length; i++) {
        const newTr = document.createElement('tr');
        const order = orderList[i]; // 각 주문 데이터에 접근

        const orderAtTd = document.createElement('td');
        orderAtTd.textContent = formatDate(order.orderAt);

        //--------------------주문번호 ---------------------------
        const orderNumberTd = document.createElement('td');
        // orderNumberTd.textContent = order.orderNumber;
        const link = document.createElement('a');
        link.href = `/mypage/order-detail/${order.id}`;
        link.textContent = order.orderNumber;
        orderNumberTd.appendChild(link);

        //--------------------주문번호 ---------------------------

        //--------------------상품정보 ---------------------------
        const nameTd = document.createElement('td');
        const productInfoBox = document.createElement('div');
        productInfoBox.classList.add('product-info-box');

        const alink = document.createElement('a');
        alink.classList.add('img-box');
        // alink.setAttribute("id","ajklsdj")
        alink.href = `/products/detail/${order.productId}`;

        const img = document.createElement('img');
        img.src = order.fileUrl;

        const contentBox = document.createElement('div');
        contentBox.classList.add('content-box');
        const nameLink = document.createElement('a');
        nameLink.textContent = order.name;
        nameLink.href = `/products/detail/${order.productId}`;
        const optionDiv = document.createElement('div');
        optionDiv.textContent = `옵션 : ${order.size} / ${order.color}`;

        alink.appendChild(img);
        contentBox.appendChild(nameLink);
        contentBox.appendChild(optionDiv);
        productInfoBox.appendChild(alink);
        productInfoBox.appendChild(contentBox);
        nameTd.appendChild(productInfoBox);

        //--------------------상품정보 ---------------------------

        const priceTd = document.createElement('td');
        priceTd.textContent = `${order.price}(${order.quantity}개)`;

        const orderStatusTd = document.createElement('td');
        const orderStatusBox = document.createElement('div');
        orderStatusTd.appendChild(orderStatusBox)
        orderStatusBox.classList.add('order-status-box')

        const orderStatusChildBox1 = document.createElement('div');
        const orderStatusChildBox2 = document.createElement('div');
        orderStatusBox.appendChild(orderStatusChildBox1)
        orderStatusBox.appendChild(orderStatusChildBox2)

        orderStatusChildBox1.textContent = order.orderStatus

        if(order.orderStatus == "주문접수" || order.orderStatus == "배송준비"){
            // const button = document.createElement('button');
            // button.addEventListener('click', function () {
            //     handleOrderAction(order.opId,"주문취소");
            // });
            // button.textContent = "주문 취소"
            // orderStatusTd.appendChild(button)
        }

        if(order.orderStatus == "배송중" || order.orderStatus == "배송완료"){
            const button1 = document.createElement('button');
            button1.addEventListener('click', function () {
                handleOrderAction(order.opId,"구매확정");
            });
            button1.textContent = "구매 확정"
            orderStatusChildBox2.appendChild(button1)

            const button2 = document.createElement('button');
            button2.addEventListener('click', function () {
                handleOrderAction(order.opId,"교환요청");
            });
            button2.textContent = "교환 요청"
            orderStatusChildBox2.appendChild(button2)

            const button3 = document.createElement('button');
            button3.addEventListener('click', function () {
                handleOrderAction(order.opId,"환불요청");
            });
            button3.textContent = "환불 요청"
            orderStatusChildBox2.appendChild(button3)
        }

        if(order.orderStatus == "구매확정"){
            if(order.writeReview == false){
                const button = document.createElement('button');
                button.addEventListener('click', function () {
                    handleOrderAction(order.opId,"리뷰작성");
                });
                button.textContent = "리뷰 작성"
                orderStatusChildBox2.appendChild(button)
            }else{
                orderStatusChildBox2.textContent = "리뷰 작성 완료"
            }
        }

        if(order.orderStatus == "교환요청" || order.orderStatus == "교환완료"){
            orderStatusChildBox2.textContent = "리뷰 작성 불가"
        }



        const quantityTd = document.createElement('td');
        quantityTd.textContent = order.quantity;

        const amountTd = document.createElement('td');
        amountTd.textContent = order.amount;

        // tr에 td들 추가
        newTr.appendChild(orderAtTd);
        newTr.appendChild(orderNumberTd);
        // newTr.appendChild(quantityTd);
        newTr.appendChild(nameTd);
        newTr.appendChild(priceTd);
        // newTr.appendChild(amountTd);
        newTr.appendChild(orderStatusTd);

        tbody.appendChild(newTr); // 행을 tbody에 추가
    }

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