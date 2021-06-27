package android.support.v4.graphics;

import android.graphics.Path;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class PathParser {
  private static final String LOGTAG = "PathParser";
  
  private static void addNode(ArrayList<PathDataNode> paramArrayList, char paramChar, float[] paramArrayOffloat) {
    paramArrayList.add(new PathDataNode(paramChar, paramArrayOffloat));
  }
  
  public static boolean canMorph(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    if (paramArrayOfPathDataNode1 == null || paramArrayOfPathDataNode2 == null)
      return false; 
    if (paramArrayOfPathDataNode1.length != paramArrayOfPathDataNode2.length)
      return false; 
    for (byte b = 0; b < paramArrayOfPathDataNode1.length; b++) {
      if ((paramArrayOfPathDataNode1[b]).mType != (paramArrayOfPathDataNode2[b]).mType || (paramArrayOfPathDataNode1[b]).mParams.length != (paramArrayOfPathDataNode2[b]).mParams.length)
        return false; 
    } 
    return true;
  }
  
  static float[] copyOfRange(float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      int i = paramArrayOffloat.length;
      if (paramInt1 >= 0 && paramInt1 <= i) {
        paramInt2 -= paramInt1;
        i = Math.min(paramInt2, i - paramInt1);
        float[] arrayOfFloat = new float[paramInt2];
        System.arraycopy(paramArrayOffloat, paramInt1, arrayOfFloat, 0, i);
        return arrayOfFloat;
      } 
      throw new ArrayIndexOutOfBoundsException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static PathDataNode[] createNodesFromPathData(String paramString) {
    if (paramString == null)
      return null; 
    ArrayList<PathDataNode> arrayList = new ArrayList();
    int i = 1;
    int j = 0;
    while (i < paramString.length()) {
      i = nextStart(paramString, i);
      String str = paramString.substring(j, i).trim();
      if (str.length() > 0) {
        float[] arrayOfFloat = getFloats(str);
        addNode(arrayList, str.charAt(0), arrayOfFloat);
      } 
      j = i;
      i++;
    } 
    if (i - j == 1 && j < paramString.length())
      addNode(arrayList, paramString.charAt(j), new float[0]); 
    return arrayList.<PathDataNode>toArray(new PathDataNode[arrayList.size()]);
  }
  
  public static Path createPathFromPathData(String paramString) {
    Path path = new Path();
    PathDataNode[] arrayOfPathDataNode = createNodesFromPathData(paramString);
    if (arrayOfPathDataNode != null)
      try {
        PathDataNode.nodesToPath(arrayOfPathDataNode, path);
        return path;
      } catch (RuntimeException runtimeException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error in parsing ");
        stringBuilder.append(paramString);
        throw new RuntimeException(stringBuilder.toString(), runtimeException);
      }  
    return null;
  }
  
  public static PathDataNode[] deepCopyNodes(PathDataNode[] paramArrayOfPathDataNode) {
    if (paramArrayOfPathDataNode == null)
      return null; 
    PathDataNode[] arrayOfPathDataNode = new PathDataNode[paramArrayOfPathDataNode.length];
    for (byte b = 0; b < paramArrayOfPathDataNode.length; b++)
      arrayOfPathDataNode[b] = new PathDataNode(paramArrayOfPathDataNode[b]); 
    return arrayOfPathDataNode;
  }
  
  private static void extract(String paramString, int paramInt, ExtractFloatResult paramExtractFloatResult) {
    paramExtractFloatResult.mEndWithNegOrDot = false;
    int i = paramInt;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    while (i < paramString.length()) {
      char c = paramString.charAt(i);
      if (c != ' ') {
        if (c != 'E' && c != 'e') {
          switch (c) {
            default:
              bool1 = false;
              break;
            case '.':
              if (!bool2) {
                bool1 = false;
                bool2 = true;
                break;
              } 
              paramExtractFloatResult.mEndWithNegOrDot = true;
            case '-':
            
            case ',':
              bool1 = false;
              bool3 = true;
              break;
          } 
        } else {
          bool1 = true;
        } 
        if (bool3)
          break; 
        continue;
      } 
      i++;
    } 
    paramExtractFloatResult.mEndPosition = i;
  }
  
  private static float[] getFloats(String paramString) {
    if (paramString.charAt(0) == 'z' || paramString.charAt(0) == 'Z')
      return new float[0]; 
    try {
      float[] arrayOfFloat = new float[paramString.length()];
      ExtractFloatResult extractFloatResult = new ExtractFloatResult();
      this();
      int i = paramString.length();
      int j = 1;
      int k;
      for (k = 0; j < i; k = n) {
        extract(paramString, j, extractFloatResult);
        int m = extractFloatResult.mEndPosition;
        int n = k;
        if (j < m) {
          arrayOfFloat[k] = Float.parseFloat(paramString.substring(j, m));
          n = k + 1;
        } 
        if (extractFloatResult.mEndWithNegOrDot) {
          j = m;
          k = n;
          continue;
        } 
        j = m + 1;
      } 
      return copyOfRange(arrayOfFloat, 0, k);
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("error in parsing \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\"");
      throw new RuntimeException(stringBuilder.toString(), numberFormatException);
    } 
  }
  
  private static int nextStart(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E')
        return paramInt; 
      paramInt++;
    } 
    return paramInt;
  }
  
  public static void updateNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    for (byte b = 0; b < paramArrayOfPathDataNode2.length; b++) {
      (paramArrayOfPathDataNode1[b]).mType = (char)(paramArrayOfPathDataNode2[b]).mType;
      for (byte b1 = 0; b1 < (paramArrayOfPathDataNode2[b]).mParams.length; b1++)
        (paramArrayOfPathDataNode1[b]).mParams[b1] = (paramArrayOfPathDataNode2[b]).mParams[b1]; 
    } 
  }
  
  private static class ExtractFloatResult {
    int mEndPosition;
    
    boolean mEndWithNegOrDot;
  }
  
  public static class PathDataNode {
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public float[] mParams;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public char mType;
    
    PathDataNode(char param1Char, float[] param1ArrayOffloat) {
      this.mType = (char)param1Char;
      this.mParams = param1ArrayOffloat;
    }
    
    PathDataNode(PathDataNode param1PathDataNode) {
      this.mType = (char)param1PathDataNode.mType;
      this.mParams = PathParser.copyOfRange(param1PathDataNode.mParams, 0, param1PathDataNode.mParams.length);
    }
    
    private static void addCommand(Path param1Path, float[] param1ArrayOffloat1, int param1Char1, int param1Char2, float[] param1ArrayOffloat2) {
      int i;
      byte b;
      float f1 = param1ArrayOffloat1[0];
      float f2 = param1ArrayOffloat1[1];
      float f3 = param1ArrayOffloat1[2];
      float f4 = param1ArrayOffloat1[3];
      float f5 = param1ArrayOffloat1[4];
      float f6 = param1ArrayOffloat1[5];
      float f7 = f1;
      float f8 = f2;
      float f9 = f3;
      float f10 = f4;
      switch (param1Char2) {
        default:
          f10 = f4;
          f9 = f3;
          f8 = f2;
          f7 = f1;
        case 'L':
        case 'M':
        case 'T':
        case 'l':
        case 'm':
        case 't':
          b = 2;
          break;
        case 'Z':
        case 'z':
          param1Path.close();
          param1Path.moveTo(f5, f6);
          f7 = f5;
          f9 = f7;
          f8 = f6;
          f10 = f8;
        case 'Q':
        case 'S':
        case 'q':
        case 's':
          b = 4;
          f7 = f1;
          f8 = f2;
          f9 = f3;
          f10 = f4;
          break;
        case 'H':
        case 'V':
        case 'h':
        case 'v':
          b = 1;
          f7 = f1;
          f8 = f2;
          f9 = f3;
          f10 = f4;
          break;
        case 'C':
        case 'c':
          b = 6;
          f7 = f1;
          f8 = f2;
          f9 = f3;
          f10 = f4;
          break;
        case 'A':
        case 'a':
          b = 7;
          f10 = f4;
          f9 = f3;
          f8 = f2;
          f7 = f1;
          break;
      } 
      int j = 0;
      int k = param1Char1;
      param1Char1 = j;
      while (i < param1ArrayOffloat2.length) {
        int m;
        int n;
        int i1;
        int i2;
        boolean bool1;
        boolean bool2;
        f1 = 0.0F;
        switch (param1Char2) {
          case 'v':
            k = i + 0;
            param1Path.rLineTo(0.0F, param1ArrayOffloat2[k]);
            f8 += param1ArrayOffloat2[k];
            break;
          case 't':
            if (k == 113 || k == 116 || k == 81 || k == 84) {
              f9 = f7 - f9;
              f10 = f8 - f10;
            } else {
              f10 = 0.0F;
              f9 = f1;
            } 
            j = i + 0;
            f1 = param1ArrayOffloat2[j];
            n = i + 1;
            param1Path.rQuadTo(f9, f10, f1, param1ArrayOffloat2[n]);
            f1 = f7 + param1ArrayOffloat2[j];
            f3 = f8 + param1ArrayOffloat2[n];
            f10 += f8;
            f9 += f7;
            f8 = f3;
            f7 = f1;
            break;
          case 's':
            if (n == 99 || n == 115 || n == 67 || n == 83) {
              f1 = f8 - f10;
              f10 = f7 - f9;
              f9 = f1;
            } else {
              f10 = 0.0F;
              f9 = 0.0F;
            } 
            i1 = i + 0;
            f3 = param1ArrayOffloat2[i1];
            m = i + 1;
            f4 = param1ArrayOffloat2[m];
            n = i + 2;
            f1 = param1ArrayOffloat2[n];
            i2 = i + 3;
            param1Path.rCubicTo(f10, f9, f3, f4, f1, param1ArrayOffloat2[i2]);
            f9 = param1ArrayOffloat2[i1] + f7;
            f10 = param1ArrayOffloat2[m] + f8;
            f7 += param1ArrayOffloat2[n];
            f8 += param1ArrayOffloat2[i2];
            break;
          case 'q':
            m = i + 0;
            f9 = param1ArrayOffloat2[m];
            n = i + 1;
            f10 = param1ArrayOffloat2[n];
            i2 = i + 2;
            f1 = param1ArrayOffloat2[i2];
            i1 = i + 3;
            param1Path.rQuadTo(f9, f10, f1, param1ArrayOffloat2[i1]);
            f9 = param1ArrayOffloat2[m] + f7;
            f10 = param1ArrayOffloat2[n] + f8;
            f7 += param1ArrayOffloat2[i2];
            f8 += param1ArrayOffloat2[i1];
            break;
          case 'm':
            m = i + 0;
            f7 += param1ArrayOffloat2[m];
            n = i + 1;
            f8 += param1ArrayOffloat2[n];
            if (i > 0) {
              param1Path.rLineTo(param1ArrayOffloat2[m], param1ArrayOffloat2[n]);
              break;
            } 
            param1Path.rMoveTo(param1ArrayOffloat2[m], param1ArrayOffloat2[n]);
            f6 = f8;
            f5 = f7;
            break;
          case 'l':
            n = i + 0;
            f1 = param1ArrayOffloat2[n];
            m = i + 1;
            param1Path.rLineTo(f1, param1ArrayOffloat2[m]);
            f7 += param1ArrayOffloat2[n];
            f8 += param1ArrayOffloat2[m];
            break;
          case 'h':
            n = i + 0;
            param1Path.rLineTo(param1ArrayOffloat2[n], 0.0F);
            f7 += param1ArrayOffloat2[n];
            break;
          case 'c':
            f4 = param1ArrayOffloat2[i + 0];
            f10 = param1ArrayOffloat2[i + 1];
            i2 = i + 2;
            f1 = param1ArrayOffloat2[i2];
            m = i + 3;
            f3 = param1ArrayOffloat2[m];
            i1 = i + 4;
            f9 = param1ArrayOffloat2[i1];
            n = i + 5;
            param1Path.rCubicTo(f4, f10, f1, f3, f9, param1ArrayOffloat2[n]);
            f9 = param1ArrayOffloat2[i2] + f7;
            f10 = param1ArrayOffloat2[m] + f8;
            f7 += param1ArrayOffloat2[i1];
            f8 += param1ArrayOffloat2[n];
            break;
          case 'a':
            n = i + 5;
            f9 = param1ArrayOffloat2[n];
            m = i + 6;
            f1 = param1ArrayOffloat2[m];
            f4 = param1ArrayOffloat2[i + 0];
            f10 = param1ArrayOffloat2[i + 1];
            f3 = param1ArrayOffloat2[i + 2];
            if (param1ArrayOffloat2[i + 3] != 0.0F) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (param1ArrayOffloat2[i + 4] != 0.0F) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            drawArc(param1Path, f7, f8, f9 + f7, f1 + f8, f4, f10, f3, bool1, bool2);
            f7 += param1ArrayOffloat2[n];
            f8 += param1ArrayOffloat2[m];
            f10 = f8;
            f9 = f7;
            break;
          case 'V':
            n = i + 0;
            param1Path.lineTo(f7, param1ArrayOffloat2[n]);
            f8 = param1ArrayOffloat2[n];
            break;
          case 'T':
            f1 = f8;
            f3 = f7;
            m = i;
          case 'S':
            m = i;
            if (n == 99 || n == 115 || n == 67 || n == 83) {
              f8 = f8 * 2.0F - f10;
              f9 = f7 * 2.0F - f9;
              f7 = f8;
              f8 = f9;
            } else {
              f9 = f7;
              f7 = f8;
              f8 = f9;
            } 
            i2 = m + 0;
            f9 = param1ArrayOffloat2[i2];
            n = m + 1;
            f1 = param1ArrayOffloat2[n];
            i1 = m + 2;
            f10 = param1ArrayOffloat2[i1];
            m += 3;
            param1Path.cubicTo(f8, f7, f9, f1, f10, param1ArrayOffloat2[m]);
            f10 = param1ArrayOffloat2[i2];
            f9 = param1ArrayOffloat2[n];
            f7 = param1ArrayOffloat2[i1];
            f8 = param1ArrayOffloat2[m];
            f1 = f9;
            f9 = f10;
            f10 = f1;
            break;
          case 'Q':
            n = i;
            i2 = n + 0;
            f8 = param1ArrayOffloat2[i2];
            m = n + 1;
            f9 = param1ArrayOffloat2[m];
            i1 = n + 2;
            f7 = param1ArrayOffloat2[i1];
            n += 3;
            param1Path.quadTo(f8, f9, f7, param1ArrayOffloat2[n]);
            f10 = param1ArrayOffloat2[i2];
            f9 = param1ArrayOffloat2[m];
            f7 = param1ArrayOffloat2[i1];
            f8 = param1ArrayOffloat2[n];
            f1 = f9;
            f9 = f10;
            f10 = f1;
            break;
          case 'M':
            n = i;
            m = n + 0;
            f7 = param1ArrayOffloat2[m];
            i2 = n + 1;
            f8 = param1ArrayOffloat2[i2];
            if (n > 0) {
              param1Path.lineTo(param1ArrayOffloat2[m], param1ArrayOffloat2[i2]);
              break;
            } 
            param1Path.moveTo(param1ArrayOffloat2[m], param1ArrayOffloat2[i2]);
            f6 = f8;
            f5 = f7;
            break;
          case 'L':
            n = i;
            m = n + 0;
            f8 = param1ArrayOffloat2[m];
            param1Path.lineTo(f8, param1ArrayOffloat2[++n]);
            f7 = param1ArrayOffloat2[m];
            f8 = param1ArrayOffloat2[n];
            break;
          case 'H':
            n = i + 0;
            param1Path.lineTo(param1ArrayOffloat2[n], f8);
            f7 = param1ArrayOffloat2[n];
            break;
          case 'C':
            n = i;
            f9 = param1ArrayOffloat2[n + 0];
            f1 = param1ArrayOffloat2[n + 1];
            i2 = n + 2;
            f7 = param1ArrayOffloat2[i2];
            m = n + 3;
            f10 = param1ArrayOffloat2[m];
            i1 = n + 4;
            f8 = param1ArrayOffloat2[i1];
            n += 5;
            param1Path.cubicTo(f9, f1, f7, f10, f8, param1ArrayOffloat2[n]);
            f7 = param1ArrayOffloat2[i1];
            f8 = param1ArrayOffloat2[n];
            f9 = param1ArrayOffloat2[i2];
            f10 = param1ArrayOffloat2[m];
            break;
          case 'A':
            n = i;
            i2 = n + 5;
            f9 = param1ArrayOffloat2[i2];
            m = n + 6;
            f3 = param1ArrayOffloat2[m];
            f4 = param1ArrayOffloat2[n + 0];
            f10 = param1ArrayOffloat2[n + 1];
            f1 = param1ArrayOffloat2[n + 2];
            if (param1ArrayOffloat2[n + 3] != 0.0F) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (param1ArrayOffloat2[n + 4] != 0.0F) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            drawArc(param1Path, f7, f8, f9, f3, f4, f10, f1, bool1, bool2);
            f7 = param1ArrayOffloat2[i2];
            f8 = param1ArrayOffloat2[m];
            f10 = f8;
            f9 = f7;
            break;
        } 
        continue;
        i = param1Char1 + b;
        k = param1Char2;
      } 
      param1ArrayOffloat1[0] = f7;
      param1ArrayOffloat1[1] = f8;
      param1ArrayOffloat1[2] = f9;
      param1ArrayOffloat1[3] = f10;
      param1ArrayOffloat1[4] = f5;
      param1ArrayOffloat1[5] = f6;
    }
    
    private static void arcToBezier(Path param1Path, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6, double param1Double7, double param1Double8, double param1Double9) {
      int i = (int)Math.ceil(Math.abs(param1Double9 * 4.0D / Math.PI));
      double d1 = Math.cos(param1Double7);
      double d2 = Math.sin(param1Double7);
      double d3 = Math.cos(param1Double8);
      double d4 = Math.sin(param1Double8);
      param1Double7 = -param1Double3;
      double d5 = param1Double7 * d1;
      double d6 = param1Double4 * d2;
      double d7 = param1Double7 * d2;
      param1Double7 = param1Double4 * d1;
      param1Double4 = i;
      Double.isNaN(param1Double4);
      double d8 = param1Double9 / param1Double4;
      byte b = 0;
      double d9 = d4 * d7 + d3 * param1Double7;
      param1Double4 = d5 * d4 - d6 * d3;
      d3 = param1Double8;
      d4 = param1Double6;
      param1Double8 = param1Double4;
      param1Double9 = param1Double5;
      param1Double4 = d2;
      param1Double5 = d1;
      param1Double6 = d7;
      d1 = d8;
      while (true) {
        d7 = param1Double3;
        if (b < i) {
          d2 = d3 + d1;
          double d10 = Math.sin(d2);
          double d11 = Math.cos(d2);
          d8 = param1Double1 + d7 * param1Double5 * d11 - d6 * d10;
          double d12 = param1Double2 + d7 * param1Double4 * d11 + param1Double7 * d10;
          d7 = d5 * d10 - d6 * d11;
          d10 = d10 * param1Double6 + d11 * param1Double7;
          d3 = d2 - d3;
          d11 = Math.tan(d3 / 2.0D);
          d3 = Math.sin(d3) * (Math.sqrt(d11 * 3.0D * d11 + 4.0D) - 1.0D) / 3.0D;
          param1Path.rLineTo(0.0F, 0.0F);
          param1Path.cubicTo((float)(param1Double9 + param1Double8 * d3), (float)(d4 + d9 * d3), (float)(d8 - d3 * d7), (float)(d12 - d3 * d10), (float)d8, (float)d12);
          b++;
          d4 = d12;
          param1Double9 = d8;
          d3 = d2;
          d9 = d10;
          param1Double8 = d7;
          continue;
        } 
        break;
      } 
    }
    
    private static void drawArc(Path param1Path, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, boolean param1Boolean1, boolean param1Boolean2) {
      double d1 = Math.toRadians(param1Float7);
      double d2 = Math.cos(d1);
      double d3 = Math.sin(d1);
      double d4 = param1Float1;
      Double.isNaN(d4);
      double d5 = param1Float2;
      Double.isNaN(d5);
      double d6 = param1Float5;
      Double.isNaN(d6);
      double d7 = (d4 * d2 + d5 * d3) / d6;
      double d8 = -param1Float1;
      Double.isNaN(d8);
      Double.isNaN(d5);
      double d9 = param1Float6;
      Double.isNaN(d9);
      double d10 = (d8 * d3 + d5 * d2) / d9;
      double d11 = param1Float3;
      Double.isNaN(d11);
      d8 = param1Float4;
      Double.isNaN(d8);
      Double.isNaN(d6);
      double d12 = (d11 * d2 + d8 * d3) / d6;
      d11 = -param1Float3;
      Double.isNaN(d11);
      Double.isNaN(d8);
      Double.isNaN(d9);
      double d13 = (d11 * d3 + d8 * d2) / d9;
      double d14 = d7 - d12;
      double d15 = d10 - d13;
      d11 = (d7 + d12) / 2.0D;
      d8 = (d10 + d13) / 2.0D;
      double d16 = d14 * d14 + d15 * d15;
      if (d16 == 0.0D) {
        Log.w("PathParser", " Points are coincident");
        return;
      } 
      double d17 = 1.0D / d16 - 0.25D;
      if (d17 < 0.0D) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Points are too far apart ");
        stringBuilder.append(d16);
        Log.w("PathParser", stringBuilder.toString());
        float f = (float)(Math.sqrt(d16) / 1.99999D);
        drawArc(param1Path, param1Float1, param1Float2, param1Float3, param1Float4, param1Float5 * f, param1Float6 * f, param1Float7, param1Boolean1, param1Boolean2);
        return;
      } 
      d17 = Math.sqrt(d17);
      d14 *= d17;
      d15 = d17 * d15;
      if (param1Boolean1 == param1Boolean2) {
        d11 -= d15;
        d8 += d14;
      } else {
        d11 += d15;
        d8 -= d14;
      } 
      d15 = Math.atan2(d10 - d8, d7 - d11);
      d10 = Math.atan2(d13 - d8, d12 - d11) - d15;
      if (d10 >= 0.0D) {
        param1Boolean1 = true;
      } else {
        param1Boolean1 = false;
      } 
      d7 = d10;
      if (param1Boolean2 != param1Boolean1)
        if (d10 > 0.0D) {
          d7 = d10 - 6.283185307179586D;
        } else {
          d7 = d10 + 6.283185307179586D;
        }  
      Double.isNaN(d6);
      d11 *= d6;
      Double.isNaN(d9);
      d8 *= d9;
      arcToBezier(param1Path, d11 * d2 - d8 * d3, d11 * d3 + d8 * d2, d6, d9, d4, d5, d1, d15, d7);
    }
    
    public static void nodesToPath(PathDataNode[] param1ArrayOfPathDataNode, Path param1Path) {
      float[] arrayOfFloat = new float[6];
      char c1 = 'm';
      byte b = 0;
      char c2;
      for (c2 = c1; b < param1ArrayOfPathDataNode.length; c2 = c1) {
        addCommand(param1Path, arrayOfFloat, c2, (param1ArrayOfPathDataNode[b]).mType, (param1ArrayOfPathDataNode[b]).mParams);
        c1 = (param1ArrayOfPathDataNode[b]).mType;
        b++;
      } 
    }
    
    public void interpolatePathDataNode(PathDataNode param1PathDataNode1, PathDataNode param1PathDataNode2, float param1Float) {
      for (byte b = 0; b < param1PathDataNode1.mParams.length; b++)
        this.mParams[b] = param1PathDataNode1.mParams[b] * (1.0F - param1Float) + param1PathDataNode2.mParams[b] * param1Float; 
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\graphics\PathParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */