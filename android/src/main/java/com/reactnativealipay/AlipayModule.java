package com.reactnativealipay;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.util.H5PayResultModel;
import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;

import java.util.Map;

@ReactModule(name = AlipayModule.NAME)
public class AlipayModule extends ReactContextBaseJavaModule {
    public static final String NAME = "Alipay";

    public AlipayModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    public void multiply(int a, int b, Promise promise) {
        promise.resolve(a * b);
    }

    public static native int nativeMultiply(int a, int b);


  @ReactMethod
  public void authWithInfo(final String infoStr, final Promise promise) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        AuthTask authTask = new AuthTask(getCurrentActivity());
        Map<String, String> map = authTask.authV2(infoStr, true);
        promise.resolve(getWritableMap(map));
      }
    };
    Thread thread = new Thread(runnable);
    thread.start();
  }

  // 添加沙箱模式
  @ReactMethod
  public void setAlipaySandbox(Boolean isSandbox) {
    if(isSandbox){
      EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
    }else {
      EnvUtils.setEnv(EnvUtils.EnvEnum.ONLINE);
    }
  }

  @ReactMethod
  public void pay(final String orderInfo, final Promise promise) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        PayTask payTask = new PayTask(getCurrentActivity());
        Map<String, String> map = payTask.payV2(orderInfo, true);
        promise.resolve(getWritableMap(map));
      }
    };
    Thread thread = new Thread(runnable);
    thread.start();
  }

  @ReactMethod
  public void payInterceptorWithUrl(final String h5PayUrl, final Promise promise) {
    PayTask payTask = new PayTask(getCurrentActivity());
    payTask.payInterceptorWithUrl(h5PayUrl, true, new H5PayCallback() {
      @Override
      public void onPayResult(H5PayResultModel h5PayResultModel) {
        WritableMap map = Arguments.createMap();
        map.putString("resultCode", h5PayResultModel.getResultCode());
        map.putString("returnUrl", h5PayResultModel.getReturnUrl());
        promise.resolve(map);
      }
    });
  }

  @ReactMethod
  public void getVersion(Promise promise) {
    PayTask payTask = new PayTask(getCurrentActivity());
    promise.resolve(payTask.getVersion());
  }

  private WritableMap getWritableMap(Map<String, String> map) {
    WritableMap writableMap = Arguments.createMap();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      writableMap.putString(entry.getKey(), entry.getValue());
    }
    return writableMap;
  }
}
