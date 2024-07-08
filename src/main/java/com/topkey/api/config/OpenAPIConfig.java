package com.topkey.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPIConfig 類別用於配置 OpenAPI 的相關設置。
 * 該配置類主要用於設置開發和prod的server URL，
 * 並定義 API 的一些基本信息如標題、版本、聯繫方式和許可證等。
 */
@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    // 從配置文件中獲取dev的 URL
    @Value("${bezkoder.openapi.dev-url}")
    private String devUrl;

    // 從配置文件中獲取prod的 URL
    @Value("${bezkoder.openapi.prod-url}")
    private String prodUrl;

    final String securitySchemeName = "bearerAuth";
    /**
     * 配置並返回一個 OpenAPI bean。
     * @return 配置好的 OpenAPI 實例。
     */
    @Bean
    public OpenAPI myOpenAPI() {
        // 配置dev的server
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        // 配置prod的server
        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        // 設置聯繫信息
        Contact contact = new Contact();
        contact.setEmail("ytcheng@topkey.com.tw");
        contact.setName("Topkey <拓凱實業>");
        contact.setUrl("http://www.topkey.com.tw/");

        // 設置許可證信息
        License mitLicense = new License().name("MIT 許可證").url("https://choosealicense.com/licenses/mit/");

        // 設置 API 基本信息
        Info info = new Info()
                .title("Topkey ERP API service")
                .version("1.0")
                .contact(contact)
                .description("此 API service 用於與 ERP 整合")
                .termsOfService("http://www.topkey.com.tw/")
                .license(mitLicense);



        // return 配置好的 OpenAPI 實例，包含server信息和基本資料
        return new OpenAPI().info(info).servers(List.of(devServer, prodServer))
                .components(new Components()
                .addSecuritySchemes( securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
//        return new OpenAPI()
//                .info(new Info().title("OA Integration API")
//                .version("1.0")
//                .description("API documentation for OA integration services"))
//                .addTagsItem(new Tag().name("Sales Orders").description("OA拋轉ERP-銷售單修改"))
//                .addTagsItem(new Tag().name("Travel Expenses").description("OA拋轉ERP-國內出差旅費"));
                
    }
}
