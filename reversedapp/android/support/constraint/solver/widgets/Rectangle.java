package android.support.constraint.solver.widgets;

public class Rectangle {
  public int height;
  
  public int width;
  
  public int x;
  
  public int y;
  
  public boolean contains(int paramInt1, int paramInt2) {
    boolean bool;
    if (paramInt1 >= this.x && paramInt1 < this.x + this.width && paramInt2 >= this.y && paramInt2 < this.y + this.height) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getCenterX() {
    return (this.x + this.width) / 2;
  }
  
  public int getCenterY() {
    return (this.y + this.height) / 2;
  }
  
  void grow(int paramInt1, int paramInt2) {
    this.x -= paramInt1;
    this.y -= paramInt2;
    this.width += paramInt1 * 2;
    this.height += paramInt2 * 2;
  }
  
  boolean intersects(Rectangle paramRectangle) {
    boolean bool;
    if (this.x >= paramRectangle.x && this.x < paramRectangle.x + paramRectangle.width && this.y >= paramRectangle.y && this.y < paramRectangle.y + paramRectangle.height) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.x = paramInt1;
    this.y = paramInt2;
    this.width = paramInt3;
    this.height = paramInt4;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\Rectangle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */