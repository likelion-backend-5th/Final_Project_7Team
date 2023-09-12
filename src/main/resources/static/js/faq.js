$(document).ready(function () {
    $('.faq-question').click(function (event) {
        event.preventDefault(); // a링크 기본 동작 막기
        toggleAnswer(this);
    });

    function toggleAnswer(linkElement) {
        $(linkElement).closest('tr').next('.faq-answer').slideToggle('fast');
    }
});