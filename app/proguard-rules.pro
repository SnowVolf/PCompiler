-keepattributes SourceFile,LineNumberTable
-dontwarn java.util.**
-dontwarn java.time.**
-dontwarn javax.**
-dontwarn com.squareup.**
-dontwarn rx.**
-dontwarn com.sun.tools.**
-dontwarn com.sun.misc.**
-dontwarn com.sun.misc.Unsafe
-dontwarn com.sun.source.**
-dontwarn com.squareup.**
-dontwarn com.google.auto.**
-dontwarn com.google.common.**
-dontwarn java.lang.ClassValue
-dontwarn com.google.j2objc.**
-dontwarn butterknife.**
-dontskipnonpubliclibraryclasses
#retrolambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
#butterknife
-keep class butterknife.*
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-dontwarn net.lingala.zip4j.**
#okhttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep interface okhttp3.**


