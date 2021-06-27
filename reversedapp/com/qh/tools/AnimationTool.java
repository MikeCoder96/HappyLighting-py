package com.qh.tools;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Property;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationTool {
  public static void overshootAnimation(int paramInt, View paramView) {
    TranslateAnimation translateAnimation = new TranslateAnimation(0.0F, -5.0F, 0.0F, 0.0F);
    translateAnimation.setInterpolator((Interpolator)new OvershootInterpolator());
    translateAnimation.setDuration(100L);
    translateAnimation.setRepeatCount(paramInt);
    translateAnimation.setRepeatMode(2);
    paramView.startAnimation((Animation)translateAnimation);
  }
  
  public static Animation shakeAnimation(int paramInt) {
    TranslateAnimation translateAnimation = new TranslateAnimation(0.0F, 10.0F, 0.0F, 0.0F);
    translateAnimation.setInterpolator((Interpolator)new CycleInterpolator(paramInt));
    translateAnimation.setDuration(500L);
    return (Animation)translateAnimation;
  }
  
  public static ObjectAnimator shakeKeyframe(View paramView, int paramInt1, int paramInt2) {
    Property property = View.TRANSLATION_X;
    Keyframe keyframe1 = Keyframe.ofFloat(0.0F, 0.0F);
    float f1 = -paramInt1;
    Keyframe keyframe2 = Keyframe.ofFloat(0.1F, f1);
    float f2 = paramInt1;
    return ObjectAnimator.ofPropertyValuesHolder(paramView, new PropertyValuesHolder[] { PropertyValuesHolder.ofKeyframe(property, new Keyframe[] { keyframe1, keyframe2, Keyframe.ofFloat(0.2F, f2), Keyframe.ofFloat(0.3F, f1), Keyframe.ofFloat(0.5F, f2), Keyframe.ofFloat(0.7F, f1), Keyframe.ofFloat(0.9F, f2), Keyframe.ofFloat(1.0F, 0.0F) }) }).setDuration(paramInt2);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\AnimationTool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */