package com.zhangteng.base.base;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhangteng.base.utils.SPUtils;
import com.zhangteng.base.widget.CommonDialog;
import com.zhangteng.base.widget.CommonTitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhangteng.base.utils.SPUtils.FILE_NAME;

public abstract class MemoryActivity extends TitlebarActivity {
    private boolean isSaveStateForEver = true;
    private Map<String, Object> outState;

    @Override
    protected void initView() {
        super.initView();
        mTitlebar.setListener((v, action, extra) -> {
            if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
                if (isSaveStateForEver()) {
                    new CommonDialog(this, "是否保存已编辑的数据？")
                            .setPositiveButton("保存")
                            .setNegativeButton("取消")
                            .setDialogCancelable(false)
                            .setDialogCanceledOnTouchOutside(false)
                            .setListener((dialog, confirm) -> {
                                isSaveStateForEver = confirm;
                                dialog.dismiss();
                                finish();
                            }).show();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (isSaveStateForEver) {
            String state = (String) SPUtils.get(this, FILE_NAME, getClass().getName(), null);
            if (!TextUtils.isEmpty(state)) {
                outState = jsonToMap(state);
                restoreStateForEver(outState);
            }
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        if (isSaveStateForEver) {
            String state = (String) SPUtils.get(this, FILE_NAME, getClass().getName(), null);
            if (!TextUtils.isEmpty(state)) {
                outState = jsonToMap(state);
                restoreStateForEver(outState);
            }
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        if (isSaveStateForEver) {
            String state = (String) SPUtils.get(this, FILE_NAME, getClass().getName(), null);
            if (!TextUtils.isEmpty(state)) {
                outState = jsonToMap(state);
                restoreStateForEver(outState);
            }
        }
    }

    protected boolean isSaveStateForEver() {
        return isSaveStateForEver;
    }

    public void setSaveStateForEver(boolean saveStateForEver) {
        isSaveStateForEver = saveStateForEver;
    }

    protected abstract void saveStateForEver(Map<String, Object> outState);

    protected abstract void restoreStateForEver(Map<String, Object> savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isSaveStateForEver()) {
            if (outState == null) outState = new HashMap<>();
            saveStateForEver(outState);
            if (outState != null && !outState.isEmpty()) {
                SPUtils.put(this, FILE_NAME, getClass().getName(), JSON.toJSONString(outState));
            } else {
                SPUtils.put(this, FILE_NAME, getClass().getName(), null);
            }
        } else {
            SPUtils.put(this, FILE_NAME, getClass().getName(), null);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSaveStateForEver()) {
            new CommonDialog(this, "是否保存已编辑的数据？")
                    .setPositiveButton("保存")
                    .setNegativeButton("取消")
                    .setDialogCancelable(false)
                    .setDialogCanceledOnTouchOutside(false)
                    .setListener((dialog, confirm) -> {
                        isSaveStateForEver = confirm;
                        dialog.dismiss();
                        super.onBackPressed();
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

    private HashMap<String, Object> jsonToMap(String jsonStr) {
        HashMap<String, Object> returnMap = new HashMap<>();
        if (TextUtils.isEmpty(jsonStr)) return returnMap;
        Map<String, Object> map = JSON.parseObject(jsonStr);
        if (map == null || map.isEmpty()) return returnMap;
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value == null) continue;
            if (value instanceof JSONObject) {
                returnMap.put(key, jsonToMap(((JSONObject) value).toString()));
            } else if (value instanceof JSONArray) {
                returnMap.put(key, jsonToList(((JSONArray) value).toJSONString()));
            } else {
                returnMap.put(key, value);
            }
        }
        return returnMap;
    }

    private List<Object> jsonToList(String jsonStr) {
        ArrayList<Object> returnList = new ArrayList<>();
        if (TextUtils.isEmpty(jsonStr)) return returnList;
        List<Object> list = JSON.parseArray(jsonStr);
        if (list == null || list.isEmpty()) return returnList;
        for (int i = 0; i < list.size(); i++) {
            Object value = list.get(i);
            if (value == null) continue;
            if (value instanceof JSONObject) {
                returnList.add(i, jsonToMap(((JSONObject) value).toString()));
            } else if (value instanceof JSONArray) {
                returnList.add(i, jsonToList(((JSONArray) value).toJSONString()));
            } else {
                returnList.add(i, value);
            }
        }
        return returnList;
    }
}
