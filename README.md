# MotionLayoutSwipeFixFrameLayout

MotionLayoutの下(階層的に上)にあるViewにタッチが通過するFrameLayout。  
**MotionLayoutを入れるViewとしてこれを使う。**

Kotlinで書かれています。

# 例
このようにRecyclerViewのスクロールとMotionLayoutの`<onSwipe>`が共存しています。

![Imgur](https://imgur.com/OFNwkIu.png)

ソースはここ：https://github.com/takusan23/MotionLayoutSwipeFixFrameLayout/tree/master/app

# 特徴
- `<onSwipe>`でタッチイベントが奪われてしまう問題を解決
- `<onSwipe>`に指定した`touchAnchorId`では、`View#setOnClickListener`が使えないが、これを使うと代替となる関数を用意

# 導入

JitPackを利用して導入できます。

[![](https://jitpack.io/v/takusan23/MotionLayoutSwipeFixFrameLayout.svg)](https://jitpack.io/#takusan23/MotionLayoutSwipeFixFrameLayout)


`app`フォルダじゃない方に入っている`build.gradle`を開き、一行足します。

```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' } // これ
    }
}
```

そしたらappフォルダの方にある、`build.gradle`を開き、書き足します。

```gradle
dependencies {
    // MotionLayoutSwipeFixFrameLayout
    implementation 'com.github.takusan23:MotionLayoutSwipeFixFrameLayout:1.0.0'
    // MotionLayout
    implementation 'androidx.constraintlayout:constraintlayout:2.0.2'

    // 以下省略
}
```

# 使い方
## 1. `MotionLayout`を置くViewGroupとして、`MotionLayoutSwipeFixFrameLayout`を使う

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- ここにMotionLayoutの下にあるViewを置く。 -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/activity_main_recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <io.github.takusan23.motionlayoutswipefixframelayout.MotionLayoutSwipeFixFrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/swipe_fix_framelayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.constraintlayout.motion.widget.MotionLayout
            android:id="@+id/fragment_quick_motionlayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layoutDescription="@xml/fragment_quick_setting_scene">

        <!-- 動かすViewなど -->

        </androidx.constraintlayout.motion.widget.MotionLayout>

    </io.github.takusan23.motionlayoutswipefixframelayout.MotionLayoutSwipeFixFrameLayout>

</FrameLayout>
```



## 2. MotionLayoutのxmlファイルを書きます。(Motion Editor等使って)  
   **このとき、`<onSwipe>`には`touchRegionId`を指定しておく必要があります。**

   ```xml
    <Transition
        app:constraintSetEnd="@id/end"
        app:constraintSetStart="@+id/start">
        <OnSwipe
            app:dragDirection="dragDown"
            app:touchAnchorId="@+id/quick_setting_panel_drag"
            app:touchRegionId="@+id/quick_setting_panel_drag" />
    </Transition>
   ```

## 3. Kotlinコードを少し書いてもらいます。必須項目は以下。

| 変数名            | 入れるもの                       |
|-------------------|----------------------------------|
| `swipeTargetView` | `<OnSwipe>`で指定したView        |
| `motionLayout`    | レイアウトに置いた`MotionLayout` |


### 任意 指定した状態の時はクリックイベントを渡す
それから、指定した`<ConstraintSet>`の時はクリックイベントを無条件で渡してほしいって時があると思います。その際は以下を指定してください。  
一番上においたGIFでは、終了時の`<ConstraintSet>`を追加しています。追加することで、`swipeTargetView`以下もタッチが効くようになります。(逆に効かないようにしておくことで下のRecyclerViewがスクロール出来てます。)  

なお、手順2で`touchRegionId`を指定したので、`swipeTargetView`以外では`MotionLayout`は動作しません。

| 配列名        | 入れるもの                      |
|---------------|---------------------------------|
| `allowIdList` | `<ConstraintSet>`に割り振ったID |

### 任意 クリックイベント
`<onSwipe>`に指定したViewは`View#setOnClickListener{ }`が使えないため、以下の高階関数を用意しました。

| 名前                               | 動作           |
|------------------------------------|----------------|
| `onSwipeTargetViewClickFunc`       | シングルタップ |
| `onSwipeTargetViewDoubleClickFunc` | ダブルタップ   |


これらをふまえて書くとこんな感じ

```kotlin
/** [io.github.takusan23.motionlayoutswipefixframelayout.MotionLayoutSwipeFixFrameLayout]の設定をする */
activity_main_swipe_fix_framelayout.apply {
    allowIdList.add(R.id.fragment_video_player_motion_transition_end)
    // 以下2つは必須
    swipeTargetView = player_image_view
    motionLayout = fragment_video_player_motionlayout
    // swipeTargetViewをクリックさせたい場合は指定してね
    onSwipeTargetViewClickFunc = {
        // プレイヤー押したとき。setOnClickListener代わり
        Toast.makeText(context, "プレイヤー押した！", Toast.LENGTH_SHORT).show()
    }
    // タブルタップ版
    onSwipeTargetViewDoubleClickFunc = { ev ->
        Toast.makeText(context, "ダブルタップ", Toast.LENGTH_SHORT).show()
    }
}
```
値は各自違うと思います

# 利用例
隠し味の恋心～

- https://github.com/takusan23/MotionLayoutSwipeFixFrameLayoutExample_QuickSettingsPanel/tree/master
- このリポジトリの`app`フォルダに書いてあるAndroidアプリが上のGIFアニメになってます。

# メモ
- ConstraintLayout(MotionLayout)の重なりは`android:elevation`によって決まるそうです。なので書いたのが早い順とかではない模様
- Motion Editorが調子悪い
    - なんか同じIDの`<Constraint>`が連続で追加されたりするんだけどこれおま環？
    - xmlファイルを手動で書き換えた場合はLayout Editor開き直せ
- 並べたい時は`Chain`を使う。`width:0dp`で均等になる？

# ライセンス
```
Copyright [2020] [takusan_23]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```