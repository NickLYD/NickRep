实验二  Layout
=====
# `LinearLayout:`
- `代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    >
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="One,One"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="One,Two"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="One,Three"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="One,Four"/>

    </LinearLayout>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Two,One"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Two,Two"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Two,Three"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Two,Four"/>

    </LinearLayout>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Three,One"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Three,Two"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Three,Three"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"

            android:text="Three,Four"/>

    </LinearLayout>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Four,One"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Four,Two"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Four,Three"/>
        <Button
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Four,Four"/>

    </LinearLayout>
</LinearLayout>
```
- `效果截图`
![效果](https://github.com/NickLYD/NickRep/blob/master/Layout/photos/LinearLayout.jpg)
# `TableLayout:`
- `代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:stretchColumns="*"
    >

    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:paddingLeft="10dp"
            android:text="Open..." />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="right"
            android:text="Crtl-O" />
    </TableRow>
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:paddingLeft="10dp"
            android:text="Save..." />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="right"
            android:text="Crtl-S" />
    </TableRow>
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:paddingLeft="10dp"
            android:text="Save As..." />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="right"
            android:text="Crtl-Shift-S" />
    </TableRow>
    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#aa000000"
        />
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="X Import..." />
    </TableRow>
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="X Export..." />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="right"
            android:text="Crtl-E" />
    </TableRow>
    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#aa000000"
        />
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:paddingLeft="10dp"
            android:text="Quit" />
    </TableRow>
</TableLayout>
```
- `效果截图`
![效果](https://github.com/NickLYD/NickRep/blob/master/Layout/photos/TableLayout.jpg)
# `ConstraintLayout:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/view1"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="#FF0000"
        android:gravity="center"
        android:text="Red"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/view2"
        android:layout_width="120dp"
        android:layout_height="100dp"
        android:background="#FFA500"
        android:gravity="center"
        android:text="RORANGE"
        app:layout_constraintRight_toLeftOf="@id/view3"
        app:layout_constraintLeft_toRightOf="@id/view1"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/view3"
        android:layout_width="120dp"
        android:layout_height="100dp"
        android:background="#FFFF00"
        android:text="YELLOW"
        android:gravity="center"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/view4"
        android:layout_width="120dp"
        android:layout_height="100dp"
        android:background="#00FF00"
        android:gravity="center"
        android:text="GREEN"
        app:layout_constraintBottom_toTopOf="@id/view7"
        app:layout_constraintRight_toLeftOf="@id/view5"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toBottomOf="@id/view1"
        app:layout_constraintHorizontal_bias="0.9"/>

    <TextView
        android:id="@+id/view5"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="#0000FF"
        android:gravity="center"
        android:text="BLUE"
        app:layout_constraintBottom_toTopOf="@id/view7"
        app:layout_constraintLeft_toLeftOf="@id/view2"
        app:layout_constraintRight_toRightOf="@id/view2"
        app:layout_constraintTop_toBottomOf="@id/view2" />

    <TextView
        android:id="@+id/view6"
        android:layout_width="120dp"
        android:layout_height="100dp"
        android:background="#7B68EE"
        android:gravity="center"
        android:text="INDIGO"
        app:layout_constraintBottom_toTopOf="@id/view7"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintLeft_toRightOf="@id/view5"
        app:layout_constraintTop_toBottomOf="@id/view3"
        app:layout_constraintHorizontal_bias="0.1" />

    <TextView
        android:id="@+id/view7"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:background="#FF69B4"
        android:gravity="center"
        android:text="VIOLET"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"/>
</android.support.constraint.ConstraintLayout>
```
- `效果截图`
![效果](https://github.com/NickLYD/NickRep/blob/master/Layout/photos/ConstraintLayout.jpg)