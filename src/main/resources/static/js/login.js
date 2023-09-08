function login() {
    let loginId = document.getElementById('loginId').value;
    let password = document.getElementById('password').value;

    let formData = new FormData;
    formData.append('loginId', loginId);
    formData.append('password', password);

    fetch('/login', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.result === 'user') {
                // 로그인 성공 시 서버에서 받아온 accessToken 값을 로컬 스토리지에 저장
                localStorage.setItem('accessToken', data.accessToken);
                // 원하는 페이지로 리디렉션
                window.location.href = '/';
            } else if (data.result === 'admin') {
                localStorage.setItem('accessToken', data.accessToken);
                window.location.href = '/admin/main';
            } else {
                alert("아이디 또는 비밀번호가 일치하지 않습니다.");
                window.location.href = '/login';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}