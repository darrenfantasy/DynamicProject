#your dex.loader patterns here

-keep class com.iqiyi.android.qigsaw.core.Qigsaw {
    <init>(...);
    void install(...);
}

-keep class * implements com.iqiyi.android.qigsaw.core.splitdownload.Downloader {
    <init>(...);
}

# ${yourApplicationId}.QigsawConfig, QigsawVersion >= 1.2.2
-keep class **.QigsawConfig {
    *;
}

