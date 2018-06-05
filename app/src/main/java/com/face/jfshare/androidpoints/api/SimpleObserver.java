package com.face.jfshare.androidpoints.api;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class SimpleObserver<T> implements Observer<T> {
    private Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T t) {
        success(t);
    }

    @Override
    public void onError(Throwable e) {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        error(e);
    }

    @Override
    public void onComplete() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    public abstract void success(T t);
    public abstract void error(Throwable e);



}
