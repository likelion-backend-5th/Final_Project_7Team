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