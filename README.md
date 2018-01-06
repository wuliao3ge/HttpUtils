# HttpUtils

Step 1. Add it in your root build.gradle at the end of repositories

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.wuliao3ge:HttpUtils:1.0.12'
	}
	
Step 3. Init
    @Override
    public void onCreate() {
        super.onCreate();
        RxRetrofitApp.Create(this)
                .setBaseUrl("...")
                .setDebug(true)
                .setProgressMassge("请求数据中,请稍等……");
    }

Step 4. 混淆

-keep class com.yy.yhttputils.** { *; }
-dontwarn javax.annotation.**
-dontwarn javax.inject.**

-dontwarn okhttp3.logging.**

-keep class okhttp3.internal.{*;}
-dontwarn okio.

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.ArrayQueueField* {
    long producerIndex; long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode consumerNode;
  }
-keep class $Properties
 -dontwarn rx.**

-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn java.lang.invoke.**
