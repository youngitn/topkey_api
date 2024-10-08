package com.topkey.api.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

import io.micrometer.observation.ObservationPredicate;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class ZipkinConfig {

  @Value("${springdoc.api-docs.path}")
  private String apiDocsPath;

  @Value("${springdoc.swagger-ui.path}")
  private String swaggerPath;

  @Value("${management.endpoints.web.base-path}")
  private String actuatorPath;

  @Bean
  ObservationPredicate noopServerRequestObservationPredicate() {

    ObservationPredicate  predicate = (name, context) -> {
      if(context instanceof ServerRequestObservationContext c) {
        HttpServletRequest servletRequest = c.getCarrier();
        String requestURI = servletRequest.getRequestURI();
        String userAgent = servletRequest.getHeader("User-Agent");
        System.out.println(userAgent);
//        if(StringUtils.containsAny(requestURI, actuatorPath, swaggerPath, apiDocsPath)) {
//          return false;
//        }
      }
      if(StringUtils.equalsAny(name,"spring.security.filterchains","spring.security.authorizations","spring.security.http.secured.requests")) {
        return false;
      }
      return true;
    };

    return predicate;
  }
}