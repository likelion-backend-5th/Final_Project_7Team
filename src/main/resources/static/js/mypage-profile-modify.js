//닉네임 중복확인
function checkDuplicateNicknameModify(existingNickname) {
    let nickname = document.getElementById("nickname").value;

    if(existingNickname == nickname) {
        return true
    } else {
        let nicknameDuplicateMessage = document.getElementById("nickname-duplicate-message");

        const nicknameRegExp = /^[a-zA-Z0-9ㄱ-ㅎ가-힣]{2,10}$/;

        if (!nicknameRegExp.test(nickname)) {
            nicknameDuplicateMessage.innerText = "닉네임은 한글, 영문, 숫자 2~10자리로 입력해주세요";
            nicknameDuplicateMessage.style.color = "gray";
            return false;
        } else {
            return fetch("/checkDuplicateNickname?nickname=" + nickname)
                .then(response => response.json())
                .then(data => {
                    if (data.duplicate) {
                        nicknameDuplicateMessage.innerText = "사용중인 닉네임입니다.";
                        nicknameDuplicateMessage.style.color = "gray";
                        return false;
                    } else {
                        nicknameDuplicateMessage.innerText = "사용 가능한 닉네임입니다.";
                        nicknameDuplicateMessage.style.color = "gray";
                        return true;
                    }
                })
                .catch(error => {
                    console.error("Error checking nickname", error);
                    return false;
                });
        }
    }


}

// 수정 버튼 클릭
async function updateProfile(existingNickname) {
    let password = document.getElementById("password").value;
    if ((password!="") && !passwordCheckVal()) {
        alert("비밀번호가 불일치합니다.");
    } else if ((password!="") && !passwordVal()) {
        alert("비밀번호를 확인해주세요.");
    } else if (!phoneVal()) {
        alert("휴대폰번호를 확인해주세요.");
    } else if (!await checkDuplicateNicknameModify(existingNickname)) {
        alert("닉네임을 확인해주세요.");
    } else {
        const form = document.getElementById('profileForm')
        const formData = new FormData(form)
        fetch("/mypage/profile/update", {
            method: "put",
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

}