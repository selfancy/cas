package com.haofei.cas.support;

import com.haofei.cas.utils.ApplicationUtil;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 上下文准备好后保存 BeanFactory
 *
 * Created by mike on 2020/3/31 since 1.0
 */
@Component
public class SaveApplicationListener implements ApplicationListener<SpringApplicationEvent> {

    @Override
    public void onApplicationEvent(SpringApplicationEvent event) {
        ConfigurableApplicationContext context = null;
        if (event instanceof ApplicationReadyEvent) {
            context = ((ApplicationReadyEvent) event).getApplicationContext();
        }
        if (event instanceof ApplicationPreparedEvent) {
            context = ((ApplicationPreparedEvent) event).getApplicationContext();
        }
        if (event instanceof ApplicationFailedEvent) {
            context = ((ApplicationFailedEvent) event).getApplicationContext();
        }
        if (context != null) {
            ApplicationUtil.saveBeanFactory(context.getBeanFactory());
        }
    }
}
