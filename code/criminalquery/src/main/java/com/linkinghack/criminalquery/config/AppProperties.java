package com.linkinghack.criminalquery.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "criminal")
public class AppProperties {
}
