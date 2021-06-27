package com.qh.blelight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.qh.tools.MusicData;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@SuppressLint({"NewApi"})
public class MusicActivity<VisualizerView> extends Activity {
  public static int HEIGHT = 65;
  
  public static int HEIGHT_FREQUENCY = 85;
  
  public static final int Light_COLOR_CHANGE = 3;
  
  public static final int MAXCOLOR = 200;
  
  public static final int MUSIC_STOP = 2;
  
  public static final int UPDATA_UI = 1;
  
  private static final float VISUALIZER_HEIGHT_DIP = 100.0F;
  
  public static int blue = 0;
  
  public static int calpha = 0;
  
  public static int cblue = 0;
  
  public static int cgreen = 50;
  
  public static int changenum = 5;
  
  public static int cred = 150;
  
  public static int green = 50;
  
  public static int red = 150;
  
  public byte[] Bytes;
  
  private int a = 0;
  
  private AudioManager audioManager;
  
  public int bomSize = 0;
  
  private Context context;
  
  public int db = 0;
  
  private boolean flay = false;
  
  public int h = 300;
  
  public ImageView img_hop;
  
  public ImageView img_mucis_mod;
  
  public ImageView img_play;
  
  public ImageView img_play_last;
  
  public ImageView img_play_next;
  
  public boolean isReplay = false;
  
  public boolean isVisualizer = false;
  
  boolean isfristplay = true;
  
  private boolean ispauseMusic = false;
  
  public LinearLayout lin_c;
  
  private LinearLayout lin_waveform;
  
  AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener() {
      public void onAudioFocusChange(int param1Int) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("focusChange=");
        stringBuilder.append(param1Int);
        Log.e("", stringBuilder.toString());
        if (MusicActivity.this.myMediaPlayer.isPlaying())
          MusicActivity.this.myMediaPlayer.pause(); 
      }
    };
  
  public AssetManager mAssetManager;
  
  private Equalizer mEqualizer;
  
  private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          StringBuilder stringBuilder;
          int i = param1Message.what;
          switch (i) {
            default:
              switch (i) {
              
              } 
              return false;
            case 3:
              MusicActivity.this.getcolorchang();
              return false;
            case 2:
              stringBuilder = new StringBuilder();
              stringBuilder.append("musicMod  ");
              stringBuilder.append(MusicActivity.this.musicMod);
              Log.e("MUSIC_STOP", stringBuilder.toString());
              if (MusicActivity.this.musicMod == 2) {
                MusicActivity.access$008(MusicActivity.this);
                if (MusicActivity.this.playingId >= MusicActivity.this.musicInfos.size())
                  MusicActivity.access$002(MusicActivity.this, 0); 
                MusicActivity.this.playMusic();
                MusicActivity.this.mHandler.sendEmptyMessage(1);
              } 
              if (MusicActivity.this.musicMod == 1) {
                i = (new Random()).nextInt(MusicActivity.this.musicInfos.size());
                if (i == MusicActivity.this.playingId) {
                  MusicActivity.access$008(MusicActivity.this);
                } else {
                  MusicActivity.access$002(MusicActivity.this, i);
                } 
                stringBuilder = new StringBuilder();
                stringBuilder.append("playingId =");
                stringBuilder.append(MusicActivity.this.playingId);
                stringBuilder.append(" ");
                stringBuilder.append(MusicActivity.this.musicInfos.size());
                Log.e("playingId", stringBuilder.toString());
                MusicActivity.this.playMusic();
                MusicActivity.this.mHandler.sendEmptyMessage(1);
              } 
              if (MusicActivity.this.musicMod == 0)
                MusicActivity.this.playMusic(); 
              return false;
            case 1:
              break;
          } 
          MusicActivity.this.updataUI();
          return false;
        }
      });
  
  private ListView mListView;
  
  public Visualizer.MeasurementPeakRms mMeasurementPeakRms = new Visualizer.MeasurementPeakRms();
  
  private MusicInfoAdapter mMusicInfoAdapter;
  
  private MyApplication mMyApplication;
  
  public Resources mResources;
  
  String mTimerFormat = "%02d:%02d";
  
  private Visualizer mVisualizer;
  
  private Handler musicHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 0) {
            Random random = new Random();
            int i;
            for (i = random.nextInt(MusicActivity.this.musicInfos.size()); MusicActivity.this.playingId == i; i = random.nextInt(MusicActivity.this.musicInfos.size()));
            MusicActivity.access$002(MusicActivity.this, i);
            MusicActivity.this.mMusicInfoAdapter.setItem(MusicActivity.this.playingId);
            MusicActivity.this.playMusic();
            MusicActivity.this.mHandler.sendEmptyMessage(1);
          } 
          if (param1Message.what == 2) {
            if (MusicActivity.this.myMediaPlayer.isPlaying())
              MusicActivity.this.myMediaPlayer.pause(); 
            MusicActivity.this.mHandler.sendEmptyMessage(1);
          } 
          return false;
        }
      });
  
  public ArrayList<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
  
  public int musicMod = 2;
  
  public MediaPlayer myMediaPlayer;
  
  private View.OnClickListener myOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        if (MusicActivity.this.mMyApplication.isOpenMusicHop()) {
          MusicActivity.this.mMyApplication.isopenmic = false;
          MusicActivity.this.mMyApplication.openmic(MusicActivity.this.mMyApplication.isopenmic);
          if (MusicActivity.this.mMyApplication.AdjustHandler != null)
            MusicActivity.this.mMyApplication.AdjustHandler.sendEmptyMessage(5); 
        } 
        int i = param1View.getId();
        if (i != 2131230891) {
          StringBuilder stringBuilder;
          switch (i) {
            default:
              return;
            case 2131230919:
              stringBuilder = new StringBuilder();
              stringBuilder.append("img_play_next ");
              stringBuilder.append(MusicActivity.this.musicMod);
              Log.e("imgext", stringBuilder.toString());
              if (MusicActivity.this.musicMod == 1) {
                MusicActivity.this.musicHandler.sendEmptyMessage(0);
                stringBuilder = new StringBuilder();
                stringBuilder.append("img_play_next ");
                stringBuilder.append(MusicActivity.this.musicMod);
                Log.e("imgext", stringBuilder.toString());
              } else {
                if (MusicActivity.this.musicInfos == null || MusicActivity.this.musicInfos.size() == 0)
                  return; 
                if (MusicActivity.this.playingId == MusicActivity.this.musicInfos.size() - 1) {
                  MusicActivity.access$002(MusicActivity.this, 0);
                } else if (MusicActivity.this.playingId == -1) {
                  MusicActivity.access$002(MusicActivity.this, 0);
                } else {
                  MusicActivity.access$008(MusicActivity.this);
                } 
                MusicActivity.this.mMusicInfoAdapter.setItem(MusicActivity.this.playingId);
                MusicActivity.this.playMusic();
                MusicActivity.this.mHandler.sendEmptyMessage(1);
              } 
            case 2131230918:
              if (MusicActivity.this.musicMod == 1) {
                MusicActivity.this.musicHandler.sendEmptyMessage(0);
                stringBuilder = new StringBuilder();
                stringBuilder.append("img_play_last ");
                stringBuilder.append(MusicActivity.this.musicMod);
                Log.e("imgext", stringBuilder.toString());
              } else {
                if (MusicActivity.this.musicInfos == null || MusicActivity.this.musicInfos.size() == 0)
                  return; 
                if (MusicActivity.this.playingId == 0) {
                  MusicActivity.access$002(MusicActivity.this, MusicActivity.this.musicInfos.size() - 1);
                } else if (MusicActivity.this.playingId == -1) {
                  MusicActivity.access$002(MusicActivity.this, 0);
                } else {
                  MusicActivity.access$010(MusicActivity.this);
                } 
                MusicActivity.this.playMusic();
                MusicActivity.this.mMusicInfoAdapter.setItem(MusicActivity.this.playingId);
                MusicActivity.this.mHandler.sendEmptyMessage(1);
              } 
            case 2131230917:
              break;
          } 
          if (MusicActivity.this.musicInfos == null || MusicActivity.this.musicInfos.size() == 0)
            return; 
          if (MusicActivity.this.playingId == -1) {
            MusicActivity.access$002(MusicActivity.this, 0);
            MusicActivity.this.playMusic();
          } 
          if (MusicActivity.this.myMediaPlayer.isPlaying()) {
            MusicActivity.this.myMediaPlayer.pause();
            MusicActivity.this.mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MusicActivity.this.db = 0;
                    MusicActivity.this.setMusicColor(Color.argb(255, MusicActivity.cred, MusicActivity.cgreen, MusicActivity.cblue));
                  }
                }200L);
          } else {
            MusicActivity.this.myMediaPlayer.start();
            MusicActivity.this.setVisualizerEnabled(true);
          } 
          MusicActivity.this.mHandler.sendEmptyMessage(1);
        } 
        SharedPreferences.Editor editor = MusicActivity.this.settings.edit();
        editor.putBoolean("isOpenMusicHop", MusicActivity.this.mMyApplication.isOpenMusicHop());
        editor.commit();
        if (MusicActivity.this.mMyApplication.isOpenMusicHop()) {
          MusicActivity.this.mMyApplication.isopenmic = false;
          MusicActivity.this.mMyApplication.openmic(MusicActivity.this.mMyApplication.isopenmic);
          if (MusicActivity.this.mMyApplication.AdjustHandler != null)
            MusicActivity.this.mMyApplication.AdjustHandler.sendEmptyMessage(5); 
        } 
      }
    };
  
  private MediaPlayer.OnCompletionListener myOnCompletionListener = new MediaPlayer.OnCompletionListener() {
      public void onCompletion(MediaPlayer param1MediaPlayer) {
        MusicActivity.this.mHandler.sendEmptyMessage(1);
        MusicActivity.this.mHandler.sendEmptyMessage(2);
      }
    };
  
  private AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        MusicActivity.this.mMusicInfoAdapter.setItem(param1Int);
        if (MusicActivity.this.mMyApplication.isOpenMusicHop()) {
          MusicActivity.this.mMyApplication.isopenmic = false;
          MusicActivity.this.mMyApplication.openmic(MusicActivity.this.mMyApplication.isopenmic);
          if (MusicActivity.this.mMyApplication.AdjustHandler != null)
            MusicActivity.this.mMyApplication.AdjustHandler.sendEmptyMessage(5); 
        } 
        if (MusicActivity.this.playingId == -1) {
          MusicActivity.access$002(MusicActivity.this, param1Int);
          MusicActivity.this.playMusic();
          MusicActivity.this.setVisualizerEnabled(true);
          MusicActivity.this.mHandler.sendEmptyMessage(1);
          return;
        } 
        if (MusicActivity.this.playingId == param1Int) {
          if (MusicActivity.this.myMediaPlayer.isPlaying()) {
            MusicActivity.this.myMediaPlayer.pause();
            MusicActivity.this.mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MusicActivity.this.db = 0;
                    MusicActivity.this.setMusicColor(Color.argb(255, MusicActivity.cred, MusicActivity.cgreen, MusicActivity.cblue));
                  }
                }200L);
          } else {
            MusicActivity.this.myMediaPlayer.start();
            MusicActivity.this.setVisualizerEnabled(true);
          } 
        } else {
          MusicActivity.access$002(MusicActivity.this, param1Int);
          MusicActivity.this.playMusic();
          MusicActivity.this.setVisualizerEnabled(true);
        } 
        MusicActivity.this.mHandler.sendEmptyMessage(1);
        MusicActivity.access$002(MusicActivity.this, param1Int);
      }
    };
  
  public float[] myPoints;
  
  private int num = 0;
  
  public int oldRms = -9000;
  
  private File path;
  
  private int playingId = -1;
  
  private ArrayList<Float> points = new ArrayList<Float>();
  
  private Runnable progressBarRunnable = new Runnable() {
      public void run() {
        MusicActivity.this.seekbar_play.setProgress(MusicActivity.this.myMediaPlayer.getCurrentPosition());
        long l = (MusicActivity.this.myMediaPlayer.getCurrentPosition() / 1000);
        String str = String.format(MusicActivity.this.mTimerFormat, new Object[] { Long.valueOf(l / 60L), Long.valueOf(l % 60L) });
        TextView textView = MusicActivity.this.tx_statrtime;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(str);
        textView.setText(stringBuilder.toString());
        if (MusicActivity.this.myMediaPlayer.isPlaying()) {
          MusicActivity.this.img_play.setBackgroundResource(2131165419);
          MusicActivity.this.pauseMusic();
          if (!MusicActivity.this.mMyApplication.isOpenVisualizer)
            MusicActivity.this.mMyApplication.mMediaRecorderDemo.startRecord(); 
        } else {
          MusicActivity.this.img_play.setBackgroundResource(2131165412);
          MusicActivity.this.mMyApplication.reSetCMD();
        } 
        if (MusicActivity.this.myMediaPlayer != null && MusicActivity.this.myMediaPlayer.isPlaying()) {
          if (MusicActivity.this.isfristplay) {
            MusicActivity.this.isfristplay = false;
            MusicActivity.this.myMediaPlayer.pause();
            MusicActivity.this.myMediaPlayer.seekTo(1);
          } 
          MusicActivity.this.mHandler.postDelayed(MusicActivity.this.progressBarRunnable, 1000L);
        } 
      }
    };
  
  public SeekBar seekbar_play;
  
  public SharedPreferences settings;
  
  public int topSize = 0;
  
  public TextView tx_album;
  
  public TextView tx_endtime;
  
  public TextView tx_music_name;
  
  public TextView tx_statrtime;
  
  public int w = 180;
  
  private int addcolor(int paramInt1, int paramInt2) {
    paramInt2 = paramInt1 + paramInt2;
    paramInt1 = paramInt2;
    if (paramInt2 > 255)
      paramInt1 = 255; 
    paramInt2 = paramInt1;
    if (paramInt1 < 0)
      paramInt2 = 0; 
    return paramInt2;
  }
  
  private void getcolorchang() {
    this.a = (int)Math.floor(Math.random() * 6.0D);
    if (this.a < 0) {
      i = 0;
    } else {
      i = this.a;
    } 
    this.a = i;
    if (this.a > 5) {
      i = 5;
    } else {
      i = this.a;
    } 
    this.a = i;
    this.db = (int)Math.floor(Math.random() * 20.0D) + 1;
    int j = this.db;
    int i = 20;
    if (j <= 20)
      i = this.db; 
    this.db = i;
    setMusicColor(Color.argb(255, cred, cgreen, cblue));
    if (this.a == 0) {
      red = 200;
      blue = 0;
      green += changenum;
      if (green >= 200) {
        green = 200;
        this.a = 1;
      } 
    } else if (this.a == 1) {
      green = 200;
      red -= changenum;
      blue = 0;
      if (red <= 0) {
        red = 0;
        this.a = 2;
      } 
    } else if (this.a == 2) {
      green = 200;
      blue += changenum;
      red = 0;
      if (blue >= 200) {
        blue = 200;
        this.a = 3;
      } 
    } else if (this.a == 3) {
      blue = 200;
      green -= changenum;
      red = 0;
      if (green <= 0) {
        green = 0;
        this.a = 4;
      } 
    } else if (this.a == 4) {
      blue = 200;
      red += changenum;
      green = 0;
      if (red >= 200) {
        red = 200;
        this.a = 5;
      } 
    } else if (this.a == 5) {
      red = 200;
      blue -= changenum;
      green = 0;
      if (blue <= 0) {
        blue = 0;
        this.a = 0;
      } 
    } else if (this.a == 6) {
      red = 100 - changenum;
      blue = 100 - changenum;
      green = 100 - changenum;
    } 
  }
  
  private void init() {
    this.lin_waveform = (LinearLayout)findViewById(2131230972);
    this.img_play = (ImageView)findViewById(2131230917);
    this.img_play_next = (ImageView)findViewById(2131230919);
    this.img_play_last = (ImageView)findViewById(2131230918);
    this.img_mucis_mod = (ImageView)findViewById(2131230913);
    this.img_play.setOnClickListener(this.myOnClickListener);
    this.img_play_next.setOnClickListener(this.myOnClickListener);
    this.img_play_last.setOnClickListener(this.myOnClickListener);
    this.img_hop = (ImageView)findViewById(2131230891);
    this.img_hop.setOnClickListener(this.myOnClickListener);
    if (this.mMyApplication != null)
      if (this.mMyApplication.isOpenMusicHop()) {
        this.img_hop.setImageResource(2131165389);
      } else {
        this.img_hop.setImageResource(2131165390);
      }  
    this.tx_album = (TextView)findViewById(2131231239);
    this.tx_music_name = (TextView)findViewById(2131231249);
    this.tx_statrtime = (TextView)findViewById(2131231252);
    this.tx_endtime = (TextView)findViewById(2131231242);
    this.seekbar_play = (SeekBar)findViewById(2131231128);
    this.seekbar_play.setProgress(0);
    this.lin_c = (LinearLayout)findViewById(2131230958);
    this.img_mucis_mod.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            StringBuilder stringBuilder1;
            Context context2;
            MusicActivity musicActivity = MusicActivity.this;
            musicActivity.musicMod++;
            if (MusicActivity.this.musicMod >= 3)
              MusicActivity.this.musicMod = 0; 
            switch (MusicActivity.this.musicMod) {
              default:
                return;
              case 2:
                MusicActivity.this.img_mucis_mod.setImageResource(2131165423);
                context2 = MusicActivity.this.context;
                stringBuilder1 = new StringBuilder();
                stringBuilder1.append("");
                stringBuilder1.append(MusicActivity.this.mResources.getString(2131624057));
                Toast.makeText(context2, stringBuilder1.toString(), 0).show();
              case 1:
                MusicActivity.this.img_mucis_mod.setImageResource(2131165424);
                context1 = MusicActivity.this.context;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("");
                stringBuilder2.append(MusicActivity.this.mResources.getString(2131624056));
                Toast.makeText(context1, stringBuilder2.toString(), 0).show();
              case 0:
                break;
            } 
            MusicActivity.this.img_mucis_mod.setImageResource(2131165422);
            Context context1 = MusicActivity.this.context;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(MusicActivity.this.mResources.getString(2131624055));
            Toast.makeText(context1, stringBuilder2.toString(), 0).show();
          }
        });
  }
  
  @SuppressLint({"NewApi"})
  private void initVisualizer() {
    this.mVisualizer = new Visualizer(this.myMediaPlayer.getAudioSessionId());
    this.mVisualizer.setMeasurementMode(1);
    this.mVisualizer.setCaptureSize(512);
    this.mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
          public void onFftDataCapture(Visualizer param1Visualizer, byte[] param1ArrayOfbyte, int param1Int) {}
          
          public void onWaveFormDataCapture(Visualizer param1Visualizer, byte[] param1ArrayOfbyte, int param1Int) {
            if (!MusicActivity.this.myMediaPlayer.isPlaying())
              return; 
            if (MusicActivity.this.mMeasurementPeakRms == null || param1Visualizer == null)
              return; 
            param1Visualizer.getMeasurementPeakRms(MusicActivity.this.mMeasurementPeakRms);
            if (MusicActivity.this.mMeasurementPeakRms.mRms == -9600) {
              if (!MusicActivity.this.flay && MusicActivity.this.num > 80)
                MusicActivity.this.mMyApplication.isOpenVisualizer = false; 
              MusicActivity.access$1208(MusicActivity.this);
              return;
            } 
            MusicActivity.this.mMyApplication.isOpenVisualizer = true;
            MusicActivity.access$1102(MusicActivity.this, true);
            if (MusicActivity.this.mMeasurementPeakRms.mRms < -9000)
              return; 
            if (!MusicActivity.this.mMyApplication.mMediaRecorderDemo.isPlay) {
              int i = MusicActivity.this.mMeasurementPeakRms.mRms;
              param1Int = MusicActivity.this.oldRms;
              MusicActivity musicActivity = MusicActivity.this;
              double d1 = MusicActivity.this.mMeasurementPeakRms.mRms;
              double d2 = (i - param1Int);
              Double.isNaN(d2);
              Double.isNaN(d1);
              param1Int = (int)(d1 + d2 * 1.5D);
              d1 = (MusicActivity.this.mMeasurementPeakRms.mRms - MusicActivity.this.oldRms);
              Double.isNaN(d1);
              musicActivity.setcolor(param1Int, d1 * 2.5D);
              MusicActivity.this.mHandler.sendEmptyMessage(3);
              MusicActivity.this.oldRms = MusicActivity.this.mMeasurementPeakRms.mRms;
            } 
          }
        }Visualizer.getMaxCaptureRate() / 2, true, false);
  }
  
  private void pauseMusic() {
    if (!this.ispauseMusic) {
      Log.e("", "pauseMusic");
      this.ispauseMusic = true;
      this.audioManager.requestAudioFocus(this.listener, 3, 2);
    } 
    if (this.mMyApplication != null)
      this.mMyApplication.reSetCMD(); 
  }
  
  private void setMusicColor(int paramInt) {
    Iterator<String> iterator = MainActivity.ControlMACs.keySet().iterator();
    if (!this.mMyApplication.isOpenMusicHop())
      return; 
    while (iterator.hasNext()) {
      String str = iterator.next();
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          byte b = myBluetoothGatt.datas[2]; 
      } 
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
          myBluetoothGatt.isopenmyMIC = false;
          if (myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && (myBluetoothGatt.mLEdevice.getName().contains("Dream#") || myBluetoothGatt.mLEdevice.getName().contains("Dream~"))) {
            myBluetoothGatt.setMusicColor(paramInt, this.db);
            continue;
          } 
          myBluetoothGatt.setMusicColor(paramInt);
        } 
      } 
    } 
  }
  
  private void setcolor(int paramInt, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: putstatic com/qh/blelight/MusicActivity.cred : I
    //   6: iconst_0
    //   7: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   10: iconst_0
    //   11: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   14: iconst_0
    //   15: putstatic com/qh/blelight/MusicActivity.calpha : I
    //   18: iload_1
    //   19: sipush #-8000
    //   22: if_icmple -> 315
    //   25: iload_1
    //   26: sipush #-3000
    //   29: if_icmpgt -> 315
    //   32: iload_1
    //   33: invokestatic abs : (I)I
    //   36: istore #4
    //   38: iload #4
    //   40: sipush #3000
    //   43: isub
    //   44: i2d
    //   45: dstore #5
    //   47: dload #5
    //   49: invokestatic isNaN : (D)Z
    //   52: pop
    //   53: dload #5
    //   55: ldc2_w 0.05
    //   58: dmul
    //   59: d2i
    //   60: i2d
    //   61: dstore #5
    //   63: dload #5
    //   65: invokestatic isNaN : (D)Z
    //   68: pop
    //   69: dload #5
    //   71: ldc2_w 255.0
    //   74: ddiv
    //   75: dstore #5
    //   77: getstatic com/qh/blelight/MusicActivity.red : I
    //   80: istore #4
    //   82: iload #4
    //   84: i2d
    //   85: dstore #7
    //   87: dload #7
    //   89: invokestatic isNaN : (D)Z
    //   92: pop
    //   93: dload #7
    //   95: dload #5
    //   97: dmul
    //   98: d2i
    //   99: istore #4
    //   101: iload #4
    //   103: putstatic com/qh/blelight/MusicActivity.cred : I
    //   106: getstatic com/qh/blelight/MusicActivity.green : I
    //   109: istore #4
    //   111: iload #4
    //   113: i2d
    //   114: dstore #7
    //   116: dload #7
    //   118: invokestatic isNaN : (D)Z
    //   121: pop
    //   122: dload #7
    //   124: dload #5
    //   126: dmul
    //   127: d2i
    //   128: istore #4
    //   130: iload #4
    //   132: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   135: getstatic com/qh/blelight/MusicActivity.blue : I
    //   138: istore #4
    //   140: iload #4
    //   142: i2d
    //   143: dstore #7
    //   145: dload #7
    //   147: invokestatic isNaN : (D)Z
    //   150: pop
    //   151: dload #5
    //   153: dload #7
    //   155: dmul
    //   156: d2i
    //   157: istore #4
    //   159: iload #4
    //   161: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   164: getstatic com/qh/blelight/MusicActivity.red : I
    //   167: istore #9
    //   169: getstatic com/qh/blelight/MusicActivity.cred : I
    //   172: istore #4
    //   174: dload_2
    //   175: ldc2_w 255.0
    //   178: ddiv
    //   179: dstore #5
    //   181: getstatic com/qh/blelight/MusicActivity.red : I
    //   184: istore #10
    //   186: iload #10
    //   188: i2d
    //   189: dstore #7
    //   191: dload #7
    //   193: invokestatic isNaN : (D)Z
    //   196: pop
    //   197: dload #7
    //   199: dload #5
    //   201: dmul
    //   202: d2i
    //   203: istore #10
    //   205: aload_0
    //   206: iload #9
    //   208: iload #4
    //   210: iload #10
    //   212: iadd
    //   213: invokespecial addcolor : (II)I
    //   216: putstatic com/qh/blelight/MusicActivity.cred : I
    //   219: getstatic com/qh/blelight/MusicActivity.green : I
    //   222: istore #9
    //   224: getstatic com/qh/blelight/MusicActivity.cgreen : I
    //   227: istore #4
    //   229: getstatic com/qh/blelight/MusicActivity.green : I
    //   232: istore #10
    //   234: iload #10
    //   236: i2d
    //   237: dstore #7
    //   239: dload #7
    //   241: invokestatic isNaN : (D)Z
    //   244: pop
    //   245: dload #7
    //   247: dload #5
    //   249: dmul
    //   250: d2i
    //   251: istore #10
    //   253: aload_0
    //   254: iload #9
    //   256: iload #4
    //   258: iload #10
    //   260: iadd
    //   261: invokespecial addcolor : (II)I
    //   264: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   267: getstatic com/qh/blelight/MusicActivity.blue : I
    //   270: istore #9
    //   272: getstatic com/qh/blelight/MusicActivity.cblue : I
    //   275: istore #4
    //   277: getstatic com/qh/blelight/MusicActivity.blue : I
    //   280: istore #10
    //   282: iload #10
    //   284: i2d
    //   285: dstore #7
    //   287: dload #7
    //   289: invokestatic isNaN : (D)Z
    //   292: pop
    //   293: dload #5
    //   295: dload #7
    //   297: dmul
    //   298: d2i
    //   299: istore #10
    //   301: aload_0
    //   302: iload #9
    //   304: iload #4
    //   306: iload #10
    //   308: iadd
    //   309: invokespecial addcolor : (II)I
    //   312: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   315: iload_1
    //   316: sipush #-3000
    //   319: if_icmple -> 619
    //   322: iload_1
    //   323: sipush #-2000
    //   326: if_icmpgt -> 619
    //   329: iload_1
    //   330: invokestatic abs : (I)I
    //   333: istore #4
    //   335: iload #4
    //   337: sipush #1500
    //   340: isub
    //   341: i2d
    //   342: dstore #5
    //   344: dload #5
    //   346: invokestatic isNaN : (D)Z
    //   349: pop
    //   350: dload #5
    //   352: ldc2_w 0.0667
    //   355: dmul
    //   356: d2i
    //   357: bipush #100
    //   359: iadd
    //   360: i2d
    //   361: dstore #5
    //   363: dload #5
    //   365: invokestatic isNaN : (D)Z
    //   368: pop
    //   369: dload #5
    //   371: ldc2_w 255.0
    //   374: ddiv
    //   375: dstore #5
    //   377: getstatic com/qh/blelight/MusicActivity.red : I
    //   380: istore #4
    //   382: iload #4
    //   384: i2d
    //   385: dstore #7
    //   387: dload #7
    //   389: invokestatic isNaN : (D)Z
    //   392: pop
    //   393: dload #7
    //   395: dload #5
    //   397: dmul
    //   398: d2i
    //   399: istore #4
    //   401: iload #4
    //   403: putstatic com/qh/blelight/MusicActivity.cred : I
    //   406: getstatic com/qh/blelight/MusicActivity.green : I
    //   409: istore #4
    //   411: iload #4
    //   413: i2d
    //   414: dstore #7
    //   416: dload #7
    //   418: invokestatic isNaN : (D)Z
    //   421: pop
    //   422: dload #7
    //   424: dload #5
    //   426: dmul
    //   427: d2i
    //   428: istore #4
    //   430: iload #4
    //   432: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   435: getstatic com/qh/blelight/MusicActivity.blue : I
    //   438: istore #4
    //   440: iload #4
    //   442: i2d
    //   443: dstore #7
    //   445: dload #7
    //   447: invokestatic isNaN : (D)Z
    //   450: pop
    //   451: dload #5
    //   453: dload #7
    //   455: dmul
    //   456: d2i
    //   457: istore #4
    //   459: iload #4
    //   461: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   464: getstatic com/qh/blelight/MusicActivity.red : I
    //   467: istore #9
    //   469: getstatic com/qh/blelight/MusicActivity.cred : I
    //   472: istore #4
    //   474: ldc2_w 1.25
    //   477: dload_2
    //   478: dmul
    //   479: ldc2_w 255.0
    //   482: ddiv
    //   483: dstore #5
    //   485: getstatic com/qh/blelight/MusicActivity.red : I
    //   488: istore #10
    //   490: iload #10
    //   492: i2d
    //   493: dstore #7
    //   495: dload #7
    //   497: invokestatic isNaN : (D)Z
    //   500: pop
    //   501: dload #7
    //   503: dload #5
    //   505: dmul
    //   506: d2i
    //   507: istore #10
    //   509: aload_0
    //   510: iload #9
    //   512: iload #4
    //   514: iload #10
    //   516: iadd
    //   517: invokespecial addcolor : (II)I
    //   520: putstatic com/qh/blelight/MusicActivity.cred : I
    //   523: getstatic com/qh/blelight/MusicActivity.green : I
    //   526: istore #4
    //   528: getstatic com/qh/blelight/MusicActivity.cgreen : I
    //   531: istore #9
    //   533: getstatic com/qh/blelight/MusicActivity.green : I
    //   536: istore #10
    //   538: iload #10
    //   540: i2d
    //   541: dstore #7
    //   543: dload #7
    //   545: invokestatic isNaN : (D)Z
    //   548: pop
    //   549: dload #7
    //   551: dload #5
    //   553: dmul
    //   554: d2i
    //   555: istore #10
    //   557: aload_0
    //   558: iload #4
    //   560: iload #9
    //   562: iload #10
    //   564: iadd
    //   565: invokespecial addcolor : (II)I
    //   568: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   571: getstatic com/qh/blelight/MusicActivity.blue : I
    //   574: istore #4
    //   576: getstatic com/qh/blelight/MusicActivity.cblue : I
    //   579: istore #9
    //   581: getstatic com/qh/blelight/MusicActivity.blue : I
    //   584: istore #10
    //   586: iload #10
    //   588: i2d
    //   589: dstore #7
    //   591: dload #7
    //   593: invokestatic isNaN : (D)Z
    //   596: pop
    //   597: dload #5
    //   599: dload #7
    //   601: dmul
    //   602: d2i
    //   603: istore #10
    //   605: aload_0
    //   606: iload #4
    //   608: iload #9
    //   610: iload #10
    //   612: iadd
    //   613: invokespecial addcolor : (II)I
    //   616: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   619: iload_1
    //   620: sipush #-2000
    //   623: if_icmple -> 919
    //   626: iload_1
    //   627: sipush #-1500
    //   630: if_icmpgt -> 919
    //   633: iload_1
    //   634: invokestatic abs : (I)I
    //   637: istore #4
    //   639: iload #4
    //   641: sipush #1500
    //   644: isub
    //   645: i2d
    //   646: dstore #5
    //   648: dload #5
    //   650: invokestatic isNaN : (D)Z
    //   653: pop
    //   654: dload #5
    //   656: ldc2_w 0.0667
    //   659: dmul
    //   660: d2i
    //   661: bipush #100
    //   663: iadd
    //   664: i2d
    //   665: dstore #5
    //   667: dload #5
    //   669: invokestatic isNaN : (D)Z
    //   672: pop
    //   673: dload #5
    //   675: ldc2_w 255.0
    //   678: ddiv
    //   679: dstore #5
    //   681: getstatic com/qh/blelight/MusicActivity.red : I
    //   684: istore #4
    //   686: iload #4
    //   688: i2d
    //   689: dstore #7
    //   691: dload #7
    //   693: invokestatic isNaN : (D)Z
    //   696: pop
    //   697: dload #7
    //   699: dload #5
    //   701: dmul
    //   702: d2i
    //   703: istore #4
    //   705: iload #4
    //   707: putstatic com/qh/blelight/MusicActivity.cred : I
    //   710: getstatic com/qh/blelight/MusicActivity.green : I
    //   713: istore #4
    //   715: iload #4
    //   717: i2d
    //   718: dstore #7
    //   720: dload #7
    //   722: invokestatic isNaN : (D)Z
    //   725: pop
    //   726: dload #7
    //   728: dload #5
    //   730: dmul
    //   731: d2i
    //   732: istore #4
    //   734: iload #4
    //   736: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   739: getstatic com/qh/blelight/MusicActivity.blue : I
    //   742: istore #4
    //   744: iload #4
    //   746: i2d
    //   747: dstore #7
    //   749: dload #7
    //   751: invokestatic isNaN : (D)Z
    //   754: pop
    //   755: dload #5
    //   757: dload #7
    //   759: dmul
    //   760: d2i
    //   761: istore #4
    //   763: iload #4
    //   765: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   768: getstatic com/qh/blelight/MusicActivity.red : I
    //   771: istore #9
    //   773: getstatic com/qh/blelight/MusicActivity.cred : I
    //   776: istore #4
    //   778: dload_2
    //   779: ldc2_w 255.0
    //   782: ddiv
    //   783: dstore #5
    //   785: getstatic com/qh/blelight/MusicActivity.red : I
    //   788: istore #10
    //   790: iload #10
    //   792: i2d
    //   793: dstore #7
    //   795: dload #7
    //   797: invokestatic isNaN : (D)Z
    //   800: pop
    //   801: dload #7
    //   803: dload #5
    //   805: dmul
    //   806: d2i
    //   807: istore #10
    //   809: aload_0
    //   810: iload #9
    //   812: iload #4
    //   814: iload #10
    //   816: iadd
    //   817: invokespecial addcolor : (II)I
    //   820: putstatic com/qh/blelight/MusicActivity.cred : I
    //   823: getstatic com/qh/blelight/MusicActivity.green : I
    //   826: istore #9
    //   828: getstatic com/qh/blelight/MusicActivity.cgreen : I
    //   831: istore #4
    //   833: getstatic com/qh/blelight/MusicActivity.green : I
    //   836: istore #10
    //   838: iload #10
    //   840: i2d
    //   841: dstore #7
    //   843: dload #7
    //   845: invokestatic isNaN : (D)Z
    //   848: pop
    //   849: dload #7
    //   851: dload #5
    //   853: dmul
    //   854: d2i
    //   855: istore #10
    //   857: aload_0
    //   858: iload #9
    //   860: iload #4
    //   862: iload #10
    //   864: iadd
    //   865: invokespecial addcolor : (II)I
    //   868: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   871: getstatic com/qh/blelight/MusicActivity.blue : I
    //   874: istore #9
    //   876: getstatic com/qh/blelight/MusicActivity.cblue : I
    //   879: istore #4
    //   881: getstatic com/qh/blelight/MusicActivity.blue : I
    //   884: istore #10
    //   886: iload #10
    //   888: i2d
    //   889: dstore #7
    //   891: dload #7
    //   893: invokestatic isNaN : (D)Z
    //   896: pop
    //   897: dload #5
    //   899: dload #7
    //   901: dmul
    //   902: d2i
    //   903: istore #10
    //   905: aload_0
    //   906: iload #9
    //   908: iload #4
    //   910: iload #10
    //   912: iadd
    //   913: invokespecial addcolor : (II)I
    //   916: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   919: iload_1
    //   920: sipush #-1500
    //   923: if_icmple -> 1223
    //   926: iload_1
    //   927: sipush #-880
    //   930: if_icmpgt -> 1223
    //   933: iload_1
    //   934: invokestatic abs : (I)I
    //   937: istore #4
    //   939: iload #4
    //   941: sipush #880
    //   944: isub
    //   945: i2d
    //   946: dstore #5
    //   948: dload #5
    //   950: invokestatic isNaN : (D)Z
    //   953: pop
    //   954: dload #5
    //   956: ldc2_w 0.07
    //   959: dmul
    //   960: d2i
    //   961: bipush #50
    //   963: iadd
    //   964: i2d
    //   965: dstore #5
    //   967: dload #5
    //   969: invokestatic isNaN : (D)Z
    //   972: pop
    //   973: dload #5
    //   975: ldc2_w 255.0
    //   978: ddiv
    //   979: dstore #5
    //   981: getstatic com/qh/blelight/MusicActivity.red : I
    //   984: istore #4
    //   986: iload #4
    //   988: i2d
    //   989: dstore #7
    //   991: dload #7
    //   993: invokestatic isNaN : (D)Z
    //   996: pop
    //   997: dload #7
    //   999: dload #5
    //   1001: dmul
    //   1002: d2i
    //   1003: istore #4
    //   1005: iload #4
    //   1007: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1010: getstatic com/qh/blelight/MusicActivity.green : I
    //   1013: istore #4
    //   1015: iload #4
    //   1017: i2d
    //   1018: dstore #7
    //   1020: dload #7
    //   1022: invokestatic isNaN : (D)Z
    //   1025: pop
    //   1026: dload #7
    //   1028: dload #5
    //   1030: dmul
    //   1031: d2i
    //   1032: istore #4
    //   1034: iload #4
    //   1036: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1039: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1042: istore #4
    //   1044: iload #4
    //   1046: i2d
    //   1047: dstore #7
    //   1049: dload #7
    //   1051: invokestatic isNaN : (D)Z
    //   1054: pop
    //   1055: dload #5
    //   1057: dload #7
    //   1059: dmul
    //   1060: d2i
    //   1061: istore #4
    //   1063: iload #4
    //   1065: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   1068: getstatic com/qh/blelight/MusicActivity.red : I
    //   1071: istore #4
    //   1073: getstatic com/qh/blelight/MusicActivity.cred : I
    //   1076: istore #9
    //   1078: dload_2
    //   1079: ldc2_w 1.5
    //   1082: dmul
    //   1083: ldc2_w 255.0
    //   1086: ddiv
    //   1087: dstore #5
    //   1089: getstatic com/qh/blelight/MusicActivity.red : I
    //   1092: istore #10
    //   1094: iload #10
    //   1096: i2d
    //   1097: dstore #7
    //   1099: dload #7
    //   1101: invokestatic isNaN : (D)Z
    //   1104: pop
    //   1105: dload #7
    //   1107: dload #5
    //   1109: dmul
    //   1110: d2i
    //   1111: istore #10
    //   1113: aload_0
    //   1114: iload #4
    //   1116: iload #9
    //   1118: iload #10
    //   1120: iadd
    //   1121: invokespecial addcolor : (II)I
    //   1124: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1127: getstatic com/qh/blelight/MusicActivity.green : I
    //   1130: istore #9
    //   1132: getstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1135: istore #4
    //   1137: getstatic com/qh/blelight/MusicActivity.green : I
    //   1140: istore #10
    //   1142: iload #10
    //   1144: i2d
    //   1145: dstore #7
    //   1147: dload #7
    //   1149: invokestatic isNaN : (D)Z
    //   1152: pop
    //   1153: dload #7
    //   1155: dload #5
    //   1157: dmul
    //   1158: d2i
    //   1159: istore #10
    //   1161: aload_0
    //   1162: iload #9
    //   1164: iload #4
    //   1166: iload #10
    //   1168: iadd
    //   1169: invokespecial addcolor : (II)I
    //   1172: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1175: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1178: istore #9
    //   1180: getstatic com/qh/blelight/MusicActivity.cblue : I
    //   1183: istore #4
    //   1185: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1188: istore #10
    //   1190: iload #10
    //   1192: i2d
    //   1193: dstore #7
    //   1195: dload #7
    //   1197: invokestatic isNaN : (D)Z
    //   1200: pop
    //   1201: dload #5
    //   1203: dload #7
    //   1205: dmul
    //   1206: d2i
    //   1207: istore #10
    //   1209: aload_0
    //   1210: iload #9
    //   1212: iload #4
    //   1214: iload #10
    //   1216: iadd
    //   1217: invokespecial addcolor : (II)I
    //   1220: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   1223: iload_1
    //   1224: sipush #-880
    //   1227: if_icmple -> 1533
    //   1230: iload_1
    //   1231: sipush #-800
    //   1234: if_icmpgt -> 1533
    //   1237: iload_1
    //   1238: invokestatic abs : (I)I
    //   1241: istore #4
    //   1243: iload #4
    //   1245: sipush #800
    //   1248: isub
    //   1249: i2d
    //   1250: dstore #5
    //   1252: dload #5
    //   1254: invokestatic isNaN : (D)Z
    //   1257: pop
    //   1258: dload #5
    //   1260: ldc2_w 0.9325
    //   1263: dmul
    //   1264: d2i
    //   1265: bipush #120
    //   1267: iadd
    //   1268: i2d
    //   1269: dstore #5
    //   1271: dload #5
    //   1273: invokestatic isNaN : (D)Z
    //   1276: pop
    //   1277: dload #5
    //   1279: ldc2_w 255.0
    //   1282: ddiv
    //   1283: dstore #5
    //   1285: getstatic com/qh/blelight/MusicActivity.red : I
    //   1288: istore #4
    //   1290: iload #4
    //   1292: i2d
    //   1293: dstore #7
    //   1295: dload #7
    //   1297: invokestatic isNaN : (D)Z
    //   1300: pop
    //   1301: dload #7
    //   1303: dload #5
    //   1305: dmul
    //   1306: d2i
    //   1307: istore #4
    //   1309: iload #4
    //   1311: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1314: getstatic com/qh/blelight/MusicActivity.green : I
    //   1317: istore #4
    //   1319: iload #4
    //   1321: i2d
    //   1322: dstore #7
    //   1324: dload #7
    //   1326: invokestatic isNaN : (D)Z
    //   1329: pop
    //   1330: dload #7
    //   1332: dload #5
    //   1334: dmul
    //   1335: d2i
    //   1336: istore #4
    //   1338: iload #4
    //   1340: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1343: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1346: istore #4
    //   1348: iload #4
    //   1350: i2d
    //   1351: dstore #7
    //   1353: dload #7
    //   1355: invokestatic isNaN : (D)Z
    //   1358: pop
    //   1359: dload #5
    //   1361: dload #7
    //   1363: dmul
    //   1364: d2i
    //   1365: istore #4
    //   1367: iload #4
    //   1369: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   1372: getstatic com/qh/blelight/MusicActivity.red : I
    //   1375: istore #9
    //   1377: getstatic com/qh/blelight/MusicActivity.cred : I
    //   1380: istore #4
    //   1382: dload_2
    //   1383: ldc2_w 1.5
    //   1386: dmul
    //   1387: ldc2_w 255.0
    //   1390: ddiv
    //   1391: dstore #5
    //   1393: getstatic com/qh/blelight/MusicActivity.red : I
    //   1396: istore #10
    //   1398: iload #10
    //   1400: i2d
    //   1401: dstore #7
    //   1403: dload #7
    //   1405: invokestatic isNaN : (D)Z
    //   1408: pop
    //   1409: dload #7
    //   1411: dload #5
    //   1413: dmul
    //   1414: d2i
    //   1415: istore #10
    //   1417: aload_0
    //   1418: iload #9
    //   1420: iconst_2
    //   1421: iadd
    //   1422: iload #4
    //   1424: iload #10
    //   1426: iadd
    //   1427: invokespecial addcolor : (II)I
    //   1430: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1433: getstatic com/qh/blelight/MusicActivity.green : I
    //   1436: istore #9
    //   1438: getstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1441: istore #4
    //   1443: getstatic com/qh/blelight/MusicActivity.green : I
    //   1446: istore #10
    //   1448: iload #10
    //   1450: i2d
    //   1451: dstore #7
    //   1453: dload #7
    //   1455: invokestatic isNaN : (D)Z
    //   1458: pop
    //   1459: dload #7
    //   1461: dload #5
    //   1463: dmul
    //   1464: d2i
    //   1465: istore #10
    //   1467: aload_0
    //   1468: iload #9
    //   1470: iconst_2
    //   1471: iadd
    //   1472: iload #4
    //   1474: iload #10
    //   1476: iadd
    //   1477: invokespecial addcolor : (II)I
    //   1480: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1483: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1486: istore #9
    //   1488: getstatic com/qh/blelight/MusicActivity.cblue : I
    //   1491: istore #4
    //   1493: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1496: istore #10
    //   1498: iload #10
    //   1500: i2d
    //   1501: dstore #7
    //   1503: dload #7
    //   1505: invokestatic isNaN : (D)Z
    //   1508: pop
    //   1509: dload #5
    //   1511: dload #7
    //   1513: dmul
    //   1514: d2i
    //   1515: istore #10
    //   1517: aload_0
    //   1518: iload #9
    //   1520: iconst_2
    //   1521: iadd
    //   1522: iload #4
    //   1524: iload #10
    //   1526: iadd
    //   1527: invokespecial addcolor : (II)I
    //   1530: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   1533: iload_1
    //   1534: sipush #-800
    //   1537: if_icmple -> 1843
    //   1540: iload_1
    //   1541: sipush #-700
    //   1544: if_icmpgt -> 1843
    //   1547: iload_1
    //   1548: invokestatic abs : (I)I
    //   1551: istore #4
    //   1553: iload #4
    //   1555: sipush #700
    //   1558: isub
    //   1559: i2d
    //   1560: dstore #5
    //   1562: dload #5
    //   1564: invokestatic isNaN : (D)Z
    //   1567: pop
    //   1568: dload #5
    //   1570: ldc2_w 1.5
    //   1573: dmul
    //   1574: d2i
    //   1575: bipush #100
    //   1577: iadd
    //   1578: i2d
    //   1579: dstore #5
    //   1581: dload #5
    //   1583: invokestatic isNaN : (D)Z
    //   1586: pop
    //   1587: dload #5
    //   1589: ldc2_w 255.0
    //   1592: ddiv
    //   1593: dstore #5
    //   1595: getstatic com/qh/blelight/MusicActivity.red : I
    //   1598: istore #4
    //   1600: iload #4
    //   1602: i2d
    //   1603: dstore #7
    //   1605: dload #7
    //   1607: invokestatic isNaN : (D)Z
    //   1610: pop
    //   1611: dload #7
    //   1613: dload #5
    //   1615: dmul
    //   1616: d2i
    //   1617: istore #4
    //   1619: iload #4
    //   1621: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1624: getstatic com/qh/blelight/MusicActivity.green : I
    //   1627: istore #4
    //   1629: iload #4
    //   1631: i2d
    //   1632: dstore #7
    //   1634: dload #7
    //   1636: invokestatic isNaN : (D)Z
    //   1639: pop
    //   1640: dload #7
    //   1642: dload #5
    //   1644: dmul
    //   1645: d2i
    //   1646: istore #4
    //   1648: iload #4
    //   1650: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1653: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1656: istore #4
    //   1658: iload #4
    //   1660: i2d
    //   1661: dstore #7
    //   1663: dload #7
    //   1665: invokestatic isNaN : (D)Z
    //   1668: pop
    //   1669: dload #5
    //   1671: dload #7
    //   1673: dmul
    //   1674: d2i
    //   1675: istore #4
    //   1677: iload #4
    //   1679: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   1682: getstatic com/qh/blelight/MusicActivity.red : I
    //   1685: istore #4
    //   1687: getstatic com/qh/blelight/MusicActivity.cred : I
    //   1690: istore #9
    //   1692: ldc2_w 1.5
    //   1695: dload_2
    //   1696: dmul
    //   1697: ldc2_w 255.0
    //   1700: ddiv
    //   1701: dstore #5
    //   1703: getstatic com/qh/blelight/MusicActivity.red : I
    //   1706: istore #10
    //   1708: iload #10
    //   1710: i2d
    //   1711: dstore #7
    //   1713: dload #7
    //   1715: invokestatic isNaN : (D)Z
    //   1718: pop
    //   1719: dload #7
    //   1721: dload #5
    //   1723: dmul
    //   1724: d2i
    //   1725: istore #10
    //   1727: aload_0
    //   1728: iload #4
    //   1730: iconst_5
    //   1731: iadd
    //   1732: iload #9
    //   1734: iload #10
    //   1736: iadd
    //   1737: invokespecial addcolor : (II)I
    //   1740: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1743: getstatic com/qh/blelight/MusicActivity.green : I
    //   1746: istore #4
    //   1748: getstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1751: istore #9
    //   1753: getstatic com/qh/blelight/MusicActivity.green : I
    //   1756: istore #10
    //   1758: iload #10
    //   1760: i2d
    //   1761: dstore #7
    //   1763: dload #7
    //   1765: invokestatic isNaN : (D)Z
    //   1768: pop
    //   1769: dload #7
    //   1771: dload #5
    //   1773: dmul
    //   1774: d2i
    //   1775: istore #10
    //   1777: aload_0
    //   1778: iload #4
    //   1780: iconst_5
    //   1781: iadd
    //   1782: iload #9
    //   1784: iload #10
    //   1786: iadd
    //   1787: invokespecial addcolor : (II)I
    //   1790: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1793: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1796: istore #9
    //   1798: getstatic com/qh/blelight/MusicActivity.cblue : I
    //   1801: istore #4
    //   1803: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1806: istore #10
    //   1808: iload #10
    //   1810: i2d
    //   1811: dstore #7
    //   1813: dload #7
    //   1815: invokestatic isNaN : (D)Z
    //   1818: pop
    //   1819: dload #5
    //   1821: dload #7
    //   1823: dmul
    //   1824: d2i
    //   1825: istore #10
    //   1827: aload_0
    //   1828: iload #9
    //   1830: iconst_5
    //   1831: iadd
    //   1832: iload #4
    //   1834: iload #10
    //   1836: iadd
    //   1837: invokespecial addcolor : (II)I
    //   1840: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   1843: iload_1
    //   1844: sipush #-700
    //   1847: if_icmple -> 2078
    //   1850: getstatic com/qh/blelight/MusicActivity.red : I
    //   1853: istore_1
    //   1854: iload_1
    //   1855: i2d
    //   1856: dstore #5
    //   1858: dload #5
    //   1860: invokestatic isNaN : (D)Z
    //   1863: pop
    //   1864: dload #5
    //   1866: ldc2_w 1.53
    //   1869: dmul
    //   1870: d2i
    //   1871: istore_1
    //   1872: iload_1
    //   1873: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1876: getstatic com/qh/blelight/MusicActivity.green : I
    //   1879: istore_1
    //   1880: iload_1
    //   1881: i2d
    //   1882: dstore #5
    //   1884: dload #5
    //   1886: invokestatic isNaN : (D)Z
    //   1889: pop
    //   1890: dload #5
    //   1892: ldc2_w 1.53
    //   1895: dmul
    //   1896: d2i
    //   1897: istore_1
    //   1898: iload_1
    //   1899: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1902: getstatic com/qh/blelight/MusicActivity.blue : I
    //   1905: istore_1
    //   1906: iload_1
    //   1907: i2d
    //   1908: dstore #5
    //   1910: dload #5
    //   1912: invokestatic isNaN : (D)Z
    //   1915: pop
    //   1916: dload #5
    //   1918: ldc2_w 1.53
    //   1921: dmul
    //   1922: d2i
    //   1923: istore_1
    //   1924: iload_1
    //   1925: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   1928: getstatic com/qh/blelight/MusicActivity.red : I
    //   1931: istore #4
    //   1933: getstatic com/qh/blelight/MusicActivity.cred : I
    //   1936: istore_1
    //   1937: dload_2
    //   1938: ldc2_w 255.0
    //   1941: ddiv
    //   1942: dstore_2
    //   1943: getstatic com/qh/blelight/MusicActivity.red : I
    //   1946: istore #9
    //   1948: iload #9
    //   1950: i2d
    //   1951: dstore #5
    //   1953: dload #5
    //   1955: invokestatic isNaN : (D)Z
    //   1958: pop
    //   1959: dload #5
    //   1961: dload_2
    //   1962: dmul
    //   1963: d2i
    //   1964: istore #9
    //   1966: aload_0
    //   1967: iload #4
    //   1969: bipush #10
    //   1971: iadd
    //   1972: iload_1
    //   1973: iload #9
    //   1975: iadd
    //   1976: invokespecial addcolor : (II)I
    //   1979: putstatic com/qh/blelight/MusicActivity.cred : I
    //   1982: getstatic com/qh/blelight/MusicActivity.green : I
    //   1985: istore #4
    //   1987: getstatic com/qh/blelight/MusicActivity.cgreen : I
    //   1990: istore_1
    //   1991: getstatic com/qh/blelight/MusicActivity.green : I
    //   1994: istore #9
    //   1996: iload #9
    //   1998: i2d
    //   1999: dstore #5
    //   2001: dload #5
    //   2003: invokestatic isNaN : (D)Z
    //   2006: pop
    //   2007: dload #5
    //   2009: dload_2
    //   2010: dmul
    //   2011: d2i
    //   2012: istore #9
    //   2014: aload_0
    //   2015: iload #4
    //   2017: bipush #10
    //   2019: iadd
    //   2020: iload_1
    //   2021: iload #9
    //   2023: iadd
    //   2024: invokespecial addcolor : (II)I
    //   2027: putstatic com/qh/blelight/MusicActivity.cgreen : I
    //   2030: getstatic com/qh/blelight/MusicActivity.blue : I
    //   2033: istore #4
    //   2035: getstatic com/qh/blelight/MusicActivity.cblue : I
    //   2038: istore_1
    //   2039: getstatic com/qh/blelight/MusicActivity.blue : I
    //   2042: istore #9
    //   2044: iload #9
    //   2046: i2d
    //   2047: dstore #5
    //   2049: dload #5
    //   2051: invokestatic isNaN : (D)Z
    //   2054: pop
    //   2055: dload_2
    //   2056: dload #5
    //   2058: dmul
    //   2059: d2i
    //   2060: istore #9
    //   2062: aload_0
    //   2063: iload #4
    //   2065: bipush #10
    //   2067: iadd
    //   2068: iload_1
    //   2069: iload #9
    //   2071: iadd
    //   2072: invokespecial addcolor : (II)I
    //   2075: putstatic com/qh/blelight/MusicActivity.cblue : I
    //   2078: aload_0
    //   2079: monitorexit
    //   2080: return
    //   2081: astore #11
    //   2083: aload_0
    //   2084: monitorexit
    //   2085: aload #11
    //   2087: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	2081	finally
    //   32	38	2081	finally
    //   77	82	2081	finally
    //   101	111	2081	finally
    //   130	140	2081	finally
    //   159	174	2081	finally
    //   181	186	2081	finally
    //   205	234	2081	finally
    //   253	282	2081	finally
    //   301	315	2081	finally
    //   329	335	2081	finally
    //   377	382	2081	finally
    //   401	411	2081	finally
    //   430	440	2081	finally
    //   459	474	2081	finally
    //   485	490	2081	finally
    //   509	538	2081	finally
    //   557	586	2081	finally
    //   605	619	2081	finally
    //   633	639	2081	finally
    //   681	686	2081	finally
    //   705	715	2081	finally
    //   734	744	2081	finally
    //   763	778	2081	finally
    //   785	790	2081	finally
    //   809	838	2081	finally
    //   857	886	2081	finally
    //   905	919	2081	finally
    //   933	939	2081	finally
    //   981	986	2081	finally
    //   1005	1015	2081	finally
    //   1034	1044	2081	finally
    //   1063	1078	2081	finally
    //   1089	1094	2081	finally
    //   1113	1142	2081	finally
    //   1161	1190	2081	finally
    //   1209	1223	2081	finally
    //   1237	1243	2081	finally
    //   1285	1290	2081	finally
    //   1309	1319	2081	finally
    //   1338	1348	2081	finally
    //   1367	1382	2081	finally
    //   1393	1398	2081	finally
    //   1417	1448	2081	finally
    //   1467	1498	2081	finally
    //   1517	1533	2081	finally
    //   1547	1553	2081	finally
    //   1595	1600	2081	finally
    //   1619	1629	2081	finally
    //   1648	1658	2081	finally
    //   1677	1692	2081	finally
    //   1703	1708	2081	finally
    //   1727	1758	2081	finally
    //   1777	1808	2081	finally
    //   1827	1843	2081	finally
    //   1850	1854	2081	finally
    //   1872	1880	2081	finally
    //   1898	1906	2081	finally
    //   1924	1937	2081	finally
    //   1943	1948	2081	finally
    //   1966	1996	2081	finally
    //   2014	2044	2081	finally
    //   2062	2078	2081	finally
  }
  
  private void setupEqualizeFxAndUi() {
    int i = this.myMediaPlayer.getAudioSessionId();
    short s1 = 0;
    this.mEqualizer = new Equalizer(0, i);
    this.mEqualizer.setEnabled(true);
    i = this.mEqualizer.getNumberOfBands();
    short s = this.mEqualizer.getBandLevelRange()[0];
    s = this.mEqualizer.getBandLevelRange()[1];
    short s2;
    for (s2 = s1; s2 < i; s2 = s1) {
      this.mEqualizer.getCenterFreq(s2);
      this.mEqualizer.setBandLevel(s2, (short)-15);
      s1 = (short)(s2 + 1);
    } 
  }
  
  public void getMusicInfo() {
    this.musicInfos.clear();
    MusicInfo musicInfo = new MusicInfo();
    musicInfo.setSongName("Alan Walker - Faded");
    musicInfo.setArtist("Alan Walker");
    musicInfo.setAlbum("Faded");
    musicInfo.setDisplay_name("Alan Walker - Faded");
    musicInfo.setAudioPath("");
    musicInfo.setPlayTime(212000L);
    this.musicInfos.add(musicInfo);
    musicInfo = new MusicInfo();
    musicInfo.setSongName("Alan Walker - Play");
    musicInfo.setArtist("K-391&Alan Walker&Martin Tungevaag&Mangoo");
    musicInfo.setAlbum("Play");
    musicInfo.setDisplay_name("Alan Walker - Play");
    musicInfo.setAudioPath("");
    musicInfo.setPlayTime(168000L);
    this.musicInfos.add(musicInfo);
    musicInfo = new MusicInfo();
    musicInfo.setSongName("Ava Max - Salt");
    musicInfo.setArtist("Ava Max");
    musicInfo.setAlbum("Salt");
    musicInfo.setDisplay_name("Ava Max - Salt");
    musicInfo.setAudioPath("");
    musicInfo.setPlayTime(180000L);
    this.musicInfos.add(musicInfo);
    Cursor cursor = MusicData.getMP3MusicInfo(this.context, this.path.toString());
    if (cursor.moveToFirst())
      do {
        musicInfo = new MusicInfo();
        String str1 = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        String str2 = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String str3 = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));
        String str4 = cursor.getString(cursor.getColumnIndexOrThrow("artist"));
        String str5 = cursor.getString(cursor.getColumnIndexOrThrow("album"));
        long l = cursor.getLong(cursor.getColumnIndexOrThrow("duration"));
        if (l < 40000L)
          continue; 
        musicInfo.setSongName(str2);
        musicInfo.setArtist(str4);
        musicInfo.setAlbum(str5);
        musicInfo.setDisplay_name(str3);
        musicInfo.setAudioPath(str1);
        musicInfo.setPlayTime(l);
        this.musicInfos.add(musicInfo);
      } while (cursor.moveToNext()); 
  }
  
  public void loadInBackground() {
    this.musicInfos.clear();
    MusicInfo musicInfo = new MusicInfo();
    musicInfo.setSongName("Alan Walker - Faded");
    musicInfo.setArtist("Alan Walker");
    musicInfo.setAlbum("Faded");
    musicInfo.setDisplay_name("Alan Walker - Faded");
    musicInfo.setAudioPath("");
    musicInfo.setPlayTime(212000L);
    this.musicInfos.add(musicInfo);
    musicInfo = new MusicInfo();
    musicInfo.setSongName("Alan Walker - Play");
    musicInfo.setArtist("K-391&Alan Walker&Martin Tungevaag&Mangoo");
    musicInfo.setAlbum("Play");
    musicInfo.setDisplay_name("Alan Walker - Play");
    musicInfo.setAudioPath("");
    musicInfo.setPlayTime(168000L);
    this.musicInfos.add(musicInfo);
    musicInfo = new MusicInfo();
    musicInfo.setSongName("Ava Max - Salt");
    musicInfo.setArtist("Ava Max");
    musicInfo.setAlbum("Salt");
    musicInfo.setDisplay_name("Ava Max - Salt");
    musicInfo.setAudioPath("");
    musicInfo.setPlayTime(180000L);
    this.musicInfos.add(musicInfo);
    Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] { "title", "album", "album_id", "artist", "duration", "_data" }, "is_music=1", null, "LOWER(artist) ASC, LOWER(album) ASC, LOWER(title) ASC");
    if (cursor == null)
      return; 
    try {
      if (cursor.moveToFirst()) {
        boolean bool;
        int i = cursor.getColumnIndex("title");
        int j = cursor.getColumnIndex("album");
        int k = cursor.getColumnIndex("artist");
        int m = cursor.getColumnIndex("duration");
        cursor.getColumnIndex("album_id");
        cursor.getColumnIndex("_display_name");
        int n = cursor.getColumnIndex("_data");
        do {
          MusicInfo musicInfo1 = new MusicInfo();
          this();
          musicInfo1.setSongName(cursor.getString(i));
          musicInfo1.setArtist(cursor.getString(k));
          musicInfo1.setAlbum(cursor.getString(j));
          musicInfo1.setDisplay_name("unknown");
          musicInfo1.setAudioPath(Uri.parse(cursor.getString(n)).toString());
          musicInfo1.setPlayTime(cursor.getLong(m));
          if (cursor.getLong(m) > 0L)
            this.musicInfos.add(musicInfo1); 
          bool = cursor.moveToNext();
        } while (bool);
      } 
      return;
    } finally {
      cursor.close();
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial onCreate : (Landroid/os/Bundle;)V
    //   5: aload_0
    //   6: ldc_w 2131361835
    //   9: invokevirtual setContentView : (I)V
    //   12: aload_0
    //   13: aload_0
    //   14: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   17: putfield mResources : Landroid/content/res/Resources;
    //   20: aload_0
    //   21: aload_0
    //   22: putfield context : Landroid/content/Context;
    //   25: invokestatic getExternalStorageState : ()Ljava/lang/String;
    //   28: ldc_w 'mounted'
    //   31: invokevirtual equals : (Ljava/lang/Object;)Z
    //   34: ifeq -> 47
    //   37: aload_0
    //   38: invokestatic getExternalStorageDirectory : ()Ljava/io/File;
    //   41: putfield path : Ljava/io/File;
    //   44: goto -> 58
    //   47: aload_0
    //   48: ldc_w 'SD card Error !'
    //   51: iconst_1
    //   52: invokestatic makeText : (Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   55: invokevirtual show : ()V
    //   58: aload_0
    //   59: aload_0
    //   60: ldc_w 'BleLight'
    //   63: iconst_0
    //   64: invokevirtual getSharedPreferences : (Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   67: putfield settings : Landroid/content/SharedPreferences;
    //   70: ldc_w 'HUAWEI'
    //   73: getstatic android/os/Build.MANUFACTURER : Ljava/lang/String;
    //   76: invokevirtual equals : (Ljava/lang/Object;)Z
    //   79: ifeq -> 102
    //   82: bipush #65
    //   84: putstatic com/qh/blelight/MusicActivity.HEIGHT : I
    //   87: bipush #85
    //   89: putstatic com/qh/blelight/MusicActivity.HEIGHT_FREQUENCY : I
    //   92: ldc_w 'HUAWEI P7-L09'
    //   95: getstatic android/os/Build.MODEL : Ljava/lang/String;
    //   98: invokevirtual equals : (Ljava/lang/Object;)Z
    //   101: pop
    //   102: ldc_w 'LGE'
    //   105: getstatic android/os/Build.MANUFACTURER : Ljava/lang/String;
    //   108: invokevirtual equals : (Ljava/lang/Object;)Z
    //   111: ifeq -> 124
    //   114: bipush #115
    //   116: putstatic com/qh/blelight/MusicActivity.HEIGHT : I
    //   119: bipush #85
    //   121: putstatic com/qh/blelight/MusicActivity.HEIGHT_FREQUENCY : I
    //   124: getstatic com/qh/blelight/AdjustActivity.mainW : I
    //   127: sipush #710
    //   130: if_icmpge -> 143
    //   133: bipush #45
    //   135: putstatic com/qh/blelight/MusicActivity.HEIGHT : I
    //   138: bipush #85
    //   140: putstatic com/qh/blelight/MusicActivity.HEIGHT_FREQUENCY : I
    //   143: aload_0
    //   144: aload_0
    //   145: getfield context : Landroid/content/Context;
    //   148: invokevirtual getAssets : ()Landroid/content/res/AssetManager;
    //   151: putfield mAssetManager : Landroid/content/res/AssetManager;
    //   154: aload_0
    //   155: aload_0
    //   156: getfield context : Landroid/content/Context;
    //   159: ldc_w 'audio'
    //   162: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   165: checkcast android/media/AudioManager
    //   168: putfield audioManager : Landroid/media/AudioManager;
    //   171: aload_0
    //   172: new android/media/MediaPlayer
    //   175: dup
    //   176: invokespecial <init> : ()V
    //   179: putfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   182: aload_0
    //   183: aload_0
    //   184: invokevirtual getApplication : ()Landroid/app/Application;
    //   187: checkcast com/qh/blelight/MyApplication
    //   190: putfield mMyApplication : Lcom/qh/blelight/MyApplication;
    //   193: aload_0
    //   194: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
    //   197: aload_0
    //   198: getfield musicHandler : Landroid/os/Handler;
    //   201: putfield MusicHandler : Landroid/os/Handler;
    //   204: aload_0
    //   205: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
    //   208: aload_0
    //   209: getfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   212: putfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   215: aload_0
    //   216: aload_0
    //   217: ldc_w 2131231014
    //   220: invokevirtual findViewById : (I)Landroid/view/View;
    //   223: checkcast android/widget/ListView
    //   226: putfield mListView : Landroid/widget/ListView;
    //   229: aload_0
    //   230: invokevirtual loadInBackground : ()V
    //   233: aload_0
    //   234: getfield musicInfos : Ljava/util/ArrayList;
    //   237: ifnull -> 265
    //   240: aload_0
    //   241: getfield musicInfos : Ljava/util/ArrayList;
    //   244: invokevirtual size : ()I
    //   247: ifle -> 265
    //   250: aload_0
    //   251: getfield musicInfos : Ljava/util/ArrayList;
    //   254: iconst_0
    //   255: invokevirtual get : (I)Ljava/lang/Object;
    //   258: checkcast com/qh/blelight/MusicInfo
    //   261: iconst_1
    //   262: invokevirtual setTag : (Z)V
    //   265: aload_0
    //   266: new com/qh/blelight/MusicActivity$MusicInfoAdapter
    //   269: dup
    //   270: aload_0
    //   271: aload_0
    //   272: getfield context : Landroid/content/Context;
    //   275: aload_0
    //   276: getfield musicInfos : Ljava/util/ArrayList;
    //   279: invokespecial <init> : (Lcom/qh/blelight/MusicActivity;Landroid/content/Context;Ljava/util/List;)V
    //   282: putfield mMusicInfoAdapter : Lcom/qh/blelight/MusicActivity$MusicInfoAdapter;
    //   285: aload_0
    //   286: getfield mListView : Landroid/widget/ListView;
    //   289: aload_0
    //   290: getfield mMusicInfoAdapter : Lcom/qh/blelight/MusicActivity$MusicInfoAdapter;
    //   293: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
    //   296: aload_0
    //   297: getfield mListView : Landroid/widget/ListView;
    //   300: aload_0
    //   301: getfield myOnItemClickListener : Landroid/widget/AdapterView$OnItemClickListener;
    //   304: invokevirtual setOnItemClickListener : (Landroid/widget/AdapterView$OnItemClickListener;)V
    //   307: aload_0
    //   308: invokespecial init : ()V
    //   311: aload_0
    //   312: getfield musicInfos : Ljava/util/ArrayList;
    //   315: invokevirtual size : ()I
    //   318: ifle -> 542
    //   321: aload_0
    //   322: getfield musicInfos : Ljava/util/ArrayList;
    //   325: iconst_0
    //   326: invokevirtual get : (I)Ljava/lang/Object;
    //   329: checkcast com/qh/blelight/MusicInfo
    //   332: astore_2
    //   333: aload_2
    //   334: ifnull -> 542
    //   337: aload_2
    //   338: invokevirtual getSongName : ()Ljava/lang/String;
    //   341: astore_3
    //   342: aload_3
    //   343: ifnull -> 358
    //   346: aload_3
    //   347: astore_1
    //   348: ldc_w ''
    //   351: aload_3
    //   352: invokevirtual equals : (Ljava/lang/Object;)Z
    //   355: ifeq -> 394
    //   358: aload_2
    //   359: invokevirtual getDisplay_name : ()Ljava/lang/String;
    //   362: astore_1
    //   363: aload_1
    //   364: ifnull -> 390
    //   367: aload_1
    //   368: invokevirtual length : ()I
    //   371: iconst_4
    //   372: if_icmple -> 390
    //   375: aload_1
    //   376: iconst_0
    //   377: aload_1
    //   378: invokevirtual length : ()I
    //   381: iconst_4
    //   382: isub
    //   383: invokevirtual substring : (II)Ljava/lang/String;
    //   386: astore_1
    //   387: goto -> 394
    //   390: ldc_w 'other'
    //   393: astore_1
    //   394: aload_0
    //   395: getfield tx_music_name : Landroid/widget/TextView;
    //   398: aload_1
    //   399: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   402: aload_0
    //   403: getfield tx_album : Landroid/widget/TextView;
    //   406: astore_1
    //   407: new java/lang/StringBuilder
    //   410: dup
    //   411: invokespecial <init> : ()V
    //   414: astore_3
    //   415: aload_3
    //   416: ldc_w ''
    //   419: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   422: pop
    //   423: aload_3
    //   424: aload_2
    //   425: invokevirtual getAlbum : ()Ljava/lang/String;
    //   428: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   431: pop
    //   432: aload_1
    //   433: aload_3
    //   434: invokevirtual toString : ()Ljava/lang/String;
    //   437: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   440: aload_0
    //   441: getfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   444: invokevirtual getDuration : ()I
    //   447: sipush #1000
    //   450: idiv
    //   451: i2l
    //   452: lstore #4
    //   454: aload_0
    //   455: getfield mTimerFormat : Ljava/lang/String;
    //   458: iconst_2
    //   459: anewarray java/lang/Object
    //   462: dup
    //   463: iconst_0
    //   464: lload #4
    //   466: ldc2_w 60
    //   469: ldiv
    //   470: invokestatic valueOf : (J)Ljava/lang/Long;
    //   473: aastore
    //   474: dup
    //   475: iconst_1
    //   476: lload #4
    //   478: ldc2_w 60
    //   481: lrem
    //   482: invokestatic valueOf : (J)Ljava/lang/Long;
    //   485: aastore
    //   486: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   489: pop
    //   490: aload_0
    //   491: getfield tx_endtime : Landroid/widget/TextView;
    //   494: ldc_w '00:00'
    //   497: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   500: aload_0
    //   501: getfield tx_statrtime : Landroid/widget/TextView;
    //   504: ldc_w '00:00'
    //   507: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   510: aload_0
    //   511: iconst_0
    //   512: putfield playingId : I
    //   515: aload_0
    //   516: invokevirtual playMusic : ()V
    //   519: aload_0
    //   520: getfield mHandler : Landroid/os/Handler;
    //   523: new com/qh/blelight/MusicActivity$2
    //   526: dup
    //   527: aload_0
    //   528: invokespecial <init> : (Lcom/qh/blelight/MusicActivity;)V
    //   531: ldc2_w 200
    //   534: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
    //   537: pop
    //   538: aload_0
    //   539: invokevirtual updataUI : ()V
    //   542: aload_0
    //   543: invokespecial initVisualizer : ()V
    //   546: aload_0
    //   547: invokespecial setupEqualizeFxAndUi : ()V
    //   550: aload_0
    //   551: getfield mVisualizer : Landroid/media/audiofx/Visualizer;
    //   554: iconst_1
    //   555: invokevirtual setEnabled : (Z)I
    //   558: pop
    //   559: aload_0
    //   560: getfield seekbar_play : Landroid/widget/SeekBar;
    //   563: new com/qh/blelight/MusicActivity$3
    //   566: dup
    //   567: aload_0
    //   568: invokespecial <init> : (Lcom/qh/blelight/MusicActivity;)V
    //   571: invokevirtual setOnSeekBarChangeListener : (Landroid/widget/SeekBar$OnSeekBarChangeListener;)V
    //   574: return
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    Intent intent;
    if (4 == paramInt) {
      intent = new Intent("android.intent.action.MAIN");
      intent.setFlags(268435456);
      intent.addCategory("android.intent.category.HOME");
      startActivity(intent);
      return true;
    } 
    return super.onKeyDown(paramInt, (KeyEvent)intent);
  }
  
  protected void onResume() {
    this.ispauseMusic = false;
    if (this.isReplay) {
      this.isReplay = false;
      if (!this.myMediaPlayer.isPlaying())
        this.myMediaPlayer.start(); 
    } 
    if (this.mMyApplication != null)
      if (this.mMyApplication.isOpenMusicHop()) {
        this.img_hop.setImageResource(2131165389);
      } else {
        this.img_hop.setImageResource(2131165390);
      }  
    super.onResume();
  }
  
  public void playMusic() {
    if (this.myMediaPlayer == null) {
      Log.e("", "myMediaPlayer == null");
      return;
    } 
    this.myMediaPlayer.reset();
    try {
      if (this.playingId == -1 || this.playingId <= 2) {
        String str = "Alan Walker - Faded.mp3";
        try {
          switch (this.playingId) {
            case 2:
              str = "Ava Max - Salt.mp3";
              break;
            case 1:
              str = "Alan Walker - Play.mp3";
              break;
            case 0:
              str = "Alan Walker - Faded.mp3";
              break;
          } 
          AssetFileDescriptor assetFileDescriptor = this.mAssetManager.openFd(str);
          FileInputStream fileInputStream1 = assetFileDescriptor.createInputStream();
          fileInputStream1.getFD();
          fileInputStream1.getChannel();
          Log.e("setDataSource", "setDataSource 8888");
          this.myMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
          this.myMediaPlayer.prepare();
          Handler handler1 = this.mHandler;
          Runnable runnable1 = new Runnable() {
              public void run() {
                MusicActivity.this.myMediaPlayer.start();
              }
            };
          super(this);
          handler1.postDelayed(runnable1, 100L);
          fileInputStream1.close();
        } catch (Exception exception) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("Exception ");
          stringBuilder.append(exception.getMessage());
          Log.e("Exception", stringBuilder.toString());
        } 
        return;
      } 
      if (this.playingId == this.musicInfos.size())
        this.playingId = 0; 
      FileInputStream fileInputStream = new FileInputStream();
      this(((MusicInfo)this.musicInfos.get(this.playingId)).getAudioPath());
      FileDescriptor fileDescriptor = fileInputStream.getFD();
      FileChannel fileChannel = fileInputStream.getChannel();
      this.myMediaPlayer.setDataSource(fileDescriptor, 0L, fileChannel.size());
      this.myMediaPlayer.prepare();
      Handler handler = this.mHandler;
      Runnable runnable = new Runnable() {
          public void run() {
            MusicActivity.this.myMediaPlayer.start();
          }
        };
      super(this);
      handler.postDelayed(runnable, 100L);
      fileInputStream.close();
    } catch (IllegalArgumentException illegalArgumentException) {
      illegalArgumentException.printStackTrace();
    } catch (SecurityException securityException) {
      securityException.printStackTrace();
    } catch (IllegalStateException illegalStateException) {
      illegalStateException.printStackTrace();
    } catch (IOException iOException) {
      iOException.printStackTrace();
      Log.e("777", "8888");
      try {
        FileInputStream fileInputStream = new FileInputStream();
        this(((MusicInfo)this.musicInfos.get(this.playingId)).getAudioPath());
        FileDescriptor fileDescriptor = fileInputStream.getFD();
        FileChannel fileChannel = fileInputStream.getChannel();
        this.myMediaPlayer.setDataSource(fileDescriptor, 0L, fileChannel.size());
        this.myMediaPlayer.prepare();
        Handler handler = this.mHandler;
        Runnable runnable = new Runnable() {
            public void run() {
              MusicActivity.this.myMediaPlayer.start();
            }
          };
        super(this);
        handler.postDelayed(runnable, 100L);
        fileInputStream.close();
      } catch (Exception exception) {}
    } 
    this.mMusicInfoAdapter.setItem(this.playingId);
  }
  
  public void setVisualizerEnabled(boolean paramBoolean) {
    if (this.mMyApplication.isOpenVisualizer && this.mVisualizer.getEnabled() != paramBoolean)
      this.mVisualizer.setEnabled(paramBoolean); 
  }
  
  public void updataUI() {
    // Byte code:
    //   0: aload_0
    //   1: getfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   4: ifnonnull -> 18
    //   7: aload_0
    //   8: getfield tx_music_name : Landroid/widget/TextView;
    //   11: ldc_w '--------'
    //   14: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   17: return
    //   18: aload_0
    //   19: getfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   22: invokevirtual getDuration : ()I
    //   25: ldc_w 1961778602
    //   28: if_icmplt -> 32
    //   31: return
    //   32: aload_0
    //   33: getfield seekbar_play : Landroid/widget/SeekBar;
    //   36: aload_0
    //   37: getfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   40: invokevirtual getDuration : ()I
    //   43: invokevirtual setMax : (I)V
    //   46: aload_0
    //   47: getfield mHandler : Landroid/os/Handler;
    //   50: aload_0
    //   51: getfield progressBarRunnable : Ljava/lang/Runnable;
    //   54: ldc2_w 1000
    //   57: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
    //   60: pop
    //   61: aload_0
    //   62: getfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   65: invokevirtual getDuration : ()I
    //   68: sipush #1000
    //   71: idiv
    //   72: i2l
    //   73: lstore_1
    //   74: aload_0
    //   75: getfield mTimerFormat : Ljava/lang/String;
    //   78: iconst_2
    //   79: anewarray java/lang/Object
    //   82: dup
    //   83: iconst_0
    //   84: lload_1
    //   85: ldc2_w 60
    //   88: ldiv
    //   89: invokestatic valueOf : (J)Ljava/lang/Long;
    //   92: aastore
    //   93: dup
    //   94: iconst_1
    //   95: lload_1
    //   96: ldc2_w 60
    //   99: lrem
    //   100: invokestatic valueOf : (J)Ljava/lang/Long;
    //   103: aastore
    //   104: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   107: astore_3
    //   108: aload_0
    //   109: getfield tx_endtime : Landroid/widget/TextView;
    //   112: astore #4
    //   114: new java/lang/StringBuilder
    //   117: dup
    //   118: invokespecial <init> : ()V
    //   121: astore #5
    //   123: aload #5
    //   125: ldc_w ''
    //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload #5
    //   134: aload_3
    //   135: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: pop
    //   139: aload #4
    //   141: aload #5
    //   143: invokevirtual toString : ()Ljava/lang/String;
    //   146: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   149: aload_0
    //   150: getfield playingId : I
    //   153: iconst_m1
    //   154: if_icmpne -> 162
    //   157: aload_0
    //   158: iconst_0
    //   159: putfield playingId : I
    //   162: aload_0
    //   163: getfield musicInfos : Ljava/util/ArrayList;
    //   166: invokevirtual size : ()I
    //   169: ifne -> 173
    //   172: return
    //   173: aload_0
    //   174: getfield musicInfos : Ljava/util/ArrayList;
    //   177: aload_0
    //   178: getfield playingId : I
    //   181: invokevirtual get : (I)Ljava/lang/Object;
    //   184: checkcast com/qh/blelight/MusicInfo
    //   187: invokevirtual getSongName : ()Ljava/lang/String;
    //   190: astore #5
    //   192: aload #5
    //   194: ifnull -> 212
    //   197: aload #5
    //   199: astore #4
    //   201: ldc_w ''
    //   204: aload #5
    //   206: invokevirtual equals : (Ljava/lang/Object;)Z
    //   209: ifeq -> 268
    //   212: aload_0
    //   213: getfield musicInfos : Ljava/util/ArrayList;
    //   216: aload_0
    //   217: getfield playingId : I
    //   220: invokevirtual get : (I)Ljava/lang/Object;
    //   223: checkcast com/qh/blelight/MusicInfo
    //   226: invokevirtual getDisplay_name : ()Ljava/lang/String;
    //   229: astore #4
    //   231: aload #4
    //   233: ifnull -> 263
    //   236: aload #4
    //   238: invokevirtual length : ()I
    //   241: iconst_4
    //   242: if_icmple -> 263
    //   245: aload #4
    //   247: iconst_0
    //   248: aload #4
    //   250: invokevirtual length : ()I
    //   253: iconst_4
    //   254: isub
    //   255: invokevirtual substring : (II)Ljava/lang/String;
    //   258: astore #4
    //   260: goto -> 268
    //   263: ldc_w 'other'
    //   266: astore #4
    //   268: aload_0
    //   269: getfield tx_music_name : Landroid/widget/TextView;
    //   272: aload #4
    //   274: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   277: aload_0
    //   278: getfield tx_album : Landroid/widget/TextView;
    //   281: astore #4
    //   283: new java/lang/StringBuilder
    //   286: dup
    //   287: invokespecial <init> : ()V
    //   290: astore #5
    //   292: aload #5
    //   294: ldc_w ''
    //   297: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   300: pop
    //   301: aload #5
    //   303: aload_0
    //   304: getfield musicInfos : Ljava/util/ArrayList;
    //   307: aload_0
    //   308: getfield playingId : I
    //   311: invokevirtual get : (I)Ljava/lang/Object;
    //   314: checkcast com/qh/blelight/MusicInfo
    //   317: invokevirtual getAlbum : ()Ljava/lang/String;
    //   320: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: pop
    //   324: aload #4
    //   326: aload #5
    //   328: invokevirtual toString : ()Ljava/lang/String;
    //   331: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   334: aload_0
    //   335: getfield myMediaPlayer : Landroid/media/MediaPlayer;
    //   338: invokevirtual isPlaying : ()Z
    //   341: ifeq -> 357
    //   344: aload_0
    //   345: getfield img_play : Landroid/widget/ImageView;
    //   348: ldc_w 2131165419
    //   351: invokevirtual setBackgroundResource : (I)V
    //   354: goto -> 367
    //   357: aload_0
    //   358: getfield img_play : Landroid/widget/ImageView;
    //   361: ldc_w 2131165412
    //   364: invokevirtual setBackgroundResource : (I)V
    //   367: return
  }
  
  public class Item {
    public TextView list_music_artist;
    
    public TextView list_music_name;
    
    public TextView list_play_time;
  }
  
  public class MusicInfoAdapter extends BaseAdapter {
    Context context;
    
    LayoutInflater inflater;
    
    List<MusicInfo> mMusicInfos;
    
    int mposition;
    
    public MusicInfoAdapter(Context param1Context, List<MusicInfo> param1List) {
      this.context = param1Context;
      this.mMusicInfos = param1List;
      this.inflater = LayoutInflater.from(param1Context);
    }
    
    public int getCount() {
      return this.mMusicInfos.size();
    }
    
    public Object getItem(int param1Int) {
      return this.mMusicInfos.get(param1Int);
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      ViewGroup viewGroup;
      MusicInfo musicInfo = this.mMusicInfos.get(param1Int);
      MusicActivity.Item item = new MusicActivity.Item();
      if (param1View != null) {
        viewGroup = (ViewGroup)param1View;
      } else {
        viewGroup = (ViewGroup)this.inflater.inflate(2131361875, null);
      } 
      if (musicInfo.getTag()) {
        viewGroup.setBackgroundColor(this.context.getResources().getColor(2131034187));
      } else {
        viewGroup.setBackgroundColor(this.context.getResources().getColor(2131034215));
      } 
      item.list_music_name = (TextView)viewGroup.findViewById(2131230991);
      item.list_music_artist = (TextView)viewGroup.findViewById(2131230994);
      item.list_play_time = (TextView)viewGroup.findViewById(2131230995);
      TextView textView2 = item.list_music_name;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(musicInfo.getSongName());
      textView2.setText(stringBuilder.toString());
      textView2 = item.list_music_artist;
      stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(musicInfo.getArtist());
      textView2.setText(stringBuilder.toString());
      long l = musicInfo.getPlayTime() / 1000L;
      String str = String.format(MusicActivity.this.mTimerFormat, new Object[] { Long.valueOf(l / 60L), Long.valueOf(l % 60L) });
      TextView textView1 = item.list_play_time;
      stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(str);
      textView1.setText(stringBuilder.toString());
      return (View)viewGroup;
    }
    
    public void setColor() {
      if (this.mMusicInfos != null && this.mMusicInfos.size() > 0) {
        for (byte b = 0; b < this.mMusicInfos.size(); b++)
          ((MusicInfo)this.mMusicInfos.get(b)).setTag(false); 
        notifyDataSetChanged();
      } 
    }
    
    public void setItem(int param1Int) {
      if (this.mMusicInfos != null && this.mMusicInfos.size() > 0) {
        for (byte b = 0; b < this.mMusicInfos.size(); b++) {
          if (param1Int == b) {
            ((MusicInfo)this.mMusicInfos.get(b)).setTag(true);
          } else {
            ((MusicInfo)this.mMusicInfos.get(b)).setTag(false);
          } 
        } 
        notifyDataSetChanged();
      } 
    }
  }
  
  class VisualizerView extends View {
    private byte[] mBytes;
    
    private Paint mPaint = new Paint();
    
    private float[] mPoints;
    
    private Rect mRect = new Rect();
    
    public VisualizerView(Context param1Context) {
      super(param1Context);
      init();
    }
    
    private void init() {
      this.mBytes = null;
      this.mPaint.setStrokeWidth(10.0F);
      this.mPaint.setAntiAlias(true);
      this.mPaint.setColor(-65536);
    }
    
    protected void onDraw(Canvas param1Canvas) {
      super.onDraw(param1Canvas);
      MusicActivity.this.w = getWidth();
      MusicActivity.this.h = getHeight();
      if (this.mBytes == null)
        return; 
      if (this.mPoints == null || this.mPoints.length < this.mBytes.length * 4)
        this.mPoints = new float[this.mBytes.length * 4]; 
      this.mRect.set(0, 0, getWidth(), getHeight());
      MusicActivity.this.points.clear();
      int i = 0;
      while (i < this.mBytes.length - 1) {
        float[] arrayOfFloat = this.mPoints;
        int i3 = i * 4;
        arrayOfFloat[i3] = (this.mRect.width() * i / (this.mBytes.length - 1));
        this.mPoints[i3 + 1] = (this.mRect.height() / 2 + (byte)(this.mBytes[i] + 128) * this.mRect.height() / 2 / 128);
        arrayOfFloat = this.mPoints;
        int i4 = this.mRect.width();
        arrayOfFloat[i3 + 2] = (i4 * ++i / (this.mBytes.length - 1));
        arrayOfFloat = this.mPoints;
        i4 = i3 + 3;
        arrayOfFloat[i4] = (this.mRect.height() / 2 + (byte)(this.mBytes[i] + 128) * this.mRect.height() / 2 / 128);
        MusicActivity.this.points.add(Float.valueOf(this.mPoints[i4]));
      } 
      int m = 1;
      int n = 0;
      i = 0;
      int i1 = 0;
      float f1 = 0.0F;
      int k = 0;
      int j = 0;
      int i2 = 0;
      float f2;
      for (f2 = 200.0F; m < MusicActivity.this.points.size() - 1; f2 = f3) {
        float f3 = ((Float)MusicActivity.this.points.get(m)).floatValue();
        ArrayList<Float> arrayList = MusicActivity.this.points;
        int i3 = m + 1;
        int i4 = n;
        int i5 = i;
        int i6 = i1;
        float f4 = f1;
        int i7 = k;
        int i8 = j;
        int i9 = i2;
        if (f3 > ((Float)arrayList.get(i3)).floatValue()) {
          i4 = n;
          i5 = i;
          i6 = i1;
          f4 = f1;
          i7 = k;
          i8 = j;
          i9 = i2;
          if (((Float)MusicActivity.this.points.get(m)).floatValue() > ((Float)MusicActivity.this.points.get(m - 1)).floatValue()) {
            int i10 = i1 + 1;
            f3 = f1;
            if (((Float)MusicActivity.this.points.get(m)).floatValue() > f1)
              f3 = ((Float)MusicActivity.this.points.get(m)).floatValue(); 
            i1 = i2;
            if (((Float)MusicActivity.this.points.get(m)).floatValue() < MusicActivity.HEIGHT)
              i1 = i2 + 1; 
            if (((Float)MusicActivity.this.points.get(m)).floatValue() > MusicActivity.HEIGHT) {
              ((Float)MusicActivity.this.points.get(m)).floatValue();
              f1 = (MusicActivity.HEIGHT + 20);
            } 
            i2 = j;
            if (((Float)MusicActivity.this.points.get(m)).floatValue() > (MusicActivity.HEIGHT + 20)) {
              i2 = j;
              if (((Float)MusicActivity.this.points.get(m)).floatValue() <= (MusicActivity.HEIGHT + 40))
                i2 = j + 1; 
            } 
            j = k;
            if (((Float)MusicActivity.this.points.get(m)).floatValue() > (MusicActivity.HEIGHT + 40)) {
              j = k;
              if (((Float)MusicActivity.this.points.get(m)).floatValue() <= (MusicActivity.HEIGHT + 60))
                j = k + 1; 
            } 
            k = i;
            if (((Float)MusicActivity.this.points.get(m)).floatValue() > (MusicActivity.HEIGHT + 60)) {
              k = i;
              if (((Float)MusicActivity.this.points.get(m)).floatValue() <= (MusicActivity.HEIGHT + 80))
                k = i + 1; 
            } 
            i4 = n;
            i5 = k;
            i6 = i10;
            f4 = f3;
            i7 = j;
            i8 = i2;
            i9 = i1;
            if (((Float)MusicActivity.this.points.get(m)).floatValue() > (MusicActivity.HEIGHT + 80)) {
              i4 = n + 1;
              i9 = i1;
              i8 = i2;
              i7 = j;
              f4 = f3;
              i6 = i10;
              i5 = k;
            } 
          } 
        } 
        f3 = f2;
        if (((Float)MusicActivity.this.points.get(m)).floatValue() < ((Float)MusicActivity.this.points.get(i3)).floatValue()) {
          f3 = f2;
          if (((Float)MusicActivity.this.points.get(m)).floatValue() < ((Float)MusicActivity.this.points.get(m - 1)).floatValue()) {
            f3 = f2;
            if (((Float)MusicActivity.this.points.get(m)).floatValue() < f2)
              f3 = ((Float)MusicActivity.this.points.get(m)).floatValue(); 
          } 
        } 
        m = i3;
        n = i4;
        i = i5;
        i1 = i6;
        f1 = f4;
        k = i7;
        j = i8;
        i2 = i9;
      } 
      if (n > 0) {
        MusicActivity.this.oldRms = 900 - n * 100;
        MusicActivity.this.oldRms = -MusicActivity.this.oldRms;
      } else if (i > 0) {
        MusicActivity.this.oldRms = 3300 - i * 80;
        MusicActivity.this.oldRms = -MusicActivity.this.oldRms;
      } else if (k > 0) {
        MusicActivity.this.oldRms = 6000 - k * 80;
        MusicActivity.this.oldRms = -MusicActivity.this.oldRms;
      } else if (j > 0) {
        MusicActivity.this.oldRms = 8000 - j * 80;
        MusicActivity.this.oldRms = -MusicActivity.this.oldRms;
      } 
      MusicActivity.calpha = 0;
      if (i1 > 80 && n > 1)
        MusicActivity.calpha = 5; 
      if (i1 > 80 && n > 3)
        MusicActivity.calpha = 8; 
      if (i1 > 80 && n > 5)
        MusicActivity.calpha = 13; 
      param1Canvas.drawLine(20.0F, (MusicActivity.this.h - 1), 20.0F, (150 - i2 * 5), this.mPaint);
      param1Canvas.drawLine(50.0F, (MusicActivity.this.h - 1), 50.0F, (150 - j * 5), this.mPaint);
      param1Canvas.drawLine(90.0F, (MusicActivity.this.h - 1), 90.0F, (150 - i * 5), this.mPaint);
      param1Canvas.drawLine(20.0F, (i * 3 + 20), 20.0F, 20.0F, this.mPaint);
      param1Canvas.drawLine(50.0F, (j * 3 + 20), 50.0F, 20.0F, this.mPaint);
      param1Canvas.drawLine(90.0F, (i2 * 3 + 20), 90.0F, 20.0F, this.mPaint);
      param1Canvas.drawLines(this.mPoints, this.mPaint);
      if (i1 >= MusicActivity.HEIGHT_FREQUENCY) {
        double d = MusicActivity.red;
        Double.isNaN(d);
        MusicActivity.cred = (int)(d * 1.5D);
        d = MusicActivity.green;
        Double.isNaN(d);
        MusicActivity.cgreen = (int)(d * 1.5D);
        d = MusicActivity.blue;
        Double.isNaN(d);
        MusicActivity.cblue = (int)(d * 1.5D);
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 0);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 0);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 0);
      } else if (i1 >= MusicActivity.HEIGHT_FREQUENCY - 30) {
        double d = MusicActivity.red;
        Double.isNaN(d);
        MusicActivity.cred = (int)(d * 1.3D);
        d = MusicActivity.green;
        Double.isNaN(d);
        MusicActivity.cgreen = (int)(d * 1.3D);
        d = MusicActivity.blue;
        Double.isNaN(d);
        MusicActivity.cblue = (int)(d * 1.3D);
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 0);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 0);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 0);
      } else if (i1 >= MusicActivity.HEIGHT_FREQUENCY - 50) {
        double d = MusicActivity.red;
        Double.isNaN(d);
        MusicActivity.cred = (int)(d * 1.1D);
        d = MusicActivity.green;
        Double.isNaN(d);
        MusicActivity.cgreen = (int)(d * 1.1D);
        d = MusicActivity.blue;
        Double.isNaN(d);
        MusicActivity.cblue = (int)(d * 1.1D);
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 0);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 0);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 0);
      } else if (i1 >= MusicActivity.HEIGHT_FREQUENCY - 80) {
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.red, 0);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.green, 0);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.blue, 0);
      } else if (i1 < 20) {
        double d = MusicActivity.red;
        Double.isNaN(d);
        MusicActivity.cred = (int)(d * 0.5D);
        d = MusicActivity.green;
        Double.isNaN(d);
        MusicActivity.cgreen = (int)(d * 0.5D);
        d = MusicActivity.blue;
        Double.isNaN(d);
        MusicActivity.cblue = (int)(d * 0.5D);
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 0);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 0);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 0);
      } else if (i1 < 10) {
        double d = MusicActivity.red;
        Double.isNaN(d);
        MusicActivity.cred = (int)(d * 0.2D);
        d = MusicActivity.green;
        Double.isNaN(d);
        MusicActivity.cgreen = (int)(d * 0.2D);
        d = MusicActivity.blue;
        Double.isNaN(d);
        MusicActivity.cblue = (int)(d * 0.2D);
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 0);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 0);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 0);
      } 
      if (f1 >= 255.0F) {
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 15);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 15);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 15);
      } else if (f1 >= 200.0F) {
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 8);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 8);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 8);
      } else if (f1 >= 180.0F) {
        MusicActivity.cred = MusicActivity.this.addcolor(MusicActivity.cred, 3);
        MusicActivity.cgreen = MusicActivity.this.addcolor(MusicActivity.cgreen, 3);
        MusicActivity.cblue = MusicActivity.this.addcolor(MusicActivity.cblue, 3);
      } else if (f1 < 150.0F) {
        if (MusicActivity.cred > MusicActivity.cgreen && MusicActivity.cred > MusicActivity.cblue) {
          MusicActivity.cred = 30;
          MusicActivity.cgreen = 5;
          MusicActivity.cblue = 0;
        } 
        if (MusicActivity.cgreen > MusicActivity.cred && MusicActivity.cgreen > MusicActivity.cblue) {
          MusicActivity.cred = 5;
          MusicActivity.cgreen = 30;
          MusicActivity.cblue = 0;
        } 
        if (MusicActivity.cblue > MusicActivity.cred && MusicActivity.cblue > MusicActivity.cgreen) {
          MusicActivity.cred = 5;
          MusicActivity.cgreen = 0;
          MusicActivity.cblue = 30;
        } 
        MusicActivity.calpha = 0;
      } 
    }
    
    public void updateVisualizer(byte[] param1ArrayOfbyte) {
      this.mBytes = param1ArrayOfbyte;
      invalidate();
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MusicActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */