package com.twg;


import com.twg.bot.utils.GetProxyOnClons;
import com.twg.bot.warframe.socket.OkHttpWebSocket;
import com.twg.common.load.LoadConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TWGApplication {
    public static void main(String[] args) {
        if (!new LoadConfig().WriteConfigFile()) {
            System.out.println("创建配置文件失败！");
            return;
        }
        if (!new LoadConfig().WriteSqlite()) {
            System.out.println("创建缓存文件失败！");
            return;
        }

        LoadConfig.initConfig();
        SpringApplication.run(TWGApplication.class, args);
        GetProxyOnClons.isHttpProxy();
        GetProxyOnClons.isSocketProxy();
        OkHttpWebSocket.init();

        System.out.println("启动成功！");
    }
}
