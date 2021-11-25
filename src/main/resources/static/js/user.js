import {Validation} from "./validation.js";

const form = document.querySelector(".form");
const errorMessage = document.querySelector("#error-message")

form.addEventListener('submit',(e) => {

    e.preventDefault();

    // メールアドレス形式の検証
    if(!(Validation.validateEmailAddress(form.emailAddress.value))) {
        errorMessage.textContent = "メールアドレス形式が正しくありません。"
        return;
    }

    console.log(form.password.value.length);

    // 入力パスワードにおける文字数の検証
    if(!(Validation.validateCharacterLimit(form.password.value,8,12))){
        errorMessage.textContent = "パスワードは8文字以上12文字以内で入力してください。"
        return;
    }

    // ニックネームが空白の場合は、「名無しさん」と自動入力する
    if(Validation.validateSpace(form.name.value)){
        form.name.value = "名無しさん";
        console.log(form.name.value)
    }

    // ニックネーム文字数の検証
    if(!(Validation.validateMaxCharacterLimit(form.name.value,15))) {
        errorMessage.textContent = "ニックネームは15文字以内で入力してください。"
        return;
    }

    form.submit();
});