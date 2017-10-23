package com.ckt.ckttodo.ui;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.ActivityScreenBinding;
import com.ckt.ckttodo.databinding.ItemLockBinding;

import io.realm.Sort;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;

public class LockScreenActivity extends SwipeUpBaseActivity {

    private HomeKeyBroadcast mHomeKeyBroadcast;
    private SelfFinishBroadCast mSelfFinishBroadCast;
    private ActivityScreenBinding mActivityScreenBinding;
    private List<EventTask> mUnFinishedTasks = new ArrayList<>();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerSelfFinishBroadCast();
        initUI();
        unLock();
    }

    private void initUI() {
        mActivityScreenBinding = DataBindingUtil.setContentView(LockScreenActivity.this, R.layout.activity_screen);
        getData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mActivityScreenBinding.rvLock.setLayoutManager(layoutManager);
        LockAdapter lockAdapter = new LockAdapter(LockScreenActivity.this, mUnFinishedTasks);
        mActivityScreenBinding.rvLock.setAdapter(lockAdapter);
    }

    private void unLock() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    private void registerHomeKeyBroadcast() {
        if (mHomeKeyBroadcast == null) {
            mHomeKeyBroadcast = new HomeKeyBroadcast(this, new HomeKeyBroadcast.OnHomeKeyListener() {
                @Override
                public void onHomeKey() {
                    Toast.makeText(getApplicationContext(), "onHomeKey", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        mHomeKeyBroadcast.registerBroadcast();
    }

    private void unRegisterHomeKeyBroadcast() {
        if (mHomeKeyBroadcast != null) {
            mHomeKeyBroadcast.unregisterBroadcast();
        }
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerHomeKeyBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterHomeKeyBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterSelfFinishBroadCast();
    }

    public void getData() {
        mUnFinishedTasks.clear();
        RealmResults<EventTask> tasks = DatebaseHelper.getInstance(LockScreenActivity.this).getRealm().where(EventTask.class).findAllSorted("taskStartTime",
                Sort.ASCENDING);
        for (EventTask task : tasks) {
            if (task.getTaskStatus() != EventTask.DONE && task.getTaskStatus() != EventTask.BLOCK && task.getTaskStatus() != EventTask.PENDING && time(task.getTaskStartTime()).equals(time(System.currentTimeMillis()))) {
                mUnFinishedTasks.add(task);
            }
        }
    }

    public String time(Long time) {
        return new SimpleDateFormat("dd").format(new Date(time)).trim();
    }

    public class SelfFinishBroadCast {

        private final KeyguardManager km;
        private Context context;
        private boolean isRegister = false;
        private BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receiveBroadcast(intent);
            }
        };

        public SelfFinishBroadCast(Context context) {
            this.context = context;
            km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        }

        public void registerBroadcast() {
            if (null != context && null != mReceiver && !isRegister) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_USER_PRESENT);
                intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
                context.registerReceiver(mReceiver, intentFilter);
                isRegister = true;
            }
        }

        public void unregisterBroadcast() {
            if (null != context && null != mReceiver && isRegister) {
                context.unregisterReceiver(mReceiver);
                isRegister = false;
            }
        }

        private void receiveBroadcast(Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_USER_PRESENT)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (km.isKeyguardSecure()) {
                        LockScreenActivity.this.finish();
                    }
                }
            }
        }

    }

    private void registerSelfFinishBroadCast() {
        if (mSelfFinishBroadCast == null) {
            mSelfFinishBroadCast = new SelfFinishBroadCast(this);
        }
        mSelfFinishBroadCast.registerBroadcast();
    }

    private void unRegisterSelfFinishBroadCast() {
        if (mSelfFinishBroadCast != null) {
            mSelfFinishBroadCast.unregisterBroadcast();
        }
    }

    public class LockAdapter extends RecyclerView.Adapter<LockViewHolder> {
        List<EventTask> eventTaskList;
        Context context;

        public LockAdapter(Context context, List<EventTask> eventTaskList) {
            this.context = context;
            this.eventTaskList = eventTaskList;
        }

        @Override
        public LockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemLockBinding itemLockBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_lock, parent, false);
            return new LockViewHolder(itemLockBinding);
        }

        @Override
        public void onBindViewHolder(LockViewHolder holder, int position) {
            holder.itemView.setTag(position);
            if (position == 0 && mUnFinishedTasks.size() == 1) {
                holder.tv_up.setVisibility(View.INVISIBLE);
                holder.tv_down.setVisibility(View.INVISIBLE);
            } else {
                if (position == 0) {
                    holder.tv_up.setVisibility(View.INVISIBLE);
                }
                if (position + 1 == mUnFinishedTasks.size()) {
                    holder.tv_down.setVisibility(View.INVISIBLE);
                }
                if(mUnFinishedTasks.size()>5 && position==4){
                    holder.tv_down.setVisibility(View.INVISIBLE);
                }
            }
            holder.setData(eventTaskList.get(position));
        }

        @Override
        public int getItemCount() {
            return (eventTaskList == null) ? 0 : (eventTaskList.size() > 5 ? 5 : eventTaskList.size());
        }

    }

    class LockViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_lockTitle;
        public TextView tv_lockTime;
        public View tv_up;
        public View tv_down;
        private ItemLockBinding itemLockBinding;

        public LockViewHolder(ItemLockBinding itemLockBinding) {
            super(itemLockBinding.getRoot());
            this.itemLockBinding = itemLockBinding;
            tv_lockTitle = itemLockBinding.tvLockTitle;
            tv_lockTime = itemLockBinding.tvLockTime;
            tv_up = itemLockBinding.tvUp;
            tv_down = itemLockBinding.tvDown;
        }

        public void setData(EventTask data) {
            itemLockBinding.setEventTask(data);
            itemLockBinding.executePendingBindings();
        }
    }
}
