# BaseLibrary

根模块，包含一些常用工具类与基类
[GitHub仓库地址](https://github.com/DL-ZhangTeng/BaseLibrary)

## 引入

### gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.DL-ZhangTeng:BaseLibrary:2.2.0'
或排除图片库
implementation("com.github.DL-ZhangTeng:BaseLibrary:2.2.0") {
    exclude group: "com.github.bumptech.glide", module: "glide"
}
```

## 部分工具功能(安装配套插件快速创建模板文件BaseLibraryTemplatePlugin-2.0.0.jar)

### 九宫格图片工具包（com/zhangteng/base/adapter、com/zhangteng/base/widget）

| 工具包名/类名                  | 描述                                                                                                                                                                      |
|--------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| PublishAdapter           | 发布九宫格选择图片视频文件；已实现拖曳；已实现的默认图片视频逻辑，需要配合ImagePicker使用；请配合 [com.zhangteng.base.widget.GridSpacingItemDecoration][com.zhangteng.base.widget.LinearSpacingItemDecoration]调整间距 |
| NineGridViewAdapter      | 九宫格图片展示                                                                                                                                                                 |
| NineGridViewClickAdapter | 实现点击事件的九宫格图片预览                                                                                                                                                          |
| ImagePreviewAdapter      | 图片预览                                                                                                                                                                    |
| ImagePreviewActivity     | 大图预览页面                                                                                                                                                                  |
| NineGridView             | 九宫格view（使用方式参照demo中NineImageActivity）                                                                                                                                   |
| NineGridViewWrapper      | 九宫格ImageView                                                                                                                                                            |

### 多级展开树形列表（com/zhangteng/base/tree、com/zhangteng/base/adapter）

| 工具包名/类名                 | 描述                                 |
|-------------------------|------------------------------------|
| TreeRecyclerViewAdapter | 树结构的列表适配器（使用方式参照demo中TreeActivity） |
| Node                    | 树数据节点，实际数据bean自动通过注解方式转换为node      |
| TreeHelper              | 树数据与注解处理类                          |
| TreeNodeChildren        | 子节点注解                              |
| TreeNodeId              | 节点id注解                             |
| TreeNodeLabel           | 节点name注解                           |
| TreeNodeParent          | 父节点注解                              |

### 顶部导航/选项卡控件（com/zhangteng/base/widget/MyTabLayout）

| MyTabLayout属性名                    | 描述：可参考原生控件TabLayout                                  |
|-----------------------------------|------------------------------------------------------|
| tabMyGravity                      | GRAVITY_FILL充满屏幕、GRAVITY_CENTER居中                    |
| tabMyMode                         | MODE_SCROLLABLE可滚动tab、MODE_FIXED固定tab                |
| tabMyTypeface                     | 未被选中tab文本样式                                          |
| tabMySelectedTypeface             | 被选中tab文本样式                                           |
| tabMyPaddingStart                 | 居左Padding                                            |
| tabMyTextColor                    | 未选中文本颜色                                              |
| tabMyPaddingEnd                   | 居右Padding                                            |
| tabMySelectedTextColor            | 被选中文本颜色                                              |
| tabMyMaxWidth                     | 最大宽度                                                 |
| tabMyBackground                   | 背景色                                                  |
| tabMyTextAppearance               | 文本样式                                                 |
| tabMyContentStart                 | tabs距TabLayout开始位置的偏移量，只有tabMyMode                   |=MODE_SCROLLABLE时有效
| tabMyMinWidth                     | 最小宽度                                                 |
| tabMyPaddingBottom                | 距底部Padding                                           |
| tabMyPaddingTop                   | 距顶部Padding                                           |
| tabMyPadding                      | Padding                                              |
| tabMyTextIconGap                  | 父节点注解                                                |
| tabMyViewNumber                   | tab数量                                                |
| tabMySelectedTextSize             | 被选中文字大小                                              |
| tabMyTextSize                     | 未选中文字大小                                              |
| tabMyTabViewSelf                  | TabView自定义时宽度自适应                                     |
| tabMyIndicatorWidth               | 指示器宽度（设置宽度后指示器Margin失效）                              |
| tabMyIndicatorHeight              | 指示器高度                                                |
| tabMyIndicatorMarginLeft          | 指示器居左Margin                                          |
| tabMyIndicatorMarginTop           | 指示器居顶部Margin                                         |
| tabMyIndicatorMarginRight         | 指示器居右Margin                                          |
| tabMyIndicatorMarginBottom        | 指示器居底部Margin                                         |
| tabMyIndicatorSelfFit             | 指示器宽度自适应文本宽度（默认false）                                |
| tabMyIndicatorMarginBottomSelfFit | 指示器自适应文本底部的距离（tabMyIndicatorSelfFit为true时生效，默认false） |
| tabMyIndicatorBottomLayer         | 指示器置于文本底部（默认false）                                   |
| tabMyIndicatorColor               | 指示器颜色                                                |
| tabMyIndicatorRoundRadius         | 指示器圆角                                                |
| tabMyIndicatorDrawable            | 图片指示器，支持drawable与mipmap                              |

## 混淆

-keep public class com.zhangteng.**.*{ *; }

## 历史版本

| 版本      | 更新                                                                         | 更新时间                |
|---------|----------------------------------------------------------------------------|---------------------|
| v2.2.0  | List模板增加VH泛型，与之前版本不兼容                                                      | 2022/12/5 at 22:57  |
| v2.1.0  | 增加自定义LayoutManager                                                         | 2022/11/26 at 14:57 |
| v2.0.2  | 沉浸式最新版本                                                                    | 2022/11/25 at 0:03  |
| v2.0.1  | 增加FlowRadioGroup                                                           | 2022/10/26 at 10:41 |
| v2.0.0  | 增加SuperTextWatcher用于支持EditText对@的支持，独立出aop、mvp、mvvm                        | 2022/9/14 at 22:18  |
| v1.5.0  | 分离出Utils库                                                                  | 2022/9/2 at 20:28   |
| v1.4.0  | LoadViewHelper更名StateViewHelper                                            | 2022/8/11 at 11:49  |
| v1.3.8  | DataStore<Preferences>工具类                                                  | 2022/8/5 at 16:44   |
| v1.3.7  | 增加网络检测注解&权限申请注解&防重复点击注解                                                    | 2022/7/22 at 15:08  |
| v1.3.6  | 部分工具类更新优化（主要是ActivityHelper.kt）&mvvm库中异常处理使用com.zhangteng.utils.IException | 2022/7/4 at 11:50   |
| v1.3.5  | 自动设置BaseViewModel                                                          | 2022/6/25 at 10:12  |
| v1.3.4  | mvvm框架增加list基类&mvvm基类方法执行顺序优化&mvvm基类删除layoutId方法，使用默认方式设置布局文件              | 2022/6/23 at 17:33  |
| v1.3.3  | aop打印方法耗时时间                                                                | 2022/6/16 at 15:38  |
| v1.3.2  | Uri转文件绝对路径工具类                                                              | 2022/5/16 at 22:20  |
| v1.3.1  | 树形结构adapter重构                                                              | 2022/3/16 at 10:45  |
| v1.3.0  | 工具类&mvp&mvvp拆分                                                             | 2022/1/20 at 23:27  |
| v1.2.9  | 顶部悬停api调整                                                                  | 2022/1/13 at 9:35   |
| v1.2.8  | 增加加载中动画gif支持                                                               | 2022/1/11 at 11:43  |
| v1.2.7  | 适配器长按事件bug                                                                 | 2022/1/4 at 21:01   |
| v1.2.6  | 动画重命名                                                                      | 2022/1/4 at 17:4    |
| v1.2.5  | 基类弹窗修改                                                                     | 2021/12/30 at 10:34 |
| v1.2.4  | Mvvm增加Repository层                                                          | 2021/12/28 at 11:47 |
| v1.2.3  | DefaultViewHolder未设置adapter bug                                            | 2021/12/18 at 10:10 |
| v1.2.2  | 自定义Tab时bug修复                                                               | 2021/12/14 at 21:58 |
| v1.2.1  | 增加线程池工具                                                                    | 2021/11/24 at 12:34 |
| v1.2.0  | 部分工具类转为扩展函数异常处理                                                            | 2021/11/11 at 19:12 |
| v1.1.20 | 增加九宫格图片展示                                                                  | 2021/9/5 at 16:51   |
| v1.1.19 | 星型进度条（使用固定星型）、树形结构demo,父节点不需要赋值（自动处理）                                      | 2021/8/31 at 14:44  |
| v1.1.17 | TabLayout 滑动ViewPager切换时tab字体错乱问题                                          | 2021/8/26 at 17:48  |
| v1.1.16 | 增加加载中动画bug                                                                 | 2021/8/25 at 17:40  |
| v1.1.15 | MyTabLayout中tab可自定义                                                        | 2021/8/24 at 23:01  |
| v1.1.13 | 增加沉浸式主题                                                                    | 2021/8/24 at 17:58  |
| v1.1.12 | 增加加载中动画类型，并支持自定义                                                           | 2021/8/23 at 13:01  |
| v1.1.11 | 增加list基类                                                                   | 2021/8/17 at 23:11  |
| v1.1.10 | 1、加载中动画增加同步注解，不允许多线程；2、加载中动画及空页面处理,并加入base类                                | 2021/8/15 at 11:20  |
| v1.1.9  | 增加popupwindow动画                                                            | 2021/8/13 at 23:31  |
| v1.1.8  | LoadViewHelper showProgressDialog bug                                      | 2021/8/5 at 22:07   |
| v1.1.7  | 重命名LoadViewHelper方法                                                        | 2021/7/15 at 16:00  |
| v1.1.6  | fragment增加mvp基类                                                            | 2021/7/3 at 10:37   |
| v1.1.5  | BaseLibrary的类全部open                                                        | 2021/6/17 at 22:35  |
| v1.1.4  | mvp模式presenter增加Model泛型&model增加imodel                                      | 2021/5/17 at 18:38  |
| v1.1.3  | Mvp模板及demo、弹窗宽度由内容宽度决定                                                     | 2021/4/19 at 16:11  |
| v1.1.2  | BaseAdapter优化增加默认ViewHolder                                                | 2020/12/2 at 14:02  |
| v1.1.1  | BaseHttpEntity增加请求中动画自动加载                                                  | 2020/11/2 at 16:35  |
| v1.1.0  | 迁移到androidx                                                                | 2020/7/22 at 11:03  |
| v1.0.5  | BaseHttpEntity增加请求中动画自动加载                                                  | 2020/11/2 at 16:35  |
| v1.0.4  | TabLayout指示器添加距底部距离自适应属性                                                   | 2020/6/24 at 17:31  |
| v1.0.3  | 更新几个工具类                                                                    | 2020/6/19 at 11:46  |
| v1.0.2  | 指示器自适应时只有mTabIndicatorBottomLayer为true才会自适应距底部高度                           | 2020/6/12 at 15:14  |
| v1.0.1  | 无数据工具bug、增加圆角控件、文件工具类-视频判断bug                                              | 2020/6/12 at 11:56  |
| v1.0.0  | 初版                                                                         | 2020/6/4 at 18:05   |

## 赞赏

如果您喜欢BaseLibrary，或感觉BaseLibrary帮助到了您，可以点右上角“Star”支持一下，您的支持就是我的动力，谢谢

## 联系我

邮箱：763263311@qq.com/ztxiaoran@foxmail.com

## License

Copyright (c) [2020] [Swing]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
