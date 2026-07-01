package vn.huuchuong.be_bee_store.popup_noti_module.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.popup_noti_module.entity.CouponSuggestNotification;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.request.CouponSuggestNotificationsReq;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.request.UpdateCouponPopupRequest;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.response.CouponSuggestNotificationResponse;
import vn.huuchuong.be_bee_store.popup_noti_module.repository.CouponSuggestNotificationsRepository;
import vn.huuchuong.be_bee_store.popup_noti_module.service.CouponSuggestNotificationsService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponSuggestNotificationsServiceImpl implements CouponSuggestNotificationsService {

    private final CouponSuggestNotificationsRepository couponSuggestNotificationsRepository;
    private final ModelMapper modelMapper;


    @Override
    public Object getNoti() {
        List<CouponSuggestNotification> lists = couponSuggestNotificationsRepository.findAll();

        return lists.stream().map(noti -> {
            CouponSuggestNotificationResponse response = new CouponSuggestNotificationResponse();
            response.setId(noti.getId());
            response.setHeader(noti.getHeader());
            response.setTitle(noti.getTitle());
            response.setDescription(noti.getDescription());
            return response;
        }).toList();
    }

    @Override
    public CouponSuggestNotificationResponse createCouponSuggestNotification(CouponSuggestNotificationsReq request) {
        CouponSuggestNotification couponSuggestNotification = modelMapper.map(request, CouponSuggestNotification.class);
        CouponSuggestNotification saved = couponSuggestNotificationsRepository.save(couponSuggestNotification);
        return modelMapper.map(saved, CouponSuggestNotificationResponse.class);
    }

    @Override
    public CouponSuggestNotificationResponse deleteCouponSuggestNotification(int id) {
        CouponSuggestNotification couponSuggestNotification = couponSuggestNotificationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        couponSuggestNotificationsRepository.delete(couponSuggestNotification);
        return modelMapper.map(couponSuggestNotification, CouponSuggestNotificationResponse.class);
    }
    @Override
    public CouponSuggestNotificationResponse updateCouponPopup(UpdateCouponPopupRequest request, int id) {
        CouponSuggestNotification couponSuggestNotification =
                couponSuggestNotificationsRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Không tìm thấy đối tượng cần thay đổi"));

        if (request.getHeader() != null && !request.getHeader().trim().isEmpty()) {
            couponSuggestNotification.setHeader(request.getHeader());
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            couponSuggestNotification.setTitle(request.getTitle());
        }

        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            couponSuggestNotification.setDescription(request.getDescription());
        }

        CouponSuggestNotification saved = couponSuggestNotificationsRepository.save(couponSuggestNotification);

        CouponSuggestNotificationResponse response = new CouponSuggestNotificationResponse();
        response.setId(saved.getId());
        response.setHeader(saved.getHeader());
        response.setTitle(saved.getTitle());
        response.setDescription(saved.getDescription());

        return response;
    }


}
