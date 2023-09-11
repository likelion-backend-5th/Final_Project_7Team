function deleteArticle() {
    const selectedIds = [];

    // 체크된 값 배열에 저장
    document.querySelectorAll('.rowCheck:checked').forEach(checkbox => {
        selectedIds.push(checkbox.value);
    })

    if (selectedIds.length === 0) {
        alert('삭제할 글을 선택해주세요')
        return
    }

    if (!confirm('글을 삭제하시겠습니까?')) {
        return false;
    }

    $.ajax({
        type: "DELETE",
        url: "/mypage/article",
        data: {articleIds: selectedIds},
        success: function () {
            alert("글이 삭제되었습니다")
            location.reload()
        }
    })

}