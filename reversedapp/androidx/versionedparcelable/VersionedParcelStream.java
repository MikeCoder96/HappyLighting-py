package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;

@RestrictTo({RestrictTo.Scope.LIBRARY})
class VersionedParcelStream extends VersionedParcel {
  private static final int TYPE_BOOLEAN = 5;
  
  private static final int TYPE_BOOLEAN_ARRAY = 6;
  
  private static final int TYPE_DOUBLE = 7;
  
  private static final int TYPE_DOUBLE_ARRAY = 8;
  
  private static final int TYPE_FLOAT = 13;
  
  private static final int TYPE_FLOAT_ARRAY = 14;
  
  private static final int TYPE_INT = 9;
  
  private static final int TYPE_INT_ARRAY = 10;
  
  private static final int TYPE_LONG = 11;
  
  private static final int TYPE_LONG_ARRAY = 12;
  
  private static final int TYPE_NULL = 0;
  
  private static final int TYPE_STRING = 3;
  
  private static final int TYPE_STRING_ARRAY = 4;
  
  private static final int TYPE_SUB_BUNDLE = 1;
  
  private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
  
  private static final Charset UTF_16 = Charset.forName("UTF-16");
  
  private final SparseArray<InputBuffer> mCachedFields;
  
  private DataInputStream mCurrentInput;
  
  private DataOutputStream mCurrentOutput;
  
  private FieldBuffer mFieldBuffer;
  
  private boolean mIgnoreParcelables;
  
  private final DataInputStream mMasterInput;
  
  private final DataOutputStream mMasterOutput;
  
  public VersionedParcelStream(InputStream paramInputStream, OutputStream paramOutputStream) {
    DataOutputStream dataOutputStream;
    this.mCachedFields = new SparseArray();
    InputStream inputStream = null;
    if (paramInputStream != null) {
      paramInputStream = new DataInputStream(paramInputStream);
    } else {
      paramInputStream = null;
    } 
    this.mMasterInput = (DataInputStream)paramInputStream;
    paramInputStream = inputStream;
    if (paramOutputStream != null)
      dataOutputStream = new DataOutputStream(paramOutputStream); 
    this.mMasterOutput = dataOutputStream;
    this.mCurrentInput = this.mMasterInput;
    this.mCurrentOutput = this.mMasterOutput;
  }
  
