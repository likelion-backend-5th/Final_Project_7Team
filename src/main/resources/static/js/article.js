function showModal() {
    document.getElementById("confirmDeleteModal").style.display = "block";
}

//취소 버튼 눌렀을 때
function cancelButton() {
    document.getElementById("confirmDeleteModal").style.display = "none";
}

window.onclick = function(event) {
    const modal = document.getElementById("confirmDeleteModal");
    if (event.target === modal) {
        cancelButton();
    }
};