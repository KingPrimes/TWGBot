<div align="center">
<img src = ".github/logo.png" width="280" height="280" alt="TWGBot"/><br>
项目编写于2020年8月12日
该项目以<a href="https://gitee.com/y_project/RuoYi-Vue" target="_blank">若依框架</a>为基础开发<br>
使用 <a href="https://github.com/MisakaTAT/Shiro" target="_blank">Shiro</a> OneBot协议的QQ机器人快速开发框架

![](https://img.shields.io/badge/JDK-1.8+-brightgreen)
![](https://img.shields.io/badge/MySql-8.5-green)
![](https://img.shields.io/badge/Redis-5.0+-green)

</div>

# 现已更名 <a href="https://github.com/KingPrimes/ZKBot"> ZeroKingBot </a> 此仓库不在更新

> 服务器资源：<br>
> <a href="https://cloud.tencent.com/act/free">腾讯云免费体验</a><br>
> <a href="https://cloud.tencent.com/act/pro/cps_3?fromSource=gwzcw.6688284.6688284.6688284&cps_key=ae3b8b6e55495d8bc53f2227ea0273d8" target="_blank">
> 腾讯云优惠</a><br>
> <a href="https://www.aliyun.com/minisite/goods?userCode=8dt5pt0g&share_source=copy_link" target="_blank">阿里云优惠</a>


> **使用方法：**<br>
> 安装**Java JDK 1.8** <br>
> 安装**MySql 8.5+** <br>
> 安装**Redis 5.0+** <br>
> 新建一个名为 **bot-server** 的数据库 编码为 **utf-8mb4** 排序规则为 **utf8mb4_0900_ai_ci**<br>
> 导入 **sql/bot-server.sql** 文件 **到你新建的数据库中**<br>
> 修改 **twg-admin/src/main/resources** 中的 **application-druid.yml** <br>
> 主库数据源 下的 **master** <br>
**username: 你的MySql用户名**<br>
**password: 你的MySql密码**<br>
> 修改 **src/main/java/com/twg/bot/enums/WarframeTypeEnum** 中的 **ADMIN_QQ(你的QQ账号)** <br>
> 此分支将不在更新,请访问 <a href="https://github.com/KingPrimes/TWGBot/tree/TWGBot-Sqlite">TWGBot-Sqlite</a> 分支


> **使用方法2-Sqlite：**<br>
> 下载 <a href="https://github.com/KingPrimes/TWGBot/releases">Releases</a> 下的 v-***-Sqlite 包文件<br>
> 1：首先解压缩文件，路径不要存在中文。<br>
> 2：配置config.ini ，管理员QQ填写你自己的QQ账号。<br>
> 3：双击安装.bat。<br>
> 4：双击启动.bat ,启动完成之后会有两个黑窗口，如果只有一个或一个都没有，请手动打开 go-cqhttp.bat与TwgBot-Server.exe<br>
> 5：初次启动需要扫码登陆你的QQ，在go-cqhttp这个黑窗口中会显示二维码，个人建议使用你的小号登陆！<br>
> 6：登陆完成之后请等待几秒钟，在go-cqhttp黑窗口中显示 ”连接成功“ 那么恭喜您，已经成功部署了机器人。<br>

<br><br>
详细视频教程：<br>
[![服务器选购与机器人部署教程](https://raw.githubusercontent.com/KingPrimes/TWGBot/TWGBot-Sqlite/.gethub/1.jpg)](https://www.bilibili.com/video/BV1DT411E7yT/ "服务器选购与机器人部署教程")<br>
[![Warframe机器人部署-更新补充](https://raw.githubusercontent.com/KingPrimes/TWGBot/TWGBot-Sqlite/.gethub/2.jpg)](https://www.bilibili.com/video/BV12B4y187da/ "Warframe机器人部署-更新补充")
<br>

### Bot-Aiml

    关键词回复模块
    --------------------------------
    主要实现了模糊关键词回复
    支持教学与回复,表情|艾特|图片|GIF图片

---------------

### Bot-Group

    群组控制模块
    ---
    入群验证码功能

---

### Bot-Image-ToGif

> ### Gif表情包生成模块
> #### 在指令后方@某个群员 如果没有@默认时你自己的头像
> **指令：**<br>
> capoo <br>
> funnny<br>
> 精神支柱<br>
---

### Bot-Server

    Bot核心模块

---

### Bot-Warframe

> ### Warframe 游戏查询模块
> 使用此模块时需要先开启功能，管理人员发送 **开启WF** 即可开启功能
> <br> 同理，发送 **关闭WF** 即可关闭功能


> 指令：<br>
> **突击**<br>
> **入侵**<br>
> **奸商**<br>
> **仲裁**<br>
> **每日特惠**<br>
> **裂隙**<br>
> **平原**<br>
> **钢铁**<br>
> **电波**<br>
> **翻译**<br>
> **紫卡倾向变动**<br>

> 查询指令用法介绍 <br>
> **/WM** 从 <a href="https://warframe.market/" target="_blank">Warframe.Market</a> 上查询物品的售价<br>
> 例如： /wm 充沛 满级、/wm ash、/wm 牛、/wm 压迫点 满级 买家 <br><br>
> **/WiKI** 拼接 <a href="https://warframe.huijiwiki.com/">灰机Wiki</a> 的搜索链接 <br><br>
> **/WR** | **/ZKWM** 从 <a href="https://warframe.market/" target="_blank">Warframe.Market</a> 上查询紫卡的售价<br>
> 例如： /wr 绝路、/wr 拉托 -爆率,多重 -有|无 <br><br>
> **/CD** | **/C** 从 <a href="https://warframe.market/" target="_blank">Warframe.Market</a> 上查询赤毒武器的售价<br>
> 例如：/CD 沙皇、/C 沙皇 <br><br>
> **/XT** | **/X** 从 <a href="https://warframe.market/" target="_blank">Warframe.Market</a> 上查询信条武器的售价<br>
> 例如：/XT 弧电、 /X 弧电

> 订阅指令<br>
> 使用订阅指令之前请发送 订阅列表查看对应的订阅内容<br>
> **订阅/取消订阅**<br>
> 此指令用于群订阅<br>
> 在订阅指令后方加上 订阅列表中的数字<br>
> 管理员/群主 可强制取消 订阅<br>
> 如： 订阅 2<br>
> 意为 ：订阅仲裁<br>
> 取消订阅 2<br>
> 意为 ： 取消订阅仲裁
> <br><br>
> **私人订阅 / 取消私人订阅**
> 此指令用于私人订阅 <br>与订阅指令不同之处 你订阅的某个内容更新时 机器人会主动@你


相关文档：<a href="https://www.yuque.com/kingprimes/twgbot" target="_blank">语雀</a><br>
如果对你有帮助还请支持一下作者<a href="https://afdian.net/@KingPrimes" target="_blank">爱发电</a>

Thanks
---
Thanks <a href="https://www.jetbrains.com/?from=TWGBot" rel="nofollow" target="_blank">JetBrains</a> Provide Free
License Support OpenSource Project

<img src="https://camo.githubusercontent.com/5816b4341e77865c19606305fd5d007098f269e38b92dae5ce93191b45e20af8/68747470733a2f2f6d696b7561632e636f6d2f696d616765732f6a6574627261696e732d76617269616e742d332e706e67" width="200" data-canonical-src="https://mikuac.com/images/jetbrains-variant-3.png" style="max-width: 100%;">
