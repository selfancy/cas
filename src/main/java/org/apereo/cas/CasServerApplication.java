package org.apereo.cas;

import org.apereo.cas.web.CasWebApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Created by mike on 2020/3/26 since 1.0
 */
@SpringBootApplication
public class CasServerApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(CasWebApplication.class)
                .properties(PropertiesLoaderUtils.loadProperties(new ClassPathResource("jdbc.properties")))
                .run(args);
    }
}
