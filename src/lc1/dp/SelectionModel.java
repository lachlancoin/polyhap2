/*     */ package lc1.dp;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*  15 */   public static String[] fromStates = { "start", "purifying", "positive", "pseudogene" };
/*     */   
/*  17 */   public static String[][] paramNames = {
/*  18 */     { "positive", "pseudogene" }, 
/*  19 */     { "positive", "pseudogene" }, 
/*  20 */     { "purifying", "pseudogene" }, 
/*  21 */     { "purifying", "positive" } };
/*     */   
/*     */ 
/*  24 */   private static double[][] default_vals = {
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  31 */     { 0.05D, 0.001D }, 
/*  32 */     { 0.05D, 0.0D }, 
/*  33 */     { 0.2D, 0.0D }, 
/*  34 */     { 0.0D, 0.0D } };
/*     */   final int[] alias;
/*     */   public final State domdom;
/*     */   public final State nucdom;
/*     */   public final State protdom;
/*     */   
/*     */   private static void check() {
/*  41 */     for (int i = 0; i < default_vals.length; i++) {
/*  42 */       if (default_vals[i][0] + default_vals[i][1] >= 0.99D) throw new RuntimeException("params not valid");
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getHMMString() {
/*  47 */     StringBuffer sb = new StringBuffer();
/*  48 */     for (int i = 0; i < default_vals.length; i++) {
/*  49 */       for (int j = 0; j < default_vals[i].length; j++) {
/*  50 */         sb.append("_");
/*  51 */         sb.append(default_vals[i][j]);
/*     */       }
/*     */     }
/*  54 */     return sb.toString();
/*     */   }
/*     */   
/*  57 */   public static double[] getVals(int k) { return default_vals[k]; }
/*     */   
/*     */   public static void setVals(double[][] vals1) {
/*  60 */     default_vals = vals1;
/*     */   }
/*     */   
/*  63 */   public static void setVals(int k, int i, double v) { default_vals[k][i] = v; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelectionModel(double[] avgProtDomEmiss, double[] avgNucDomEmiss, double[] avgDomDomEmiss, int[] alias)
/*     */   {
/*  72 */     this(default_vals, avgProtDomEmiss, avgNucDomEmiss, avgDomDomEmiss, alias);
/*     */   }
/*     */   
/*     */   public SelectionModel(double[][] vals, double[] avgProtDomEmiss, double[] avgNucDomEmiss, double[] avgDomDomEmiss, int[] alias)
/*     */   {
/*  77 */     super("");
/*  78 */     this.alias = alias;
/*  79 */     check();
/*  80 */     this.domdom = addState(new SiteEmissionState("d", avgDomDomEmiss, alias, false));
/*  81 */     this.protdom = addState(new SiteEmissionState("p", avgProtDomEmiss, alias, false));
/*  82 */     this.nucdom = addState(new SiteEmissionState("n", avgNucDomEmiss, alias, false));
/*  83 */     super.initialiseTransitions();
/*     */     
/*     */ 
/*     */ 
/*  87 */     setTransition(this.MAGIC, this.domdom, new Double(1.0D - vals[0][0] - vals[0][1]).doubleValue());
/*  88 */     setTransition(this.MAGIC, this.protdom, new Double(vals[0][0]).doubleValue());
/*  89 */     setTransition(this.MAGIC, this.nucdom, new Double(vals[0][1]).doubleValue());
/*     */     
/*  91 */     setTransition(this.domdom, this.domdom, new Double(0.99D - vals[1][0] - vals[1][1]).doubleValue());
/*  92 */     setTransition(this.domdom, this.protdom, new Double(vals[1][0]).doubleValue());
/*  93 */     setTransition(this.domdom, this.nucdom, new Double(vals[1][1]).doubleValue());
/*  94 */     setTransition(this.domdom, this.MAGIC, new Double(0.01D).doubleValue());
/*     */     
/*  96 */     setTransition(this.protdom, this.domdom, new Double(vals[2][0]).doubleValue());
/*  97 */     setTransition(this.protdom, this.protdom, new Double(0.99D - vals[2][0] - vals[2][1]).doubleValue());
/*  98 */     setTransition(this.protdom, this.nucdom, new Double(vals[2][1]).doubleValue());
/*  99 */     setTransition(this.protdom, this.MAGIC, new Double(0.01D).doubleValue());
/*     */     
/* 101 */     setTransition(this.nucdom, this.domdom, new Double(vals[3][0]).doubleValue());
/* 102 */     setTransition(this.nucdom, this.nucdom, new Double(0.99D - vals[3][0] - vals[3][1]).doubleValue());
/* 103 */     setTransition(this.nucdom, this.protdom, new Double(vals[3][1]).doubleValue());
/* 104 */     setTransition(this.nucdom, this.MAGIC, new Double(0.01D).doubleValue());
/* 105 */     super.fix();
/*     */   }
/*     */   
/*     */   public double[][] getVals() {
/* 109 */     return default_vals;
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
/* 120 */     DP dp = new DP(this, "selection", false, this.alias.length);
/* 121 */     dp.reset();
/* 122 */     dp.search(true);
/* 123 */     return dp.getPosteriorMatch();
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 127 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   protected List initStateSpace() {
/* 131 */     return new ArrayList();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/SelectionModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */