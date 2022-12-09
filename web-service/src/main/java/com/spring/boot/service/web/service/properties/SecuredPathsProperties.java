package com.spring.boot.service.web.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties (prefix = "security.secured.paths")
@Data
@Component
public class SecuredPathsProperties
{
    /*
     * list of URLs that may be accessed by anyone
     */
    String[] permitAll = new String[0];

}
