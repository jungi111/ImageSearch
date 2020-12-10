package com.example.imagesearch

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this,"f7b9af7f4e1684b305194430a7f45354");
    }
}