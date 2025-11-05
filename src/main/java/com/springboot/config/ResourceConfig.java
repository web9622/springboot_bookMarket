package com.springboot.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

	@Value("${file.uploadDir}")
	private String uploadPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// ✨ 최종 안전 로직: OS 독립적인 절대 경로 URI 생성 및 매핑
		Path uploadDir = Paths.get(uploadPath).normalize();
		String absolutePathString = uploadDir.toAbsolutePath().toString();
		String fileUri = Paths.get(absolutePathString).toUri().toString();

		registry.addResourceHandler("/upload/**")
				.addResourceLocations(fileUri);
	}
	
}
