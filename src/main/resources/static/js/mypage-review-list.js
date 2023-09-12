document.addEventListener('DOMContentLoaded', async function loadReviewPage() {

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };
    let data = {};

    try {
        const response = await fetch('/mypage/review/data', {
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
                await loadReviewPage(); // 원래 요청을 다시 시도
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
    const reviewList = data.reviewList.content;
    const reviewContainer = document.querySelector('.review-list-container');

    reviewContainer.innerHTML = '';

    reviewList.forEach((review) => {
        const reviewListBox = document.createElement('div');
        reviewListBox.classList.add('review-list-box');

        const contentBox = document.createElement('div');
        contentBox.classList.add('content-box');
        contentBox.innerHTML = `
            <p>${review.pname}</p>
            <p>${review.size} / ${review.color}</p>
            <div class="rating">
                <span>${'★'.repeat(review.rating)}</span>
            </div>
            <p>${review.description}</p>
            <p>${formatDate(review.createdAt)}</p>
        `;

        const btnAndImgBox = document.createElement('div');
        btnAndImgBox.classList.add('btn-and-img-box');
        btnAndImgBox.innerHTML = `
            <button type="button" class="btn-review-list-update" onclick="location.href='/mypage/review/${review.id}'">수정</button>
            <button type="button" class="btn-review-list-delete" onclick="deleteReview(${review.id})">삭제</button>
        `;

        if (review.fileUrl) {
            const imgBox = document.createElement('div');
            imgBox.classList.add('img-box');
            const img = document.createElement('img');
            img.src = review.fileUrl;
            imgBox.appendChild(img);
            btnAndImgBox.appendChild(imgBox);
        }

        reviewListBox.appendChild(contentBox);
        reviewListBox.appendChild(btnAndImgBox);

        reviewContainer.appendChild(reviewListBox)

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