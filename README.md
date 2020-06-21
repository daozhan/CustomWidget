# 自定义VIEW示例

## 列表

![#6495ED](https://placehold.it/15/6495ED/000000?text=+)&nbsp;&nbsp;&nbsp;&nbsp;基础绘制API示例

![#6495ED](https://placehold.it/15/6495ED/000000?text=+)&nbsp;&nbsp;&nbsp;&nbsp;刻度条用法

![#6495ED](https://placehold.it/15/6495ED/000000?text=+)&nbsp;&nbsp;&nbsp;&nbsp;仿滴滴上车点和大头针动画

![#6495ED](https://placehold.it/15/6495ED/000000?text=+)&nbsp;&nbsp;&nbsp;&nbsp;仿网易云音乐鲸云音效

![#6CBE89](https://placehold.it/15/6CBE89/000000?text=+)&nbsp;&nbsp;&nbsp;&nbsp;仿滴滴首页嵌套滑动

## 详情

### 基础绘制API示例

这个示例的意图是想总结一下，在自定义view开发中常被我们所用到的api方法。之所以有了这个想法，是因为自定义view写的多了，总感觉掌握的知识点越来越杂，毫无章法。所以也就有了这么一个想串串知识点的念头。

<p align="left">
  <img width="260" height="450" src="https://github.com/MingJieZuo/CustomViewCollection/blob/master/app/src/main/assets/imgfolder/figure_view.gif">
</p>

### 刻度条用法

刻度线的绘制是通过不断旋转canvas画布来循环画线实现的，是比较常规的绘制方案。颜色的渐变效果，是获取每个刻度所对应的颜色段内等比例的16进制颜色值。此view的难点是外围文字在环绕过程中，坐标位置的确认，即依圆心坐标，半径，扇形角度，如何计算出扇形终射线与圆弧交叉点的xy坐标，所幸网上都能找到解决方案及背后的数学模型，具体代码可查看示例。

拖动的实现是在规定的区域内，当手指按下，手指滑动，手指弹起时，不断绘制对应的进度p，给人一种圆环被拖着动画的错觉，其实这只是不断重绘的结果。通过onTouchEvent方法来监听手势及获取当前坐标即可。难点在于这是一个弧形轨迹，我们怎么通过当前坐标来获取角度，再根据角度获取相对应的进度。需要注意的是，在我们拖动小图标时，我们需要定一个特定的接收事件的区域范围，只有当用户按在了规定的可滑动区域内，才能让用户拖动进度条，并不是在任意位置都能拖动小图标改变进度的，具体代码可查看示例。

<p align="left">
  <img width="300" height="580" src="https://github.com/MingJieZuo/CustomViewCollection/blob/master/app/src/main/assets/imgfolder/scale.gif">
</p>

### 仿滴滴上车点和大头针动画

考虑到完全绘制大头针会造成Ui不通用的问题，例如实际需要的效果肯定和滴滴不同，所以如果将整个大头针通过Draw进行绘制，那么在移植这个view的时候，改动就会很大。所以现将大头针分为了顶部圆圈View和下面的针Bitmap，只通过更改自定义圆圈的大小颜色等属性来最大限度的适配Ui。

大头针的加载动画和底部波纹扩散效果，是通过内部handler定时绘制的，每次改变半径和颜色。View的跳动动画这里选择通过AnimatorSet组合动画来实现。至于推荐上车点的圆点文字及描边效果也是通过View绘制实现的，具体代码可查看示例。

<p align="left">
  <img width="260" height="450" src="https://github.com/MingJieZuo/CustomViewCollection/blob/master/app/src/main/assets/imgfolder/spot.gif">
</p>

### 仿网易云音乐鲸云音效

波纹扩散效果，是通过定时改变波纹半径来实现的，此波纹是由先后两个空心圆组成，在实现过程中要注意时间和各自的尺寸变化。圆球效果同样也是定时绘制的结果，平滑运动只是错觉。在这里是每隔200ms（波纹的定时值）在相应的位置进行绘制的，由于波纹扩散周期较短，所以我将圆球的隔旋转周期定为了45度，可根据业务自行修改。这里的难点是在于怎么找到圆球的圆心坐标， 即根据圆心坐标，半径，扇形角度来求扇形终射线与圆弧交叉点的xy坐标的问题，具体代码可查看示例。

动感环绕效果是由四段贝塞尔曲线来拟合实现的。但这种方式出来的效果跟真正的动感环绕差别很大，所以鲸云音效不太可能是由这种方式实现的。如果有更贴近的实现方法，希望不吝赐教。运动中的圆环，是不断的随机更改控制点的坐标，并为起始点添加偏移量的结果，这是一个不断调试的过程…，需要不断调整控制点来控制凸起的幅度，很难找到一个完美的效果。

<p align="left">
  <img width="300" height="580" src="https://github.com/MingJieZuo/CustomViewCollection/blob/master/app/src/main/assets/imgfolder/bmusic.gif">
</p>
