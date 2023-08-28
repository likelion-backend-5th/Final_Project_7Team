const errors = [];

function validateProductData(data) {

    if (data.categoryId.trim() === "") {
        errors.push("카테고리가 선택되지 않았습니다.");
    }

    if (!data.name || data.name.trim() === "") {
        errors.push("상품명이 작성되지 않았습니다.");
    }

    if (!data.price || isNaN(data.price) || parseInt(data.price) <= 0) {
        errors.push("유효한 상품 가격이 작성되지 않았습니다.");
    }

    let optionList = [];

    data.productOptionList.forEach(function (option) {
        optionList.push(option.size);
        if (!option.color || option.color.trim() === "") {
            errors.push("색상이 작성되지 않았습니다.");
        }

        if (!option.size || option.size.trim() === "") {
            errors.push("사이즈가 작성되지 않았습니다.");
        }

        if (!option.stock || isNaN(option.stock) || parseInt(option.stock) <= 0) {
            errors.push("유효한 판매수량이 작성되지 않았습니다.");
        }
    });

    for (let i = 0; i < optionList.length; i++) {
        if(optionList.includes(optionList[i])) {
            errors.push("같은 사이즈가 선택되었습니다. 다른 사이즈를 선택해주세요.");
            break;
        }
    }

    return errors;
}

function validateImage(img) {

    if (!img) {
        errors.push("이미지를 선택해주세요.");
    } else {
        // 파일 크기 제한 (예: 5MB)
        const maxSize = 10 * 1024 * 1024; // 10MB
        if (img.size > maxSize) {
            errors.push("이미지의 크기가 너무 큽니다.");
        }

        // 허용된 확장자 체크
        const allowedExtensions = ["jpg", "jpeg", "png", "gif"];
        const imgExtension = img.name.split(".").pop().toLowerCase();
        if (!allowedExtensions.includes(imgExtension)) {
            errors.push("이미지는 jpg, jpeg, png, gif 확장자만 허용됩니다.");
        }
    }

    return errors;
}