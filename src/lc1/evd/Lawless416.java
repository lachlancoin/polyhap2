/*     */ package lc1.evd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Lawless416
/*     */   extends LawlessFunction
/*     */ {
/*     */   public Lawless416(double[] x, double[] y, int n)
/*     */   {
/* 110 */     super(x, y, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] evaluate(double lambda)
/*     */   {
/*     */     double total;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     double xxesum;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     double xsum;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     double xesum;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 140 */     double esum = xesum = xsum = xxesum = total = 0.0D;
/*     */     
/* 142 */     for (int i = 0; i < this.n; i++) {
/* 143 */       double mult = this.y == null ? 1.0D : this.y[i];
/* 144 */       xsum += mult * this.x[i];
/* 145 */       xesum += mult * this.x[i] * Math.exp(-1.0D * lambda * this.x[i]);
/* 146 */       xxesum += mult * this.x[i] * this.x[i] * Math.exp(-1.0D * lambda * this.x[i]);
/* 147 */       esum += mult * Math.exp(-1.0D * lambda * this.x[i]);
/* 148 */       total += mult;
/*     */     }
/*     */     
/* 151 */     double result = 1.0D / lambda - xsum / total + xesum / esum;
/* 152 */     double grad = xesum / esum * (xesum / esum) - xxesum / esum - 
/* 153 */       1.0D / (lambda * lambda);
/*     */     
/* 155 */     return new double[] { result, grad };
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/evd/Lawless416.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */