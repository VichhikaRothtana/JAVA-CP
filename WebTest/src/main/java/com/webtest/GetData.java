package com.webtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class GetData {
	  @Configuration
      public class MyConfiguration {
          @Bean(name = "urlService")
          public UrlService urlService() {
              return () -> "domain.com/myapp";
          }
      }

      public interface UrlService {
          String getApplicationUrl();
      }
}
