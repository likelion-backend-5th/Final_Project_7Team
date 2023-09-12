$(document).ready(function () {
    loadComments(0);
    $('#commentValue').keypress(function(event) {
        if (event.which === 13) { // Enter key code
            addComment();
        }
    });
    }
);

$('#commentValue').keypress(function(event) {
    if (event.which === 13) { // Enter key code
        addComment();
    }
});

function loadComments(page) {
    let communityId = parseInt($("#communityId").val());
    $.ajax({
        url: `/admin/communities/${communityId}/comments?page=${page}`,
        type: "GET",
        contentType: "application/json",
        success: function (response) {
            // 성공적으로 처리된 경우의 동작
            if(response) {
                updateComments(response); // 받은 데이터로 댓글 업데이트
                createPagination(response); // 페이지네이션 업데이트
            } else {
                updateComments(response)
            }

        },
        error: function (error) {
            // 오류 처리 동작
            window.location.href = "/admin/error-page/500";
        }
    });
}

function updateComments(response) {

    let commentContainer = $("#commentContainer"); // 댓글을 표시할 컨테이너 요소
    if (response) {
        commentContainer.empty(); // 컨테이너 내용 초기화
        // 댓글 데이터를 반복하여 표시
        response.content.forEach(function (comment) {
            const formattedCreatedAt = formatDateTime(comment.createdAt);
            let commentId = parseInt(comment.id);

            let commentElement = `
                <div class="px-4 py-3 mb-8 bg-white rounded-lg shadow-md dark:bg-gray-800">
                    <div class="flex">
                        <b class="block text-gray-700 dark:text-gray-400 ml-3 mt-4">${comment.commentWriter}</b>
                        <span class="block text-gray-700 dark:text-gray-400 mr-3 mt-4" style="margin-left: auto">${formattedCreatedAt}</span>
                    </div>
                    <div class="flex">
                        <span class="block text-gray-700 dark:text-gray-400 ml-3 mt-4 mb-4" style="width: 94%">${comment.content}</span>
                        <span class="mt-4 mb-4" style="margin-left: auto;">
                            <a class="deleteCommentButton px-4 py-2 text-sm font-medium leading-5 text-white" 
                                onclick="deleteComment(${commentId})"
                            >
                                삭제
                            </a>
                        </span>
                    </div>
                    <hr>
                </div>
            `;
            commentContainer.append(commentElement);
        });
    } else {
        commentContainer.append("<span class='block text-gray-700 dark:text-gray-400 ml-3 mt-4 mb-4 text-center text-sm'>등록된 댓글이 없습니다.</span>");
        $("#outerDiv").css("display","none");
    }
}

function formatDateTime(dateTimeString) {
    const dateTime = new Date(dateTimeString);
    const year = dateTime.getFullYear();
    const month = String(dateTime.getMonth() + 1).padStart(2, '0');
    const day = String(dateTime.getDate()).padStart(2, '0');
    const hours = String(dateTime.getHours()).padStart(2, '0');
    const minutes = String(dateTime.getMinutes()).padStart(2, '0');
    const seconds = String(dateTime.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

function createPagination(page) {
    let pageNationContainer = $("#pagination");

    // 페이지네이션 컨테이너에 추가
    pageNationContainer.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur+1)/10.0)*10; // 1~10
    let startPage = endPage-9; // 1~10
    if(endPage>page.totalPages-1) // totalPage는 1부터 센다 그래서 1을 빼줌
        endPage=page.totalPages;

    if(cur > 0) // 이전 버튼
        pageNationContainer.append(`<li class="mr-3"><a onclick="loadComments(${cur - 1})"> < </a></li>`);

    for(let i=startPage; i <= endPage; i++) { // 페이지네이션
        pageNationContainer.append(`<li class="mr-3"><a onclick="loadComments(${i - 1})"> ${i} </a></li>`);
    }
    if(cur + 1 <page.totalPages) // 다음 버튼
        pageNationContainer.append(`<li class="mr-3"><a onclick="loadComments(${cur + 1})"> > </a></li>`);
        pageNationContainer.append(`<input type="hidden" id="cur" value="${cur}">`)

}

async function deleteComment(commentId) {
    let communityId = parseInt($("#communityId").val());

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };
    // 선택된 상품의 상태값 변경 요청
    try {
        const response = await fetch(`/admin/communities/${communityId}/comments/${commentId}`, {
            method: 'POST',
            headers: headers
        });

        if (response.ok) {
            alert('삭제되었습니다.');
            window.location.reload();
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await deleteComment(); // 원래 요청을 다시 시도
            } else {
                alert('토큰 재발급 실패');
            }
        }
    } catch (error) {
        alert("오류 발생");
    }
}

async function deleteCommunity() {
    let selectedItems = [];

    let id = $("#communityId").val();
    selectedItems.push({id: id});

    if (selectedItems.length > 0) {
        const accessToken = localStorage.getItem("accessToken");
        const headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        };
        // 선택된 상품의 상태값 변경 요청
        try {
            const response = await fetch('/admin/communities/delete-list', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(selectedItems)
            });

            if (response.ok) {
                alert('삭제되었습니다.');
                window.location.href = "/admin/communities";
            } else if (response.status === 401 || response.status === 403) {
                const reissueResponse = await fetch('/reissue', {
                    method: 'POST',
                    credentials: 'same-origin' // 쿠키를 보내기 위해 필수
                });

                if (reissueResponse.ok) {
                    const reissuedData = await reissueResponse.json();
                    localStorage.setItem('accessToken', reissuedData.accessToken);
                    await deleteCommunity(); // 원래 요청을 다시 시도
                } else {
                    alert('토큰 재발급 실패');
                }
            }
        } catch (error) {
            alert("오류 발생");
        }
    }
}


async function addComment() {

    let value = $("#commentValue").val();
    let communityId = parseInt($("#communityId").val());

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };
    try {
        const response = await fetch(`/admin/communities/${communityId}/comments/create`, {
            method: 'POST',
            headers: headers,
            body: value
        });

        if (response.ok) {
            alert('등록되었습니다.');
            window.location.reload();
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await addComment(); // 원래 요청을 다시 시도
            } else {
                alert('토큰 재발급 실패');
            }
        }
    } catch (error) {
        alert("오류 발생");
    }
}