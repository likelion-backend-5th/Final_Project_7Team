function changeQuantity(id, mp) {

    var currentQuantity = parseInt($("#cart-" + id).text().trim()) // 공백 제거
    // '-' 버튼을 눌렀을 때 수량이 1 미만이 되는 경우
    if(mp==='minus' && currentQuantity <= 1) {
        alert("수량은 1개 이상이어야 합니다.");
        return;
    }

    $.ajax({
        type: "PUT",
        url: "/cart",
        data: {
            id: id,
            mp: mp
        },
        success: function (response) {
            // 응답으로 받은 업데이트된 수량을 사용하여 수량 업데이트
            $("#cart-" + id).text(response);
            // location.reload()
        }
    });
}

// const rowCheck = document.getElementsByClassName("rowCheck");
// const rowCount = rowCheck.length


function deleteCart() {
    const selectedIds = [];

    // 체크된 값 배열에 저장
    document.querySelectorAll('.rowCheck:checked').forEach(checkbox => {
        selectedIds.push(checkbox.value);
    })

    if(selectedIds.length === 0) {
        alert('삭제할 상품을 선택해주세요')
        return
    }

    if (!confirm('상품을 삭제하시겠습니까?')) {
        return false;
    }

    $.ajax({
        type: "DELETE",
        url: "/cart",
        data: { cartIds: selectedIds },
        success: function () {
            alert("상품이 삭제되었습니다")
            location.reload()
        }
    })
}