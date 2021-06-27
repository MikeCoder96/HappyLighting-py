package com.android.soundrecorder.sound;

public class Complex {
  private double image;
  
  private double real;
  
  public Complex() {
    this(0.0D, 0.0D);
  }
  
  public Complex(double paramDouble1, double paramDouble2) {
    this.real = paramDouble1;
    this.image = paramDouble2;
  }
  
  public double getImage() {
    return this.image;
  }
  
  public final double getMod() {
    return Math.sqrt(getReal() * getReal() + getImage() * getImage());
  }
  
  public double getReal() {
    return this.real;
  }
  
  public Complex minus(Complex paramComplex) {
    return new Complex(getReal() - paramComplex.getReal(), getImage() - paramComplex.getImage());
  }
  
  public Complex multiple(Complex paramComplex) {
    return new Complex(getReal() * paramComplex.getReal() - getImage() * paramComplex.getImage(), getReal() * paramComplex.getImage() + getImage() * paramComplex.getReal());
  }
  
  public Complex plus(Complex paramComplex) {
    return new Complex(getReal() + paramComplex.getReal(), getImage() + paramComplex.getImage());
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Complex{real=");
    stringBuilder.append(this.real);
    stringBuilder.append(", image=");
    stringBuilder.append(this.image);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\sound\Complex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */