package com.zq.api.task;

import com.zq.api.config.ConfigCache;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wilmiam
 * @since 2021-07-13 09:54
 */
@Component
@RequiredArgsConstructor
public class ConfigTask {


    @Scheduled(cron = "0/5 * * * * ?")
    public void updateConfig() {
        ConfigCache.update();
    }

}
