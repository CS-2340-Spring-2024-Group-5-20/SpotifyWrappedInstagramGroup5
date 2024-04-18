package com.example.spotifywrappedinstagramgroup5;

import com.example.Models.User;

import java.util.List;

public interface DataCallBackSearch {
    void onCallback(List<User> users);
    void onError(Exception e);

}
