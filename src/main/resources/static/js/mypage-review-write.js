document.addEventListener('DOMContentLoaded', async function loadReviewPage() {

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };
    let data ={};

    let searchPath = window.location.pathname
    let opId = searchPath.split('/')[4]

    try {
        const response = await fetch(`/mypage/order-list/review/${opId}/data`, {
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

    const submitReviewButton = document.querySelector('#btn-submit-review');
    submitReviewButton.addEventListener('click', function requestReview() {

        // const opId = data.orderProduct.id

        const reviewImgInput = document.getElementById('review-img-input')

        const formData = new FormData()

        const reviewImg = reviewImgInput.files[0]
        formData.append('reviewImg', reviewImg)

        const rating = document.querySelector('input[name="rate"]:checked')
        if (!rating) {
            alert('별점을 선택해주세요')
            return
        }

        const reviewData = {
            description: document.getElementById('text-review').value,
            rating: rating.value
        }

        // 특수 문자와 공백을 제거 후 글자 수 체크 (20자 이상)
        const cleanedDescription = reviewData.description.replace(/[^\w\s가-힣]/gi, '').replace(/\s/g, '')
        if (cleanedDescription.length < 20) {
            alert('후기 내용을 20자 이상 작성해주세요 (공백, 특수 문자, 단순 문자 제외)')
            return
        }

        formData.append('reviewFormDto', new Blob([JSON.stringify(reviewData)], {type: 'application/json'}));

        fetch("/mypage/order-list/review/" + opId, {
            method: "POST",
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    alert("리뷰가 등록되었습니다")
                    location.href = "/mypage/order-list"
                } else {
                    alert("리뷰 등록에 실패하였습니다")
                }
            })
    })

    // dataOnPage(data);
});

// 리뷰 작성 요청
// function requestReview(opId) {
//     const reviewImgInput = document.getElementById('review-img-input')
//
//     const formData = new FormData()
//
//     const reviewImg = reviewImgInput.files[0]
//     formData.append('reviewImg', reviewImg)
//
//     const rating = document.querySelector('input[name="rate"]:checked')
//     if (!rating) {
//         alert('별점을 선택해주세요')
//         return
//     }
//
//     const reviewData = {
//         description: document.getElementById('text-review').value,
//         rating: rating.value
//     }
//
//     // 특수 문자와 공백을 제거 후 글자 수 체크 (20자 이상)
//     const cleanedDescription = reviewData.description.replace(/[^\w\s가-힣]/gi, '').replace(/\s/g, '')
//     if (cleanedDescription.length < 20) {
//         alert('후기 내용을 20자 이상 작성해주세요 (공백, 특수 문자, 단순 문자 제외)')
//         return
//     }
//
//     formData.append('reviewFormDto', new Blob([JSON.stringify(reviewData)], {type: 'application/json'}));
//
//     fetch("/mypage/order-list/review/" + opId, {
//         method: "POST",
//         body: formData
//     })
//         .then(response => {
//             if (response.ok) {
//                 alert("리뷰가 등록되었습니다")
//                 location.href = "/mypage/order-list"
//             } else {
//                 alert("리뷰 등록에 실패하였습니다")
//             }
//         })
//
// }