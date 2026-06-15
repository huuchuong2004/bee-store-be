package vn.huuchuong.be_bee_store.base;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    // Khai báo 1 kiểu để đồng nhất kiểu dưx liệu trả về cho frontend
    private T data;
    private String message;







    // Helper cho code gọn hơn
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(data, message);
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(null, message);
    }


}
