function searchArticle() {
    let filter = document.getElementById('filter').value;
    let keyword = document.getElementById('keyword').value;
    let currentURL = window.location.href;
    let urlParams = new URLSearchParams(currentURL);
    let categoryId = urlParams.get("categoryId");
    if (categoryId == null) {
        categoryId = ''
    }
    window.location.href = '/community?categoryId=' + categoryId + '&filter=' + encodeURIComponent(filter) + '&keyword=' + encodeURIComponent(keyword);
}