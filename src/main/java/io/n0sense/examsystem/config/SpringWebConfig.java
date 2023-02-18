package io.n0sense.examsystem.config;

import io.n0sense.examsystem.interceptor.SecurityInterceptor;
import io.n0sense.examsystem.interceptor.StatisticsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringWebConfig implements WebMvcConfigurer {
    private final StatisticsInterceptor statisticsInterceptor;
    private final SecurityInterceptor securityInterceptor;
    @Autowired
    public SpringWebConfig(StatisticsInterceptor statisticsInterceptor, SecurityInterceptor securityInterceptor){
        this.statisticsInterceptor = statisticsInterceptor;
        this.securityInterceptor = securityInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statisticsInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/api/**");
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/api/**");
    }
}
