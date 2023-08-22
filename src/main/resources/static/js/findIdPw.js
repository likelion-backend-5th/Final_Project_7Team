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