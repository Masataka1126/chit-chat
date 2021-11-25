export class Validation {

    /**
     * メールアドレスの形式チェック
     * @param emailAddress
     * @return boolean
     * 形式に適合している場合、true
     */
    static validateEmailAddress(emailAddress) {
        const regex =
            /^[a-zA-Z0-9_+-]+(\.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\.)+[a-zA-Z]{2,}$/;
        return regex.test(emailAddress);
    }

    /**
     * 文字数チェック
     * @param inputString 入力文字列
     * @param min 最小文字数
     * @param max 最大文字数
     * @return boolean
     * 文字数がmin以上max以内の場合、true
     */
    static validateCharacterLimit(inputString,min,max) {
        return inputString.length >= min && inputString.length <= max;
    }

    /**
     * 名前の文字数チェック
     * @param inputString
     * @param max
     * @return boolean
     * 文字数が15字以内の場合、true
     */
    static validateMaxCharacterLimit(inputString,max) {
        return inputString.length <= max;
    }

    /**
     * @param inputString
     * @return boolean
     * nullまたは空白の場合、true
     */
    static　validateSpace(inputString) {
        return (inputString == null) || !(inputString.trim());
    }

}