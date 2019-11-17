## onSizeChanged()

 
Android doesn't know the real size at start, it needs to calculate it. Once it's done, onSizeChanged() will notify you with the real size.

onSizeChanged() is called once the size as been calculated. Events don't have to come from users all the time. In this example when android change the size, onSizeChanged() is called. Same thing with onDraw(), when the view should be drawn onDraw() is called.

onMeasure() is called automatically right after a call to measure().

<br/><br/>



## MotionEvent

### 事件类型
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

<br/>


### 事件坐标
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
<br/>


### Pointer
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

<br/>


### getAction 与 getActionMasked
当 `MotionEvent` 对象只包含一个触摸点的事件时，这两个函数的结果相同。

当包含多个触摸点时， `getAction` 获得的 `int` 值由 pointer 的 `index` 值和事件类型值组合而成，而 `getActionWithMasked` 只返回事件的类型值。

<br/>


### 批处理
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
<br/><br/>


## Rect 和 RectF
`Rect` 是 “Rectangle” 简写的英文单词，中文意思“矩形或长方形”，Rect 对象持有一个矩形的四个 `integer` 坐标值。
`RectF` 对象持有一个矩形的四个 `float` 坐标值。

从实现的方式上看，`Rect` 是一个 `final` 类实现 `Parcelable` 接口，`RectF` 是一个普通类实现 `Parcelable` 接口。

`Rect` 和 `RectF` 除了记录的坐标数据类型不一样外，两个类提供的方法大体上都是一样的。

<br/><br/>



## Semaphore
`Semaphore` 亦称**信号量**，用以控制同时访问特定资源的线程数量，通过协调各个线程，以保证合理的使用资源。

`Semaphore` 内部维护了一组虚拟的许可，许可的数量可以通过构造函数的参数指定。

&emsp;·访问特定资源前，必须使用方法 `acquire()` 获得许可，如果许可数量为0，该线程则一直阻塞，直到有可用许可。

&emsp;·访问资源后，使用方法 `release()` 释放许可，同时通知并唤醒等待该许可的所有进程。

`Semaphore` 和 `ReentrantLock` 类似，获取许可有**公平策略**和**非公平许可策略**，默认情况下使用非公平策略。

<br/>


### 实现原理
 ① 非公平策略

（1）方法 acquires() 实现，核心代码如下：
```java
final int nonfairTryAcquireShared(int acquires) {
    for (;;) {
        int available = getState();
        int remaining = available - acquires;
        if (remaining < 0 ||
            compareAndSetState(available, remaining))
            return remaining;
    }
}
```
`acquires` 值默认为 1 ，表示尝试获取 1 个许可，`remaining` 代表剩余的许可数。

&emsp; 如果 `remaining < 0` ，表示目前没有剩余的许可。

&emsp; 当前线程进入 `AQS` 中的 `doAcquireSharedInterruptibly` 方法等待可用许可并挂起，直到被唤醒。


（2）方法 release() 实现，核心代码如下：
```java
protected final boolean tryReleaseShared(int releases) {
    for (;;) {
        int current = getState();
        int next = current + releases;
        if (next < current) // overflow
            throw new Error("Maximum permit count exceeded");
        if (compareAndSetState(current, next))
            return true;
    }
}
```
`releases` 值默认为 1 ，表示尝试释放 1 个许可，`next` 代表如果许可释放成功，可用许可的数量。

&emsp; 通过 `unsafe.compareAndSwapInt` 修改 `state` 的值，确保同一时刻只有一个线程可以释放成功。

&emsp; 许可释放成功，当前线程进入到 `AQS` 的 `doReleaseShared` 方法，唤醒队列中等待许可的线程。

<br/>

**<font color=LIGHTBLUE>至于非公平性体现在哪里？</font>**

&emsp; 当一个线程 A 执行方法 `acquire` 时，会直接尝试获取许可，而不管同一时刻阻塞队列中是否有线程也在等待许可，如果恰好有线程 C 执行 `release` 释放许可，并唤醒阻塞队列中第一个等待的线程 B ，这个时候，线程 A 和线程 B 是*共同竞争*可用许可，不公平性就是这么体现出来的，线程 A 一点时间都没等待就和线程 B 同等对待。

<br/>


② 公平策略

（1）方法 `acpuire` 实现，核心代码如下：
```java
protected int tryAcquireShared(int acquires) {
    for (;;) {
        if (hasQueuedPredecessors())
            return -1;
        int available = getState();
        int remaining = available - acquires;
        if (remaining < 0 ||
            compareAndSetState(available, remaining))
            return remaining;
    }
}
```
`acquires` 值默认为 1 ，表示尝试获取 1 个许可，`remaining` 代表剩余的许可数。

可以看到和非公平策略相比，就多了一个对阻塞队列的检查。

&emsp; 如果阻塞队列没有等待的线程，则参与许可的竞争。

&emsp; 否则直接插入到阻塞队列尾节点并挂起，等待被唤醒。

（2）方法 `release` 的实现与非公平策略中的相同。

<br/><br/>



## Exceptions
#### <font color=RED> java.lang.NullPointerException: Attempt to invoke virtual method 'boolean android.graphics.Bitmap.isRecycled()' on a null object reference </font>
加载的图片文件是 `.bmp` 会导致导入异常，使 Bitmap 对象 `img` 为空， `.png` 文件可以。

<br/>


####  <font color=RED> java.util.ConcurrentModificationException </font>
https://www.cnblogs.com/dolphin0520/p/3933551.html

<br/>


#### <font color=RED> java.lang.ArrayIndexOutOfBoundsException </font>
可能存在以下情况： Frame 的方法 onDraw() 中对指定 index 处的对象进行绘制时，该对象的线程中已经判定该对象出界或发生碰撞而将该对象从对象列表中移除，此时会发生越界异常。

当多个并发线程对 Objects 类中的静态共享资源同时进行访问和修改时可能造成该异常（**读者-写者问题**）。

<br/>

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
    do {
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
    } while (true);
    
}

void write () {
    do {
        wait(wmutex);
        
        perform write operations;

        signal(wmutex);
    } while (true);
```

