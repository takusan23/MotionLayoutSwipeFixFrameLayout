# MotionLayoutSwipeFixFrameLayout

MotionLayoutの下(階層的に上)にあるViewにタッチが通過するFrameLayout。  
**MotionLayoutを入れるViewとしてこれを使う。**

Kotlinで書かれています。

# 例
このようにRecyclerViewのスクロールとMotionLayoutの`<onSwipe>`が共存しています。

![Imgur](https://imgur.com/lhJB6Vr.gif)

ソースはここ：https://github.com/takusan23/MotionLayoutSwipeFixFrameLayout/tree/master/app

# 特徴
- `<onSwipe>`でタッチイベントが奪われてしまう問題を解決
- `<onSwipe>`に指定した`touchAnchorId`では、`View#setOnClickListener`が使えない問題を、高階関数を利用することで解決。

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

### 任意 swipeTargetViewの上にViewを重ねる場合
重ねたViewに、`View.setOnClickListener`をつけると、もれなく`onSwipeTargetViewClickFunc`が呼ばれてしまいます。  
さらに、まれによく`View.setOnClickListener`すら呼ばれない時があるため、以下の手順を踏む必要があります。

例：プレイヤーの上に置く再生ボタン等

#### blockViewList配列に重ねるViewを追加する
`blockViewList`に追加したViewをクリックすると`onSwipeTargetViewClickFunc`は呼ばれず、代わりに`onBlockViewClickFunc`を呼びます。

`View.setOnClickListener`で動く場合は良いのですが、動かない場合があるので、その際は`onBlockViewClickFunc`でクリックイベントを処理してください。

#### blockViewListへ追加するViewが多い場合
登録を楽にする関数を用意しました。

|関数名|機能|
|---|---|
|`addAllIsClickableViewFromParentView`|引数に入れたViewGroup内から、`View.isClickable`が`true`のViewを登録します。これは再帰的に動くため、ViewGroup内にあるViewGroupも登録されます。|
|`getChildViewRecursive`|再帰的にViewを探し出してView配列を返す関数。ViewGroup内にあるViewGroupも登録される。`addAllIsClickableViewFromParentView`の内部で使っているが、プライベートな関数ではないので利用できます。|

これらをふまえて書くとこんな感じ

```kotlin
/** [io.github.takusan23.motionlayoutswipefixframelayout.MotionLayoutSwipeFixFrameLayout]の設定をする */
activity_main_swipe_fix_framelayout.apply {
    allowIdList.add(R.id.fragment_video_player_motion_transition_end)
    // 以下2つは必須
    swipeTargetView = player_framelayout
    motionLayout = fragment_video_player_motionlayout
    
    // swipeTargetViewをクリックさせたい場合は指定してね
    onSwipeTargetViewClickFunc = {
        // プレイヤー押したとき。setOnClickListener代わり
        showToast("プレイヤー押した！")
    }
    // タブルタップ版
    onSwipeTargetViewDoubleClickFunc = { ev ->
        showToast("だぶるたっぷ")
    }
    
    // swipeTargetViewの上にViewを重ねるて、そのViewにクリックイベントを付ける場合は以下の配列にそのViewを入れてください。（この例だと再生ボタン）
    // blockViewList.add(player_fragment_play_button)
    // もしblockViewListへ追加するViewが多い場合は、isClickableがtrueになっているViewを再帰的に取得する（すべて取得する）関数があります
    addAllIsClickableViewFromParentView(player_framelayout)
    
    // blockViewListに追加したViewが押されたとき
    onBlockViewClickFunc = { view ->
        if (view?.id == player_fragment_play_button?.id) {
            showToast("再生")
        }
    }
}
```
値は各自違うと思います

# 利用例
隠し味の恋心～

- https://github.com/takusan23/MotionLayoutSwipeFixFrameLayoutExample_QuickSettingsPanel/tree/master

- ちなみに一番上においたGIFは、このリポジトリにある`app`フォルダを参照してください
    - MainActivityには動画一覧RecyclerViewが、その上にMotionLayoutを使ったFragment(VideoPlayerFragement)が乗っています。

# メモ
- ConstraintLayout(MotionLayout)の重なりは`android:elevation`によって決まるそうです。なので書いたのが早い順とかではない模様
- Motion Editorが調子悪い
    - なんか同じIDの`<Constraint>`が連続で追加されたりするんだけどこれおま環？
    - xmlファイルを手動で書き換えた場合はLayout Editor開き直せ
- 並べたい時は`Chain`を使う。`width:0dp`で均等になる？
- `View#rawX`は画面から見ての座標。`View#x`は親Viewから見ての位置

# ライセンス
```
Copyright 2020 takusan_23

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