  private void readObject(int paramInt, String paramString, Bundle paramBundle) {
    StringBuilder stringBuilder;
    switch (paramInt) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown type ");
        stringBuilder.append(paramInt);
        throw new RuntimeException(stringBuilder.toString());
      case 14:
        paramBundle.putFloatArray((String)stringBuilder, readFloatArray());
        return;
      case 13:
        paramBundle.putFloat((String)stringBuilder, readFloat());
        return;
      case 12:
        paramBundle.putLongArray((String)stringBuilder, readLongArray());
        return;
      case 11:
        paramBundle.putLong((String)stringBuilder, readLong());
        return;
      case 10:
        paramBundle.putIntArray((String)stringBuilder, readIntArray());
        return;
      case 9:
        paramBundle.putInt((String)stringBuilder, readInt());
        return;
      case 8:
        paramBundle.putDoubleArray((String)stringBuilder, readDoubleArray());
        return;
      case 7:
        paramBundle.putDouble((String)stringBuilder, readDouble());
        return;
      case 6:
        paramBundle.putBooleanArray((String)stringBuilder, readBooleanArray());
        return;
      case 5:
        paramBundle.putBoolean((String)stringBuilder, readBoolean());
        return;
      case 4:
        paramBundle.putStringArray((String)stringBuilder, readArray(new String[0]));
        return;
      case 3:
        paramBundle.putString((String)stringBuilder, readString());
        return;
      case 2:
        paramBundle.putBundle((String)stringBuilder, readBundle());
        return;
      case 1:
        paramBundle.putBundle((String)stringBuilder, readBundle());
        return;
      case 0:
        break;
    } 
    paramBundle.putParcelable((String)stringBuilder, null);
  }
  
  private void writeObject(Object paramObject) {
    if (paramObject == null) {
      writeInt(0);
    } else if (paramObject instanceof Bundle) {
      writeInt(1);
      writeBundle((Bundle)paramObject);
    } else if (paramObject instanceof String) {
      writeInt(3);
      writeString((String)paramObject);
    } else if (paramObject instanceof String[]) {
      writeInt(4);
      writeArray((String[])paramObject);
    } else if (paramObject instanceof Boolean) {
      writeInt(5);
      writeBoolean(((Boolean)paramObject).booleanValue());
    } else if (paramObject instanceof boolean[]) {
      writeInt(6);
      writeBooleanArray((boolean[])paramObject);
    } else if (paramObject instanceof Double) {
      writeInt(7);
      writeDouble(((Double)paramObject).doubleValue());
    } else if (paramObject instanceof double[]) {
      writeInt(8);
      writeDoubleArray((double[])paramObject);
    } else if (paramObject instanceof Integer) {
      writeInt(9);
      writeInt(((Integer)paramObject).intValue());
    } else if (paramObject instanceof int[]) {
      writeInt(10);
      writeIntArray((int[])paramObject);
    } else if (paramObject instanceof Long) {
      writeInt(11);
      writeLong(((Long)paramObject).longValue());
    } else if (paramObject instanceof long[]) {
      writeInt(12);
      writeLongArray((long[])paramObject);
    } else if (paramObject instanceof Float) {
      writeInt(13);
      writeFloat(((Float)paramObject).floatValue());
    } else {
      if (paramObject instanceof float[]) {
        writeInt(14);
        writeFloatArray((float[])paramObject);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unsupported type ");
      stringBuilder.append(paramObject.getClass());
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  public void closeField() {
    if (this.mFieldBuffer != null)
      try {
        if (this.mFieldBuffer.mOutput.size() != 0)
          this.mFieldBuffer.flushField(); 
        this.mFieldBuffer = null;
      } catch (IOException iOException) {
        throw new VersionedParcel.ParcelException(iOException);
      }  
  }
  
  protected VersionedParcel createSubParcel() {
    return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput);
  }
  
  public boolean isStream() {
    return true;
  }
  
  public boolean readBoolean() {
    try {
      return this.mCurrentInput.readBoolean();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public Bundle readBundle() {
    int i = readInt();
    if (i < 0)
      return null; 
    Bundle bundle = new Bundle();
    for (byte b = 0; b < i; b++) {
      String str = readString();
      readObject(readInt(), str, bundle);
    } 
    return bundle;
  }
  
  public byte[] readByteArray() {
    try {
      int i = this.mCurrentInput.readInt();
      if (i > 0) {
        byte[] arrayOfByte = new byte[i];
        this.mCurrentInput.readFully(arrayOfByte);
        return arrayOfByte;
      } 
      return null;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public double readDouble() {
    try {
      return this.mCurrentInput.readDouble();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public boolean readField(int paramInt) {
    InputBuffer inputBuffer = (InputBuffer)this.mCachedFields.get(paramInt);
    if (inputBuffer != null) {
      this.mCachedFields.remove(paramInt);
      this.mCurrentInput = inputBuffer.mInputStream;
      return true;
    } 
    try {
      while (true) {
        int i = this.mMasterInput.readInt();
        int j = i & 0xFFFF;
        int k = j;
        if (j == 65535)
          k = this.mMasterInput.readInt(); 
        inputBuffer = new InputBuffer();
        this(i >> 16 & 0xFFFF, k, this.mMasterInput);
        if (inputBuffer.mFieldId == paramInt) {
          this.mCurrentInput = inputBuffer.mInputStream;
          return true;
        } 
        this.mCachedFields.put(inputBuffer.mFieldId, inputBuffer);
      } 
    } catch (IOException iOException) {
      return false;
    } 
  }
  
  public float readFloat() {
    try {
      return this.mCurrentInput.readFloat();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public int readInt() {
    try {
      return this.mCurrentInput.readInt();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public long readLong() {
    try {
      return this.mCurrentInput.readLong();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public <T extends Parcelable> T readParcelable() {
    return null;
  }
  
  public String readString() {
    try {
      int i = this.mCurrentInput.readInt();
      if (i > 0) {
        byte[] arrayOfByte = new byte[i];
        this.mCurrentInput.readFully(arrayOfByte);
        return new String(arrayOfByte, UTF_16);
      } 
      return null;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public IBinder readStrongBinder() {
    return null;
  }
  
  public void setOutputField(int paramInt) {
    closeField();
    this.mFieldBuffer = new FieldBuffer(paramInt, this.mMasterOutput);
    this.mCurrentOutput = this.mFieldBuffer.mDataStream;
  }
  
  public void setSerializationFlags(boolean paramBoolean1, boolean paramBoolean2) {
    if (paramBoolean1) {
      this.mIgnoreParcelables = paramBoolean2;
      return;
    } 
    throw new RuntimeException("Serialization of this object is not allowed");
  }
  
  public void writeBoolean(boolean paramBoolean) {
    try {
      this.mCurrentOutput.writeBoolean(paramBoolean);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeBundle(Bundle paramBundle) {
    if (paramBundle != null) {
      try {
        Set set = paramBundle.keySet();
        this.mCurrentOutput.writeInt(set.size());
        for (String str : set) {
          writeString(str);
          writeObject(paramBundle.get(str));
        } 
      } catch (IOException iOException) {}
    } else {
      this.mCurrentOutput.writeInt(-1);
    } 
  }
  
  public void writeByteArray(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      try {
        this.mCurrentOutput.writeInt(paramArrayOfbyte.length);
        this.mCurrentOutput.write(paramArrayOfbyte);
      } catch (IOException iOException) {}
    } else {
      this.mCurrentOutput.writeInt(-1);
    } 
  }
  
  public void writeByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      try {
        this.mCurrentOutput.writeInt(paramInt2);
        this.mCurrentOutput.write(paramArrayOfbyte, paramInt1, paramInt2);
      } catch (IOException iOException) {}
    } else {
      this.mCurrentOutput.writeInt(-1);
    } 
  }
  
  public void writeDouble(double paramDouble) {
    try {
      this.mCurrentOutput.writeDouble(paramDouble);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeFloat(float paramFloat) {
    try {
      this.mCurrentOutput.writeFloat(paramFloat);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeInt(int paramInt) {
    try {
      this.mCurrentOutput.writeInt(paramInt);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeLong(long paramLong) {
    try {
      this.mCurrentOutput.writeLong(paramLong);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeParcelable(Parcelable paramParcelable) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Parcelables cannot be written to an OutputStream");
  }
  
  public void writeString(String paramString) {
    if (paramString != null) {
      try {
        byte[] arrayOfByte = paramString.getBytes(UTF_16);
        this.mCurrentOutput.writeInt(arrayOfByte.length);
        this.mCurrentOutput.write(arrayOfByte);
      } catch (IOException iOException) {}
    } else {
      this.mCurrentOutput.writeInt(-1);
    } 
  }
  
  public void writeStrongBinder(IBinder paramIBinder) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Binders cannot be written to an OutputStream");
  }
  
  public void writeStrongInterface(IInterface paramIInterface) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Binders cannot be written to an OutputStream");
  }
  
  private static class FieldBuffer {
    final DataOutputStream mDataStream = new DataOutputStream(this.mOutput);
    
    private final int mFieldId;
    
    final ByteArrayOutputStream mOutput = new ByteArrayOutputStream();
    
    private final DataOutputStream mTarget;
    
    FieldBuffer(int param1Int, DataOutputStream param1DataOutputStream) {
      this.mFieldId = param1Int;
      this.mTarget = param1DataOutputStream;
    }
    
    void flushField() throws IOException {
      int k;
      this.mDataStream.flush();
      int i = this.mOutput.size();
      int j = this.mFieldId;
      if (i >= 65535) {
        k = 65535;
      } else {
        k = i;
      } 
      this.mTarget.writeInt(j << 16 | k);
      if (i >= 65535)
        this.mTarget.writeInt(i); 
      this.mOutput.writeTo(this.mTarget);
    }
  }
  
  private static class InputBuffer {
    final int mFieldId;
    
    final DataInputStream mInputStream;
    
    private final int mSize;
    
    InputBuffer(int param1Int1, int param1Int2, DataInputStream param1DataInputStream) throws IOException {
      this.mSize = param1Int2;
      this.mFieldId = param1Int1;
      byte[] arrayOfByte = new byte[this.mSize];
      param1DataInputStream.readFully(arrayOfByte);
      this.mInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte));
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\androidx\versionedparcelable\VersionedParcelStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */