package com.ym.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 服务端配置类
 */
@Component
@ConfigurationProperties(prefix="fileServer")
@Data
public class FileServerConfiger {

    /** 文件保存路径 **/
    private String pathDirectory;

    /** token**/
   // private String token;

    /** 服务端私钥**/
    private String serverPrivateKey;

    /** 服务端公钥**/
    private String serverPublicKey;

    /** 客户端端公钥**/
    private String clientPublicKey;
}
