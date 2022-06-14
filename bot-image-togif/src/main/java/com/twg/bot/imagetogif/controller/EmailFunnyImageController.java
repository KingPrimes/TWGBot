package com.twg.bot.imagetogif.controller;


import com.twg.bot.utils.PrivateAddApi;
import com.twg.common.jhlabs.image.ScaleFilter;
import com.twg.common.utils.image.ImageUtils;
import com.twg.common.utils.image.combiner.ImageCombiner;
import com.twg.common.utils.image.combiner.enums.OutputFormat;
import com.twg.framework.interceptor.IgnoreAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.twg.common.utils.image.ImageToGif.imagesToGif;
import static com.twg.common.utils.image.RotateImage.rotateImage;

@RestController
@RequestMapping("/Gif/Email/Funny")
public class EmailFunnyImageController {
    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getImage/{id}/{ms}")
    public void getImage(@PathVariable("id") long id, @PathVariable("ms") int ms, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "image/gif");
        //Gif图像组
        List<BufferedImage> ims = new ArrayList<>();
        //获取用户头像
        BufferedImage prk = ImageIO.read(new URL(PrivateAddApi.getPrivateHeadImage(id)));
        //对头像圆角处理
        ImageCombiner pr = new ImageCombiner(prk, OutputFormat.PNG);
        pr.setCanvasRoundCorner(200);
        //头像圆角处理
        BufferedImage privateImage = pr.combine();
        int x = 0;
        //遍历Png素材
        for (int i = 1; i <= 16; i++) {
            //素材地址
            String path = "/images-gif/email-funny/" + i + ".png";
            //加载素材到内存
            BufferedImage s = ImageUtils.getImage(path);
            //加载素材到内存
            assert s != null;
            ImageCombiner combiner = new ImageCombiner(new BufferedImage(s.getWidth(), s.getHeight(), BufferedImage.TYPE_INT_ARGB), OutputFormat.PNG);

            //写头像
            combiner.addImageElement(
                    //缩放图像
                    new ScaleFilter(80, 80)
                            //处理图像
                            .filter(
                                    //旋转图像
                                    rotateImage(privateImage, x)), 110, 80);
            //添加底图
            combiner.addImageElement(s, 0, 0);
            ims.add(combiner.combine());
            //每次旋转角度
            x += 22;
        }
        if (ms > 0) {
            response.getOutputStream().write(imagesToGif(ims, ms).toByteArray());
        } else {
            response.getOutputStream().write(imagesToGif(ims).toByteArray());
        }
    }
}
