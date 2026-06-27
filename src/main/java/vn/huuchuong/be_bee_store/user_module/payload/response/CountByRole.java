package vn.huuchuong.be_bee_store.user_module.payload.response;

import lombok.Data;

@Data
public class CountByRole {

    int count_admin;
    int count_staff;
    int count_user;
}
