/*     */ package lc1.dp.model;
/*     */ 
/*     */ import lc1.dp.core.DP;
/*     */ import lc1.dp.states.SiteEmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.StateDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectionModel
/*     */   extends FastMarkovModel
/*     */ {
/*     */   static final long serialVersionUID = 1L;
/*  17 */   public static String[] fromStates = { "start", "purifying", "positive", "pseudogene" };
/*     */   final int[] states_in;
/*  19 */   public static String[][] paramNames = {
/*  20 */     { "positive", "pseudogene" }, 
/*  21 */     { "positive", "pseudogene" }, 
/*  22 */     { "purifying", "pseudogene" }, 
/*  23 */     { "purifying", "positive" } };
/*     */   
/*     */ 
/*  26 */   private static double[][] default_vals = {
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  33 */     { 0.05D, 0.001D }, 
/*  34 */     { 0.05D, 0.0D }, 
/*  35 */     { 0.2D, 0.0D }, 
/*  36 */     { 0.0D, 0.0D } };
/*     */   final int[] alias;
/*     */   public final State domdom;
/*     */   public final State nucdom;
/*     */   public final State protdom;
/*     */   
/*     */   private static void check() {
/*  43 */     for (int i = 0; i < default_vals.length; i++) {
/*  44 */       if (default_vals[i][0] + default_vals[i][1] >= 0.99D) throw new RuntimeException("params not valid");
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getHMMString() {
/*  49 */     StringBuffer sb = new StringBuffer();
/*  50 */     for (int i = 0; i < default_vals.length; i++) {
/*  51 */       for (int j = 0; j < default_vals[i].length; j++) {
/*  52 */         sb.append("_");
/*  53 */         sb.append(default_vals[i][j]);
/*     */       }
/*     */     }
/*  56 */     return sb.toString();
/*     */   }
/*     */   
/*  59 */   public static double[] getVals(int k) { return default_vals[k]; }
/*     */   
/*     */   public static void setVals(double[][] vals1) {
/*  62 */     default_vals = vals1;
/*     */   }
/*     */   
/*  65 */   public static void setVals(int k, int i, double v) { default_vals[k][i] = v; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelectionModel(double[] avgProtDomEmiss, double[] avgNucDomEmiss, double[] avgDomDomEmiss, int[] alias)
/*     */   {
/*  74 */     this(default_vals, avgProtDomEmiss, avgNucDomEmiss, avgDomDomEmiss, alias);
/*     */   }
/*     */   
/*     */   public SelectionModel(double[][] vals, double[] avgProtDomEmiss, double[] avgNucDomEmiss, double[] avgDomDomEmiss, int[] alias)
/*     */   {
/*  79 */     super("", null);
/*  80 */     this.alias = alias;
/*  81 */     check();
/*  82 */     this.domdom = addState(new SiteEmissionState("d", avgDomDomEmiss, alias, false));
/*  83 */     this.protdom = addState(new SiteEmissionState("p", avgProtDomEmiss, alias, false));
/*  84 */     this.nucdom = addState(new SiteEmissionState("n", avgNucDomEmiss, alias, false));
/*  85 */     super.initialiseTransitions();
/*  86 */     this.states_in = new int[modelLength()];
/*  87 */     for (int i = 0; i < this.states_in.length; i++) {
/*  88 */       this.states_in[i] = i;
/*     */     }
/*     */     
/*     */ 
/*  92 */     setTransition(MAGIC, this.domdom, new Double(1.0D - vals[0][0] - vals[0][1]).doubleValue());
/*  93 */     setTransition(MAGIC, this.protdom, new Double(vals[0][0]).doubleValue());
/*  94 */     setTransition(MAGIC, this.nucdom, new Double(vals[0][1]).doubleValue());
/*     */     
/*  96 */     setTransition(this.domdom, this.domdom, new Double(0.99D - vals[1][0] - vals[1][1]).doubleValue());
/*  97 */     setTransition(this.domdom, this.protdom, new Double(vals[1][0]).doubleValue());
/*  98 */     setTransition(this.domdom, this.nucdom, new Double(vals[1][1]).doubleValue());
/*  99 */     setTransition(this.domdom, MAGIC, new Double(0.01D).doubleValue());
/*     */     
/* 101 */     setTransition(this.protdom, this.domdom, new Double(vals[2][0]).doubleValue());
/* 102 */     setTransition(this.protdom, this.protdom, new Double(0.99D - vals[2][0] - vals[2][1]).doubleValue());
/* 103 */     setTransition(this.protdom, this.nucdom, new Double(vals[2][1]).doubleValue());
/* 104 */     setTransition(this.protdom, MAGIC, new Double(0.01D).doubleValue());
/*     */     
/* 106 */     setTransition(this.nucdom, this.domdom, new Double(vals[3][0]).doubleValue());
/* 107 */     setTransition(this.nucdom, this.nucdom, new Double(0.99D - vals[3][0] - vals[3][1]).doubleValue());
/* 108 */     setTransition(this.nucdom, this.protdom, new Double(vals[3][1]).doubleValue());
/* 109 */     setTransition(this.nucdom, MAGIC, new Double(0.01D).doubleValue());
/*     */   }
/*     */   
/*     */   public double[][] getVals()
/*     */   {
/* 114 */     return default_vals;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[][] getSitePosterior()
/*     */   {
/* 125 */     DP dp = new DP(this, "selection", false, this.alias.length);
/* 126 */     dp.reset(false);
/* 127 */     dp.search(true);
/* 128 */     return dp.getPosteriorMatch();
/*     */   }
/*     */   
/*     */   public Object clone(boolean swtch) {
/* 132 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public boolean converged()
/*     */   {
/* 137 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPseudoCountWeights(double[] d, double[] d1) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void addCounts(StateDistribution[] transProbs, int i, int numIndiv) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialiseTransitionCounts() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void transferCountsToProbs(int index) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public int[] statesIn(int j, int i)
/*     */   {
/* 161 */     return this.states_in;
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i)
/*     */   {
/* 166 */     return this.states_in;
/*     */   }
/*     */   
/*     */   public boolean trainEmissions()
/*     */   {
/* 171 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/SelectionModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */