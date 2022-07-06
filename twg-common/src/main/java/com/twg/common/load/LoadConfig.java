package com.twg.common.load;

import com.google.common.io.Files;
import com.twg.common.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class LoadConfig {

    public static Properties prop = new Properties();

    public static void initConfig() {
        try {
            prop.load(new FileInputStream("./config.ini"));
        } catch (Exception ignored) {

        }
    }

    //判断配置文件是否存在不存在则新建一个配置文件
    public boolean WriteConfigFile() throws IOException {
        //config.ini
        File file = new File("./config.ini");
        if (!file.isFile()) {
            Files.copy(new File(Objects.requireNonNull(LoadConfig.class.getClassLoader().getResource("config.ini")).getPath()), file);
        }
        return file.isFile();
    }

    public long getAdmin() {
        String admin = prop.getProperty("admin");
        if (StringUtils.isNumber(admin)) return Long.parseLong(admin);
        return 0;
    }

}
