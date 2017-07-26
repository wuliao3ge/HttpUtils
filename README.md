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
	        compile 'com.github.wuliao3ge:HttpUtils:1.0.0'
	}
	
Step 3. Init
    @Override
    public void onCreate() {
        super.onCreate();
        RxRetrofitApp.init(...);
    }
