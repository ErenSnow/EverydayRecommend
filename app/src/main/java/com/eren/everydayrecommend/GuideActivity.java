package com.eren.everydayrecommend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.eren.everydayrecommend.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * 欢迎界面
 */
public class GuideActivity extends AppCompatActivity {
    TextView mTvTime;
    int max = 3;
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        mTvTime = (TextView) findViewById(R.id.tv_guide_time);
        mTvTime.setText(max + " s");
        Observable.timer(1, TimeUnit.SECONDS)
                .repeat(3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {//切断订阅关系
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        max--;
                        mTvTime.setText(max + " s");
                        if (max == 0) {
                            startActivity(new Intent(GuideActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
 /*               .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        max--;
                        mTvTime.setText(max + " s");
                        if (max == 0) {
                            startActivity(new Intent(GuideActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
