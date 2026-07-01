package vn.huuchuong.be_bee_store.config;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import vn.huuchuong.be_bee_store.auth_module.entity.User;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    @Named("userToId")
    default UUID userToId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("userToName")
    default String userToName(User user) {
        String fullName = user.getFirstName()+user.getLastName();
        return user != null ? fullName : null;
    }
}