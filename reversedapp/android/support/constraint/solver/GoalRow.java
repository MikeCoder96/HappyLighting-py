package android.support.constraint.solver;

public class GoalRow extends ArrayRow {
  public GoalRow(Cache paramCache) {
    super(paramCache);
  }
  
  public void addError(SolverVariable paramSolverVariable) {
    super.addError(paramSolverVariable);
    paramSolverVariable.usageInRowCount--;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\GoalRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */