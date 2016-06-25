package com.mrsmyx.yorehab.callbacks;

import com.mrsmyx.yorehab.models.YoItem;

import java.util.List;

/**
 * Created by cj on 1/24/16.
 */
public interface YoSearchListener {
    void OnItemSearchFound(List<YoItem> yoItemList);
    void OnItemSearchError(String message);
}
