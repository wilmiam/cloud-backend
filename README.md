## 一、cloud-backend

#### 接口改过, 需要配合改版的前端页面 前端页面地址: http://139.159.239.83:9093/framework/front-backend.git

#### 需要什么工具类common-utils中没有的可以先看下hutool文档, 提供了大量工具
https://www.hutool.cn/

## 二、打包

#### 想要单独打包某个模块必须先安装父工程，当然也可以直接在父工程install，但这样会把所有模块都安装到本地maven仓库。

#### 下面是只安装父工程不安装子模块的命令，在父工程的pom文件同级目录下执行。

    mvn clean install -N

#### 此外还要install被依赖的模块，这样你想要单独打包的模块才能正常打包。