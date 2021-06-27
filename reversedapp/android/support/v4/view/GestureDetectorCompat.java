package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener) {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler) {
    if (Build.VERSION.SDK_INT > 17) {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
    } else {
      this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
    } 
  }
  
  public boolean isLongpressEnabled() {
    return this.mImpl.isLongpressEnabled();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean) {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener) {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }
  
  static interface GestureDetectorCompatImpl {
    boolean isLongpressEnabled();
    
    boolean onTouchEvent(MotionEvent param1MotionEvent);
    
    void setIsLongpressEnabled(boolean param1Boolean);
    
    void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    
    private static final int LONG_PRESS = 2;
    
    private static final int SHOW_PRESS = 1;
    
    private static final int TAP = 3;
    
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    
    private boolean mAlwaysInBiggerTapRegion;
    
    private boolean mAlwaysInTapRegion;
    
    MotionEvent mCurrentDownEvent;
    
    boolean mDeferConfirmSingleTap;
    
    GestureDetector.OnDoubleTapListener mDoubleTapListener;
    
    private int mDoubleTapSlopSquare;
    
    private float mDownFocusX;
    
    private float mDownFocusY;
    
    private final Handler mHandler;
    
    private boolean mInLongPress;
    
    private boolean mIsDoubleTapping;
    
    private boolean mIsLongpressEnabled;
    
    private float mLastFocusX;
    
    private float mLastFocusY;
    
    final GestureDetector.OnGestureListener mListener;
    
    private int mMaximumFlingVelocity;
    
    private int mMinimumFlingVelocity;
    
    private MotionEvent mPreviousUpEvent;
    
    boolean mStillDown;
    
    private int mTouchSlopSquare;
    
    private VelocityTracker mVelocityTracker;
    
    static {
    
    }
    
    GestureDetectorCompatImplBase(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      if (param1Handler != null) {
        this.mHandler = new GestureHandler(param1Handler);
      } else {
        this.mHandler = new GestureHandler();
      } 
      this.mListener = param1OnGestureListener;
      if (param1OnGestureListener instanceof GestureDetector.OnDoubleTapListener)
        setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)param1OnGestureListener); 
      init(param1Context);
    }
    
    private void cancel() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void cancelTaps() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void init(Context param1Context) {
      if (param1Context != null) {
        if (this.mListener != null) {
          this.mIsLongpressEnabled = true;
          ViewConfiguration viewConfiguration = ViewConfiguration.get(param1Context);
          int i = viewConfiguration.getScaledTouchSlop();
          int j = viewConfiguration.getScaledDoubleTapSlop();
          this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
          this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
          this.mTouchSlopSquare = i * i;
          this.mDoubleTapSlopSquare = j * j;
          return;
        } 
        throw new IllegalArgumentException("OnGestureListener must not be null");
      } 
      throw new IllegalArgumentException("Context must not be null");
    }
    
    private boolean isConsideredDoubleTap(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, MotionEvent param1MotionEvent3) {
      boolean bool = this.mAlwaysInBiggerTapRegion;
      boolean bool1 = false;
      if (!bool)
        return false; 
      if (param1MotionEvent3.getEventTime() - param1MotionEvent2.getEventTime() > DOUBLE_TAP_TIMEOUT)
        return false; 
      int i = (int)param1MotionEvent1.getX() - (int)param1MotionEvent3.getX();
      int j = (int)param1MotionEvent1.getY() - (int)param1MotionEvent3.getY();
      if (i * i + j * j < this.mDoubleTapSlopSquare)
        bool1 = true; 
      return bool1;
    }
    
    void dispatchLongPress() {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }
    
    public boolean isLongpressEnabled() {
      return this.mIsLongpressEnabled;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getAction : ()I
      //   4: istore_2
      //   5: aload_0
      //   6: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   9: ifnonnull -> 19
      //   12: aload_0
      //   13: invokestatic obtain : ()Landroid/view/VelocityTracker;
      //   16: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   19: aload_0
      //   20: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   23: aload_1
      //   24: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
      //   27: iload_2
      //   28: sipush #255
      //   31: iand
      //   32: istore_3
      //   33: iconst_0
      //   34: istore #4
      //   36: iload_3
      //   37: bipush #6
      //   39: if_icmpne -> 47
      //   42: iconst_1
      //   43: istore_2
      //   44: goto -> 49
      //   47: iconst_0
      //   48: istore_2
      //   49: iload_2
      //   50: ifeq -> 62
      //   53: aload_1
      //   54: invokevirtual getActionIndex : ()I
      //   57: istore #5
      //   59: goto -> 65
      //   62: iconst_m1
      //   63: istore #5
      //   65: aload_1
      //   66: invokevirtual getPointerCount : ()I
      //   69: istore #6
      //   71: iconst_0
      //   72: istore #7
      //   74: fconst_0
      //   75: fstore #8
      //   77: fconst_0
      //   78: fstore #9
      //   80: iload #7
      //   82: iload #6
      //   84: if_icmpge -> 125
      //   87: iload #5
      //   89: iload #7
      //   91: if_icmpne -> 97
      //   94: goto -> 119
      //   97: fload #8
      //   99: aload_1
      //   100: iload #7
      //   102: invokevirtual getX : (I)F
      //   105: fadd
      //   106: fstore #8
      //   108: fload #9
      //   110: aload_1
      //   111: iload #7
      //   113: invokevirtual getY : (I)F
      //   116: fadd
      //   117: fstore #9
      //   119: iinc #7, 1
      //   122: goto -> 80
      //   125: iload_2
      //   126: ifeq -> 137
      //   129: iload #6
      //   131: iconst_1
      //   132: isub
      //   133: istore_2
      //   134: goto -> 140
      //   137: iload #6
      //   139: istore_2
      //   140: iload_2
      //   141: i2f
      //   142: fstore #10
      //   144: fload #8
      //   146: fload #10
      //   148: fdiv
      //   149: fstore #8
      //   151: fload #9
      //   153: fload #10
      //   155: fdiv
      //   156: fstore #11
      //   158: iload_3
      //   159: tableswitch default -> 200, 0 -> 924, 1 -> 653, 2 -> 403, 3 -> 392, 4 -> 200, 5 -> 357, 6 -> 207
      //   200: iload #4
      //   202: istore #12
      //   204: goto -> 1186
      //   207: aload_0
      //   208: fload #8
      //   210: putfield mLastFocusX : F
      //   213: aload_0
      //   214: fload #8
      //   216: putfield mDownFocusX : F
      //   219: aload_0
      //   220: fload #11
      //   222: putfield mLastFocusY : F
      //   225: aload_0
      //   226: fload #11
      //   228: putfield mDownFocusY : F
      //   231: aload_0
      //   232: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   235: sipush #1000
      //   238: aload_0
      //   239: getfield mMaximumFlingVelocity : I
      //   242: i2f
      //   243: invokevirtual computeCurrentVelocity : (IF)V
      //   246: aload_1
      //   247: invokevirtual getActionIndex : ()I
      //   250: istore #5
      //   252: aload_1
      //   253: iload #5
      //   255: invokevirtual getPointerId : (I)I
      //   258: istore_2
      //   259: aload_0
      //   260: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   263: iload_2
      //   264: invokevirtual getXVelocity : (I)F
      //   267: fstore #8
      //   269: aload_0
      //   270: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   273: iload_2
      //   274: invokevirtual getYVelocity : (I)F
      //   277: fstore #9
      //   279: iconst_0
      //   280: istore_2
      //   281: iload #4
      //   283: istore #12
      //   285: iload_2
      //   286: iload #6
      //   288: if_icmpge -> 1186
      //   291: iload_2
      //   292: iload #5
      //   294: if_icmpne -> 300
      //   297: goto -> 351
      //   300: aload_1
      //   301: iload_2
      //   302: invokevirtual getPointerId : (I)I
      //   305: istore #7
      //   307: aload_0
      //   308: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   311: iload #7
      //   313: invokevirtual getXVelocity : (I)F
      //   316: fload #8
      //   318: fmul
      //   319: aload_0
      //   320: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   323: iload #7
      //   325: invokevirtual getYVelocity : (I)F
      //   328: fload #9
      //   330: fmul
      //   331: fadd
      //   332: fconst_0
      //   333: fcmpg
      //   334: ifge -> 351
      //   337: aload_0
      //   338: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   341: invokevirtual clear : ()V
      //   344: iload #4
      //   346: istore #12
      //   348: goto -> 1186
      //   351: iinc #2, 1
      //   354: goto -> 281
      //   357: aload_0
      //   358: fload #8
      //   360: putfield mLastFocusX : F
      //   363: aload_0
      //   364: fload #8
      //   366: putfield mDownFocusX : F
      //   369: aload_0
      //   370: fload #11
      //   372: putfield mLastFocusY : F
      //   375: aload_0
      //   376: fload #11
      //   378: putfield mDownFocusY : F
      //   381: aload_0
      //   382: invokespecial cancelTaps : ()V
      //   385: iload #4
      //   387: istore #12
      //   389: goto -> 1186
      //   392: aload_0
      //   393: invokespecial cancel : ()V
      //   396: iload #4
      //   398: istore #12
      //   400: goto -> 1186
      //   403: aload_0
      //   404: getfield mInLongPress : Z
      //   407: ifeq -> 417
      //   410: iload #4
      //   412: istore #12
      //   414: goto -> 1186
      //   417: aload_0
      //   418: getfield mLastFocusX : F
      //   421: fload #8
      //   423: fsub
      //   424: fstore #9
      //   426: aload_0
      //   427: getfield mLastFocusY : F
      //   430: fload #11
      //   432: fsub
      //   433: fstore #10
      //   435: aload_0
      //   436: getfield mIsDoubleTapping : Z
      //   439: ifeq -> 459
      //   442: iconst_0
      //   443: aload_0
      //   444: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   447: aload_1
      //   448: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   453: ior
      //   454: istore #12
      //   456: goto -> 1186
      //   459: aload_0
      //   460: getfield mAlwaysInTapRegion : Z
      //   463: ifeq -> 594
      //   466: fload #8
      //   468: aload_0
      //   469: getfield mDownFocusX : F
      //   472: fsub
      //   473: f2i
      //   474: istore_2
      //   475: fload #11
      //   477: aload_0
      //   478: getfield mDownFocusY : F
      //   481: fsub
      //   482: f2i
      //   483: istore #5
      //   485: iload_2
      //   486: iload_2
      //   487: imul
      //   488: iload #5
      //   490: iload #5
      //   492: imul
      //   493: iadd
      //   494: istore_2
      //   495: iload_2
      //   496: aload_0
      //   497: getfield mTouchSlopSquare : I
      //   500: if_icmple -> 567
      //   503: aload_0
      //   504: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   507: aload_0
      //   508: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   511: aload_1
      //   512: fload #9
      //   514: fload #10
      //   516: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   521: istore #4
      //   523: aload_0
      //   524: fload #8
      //   526: putfield mLastFocusX : F
      //   529: aload_0
      //   530: fload #11
      //   532: putfield mLastFocusY : F
      //   535: aload_0
      //   536: iconst_0
      //   537: putfield mAlwaysInTapRegion : Z
      //   540: aload_0
      //   541: getfield mHandler : Landroid/os/Handler;
      //   544: iconst_3
      //   545: invokevirtual removeMessages : (I)V
      //   548: aload_0
      //   549: getfield mHandler : Landroid/os/Handler;
      //   552: iconst_1
      //   553: invokevirtual removeMessages : (I)V
      //   556: aload_0
      //   557: getfield mHandler : Landroid/os/Handler;
      //   560: iconst_2
      //   561: invokevirtual removeMessages : (I)V
      //   564: goto -> 570
      //   567: iconst_0
      //   568: istore #4
      //   570: iload #4
      //   572: istore #12
      //   574: iload_2
      //   575: aload_0
      //   576: getfield mTouchSlopSquare : I
      //   579: if_icmple -> 921
      //   582: aload_0
      //   583: iconst_0
      //   584: putfield mAlwaysInBiggerTapRegion : Z
      //   587: iload #4
      //   589: istore #12
      //   591: goto -> 921
      //   594: fload #9
      //   596: invokestatic abs : (F)F
      //   599: fconst_1
      //   600: fcmpl
      //   601: ifge -> 618
      //   604: iload #4
      //   606: istore #12
      //   608: fload #10
      //   610: invokestatic abs : (F)F
      //   613: fconst_1
      //   614: fcmpl
      //   615: iflt -> 1186
      //   618: aload_0
      //   619: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   622: aload_0
      //   623: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   626: aload_1
      //   627: fload #9
      //   629: fload #10
      //   631: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   636: istore #12
      //   638: aload_0
      //   639: fload #8
      //   641: putfield mLastFocusX : F
      //   644: aload_0
      //   645: fload #11
      //   647: putfield mLastFocusY : F
      //   650: goto -> 1186
      //   653: aload_0
      //   654: iconst_0
      //   655: putfield mStillDown : Z
      //   658: aload_1
      //   659: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   662: astore #13
      //   664: aload_0
      //   665: getfield mIsDoubleTapping : Z
      //   668: ifeq -> 688
      //   671: aload_0
      //   672: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   675: aload_1
      //   676: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   681: iconst_0
      //   682: ior
      //   683: istore #12
      //   685: goto -> 856
      //   688: aload_0
      //   689: getfield mInLongPress : Z
      //   692: ifeq -> 711
      //   695: aload_0
      //   696: getfield mHandler : Landroid/os/Handler;
      //   699: iconst_3
      //   700: invokevirtual removeMessages : (I)V
      //   703: aload_0
      //   704: iconst_0
      //   705: putfield mInLongPress : Z
      //   708: goto -> 830
      //   711: aload_0
      //   712: getfield mAlwaysInTapRegion : Z
      //   715: ifeq -> 758
      //   718: aload_0
      //   719: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   722: aload_1
      //   723: invokeinterface onSingleTapUp : (Landroid/view/MotionEvent;)Z
      //   728: istore #12
      //   730: aload_0
      //   731: getfield mDeferConfirmSingleTap : Z
      //   734: ifeq -> 755
      //   737: aload_0
      //   738: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   741: ifnull -> 755
      //   744: aload_0
      //   745: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   748: aload_1
      //   749: invokeinterface onSingleTapConfirmed : (Landroid/view/MotionEvent;)Z
      //   754: pop
      //   755: goto -> 856
      //   758: aload_0
      //   759: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   762: astore #14
      //   764: aload_1
      //   765: iconst_0
      //   766: invokevirtual getPointerId : (I)I
      //   769: istore_2
      //   770: aload #14
      //   772: sipush #1000
      //   775: aload_0
      //   776: getfield mMaximumFlingVelocity : I
      //   779: i2f
      //   780: invokevirtual computeCurrentVelocity : (IF)V
      //   783: aload #14
      //   785: iload_2
      //   786: invokevirtual getYVelocity : (I)F
      //   789: fstore #9
      //   791: aload #14
      //   793: iload_2
      //   794: invokevirtual getXVelocity : (I)F
      //   797: fstore #8
      //   799: fload #9
      //   801: invokestatic abs : (F)F
      //   804: aload_0
      //   805: getfield mMinimumFlingVelocity : I
      //   808: i2f
      //   809: fcmpl
      //   810: ifgt -> 836
      //   813: fload #8
      //   815: invokestatic abs : (F)F
      //   818: aload_0
      //   819: getfield mMinimumFlingVelocity : I
      //   822: i2f
      //   823: fcmpl
      //   824: ifle -> 830
      //   827: goto -> 836
      //   830: iconst_0
      //   831: istore #12
      //   833: goto -> 856
      //   836: aload_0
      //   837: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   840: aload_0
      //   841: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   844: aload_1
      //   845: fload #8
      //   847: fload #9
      //   849: invokeinterface onFling : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   854: istore #12
      //   856: aload_0
      //   857: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   860: ifnull -> 870
      //   863: aload_0
      //   864: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   867: invokevirtual recycle : ()V
      //   870: aload_0
      //   871: aload #13
      //   873: putfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   876: aload_0
      //   877: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   880: ifnull -> 895
      //   883: aload_0
      //   884: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   887: invokevirtual recycle : ()V
      //   890: aload_0
      //   891: aconst_null
      //   892: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   895: aload_0
      //   896: iconst_0
      //   897: putfield mIsDoubleTapping : Z
      //   900: aload_0
      //   901: iconst_0
      //   902: putfield mDeferConfirmSingleTap : Z
      //   905: aload_0
      //   906: getfield mHandler : Landroid/os/Handler;
      //   909: iconst_1
      //   910: invokevirtual removeMessages : (I)V
      //   913: aload_0
      //   914: getfield mHandler : Landroid/os/Handler;
      //   917: iconst_2
      //   918: invokevirtual removeMessages : (I)V
      //   921: goto -> 1186
      //   924: aload_0
      //   925: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   928: ifnull -> 1037
      //   931: aload_0
      //   932: getfield mHandler : Landroid/os/Handler;
      //   935: iconst_3
      //   936: invokevirtual hasMessages : (I)Z
      //   939: istore #12
      //   941: iload #12
      //   943: ifeq -> 954
      //   946: aload_0
      //   947: getfield mHandler : Landroid/os/Handler;
      //   950: iconst_3
      //   951: invokevirtual removeMessages : (I)V
      //   954: aload_0
      //   955: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   958: ifnull -> 1024
      //   961: aload_0
      //   962: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   965: ifnull -> 1024
      //   968: iload #12
      //   970: ifeq -> 1024
      //   973: aload_0
      //   974: aload_0
      //   975: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   978: aload_0
      //   979: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   982: aload_1
      //   983: invokespecial isConsideredDoubleTap : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;Landroid/view/MotionEvent;)Z
      //   986: ifeq -> 1024
      //   989: aload_0
      //   990: iconst_1
      //   991: putfield mIsDoubleTapping : Z
      //   994: aload_0
      //   995: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   998: aload_0
      //   999: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1002: invokeinterface onDoubleTap : (Landroid/view/MotionEvent;)Z
      //   1007: iconst_0
      //   1008: ior
      //   1009: aload_0
      //   1010: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   1013: aload_1
      //   1014: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   1019: ior
      //   1020: istore_2
      //   1021: goto -> 1039
      //   1024: aload_0
      //   1025: getfield mHandler : Landroid/os/Handler;
      //   1028: iconst_3
      //   1029: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT : I
      //   1032: i2l
      //   1033: invokevirtual sendEmptyMessageDelayed : (IJ)Z
      //   1036: pop
      //   1037: iconst_0
      //   1038: istore_2
      //   1039: aload_0
      //   1040: fload #8
      //   1042: putfield mLastFocusX : F
      //   1045: aload_0
      //   1046: fload #8
      //   1048: putfield mDownFocusX : F
      //   1051: aload_0
      //   1052: fload #11
      //   1054: putfield mLastFocusY : F
      //   1057: aload_0
      //   1058: fload #11
      //   1060: putfield mDownFocusY : F
      //   1063: aload_0
      //   1064: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1067: ifnull -> 1077
      //   1070: aload_0
      //   1071: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1074: invokevirtual recycle : ()V
      //   1077: aload_0
      //   1078: aload_1
      //   1079: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   1082: putfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1085: aload_0
      //   1086: iconst_1
      //   1087: putfield mAlwaysInTapRegion : Z
      //   1090: aload_0
      //   1091: iconst_1
      //   1092: putfield mAlwaysInBiggerTapRegion : Z
      //   1095: aload_0
      //   1096: iconst_1
      //   1097: putfield mStillDown : Z
      //   1100: aload_0
      //   1101: iconst_0
      //   1102: putfield mInLongPress : Z
      //   1105: aload_0
      //   1106: iconst_0
      //   1107: putfield mDeferConfirmSingleTap : Z
      //   1110: aload_0
      //   1111: getfield mIsLongpressEnabled : Z
      //   1114: ifeq -> 1151
      //   1117: aload_0
      //   1118: getfield mHandler : Landroid/os/Handler;
      //   1121: iconst_2
      //   1122: invokevirtual removeMessages : (I)V
      //   1125: aload_0
      //   1126: getfield mHandler : Landroid/os/Handler;
      //   1129: iconst_2
      //   1130: aload_0
      //   1131: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1134: invokevirtual getDownTime : ()J
      //   1137: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1140: i2l
      //   1141: ladd
      //   1142: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT : I
      //   1145: i2l
      //   1146: ladd
      //   1147: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1150: pop
      //   1151: aload_0
      //   1152: getfield mHandler : Landroid/os/Handler;
      //   1155: iconst_1
      //   1156: aload_0
      //   1157: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1160: invokevirtual getDownTime : ()J
      //   1163: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1166: i2l
      //   1167: ladd
      //   1168: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1171: pop
      //   1172: iload_2
      //   1173: aload_0
      //   1174: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   1177: aload_1
      //   1178: invokeinterface onDown : (Landroid/view/MotionEvent;)Z
      //   1183: ior
      //   1184: istore #12
      //   1186: iload #12
      //   1188: ireturn
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mIsLongpressEnabled = param1Boolean;
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDoubleTapListener = param1OnDoubleTapListener;
    }
    
    private class GestureHandler extends Handler {
      GestureHandler() {}
      
      GestureHandler(Handler param2Handler) {
        super(param2Handler.getLooper());
      }
      
      public void handleMessage(Message param2Message) {
        StringBuilder stringBuilder;
        switch (param2Message.what) {
          default:
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown message ");
            stringBuilder.append(param2Message);
            throw new RuntimeException(stringBuilder.toString());
          case 3:
            if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener != null)
              if (!GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown) {
                GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
              } else {
                GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
              }  
            return;
          case 2:
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
            return;
          case 1:
            break;
        } 
        GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
      }
    }
  }
  
  private class GestureHandler extends Handler {
    GestureHandler() {}
    
    GestureHandler(Handler param1Handler) {
      super(param1Handler.getLooper());
    }
    
    public void handleMessage(Message param1Message) {
      StringBuilder stringBuilder;
      switch (param1Message.what) {
        default:
          stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown message ");
          stringBuilder.append(param1Message);
          throw new RuntimeException(stringBuilder.toString());
        case 3:
          if (this.this$0.mDoubleTapListener != null)
            if (!this.this$0.mStillDown) {
              this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
            } else {
              this.this$0.mDeferConfirmSingleTap = true;
            }  
          return;
        case 2:
          this.this$0.dispatchLongPress();
          return;
        case 1:
          break;
      } 
      this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
    }
  }
  
  static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
    private final GestureDetector mDetector;
    
    GestureDetectorCompatImplJellybeanMr2(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      this.mDetector = new GestureDetector(param1Context, param1OnGestureListener, param1Handler);
    }
    
    public boolean isLongpressEnabled() {
      return this.mDetector.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      return this.mDetector.onTouchEvent(param1MotionEvent);
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mDetector.setIsLongpressEnabled(param1Boolean);
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDetector.setOnDoubleTapListener(param1OnDoubleTapListener);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\view\GestureDetectorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */