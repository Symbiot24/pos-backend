package com.pos.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopProfileResponse {

    private Long shopId;
    private String shopName;
    private String email;
    private String address;
    private String gstNumber;
    private String phone;
    private String supportEmail;
    private String logoUrl;
    private String footerMessage;
    private Boolean printLogo;
    private Boolean showGst;
}
