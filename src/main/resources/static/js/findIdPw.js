// 모달 띄우기
function openModal(message) {
    const modal = document.getElementById("modal");
    const modalMessage = document.getElementById("modalMessage");

    modalMessage.innerText = message;
    modal.style.display = "block";
}

// 모달 닫기
function closeModal() {
    const modal = document.getElementById("modal");
    modal.style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    const modal = document.getElementById("modal");
    if (event.target === modal) {
        closeModal();
    }
};

//아이디 찾기
function findId() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;

    fetch(`/findId?name=${name}&email=${email}`)
        .then(response => response.json())
        .then(data => {
            openModal(data.message);
        })
        .catch(error => {
            console.error("Error finding ID", error);
        });
}

//비밀번호 찾기(계정 있는지 검사)
function findPw() {
    const loginId = document.getElementById("loginId").value;
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;

    fetch(`/findPassword?loginId=${loginId}&name=${name}&email=${email}`)
        .then(response => response.json())
        .then(data => {
            openModal(data.message);
            if (data.message !== "입력하신 이메일로 임시 비밀번호를 발급합니다.") {
                document.getElementById("getPwBtn").style.display = "none";
            } else {
                document.getElementById("getPwBtn").style.display = "block";
            }
        })
        .catch(error => {
            console.error("Error finding Password", error);
        });
}

//비밀번호 찾기(임시 비밀번호 전송)
function getTempPw() {
    $.ajax({
        url: "/tempPassword",
        type: "post",
        dataType: "text",
        data: { "email": $("#email").val() },
        success: function (data) {
            alert("임시 비밀번호가 전송되었습니다.");
        },
        error: function (request, status, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
        }
    });
}