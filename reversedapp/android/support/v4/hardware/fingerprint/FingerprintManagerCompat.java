package android.support.v4.hardware.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public final class FingerprintManagerCompat {
  private final Context mContext;
  
  private FingerprintManagerCompat(Context paramContext) {
    this.mContext = paramContext;
  }
  
  @NonNull
  public static FingerprintManagerCompat from(@NonNull Context paramContext) {
    return new FingerprintManagerCompat(paramContext);
  }
  
  @Nullable
  @RequiresApi(23)
  private static FingerprintManager getFingerprintManagerOrNull(@NonNull Context paramContext) {
    return paramContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint") ? (FingerprintManager)paramContext.getSystemService(FingerprintManager.class) : null;
  }
  
  @RequiresApi(23)
  static CryptoObject unwrapCryptoObject(FingerprintManager.CryptoObject paramCryptoObject) {
    return (paramCryptoObject == null) ? null : ((paramCryptoObject.getCipher() != null) ? new CryptoObject(paramCryptoObject.getCipher()) : ((paramCryptoObject.getSignature() != null) ? new CryptoObject(paramCryptoObject.getSignature()) : ((paramCryptoObject.getMac() != null) ? new CryptoObject(paramCryptoObject.getMac()) : null)));
  }
  
  @RequiresApi(23)
  private static FingerprintManager.AuthenticationCallback wrapCallback(final AuthenticationCallback callback) {
    return new FingerprintManager.AuthenticationCallback() {
        public void onAuthenticationError(int param1Int, CharSequence param1CharSequence) {
          callback.onAuthenticationError(param1Int, param1CharSequence);
        }
        
        public void onAuthenticationFailed() {
          callback.onAuthenticationFailed();
        }
        
        public void onAuthenticationHelp(int param1Int, CharSequence param1CharSequence) {
          callback.onAuthenticationHelp(param1Int, param1CharSequence);
        }
        
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult param1AuthenticationResult) {
          callback.onAuthenticationSucceeded(new FingerprintManagerCompat.AuthenticationResult(FingerprintManagerCompat.unwrapCryptoObject(param1AuthenticationResult.getCryptoObject())));
        }
      };
  }
  
  @RequiresApi(23)
  private static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject paramCryptoObject) {
    return (paramCryptoObject == null) ? null : ((paramCryptoObject.getCipher() != null) ? new FingerprintManager.CryptoObject(paramCryptoObject.getCipher()) : ((paramCryptoObject.getSignature() != null) ? new FingerprintManager.CryptoObject(paramCryptoObject.getSignature()) : ((paramCryptoObject.getMac() != null) ? new FingerprintManager.CryptoObject(paramCryptoObject.getMac()) : null)));
  }
  
  @RequiresPermission("android.permission.USE_FINGERPRINT")
  public void authenticate(@Nullable CryptoObject paramCryptoObject, int paramInt, @Nullable CancellationSignal paramCancellationSignal, @NonNull AuthenticationCallback paramAuthenticationCallback, @Nullable Handler paramHandler) {
    if (Build.VERSION.SDK_INT >= 23) {
      FingerprintManager fingerprintManager = getFingerprintManagerOrNull(this.mContext);
      if (fingerprintManager != null) {
        if (paramCancellationSignal != null) {
          CancellationSignal cancellationSignal = (CancellationSignal)paramCancellationSignal.getCancellationSignalObject();
        } else {
          paramCancellationSignal = null;
        } 
        fingerprintManager.authenticate(wrapCryptoObject(paramCryptoObject), (CancellationSignal)paramCancellationSignal, paramInt, wrapCallback(paramAuthenticationCallback), paramHandler);
      } 
    } 
  }
  
  @RequiresPermission("android.permission.USE_FINGERPRINT")
  public boolean hasEnrolledFingerprints() {
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 23) {
      FingerprintManager fingerprintManager = getFingerprintManagerOrNull(this.mContext);
      boolean bool1 = bool;
      if (fingerprintManager != null) {
        bool1 = bool;
        if (fingerprintManager.hasEnrolledFingerprints())
          bool1 = true; 
      } 
      return bool1;
    } 
    return false;
  }
  
  @RequiresPermission("android.permission.USE_FINGERPRINT")
  public boolean isHardwareDetected() {
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 23) {
      FingerprintManager fingerprintManager = getFingerprintManagerOrNull(this.mContext);
      boolean bool1 = bool;
      if (fingerprintManager != null) {
        bool1 = bool;
        if (fingerprintManager.isHardwareDetected())
          bool1 = true; 
      } 
      return bool1;
    } 
    return false;
  }
  
  public static abstract class AuthenticationCallback {
    public void onAuthenticationError(int param1Int, CharSequence param1CharSequence) {}
    
    public void onAuthenticationFailed() {}
    
    public void onAuthenticationHelp(int param1Int, CharSequence param1CharSequence) {}
    
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult param1AuthenticationResult) {}
  }
  
  public static final class AuthenticationResult {
    private final FingerprintManagerCompat.CryptoObject mCryptoObject;
    
    public AuthenticationResult(FingerprintManagerCompat.CryptoObject param1CryptoObject) {
      this.mCryptoObject = param1CryptoObject;
    }
    
    public FingerprintManagerCompat.CryptoObject getCryptoObject() {
      return this.mCryptoObject;
    }
  }
  
  public static class CryptoObject {
    private final Cipher mCipher;
    
    private final Mac mMac;
    
    private final Signature mSignature;
    
    public CryptoObject(@NonNull Signature param1Signature) {
      this.mSignature = param1Signature;
      this.mCipher = null;
      this.mMac = null;
    }
    
    public CryptoObject(@NonNull Cipher param1Cipher) {
      this.mCipher = param1Cipher;
      this.mSignature = null;
      this.mMac = null;
    }
    
    public CryptoObject(@NonNull Mac param1Mac) {
      this.mMac = param1Mac;
      this.mCipher = null;
      this.mSignature = null;
    }
    
    @Nullable
    public Cipher getCipher() {
      return this.mCipher;
    }
    
    @Nullable
    public Mac getMac() {
      return this.mMac;
    }
    
    @Nullable
    public Signature getSignature() {
      return this.mSignature;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\hardware\fingerprint\FingerprintManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */