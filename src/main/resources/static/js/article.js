function showModal() {
    document.getElementById("confirmDeleteModal").style.display = "block";
}

//취소 버튼 눌렀을 때
function cancelButton() {
    document.getElementById("confirmDeleteModal").style.display = "none";
}

window.onclick = function(event) {
    const modal = document.getElementById("confirmDeleteModal");
    const modalUC = document.getElementById("commentModal");
    const modalDC = document.getElementById("commentDeleteModal");
    if (event.target === modal) {
        cancelButton();
    } else if (event.target === modalUC) {
        closeCommentUpdateModal();
    } else if (event.target === modalDC) {
        cancelCommentDeleteButton();
    }
};

function commentUpdateModal(button) {
    document.getElementById("commentModal").style.display = "block";
    const commentId = button.getAttribute('data-comment-id');
    const commentContent = button.getAttribute('data-comment-content');
    console.log(commentId);
    document.getElementById("commentIdText").value = commentId;
    document.getElementById("commentEditTextarea").textContent = commentContent;
}

function closeCommentUpdateModal() {
    document.getElementById("commentModal").style.display = "none";
}

function commentDeleteModal(button) {
    document.getElementById("commentDeleteModal").style.display = "block";
    const commentId = button.getAttribute('data-comment-id');
    console.log(commentId);
    document.getElementById("commentIdTextDelete").value = commentId;
}

function cancelCommentDeleteButton() {
    document.getElementById("commentDeleteModal").style.display = "none";
}