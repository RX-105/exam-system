package io.n0sense.examsystem.config;

import io.n0sense.examsystem.interceptor.StatisticsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringWebConfig implements WebMvcConfigurer {
    private final StatisticsInterceptor statisticsInterceptor;
    @Autowired
    public SpringWebConfig(StatisticsInterceptor statisticsInterceptor){
        this.statisticsInterceptor = statisticsInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statisticsInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/api/**");
    }
}
