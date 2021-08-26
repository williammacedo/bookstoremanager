package com.williammacedo.bookstoremanager.custominfoendpoint;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Component
@EndpointWebExtension(endpoint = InfoEndpoint.class)
public class InfoWebPointExtension {

    public static final int STATUS_INFO = 200;
    private InfoEndpoint delegate;

    @ReadOperation
    public WebEndpointResponse<Map<String, Object>> info() {
        Map<String, Object> info = this.delegate.info();
        Map<String, Object> customInfo = new HashMap<>(info);
        customInfo.put("customInfo", "handsOn");
        return new WebEndpointResponse<>(customInfo, STATUS_INFO);
    }

}
