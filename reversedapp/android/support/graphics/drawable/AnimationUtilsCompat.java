package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AnimationUtilsCompat {
  private static Interpolator createInterpolatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    PathInterpolatorCompat pathInterpolatorCompat;
    int i = paramXmlPullParser.getDepth();
    paramResources = null;
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        LinearInterpolator linearInterpolator;
        AccelerateInterpolator accelerateInterpolator;
        DecelerateInterpolator decelerateInterpolator;
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
        CycleInterpolator cycleInterpolator;
        AnticipateInterpolator anticipateInterpolator;
        OvershootInterpolator overshootInterpolator;
        AnticipateOvershootInterpolator anticipateOvershootInterpolator;
        BounceInterpolator bounceInterpolator;
        if (j != 2)
          continue; 
        AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
        String str = paramXmlPullParser.getName();
        if (str.equals("linearInterpolator")) {
          linearInterpolator = new LinearInterpolator();
          continue;
        } 
        if (linearInterpolator.equals("accelerateInterpolator")) {
          accelerateInterpolator = new AccelerateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (accelerateInterpolator.equals("decelerateInterpolator")) {
          decelerateInterpolator = new DecelerateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (decelerateInterpolator.equals("accelerateDecelerateInterpolator")) {
          accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
          continue;
        } 
        if (accelerateDecelerateInterpolator.equals("cycleInterpolator")) {
          cycleInterpolator = new CycleInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (cycleInterpolator.equals("anticipateInterpolator")) {
          anticipateInterpolator = new AnticipateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (anticipateInterpolator.equals("overshootInterpolator")) {
          overshootInterpolator = new OvershootInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (overshootInterpolator.equals("anticipateOvershootInterpolator")) {
          anticipateOvershootInterpolator = new AnticipateOvershootInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (anticipateOvershootInterpolator.equals("bounceInterpolator")) {
          bounceInterpolator = new BounceInterpolator();
          continue;
        } 
        if (bounceInterpolator.equals("pathInterpolator")) {
          pathInterpolatorCompat = new PathInterpolatorCompat(paramContext, attributeSet, paramXmlPullParser);
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown interpolator name: ");
        stringBuilder.append(paramXmlPullParser.getName());
        throw new RuntimeException(stringBuilder.toString());
      } 
      break;
    } 
    return pathInterpolatorCompat;
  }
  
  public static Interpolator loadInterpolator(Context paramContext, int paramInt) throws Resources.NotFoundException {
    if (Build.VERSION.SDK_INT >= 21)
      return AnimationUtils.loadInterpolator(paramContext, paramInt); 
    Context context1 = null;
    Context context2 = null;
    IOException iOException1 = null;
    if (paramInt == 17563663) {
      try {
        return (Interpolator)new FastOutLinearInInterpolator();
      } catch (XmlPullParserException xmlPullParserException) {
        paramContext = context2;
      } catch (IOException null) {
        paramContext = context1;
      } finally {}
    } else {
      XmlPullParserException xmlPullParserException;
      if (paramInt == 17563661)
        return (Interpolator)new FastOutSlowInInterpolator(); 
      if (paramInt == 17563662)
        return (Interpolator)new LinearOutSlowInInterpolator(); 
      XmlResourceParser xmlResourceParser = paramContext.getResources().getAnimation(paramInt);
      try {
        return createInterpolatorFromXml(paramContext, paramContext.getResources(), paramContext.getTheme(), (XmlPullParser)xmlResourceParser);
      } catch (XmlPullParserException xmlPullParserException1) {
        XmlResourceParser xmlResourceParser1 = xmlResourceParser;
      } catch (IOException iOException) {
        XmlPullParserException xmlPullParserException1 = xmlPullParserException;
      } finally {
        paramContext = null;
      } 
    } 
    Context context3 = paramContext;
    Resources.NotFoundException notFoundException = new Resources.NotFoundException();
    context3 = paramContext;
    StringBuilder stringBuilder = new StringBuilder();
    context3 = paramContext;
    this();
    IOException iOException2;
    context3 = paramContext;
    stringBuilder.append("Can't load animation resource ID #0x");
    context3 = paramContext;
    stringBuilder.append(Integer.toHexString(paramInt));
    context3 = paramContext;
    this(stringBuilder.toString());
    context3 = paramContext;
    notFoundException.initCause(iOException2);
    context3 = paramContext;
    throw notFoundException;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\graphics\drawable\AnimationUtilsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */