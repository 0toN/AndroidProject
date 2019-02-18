#指定代码的压缩级别
-optimizationpasses 5

#进行除 算法指令精简、字段、类合并外的所有优化
-optimizations !code/simplification/arithmetic,!field/*,!class/merging*/

# 使混淆后的所有代码文件处于同一个目录
-repackageclasses

#忽略警告
-ignorewarnings

#apk 包内所有 class 的内部结构
-dump class_files.txt

#未混淆的类和成员
-printseeds seeds.txt

#列出从 apk 中删除的代码
-printusage unused.txt

#混淆前后的映射
-printmapping mapping.txt

# 移除日志代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String,int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# 保持哪些类不被混淆
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

# V4包
-keep class android.support.v4.**{
    public *;
}

# 保持native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#保持 Serializable 不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

 # EventBus
 -keepclassmembers class * {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }
 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
}