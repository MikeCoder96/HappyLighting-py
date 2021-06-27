package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import java.util.HashSet;

public class ConstraintAnchor {
  private static final boolean ALLOW_BINARY = false;
  
  public static final int AUTO_CONSTRAINT_CREATOR = 2;
  
  public static final int SCOUT_CREATOR = 1;
  
  private static final int UNSET_GONE_MARGIN = -1;
  
  public static final int USER_CREATOR = 0;
  
  private int mConnectionCreator = 0;
  
  private ConnectionType mConnectionType = ConnectionType.RELAXED;
  
  int mGoneMargin = -1;
  
  public int mMargin = 0;
  
  final ConstraintWidget mOwner;
  
  private ResolutionAnchor mResolutionAnchor = new ResolutionAnchor(this);
  
  SolverVariable mSolverVariable;
  
  private Strength mStrength = Strength.NONE;
  
  ConstraintAnchor mTarget;
  
  final Type mType;
  
  public ConstraintAnchor(ConstraintWidget paramConstraintWidget, Type paramType) {
    this.mOwner = paramConstraintWidget;
    this.mType = paramType;
  }
  
  private boolean isConnectionToMe(ConstraintWidget paramConstraintWidget, HashSet<ConstraintWidget> paramHashSet) {
    if (paramHashSet.contains(paramConstraintWidget))
      return false; 
    paramHashSet.add(paramConstraintWidget);
    if (paramConstraintWidget == getOwner())
      return true; 
    ArrayList<ConstraintAnchor> arrayList = paramConstraintWidget.getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && isConnectionToMe(constraintAnchor.getTarget().getOwner(), paramHashSet))
        return true; 
    } 
    return false;
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt) {
    return connect(paramConstraintAnchor, paramInt, -1, Strength.STRONG, 0, false);
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, int paramInt2) {
    return connect(paramConstraintAnchor, paramInt1, -1, Strength.STRONG, paramInt2, false);
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, int paramInt2, Strength paramStrength, int paramInt3, boolean paramBoolean) {
    if (paramConstraintAnchor == null) {
      this.mTarget = null;
      this.mMargin = 0;
      this.mGoneMargin = -1;
      this.mStrength = Strength.NONE;
      this.mConnectionCreator = 2;
      return true;
    } 
    if (!paramBoolean && !isValidConnection(paramConstraintAnchor))
      return false; 
    this.mTarget = paramConstraintAnchor;
    if (paramInt1 > 0) {
      this.mMargin = paramInt1;
    } else {
      this.mMargin = 0;
    } 
    this.mGoneMargin = paramInt2;
    this.mStrength = paramStrength;
    this.mConnectionCreator = paramInt3;
    return true;
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, Strength paramStrength, int paramInt2) {
    return connect(paramConstraintAnchor, paramInt1, -1, paramStrength, paramInt2, false);
  }
  
  public int getConnectionCreator() {
    return this.mConnectionCreator;
  }
  
  public ConnectionType getConnectionType() {
    return this.mConnectionType;
  }
  
  public int getMargin() {
    return (this.mOwner.getVisibility() == 8) ? 0 : ((this.mGoneMargin > -1 && this.mTarget != null && this.mTarget.mOwner.getVisibility() == 8) ? this.mGoneMargin : this.mMargin);
  }
  
  public final ConstraintAnchor getOpposite() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case BOTTOM:
        return this.mOwner.mTop;
      case TOP:
        return this.mOwner.mBottom;
      case RIGHT:
        return this.mOwner.mLeft;
      case LEFT:
        return this.mOwner.mRight;
      case CENTER:
      case BASELINE:
      case CENTER_X:
      case CENTER_Y:
      case NONE:
        break;
    } 
    return null;
  }
  
  public ConstraintWidget getOwner() {
    return this.mOwner;
  }
  
  public int getPriorityLevel() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case NONE:
        return 0;
      case CENTER_Y:
        return 0;
      case CENTER_X:
        return 0;
      case BASELINE:
        return 1;
      case BOTTOM:
        return 2;
      case TOP:
        return 2;
      case RIGHT:
        return 2;
      case LEFT:
        return 2;
      case CENTER:
        break;
    } 
    return 2;
  }
  
  public ResolutionAnchor getResolutionNode() {
    return this.mResolutionAnchor;
  }
  
  public int getSnapPriorityLevel() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case NONE:
        return 0;
      case CENTER_Y:
        return 1;
      case CENTER_X:
        return 0;
      case BASELINE:
        return 2;
      case BOTTOM:
        return 0;
      case TOP:
        return 0;
      case RIGHT:
        return 1;
      case LEFT:
        return 1;
      case CENTER:
        break;
    } 
    return 3;
  }
  
  public SolverVariable getSolverVariable() {
    return this.mSolverVariable;
  }
  
  public Strength getStrength() {
    return this.mStrength;
  }
  
  public ConstraintAnchor getTarget() {
    return this.mTarget;
  }
  
  public Type getType() {
    return this.mType;
  }
  
  public boolean isConnected() {
    boolean bool;
    if (this.mTarget != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget) {
    if (isConnectionToMe(paramConstraintWidget, new HashSet<ConstraintWidget>()))
      return false; 
    ConstraintWidget constraintWidget = getOwner().getParent();
    return (constraintWidget == paramConstraintWidget) ? true : ((paramConstraintWidget.getParent() == constraintWidget));
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget, ConstraintAnchor paramConstraintAnchor) {
    return isConnectionAllowed(paramConstraintWidget);
  }
  
  public boolean isSideAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case LEFT:
      case RIGHT:
      case TOP:
      case BOTTOM:
        return true;
      case CENTER:
      case BASELINE:
      case CENTER_X:
      case CENTER_Y:
      case NONE:
        break;
    } 
    return false;
  }
  
  public boolean isSimilarDimensionConnection(ConstraintAnchor paramConstraintAnchor) {
    Type type2 = paramConstraintAnchor.getType();
    Type type1 = this.mType;
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    if (type2 == type1)
      return true; 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case NONE:
        return false;
      case TOP:
      case BOTTOM:
      case BASELINE:
      case CENTER_Y:
        bool2 = bool3;
        if (type2 != Type.TOP) {
          bool2 = bool3;
          if (type2 != Type.BOTTOM) {
            bool2 = bool3;
            if (type2 != Type.CENTER_Y)
              if (type2 == Type.BASELINE) {
                bool2 = bool3;
              } else {
                bool2 = false;
              }  
          } 
        } 
        return bool2;
      case LEFT:
      case RIGHT:
      case CENTER_X:
        bool2 = bool1;
        if (type2 != Type.LEFT) {
          bool2 = bool1;
          if (type2 != Type.RIGHT)
            if (type2 == Type.CENTER_X) {
              bool2 = bool1;
            } else {
              bool2 = false;
            }  
        } 
        return bool2;
      case CENTER:
        break;
    } 
    if (type2 == Type.BASELINE)
      bool2 = false; 
    return bool2;
  }
  
  public boolean isSnapCompatibleWith(ConstraintAnchor paramConstraintAnchor) {
    int i;
    if (this.mType == Type.CENTER)
      return false; 
    if (this.mType == paramConstraintAnchor.getType())
      return true; 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case CENTER_Y:
        switch (paramConstraintAnchor.getType()) {
          default:
            return false;
          case BOTTOM:
            return true;
          case TOP:
            break;
        } 
        return true;
      case CENTER_X:
        switch (paramConstraintAnchor.getType()) {
          default:
            return false;
          case RIGHT:
            return true;
          case LEFT:
            break;
        } 
        return true;
      case BOTTOM:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 4) ? (!(i != 8)) : true;
      case TOP:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 5) ? (!(i != 8)) : true;
      case RIGHT:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 2) ? (!(i != 7)) : true;
      case LEFT:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 3) ? (!(i != 7)) : true;
      case CENTER:
      case BASELINE:
      case NONE:
        break;
    } 
    return false;
  }
  
  public boolean isValidConnection(ConstraintAnchor paramConstraintAnchor) {
    boolean bool1 = false;
    if (paramConstraintAnchor == null)
      return false; 
    Type type = paramConstraintAnchor.getType();
    if (type == this.mType)
      return !(this.mType == Type.BASELINE && (!paramConstraintAnchor.getOwner().hasBaseline() || !getOwner().hasBaseline())); 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case BASELINE:
      case CENTER_X:
      case CENTER_Y:
      case NONE:
        return false;
      case TOP:
      case BOTTOM:
        if (type == Type.TOP || type == Type.BOTTOM) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        bool2 = bool1;
        if (paramConstraintAnchor.getOwner() instanceof Guideline) {
          if (bool1 || type == Type.CENTER_Y)
            return true; 
          bool2 = false;
        } 
        return bool2;
      case LEFT:
      case RIGHT:
        if (type == Type.LEFT || type == Type.RIGHT) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        bool2 = bool1;
        if (paramConstraintAnchor.getOwner() instanceof Guideline) {
          if (bool1 || type == Type.CENTER_X)
            return true; 
          bool2 = false;
        } 
        return bool2;
      case CENTER:
        break;
    } 
    boolean bool2 = bool1;
    if (type != Type.BASELINE) {
      bool2 = bool1;
      if (type != Type.CENTER_X) {
        bool2 = bool1;
        if (type != Type.CENTER_Y)
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  public boolean isVerticalAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case TOP:
      case BOTTOM:
      case BASELINE:
      case CENTER_Y:
      case NONE:
        return true;
      case CENTER:
      case LEFT:
      case RIGHT:
      case CENTER_X:
        break;
    } 
    return false;
  }
  
  public void reset() {
    this.mTarget = null;
    this.mMargin = 0;
    this.mGoneMargin = -1;
    this.mStrength = Strength.STRONG;
    this.mConnectionCreator = 0;
    this.mConnectionType = ConnectionType.RELAXED;
    this.mResolutionAnchor.reset();
  }
  
  public void resetSolverVariable(Cache paramCache) {
    if (this.mSolverVariable == null) {
      this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, null);
    } else {
      this.mSolverVariable.reset();
    } 
  }
  
  public void setConnectionCreator(int paramInt) {
    this.mConnectionCreator = paramInt;
  }
  
  public void setConnectionType(ConnectionType paramConnectionType) {
    this.mConnectionType = paramConnectionType;
  }
  
  public void setGoneMargin(int paramInt) {
    if (isConnected())
      this.mGoneMargin = paramInt; 
  }
  
  public void setMargin(int paramInt) {
    if (isConnected())
      this.mMargin = paramInt; 
  }
  
  public void setStrength(Strength paramStrength) {
    if (isConnected())
      this.mStrength = paramStrength; 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.mOwner.getDebugName());
    stringBuilder.append(":");
    stringBuilder.append(this.mType.toString());
    return stringBuilder.toString();
  }
  
  public enum ConnectionType {
    RELAXED, STRICT;
    
    static {
    
    }
  }
  
  public enum Strength {
    NONE, STRONG, WEAK;
    
    static {
    
    }
  }
  
  public enum Type {
    NONE, RIGHT, TOP, BASELINE, BOTTOM, CENTER, CENTER_X, CENTER_Y, LEFT;
    
    static {
      RIGHT = new Type("RIGHT", 3);
      BOTTOM = new Type("BOTTOM", 4);
      BASELINE = new Type("BASELINE", 5);
      CENTER = new Type("CENTER", 6);
      CENTER_X = new Type("CENTER_X", 7);
      CENTER_Y = new Type("CENTER_Y", 8);
      $VALUES = new Type[] { NONE, LEFT, TOP, RIGHT, BOTTOM, BASELINE, CENTER, CENTER_X, CENTER_Y };
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintAnchor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */