package vn.huuchuong.be_bee_store.auth_module.error;



import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_CREDENTIALS(
            "INVALID_CREDENTIALS",
            "Sai username hoặc password"
    ),

    ACCOUNT_NOT_ACTIVATED(
            "ACCOUNT_NOT_ACTIVATED",
            "Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để kích hoạt tài khoản."
    ),

    USERNAME_ALREADY_EXISTS(
            "USERNAME_ALREADY_EXISTS",
            "Username already exists"
    ),

    EMAIL_ALREADY_EXISTS(
            "EMAIL_ALREADY_EXISTS",
            "Email already exists"
    ),

    PHONE_ALREADY_EXISTS(
            "PHONE_ALREADY_EXISTS",
            "Phone already exists"
    );

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}