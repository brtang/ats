package ats;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class IpInterceptorConfigurer extends WebMvcConfigurerAdapter{
	
	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(ipInterceptor());
	}
	
	@Bean
	public IpInterceptor ipInterceptor() {
		return new IpInterceptor();
	}
	
}
