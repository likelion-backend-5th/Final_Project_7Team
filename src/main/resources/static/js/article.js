function showReportArticleModal() {
    document.getElementById("reportArticleModal").style.display = "block";
}

function cancelReportArticleModal() {
    document.getElementById("reportArticleModal").style.display = "none";
}

function showReportCommentModal(button) {
    document.getElementById("reportCommentModal").style.display = "block";
    const commentId = button.getAttribute('data-comment-id');
    const commentWriter = button.getAttribute('data-comment-writer');
    document.getElementById("commentReportIdText").value = commentId;
    document.getElementById("commentReportWriterText").textContent = commentWriter;
}

function cancelReportCommentModal() {
    document.getElementById("reportCommentModal").style.display = "none";
}

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
    const modalRA = document.getElementById("reportArticleModal");
    const modalRC = document.getElementById("reportCommentModal");
    if (event.target === modal) {
        cancelButton();
    } else if (event.target === modalUC) {
        closeCommentUpdateModal();
    } else if (event.target === modalDC) {
        cancelCommentDeleteButton();
    } else if (event.target === modalRA) {
        cancelReportArticleModal();
    } else if (event.target === modalRC) {
        cancelReportCommentModal();
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