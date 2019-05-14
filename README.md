## 开篇

`Shortcuts` 功能跟随着 Android7.1 Nougat 一起诞生，其主要目在于用户可以定义一些常用的操作路径，以快捷方式的形式存在，这些快捷方式展示在可以支持的设备上，帮助用户快速启动常用或者推荐的页面和行为。

最近也是有 Shortcut 相关的需求需要开发，特此进行了总结，希望可以帮助到大家。[ShortcutsDemo](https://github.com/xiaweizi/ShortcutsDemo)



## 概览

快捷方式一般是以两种方式存在：

一种通过 **长按** 应用 icon，此时会弹出列表弹窗，里面展示需要操作的路径，对于没有配置快捷方式的应用，一般只会展示分享或者应用信息的入口(不同手机可能展示不一样)。如果该应用配置了快捷方式，那么在列表中则会展示对应的快捷方式入口。

![没有配置](http://xiaweizi.top/2019-05-11-063929.png)

![配置的](http://xiaweizi.top/2019-05-11-064004.png)

如上图所示，配置和没有配置快捷方式的区别就在于，列表中是否配置自定义的快捷方式入口。

另一种则以桌面快捷方式的形式存在，同一种行为可以存在多个相同的桌面快捷方式。

![pinedShow](http://xiaweizi.top/2019-05-11-064241.gif)

如上图，对于 **Chrome-打开新的标签页** 这种行为可以有多个相同的桌面快捷方式。

### Shortcuts 类型

每个快捷方式都可以携带一个或多个 intent，当用户点击快捷方式时，每个 intent 都会触发应用中对应的操作，一般快捷方式的创建类型取决于你具体快捷方式存在的形态和你想赋予他什么样的行为。可以以下面的例子作为参考：

- 在天气应用中，想查看最近几天天气的趋势
- 在电子邮件应用中，想创建新的电子邮件
- 在地图应用中，定位一个具体的位置
- 在聊天应用中，向指定好友发送信息
- 在媒体应用中，播放电视节目的下一集
- 在游戏应用中，加载游戏最后一个保存的时间点

... ...

你可以为你的应用发布以下类型的快捷方式‘

- **静态的快捷方式：**  其直接会打包到 apk 或 apk bundle 中，安装完应用便存在快捷方式入口。
- **动态的快捷方式：** 只有在应用运行时才会创建，可以随时的更新、添加和删除对应的快捷方式。
- **桌面快捷方式：** 必须在用户授权的情况下，可以主动的添加快捷方式到桌面，同样可以拷贝动态和静态的快捷方式到桌面。

### Shortcuts 限制条件

虽然对于一个应用程序一般可以创建五个快捷方式，其中包括静态和动态的，但是但多数的设备上只能展示 **四个**。

但是桌面快捷方式是不做限制的，不过桌面快捷方式非用户主动删除的话，是没法移除的，只能通过禁用的方式让该桌面快捷方式失效。

## 使用

快捷方式可以帮助用户快速访问常用的路径和页面，从而为用户提供特定类型的内容。代码以上传至 [ShortcutsDemo](https://github.com/xiaweizi/ShortcutsDemo)

### Shortcuts 类型选择

那该如何选择快捷方式类型，这取决你的快捷方式是应用驱动还是用户驱动。虽然静态快捷方式意图不可更改，动态的可更改，但是这两种都是属于应用驱动。如果用户想自定义想要的意图，通过桌面快捷方式形式的展现，那这就是用户驱动。

怎么理解呢？用简书作为例子进行讲解：

![image-20190511163016540](http://xiaweizi.top/2019-05-11-083019.png)

- **静态快捷方式：** 这种最适合那种在整个程序的生命周期中，意图不会改变,始完成整同一种行为。鉴于程序一般只能显示四个快捷方式，那静态的快捷方式一般对于那种比较常见的行为非常有用和有必要。

  例如上图简书中，像「搜索」入口就比较常见，不需要传递参数或传递的参数不会改变，那这种就建议使用静态快捷方式。

- **动态快捷方式：** 这种一般对意图较为敏感的操作。意图可能在应用运行中发生改变，需要更新快捷方式。

  例如简书入口中的 「我的公开文章」，因为多账号的原因，可能切换账号，那跳转的页面所携带的参数就会改变，快捷方式就需要更新，这种就需要使用静态快捷方式。

- **桌面快捷方式：** 这种允许用户自定义跳转意图。

  例如简书支持将关注的人创建快捷方式到桌面，下次直接可以访问该人的动态信息，这种完全是用户自发的创建，所以使用桌面快捷方式。

### Shortcuts 类型创建

有了上述类型的具体描述，下面针对这三种快捷方式的创建进行例子介绍。

#### 创建静态快捷方式

静态快捷方式提供应用程序内的通用跳转，这些一般在整个程序的生命周期内是保持不变的。

通过以下步骤完成静态快捷方式的创建：

1. 在 `AndroidManifest.xml` 中找到配置 `android.intent.action.MAIN` 和 `android.intent.category.LAUNCHER` 的 `Activity`。

2. 添加 `<meta-data>` 元素到 Activity 中

   ```xml
   <activity android:name=".MainActivity">
     <intent-filter>
       <action android:name="android.intent.action.MAIN" />
   
       <category android:name="android.intent.category.LAUNCHER" />
     </intent-filter>
   
     <meta-data
                android:name="android.app.shortcuts"
                android:resource="@xml/shortcuts" />
   </activity>
   ```

3. 创建新的资源文件：`res/xml/shortcuts.xml`

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
       <shortcut
           android:enabled="true"
           android:icon="@drawable/add"
           android:shortcutDisabledMessage="@string/static_disabled_message"
           android:shortcutId="staticId"
           android:shortcutLongLabel="@string/static_shortcut_long_label"
           android:shortcutShortLabel="@string/static_shortcut_short_label">
           <intent
               android:action="android.intent.action.VIEW"
               android:data="content://xiaweizi.com/fromStaticShortcut"
               android:targetClass="com.example.xiaweizi.shortcutsdemo.TestActivity"
               android:targetPackage="com.example.xiaweizi.shortcutsdemo" />
           <categories android:name="android.shortcut.conversation" />
       </shortcut>
   </shortcuts>
   ```

具体参数配置说明如下：

| 标签名称                        | 描述                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| android:shortcutId              | shortcut 的唯一标识，更新删除都需要他最为关键字              |
| android:shortcutShortLabel      | 描述快捷方式的简明短语，如果可能尽可能控制为 10 个字符       |
| android:shortcutLongLabel       | 描述快捷方式的扩展短语，如果空间足够会显示此值，尽可能控制在 25 个字符 |
| android:shortcutDisabledMessage | 如果桌面快捷方式被禁用，点击会弹出此提示内容                 |
| android:enabled                 | 控制桌面快捷方式是否被禁用                                   |
| android:icon                    | 快捷方式旁边的图标                                           |
| android:action                  | intent 跳转的 action                                         |
| android:targetPackage           | 跳转页面的包名                                               |
| android:targetClass             | 跳转页面的完整路径                                           |

4. 如果有数据的传递，要有对应的解析

   ```java
   if (getIntent().getData() != null && TextUtils.equals(getIntent().getAction(), Intent.ACTION_VIEW)) {
     Uri uri = getIntent().getData();
     List<String> pathSegments = uri.getPathSegments();
     if (pathSegments != null && pathSegments.size() > 0) {
       tvTest.setText(pathSegments.get(0));
     }
   }
   ```

**最终的运行效果：**

![staticShortcut](http://xiaweizi.top/2019-05-11-070214.gif)

#### 创建动态快捷方式

动态快捷方式提供向指向应用内特定的跳转或数据传递，这些跳转和数据可能会在应用执行中发生变化。

此时需要借助 `ShortcutManager` 提供的 API 来完成动态快捷方式的相应操作：

- **创建：** 使用 `setDynamicShortcuts()` 重新定义动态快捷方式的完整列表
- **添加：** 使用 `addDynamicShortcut()` 来扩充现有的动态快捷方式列表
- **更新：** 使用 `updateShortcuts()` 方法进行更新现有的动态快捷方式列表
- **删除：** 使用 `removeDynamicShortcuts()` 移除一组动态快捷方式，或者使用 `removeAllDynamicShortcuts()` 移除所有动态快捷方式

以创建为例，其他差不多，自行尝试，具体的操作可参考下面的代码：

1. 创建 `ShortcutInfo` 对象

   ```java
   @TargetApi(Build.VERSION_CODES.N_MR1)
   private ShortcutInfo createShortcutInfo1() {
     return new ShortcutInfo.Builder(this, ID_DYNAMIC_1)
       .setShortLabel(getString(R.string.dynamic_shortcut_short_label1))
       .setLongLabel(getString(R.string.dynamic_shortcut_long_label1))
       .setIcon(Icon.createWithResource(this, R.drawable.add))
       .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://xiaweizi.cn/")))
       .build();
   }
   ```

2. 调用  `setDynamicShortcuts()` 覆盖掉之前的，重新设置新的动态快捷方式列表

   ```java
   private void setDynamicShortcuts() {
     if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
       ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
       List<ShortcutInfo> shortcutInfo = new ArrayList<>();
       shortcutInfo.add(createShortcutInfo1());
       shortcutInfo.add(createShortcutInfo2());
       if (shortcutManager != null) {
         shortcutManager.setDynamicShortcuts(shortcutInfo);
       }
     }
   }
   ```

3. 可以配置 label 的字体颜色

   ```java
   @TargetApi(Build.VERSION_CODES.N_MR1)
   private ShortcutInfo createShortcutInfo2() {
     Intent intent = new Intent(this, TestActivity.class);
     intent.setAction(Intent.ACTION_VIEW);
     intent.putExtra("key", "fromDynamicShortcut");
     ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
     String label = getResources().getString(R.string.dynamic_shortcut_short_label2);
     SpannableStringBuilder colouredLabel = new SpannableStringBuilder(label);
     colouredLabel.setSpan(colorSpan, 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
     return new ShortcutInfo.Builder(this, ID_DYNAMIC_2)
       .setShortLabel(colouredLabel)
       .setLongLabel(getString(R.string.dynamic_shortcut_long_label2))
       .setIcon(Icon.createWithResource(this, R.drawable.link))
       .setIntent(intent)
       .build();
   }
   ```

**最终的运行效果：**

![dynamicShortcut](http://xiaweizi.top/2019-05-11-073049.gif)

#### 创建桌面快捷方式

在 Android 8.0（API26）或者更高的版本上，可以创建桌面快捷方式。与静态和动态快捷方式不同，桌面快捷方式支持在设备上单独的 icon 展示。

如果想创建桌面快捷方式，按照以下步骤进行完成：

1. 使用 `isRequestPinShortcutSupported()` 来验证设备是否支持应用创建桌面快捷方式
2. 根据快捷方式是否已经存在，用下面两种方式之一来创建 `ShortcutInfo` 对象：
   1. 如果快捷方式已经存在，请创建仅包含现有快捷方式 id 的 `ShortcutInfo` 对象，系统自动查找并带上与快捷方式有关的所有相关数据
   2. 如果要固定新的快捷方式，请创建一个 `ShortcutInfo` 对象，其中包含新的快捷方式 id、意图和短标签
3. 尝试通过调用 `requestPinShortcut()` 将快捷方式固定到设备桌面上，在此过程中，可以传入 `pendingIntent` 对象，该对象仅在快捷方式成功固定时告知应用。

具体的代码可参考下面：

```java
private void createPinnedShortcuts() {
  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
    ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
    if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
      Intent intent = new Intent(this, TestActivity.class);
      intent.setAction(Intent.ACTION_VIEW);
      intent.putExtra("key", "fromPinnedShortcut");
      ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, "my-shortcut")
        .setShortLabel(getString(R.string.pinned_shortcut_short_label2))
        .setLongLabel(getString(R.string.pinned_shortcut_long_label2))
        .setIcon(Icon.createWithResource(this, R.drawable.add))
        .setIntent(intent)
        .build();
      Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo);
      PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                                                                 pinnedShortcutCallbackIntent, 0);
      boolean b = shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
      Utils.showToast(this, "set pinned shortcuts " + (b ? "success" : "failed") + "!");
    }
  }
}
```

**最终运行效果：**

![pinnedShortcut](http://xiaweizi.top/2019-05-11-073908.gif)

好了，基础简单的使用就介绍到这了，相信对 Shortcuts 的使用场景和具体实现都有一定的了解了，接下来开始介绍进阶的使用。

### Shortcuts 进阶使用

快捷方式创建完成后，可能还需要对其进行管理，比如动态更新或者禁用某些快捷方式，此时就需要了解一些进阶的使用了。

#### 移除 Shortcut

对于 **静态快捷方式** 而言，其在一开始就打包到了 apk 或者 apk bundle 中，是不允许对其进行更改的，除非发布新的版本覆盖掉之前的快捷方式，不然会一直存在。

对于 **动态快捷方式** ，既然可以在代码中进行创建，同样也可以在代码中进行移除，这个之前也介绍过。

而对于 **桌面快捷方式**，它直接展示在桌面上，始终可见，仅在以下情况才能删除桌面快捷方式。

- 用户主动移除
- 卸载与快捷方式的应用
- 用户在应用信息中，清除全部的缓存数据

#### Shortcut 展示顺序

对于一个应用上所有的快捷方式，展示的规则按照以下顺序：

1. **静态快捷方式：** `isDeclaredInManifest()` 放回 true 的快捷方式
2. **动态快捷方式**：`ShortcutInfo.isDynamic()` 返回 true 的快捷方式

在每种快捷方式中，又会按照 `ShortcutInfo.getRank()` 按等级递增的顺序排序。等级是非负的，连续的整数， 调用 `updateShortcuts(List)`，`addDynamicShortcuts(List)` 或 `setDynamicShortcuts(List)` 时，可以更新现有快捷方式的等级。

> 排名是自动调整的，因此它们对于每种类型的快捷方式都是唯一的。 例如，有三个 rank 分别为 0、1和2 的动态快捷方式，此时再添加一个 rank 为 1 的动态快捷方式放在第二的位置上，那最后两个就会被顺延一个位置，rank 变成 2和3。

#### Shortcut intents 配置

如果希望应用在用户激活快捷方式时执行多项操作，则可以将其配置为触发后多项活动。你可以通过分配多个 intent，启动一个 activity 到另一个 activity，具体的要取决你快捷方式的类型。

使用 `ShortcutInfo.Builder` 创建快捷方式时，可以使用 `setIntents()` 而不是 `setIntent()`。通过调用 `setIntents()` 你可以在用户点击快捷方式时触发多个 activity，将除列表中最后一个 activity 之外的所有 activity 放在后续堆栈中。如果此时点击返回按钮，会按照之前存储的堆栈 activity 顺序进行展示，而不是直接回到首页。

比如按照下面代码配置多个 intent：

```java
@TargetApi(Build.VERSION_CODES.N_MR1)
private ShortcutInfo createShortcutInfo1() {
  Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://xiaweizi.cn/"));
  Intent intent = new Intent(this, TestActivity.class);
  intent.setAction(Intent.ACTION_VIEW);
  intent.putExtra("key", "fromDynamicShortcut");
  return new ShortcutInfo.Builder(this, ID_DYNAMIC_1)
    .setShortLabel(getString(R.string.dynamic_shortcut_short_label1))
    .setLongLabel(getString(R.string.dynamic_shortcut_long_label1))
    .setIcon(Icon.createWithResource(this, R.drawable.add))
    .setIntents(new Intent[]{intent, intent1})
    .build();
}
```

那么它会一次触发 intent 和 intent1,此时 intent 被压入 intent1 的栈底，点击返回，则展示 intent 的信息。如图：

![intents](http://xiaweizi.top/2019-05-11-080231.gif)

还有一个问题，静态快捷方式是不能拥有自定的 `intent flag` 的，静态快捷方式始终设置为 `Intent.FLAG_ACTIVITY_NEW_TASK` 和 `Intent.FLAG_ACTIVITY_CLEAR_TASK` 这意味着，当应用程序已经在运行时，启动静态快捷方式时，应用中所有的活动都将被销毁。如果不希望出现这种情况，可以使用 trampoline activity，或者在 `Activity.onCreate(Bundle)` 中启动一个 不可见的 activity，然后调用 `Activity.finish()`。

1. 在 `AndroidManifest.xml` 中， `trampoline activity` 应用设置 `android:taskAffinity=""`。
2. 在快捷方式资源文件中，静态快捷方式中的 intent 应引用 `trampoline activity`。

#### 更新 pinned Shortcuts

每个应用最多包含 `getMaxShortcutCountPerActivity()` 个快捷方式，其中包括动态和静态的总和。但是桌面快捷方式的数量是不限制的。

当动态快捷方式被放置到桌面时，即使代码中将该动态快捷方式移除，桌面的还依然存在，因此对于桌面的快捷方式是不止 `getMaxShortcutCountPerActivity` 的限制的。

假设 `getMaxShortcutCountPerActivity()` 的值为4：

1. 聊天应用程序发布四个动态快捷方式，表示最近的四个对话(c1,c2,c3,c4)

2. 用户将所有的快捷方式复制到桌面

3. 然后用户又启动三个额外的最近对话(c5,c6和c7)，这是重新发布更新动态快捷方式，那新的快捷方式列表为：c4,c5,c6,c7。改应用必须删除过 c1,c2喝c3，因为只能展示四个快捷方式，但是桌面已经保存的这三个快捷方式是可以正常访问的。

   用户现在其实可以总共访问七个快捷方式，其中包括四个最大的动态快捷方式和桌面的三个快捷方式

4. 应用程序可以使用 `updateShortcuts(List)` 来更新上述七个任意快捷方式

5. 使用 `addDynamicShortcuts()` 和 `setDynamicShortcuts()` 同样可以更新具有相同 shortcutId 的快捷方式对象，但是他们不能跟新非动态的快捷方式。

#### 系统设置更改

系统设置的更改，比如修改系统的语言，Shortcuts 是不能动态更新的，此时需要创建广播监听 `Intent.ACTION_LOCALE_CHANGED` ,当收到广播时重新更新快捷方式，保证快捷方式展示没有问题。

处理前：

![before](http://xiaweizi.top/2019-05-11-081929.gif)

处理后：

![after](http://xiaweizi.top/2019-05-11-081940.gif)

#### track Shortcuts

为了确定静态和动态快捷方式以哪种方式出现，每次启动都会检查快捷方式的激活历史记录。可以通过调用 `reportShortcutUsed()` 方法传入其 shortcutId，提高 action 的响应速度。

#### ShortCuts 频率限制

当使用 `setDynamicShortcuts()`、`addDynamicShortcuts()` 和 `updateDynamicShortcuts()` 方法时，需要注意的是，你可能在后台调用这方法是有固定的次数限制的，那可以调用方法的次数限制就是 *rate limiting*。此功能用于防止 `ShortcutManager` 过度消耗设备资源。

当处于 *rate limiting* 中，`isRateLimitingActive()` 返回 true，但是在某些操作执行会重置这个值，因此即使是在后台应用程序也可以调用 `shortcutManager`方法，直到再次达到速率限制。这些操作包括：

1. 应用再次回到前台
2. 系统区域设置更改
3. 用户在通知栏处理嵌入的交互操作

如果在开发或者测试中遇到次数被限制的情况，可以在 开发者选项中 -> **重置 ShortcutsManager 调用频率限制 来**恢复。或者使用 adb 命令

```
adb shell cmd shortcut reset-throttling [ --user your-user-id ]
```

### 建议

在设计和创建快捷方式时，请遵循以下建议：

#### 遵循设计准则

要使应用程序的快捷方式与系统应用程序使用的快捷方式在视觉上保持一致，请遵循 [快捷方式设计指南](https://developer.android.com/shareables/design/app-shortcuts-design-guidelines.pdf)

#### 仅发布四个不同的快捷方式

尽管 API 目前支持给任何应用最多五个快捷方式(静态和动态)，但还是建议仅发布四个不同的快捷方式，以改善在设备上的视觉效果。

#### 限制快捷方式的描述长度

快捷方式的菜单空间有限，在桌面展示应用程序需要考虑到这个因素。如果可以的话，将快捷方式的 **shortLable** 长度限制在 10 个字符，并将 **longLable** 长度限制在 25个字符。

#### 记录快捷方式和其操作的历史记录

对于创建的每个快捷方式，请考虑用户在应用中是否可以直接用不同方式来完成相同的任务，需要记住的是，这种情况下，调用 `reportShortcutUsed()` ，这样 launcher 就可以提高 shortcut 对应的 actions 的响应速度。

#### 只有在 shortcuts 的意义存在时更新

当改变动态快捷方式时，只有在 shortcut 仍然保持它的含义时，调用 `updateShortcuts()` 方法改变它的信息，否则，应该使用 `addDynamicShortcuts()` 或 `setDynamicShortcuts()` 创建一个具有新含义的 shortcutId 的快捷方式。

例如，如果我们已经创建了导航到一个超市的快捷方式，如果超市的名称改变了但是位置并没有变化时，只更新信息是合适的，但是如果用户开始再一个不同位置的超市购物时，最好就是创建一个新的快捷方式。

#### 每次打开 APP 都需要检查快捷方式

在备份或恢复时，动态 shortcuts 不会被保存， 正是因为这个原因，推荐我们在需要 APP 启动和重新发布动态快捷方式时，检查 `getDynamicShortcuts()` 的对象的数量。

参考自

[官方Demo](https://github.com/googlesamples/android-AppShortcuts)

[官方文档](https://developer.android.com/guide/topics/ui/shortcuts)

[原文地址](http://xiaweizi.cn/article/a19d/)