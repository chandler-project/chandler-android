package com.chandlersystem.chandler.utilities;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * define all animation here
 */

public class AnimUti {
    private AnimUti() {
    }

    /**
     * An example of very simple Animation: FadeIn
     *
     * @param delay    is delayed time before animation running
     * @param duration is total time of animation
     * @return Animation
     */
    public static Animation getAnimationFadeIn(int delay, int duration) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setStartOffset(delay);
        fadeIn.setDuration(duration);
        return fadeIn;
    }

    /**
     * An example of very simple Animation: FadeOut
     *
     * @param delay    is delayed time before animation running
     * @param duration is total time of animation
     * @return Animation
     */
    public static Animation getAnimationFadeOut(int delay, int duration) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator()); //and this
        fadeOut.setStartOffset(delay);
        fadeOut.setDuration(duration);
        return fadeOut;
    }

    /**
     * @param v                represents the view
     * @param duration         the duration of animation Ex:500 Milliseconds
     * @param timeInterpolator accelerate type
     * @param fromAlpha        begin opacity
     * @param toAlpha          end opacity
     * @return
     */
    public static ObjectAnimator getFadeAnimation(View v, long duration, TimeInterpolator timeInterpolator, float fromAlpha, float toAlpha) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA, fromAlpha, toAlpha);
        animator.setDuration(duration);
        animator.setInterpolator(timeInterpolator);
        return animator;
    }
}
