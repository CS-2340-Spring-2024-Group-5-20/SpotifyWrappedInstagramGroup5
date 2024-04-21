package com.example.spotifywrappedinstagramgroup5;

import com.example.Models.CommentModel;

import java.util.List;

public interface CommentPageCallBack {
    void onCallback(List<CommentModel> wrappedModelList);
    void onError(Exception e);
}
