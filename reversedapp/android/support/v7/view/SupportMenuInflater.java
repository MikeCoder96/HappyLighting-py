package android.support.v7.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.support.annotation.LayoutRes;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.support.v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SupportMenuInflater extends MenuInflater {
  static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
  
  static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class };
  
  static final String LOG_TAG = "SupportMenuInflater";
  
  static final int NO_ID = 0;
  
  private static final String XML_GROUP = "group";
  
  private static final String XML_ITEM = "item";
  
  private static final String XML_MENU = "menu";
  
  final Object[] mActionProviderConstructorArguments;
  
  final Object[] mActionViewConstructorArguments;
  
  Context mContext;
  
  private Object mRealOwner;
  
  static {
    ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
  }
  
  public SupportMenuInflater(Context paramContext) {
    super(paramContext);
    this.mContext = paramContext;
    this.mActionViewConstructorArguments = new Object[] { paramContext };
    this.mActionProviderConstructorArguments = this.mActionViewConstructorArguments;
  }
  
  private Object findRealOwner(Object paramObject) {
    return (paramObject instanceof android.app.Activity) ? paramObject : ((paramObject instanceof ContextWrapper) ? findRealOwner(((ContextWrapper)paramObject).getBaseContext()) : paramObject);
  }
  
  private void parseMenu(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Menu paramMenu) throws XmlPullParserException, IOException {
    StringBuilder stringBuilder;
    int j;
    MenuState menuState = new MenuState(paramMenu);
    int i = paramXmlPullParser.getEventType();
    do {
      if (i == 2) {
        String str = paramXmlPullParser.getName();
        if (str.equals("menu")) {
          int n = paramXmlPullParser.next();
          break;
        } 
        stringBuilder = new StringBuilder();
        stringBuilder.append("Expecting menu, got ");
        stringBuilder.append(str);
        throw new RuntimeException(stringBuilder.toString());
      } 
      j = stringBuilder.next();
      i = j;
    } while (j != 1);
    Menu menu = null;
    i = 0;
    int k = 0;
    int m = j;
    while (i == 0) {
      String str1;
      Menu menu1;
      int n;
      String str2;
      switch (m) {
        default:
          j = k;
          paramMenu = menu;
          n = i;
          break;
        case 3:
          str2 = stringBuilder.getName();
          if (k && str2.equals(menu)) {
            paramMenu = null;
            j = 0;
            n = i;
            break;
          } 
          if (str2.equals("group")) {
            menuState.resetGroup();
            j = k;
            paramMenu = menu;
            n = i;
            break;
          } 
          if (str2.equals("item")) {
            j = k;
            paramMenu = menu;
            n = i;
            if (!menuState.hasAddedItem()) {
              if (menuState.itemActionProvider != null && menuState.itemActionProvider.hasSubMenu()) {
                menuState.addSubMenuItem();
                j = k;
                paramMenu = menu;
                n = i;
                break;
              } 
              menuState.addItem();
              j = k;
              paramMenu = menu;
              n = i;
            } 
            break;
          } 
          j = k;
          paramMenu = menu;
          n = i;
          if (str2.equals("menu")) {
            n = 1;
            j = k;
            paramMenu = menu;
          } 
          break;
        case 2:
          if (k) {
            j = k;
            paramMenu = menu;
            n = i;
            break;
          } 
          str1 = stringBuilder.getName();
          if (str1.equals("group")) {
            menuState.readGroup(paramAttributeSet);
            j = k;
            menu1 = menu;
            n = i;
            break;
          } 
          if (menu1.equals("item")) {
            menuState.readItem(paramAttributeSet);
            j = k;
            menu1 = menu;
            n = i;
            break;
          } 
          if (menu1.equals("menu")) {
            parseMenu((XmlPullParser)stringBuilder, paramAttributeSet, (Menu)menuState.addSubMenuItem());
            j = k;
            menu1 = menu;
            n = i;
            break;
          } 
          j = 1;
          n = i;
          break;
        case 1:
          throw new RuntimeException("Unexpected end of document");
      } 
      m = stringBuilder.next();
      k = j;
      menu = menu1;
      i = n;
    } 
  }
  
  Object getRealOwner() {
    if (this.mRealOwner == null)
      this.mRealOwner = findRealOwner(this.mContext); 
    return this.mRealOwner;
  }
  
  public void inflate(@LayoutRes int paramInt, Menu paramMenu) {
    if (!(paramMenu instanceof android.support.v4.internal.view.SupportMenu)) {
      super.inflate(paramInt, paramMenu);
      return;
    } 
    Menu menu1 = null;
    Menu menu2 = null;
    IOException iOException1 = null;
    try {
      XmlPullParserException xmlPullParserException;
      XmlResourceParser xmlResourceParser = this.mContext.getResources().getLayout(paramInt);
      try {
        parseMenu((XmlPullParser)xmlResourceParser, Xml.asAttributeSet((XmlPullParser)xmlResourceParser), paramMenu);
        return;
      } catch (XmlPullParserException xmlPullParserException1) {
        XmlResourceParser xmlResourceParser1 = xmlResourceParser;
      } catch (IOException iOException) {
        XmlPullParserException xmlPullParserException1 = xmlPullParserException;
      } finally {
        paramMenu = null;
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      paramMenu = menu2;
    } catch (IOException iOException2) {
      paramMenu = menu1;
      Menu menu = paramMenu;
      InflateException inflateException1 = new InflateException();
      menu = paramMenu;
      this("Error inflating menu XML", iOException2);
      menu = paramMenu;
      throw inflateException1;
    } finally {}
    Menu menu3 = paramMenu;
    InflateException inflateException = new InflateException();
    menu3 = paramMenu;
    this("Error inflating menu XML", iOException2);
    menu3 = paramMenu;
    throw inflateException;
  }
  
  private static class InflatedOnMenuItemClickListener implements MenuItem.OnMenuItemClickListener {
    private static final Class<?>[] PARAM_TYPES = new Class[] { MenuItem.class };
    
    private Method mMethod;
    
    private Object mRealOwner;
    
    public InflatedOnMenuItemClickListener(Object param1Object, String param1String) {
      this.mRealOwner = param1Object;
      Class<?> clazz = param1Object.getClass();
      try {
        this.mMethod = clazz.getMethod(param1String, PARAM_TYPES);
        return;
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Couldn't resolve menu item onClick handler ");
        stringBuilder.append(param1String);
        stringBuilder.append(" in class ");
        stringBuilder.append(clazz.getName());
        InflateException inflateException = new InflateException(stringBuilder.toString());
        inflateException.initCause(exception);
        throw inflateException;
      } 
    }
    
    public boolean onMenuItemClick(MenuItem param1MenuItem) {
      try {
        if (this.mMethod.getReturnType() == boolean.class)
          return ((Boolean)this.mMethod.invoke(this.mRealOwner, new Object[] { param1MenuItem })).booleanValue(); 
        this.mMethod.invoke(this.mRealOwner, new Object[] { param1MenuItem });
        return true;
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      } 
    }
  }
  
  private class MenuState {
    private static final int defaultGroupId = 0;
    
    private static final int defaultItemCategory = 0;
    
    private static final int defaultItemCheckable = 0;
    
    private static final boolean defaultItemChecked = false;
    
    private static final boolean defaultItemEnabled = true;
    
    private static final int defaultItemId = 0;
    
    private static final int defaultItemOrder = 0;
    
    private static final boolean defaultItemVisible = true;
    
    private int groupCategory;
    
    private int groupCheckable;
    
    private boolean groupEnabled;
    
    private int groupId;
    
    private int groupOrder;
    
    private boolean groupVisible;
    
    ActionProvider itemActionProvider;
    
    private String itemActionProviderClassName;
    
    private String itemActionViewClassName;
    
    private int itemActionViewLayout;
    
    private boolean itemAdded;
    
    private int itemAlphabeticModifiers;
    
    private char itemAlphabeticShortcut;
    
    private int itemCategoryOrder;
    
    private int itemCheckable;
    
    private boolean itemChecked;
    
    private CharSequence itemContentDescription;
    
    private boolean itemEnabled;
    
    private int itemIconResId;
    
    private ColorStateList itemIconTintList = null;
    
    private PorterDuff.Mode itemIconTintMode = null;
    
    private int itemId;
    
    private String itemListenerMethodName;
    
    private int itemNumericModifiers;
    
    private char itemNumericShortcut;
    
    private int itemShowAsAction;
    
    private CharSequence itemTitle;
    
    private CharSequence itemTitleCondensed;
    
    private CharSequence itemTooltipText;
    
    private boolean itemVisible;
    
    private Menu menu;
    
    public MenuState(Menu param1Menu) {
      this.menu = param1Menu;
      resetGroup();
    }
    
    private char getShortcut(String param1String) {
      return (param1String == null) ? Character.MIN_VALUE : param1String.charAt(0);
    }
    
    private <T> T newInstance(String param1String, Class<?>[] param1ArrayOfClass, Object[] param1ArrayOfObject) {
      try {
        null = SupportMenuInflater.this.mContext.getClassLoader().loadClass(param1String).getConstructor(param1ArrayOfClass);
        null.setAccessible(true);
        return (T)null.newInstance(param1ArrayOfObject);
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot instantiate class: ");
        stringBuilder.append(param1String);
        Log.w("SupportMenuInflater", stringBuilder.toString(), exception);
        return null;
      } 
    }
    
    private void setItem(MenuItem param1MenuItem) {
      MenuItem menuItem = param1MenuItem.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled);
      int i = this.itemCheckable;
      boolean bool = false;
      if (i >= 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      menuItem.setCheckable(bool1).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId);
      if (this.itemShowAsAction >= 0)
        param1MenuItem.setShowAsAction(this.itemShowAsAction); 
      if (this.itemListenerMethodName != null)
        if (!SupportMenuInflater.this.mContext.isRestricted()) {
          param1MenuItem.setOnMenuItemClickListener(new SupportMenuInflater.InflatedOnMenuItemClickListener(SupportMenuInflater.this.getRealOwner(), this.itemListenerMethodName));
        } else {
          throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
        }  
      boolean bool1 = param1MenuItem instanceof MenuItemImpl;
      if (bool1)
        MenuItemImpl menuItemImpl = (MenuItemImpl)param1MenuItem; 
      if (this.itemCheckable >= 2)
        if (bool1) {
          ((MenuItemImpl)param1MenuItem).setExclusiveCheckable(true);
        } else if (param1MenuItem instanceof MenuItemWrapperICS) {
          ((MenuItemWrapperICS)param1MenuItem).setExclusiveCheckable(true);
        }  
      if (this.itemActionViewClassName != null) {
        param1MenuItem.setActionView(newInstance(this.itemActionViewClassName, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
        bool = true;
      } 
      if (this.itemActionViewLayout > 0)
        if (!bool) {
          param1MenuItem.setActionView(this.itemActionViewLayout);
        } else {
          Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
        }  
      if (this.itemActionProvider != null)
        MenuItemCompat.setActionProvider(param1MenuItem, this.itemActionProvider); 
      MenuItemCompat.setContentDescription(param1MenuItem, this.itemContentDescription);
      MenuItemCompat.setTooltipText(param1MenuItem, this.itemTooltipText);
      MenuItemCompat.setAlphabeticShortcut(param1MenuItem, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
      MenuItemCompat.setNumericShortcut(param1MenuItem, this.itemNumericShortcut, this.itemNumericModifiers);
      if (this.itemIconTintMode != null)
        MenuItemCompat.setIconTintMode(param1MenuItem, this.itemIconTintMode); 
      if (this.itemIconTintList != null)
        MenuItemCompat.setIconTintList(param1MenuItem, this.itemIconTintList); 
    }
    
    public void addItem() {
      this.itemAdded = true;
      setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
    }
    
    public SubMenu addSubMenuItem() {
      this.itemAdded = true;
      SubMenu subMenu = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
      setItem(subMenu.getItem());
      return subMenu;
    }
    
    public boolean hasAddedItem() {
      return this.itemAdded;
    }
    
    public void readGroup(AttributeSet param1AttributeSet) {
      TypedArray typedArray = SupportMenuInflater.this.mContext.obtainStyledAttributes(param1AttributeSet, R.styleable.MenuGroup);
      this.groupId = typedArray.getResourceId(R.styleable.MenuGroup_android_id, 0);
      this.groupCategory = typedArray.getInt(R.styleable.MenuGroup_android_menuCategory, 0);
      this.groupOrder = typedArray.getInt(R.styleable.MenuGroup_android_orderInCategory, 0);
      this.groupCheckable = typedArray.getInt(R.styleable.MenuGroup_android_checkableBehavior, 0);
      this.groupVisible = typedArray.getBoolean(R.styleable.MenuGroup_android_visible, true);
      this.groupEnabled = typedArray.getBoolean(R.styleable.MenuGroup_android_enabled, true);
      typedArray.recycle();
    }
    
    public void readItem(AttributeSet param1AttributeSet) {
      boolean bool;
      TypedArray typedArray = SupportMenuInflater.this.mContext.obtainStyledAttributes(param1AttributeSet, R.styleable.MenuItem);
      this.itemId = typedArray.getResourceId(R.styleable.MenuItem_android_id, 0);
      this.itemCategoryOrder = typedArray.getInt(R.styleable.MenuItem_android_menuCategory, this.groupCategory) & 0xFFFF0000 | typedArray.getInt(R.styleable.MenuItem_android_orderInCategory, this.groupOrder) & 0xFFFF;
      this.itemTitle = typedArray.getText(R.styleable.MenuItem_android_title);
      this.itemTitleCondensed = typedArray.getText(R.styleable.MenuItem_android_titleCondensed);
      this.itemIconResId = typedArray.getResourceId(R.styleable.MenuItem_android_icon, 0);
      this.itemAlphabeticShortcut = getShortcut(typedArray.getString(R.styleable.MenuItem_android_alphabeticShortcut));
      this.itemAlphabeticModifiers = typedArray.getInt(R.styleable.MenuItem_alphabeticModifiers, 4096);
      this.itemNumericShortcut = getShortcut(typedArray.getString(R.styleable.MenuItem_android_numericShortcut));
      this.itemNumericModifiers = typedArray.getInt(R.styleable.MenuItem_numericModifiers, 4096);
      if (typedArray.hasValue(R.styleable.MenuItem_android_checkable)) {
        this.itemCheckable = typedArray.getBoolean(R.styleable.MenuItem_android_checkable, false);
      } else {
        this.itemCheckable = this.groupCheckable;
      } 
      this.itemChecked = typedArray.getBoolean(R.styleable.MenuItem_android_checked, false);
      this.itemVisible = typedArray.getBoolean(R.styleable.MenuItem_android_visible, this.groupVisible);
      this.itemEnabled = typedArray.getBoolean(R.styleable.MenuItem_android_enabled, this.groupEnabled);
      this.itemShowAsAction = typedArray.getInt(R.styleable.MenuItem_showAsAction, -1);
      this.itemListenerMethodName = typedArray.getString(R.styleable.MenuItem_android_onClick);
      this.itemActionViewLayout = typedArray.getResourceId(R.styleable.MenuItem_actionLayout, 0);
      this.itemActionViewClassName = typedArray.getString(R.styleable.MenuItem_actionViewClass);
      this.itemActionProviderClassName = typedArray.getString(R.styleable.MenuItem_actionProviderClass);
      if (this.itemActionProviderClassName != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
        this.itemActionProvider = newInstance(this.itemActionProviderClassName, SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionProviderConstructorArguments);
      } else {
        if (bool)
          Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified."); 
        this.itemActionProvider = null;
      } 
      this.itemContentDescription = typedArray.getText(R.styleable.MenuItem_contentDescription);
      this.itemTooltipText = typedArray.getText(R.styleable.MenuItem_tooltipText);
      if (typedArray.hasValue(R.styleable.MenuItem_iconTintMode)) {
        this.itemIconTintMode = DrawableUtils.parseTintMode(typedArray.getInt(R.styleable.MenuItem_iconTintMode, -1), this.itemIconTintMode);
      } else {
        this.itemIconTintMode = null;
      } 
      if (typedArray.hasValue(R.styleable.MenuItem_iconTint)) {
        this.itemIconTintList = typedArray.getColorStateList(R.styleable.MenuItem_iconTint);
      } else {
        this.itemIconTintList = null;
      } 
      typedArray.recycle();
      this.itemAdded = false;
    }
    
    public void resetGroup() {
      this.groupId = 0;
      this.groupCategory = 0;
      this.groupOrder = 0;
      this.groupCheckable = 0;
      this.groupVisible = true;
      this.groupEnabled = true;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\view\SupportMenuInflater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */