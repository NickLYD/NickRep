实验四  PreferenceFragment
=====
# `Preference:`
- `preferences.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
    <PreferenceCategory
        android:title="In-line preferences">
        <CheckBoxPreference
            android:key="inline"
            android:title="CheckBoxPreference"
            android:summary="This is a checkbox"
            />
    </PreferenceCategory>
    <PreferenceCategory
        android:title="Dialog-based preferences">
        <EditTextPreference
            android:key="edit"
            android:title="Edit text perference"
            android:summary="An example that uses edit text dialog"
            android:dialogTitle="Enter your favorite animal"
            android:positiveButtonText="OK"
            android:negativeButtonText="Cancel"
            />
        <ListPreference
            android:key="list"
            android:title="List perference"
            android:summary="An example that uses a list dialog"
            android:dialogTitle="Choose One"
            android:entries="@array/list_entries"
            android:entryValues="@array/list_entries_value"
            android:negativeButtonText="Cancle"
            />
    </PreferenceCategory>
    <PreferenceCategory
        android:title="Launch preferences">
        <PreferenceScreen
            android:key="ps"
            android:title="Screen perference"
            android:summary="Shows another screen of perferences">
            <CheckBoxPreference
                android:key="che"
                android:title="Toggle preference"
                android:summary="Preference that is on the next screen but same hierarchy"/>
        </PreferenceScreen>
        <PreferenceScreen
            android:key="prs"
            android:title="Intent perference"
            android:summary="Launches an Activity from an Itent">
            <intent android:action="android.intent.action.VIEW"
                android:data="http://www.baidu.com" />
        </PreferenceScreen>
    </PreferenceCategory>
    <PreferenceCategory
        android:title="Perference preferences">
        <CheckBoxPreference
            android:key="parentCheck"
            android:title="Parent checkbox preference"
            android:summary="This is visual a parent"
            />
        <CheckBoxPreference
            android:key="childCheck"
            android:title="Child checkbox preference"
            android:summary="This is visual a child"
            android:dependency="parentCheck"
            />
    </PreferenceCategory>
</PreferenceScreen>
```
- `list_entries.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="list_entries">
        <item>Alpha Option 01</item>
        <item>Beta Option 02</item>
        <item>Charlie Option 03</item>
    </string-array>
</resources>
```
- `list_entries_value.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="list_entries_value">
        <item>one</item>
        <item>two</item>
        <item>three</item>
    </string-array>
</resources>
```
- `效果截图`

![效果](https://github.com/NickLYD/NickRep/blob/master/PreferenceFragment/photos/Preference1.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/PreferenceFragment/photos/Preference2.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/PreferenceFragment/photos/Preference3.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/PreferenceFragment/photos/Preference4.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/PreferenceFragment/photos/Preference5.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/PreferenceFragment/photos/Preference6.jpg)