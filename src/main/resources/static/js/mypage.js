// 프로필 (profile.html) =======================================================
// 회원 정보 수정
function openProfilePopup() {
    window.open("/mypage/edit-profile", "회원 정보 수정", "width=600, height=600")
}

// 펫 등록
function openPetPopup() {
    window.open("/mypage/edit-pet", "우리 아이 등록/수정", "width=600, height=600")
}


// 주문내역 (order_list.html) =======================================================
// 주문 상태에 따른 필터링
function filterOrderStatus(orderStatus) {
    if(orderStatus === 'ALL') {
        location.href = "/mypage/order-list"
    } else {
        location.href = "/mypage/order-list?orderStatus=" + orderStatus;
    }
}

// 주문 상태별 버튼 클릭
function handleOrderAction(opId, action) {
    if (action === '구매확정') {
        if (confirm("구매 확정을 하시겠습니까? 구매 확정 시 교환 또는 환불 요청이 불가능합니다.")) {
            fetch("/mypage/order-list/purchase-confirm/" + opId, {
                method: "put"
            }).then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    throw new Error('구매 확정 실패');
                }
            }).then(data => {
                alert(data.message)
                location.reload()
            }).catch(error => {
                alert(error.message)
            })
        } else {
            return false
        }
    }

    // if (action === '교환요청' || action === '환불요청') {
    if (action === '교환요청') {
        location.href = "/mypage/order-list/exchange/" + opId
    }
}

// 교환 및 환불 요청
function exchangeRefund() {

}

// 배송지 (address) ==========================================================
// 배송지 등록 버튼 클릭
function openAddressPopup() {
    open("/mypage/add-address", "배송지 입력", "width=600, height=600, left=0, top=0")
}

// 배송지 등록 (post)
function submitAddress() {
    const form = document.getElementById('addressForm')
    const formData = new FormData(form)
    fetch("/mypage/add-address", {
        method: "post",
        body: formData,
    })
        .then(response => {
                if (response.ok) {
                    self.close();
                    alert("등록되었습니다.")
                    opener.parent.location.reload();
                }
            }
        )
}

// 주소 API
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ''; // 참고 항목 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }

            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("address").value = roadAddr;

        }
    }).open();
}

function deleteAddress(addressId) {
    if (!confirm('배송지를 삭제하시겠습니까?')) {
        return false;
    }

    fetch("/mypage/address/delete/" + addressId, {
        method: "post",
    }).then(response => {
            if (response.ok) {
                alert('삭제되었습니다');
                location.reload()
            }
        }
    )
}

function openEditAddressPopup(addressId) {
    open("/mypage/address/update/" + addressId, "배송지 수정", "width=600, height=600, left=0, top=0")
}

function updateAddress(addressId) {
    const form = document.getElementById('addressForm')
    const formData = new FormData(form)
    fetch("/mypage/address/update/" + addressId, {
        method: "post",
        body: formData
    }).then(response => {
            if (response.ok) {
                alert('수정되었습니다');
                self.close();
                opener.parent.location.reload();
            }
        }
    )
}