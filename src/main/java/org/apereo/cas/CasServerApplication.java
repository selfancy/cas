package org.apereo.cas;

import org.apereo.cas.web.CasWebApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by mike on 2020/3/26 since 1.0
 */
@SpringBootApplication
public class CasServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(CasWebApplication.class)
                .run(args);
    }
}
