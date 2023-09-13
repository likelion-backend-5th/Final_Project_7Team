document.addEventListener('DOMContentLoaded', async function loadCartPage() {

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };

    const allCheck = document.querySelector(".allCheck");
    const rowChecks = document.querySelectorAll(".rowCheck");

    allCheck.addEventListener("change", function () {
        const isChecked = allCheck.checked;
        rowChecks.forEach(rowCheck => {
            rowCheck.checked = isChecked;
        });
    })

    let data ={};

    try {
        const response = await fetch('/cart/data', {
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
                await loadCartPage(); // 원래 요청을 다시 시도
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
    const cartListData = data.cartList.content
    const totalAmountData = data.totalAmount

    const cartTotal = document.querySelector('.cart-total')
    cartTotal.innerText = data.cartList.totalElements

    const cartTable = document.querySelector(".cart-table");

    cartListData.forEach(cart => {
        const newRow = document.createElement("tr");
        newRow.innerHTML = `
                    <td><input type="checkbox" class="rowCheck" value="${cart.id}"></td>
                    <td>
                        <div class="cart-p-box">
                            <div class="cart-img-box"><img src="${cart.fileUrl}"></div>
                            <div class="cart-pname-box ${cart.stock === 0 ? 'sold-out' : 'sell'}">
                                ${cart.stock === 0 ? '<span>[품절]</span>' : ''}
                                <a class="pname" href="#">${cart.name}</a>
                                <p class="poption">${cart.size} / ${cart.color}</p>
                            </div>
                        </div>
                    </td>
                    <td><div>${cart.price}원</div></td>
                    <td>
                        <div>
                            <button class="btn_minus" onclick="changeQuantity(${cart.id}, 'minus', ${cart.price})">-</button>
                            <p id="quantity-${cart.id}">${cart.productCnt}</p>
                            <button class="btn_plus" onclick="changeQuantity(${cart.id}, 'plus', ${cart.price})">+</button>
                        </div>
                    </td>
                    <td id="price-${cart.id}" class="p-price">${cart.price * cart.productCnt}원</td>
                `;

        cartTable.appendChild(newRow);
    });

    const newRow = document.createElement("tr");
    newRow.innerHTML = `
            <td colspan="5" class="td-total-price">
                <p>전체 상품 금액</p>
                <p id="total-price"></p>
            </td>
    `
    cartTable.appendChild(newRow);

    // 총 상품 금액 업데이트
    const totalAmountElement = document.getElementById("total-price");
    totalAmountElement.innerText = totalAmountData + "원";
}

function changeQuantity(cartId, mp, price) {

    var currentQuantity = parseInt($("#quantity-" + cartId).text().trim()) // 공백 제거
    // '-' 버튼을 눌렀을 때 수량이 1 미만이 되는 경우
    if (mp === 'minus' && currentQuantity <= 1) {
        alert("수량은 1개 이상이어야 합니다.");
        return;
    }

    $.ajax({
        type: "PUT",
        url: "/cart",
        data: {
            id: cartId,
            mp: mp
        },
        success: function (response) {
            // 응답으로 받은 업데이트된 수량을 사용하여 수량 업데이트
            $("#quantity-" + cartId).text(response);
            // 해당 상품 총 금액 변경
            $("#price-" + cartId).text(price * response + '원');

            // 전체 상품 금액 변경
            let totalPrice = 0;
            $(".p-price").each(function () {
                let rowPrice = parseFloat($(this).text().replace('원', '').trim());
                totalPrice += rowPrice;
            });
            // totalPrice += $("#price-").text().replace('원', '').trim()
            $("#total-price").text(totalPrice + '원');
        }
    });
}


function deleteCart() {
    const selectedIds = [];

    // 체크된 값 배열에 저장
    document.querySelectorAll('.rowCheck:checked').forEach(checkbox => {
        selectedIds.push(checkbox.value);
    })

    if (selectedIds.length === 0) {
        alert('삭제할 상품을 선택해주세요')
        return
    }

    if (!confirm('상품을 삭제하시겠습니까?')) {
        return false;
    }

    $.ajax({
        type: "DELETE",
        url: "/cart",
        data: {cartIds: selectedIds},
        success: function () {
            alert("상품이 삭제되었습니다")
            location.reload()
        }
    })
}