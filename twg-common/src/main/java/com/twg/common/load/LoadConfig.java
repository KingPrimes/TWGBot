package com.twg.common.load;

import com.twg.common.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class LoadConfig {

    public static Properties prop = new Properties();

    public static void initConfig() {
        try {
            prop.load(new FileInputStream(System.getProperty("user.dir") + "/config.ini"));
        } catch (Exception ignored) {
        }
    }

    public static long getAdmin() {
        String admin = prop.getProperty("admin");
        if (StringUtils.isNumber(admin)) return Long.parseLong(admin);
        return 0;
    }

    //判断配置文件是否存在不存在则新建一个配置文件
    public boolean WriteConfigFile() {
        //config
        File file = new File("./config.ini");
        if (!file.isFile()) {
            try {
                InputStream in = LoadConfig.class.getResourceAsStream("/cfg.txt");
                assert in != null;
                Files.copy(in, file.toPath());
            } catch (Exception e) {
                return false;
            }

        }
        return file.isFile();
    }

}
