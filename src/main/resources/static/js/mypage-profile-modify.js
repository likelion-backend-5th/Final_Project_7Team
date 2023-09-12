document.addEventListener('DOMContentLoaded', async function loadProfileUpdatePage() {

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };
    let data = {};

    try {
        const response = await fetch('/mypage/profile/update/data', {
            method: 'GET',
            headers: headers,
        });

        if (response.ok) {
            data = await response.json();
            console.log("data", data)
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await loadProfileUpdatePage(); // 원래 요청을 다시 시도
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

    const nicknameBtn = document.querySelector('.btn-profile')
    nicknameBtn.addEventListener('click', function () {
        checkDuplicateNicknameModify(data.profile.nickname)
    })

    const updateProfileBtn = document.getElementById('btn-submit');
    updateProfileBtn.addEventListener('click', async function () {
        let password = document.getElementById("password").value;
        if ((password != "") && !passwordCheckVal()) {
            alert("비밀번호가 불일치합니다.");
        } else if ((password != "") && !passwordVal()) {
            alert("비밀번호를 확인해주세요.");
        } else if (!phoneVal()) {
            alert("휴대폰번호를 확인해주세요.");
        } else if (!await checkDuplicateNicknameModify(data.profile.nickname)) {
            alert("닉네임을 확인해주세요.");
        } else {
            const form = document.getElementById('profileForm')
            const formData = new FormData(form)
            fetch(`/mypage/profile/update/${data.profile.loginId}`, {
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
    })
})

function dataOnPage(data) {

    const profile = data.profile;
    document.querySelector('.id').value = profile.loginId;
    document.querySelector('.name').value = profile.name;
    document.querySelector('.phone').value = profile.phone;
    document.querySelector('.email').value = profile.email;
    document.querySelector('.nickname').value = profile.nickname;

}


//닉네임 중복확인
function checkDuplicateNicknameModify(existingNickname) {
    let nickname = document.getElementById("nickname").value;

    if (existingNickname === nickname) {
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

