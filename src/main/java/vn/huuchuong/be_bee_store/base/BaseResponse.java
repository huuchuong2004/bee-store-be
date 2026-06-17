package vn.huuchuong.be_bee_store.base;



import lombok.*;
import vn.huuchuong.be_bee_store.auth_module.error.ErrorCode;

@Getter
@Setter
@NoArgsConstructor

@Builder

public class BaseResponse<T> {
    // Khai báo 1 kiểu để đồng nhất kiểu dưx liệu trả về cho frontend
    private boolean success;
    private String message;
    private String errorCode;
    private String errorName;
    private T data;
    private Object meta;





    public BaseResponse(T data, String message) {
        this.success = true;
        this.data = data;
        this.message = message;
        this.errorCode = null;
        this.errorName = null;
        this.meta = null;
    }


    public BaseResponse(boolean success, String message, String errorCode, String errorName, T data, Object meta) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.errorName = errorName;
        this.data = data;
        this.meta = meta;
    }


    public static <T> BaseResponse<T> success(T data, String message) {
        return BaseResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errorCode(null)
                .errorName(null)
                .meta(null)
                .build();
    }


    public static <T> BaseResponse<T> error(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errorCode(null)
                .errorName(null)
                .meta(null)
                .build();
    }


    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(errorCode.getMessage())
                .errorCode(errorCode.getCode())
                .errorName(errorCode.name())
                .data(null)
                .meta(null)
                .build();
    }


    public static <T> BaseResponse<T> error(ErrorCode errorCode, Object meta) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(errorCode.getMessage())
                .errorCode(errorCode.getCode())
                .errorName(errorCode.name())
                .data(null)
                .meta(meta)
                .build();
    }
}
