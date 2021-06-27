package android.support.constraint.solver;

import java.util.ArrayList;

public class Metrics {
  public long additionalMeasures;
  
  public long barrierConnectionResolved;
  
  public long bfs;
  
  public long centerConnectionResolved;
  
  public long chainConnectionResolved;
  
  public long constraints;
  
  public long errors;
  
  public long extravariables;
  
  public long fullySolved;
  
  public long graphOptimizer;
  
  public long iterations;
  
  public long lastTableSize;
  
  public long matchConnectionResolved;
  
  public long maxRows;
  
  public long maxTableSize;
  
  public long maxVariables;
  
  public long measures;
  
  public long minimize;
  
  public long minimizeGoal;
  
  public long nonresolvedWidgets;
  
  public long oldresolvedWidgets;
  
  public long optimize;
  
  public long pivots;
  
  public ArrayList<String> problematicLayouts = new ArrayList<String>();
  
  public long resolutions;
  
  public long resolvedWidgets;
  
  public long simpleconstraints;
  
  public long slackvariables;
  
  public long tableSizeIncrease;
  
  public long variables;
  
  public void reset() {
    this.measures = 0L;
    this.additionalMeasures = 0L;
    this.resolutions = 0L;
    this.tableSizeIncrease = 0L;
    this.maxTableSize = 0L;
    this.lastTableSize = 0L;
    this.maxVariables = 0L;
    this.maxRows = 0L;
    this.minimize = 0L;
    this.minimizeGoal = 0L;
    this.constraints = 0L;
    this.simpleconstraints = 0L;
    this.optimize = 0L;
    this.iterations = 0L;
    this.pivots = 0L;
    this.bfs = 0L;
    this.variables = 0L;
    this.errors = 0L;
    this.slackvariables = 0L;
    this.extravariables = 0L;
    this.fullySolved = 0L;
    this.graphOptimizer = 0L;
    this.resolvedWidgets = 0L;
    this.oldresolvedWidgets = 0L;
    this.nonresolvedWidgets = 0L;
    this.centerConnectionResolved = 0L;
    this.matchConnectionResolved = 0L;
    this.chainConnectionResolved = 0L;
    this.barrierConnectionResolved = 0L;
    this.problematicLayouts.clear();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\n*** Metrics ***\nmeasures: ");
    stringBuilder.append(this.measures);
    stringBuilder.append("\nadditionalMeasures: ");
    stringBuilder.append(this.additionalMeasures);
    stringBuilder.append("\nresolutions passes: ");
    stringBuilder.append(this.resolutions);
    stringBuilder.append("\ntable increases: ");
    stringBuilder.append(this.tableSizeIncrease);
    stringBuilder.append("\nmaxTableSize: ");
    stringBuilder.append(this.maxTableSize);
    stringBuilder.append("\nmaxVariables: ");
    stringBuilder.append(this.maxVariables);
    stringBuilder.append("\nmaxRows: ");
    stringBuilder.append(this.maxRows);
    stringBuilder.append("\n\nminimize: ");
    stringBuilder.append(this.minimize);
    stringBuilder.append("\nminimizeGoal: ");
    stringBuilder.append(this.minimizeGoal);
    stringBuilder.append("\nconstraints: ");
    stringBuilder.append(this.constraints);
    stringBuilder.append("\nsimpleconstraints: ");
    stringBuilder.append(this.simpleconstraints);
    stringBuilder.append("\noptimize: ");
    stringBuilder.append(this.optimize);
    stringBuilder.append("\niterations: ");
    stringBuilder.append(this.iterations);
    stringBuilder.append("\npivots: ");
    stringBuilder.append(this.pivots);
    stringBuilder.append("\nbfs: ");
    stringBuilder.append(this.bfs);
    stringBuilder.append("\nvariables: ");
    stringBuilder.append(this.variables);
    stringBuilder.append("\nerrors: ");
    stringBuilder.append(this.errors);
    stringBuilder.append("\nslackvariables: ");
    stringBuilder.append(this.slackvariables);
    stringBuilder.append("\nextravariables: ");
    stringBuilder.append(this.extravariables);
    stringBuilder.append("\nfullySolved: ");
    stringBuilder.append(this.fullySolved);
    stringBuilder.append("\ngraphOptimizer: ");
    stringBuilder.append(this.graphOptimizer);
    stringBuilder.append("\nresolvedWidgets: ");
    stringBuilder.append(this.resolvedWidgets);
    stringBuilder.append("\noldresolvedWidgets: ");
    stringBuilder.append(this.oldresolvedWidgets);
    stringBuilder.append("\nnonresolvedWidgets: ");
    stringBuilder.append(this.nonresolvedWidgets);
    stringBuilder.append("\ncenterConnectionResolved: ");
    stringBuilder.append(this.centerConnectionResolved);
    stringBuilder.append("\nmatchConnectionResolved: ");
    stringBuilder.append(this.matchConnectionResolved);
    stringBuilder.append("\nchainConnectionResolved: ");
    stringBuilder.append(this.chainConnectionResolved);
    stringBuilder.append("\nbarrierConnectionResolved: ");
    stringBuilder.append(this.barrierConnectionResolved);
    stringBuilder.append("\nproblematicsLayouts: ");
    stringBuilder.append(this.problematicLayouts);
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\Metrics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */