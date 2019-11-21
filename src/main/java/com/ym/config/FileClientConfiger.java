package com.ym.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="fileClient")
@Data
public class FileClientConfiger {

    /** token**/
    //private String token;

    /** 客户端私钥**/
    private String clientPrivateKey;

    /** 客户端公钥**/
    private String clientPublicKey;

    /** 服务端公钥**/
    private String serverPublicKey;
}
