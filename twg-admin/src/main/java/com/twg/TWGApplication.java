package com.twg;

import com.twg.bot.warframe.socket.OkHttpWebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

import static com.twg.bot.utils.GetProxyOnClons.isHttpProxy;
import static com.twg.bot.utils.GetProxyOnClons.isSocketProxy;


/**
 * 启动程序
 *
 * @author ruoyi
 */
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TWGApplication {
    public static void main(String[] args) {
        SpringApplication.run(TWGApplication.class, args);
        isHttpProxy();
        isSocketProxy();
        OkHttpWebSocket.init();
        System.out.println("启动成功！");
    }
}
