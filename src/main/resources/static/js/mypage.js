// 프로필 (profile.html) =================================================
// 회원 정보 수정
function openProfilePopup() {
    window.open("/mypage/edit-profile", "회원 정보 수정", "width=600, height=600")
}

// 펫 등록
function openPetPopup() {
    window.open("/mypage/edit-pet", "우리 아이 등록/수정", "width=600, height=600")
}



// 배송지 (address) =================================================
// 배송지 등록 버튼 클릭
function openAddressPopup() {
    // window.open("팝업될 문서 경로","팝업될 문서 이름","옵션(위치, bar표시, 크기 등)");
    window.open("/mypage/add-address", "배송지 입력", "width=600, height=600, left=0, top=0")
}