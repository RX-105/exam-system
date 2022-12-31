package io.n0sense.examsystem.config;

import io.n0sense.examsystem.interceptor.StatisticsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new StatisticsInterceptor())
                .addPathPatterns("/**");
    }
}
