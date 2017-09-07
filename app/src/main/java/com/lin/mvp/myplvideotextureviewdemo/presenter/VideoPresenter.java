package com.lin.mvp.myplvideotextureviewdemo.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.lin.mvp.myplvideotextureviewdemo.api.ApiManager;
import com.lin.mvp.myplvideotextureviewdemo.base.BasePresenterImpl;
import com.lin.mvp.myplvideotextureviewdemo.bean.NeteastVideoSummary;
import com.lin.mvp.myplvideotextureviewdemo.contract.VideoContract;
import com.lin.mvp.myplvideotextureviewdemo.global.Config;
import com.lin.mvp.myplvideotextureviewdemo.util.CacheUtil;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mvp on 2016/9/8.
 */

public class VideoPresenter extends BasePresenterImpl implements VideoContract.IVideoPresenter {
    private VideoContract.IVideoView mVideoView;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();
    private Context context;

    public VideoPresenter(Context context, VideoContract.IVideoView mVideoView) {
        this.mVideoView = mVideoView;
        mCacheUtil = CacheUtil.get(context);
        this.context = context;
    }


    @Override
    public void getVideoData(String id, int startPage) {
        mVideoView.showProgressDialog();
        addSubscription(ApiManager.getInstence().getVideoService().getVideoList(id, startPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeteastVideoSummary>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mVideoView.hidProgressDialog();
                        mVideoView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(NeteastVideoSummary videoData) {
                        mVideoView.hidProgressDialog();
                        mCacheUtil.put(Config.VIDEO, gson.toJson(videoData));
                        mVideoView.updateVideoData(videoData);
                    }
                }));
    }
}
