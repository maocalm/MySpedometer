# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\liuxing\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keep public class * extends java.lang.Exception

-dontwarn com.google.android.gms.**
-keep public class com.google.android.gms.**
#admob
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.google.ads.** {
   public *;
}
-keep public class com.facebook.ads.** {
   public *;
}
# Facebook
-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}
-keep enum com.facebook.** {*;}
-dontwarn com.facebook.ads.**

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep public class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep class dalvik.system.VMRuntime {
    java.lang.String getRuntime();
}
-keep class android.os.Build {
    java.lang.String[] SUPPORTED_ABIS;
    java.lang.String CPU_ABI;
}
-keep class android.content.res.Configuration {
    android.os.LocaledList getLocales();
    java.util.Locale locale;
}
-keep class android.os.LocaledList {
    java.util.Locale get(int);
}

#greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**


#evenbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-dontwarn org.apache.http.**
-ignorewarnings
-keep class * {
    public private *;
}
-keep class com.xyz.** { *; }
-dontwarn com.xyz.**