# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\IDE\sdk/tools/proguard/proguard-android.txt
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
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#litepal混淆规则
-dontwarn org.litepal.*
-keep class org.litepal.** { *; }
-keep enum org.litepal.**
-keep interface org.litepal.** { *; }
-keep public class * extends org.litepal.**
-keepattributes *Annotation*
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * extends org.litepal.crud.DataSupport{*;}

# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# okhttp
-dontwarn okhttp3.**
-keep class okhttp3.** {*; }
#jackson
-dontpreverify
-dontwarn
-dontnote
-verbose

-keepattributes Signature,*Annotation*,*EnclosingMethod*
-keep class org.codehaus.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }

#百度地图
-keep class com.baidu.navisdk.** { *; }
-keep interface com.baidu.navisdk.** { *; }
-dontwarn com.baidu.navisdk.**

-keep class com.sinovoice.hcicloudsdk.**{*;}
-keep interface com.sinovoice.hcicloudsdk.**{*;}
-dontwarn com.sinovoice.hcicloudsdk.**
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

-dontwarn com.squareup.**
-keep class com.squareup.** {*; }

-dontwarn com.baidu.**
-keep class com.trello.** {*; }

-dontwarn com.trello.**
-keep class com.trello.** {*; }

-dontwarn org.codehaus.**
-keep class org.codehaus.** {*; }

-dontwarn cn.jpush.**
-keep class cn.jpush.** {*; }


-dontwarn okio.**
-keep class okio.** {*; }

-dontwarn retrofit2.**
-keep class retrofit2.** {*; }