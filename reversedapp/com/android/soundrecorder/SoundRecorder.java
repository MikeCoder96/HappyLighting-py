package com.android.soundrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.qh.blelight.MyApplication;
import com.qh.blelight.MyBluetoothGatt;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SoundRecorder extends Activity implements Recorder.OnStateChangedListener {
  private static final int ANIMATIONEACHOFFSET = 600;
  
  static final String ANY_ANY = "*/*";
  
  static final String AUDIO_3GPP = "audio/3gpp";
  
  static final String AUDIO_AMR = "audio/amr";
  
  static final String AUDIO_ANY = "audio/*";
  
  static final int BITRATE_3GPP = 5900;
  
  static final int BITRATE_AMR = 5900;
  
  public static final int MAXCOLOR = 254;
  
  static final String MAX_FILE_SIZE_KEY = "max_file_size";
  
  static final String RECORDER_STATE_KEY = "recorder_state";
  
  static final String SAMPLE_INTERRUPTED_KEY = "sample_interrupted";
  
  static final String STATE_FILE_NAME = "soundrecorder.state";
  
  static final String TAG = "SoundRecorder";
  
  public static int changenum = 10;
  
  private int a = 0;
  
  private AnimationSet aniSet;
  
  private AnimationSet aniSet2;
  
  private AnimationSet aniSet3;
  
  public int blue = 0;
  
  private ImageView btn;
  
  public int green = 0;
  
  private Handler handler = new Handler() {
      public void handleMessage(Message param1Message) {
        if (param1Message.what == 546) {
          SoundRecorder.this.wave2.startAnimation((Animation)SoundRecorder.this.aniSet2);
        } else if (param1Message.what == 819) {
          SoundRecorder.this.wave3.startAnimation((Animation)SoundRecorder.this.aniSet3);
        } 
        super.handleMessage(param1Message);
      }
    };
  
  public ImageView img_microphone;
  
  public boolean isOk = false;
  
  String mErrorUiMessage = null;
  
  final Handler mHandler = new Handler();
  
  long mMaxFileSize = -1L;
  
  public MyApplication mMyApplication;
  
  Recorder mRecorder;
  
  RemainingTimeCalculator mRemainingTimeCalculator;
  
  String mRequestedType = "audio/*";
  
  private BroadcastReceiver mSDCardMountEventReceiver = null;
  
  boolean mSampleInterrupted = false;
  
  public Handler mShowHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          SoundRecorder.this.setCircleShow(param1Message.what);
          return false;
        }
      });
  
  String mTimerFormat;
  
  Runnable mUpdateTimer = new Runnable() {
      public void run() {
        SoundRecorder.this.updateTimerView();
      }
    };
  
  VUMeter mVUMeter;
  
  PowerManager.WakeLock mWakeLock;
  
  private int num = 0;
  
  public int red = 255;
  
  public Handler soundControlHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 0) {
            SoundRecorder.this.mRecorder.stop();
            SoundRecorder.this.mRecorder.clear();
            SoundRecorder.this.finish();
          } 
          return false;
        }
      });
  
  private ImageView wave1;
  
  private ImageView wave2;
  
  private ImageView wave3;
  
  private void addColor() {
    if (this.a == 0) {
      this.red = 254;
      this.blue = 0;
      this.green += changenum;
      if (this.green >= 254) {
        this.green = 254;
        this.a = 1;
      } 
    } else if (this.a == 1) {
      this.green = 254;
      this.red -= changenum;
      if (this.red <= 0) {
        this.red = 0;
        this.a = 2;
      } 
    } else if (this.a == 2) {
      this.green = 254;
      this.blue += changenum;
      if (this.blue >= 254) {
        this.blue = 200;
        this.a = 3;
      } 
    } else if (this.a == 3) {
      this.blue = 254;
      this.green -= changenum;
      if (this.green <= 0) {
        this.green = 0;
        this.a = 4;
      } 
    } else if (this.a == 4) {
      this.blue = 254;
      this.red += changenum;
      if (this.red >= 254) {
        this.red = 254;
        this.a = 5;
      } 
    } else if (this.a == 5) {
      this.red = 254;
      this.blue -= changenum;
      if (this.blue <= 0) {
        this.blue = 0;
        this.a = 0;
      } 
    } 
  }
  
  private Uri addToMediaDB(File paramFile) {
    Resources resources = getResources();
    ContentValues contentValues = new ContentValues();
    long l1 = System.currentTimeMillis();
    long l2 = paramFile.lastModified();
    Date date = new Date(l1);
    String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    long l3 = this.mRecorder.sampleLength();
    contentValues.put("is_music", "0");
    contentValues.put("title", str);
    contentValues.put("_data", paramFile.getAbsolutePath());
    contentValues.put("date_added", Integer.valueOf((int)(l1 / 1000L)));
    contentValues.put("date_modified", Integer.valueOf((int)(l2 / 1000L)));
    contentValues.put("duration", Long.valueOf(l3 * 1000L));
    contentValues.put("mime_type", this.mRequestedType);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("Inserting audio record: ");
    stringBuilder1.append(contentValues.toString());
    Log.d("SoundRecorder", stringBuilder1.toString());
    ContentResolver contentResolver = getContentResolver();
    Uri uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("ContentURI: ");
    stringBuilder2.append(uri2);
    Log.d("SoundRecorder", stringBuilder2.toString());
    Uri uri1 = contentResolver.insert(uri2, contentValues);
    if (uri1 == null) {
      (new AlertDialog.Builder((Context)this)).setTitle(2131624000).setCancelable(false).show();
      return null;
    } 
    if (getPlaylistId(resources) == -1)
      createPlaylist(resources, contentResolver); 
    addToPlaylist(contentResolver, Integer.valueOf(uri1.getLastPathSegment()).intValue(), getPlaylistId(resources));
    sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", uri1));
    return uri1;
  }
  
  private void addToPlaylist(ContentResolver paramContentResolver, int paramInt, long paramLong) {
    Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", paramLong);
    Cursor cursor = paramContentResolver.query(uri, new String[] { "count(*)" }, null, null, null);
    cursor.moveToFirst();
    int i = cursor.getInt(0);
    cursor.close();
    ContentValues contentValues = new ContentValues();
    contentValues.put("play_order", Integer.valueOf(i + paramInt));
    contentValues.put("audio_id", Integer.valueOf(paramInt));
    paramContentResolver.insert(uri, contentValues);
  }
  
  private void cancalWaveAnimation() {
    this.wave1.clearAnimation();
    this.wave2.clearAnimation();
    this.wave3.clearAnimation();
  }
  
  private Uri createPlaylist(Resources paramResources, ContentResolver paramContentResolver) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("name", "My recordings");
    return paramContentResolver.insert(MediaStore.Audio.Playlists.getContentUri("external"), contentValues);
  }
  
  private AnimationSet getNewAnimationSet() {
    AnimationSet animationSet = new AnimationSet(true);
    ScaleAnimation scaleAnimation = new ScaleAnimation(1.0F, 3.3F, 1.0F, 3.3F, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setDuration(1800L);
    scaleAnimation.setRepeatCount(-1);
    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.1F);
    alphaAnimation.setRepeatCount(-1);
    animationSet.setDuration(1800L);
    animationSet.addAnimation((Animation)scaleAnimation);
    animationSet.addAnimation((Animation)alphaAnimation);
    return animationSet;
  }
  
  private int getPlaylistId(Resources paramResources) {
    Cursor cursor = query(MediaStore.Audio.Playlists.getContentUri("external"), new String[] { "_id" }, "name=?", new String[] { "My recordings" }, (String)null);
    if (cursor == null)
      Log.v("SoundRecorder", "query returns null"); 
    byte b = -1;
    int i = b;
    if (cursor != null) {
      cursor.moveToFirst();
      i = b;
      if (!cursor.isAfterLast())
        i = cursor.getInt(0); 
    } 
    cursor.close();
    return i;
  }
  
  private void init() {}
  
  private void initResourceRefs() {
    this.mVUMeter = (VUMeter)findViewById(2131231261);
    this.mVUMeter.setRecorder(this.mRecorder);
    this.mVUMeter.setShowHandler(this.mShowHandler);
  }
  
  private Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
    try {
      ContentResolver contentResolver = getContentResolver();
      return (contentResolver == null) ? null : contentResolver.query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    } catch (UnsupportedOperationException unsupportedOperationException) {
      return null;
    } 
  }
  
  private void registerExternalStorageListener() {
    if (this.mSDCardMountEventReceiver == null) {
      this.mSDCardMountEventReceiver = new BroadcastReceiver() {
          public void onReceive(Context param1Context, Intent param1Intent) {
            String str = param1Intent.getAction();
            if (str.equals("android.intent.action.MEDIA_EJECT")) {
              SoundRecorder.this.mRecorder.delete();
            } else if (str.equals("android.intent.action.MEDIA_MOUNTED")) {
              SoundRecorder.this.mSampleInterrupted = false;
              SoundRecorder.this.updateUi();
            } 
          }
        };
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction("android.intent.action.MEDIA_EJECT");
      intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
      intentFilter.addDataScheme("file");
      registerReceiver(this.mSDCardMountEventReceiver, intentFilter);
    } 
  }
  
  private void saveSample() {
    if (this.mRecorder.sampleLength() == 0)
      return; 
    try {
      Uri uri = addToMediaDB(this.mRecorder.sampleFile());
      if (uri == null)
        return; 
      setResult(-1, (new Intent()).setData(uri));
      return;
    } catch (UnsupportedOperationException unsupportedOperationException) {
      return;
    } 
  }
  
  private void setCircleShow(int paramInt) {
    if (this.num != paramInt) {
      int i = Color.argb(255, this.red, this.green, this.blue);
      double d = paramInt;
      Double.isNaN(d);
      setMusicColor(i, (int)(d * 50.5D));
    } 
    this.num = paramInt;
    addColor();
  }
  
  private void setMusicColor(int paramInt1, int paramInt2) {
    for (String str : this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = (MyBluetoothGatt)this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          byte b = myBluetoothGatt.datas[2]; 
      } 
      int i = Color.argb(255, Color.red(paramInt1) * paramInt2 / 255, Color.green(paramInt1) * paramInt2 / 255, Color.blue(paramInt1) * paramInt2 / 255);
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = (MyBluetoothGatt)this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
          myBluetoothGatt.setMusicColor(i); 
      } 
    } 
  }
  
  private void showWaveAnimation() {
    this.wave1.startAnimation((Animation)this.aniSet);
    this.handler.sendEmptyMessageDelayed(546, 600L);
    this.handler.sendEmptyMessageDelayed(819, 1200L);
  }
  
  private void stopAudioPlayback() {
    Intent intent = new Intent("com.android.music.musicservicecommand");
    intent.putExtra("command", "pause");
    sendBroadcast(intent);
  }
  
  private void updateTimeRemaining() {
    if (this.mRemainingTimeCalculator.timeRemaining() <= 0L) {
      this.mSampleInterrupted = true;
      switch (this.mRemainingTimeCalculator.currentLowerLimit()) {
        default:
          this.mErrorUiMessage = null;
          return;
        case 2:
          this.mErrorUiMessage = "";
          return;
        case 1:
          break;
      } 
      this.mErrorUiMessage = "";
      return;
    } 
    getResources();
  }
  
  private void updateTimerView() {
    boolean bool;
    getResources();
    int i = this.mRecorder.state();
    if (i == 1 || i == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.mRecorder.progress();
    } else {
      this.mRecorder.sampleLength();
    } 
    if (i != 2 && i == 1)
      updateTimeRemaining(); 
    if (bool)
      this.mHandler.postDelayed(this.mUpdateTimer, 1000L); 
  }
  
  private void updateUi() {
    boolean bool;
    String str;
    getResources();
    switch (this.mRecorder.state()) {
      case 2:
        this.mVUMeter.setVisibility(4);
        break;
      case 1:
        this.mVUMeter.setVisibility(0);
        break;
      case 0:
        if (this.mRecorder.sampleLength() == 0) {
          this.mVUMeter.setVisibility(0);
        } else {
          this.mVUMeter.setVisibility(4);
        } 
        bool = this.mSampleInterrupted;
        str = this.mErrorUiMessage;
        break;
    } 
    updateTimerView();
    this.mVUMeter.invalidate();
    float f = 0.3926991F;
    if (this.mRecorder != null)
      f = 0.3926991F + this.mRecorder.getMaxAmplitude() * 2.3561947F / 32768.0F; 
    if (f <= 0.0F)
      Math.max(f, -0.18F); 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    setContentView(2131361830);
    initResourceRefs();
    updateUi();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    Intent intent = getIntent();
    if (intent != null) {
      String str = intent.getType();
      if ("audio/amr".equals(str) || "audio/3gpp".equals(str) || "audio/*".equals(str) || "*/*".equals(str)) {
        this.mRequestedType = str;
      } else if (str != null) {
        setResult(0);
        finish();
        return;
      } 
      this.mMaxFileSize = intent.getLongExtra("android.provider.MediaStore.extra.MAX_BYTES", -1L);
    } 
    if ("audio/*".equals(this.mRequestedType) || "*/*".equals(this.mRequestedType))
      this.mRequestedType = "audio/3gpp"; 
    setContentView(2131361830);
    this.aniSet = getNewAnimationSet();
    this.aniSet2 = getNewAnimationSet();
    this.aniSet3 = getNewAnimationSet();
    this.btn = (ImageView)findViewById(2131230762);
    this.wave1 = (ImageView)findViewById(2131231271);
    this.wave2 = (ImageView)findViewById(2131231272);
    this.wave3 = (ImageView)findViewById(2131231273);
    showWaveAnimation();
    this.mMyApplication = (MyApplication)getApplication();
    this.mMyApplication.soundControlHandler = this.soundControlHandler;
    this.mRecorder = new Recorder();
    this.mRecorder.setOnStateChangedListener(this);
    this.mRemainingTimeCalculator = new RemainingTimeCalculator();
    this.mWakeLock = ((PowerManager)getSystemService("power")).newWakeLock(6, "SoundRecorder");
    initResourceRefs();
    setResult(0);
    registerExternalStorageListener();
    if (paramBundle != null) {
      paramBundle = paramBundle.getBundle("recorder_state");
      if (paramBundle != null) {
        this.mRecorder.restoreState(paramBundle);
        this.mSampleInterrupted = paramBundle.getBoolean("sample_interrupted", false);
        this.mMaxFileSize = paramBundle.getLong("max_file_size", -1L);
      } 
    } 
    updateUi();
    this.isOk = true;
    init();
  }
  
  public void onDestroy() {
    if (this.mSDCardMountEventReceiver != null) {
      unregisterReceiver(this.mSDCardMountEventReceiver);
      this.mSDCardMountEventReceiver = null;
    } 
    super.onDestroy();
  }
  
  public void onError(int paramInt) {
    String str;
    getResources();
    switch (paramInt) {
      default:
        str = null;
        break;
      case 2:
      case 3:
        str = "";
        break;
      case 1:
        str = "";
        break;
    } 
    if (str != null)
      (new AlertDialog.Builder((Context)this)).setTitle(2131624000).setMessage(str).setPositiveButton("OK", null).setCancelable(false).show(); 
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return (paramInt == 4) ? true : super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onPause() {
    int i = this.mRecorder.state();
    boolean bool = true;
    if (i != 1)
      bool = false; 
    this.mSampleInterrupted = bool;
    super.onPause();
  }
  
  protected void onResume() {
    if (this.isOk)
      openRecorder(); 
    cancalWaveAnimation();
    showWaveAnimation();
    super.onResume();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    if (this.mRecorder.sampleLength() == 0)
      return; 
    Bundle bundle = new Bundle();
    this.mRecorder.saveState(bundle);
    bundle.putBoolean("sample_interrupted", this.mSampleInterrupted);
    bundle.putLong("max_file_size", this.mMaxFileSize);
    paramBundle.putBundle("recorder_state", bundle);
  }
  
  public void onStateChanged(int paramInt) {
    if (paramInt == 2 || paramInt == 1) {
      this.mSampleInterrupted = false;
      this.mErrorUiMessage = null;
      this.mWakeLock.acquire();
    } else if (this.mWakeLock.isHeld()) {
      this.mWakeLock.release();
    } 
    updateUi();
  }
  
  public void onStop() {
    super.onStop();
  }
  
  public void openRecorder() {
    this.mRemainingTimeCalculator.reset();
    if (!Environment.getExternalStorageState().equals("mounted")) {
      this.mSampleInterrupted = true;
      this.mErrorUiMessage = "insert_sd_card";
      updateUi();
    } else if (!this.mRemainingTimeCalculator.diskSpaceAvailable()) {
      this.mSampleInterrupted = true;
      this.mErrorUiMessage = "insert_sd_card";
      updateUi();
    } else {
      stopAudioPlayback();
      if ("audio/amr".equals(this.mRequestedType)) {
        this.mRemainingTimeCalculator.setBitRate(5900);
        this.mRecorder.startRecording(3, ".amr", (Context)this);
      } else if ("audio/3gpp".equals(this.mRequestedType)) {
        this.mRemainingTimeCalculator.setBitRate(5900);
        this.mRecorder.startRecording(1, ".3gpp", (Context)this);
      } else {
        throw new IllegalArgumentException("Invalid output file type requested");
      } 
      if (this.mMaxFileSize != -1L)
        this.mRemainingTimeCalculator.setFileSizeLimit(this.mRecorder.sampleFile(), this.mMaxFileSize); 
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\SoundRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */