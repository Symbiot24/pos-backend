package com.pos.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    // Shop owner details (Send to frontend after login/register)
    private Long shopId;
    private String fullName;
    private String shopName;
    private String email;

    public JwtResponse(String accessToken, String refreshToken, Long shopId, String fullName, String shopName, String email){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.shopId = shopId;
        this.fullName = fullName;
        this.shopName = shopName;
        this.email = email;
    }

}
