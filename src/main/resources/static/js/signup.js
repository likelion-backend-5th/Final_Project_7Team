//아이디 유효성검사 & 중복확인
function checkDuplicateId() {
    let loginId = document.getElementById("loginId").value;
    let idValidationMessage = document.getElementById("id-validation-message");

    const idRegExp = /^[a-z0-9]{8,15}$/;

    if (!idRegExp.test(loginId)) {
        idValidationMessage.innerText = "아이디는 영문, 숫자 조합 8~15자리로 입력해주세요";
        idValidationMessage.style.color = "gray";
        return false;
    } else {
        return fetch("/checkDuplicateId?loginId=" + loginId)
            .then(response => response.json())
            .then(data => {
                if (data.duplicate) {
                    idValidationMessage.innerText = "사용중인 아이디입니다.";
                    idValidationMessage.style.color = "gray";
                    return false;
                } else {
                    idValidationMessage.innerText = "사용 가능한 아이디입니다.";
                    idValidationMessage.style.color = "gray";
                    return true;
                }
            })
            .catch(error => {
                console.error("Error checking loginId", error);
                return false;
            });
    }
}

//인증번호 전송 버튼 활성화
function toggleEmailButton() {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    let email = document.getElementById("email").value;
    let emailBtn = document.getElementById("emailBtn");

    if(email !== "" && emailRegex.test(email)) {
        emailBtn.disabled = false;
    } else {
        emailBtn.disabled = true;
    }
}

//이메일 중복확인 & 이메일 인증(인증번호 전송)
function emailVerification() {
    let email = document.getElementById("email").value;
    let message = document.getElementById("email-verification-message");

    return fetch("/checkDuplicateEmail?email=" + email)
        .then(response => response.json())
        .then(data => {
            if (data.duplicate) {
                message.innerText = "해당 이메일로 가입한 계정이 존재합니다.";
                message.style.color = "gray";
                return false;
            } else {
                return new Promise((resolve, reject) => {
                    document.getElementById("emailVerificationFields").style.display = "block";
                    $.ajax({
                        url: "/email",
                        type: "post",
                        dataType: "json",
                        data: { "email": $("#email").val() },
                        success: function (data) {
                            alert("인증번호가 전송되었습니다");
                            $("#verificationCode").attr("value", data);
                            resolve(true); // 인증번호 전송 성공
                        },
                        error: function (request, status, error) {
                            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                            reject(false); // 인증번호 전송 실패
                        }
                    });
                });
            }
        })
        .catch(error => {
            console.error("Error checking loginId", error);
            return false;
        });
}

//이메일 인증(인증번호 확인)
function verifyCode() {
    let inputCode = document.getElementById("verificationCodeInput").value;
    let code = document.getElementById("verificationCode").value;
    let message = document.getElementById("email-verification-message");

    if (inputCode === code) {
        message.innerText = "인증되었습니다.";
        message.style.color = "gray";
        return true;
    } else {
        message.innerText = "인증번호가 다릅니다.";
        message.style.color = "gray";
        return false;
    }
}

//닉네임 중복확인
function checkDuplicateNickname() {
    let nickname = document.getElementById("nickname").value;
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

//비밀번호 유효성검사
function passwordVal() {
    let password = document.getElementById("password").value;

    let pwValidationMessage = document.getElementById("pw-validation-message");
    const pwRegExp = /^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?~\-]).{8,20}$/;

    //비밀번호
    if(!pwRegExp.test(password)) {
        pwValidationMessage.innerText = "비밀번호는 영문, 특수문자, 숫자 조합 8~20자리로 입력해주세요";
        pwValidationMessage.style.color = "gray";
        return false;
    } else {
        pwValidationMessage.innerText = "";
        pwValidationMessage.style.color = "gray";
        return true;
    }
}

//비밀번호 확인 유효성 검사
function passwordCheckVal() {
    let password = document.getElementById("password").value;
    let checkPassword = document.getElementById("checkPassword").value;
    let cpwValidationMessage = document.getElementById("cpw-validation-message");

    if (password !== checkPassword) {
        cpwValidationMessage.innerText = "비밀번호가 일치하지 않습니다";
        cpwValidationMessage.style.color = "gray"
        return false;
    } else {
        cpwValidationMessage.innerText = "비밀번호가 일치합니다";
        cpwValidationMessage.style.color = "gray";
        return true;
    }
}

//휴대폰 번호 유효성 검사
function phoneVal() {
    let phone = document.getElementById("phone").value;
    let phoneValidationMessage = document.getElementById("phone-validation-message");

    const phRegExp = /^010[0-9]{8}$/;

    //휴대폰 번호
    if(!phRegExp.test(phone)) {
        phoneValidationMessage.innerText = "'-'없이 숫자로만 입력해주세요"
        phoneValidationMessage.style.color = "gray";
        return false;
    } else {
        phoneValidationMessage.innerText = "";
        phoneValidationMessage.style.color = "gray";
        return true;
    }
}


//제출 전 확인
async function checkAndSubmit() {
    if (!await checkDuplicateId()) {
        alert("아이디를 확인해주세요.");
    } else if (!passwordVal()) {
        alert("비밀번호를 확인해주세요.");
    } else if (!passwordCheckVal()) {
        alert("비밀번호가 불일치합니다.");
    } else if (!phoneVal()) {
        alert("휴대폰번호를 확인해주세요.");
    } else if (!verifyCode()) {
        alert("이메일 인증을 진행해주세요.");
    } else if (!await checkDuplicateNickname()) {
        alert("닉네임을 확인해주세요.");
    } else {
        document.getElementById("signupForm").submit();
    }
}