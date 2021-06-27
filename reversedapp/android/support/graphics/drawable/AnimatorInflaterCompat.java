package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AnimatorInflaterCompat {
  private static final boolean DBG_ANIMATOR_INFLATER = false;
  
  private static final int MAX_NUM_POINTS = 100;
  
  private static final String TAG = "AnimatorInflater";
  
  private static final int TOGETHER = 0;
  
  private static final int VALUE_TYPE_COLOR = 3;
  
  private static final int VALUE_TYPE_FLOAT = 0;
  
  private static final int VALUE_TYPE_INT = 1;
  
  private static final int VALUE_TYPE_PATH = 2;
  
  private static final int VALUE_TYPE_UNDEFINED = 4;
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, float paramFloat) throws XmlPullParserException, IOException {
    return createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser), null, 0, paramFloat);
  }
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, AnimatorSet paramAnimatorSet, int paramInt, float paramFloat) throws XmlPullParserException, IOException {
    int j;
    int i = paramXmlPullParser.getDepth();
    TypedArray typedArray = null;
    ArrayList<TypedArray> arrayList = null;
    while (true) {
      int k = paramXmlPullParser.next();
      j = 0;
      boolean bool = false;
      if ((k != 3 || paramXmlPullParser.getDepth() > i) && k != 1) {
        ObjectAnimator objectAnimator;
        TypedArray typedArray1;
        if (k != 2)
          continue; 
        String str = paramXmlPullParser.getName();
        if (str.equals("objectAnimator")) {
          objectAnimator = loadObjectAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, paramFloat, paramXmlPullParser);
        } else {
          ValueAnimator valueAnimator;
          if (objectAnimator.equals("animator")) {
            valueAnimator = loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, null, paramFloat, paramXmlPullParser);
          } else {
            AnimatorSet animatorSet;
            if (valueAnimator.equals("set")) {
              animatorSet = new AnimatorSet();
              typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR_SET);
              j = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "ordering", 0, 0);
              createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, paramAttributeSet, animatorSet, j, paramFloat);
              typedArray.recycle();
            } else if (animatorSet.equals("propertyValuesHolder")) {
              PropertyValuesHolder[] arrayOfPropertyValuesHolder = loadValues(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser));
              if (arrayOfPropertyValuesHolder != null && typedArray != null && typedArray instanceof ValueAnimator)
                ((ValueAnimator)typedArray).setValues(arrayOfPropertyValuesHolder); 
              bool = true;
              typedArray1 = typedArray;
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Unknown animator name: ");
              stringBuilder.append(paramXmlPullParser.getName());
              throw new RuntimeException(stringBuilder.toString());
            } 
          } 
        } 
        typedArray = typedArray1;
        if (paramAnimatorSet != null) {
          typedArray = typedArray1;
          if (!bool) {
            ArrayList<TypedArray> arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(typedArray1);
            typedArray = typedArray1;
            arrayList = arrayList1;
          } 
        } 
        continue;
      } 
      break;
    } 
    if (paramAnimatorSet != null && arrayList != null) {
      Animator[] arrayOfAnimator = new Animator[arrayList.size()];
      Iterator<TypedArray> iterator = arrayList.iterator();
      for (int k = j; iterator.hasNext(); k++)
        arrayOfAnimator[k] = (Animator)iterator.next(); 
      if (paramInt == 0) {
        paramAnimatorSet.playTogether(arrayOfAnimator);
      } else {
        paramAnimatorSet.playSequentially(arrayOfAnimator);
      } 
    } 
    return (Animator)typedArray;
  }
  
  private static Keyframe createNewKeyframe(Keyframe paramKeyframe, float paramFloat) {
    if (paramKeyframe.getType() == float.class) {
      paramKeyframe = Keyframe.ofFloat(paramFloat);
    } else if (paramKeyframe.getType() == int.class) {
      paramKeyframe = Keyframe.ofInt(paramFloat);
    } else {
      paramKeyframe = Keyframe.ofObject(paramFloat);
    } 
    return paramKeyframe;
  }
  
  private static void distributeKeyframes(Keyframe[] paramArrayOfKeyframe, float paramFloat, int paramInt1, int paramInt2) {
    paramFloat /= (paramInt2 - paramInt1 + 2);
    while (paramInt1 <= paramInt2) {
      paramArrayOfKeyframe[paramInt1].setFraction(paramArrayOfKeyframe[paramInt1 - 1].getFraction() + paramFloat);
      paramInt1++;
    } 
  }
  
  private static void dumpKeyframes(Object[] paramArrayOfObject, String paramString) {
    if (paramArrayOfObject == null || paramArrayOfObject.length == 0)
      return; 
    Log.d("AnimatorInflater", paramString);
    int i = paramArrayOfObject.length;
    for (byte b = 0; b < i; b++) {
      Float float_;
      Object object;
      Keyframe keyframe = (Keyframe)paramArrayOfObject[b];
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Keyframe ");
      stringBuilder.append(b);
      stringBuilder.append(": fraction ");
      if (keyframe.getFraction() < 0.0F) {
        paramString = "null";
      } else {
        float_ = Float.valueOf(keyframe.getFraction());
      } 
      stringBuilder.append(float_);
      stringBuilder.append(", ");
      stringBuilder.append(", value : ");
      if (keyframe.hasValue()) {
        object = keyframe.getValue();
      } else {
        object = "null";
      } 
      stringBuilder.append(object);
      Log.d("AnimatorInflater", stringBuilder.toString());
    } 
  }
  
  private static PropertyValuesHolder getPVH(TypedArray paramTypedArray, int paramInt1, int paramInt2, int paramInt3, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: iload_2
    //   2: invokevirtual peekValue : (I)Landroid/util/TypedValue;
    //   5: astore #5
    //   7: aload #5
    //   9: ifnull -> 18
    //   12: iconst_1
    //   13: istore #6
    //   15: goto -> 21
    //   18: iconst_0
    //   19: istore #6
    //   21: iload #6
    //   23: ifeq -> 36
    //   26: aload #5
    //   28: getfield type : I
    //   31: istore #7
    //   33: goto -> 39
    //   36: iconst_0
    //   37: istore #7
    //   39: aload_0
    //   40: iload_3
    //   41: invokevirtual peekValue : (I)Landroid/util/TypedValue;
    //   44: astore #5
    //   46: aload #5
    //   48: ifnull -> 57
    //   51: iconst_1
    //   52: istore #8
    //   54: goto -> 60
    //   57: iconst_0
    //   58: istore #8
    //   60: iload #8
    //   62: ifeq -> 75
    //   65: aload #5
    //   67: getfield type : I
    //   70: istore #9
    //   72: goto -> 78
    //   75: iconst_0
    //   76: istore #9
    //   78: iload_1
    //   79: istore #10
    //   81: iload_1
    //   82: iconst_4
    //   83: if_icmpne -> 121
    //   86: iload #6
    //   88: ifeq -> 99
    //   91: iload #7
    //   93: invokestatic isColorType : (I)Z
    //   96: ifne -> 112
    //   99: iload #8
    //   101: ifeq -> 118
    //   104: iload #9
    //   106: invokestatic isColorType : (I)Z
    //   109: ifeq -> 118
    //   112: iconst_3
    //   113: istore #10
    //   115: goto -> 121
    //   118: iconst_0
    //   119: istore #10
    //   121: iload #10
    //   123: ifne -> 131
    //   126: iconst_1
    //   127: istore_1
    //   128: goto -> 133
    //   131: iconst_0
    //   132: istore_1
    //   133: aconst_null
    //   134: astore #5
    //   136: aconst_null
    //   137: astore #11
    //   139: iload #10
    //   141: iconst_2
    //   142: if_icmpne -> 340
    //   145: aload_0
    //   146: iload_2
    //   147: invokevirtual getString : (I)Ljava/lang/String;
    //   150: astore #12
    //   152: aload_0
    //   153: iload_3
    //   154: invokevirtual getString : (I)Ljava/lang/String;
    //   157: astore #11
    //   159: aload #12
    //   161: invokestatic createNodesFromPathData : (Ljava/lang/String;)[Landroid/support/v4/graphics/PathParser$PathDataNode;
    //   164: astore #13
    //   166: aload #11
    //   168: invokestatic createNodesFromPathData : (Ljava/lang/String;)[Landroid/support/v4/graphics/PathParser$PathDataNode;
    //   171: astore #14
    //   173: aload #13
    //   175: ifnonnull -> 186
    //   178: aload #5
    //   180: astore_0
    //   181: aload #14
    //   183: ifnull -> 728
    //   186: aload #13
    //   188: ifnull -> 307
    //   191: new android/support/graphics/drawable/AnimatorInflaterCompat$PathDataEvaluator
    //   194: dup
    //   195: invokespecial <init> : ()V
    //   198: astore_0
    //   199: aload #14
    //   201: ifnull -> 288
    //   204: aload #13
    //   206: aload #14
    //   208: invokestatic canMorph : ([Landroid/support/v4/graphics/PathParser$PathDataNode;[Landroid/support/v4/graphics/PathParser$PathDataNode;)Z
    //   211: ifeq -> 238
    //   214: aload #4
    //   216: aload_0
    //   217: iconst_2
    //   218: anewarray java/lang/Object
    //   221: dup
    //   222: iconst_0
    //   223: aload #13
    //   225: aastore
    //   226: dup
    //   227: iconst_1
    //   228: aload #14
    //   230: aastore
    //   231: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   234: astore_0
    //   235: goto -> 304
    //   238: new java/lang/StringBuilder
    //   241: dup
    //   242: invokespecial <init> : ()V
    //   245: astore_0
    //   246: aload_0
    //   247: ldc_w ' Can't morph from '
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload_0
    //   255: aload #12
    //   257: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   260: pop
    //   261: aload_0
    //   262: ldc_w ' to '
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: pop
    //   269: aload_0
    //   270: aload #11
    //   272: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   275: pop
    //   276: new android/view/InflateException
    //   279: dup
    //   280: aload_0
    //   281: invokevirtual toString : ()Ljava/lang/String;
    //   284: invokespecial <init> : (Ljava/lang/String;)V
    //   287: athrow
    //   288: aload #4
    //   290: aload_0
    //   291: iconst_1
    //   292: anewarray java/lang/Object
    //   295: dup
    //   296: iconst_0
    //   297: aload #13
    //   299: aastore
    //   300: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   303: astore_0
    //   304: goto -> 728
    //   307: aload #5
    //   309: astore_0
    //   310: aload #14
    //   312: ifnull -> 728
    //   315: aload #4
    //   317: new android/support/graphics/drawable/AnimatorInflaterCompat$PathDataEvaluator
    //   320: dup
    //   321: invokespecial <init> : ()V
    //   324: iconst_1
    //   325: anewarray java/lang/Object
    //   328: dup
    //   329: iconst_0
    //   330: aload #14
    //   332: aastore
    //   333: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   336: astore_0
    //   337: goto -> 728
    //   340: iload #10
    //   342: iconst_3
    //   343: if_icmpne -> 354
    //   346: invokestatic getInstance : ()Landroid/support/graphics/drawable/ArgbEvaluator;
    //   349: astore #12
    //   351: goto -> 357
    //   354: aconst_null
    //   355: astore #12
    //   357: iload_1
    //   358: ifeq -> 505
    //   361: iload #6
    //   363: ifeq -> 460
    //   366: iload #7
    //   368: iconst_5
    //   369: if_icmpne -> 383
    //   372: aload_0
    //   373: iload_2
    //   374: fconst_0
    //   375: invokevirtual getDimension : (IF)F
    //   378: fstore #15
    //   380: goto -> 391
    //   383: aload_0
    //   384: iload_2
    //   385: fconst_0
    //   386: invokevirtual getFloat : (IF)F
    //   389: fstore #15
    //   391: iload #8
    //   393: ifeq -> 443
    //   396: iload #9
    //   398: iconst_5
    //   399: if_icmpne -> 413
    //   402: aload_0
    //   403: iload_3
    //   404: fconst_0
    //   405: invokevirtual getDimension : (IF)F
    //   408: fstore #16
    //   410: goto -> 421
    //   413: aload_0
    //   414: iload_3
    //   415: fconst_0
    //   416: invokevirtual getFloat : (IF)F
    //   419: fstore #16
    //   421: aload #4
    //   423: iconst_2
    //   424: newarray float
    //   426: dup
    //   427: iconst_0
    //   428: fload #15
    //   430: fastore
    //   431: dup
    //   432: iconst_1
    //   433: fload #16
    //   435: fastore
    //   436: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   439: astore_0
    //   440: goto -> 499
    //   443: aload #4
    //   445: iconst_1
    //   446: newarray float
    //   448: dup
    //   449: iconst_0
    //   450: fload #15
    //   452: fastore
    //   453: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   456: astore_0
    //   457: goto -> 499
    //   460: iload #9
    //   462: iconst_5
    //   463: if_icmpne -> 477
    //   466: aload_0
    //   467: iload_3
    //   468: fconst_0
    //   469: invokevirtual getDimension : (IF)F
    //   472: fstore #15
    //   474: goto -> 485
    //   477: aload_0
    //   478: iload_3
    //   479: fconst_0
    //   480: invokevirtual getFloat : (IF)F
    //   483: fstore #15
    //   485: aload #4
    //   487: iconst_1
    //   488: newarray float
    //   490: dup
    //   491: iconst_0
    //   492: fload #15
    //   494: fastore
    //   495: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   498: astore_0
    //   499: aload_0
    //   500: astore #5
    //   502: goto -> 702
    //   505: iload #6
    //   507: ifeq -> 637
    //   510: iload #7
    //   512: iconst_5
    //   513: if_icmpne -> 527
    //   516: aload_0
    //   517: iload_2
    //   518: fconst_0
    //   519: invokevirtual getDimension : (IF)F
    //   522: f2i
    //   523: istore_1
    //   524: goto -> 552
    //   527: iload #7
    //   529: invokestatic isColorType : (I)Z
    //   532: ifeq -> 545
    //   535: aload_0
    //   536: iload_2
    //   537: iconst_0
    //   538: invokevirtual getColor : (II)I
    //   541: istore_1
    //   542: goto -> 552
    //   545: aload_0
    //   546: iload_2
    //   547: iconst_0
    //   548: invokevirtual getInt : (II)I
    //   551: istore_1
    //   552: iload #8
    //   554: ifeq -> 620
    //   557: iload #9
    //   559: iconst_5
    //   560: if_icmpne -> 574
    //   563: aload_0
    //   564: iload_3
    //   565: fconst_0
    //   566: invokevirtual getDimension : (IF)F
    //   569: f2i
    //   570: istore_2
    //   571: goto -> 599
    //   574: iload #9
    //   576: invokestatic isColorType : (I)Z
    //   579: ifeq -> 592
    //   582: aload_0
    //   583: iload_3
    //   584: iconst_0
    //   585: invokevirtual getColor : (II)I
    //   588: istore_2
    //   589: goto -> 599
    //   592: aload_0
    //   593: iload_3
    //   594: iconst_0
    //   595: invokevirtual getInt : (II)I
    //   598: istore_2
    //   599: aload #4
    //   601: iconst_2
    //   602: newarray int
    //   604: dup
    //   605: iconst_0
    //   606: iload_1
    //   607: iastore
    //   608: dup
    //   609: iconst_1
    //   610: iload_2
    //   611: iastore
    //   612: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   615: astore #5
    //   617: goto -> 702
    //   620: aload #4
    //   622: iconst_1
    //   623: newarray int
    //   625: dup
    //   626: iconst_0
    //   627: iload_1
    //   628: iastore
    //   629: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   632: astore #5
    //   634: goto -> 702
    //   637: aload #11
    //   639: astore #5
    //   641: iload #8
    //   643: ifeq -> 702
    //   646: iload #9
    //   648: iconst_5
    //   649: if_icmpne -> 663
    //   652: aload_0
    //   653: iload_3
    //   654: fconst_0
    //   655: invokevirtual getDimension : (IF)F
    //   658: f2i
    //   659: istore_1
    //   660: goto -> 688
    //   663: iload #9
    //   665: invokestatic isColorType : (I)Z
    //   668: ifeq -> 681
    //   671: aload_0
    //   672: iload_3
    //   673: iconst_0
    //   674: invokevirtual getColor : (II)I
    //   677: istore_1
    //   678: goto -> 688
    //   681: aload_0
    //   682: iload_3
    //   683: iconst_0
    //   684: invokevirtual getInt : (II)I
    //   687: istore_1
    //   688: aload #4
    //   690: iconst_1
    //   691: newarray int
    //   693: dup
    //   694: iconst_0
    //   695: iload_1
    //   696: iastore
    //   697: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   700: astore #5
    //   702: aload #5
    //   704: astore_0
    //   705: aload #5
    //   707: ifnull -> 728
    //   710: aload #5
    //   712: astore_0
    //   713: aload #12
    //   715: ifnull -> 728
    //   718: aload #5
    //   720: aload #12
    //   722: invokevirtual setEvaluator : (Landroid/animation/TypeEvaluator;)V
    //   725: aload #5
    //   727: astore_0
    //   728: aload_0
    //   729: areturn
  }
  
  private static int inferValueTypeFromValues(TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    boolean bool2;
    TypedValue typedValue2 = paramTypedArray.peekValue(paramInt1);
    int i = 1;
    boolean bool1 = false;
    if (typedValue2 != null) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 != 0) {
      bool2 = typedValue2.type;
    } else {
      bool2 = false;
    } 
    TypedValue typedValue1 = paramTypedArray.peekValue(paramInt2);
    if (typedValue1 != null) {
      paramInt2 = i;
    } else {
      paramInt2 = 0;
    } 
    if (paramInt2 != 0) {
      i = typedValue1.type;
    } else {
      i = 0;
    } 
    if (paramInt1 == 0 || !isColorType(bool2)) {
      paramInt1 = bool1;
      if (paramInt2 != 0) {
        paramInt1 = bool1;
        if (isColorType(i))
          return 3; 
      } 
      return paramInt1;
    } 
    return 3;
  }
  
  private static int inferValueTypeOfKeyframe(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) {
    boolean bool;
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    byte b1 = 0;
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null) {
      bool = true;
    } else {
      bool = false;
    } 
    byte b2 = b1;
    if (bool) {
      b2 = b1;
      if (isColorType(typedValue.type))
        b2 = 3; 
    } 
    typedArray.recycle();
    return b2;
  }
  
  private static boolean isColorType(int paramInt) {
    boolean bool;
    if (paramInt >= 28 && paramInt <= 31) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static Animator loadAnimator(Context paramContext, @AnimatorRes int paramInt) throws Resources.NotFoundException {
    Animator animator;
    if (Build.VERSION.SDK_INT >= 24) {
      animator = AnimatorInflater.loadAnimator(paramContext, paramInt);
    } else {
      animator = loadAnimator((Context)animator, animator.getResources(), animator.getTheme(), paramInt);
    } 
    return animator;
  }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, @AnimatorRes int paramInt) throws Resources.NotFoundException {
    return loadAnimator(paramContext, paramResources, paramTheme, paramInt, 1.0F);
  }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, @AnimatorRes int paramInt, float paramFloat) throws Resources.NotFoundException {
    Context context1 = null;
    Context context2 = null;
    XmlResourceParser xmlResourceParser = null;
    try {
      XmlResourceParser xmlResourceParser1 = paramResources.getAnimation(paramInt);
      try {
        return createAnimatorFromXml(paramContext, paramResources, paramTheme, (XmlPullParser)xmlResourceParser1, paramFloat);
      } catch (XmlPullParserException xmlPullParserException) {
      
      } catch (IOException null) {
      
      } finally {
        paramContext = null;
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      paramContext = context2;
    } catch (IOException iOException) {
      paramContext = context1;
      Context context = paramContext;
      Resources.NotFoundException notFoundException1 = new Resources.NotFoundException();
      context = paramContext;
      StringBuilder stringBuilder1 = new StringBuilder();
      context = paramContext;
      this();
      context = paramContext;
      stringBuilder1.append("Can't load animation resource ID #0x");
      context = paramContext;
      stringBuilder1.append(Integer.toHexString(paramInt));
      context = paramContext;
      this(stringBuilder1.toString());
      context = paramContext;
      notFoundException1.initCause(iOException);
      context = paramContext;
      throw notFoundException1;
    } finally {}
    Context context3 = paramContext;
    Resources.NotFoundException notFoundException = new Resources.NotFoundException();
    context3 = paramContext;
    StringBuilder stringBuilder = new StringBuilder();
    context3 = paramContext;
    this();
    context3 = paramContext;
    stringBuilder.append("Can't load animation resource ID #0x");
    context3 = paramContext;
    stringBuilder.append(Integer.toHexString(paramInt));
    context3 = paramContext;
    this(stringBuilder.toString());
    context3 = paramContext;
    notFoundException.initCause(iOException);
    context3 = paramContext;
    throw notFoundException;
  }
  
  private static ValueAnimator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, ValueAnimator paramValueAnimator, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    TypedArray typedArray2 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR);
    TypedArray typedArray1 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
    ValueAnimator valueAnimator = paramValueAnimator;
    if (paramValueAnimator == null)
      valueAnimator = new ValueAnimator(); 
    parseAnimatorFromTypeArray(valueAnimator, typedArray2, typedArray1, paramFloat, paramXmlPullParser);
    int i = TypedArrayUtils.getNamedResourceId(typedArray2, paramXmlPullParser, "interpolator", 0, 0);
    if (i > 0)
      valueAnimator.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(paramContext, i)); 
    typedArray2.recycle();
    if (typedArray1 != null)
      typedArray1.recycle(); 
    return valueAnimator;
  }
  
  private static Keyframe loadKeyframe(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int paramInt, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    Keyframe keyframe;
    boolean bool;
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    float f = TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "fraction", 3, -1.0F);
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = paramInt;
    if (paramInt == 4)
      if (bool && isColorType(typedValue.type)) {
        i = 3;
      } else {
        i = 0;
      }  
    if (bool) {
      if (i != 3) {
        switch (i) {
          default:
            typedValue = null;
            break;
          case 0:
            keyframe = Keyframe.ofFloat(f, TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "value", 0, 0.0F));
            break;
          case 1:
            keyframe = Keyframe.ofInt(f, TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "value", 0, 0));
            break;
        } 
      } else {
      
      } 
    } else if (i == 0) {
      keyframe = Keyframe.ofFloat(f);
    } else {
      keyframe = Keyframe.ofInt(f);
    } 
    paramInt = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "interpolator", 1, 0);
    if (paramInt > 0)
      keyframe.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(paramContext, paramInt)); 
    typedArray.recycle();
    return keyframe;
  }
  
  private static ObjectAnimator loadObjectAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    ObjectAnimator objectAnimator = new ObjectAnimator();
    loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, (ValueAnimator)objectAnimator, paramFloat, paramXmlPullParser);
    return objectAnimator;
  }
  
  private static PropertyValuesHolder loadPvh(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, String paramString, int paramInt) throws XmlPullParserException, IOException {
    PropertyValuesHolder propertyValuesHolder;
    Context context = null;
    ArrayList<Keyframe> arrayList = null;
    int i = paramInt;
    while (true) {
      paramInt = paramXmlPullParser.next();
      if (paramInt != 3 && paramInt != 1) {
        if (paramXmlPullParser.getName().equals("keyframe")) {
          paramInt = i;
          if (i == 4)
            paramInt = inferValueTypeOfKeyframe(paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), paramXmlPullParser); 
          Keyframe keyframe = loadKeyframe(paramContext, paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), paramInt, paramXmlPullParser);
          ArrayList<Keyframe> arrayList1 = arrayList;
          if (keyframe != null) {
            arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(keyframe);
          } 
          paramXmlPullParser.next();
          i = paramInt;
          arrayList = arrayList1;
        } 
        continue;
      } 
      break;
    } 
    paramContext = context;
    if (arrayList != null) {
      int j = arrayList.size();
      paramContext = context;
      if (j > 0) {
        int k = 0;
        Keyframe keyframe1 = arrayList.get(0);
        Keyframe keyframe2 = arrayList.get(j - 1);
        float f = keyframe2.getFraction();
        paramInt = j;
        if (f < 1.0F)
          if (f < 0.0F) {
            keyframe2.setFraction(1.0F);
            paramInt = j;
          } else {
            arrayList.add(arrayList.size(), createNewKeyframe(keyframe2, 1.0F));
            paramInt = j + 1;
          }  
        f = keyframe1.getFraction();
        j = paramInt;
        if (f != 0.0F)
          if (f < 0.0F) {
            keyframe1.setFraction(0.0F);
            j = paramInt;
          } else {
            arrayList.add(0, createNewKeyframe(keyframe1, 0.0F));
            j = paramInt + 1;
          }  
        Keyframe[] arrayOfKeyframe = new Keyframe[j];
        arrayList.toArray(arrayOfKeyframe);
        for (paramInt = k; paramInt < j; paramInt++) {
          keyframe2 = arrayOfKeyframe[paramInt];
          if (keyframe2.getFraction() < 0.0F)
            if (paramInt == 0) {
              keyframe2.setFraction(0.0F);
            } else {
              int m = j - 1;
              if (paramInt == m) {
                keyframe2.setFraction(1.0F);
              } else {
                k = paramInt + 1;
                int n = paramInt;
                while (k < m && arrayOfKeyframe[k].getFraction() < 0.0F) {
                  n = k;
                  k++;
                } 
                distributeKeyframes(arrayOfKeyframe, arrayOfKeyframe[n + 1].getFraction() - arrayOfKeyframe[paramInt - 1].getFraction(), paramInt, n);
              } 
            }  
        } 
        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofKeyframe(paramString, arrayOfKeyframe);
        propertyValuesHolder = propertyValuesHolder1;
        if (i == 3) {
          propertyValuesHolder1.setEvaluator(ArgbEvaluator.getInstance());
          propertyValuesHolder = propertyValuesHolder1;
        } 
      } 
    } 
    return propertyValuesHolder;
  }
  
  private static PropertyValuesHolder[] loadValues(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet) throws XmlPullParserException, IOException {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder;
    ArrayList<PropertyValuesHolder> arrayList;
    int i;
    Context context = null;
    PropertyValuesHolder propertyValuesHolder = null;
    while (true) {
      int j = paramXmlPullParser.getEventType();
      i = 0;
      if (j != 3 && j != 1) {
        if (j != 2) {
          paramXmlPullParser.next();
          continue;
        } 
        if (paramXmlPullParser.getName().equals("propertyValuesHolder")) {
          ArrayList<PropertyValuesHolder> arrayList1;
          TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
          String str = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "propertyName", 3);
          i = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "valueType", 2, 4);
          PropertyValuesHolder propertyValuesHolder1 = loadPvh(paramContext, paramResources, paramTheme, paramXmlPullParser, str, i);
          PropertyValuesHolder propertyValuesHolder2 = propertyValuesHolder1;
          if (propertyValuesHolder1 == null)
            propertyValuesHolder2 = getPVH(typedArray, i, 0, 1, str); 
          propertyValuesHolder1 = propertyValuesHolder;
          if (propertyValuesHolder2 != null) {
            propertyValuesHolder1 = propertyValuesHolder;
            if (propertyValuesHolder == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(propertyValuesHolder2);
          } 
          typedArray.recycle();
          arrayList = arrayList1;
        } 
        paramXmlPullParser.next();
        continue;
      } 
      break;
    } 
    paramContext = context;
    if (arrayList != null) {
      int j = arrayList.size();
      PropertyValuesHolder[] arrayOfPropertyValuesHolder1 = new PropertyValuesHolder[j];
      while (true) {
        arrayOfPropertyValuesHolder = arrayOfPropertyValuesHolder1;
        if (i < j) {
          arrayOfPropertyValuesHolder1[i] = arrayList.get(i);
          i++;
          continue;
        } 
        break;
      } 
    } 
    return arrayOfPropertyValuesHolder;
  }
  
  private static void parseAnimatorFromTypeArray(ValueAnimator paramValueAnimator, TypedArray paramTypedArray1, TypedArray paramTypedArray2, float paramFloat, XmlPullParser paramXmlPullParser) {
    long l1 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "duration", 1, 300);
    long l2 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "startOffset", 2, 0);
    int i = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "valueType", 7, 4);
    int j = i;
    if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueFrom")) {
      j = i;
      if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueTo")) {
        int k = i;
        if (i == 4)
          k = inferValueTypeFromValues(paramTypedArray1, 5, 6); 
        PropertyValuesHolder propertyValuesHolder = getPVH(paramTypedArray1, k, 5, 6, "");
        j = k;
        if (propertyValuesHolder != null) {
          paramValueAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder });
          j = k;
        } 
      } 
    } 
    paramValueAnimator.setDuration(l1);
    paramValueAnimator.setStartDelay(l2);
    paramValueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatCount", 3, 0));
    paramValueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatMode", 4, 1));
    if (paramTypedArray2 != null)
      setupObjectAnimator(paramValueAnimator, paramTypedArray2, j, paramFloat, paramXmlPullParser); 
  }
  
  private static void setupObjectAnimator(ValueAnimator paramValueAnimator, TypedArray paramTypedArray, int paramInt, float paramFloat, XmlPullParser paramXmlPullParser) {
    // Byte code:
    //   0: aload_0
    //   1: checkcast android/animation/ObjectAnimator
    //   4: astore #5
    //   6: aload_1
    //   7: aload #4
    //   9: ldc_w 'pathData'
    //   12: iconst_1
    //   13: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   16: astore #6
    //   18: aload #6
    //   20: ifnull -> 121
    //   23: aload_1
    //   24: aload #4
    //   26: ldc_w 'propertyXName'
    //   29: iconst_2
    //   30: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   33: astore_0
    //   34: aload_1
    //   35: aload #4
    //   37: ldc_w 'propertyYName'
    //   40: iconst_3
    //   41: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   44: astore #4
    //   46: iload_2
    //   47: iconst_2
    //   48: if_icmpeq -> 51
    //   51: aload_0
    //   52: ifnonnull -> 100
    //   55: aload #4
    //   57: ifnull -> 63
    //   60: goto -> 100
    //   63: new java/lang/StringBuilder
    //   66: dup
    //   67: invokespecial <init> : ()V
    //   70: astore_0
    //   71: aload_0
    //   72: aload_1
    //   73: invokevirtual getPositionDescription : ()Ljava/lang/String;
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_0
    //   81: ldc_w ' propertyXName or propertyYName is needed for PathData'
    //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: new android/view/InflateException
    //   91: dup
    //   92: aload_0
    //   93: invokevirtual toString : ()Ljava/lang/String;
    //   96: invokespecial <init> : (Ljava/lang/String;)V
    //   99: athrow
    //   100: aload #6
    //   102: invokestatic createPathFromPathData : (Ljava/lang/String;)Landroid/graphics/Path;
    //   105: aload #5
    //   107: fload_3
    //   108: ldc_w 0.5
    //   111: fmul
    //   112: aload_0
    //   113: aload #4
    //   115: invokestatic setupPathMotion : (Landroid/graphics/Path;Landroid/animation/ObjectAnimator;FLjava/lang/String;Ljava/lang/String;)V
    //   118: goto -> 136
    //   121: aload #5
    //   123: aload_1
    //   124: aload #4
    //   126: ldc_w 'propertyName'
    //   129: iconst_0
    //   130: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   133: invokevirtual setPropertyName : (Ljava/lang/String;)V
    //   136: return
  }
  
  private static void setupPathMotion(Path paramPath, ObjectAnimator paramObjectAnimator, float paramFloat, String paramString1, String paramString2) {
    PathMeasure pathMeasure = new PathMeasure(paramPath, false);
    ArrayList<Float> arrayList = new ArrayList();
    arrayList.add(Float.valueOf(0.0F));
    float f = 0.0F;
    while (true) {
      float f1 = f + pathMeasure.getLength();
      arrayList.add(Float.valueOf(f1));
      f = f1;
      if (!pathMeasure.nextContour()) {
        PathMeasure pathMeasure1 = new PathMeasure(paramPath, false);
        int i = Math.min(100, (int)(f1 / paramFloat) + 1);
        float[] arrayOfFloat1 = new float[i];
        float[] arrayOfFloat2 = new float[i];
        float[] arrayOfFloat3 = new float[2];
        f = f1 / (i - 1);
        byte b = 0;
        paramFloat = 0.0F;
        int j = 0;
        while (true) {
          PropertyValuesHolder propertyValuesHolder;
          pathMeasure = null;
          if (b < i) {
            pathMeasure1.getPosTan(paramFloat - ((Float)arrayList.get(j)).floatValue(), arrayOfFloat3, null);
            arrayOfFloat1[b] = arrayOfFloat3[0];
            arrayOfFloat2[b] = arrayOfFloat3[1];
            paramFloat += f;
            int k = j + 1;
            int m = j;
            if (k < arrayList.size()) {
              m = j;
              if (paramFloat > ((Float)arrayList.get(k)).floatValue()) {
                pathMeasure1.nextContour();
                m = k;
              } 
            } 
            b++;
            j = m;
            continue;
          } 
          if (paramString1 != null) {
            PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat(paramString1, arrayOfFloat1);
          } else {
            arrayOfFloat1 = null;
          } 
          PathMeasure pathMeasure2 = pathMeasure;
          if (paramString2 != null)
            propertyValuesHolder = PropertyValuesHolder.ofFloat(paramString2, arrayOfFloat2); 
          if (arrayOfFloat1 == null) {
            paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder });
          } else if (propertyValuesHolder == null) {
            paramObjectAnimator.setValues(new PropertyValuesHolder[] { (PropertyValuesHolder)arrayOfFloat1 });
          } else {
            paramObjectAnimator.setValues(new PropertyValuesHolder[] { (PropertyValuesHolder)arrayOfFloat1, propertyValuesHolder });
          } 
          return;
        } 
        break;
      } 
    } 
  }
  
  private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]> {
    private PathParser.PathDataNode[] mNodeArray;
    
    PathDataEvaluator() {}
    
    PathDataEvaluator(PathParser.PathDataNode[] param1ArrayOfPathDataNode) {
      this.mNodeArray = param1ArrayOfPathDataNode;
    }
    
    public PathParser.PathDataNode[] evaluate(float param1Float, PathParser.PathDataNode[] param1ArrayOfPathDataNode1, PathParser.PathDataNode[] param1ArrayOfPathDataNode2) {
      if (PathParser.canMorph(param1ArrayOfPathDataNode1, param1ArrayOfPathDataNode2)) {
        if (this.mNodeArray == null || !PathParser.canMorph(this.mNodeArray, param1ArrayOfPathDataNode1))
          this.mNodeArray = PathParser.deepCopyNodes(param1ArrayOfPathDataNode1); 
        for (byte b = 0; b < param1ArrayOfPathDataNode1.length; b++)
          this.mNodeArray[b].interpolatePathDataNode(param1ArrayOfPathDataNode1[b], param1ArrayOfPathDataNode2[b], param1Float); 
        return this.mNodeArray;
      } 
      throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\graphics\drawable\AnimatorInflaterCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */