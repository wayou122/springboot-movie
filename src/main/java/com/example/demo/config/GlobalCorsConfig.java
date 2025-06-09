package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 對所有路徑有效
				.allowedOrigins("http://localhost:5173") // 允許的來源
				.allowCredentials(true) // 允許攜帶 cookie
				.allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS") // 允許的 HTTP 方法
				.allowedHeaders("*"); // 允許的 headers
	}

	//靜態資源映射
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:uploads/");
	}
}
