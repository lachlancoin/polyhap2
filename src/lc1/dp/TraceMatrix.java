/*     */ package lc1.dp;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TraceMatrix
/*     */ {
/*     */   public final AbstractTerm[][] trace;
/*     */   private final double[] logscale;
/*     */   double overall;
/*     */   final AbstractTerm nullAbstractTerm;
/*     */   final int seqLength;
/*     */   final boolean forward;
/*     */   
/*     */   public AbstractTerm getTrace(int j, int i)
/*     */   {
/*  21 */     if ((i >= 0) && (i < this.seqLength)) {
/*  22 */       AbstractTerm[] term = this.trace[j];
/*  23 */       if (term == null) return null;
/*  24 */       return term[i];
/*     */     }
/*  26 */     if ((j == 0) && (i == -1) && (this.forward)) return this.nullAbstractTerm;
/*  27 */     if ((j == 0) && (i == this.seqLength) && (!this.forward)) return this.nullAbstractTerm;
/*  28 */     return null;
/*     */   }
/*     */   
/*     */   public double getScore(int j, int i, boolean logspace) {
/*  32 */     AbstractTerm t = getTrace(j, i);
/*  33 */     if (t == null) return logspace ? Double.NEGATIVE_INFINITY : 0.0D;
/*  34 */     return t.score();
/*     */   }
/*     */   
/*     */   public double getLogScale(int i) {
/*  38 */     if (((i == -1) && (this.forward)) || ((i == this.seqLength) && (!this.forward))) return 0.0D;
/*  39 */     return this.logscale[i];
/*     */   }
/*     */   
/*     */   public void setLogscale(int i, double d) {
/*  43 */     this.logscale[i] = d;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  48 */     Arrays.fill(this.logscale, 0.0D);
/*  49 */     for (int j = 0; j < this.trace.length; j++) {
/*  50 */       Arrays.fill(this.trace, null);
/*     */     }
/*  52 */     this.overall = 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   TraceMatrix(int modelLength, int seqLength, boolean forward, boolean logspace)
/*     */   {
/*  59 */     this.forward = forward;
/*  60 */     this.nullAbstractTerm = new Term(-1, -1, logspace ? 0 : 1);
/*  61 */     this.trace = new AbstractTerm[modelLength][];
/*  62 */     this.seqLength = seqLength;
/*  63 */     this.logscale = new double[seqLength];
/*  64 */     Arrays.fill(this.logscale, 0.0D);
/*     */   }
/*     */   
/*  67 */   double[] minScore(int i) { double min = Double.POSITIVE_INFINITY;
/*  68 */     double max = Double.NEGATIVE_INFINITY;
/*  69 */     for (int j = 0; j < this.trace.length; j++) {
/*  70 */       AbstractTerm[] trace_j = this.trace[j];
/*  71 */       if ((trace_j != null) && (trace_j[i] != null)) {
/*  72 */         if (trace_j[i].scaleScore() < min) {
/*  73 */           min = trace_j[i].scaleScore();
/*     */         }
/*  75 */         if (trace_j[i].scaleScore() > max)
/*  76 */           max = trace_j[i].scaleScore();
/*     */       }
/*     */     }
/*  79 */     if (max <= 0.0D) {
/*  80 */       double[] sc = new double[this.trace.length];
/*  81 */       for (int j = 0; j < this.trace.length; j++) {
/*  82 */         AbstractTerm[] trace_j = this.trace[j];
/*  83 */         if ((trace_j != null) && (trace_j[i] != null))
/*     */         {
/*     */ 
/*  86 */           sc[j] = trace_j[i].scaleScore(); }
/*     */       }
/*  88 */       throw new RuntimeException("max should be greater than zero");
/*     */     }
/*  90 */     return new double[] { Math.log(min), Math.log(max) };
/*     */   }
/*     */   
/*     */   void scale(double d, int i) {
/*  94 */     for (int j = 0; j < this.trace.length; j++) {
/*  95 */       AbstractTerm[] trace_j = this.trace[j];
/*  96 */       if ((trace_j != null) && (trace_j[i] != null)) trace_j[i].scale(d);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTrace(Integer j, int i, AbstractTerm term)
/*     */   {
/* 102 */     AbstractTerm[] t = this.trace[j.intValue()];
/* 103 */     if (t == null) {
/* 104 */       this.trace[j.intValue()] = (t = new AbstractTerm[this.seqLength]);
/*     */     }
/* 106 */     t[i] = term;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/TraceMatrix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */