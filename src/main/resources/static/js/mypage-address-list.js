document.addEventListener('DOMContentLoaded', async function loadAddressPage() {

    const accessToken = localStorage.getItem("accessToken");
    const headers = {
        'Authorization': `Bearer ${accessToken}`
    };
    let data ={};

    try {
        const response = await fetch('/mypage/address/data', {
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
                await loadAddressPage(); // 원래 요청을 다시 시도
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
    const addressList = data.addressList.content;
    const addressTable = document.querySelector('.mypage-address-table tbody');

    addressList.forEach((address) => {
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
        <td>
          <div>${address.addressName}</div>
        </td>
        <td>
          <div>${address.name}</div>
        </td>
        <td>
          <div class="td-address">
            ${address.defaultAddress === 'Y' ? '<span class="default-address">기본 배송지</span>' : ''}
            <div>${address.address}</div>
          </div>
        </td>
        <td>
          <div>${address.phone}</div>
        </td>
        <td>
          <button class="btn-address-update" onclick="openEditAddressPopup(${address.id})">수정</button>
          <button class="btn-address-delete" type="button" onclick="deleteAddress(${address.id})">삭제</button>
        </td>
      `;

        addressTable.appendChild(newRow);
    });
}