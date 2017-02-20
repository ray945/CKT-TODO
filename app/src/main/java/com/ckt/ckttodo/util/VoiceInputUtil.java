package com.ckt.ckttodo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ckt on 2/17/17.
 */

public class VoiceInputUtil {

    private static final String TAG = "VoiceInputUtil";
    private final static String PRIVATE_NAME = "com.iflytek.setting";
    private static SpeechRecognizer mRecognizer;
    private static Toast mToast;
    private static SharedPreferences mSharedPreferences;
    private HashMap<String, String> mResults;
    private VoiceChangeListener mListener;

    public VoiceInputUtil(Context context) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + Constants.XUNFEI_APPID);
        mRecognizer = SpeechRecognizer.createRecognizer(context, mInitListener);
        mSharedPreferences = context.getSharedPreferences(PRIVATE_NAME, Context.MODE_PRIVATE);
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mResults = new HashMap<>();
    }

    public void setOnVoiceChangeListener(VoiceChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String re = results.getResultString();
            if (re == null) {
                return;
            }
            Log.d(TAG, results.getResultString());
            String finResult = returnResult(results);

            if (isLast) {
                mListener.onBackResult(finResult);
                mResults.clear();
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            mListener.onVoiceChanged(volume);
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    /**
     * 参数设置
     *
     * @return
     */
    public static void setParam() {
        // 清空参数
        mRecognizer.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mRecognizer.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mRecognizer.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mRecognizer.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mRecognizer.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    private String returnResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = "";
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "printResult: sn = " + sn + "  text = " + text);
        mResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mResults.keySet()) {
            resultBuffer.append(mResults.get(key));
        }
        Log.d(TAG, "printResult: resultBuffer = " + resultBuffer.toString());
        return resultBuffer.toString();
    }


    private void showTip(String tip) {
        mToast.setText(tip);
        mToast.show();
    }

    public void startListening() {
        setParam();
        mRecognizer.startListening(mRecognizerListener);
        Log.e(TAG, "task click " + mRecognizer.isListening());
    }

    public void stopListening() {
        mRecognizer.stopListening();
    }

    public boolean isListening() {
        return mRecognizer.isListening();
    }

    public interface VoiceChangeListener {

        void onVoiceChanged(int volume);

        void onBackResult(String result);

    }


}
