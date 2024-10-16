package com.francodavyd.config;

import com.francodavyd.utils.JWTUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor {

    @Autowired
    private JWTUtils jwtUtils; // Inyectamos el JWTUtils para obtener el token

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Obtenemos el JWT actual
        String jwt = jwtUtils.getCurrentJwt();

        if (jwt != null) {
            // Agregamos el token al encabezado Authorization de la solicitud
            requestTemplate.header("Authorization", "Bearer " + jwt);
        }
    }
}