package com.example.spotifywrappedinstagramgroup5;

import java.util.List;

public interface DataCallback {
    void onCallback(List<WrappedModel> wrappedModelList);
    void onError(Exception e);
}
