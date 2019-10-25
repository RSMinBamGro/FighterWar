## MotionEvent

#### 事件类型
**事件类型**指 `MotionEvent` 对象所代表的动作。
通常使用三种类型：`ACTION_DOWN`、`ACTION_UP`、`ACTION_MOVE`，分别对应点击屏幕、离开屏幕及在屏幕上滑动。

```java
int action = MotionEventCompat.getActionMasked(event);
switch(action) {
    case MotionEvent.ACTION_DOWN:
        break;
    case MotionEvent.ACTION_MOVE:
        break;
    case MotionEvent.ACTION_UP:
        break;
    }
```

上述三种动作所产生的一系列事件组成一个**事件流**，它包括一个 `ACTION_DOWN` 事件，多个 `ACTION_MOVE` 事件和一个 `ACTION_UP` 事件。


#### 事件坐标
每个触摸事件都代表用户在屏幕上的一个动作，而每个动作必定有其发生的位置。

&emsp;· `getX()` 和 `getY()` ：通过这两个函数获得的 x , y 值是相对的坐标值，相对于消费这个事件的视图的左上点的坐标。

&emsp;· `getRawX()` 和 `getRawY()` ：通过这两个函数获得的 x , y 值是绝对坐标，是相对于屏幕的。

```java
/**
 * 以下代码截取自 ViewGroup 的函数 dispatchTransformedTouchEvent()，展示了父视图把事件分发给子视图时，getX()和getY所获得的相关坐标是如何改变的。
 * 当父视图处理事件时，上述两个函数获得的相对坐标是相对于父视图的，然后通过以下代码，调整了相对坐标的值，让其变为相对于子视图的坐标
 */
final float offsetX = mScrollX - child.mLeft;
final float offsetY = mScrollY - child.mTop;
event.offsetLocation(offsetX, offsetY);
handled = child.dispatchTouchEvent(event);
event.offsetLocation(-offsetX, -offsetY);
```


#### Pointer
`MotionEvent` 中引入了 `Pointer` 来支持多点触控，一个 pointer 即代表一个触控点，每个 pointer 都有自己的事件类型和横轴坐标值。

一个 `MotionEvent` 对象中可能存储多个 pointer 的相关信息，每个pointer都有自己的 `id` 和 `index` 。pointer 的 `id` 在整个事件流中不会变化，而 `index` 会变化。所以，当需要记录一个触摸点的事件流时，只需要保存其 `id` ,然后使用方法 `findPointerIndex(int)` 获得其 `index` 值，之后再获取其他信息。

`MotionEvent` 类中的很多方法都可以传入一个 `int` 值作为参数，其实传入的即 pointer 的 `index` 值。比如方法 `getX(pointerIndex)` 和 `getY(pointerIndex)` 。

```java
private final static int INVALID_ID = -1;
private int mActivePointerId = INVALID_ID;
private int mSecondaryPointerId = INVALID_ID;
private float mPrimaryLastX = -1;
private float mPrimaryLastY = -1;
private float mSecondaryLastX = -1;
private float mSecondaryLastY = -1;
public boolean onTouchEvent(MotionEvent event) {
    int action = MotionEventCompat.getActionMasked(event);
    switch (action) {
        case MotionEvent.ACTION_DOWN:
            int index = event.getActionIndex();
            mActivePointerId = event.getPointerId(index);
            mPrimaryLastX = MotionEventCompat.getX(event,index);
            mPrimaryLastY = MotionEventCompat.getY(event,index);
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            index = event.getActionIndex();
            mSecondaryPointerId = event.getPointerId(index);
            mSecondaryLastX = event.getX(index);
            mSecondaryLastY = event.getY(index);
            break;
        case MotionEvent.ACTION_MOVE:
            index = event.findPointerIndex(mActivePointerId);
            int secondaryIndex = MotionEventCompat.findPointerIndex(event,mSecondaryPointerId);
            final float x = MotionEventCompat.getX(event,index);
            final float y = MotionEventCompat.getY(event,index);
            final float secondX = MotionEventCompat.getX(event,secondaryIndex);
            final float secondY = MotionEventCompat.getY(event,secondaryIndex);
            break;
        case MotionEvent.ACTION_POINTER_UP:
            // 涉及pointer id的转换
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            mActivePointerId = INVALID_ID;
            mPrimaryLastX =-1;
            mPrimaryLastY = -1;
             break;
    }
    return true;
}
```

除了 pointer ，`MotionEvent` 还引入了两个事件类型以支持多点触控：

&emsp;· `ACTION_POINTER_DOWN` ：表示在已经有一个触摸点的情况下，新出现了一个触摸点。

