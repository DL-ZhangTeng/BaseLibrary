
# BaseLibrary
根模块，包含一些常用工具类与基类
[GitHub仓库地址](https://github.com/duoluo9/BaseLibrary)
## 引入
### gradle
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.DL-ZhangTeng:BaseLibrary:1.1.20'
//库所使用的三方
implementation 'com.google.android.material:material:1.2.1'
implementation 'com.alibaba:fastjson:1.2.70'

implementation  'com.scwang.smart:refresh-layout-kernel:2.0.3'      //核心必须依赖
implementation  'com.scwang.smart:refresh-header-classics:2.0.3'    //经典刷新头
implementation  'com.scwang.smart:refresh-footer-classics:2.0.3'    //经典加载

implementation 'com.github.DL-ZhangTeng:ImagePicker:1.1.7'
implementation 'com.github.chrisbanes:PhotoView:2.0.0'
```

## 部分工具功能

### MVP工具包（com/zhangteng/base/mvp/base、com/zhangteng/base/base）

工具包名/类名| 描述
--- | ---
IModel| M层接口
IView| V层接口
IPresenter| P层接口
BaseModel| M层空实现
BaseLoadingView| V层带加载中方法的接口
BaseNoNetworkView|V层带网络状态方法的接口，继承自BaseLoadingView
BaseRefreshView| V层带刷新方法的接口，继承自BaseNoNetworkView
BasePresenter| P层实现，实现了attachView、detachView、onDestroy等方法
BaseLoadingPresenter| P层加载中动画实现，增加了getBaseLoadingView()方法
BaseHttpEntity| model到presenter的回调
LoadingPresenterHandler| 使用代理的方式自动调用加载动画开启与关闭方法，同步执行方法时才有意义(好像没啥用...)
BaseMvpFragment| 使用Mvp模式Fragment基类（可使用插件自动创建Fragment。[插件仓库地址](https://github.com/duoluo9/BaseLibraryPlugin)）
BaseListMvpFragment| 使用Mvp模式列表Fragment基类
BaseMvpActivity| 使用Mvp模式Activity基类（可使用插件自动创建Activity。[插件仓库地址](https://github.com/duoluo9/BaseLibraryPlugin)）
BaseListMvpActivity| 使用Mvp模式列表Activity基类

### 九宫格图片工具包（com/zhangteng/base/adapter、com/zhangteng/base/widget）

工具包名/类名| 描述
--- | ---
PublishAdapter| 发布九宫格选择图片视频文件；已实现拖曳；已实现的默认图片视频逻辑，需要配合ImagePicker使用；请配合 [com.zhangteng.base.widget.GridSpacingItemDecoration][com.zhangteng.base.widget.LinearSpacingItemDecoration]调整间距
NineGridViewAdapter| 九宫格图片展示
NineGridViewClickAdapter| 实现点击事件的九宫格图片预览
ImagePreviewAdapter| 图片预览
ImagePreviewActivity| 大图预览页面
NineGridView|九宫格view（使用方式参照demo中NineImageActivity）
NineGridViewWrapper| 九宫格ImageView

### 多级展开树形列表（com/zhangteng/base/tree、com/zhangteng/base/adapter）

工具包名/类名| 描述
--- | ---
TreeRecyclerViewAdapter| 树结构的列表适配器（使用方式参照demo中TreeActivity）
Node| 树数据节点，实际数据bean自动通过注解方式转换为node
TreeHelper| 树数据与注解处理类
TreeNodeChildren| 子节点注解
TreeNodeId| 节点id注解
TreeNodeLabel| 节点name注解
TreeNodeParent| 父节点注解

### 顶部导航/选项卡控件（com/zhangteng/base/widget/MyTabLayout）
MyTabLayout属性名| 描述：可参考原生控件TabLayout
--- | ---
tabMyGravity| GRAVITY_FILL充满屏幕、GRAVITY_CENTER居中
tabMyMode| MODE_SCROLLABLE可滚动tab、MODE_FIXED固定tab
tabMyTypeface| 未被选中tab文本样式
tabMySelectedTypeface| 被选中tab文本样式
tabMyIndicatorColor| 指示器颜色
tabMyIndicatorHeight| 指示器高度
tabMyPaddingStart| 居左Padding
tabMyTextColor| 未选中文本颜色
tabMyPaddingEnd| 居右Padding
tabMySelectedTextColor| 被选中文本颜色
tabMyMaxWidth| 最大宽度
tabMyBackground| 背景色
tabMyTextAppearance| 文本样式
tabMyIndicatorPaddingLeft| 指示器居左Padding
tabMyIndicatorPaddingRight| 指示器居右Padding
tabMyIndicatorMarginBottom| 指示器居底部Margin
tabMyIndicatorMarginTop| 指示器居顶部Margin
tabMyIndicatorSelfFit| 指示器宽度自适应文本宽度
tabMyIndicatorMarginBottomSelfFit| 指示器自适应文本底部的距离（tabMyIndicatorSelfFit为true时生效）
tabMyIndicatorBottomLayer| 指示器置于文本底部
tabMyIndicatorRoundRadius| 指示器圆角
tabMyContentStart| tabs距TabLayout开始位置的偏移量，只有tabMyMode|=MODE_SCROLLABLE时有效
tabMyMinWidth| 最小宽度
tabMyPaddingBottom| 距底部Padding
tabMyPaddingTop| 距顶部Padding
tabMyPadding| Padding
tabMyTextIconGap| 父节点注解
tabMyViewNumber| tab数量
tabMySelectedTextSize| 被选中文字大小
tabMyTextSize| 未选中文字大小
tabMyTabViewSelf| TabView自定义时宽度自适应

## 混淆
-keep public class com.zhangteng.**.*{ *; }
## 历史版本

版本| 更新| 更新时间
--- | --- | ---
v1.1.20| 增加九宫格图片展示| 2021/9/5 at 16:51
v1.1.19| 星型进度条（使用固定星型）、树形结构demo,父节点不需要赋值（自动处理）| 2021/8/31 at 14:44
v1.1.17| TabLayout 滑动ViewPager切换时tab字体错乱问题| 2021/8/26 at 17:48
v1.1.16| 增加加载中动画bug| 2021/8/25 at 17:40
v1.1.15| MyTabLayout中tab可自定义| 2021/8/24 at 23:01
v1.1.13| 增加沉浸式主题| 2021/8/24 at 17:58
v1.1.12| 增加加载中动画类型，并支持自定义| 2021/8/23 at 13:01
v1.1.11| 增加list基类| 2021/8/17 at 23:11
v1.1.10| 1、加载中动画增加同步注解，不允许多线程；2、加载中动画及空页面处理,并加入base类| 2021/8/15 at 11:20
v1.1.9| 增加popupwindow动画| 2021/8/13 at 23:31
v1.1.8| LoadViewHelper showProgressDialog bug| 2021/8/5 at 22:07
v1.1.7| 重命名LoadViewHelper方法| 2021/7/15 at 16:00
v1.1.6| fragment增加mvp基类| 2021/7/3 at 10:37
v1.1.5| BaseLibrary的类全部open| 2021/6/17 at 22:35
v1.1.4| mvp模式presenter增加Model泛型&model增加imodel| 2021/5/17 at 18:38
v1.1.3| Mvp模板及demo、弹窗宽度由内容宽度决定| 2021/4/19 at 16:11
v1.1.2| BaseAdapter优化增加默认ViewHolder| 2020/12/2 at 14:02
v1.1.1| BaseHttpEntity增加请求中动画自动加载| 2020/11/2 at 16:35
v1.1.0| 迁移到androidx| 2020/7/22 at 11:03
v1.0.5| BaseHttpEntity增加请求中动画自动加载| 2020/11/2 at 16:35
v1.0.4| TabLayout指示器添加距底部距离自适应属性| 2020/6/24 at 17:31
v1.0.3| 更新几个工具类| 2020/6/19 at 11:46
v1.0.2| 指示器自适应时只有mTabIndicatorBottomLayer为true才会自适应距底部高度| 2020/6/12 at 15:14
v1.0.1| 无数据工具bug、增加圆角控件、文件工具类-视频判断bug| 2020/6/12 at 11:56
v1.0.0| 初版| 2020/6/4 at 18:05

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
