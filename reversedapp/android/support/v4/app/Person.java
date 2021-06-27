package android.support.v4.app;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.IconCompat;

public class Person {
  private static final String ICON_KEY = "icon";
  
  private static final String IS_BOT_KEY = "isBot";
  
  private static final String IS_IMPORTANT_KEY = "isImportant";
  
  private static final String KEY_KEY = "key";
  
  private static final String NAME_KEY = "name";
  
  private static final String URI_KEY = "uri";
  
  @Nullable
  IconCompat mIcon;
  
  boolean mIsBot;
  
  boolean mIsImportant;
  
  @Nullable
  String mKey;
  
  @Nullable
  CharSequence mName;
  
  @Nullable
  String mUri;
  
  Person(Builder paramBuilder) {
    this.mName = paramBuilder.mName;
    this.mIcon = paramBuilder.mIcon;
    this.mUri = paramBuilder.mUri;
    this.mKey = paramBuilder.mKey;
    this.mIsBot = paramBuilder.mIsBot;
    this.mIsImportant = paramBuilder.mIsImportant;
  }
  
  @NonNull
  @RequiresApi(28)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static Person fromAndroidPerson(@NonNull android.app.Person paramPerson) {
    IconCompat iconCompat;
    Builder builder = (new Builder()).setName(paramPerson.getName());
    if (paramPerson.getIcon() != null) {
      iconCompat = IconCompat.createFromIcon(paramPerson.getIcon());
    } else {
      iconCompat = null;
    } 
    return builder.setIcon(iconCompat).setUri(paramPerson.getUri()).setKey(paramPerson.getKey()).setBot(paramPerson.isBot()).setImportant(paramPerson.isImportant()).build();
  }
  
  @NonNull
  public static Person fromBundle(@NonNull Bundle paramBundle) {
    Bundle bundle = paramBundle.getBundle("icon");
    Builder builder = (new Builder()).setName(paramBundle.getCharSequence("name"));
    if (bundle != null) {
      IconCompat iconCompat = IconCompat.createFromBundle(bundle);
    } else {
      bundle = null;
    } 
    return builder.setIcon((IconCompat)bundle).setUri(paramBundle.getString("uri")).setKey(paramBundle.getString("key")).setBot(paramBundle.getBoolean("isBot")).setImportant(paramBundle.getBoolean("isImportant")).build();
  }
  
  @Nullable
  public IconCompat getIcon() {
    return this.mIcon;
  }
  
  @Nullable
  public String getKey() {
    return this.mKey;
  }
  
  @Nullable
  public CharSequence getName() {
    return this.mName;
  }
  
  @Nullable
  public String getUri() {
    return this.mUri;
  }
  
  public boolean isBot() {
    return this.mIsBot;
  }
  
  public boolean isImportant() {
    return this.mIsImportant;
  }
  
  @NonNull
  @RequiresApi(28)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public android.app.Person toAndroidPerson() {
    Icon icon;
    android.app.Person.Builder builder = (new android.app.Person.Builder()).setName(getName());
    if (getIcon() != null) {
      icon = getIcon().toIcon();
    } else {
      icon = null;
    } 
    return builder.setIcon(icon).setUri(getUri()).setKey(getKey()).setBot(isBot()).setImportant(isImportant()).build();
  }
  
  @NonNull
  public Builder toBuilder() {
    return new Builder(this);
  }
  
  @NonNull
  public Bundle toBundle() {
    Bundle bundle2;
    Bundle bundle1 = new Bundle();
    bundle1.putCharSequence("name", this.mName);
    if (this.mIcon != null) {
      bundle2 = this.mIcon.toBundle();
    } else {
      bundle2 = null;
    } 
    bundle1.putBundle("icon", bundle2);
    bundle1.putString("uri", this.mUri);
    bundle1.putString("key", this.mKey);
    bundle1.putBoolean("isBot", this.mIsBot);
    bundle1.putBoolean("isImportant", this.mIsImportant);
    return bundle1;
  }
  
  public static class Builder {
    @Nullable
    IconCompat mIcon;
    
    boolean mIsBot;
    
    boolean mIsImportant;
    
    @Nullable
    String mKey;
    
    @Nullable
    CharSequence mName;
    
    @Nullable
    String mUri;
    
    public Builder() {}
    
    Builder(Person param1Person) {
      this.mName = param1Person.mName;
      this.mIcon = param1Person.mIcon;
      this.mUri = param1Person.mUri;
      this.mKey = param1Person.mKey;
      this.mIsBot = param1Person.mIsBot;
      this.mIsImportant = param1Person.mIsImportant;
    }
    
    @NonNull
    public Person build() {
      return new Person(this);
    }
    
    @NonNull
    public Builder setBot(boolean param1Boolean) {
      this.mIsBot = param1Boolean;
      return this;
    }
    
    @NonNull
    public Builder setIcon(@Nullable IconCompat param1IconCompat) {
      this.mIcon = param1IconCompat;
      return this;
    }
    
    @NonNull
    public Builder setImportant(boolean param1Boolean) {
      this.mIsImportant = param1Boolean;
      return this;
    }
    
    @NonNull
    public Builder setKey(@Nullable String param1String) {
      this.mKey = param1String;
      return this;
    }
    
    @NonNull
    public Builder setName(@Nullable CharSequence param1CharSequence) {
      this.mName = param1CharSequence;
      return this;
    }
    
    @NonNull
    public Builder setUri(@Nullable String param1String) {
      this.mUri = param1String;
      return this;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\app\Person.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */