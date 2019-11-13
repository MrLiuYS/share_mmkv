package com.silencezhou.share_mmkv;

import com.tencent.mmkv.MMKV;

import java.util.Arrays;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** ShareMmkvPlugin */
public class ShareMmkvPlugin implements MethodCallHandler {

  public static final String KEY = "key";
  public static final String VALUE = "value";

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.silencezhou.sharemmkv");
    channel.setMethodCallHandler(new ShareMmkvPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {


    String method = call.method;
    Map<String, Object> arguments = (Map<String, Object>) call.arguments;

    String key = (String) arguments.get(KEY);
    Object value = arguments.get(VALUE);

    MMKV mmkv = MMKV.defaultMMKV();

    if (mmkv == null) {
      result.error("MMKVException", "MMKV is null.", null);
      return;
    }

    try {
      switch (method) {
        case "setBool":
          boolean setBoolStatus = mmkv.encode(key, (boolean) value);
          result.success(setBoolStatus);
          break;
        case "getBool":
          if (mmkv.contains(key)) {
            boolean getBoolStatus = mmkv.decodeBool(key);
            result.success(getBoolStatus);
          } else {
            result.success(null);
          }
          break;
        case "setInt":
          boolean setIntStatus;
          if (value instanceof Long) {
            setIntStatus = mmkv.encode(key, (long) value);
          } else {
            setIntStatus = mmkv.encode(key, (int) value);
          }
          result.success(setIntStatus);
          break;
        case "getInt":
          if (mmkv.contains(key)) {
            long getLongStatus = mmkv.decodeLong(key);
            result.success(getLongStatus);
          } else {
            result.success(null);
          }
          break;
        case "setDouble":
          boolean setDoubleStatus = mmkv.encode(key, (double) value);
          result.success(setDoubleStatus);
          break;
        case "getDouble":
          if (mmkv.contains(key)) {
            double getDoubleStatus = mmkv.decodeDouble(key);
            result.success(getDoubleStatus);
          } else {
            result.success(null);
          }
          break;
        case "setString":
          boolean setStringStatus = mmkv.encode(key, (String) value);
          result.success(setStringStatus);
          break;
        case "getString":
          String getStringStatus = mmkv.decodeString(key);
          result.success(getStringStatus);
          break;
        case "getBytes":
          byte[] getBytesStatus = mmkv.decodeBytes(key);
          result.success(getBytesStatus);
          break;
        case "contains":
          result.success(mmkv.contains(key));
          break;
        case "remove":
          mmkv.removeValueForKey(key);
          result.success(true);
          break;
        case "clear":
          mmkv.clearAll();
          result.success(true);
          break;
        case "count":
          result.success(mmkv.count());
          break;
        case "allKeys":
          String[] keys = mmkv.allKeys();
          if (keys == null) {
            result.success(null);
          } else {
            result.success(Arrays.asList(keys));
          }
          break;
        default:
          result.notImplemented();
          break;
      }

    } catch (Exception e) {
      result.error("Exception encountered", call.method, e);
    }
  }
}
