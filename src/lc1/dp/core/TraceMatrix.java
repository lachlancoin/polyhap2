/*    */ package lc1.dp.core;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TraceMatrix
/*    */ {
/*    */   public final AbstractTerm[][] trace;
/*    */   private final double[] logscale;
/*    */   double overall;
/*    */   final AbstractTerm nullAbstractTerm;
/*    */   final int seqLength;
/*    */   final boolean forward;
/*    */   
/*    */   public AbstractTerm getTrace(int j, int i)
/*    */   {
/* 19 */     if ((i >= 0) && (i < this.seqLength)) {
/* 20 */       AbstractTerm[] term = this.trace[j];
/* 21 */       if (term == null) return null;
/* 22 */       return term[i];
/*    */     }
/* 24 */     if ((j == 0) && (i == -1) && (this.forward)) return this.nullAbstractTerm;
/* 25 */     if ((j == 0) && (i == this.seqLength) && (!this.forward)) return this.nullAbstractTerm;
/* 26 */     return null;
/*    */   }
/*    */   
/*    */   public double getScore(int j, int i, boolean logspace) {
/* 30 */     AbstractTerm t = getTrace(j, i);
/* 31 */     if (t == null) return logspace ? Double.NEGATIVE_INFINITY : 0.0D;
/* 32 */     return t.score();
/*    */   }
/*    */   
/*    */   public double getLogScale(int i) {
/* 36 */     if (((i == -1) && (this.forward)) || ((i == this.seqLength) && (!this.forward))) return 0.0D;
/* 37 */     return this.logscale[i];
/*    */   }
/*    */   
/*    */   public void setLogscale(int i, double d) {
/* 41 */     this.logscale[i] = d;
/*    */   }
/*    */   
/*    */   public void clear()
/*    */   {
/* 46 */     Arrays.fill(this.logscale, 0.0D);
/* 47 */     for (int j = 0; j < this.trace.length; j++) {
/* 48 */       Arrays.fill(this.trace, null);
/*    */     }
/* 50 */     this.overall = 0.0D;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   TraceMatrix(int modelLength, int seqLength, boolean forward, boolean logspace)
/*    */   {
/* 57 */     this.forward = forward;
/* 58 */     this.nullAbstractTerm = new Term(-1, -1, logspace ? 0 : 1);
/* 59 */     this.trace = new AbstractTerm[modelLength][];
/* 60 */     this.seqLength = seqLength;
/* 61 */     this.logscale = new double[seqLength];
/* 62 */     Arrays.fill(this.logscale, 0.0D);
/*    */   }
/*    */   
/* 65 */   double[] minScore(int i) { double min = Double.POSITIVE_INFINITY;
/* 66 */     double max = Double.NEGATIVE_INFINITY;
/* 67 */     for (int j = 0; j < this.trace.length; j++) {
/* 68 */       AbstractTerm[] trace_j = this.trace[j];
/* 69 */       if ((trace_j != null) && (trace_j[i] != null)) {
/* 70 */         if (trace_j[i].scaleScore() < min) {
/* 71 */           min = trace_j[i].scaleScore();
/*    */         }
/* 73 */         if (trace_j[i].scaleScore() > max) {
/* 74 */           max = trace_j[i].scaleScore();
/*    */         }
/*    */       }
/*    */     }
/* 78 */     return new double[] { Math.log(min), Math.log(max) };
/*    */   }
/*    */   
/*    */   void scale(double d, int i) {
/* 82 */     for (int j = 0; j < this.trace.length; j++) {
/* 83 */       AbstractTerm[] trace_j = this.trace[j];
/* 84 */       if ((trace_j != null) && (trace_j[i] != null)) trace_j[i].scale(d);
/*    */     }
/*    */   }
/*    */   
/*    */   public void setTrace(Integer j, int i, AbstractTerm term)
/*    */   {
/* 90 */     AbstractTerm[] t = this.trace[j.intValue()];
/* 91 */     if (t == null) {
/* 92 */       this.trace[j.intValue()] = (t = new AbstractTerm[this.seqLength]);
/*    */     }
/* 94 */     t[i] = term;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/TraceMatrix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */