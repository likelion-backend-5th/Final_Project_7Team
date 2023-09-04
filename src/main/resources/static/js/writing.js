let fileNo = 0;
let filesArr = new Array();

/* 글쓰기 */
async function createArticle() {
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    const selectedCategory = document.getElementById('selectedCategory').value;
    const imagesInput = document.getElementById('images');
    const images = imagesInput.files;

    if (title === '' || content === '' || selectedCategory === 'disabled') {
        alert('제목, 내용, 카테고리를 모두 입력해주세요.');
        return;
    }

    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('selectedCategory', selectedCategory);
    for (let i = 0; i < filesArr.length; i++) {
        formData.append("images", filesArr[i]);
    }

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };

    try {
        const response = await fetch('/community/write', {
            method: 'POST',
            headers: headers,
            body: formData
        });

        if (response.ok) {
            alert('글을 등록했습니다.');
            window.location.href="/community";
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await createArticle(); // 원래 요청을 다시 시도
            } else {
                alert('권한이 없습니다.');
                window.location.href="/login";
            }
        } else {
            alert("카테고리, 제목, 내용을 모두 입력하셨는지 확인해주세요");
        }
    } catch (error) {
        console.error('오류:', error);
        alert('권한이 없습니다.');
        window.location.href="/login";
    }
}

/* 첨부파일 추가 */
function addFile(obj){
    let maxFileCnt = 5;   // 첨부파일 최대 개수
    let attFileCnt = document.querySelectorAll('.filebox').length;    // 기존 추가된 첨부파일 개수
    let remainFileCnt = maxFileCnt - attFileCnt;    // 추가로 첨부가능한 개수
    let curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
    } else {
        for (const file of obj.files) {
            // 첨부파일 검증
            if (validation(file)) {
                // 파일 배열에 담기
                let reader = new FileReader();
                reader.onload = function () {
                    filesArr.push(file);
                };
                reader.readAsDataURL(file);

                // 목록 추가
                let htmlData = '';
                htmlData += '<div id="file' + fileNo + '" class="filebox">';
                htmlData += '   <p class="name">' + file.name + '</p>';
                htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo + ');"><i class="bi bi-dash-square"></i></a>';
                htmlData += '</div>';
                $('.file-list').append(htmlData);
                fileNo++;
            } else {
                continue;
            }
        }
    }
    // 초기화
    document.querySelector("input[type=file]").value = "";
}

/* 첨부파일 검증 */
function validation(obj){
    const fileTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}

/* 첨부파일 삭제 */
function deleteFile(num) {
    document.querySelector("#file" + num).remove();
    filesArr[num].is_delete = true;
}