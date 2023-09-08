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