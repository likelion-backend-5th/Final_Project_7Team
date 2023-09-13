// 클릭 이벤트
$(document).ready(function() {
    // Enter key event handler
    $('#keyword').keypress(function(event) {
        if (event.which === 13) { // Enter key code
            searchProducts();
        }
    });

    $('#search-bnt').click(function() {
        searchProducts();
    });

    // 페이지 로딩 후 초기 체크
    if ($("#filter").val() === "status") {
        $("#searchDiv").css("display", "none");
        $("#search-bnt").css("display", "none");
        $("#selectDiv").css("display", "block");
    } else {
        $("#selectDiv").css("display", "none");
        $("#search-bnt").css("display", "block");
        $("#searchDiv").css("display", "block");
    }

    // 셀렉트박스 생성
    $("#filter").on("change", function() {
        if ($(this).val() === "status") {
            $("#searchDiv").css("display", "none");
            $("#search-bnt").css("display", "none");
            $("#selectDiv").css("display", "block");
        } else {
            $("#selectDiv").css("display", "none");
            $("#search-bnt").css("display", "block");
            $("#searchDiv").css("display", "block");
        }
    });
});

function searchByStatus(status) {
    window.location.href = '/admin/products?filter=status&keyword=' + encodeURIComponent(status);
}

function searchProducts() {
    let filter = $('#filter').val();
    let keyword = $('#keyword').val();
    // Redirect to the new URL to reload the page
    window.location.href = '/admin/products?filter=' + encodeURIComponent(filter) + '&keyword=' + encodeURIComponent(keyword);
}

// 페이지 이동
function navigateToPage(page) {
    let filter = $('#filter').val();
    let keyword = $('#keyword').val();
    let hidden = $('#hidden').val();

    if(hidden != null) {
        filter = encodeURIComponent(filter);
        hidden = encodeURIComponent(hidden);
        location.href = '/admin/products?page=' + page + '&filter=' + filter + '&keyword=' + hidden;
    } else if(keyword != null){
        filter = encodeURIComponent(filter);
        keyword = encodeURIComponent(keyword);
        location.href = '/admin/products?page=' + page + '&filter=' + filter + '&keyword=' + keyword;
    } else {
        location.href = '/admin/products?page=' + page;
    }
}

// 상품 삭제
async function deleteProduct(productId) {
    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };
    try {
        const response = await fetch(`/admin/products/${productId}/delete`, {
            method: 'POST',
            headers: headers,
        });

        if (response.ok) {
            alert("삭제되었습니다.");
            window.location.href = "/admin/products";
        } else if (response.status === 401 || response.status === 403) {
            const reissueResponse = await fetch('/reissue', {
                method: 'POST',
                credentials: 'same-origin' // 쿠키를 보내기 위해 필수
            });

            if (reissueResponse.ok) {
                const reissuedData = await reissueResponse.json();
                localStorage.setItem('accessToken', reissuedData.accessToken);
                await deleteProduct(productId); // 원래 요청을 다시 시도
            } else {
                alert('토큰 재발급 실패');
            }
        }
    } catch (error) {
        alert("오류 발생");
    }
}