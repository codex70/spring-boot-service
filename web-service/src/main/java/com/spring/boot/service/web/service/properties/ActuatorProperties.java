package com.spring.boot.service.web.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties (prefix = "spring.security.user")
@Data
@Component
public class ActuatorProperties
{

    /*
     * actuator service user name
     */
    private String name;

    /*
     * actuator service user password
     */
    private String password;

    /*
     * actuator service roles
     */
    private String roles;
}
