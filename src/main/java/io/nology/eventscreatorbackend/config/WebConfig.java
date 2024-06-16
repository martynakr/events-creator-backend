// package io.nology.eventscreatorbackend.config;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//     private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
//     public void addCorsMappings(CorsRegistry registry) {
//         String[] allowedOrigins = {"http://localhost:5173/", "http://127.0.0.1:5173"};
//         logger.info("Configuring CORS");
//         registry.addMapping("/**")
//             .allowedOrigins(allowedOrigins)
//             .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
//             .allowedHeaders("**");
//     }
// }