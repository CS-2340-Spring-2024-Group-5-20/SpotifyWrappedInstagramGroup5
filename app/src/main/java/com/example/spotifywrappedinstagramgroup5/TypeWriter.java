package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

public class TypeWriter extends AppCompatTextView {
    private CharSequence myText;
    private int myIndex;
    private long myDelay = 150;

    private Handler myHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(myText.subSequence(0, myIndex++));
            if (myIndex <= myText.length()) {
                myHandler.postDelayed(characterAdder, myDelay);
            }
        }
    };

    public TypeWriter(@NonNull Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void animateText(CharSequence text) {
        myText = text;
        myIndex = 0;
        setText("");
        myHandler.removeCallbacks(characterAdder);
        myHandler.postDelayed(characterAdder, myDelay);
    }

    public void setCharacterDelay(long delay) {
        myDelay = delay;
    }

    // Method to start the animation
    public void startAnimation() {
        animateText(getText());
    }
}
