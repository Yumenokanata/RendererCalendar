[![](https://jitpack.io/v/Yumenokanata/RendererCalendar.svg)](https://jitpack.io/#Yumenokanata/RendererCalendar)

# RendererCalendar
通用、具有重用性、性能良好的Calendar控件  

![sample](https://github.com/Yumenokanata/RendererCalendar/blob/master/pic.jpg)
  
1. 纯Canvas绘制，相比xml或ViewPager的实现方式性能更优
2. 可自定义Renderer渲染器，类似于Adapter的渲染方式，每个渲染器只负责“天”的方格，并提供星期等多种自定义属性，最大限度提供自定义空间
3. 使用Bitmap缓存，每个月份只会绘制一次
4. 使用Map作为每日数据的保存结构，优化数据分发（同时也提供自定义方法进行数据分发）
5. 缓存的Bitmap会被回收，不会引起频繁内存重分配
6. 独立线程计算和渲染，主线程只负责最简单的绘制，最大限度减少主线程堵塞
7. 可滑动多页（ViewPager实现方式只能一页一页滑动）
8. 自定义的滑动阻尼器，提供平滑自然的惯性阻尼效果（以后根据需要可能会开放阻尼器的自定义接口）
9. 几乎所有资源和对象在运行期都会进行回收处理，最大程度减少GC

## 添加到Android studio
Step1: 在根build.gradle中添加仓库：
```groovy
allprojects {
	repositories {
        jcenter()
		maven { url "https://jitpack.io" }
	}
}
```

Step2: 在工程中添加依赖：
```groovy
dependencies {
    compile 'com.github.Yumenokanata:RendererCalendar:x,y,z'
}
```

## 使用方式
```xml
<indi.yume.tools.renderercalendar.CalendarView
                android:id="@+id/calendar_view"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:titleTextSize="11sp"
                app:titleHeight="23dp"
                app:borderLineWidth="2dp"
                app:borderLineColor="@color/calendar_line_color"/>
```

一、xml中的控件属性：

showBorderLine  是否显示边框  布尔
borderLineWidth  边框宽度  尺寸
borderLineColor  边框颜色  颜色

titleList  上方星期的字符列表  string-array类型资源
titlePadding  星期的上下padding  尺寸
showTitle   是否显示星期title  布尔
titleTextSize   星期title的字体大小  尺寸
titleTextShowStyle  星期title的样式  包括 bold 粗体和 italic 斜体

pageLRPadding  月份page之间的间隔  尺寸

changePageVeRate  通过方法切换月份（上一个月和下一个月）时的切换速度比率（默认为2）  浮点数
backToOriOffsetVeRate  回复到对齐位置时的速度比率（默认为1）  浮点数

minFlingVelocityRate  最低的滑动速度比率（以下时不会惯性滚动）（默认为1）  浮点数
maxFlingVelocityRate  最大的滑动速度比率（即速度最大值）（默认为1）  浮点数
flingDecelerationRate  惯性滑动的减速阻尼的比率（默认为1）  浮点数


android:padding
android:paddingLeft
android:paddingRight
android:paddingTop
android:paddingBottom

eg:
```xml
<indi.yume.tools.view.renderercalendar.CalendarView
        android:id="@+id/calendarView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="1sp"
        android:paddingTop="0dp"
        app:titlePadding="10dp"
        app:titleList="@array/week_title"/>
```
        
二、Listener：
日期被点击时回调
```java
public interface OnDayClickListener {
	/* @param date 被点击的日期
	 * @param isInThisMonth 被点击的日期是否是在该page的月份里
	 */
    public void onDayClick(DayDate date, boolean isInThisMonth);
}
```

月份切换时回调
```java
public interface OnMonthChangedListener {
	// @param date 切换到的日期
    void onMonthChanged(DayDate date);
}
```

三、public方法  
1. DayDate getSelectDay()   
	返回被选择的日期（未选择时返回null）  
2. void jumpToDay(DayDate targetDay, boolean select)  
	跳转到指定日期  
	@param targetDay 跳转到的日期  
	@param select  是否将该日期设为选中  
3. void moveToBackMonth()  
	切换到上一个月  
4. void moveToNextMonth()  
	切换到下一个月  
5. void setTitleColor(int[] colorList)  
	设置星期title的字体颜色（列表中的每个对应一个星期title，从左至右）（务必与title的数量一致）  
6. void setRendererBuilder(BaseDayRendererBuilder rendererBuilder)  
	设置日历的渲染器（略,类似于AdapterRenderer项目的使用方式：BaseRendererBuilder和BaseRenderer）  
	
###License
<pre>
Copyright 2015 Yumenokanata

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