&emsp;· `ACTION_POINTER_UP` ：表示在多个触摸点存在的情况下，其中一个触摸点消失。它与 `ACTION_UP` 的区别就是：它在多个触摸点中的一个触摸点消失时（此时，还有其他触摸点存在）产生，而 `ACTION_UP` 在最后一个触摸点消失时产生。


#### getAction 与 getActionMasked
当 `MotionEvent` 对象只包含一个触摸点的事件时，这两个函数的结果相同。

当包含多个触摸点时， `getAction` 获得的 `int` 值由 pointer 的 `index` 值和事件类型值组合而成，而 `getActionWithMasked` 只返回事件的类型值。


#### 批处理
为了保证效率，Android 系统在处理 `ACTION_MOVE` 事件时会将连续的几个多触点移动事件打包到一个 `MotionEvent` 对象中。

可以通过 `getX(int)` 和 `getY(int)` 来获得最近发生的一个触摸点事件的坐标，然后使用 `getHistorical(int,int)` 和 `getHistorical(int,int)` 来获得时间稍早的触点事件的坐标，二者是发生时间先后的关系。

因此，应该先处理通过getHistoricalXX相关函数获得的事件信息，然后再处理当前的事件信息。

```java
// Android Guide中相关的例子
void printSamples(MotionEvent ev) {
     final int historySize = ev.getHistorySize();
     final int pointerCount = ev.getPointerCount();
     for (int h = 0; h < historySize; h++) {
         System.out.printf("At time %d:", ev.getHistoricalEventTime(h));
         for (int p = 0; p < pointerCount; p++) {
             System.out.printf("  pointer %d: (%f,%f)",
                 ev.getPointerId(p), ev.getHistoricalX(p, h), ev.getHistoricalY(p, h));
         }
     }
     System.out.printf("At time %d:", ev.getEventTime());
     for (int p = 0; p < pointerCount; p++) {
         System.out.printf("  pointer %d: (%f,%f)",
             ev.getPointerId(p), ev.getX(p), ev.getY(p));
     }
 }
```



## Rect 和 RectF
`Rect` 是“Rectangle”简写的英文单词，中文意思“矩形或长方形”，Rect 对象持有一个矩形的四个 `integer` 坐标值。
`RectF` 对象持有一个矩形的四个 `float` 坐标值。

从实现的方式上看，`Rect` 是一个 `final` 类实现 `Parcelable` 接口，`RectF` 是一个普通类实现 `Parcelable` 接口。

`Rect` 和 `RectF` 除了记录的坐标数据类型不一样外，两个类提供的方法大体上都是一样的。



## Exceptions
#### <font color=RED> java.lang.NullPointerException: Attempt to invoke virtual method 'boolean android.graphics.Bitmap.isRecycled()' on a null object reference </font>
加载的图片文件是 `.bmp` 会导致导入异常，使 Bitmap 对象 `img` 为空， `.png` 文件可以。

####  <font color=RED> java.util.ConcurrentModificationException </font>
https://www.cnblogs.com/dolphin0520/p/3933551.html

#### <font color=RED> java.lang.ArrayIndexOutOfBoundsException </font>
可能存在以下情况： Frame 的方法 onDraw() 中对指定 index 处的对象进行绘制时，该对象的线程中已经判定该对象出界或发生碰撞而将该对象从对象列表中移除，此时会发生越界异常。

当多个并发线程对 Objects 类中的静态共享资源同时进行访问和修改时可能造成该异常（**读者-写者问题**）。

可以通过信号量（`Semaphore`） 解决，算法描述如下：

&emsp;为实现读进程与写进程的互斥设置信号量 `wmutex` ，同时设置整形变量 readCount 记录读进程的数量。

&emsp;只要有一个读进程在执行，写进程便不允许执行，因此仅当 `readCount == 0` 时，读进程才需要进行 `wait(wmutex)` 操作。

&emsp;`wait(wmutex)` 操作成功后，读进程执行，同时 `readCount ++` 。

&emsp;仅当读进程完成且完成操作 `readCount --` 之后，才会执行 `signal(wmutex)` 操作，此后写操作才能执行。

&emsp;由于 `readCount` 是可以被多个读进程访问的临界资源，所以也需要为其设置信号量 `rmutex` 。

```markdown
semaphore rmutex = 1, wmutex = 1;
int readCount = 0;

void read() {
    if(readCount == 0)
        wait(wmutex);

    wait(rmutex);
    readCount ++;
    signal(rmutex);

    perform read operations;

    wait(rmutex);
    readCount --;
    signal(rmutex);

    if(readCount == 0)
        wait(wmutex);
}

void write () {
    wait(wmutex);
    
    perform write operations;

    signal(wmutex);
}
```

