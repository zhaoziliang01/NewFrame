package com.face.jfshare.androidpoints.adapter.recycle;

/**
 * 针对Recycleview 的adapter 工具类
 *
 */
public interface MultiItemTypeSupport<T> {

    int getLayoutId(int viewType);

    int getItemViewType(int position, T t);

}
