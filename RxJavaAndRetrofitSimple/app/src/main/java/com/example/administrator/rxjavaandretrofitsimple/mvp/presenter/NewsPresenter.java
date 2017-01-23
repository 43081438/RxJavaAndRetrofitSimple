package com.example.administrator.rxjavaandretrofitsimple.mvp.presenter;

import com.example.administrator.rxjavaandretrofitsimple.api.ApiManager;
import com.example.administrator.rxjavaandretrofitsimple.api.RxNetworkResponseObserver;
import com.example.administrator.rxjavaandretrofitsimple.bean.NewsEntity;
import com.example.administrator.rxjavaandretrofitsimple.mvp.model.NewsModel;
import com.example.administrator.rxjavaandretrofitsimple.mvp.presenter.base.BasePresenter;
import com.example.administrator.rxjavaandretrofitsimple.mvp.view.NewsView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：quzongyang
 *
 * 创建时间：2017/1/18
 *
 * 类描述：新闻
 */

public class NewsPresenter extends BasePresenter<NewsView, NewsModel> {
    /**
     * 获取新闻信息
     * @param type  新闻类型
     * top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
     */
    public void getNews() {
        getView().startLoadingView();
        Observable<NewsEntity> codeEntityObservable = getModel().getNews(ApiManager.getCacheControl());
        RxNetworkResponseObserver<NewsEntity> subscriber = new RxNetworkResponseObserver<NewsEntity>() {
            @Override
            public void onBeforeResponseOperation() {
                super.onBeforeResponseOperation();
                getView().hideLoadingView();
            }

            @Override
            public void onResponseFail(String msg) {
                getView().showError(msg);
            }

            @Override
            public void onResponse(NewsEntity result) {
                getView().updateView(result);
            }

            @Override
            public void onResponseStatusFail(String msgCode, String msg) {
                getView().showError(msg);
            }
        };
        codeEntityObservable.doOnNext(new Action1<NewsEntity>() {
            @Override
            public void call(NewsEntity entity) {

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                getView().startLoadingView();
            }
        }).subscribe(subscriber);
        addSubscrib(subscriber);
    }
}