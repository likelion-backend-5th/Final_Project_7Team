document.addEventListener('DOMContentLoaded', async function loadArticlePage() {

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };
    let data ={};

    try {
        const response = await fetch('/mypage/article/data', {
            method: 'GET',
            headers: headers,
        });

        if (response.ok) {
            data =  await response.json();
            console.log("data",data)
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await loadArticlePage(); // 원래 요청을 다시 시도
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
});

function dataOnPage(data) {
    const articleList = data.articleList.content;
    const articleTable = document.querySelector('.mypage-article-table tbody');

    articleList.forEach((article) => {
        const articleRow = document.createElement('tr');

        articleRow.innerHTML = `
            <td><input type="checkbox" class="rowCheck" value="${article.articleId}"></td>
            <td>${article.articleId}</td>
            <td class="td-title">
                <span class="category">${'[' + article.categoryName + ']'}</span>
                <a href="/community/${article.articleId}">${article.title}</a>
                <span class="comment-cnt">${'(' + article.commentCnt + ')'}</span>
            </td>
            <td>${article.viewCnt}</td>
            <td>${article.likeCnt}</td>
            <td>${formatDate(article.createdAt)}</td>
        `;

        articleTable.appendChild(articleRow);
    });
}

function formatDate(inputDate) {
    const date = new Date(inputDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

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