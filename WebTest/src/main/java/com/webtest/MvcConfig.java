package com.webtest;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; 
 
@Configuration
public class MvcConfig implements WebMvcConfigurer {
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        exposeDirectory("user-photos", registry);
//    }
//     
//    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
//        Path uploadDir = Paths.get(dirName);
//        String uploadPath = uploadDir.toFile().getAbsolutePath();
//         
//        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
//         
//        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
//    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		Path imageUploadDir = Paths.get("./question-images");
		String imageUploadPath = imageUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/question-images/**").addResourceLocations("file:/" + imageUploadPath + "/");
	}
}
