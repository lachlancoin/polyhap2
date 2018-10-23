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
/*     */ class Lawless422
/*     */   extends LawlessFunction
/*     */ {
/*     */   int z;
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
/*     */   float c;
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
/*     */   public Lawless422(double[] x, double[] y, int n, int z, float c)
/*     */   {
/* 183 */     super(x, y, n);
/* 184 */     this.n = n;
/* 185 */     this.z = z;
/* 186 */     this.c = c;
/*     */   }
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
/*     */   public double[] evaluate(double lambda)
/*     */   {
/* 211 */     double esum = 0.0D;
/* 212 */     double xesum = 0.0D;
/* 213 */     double xxesum = 0.0D;
/* 214 */     double xsum = 0.0D;
/*     */     
/* 216 */     double total = 0.0D;
/*     */     
/*     */ 
/* 219 */     for (int i = 0; i < this.n; i++) {
/* 220 */       double mult = this.y == null ? 1.0D : this.y[i];
/* 221 */       xsum += mult * this.x[i];
/* 222 */       esum += mult * Math.exp(-1.0D * lambda * this.x[i]);
/* 223 */       xesum += mult * this.x[i] * Math.exp(-1.0D * lambda * this.x[i]);
/* 224 */       xxesum += mult * this.x[i] * this.x[i] * Math.exp(-1.0D * lambda * this.x[i]);
/* 225 */       total += mult;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 231 */     esum += this.z * Math.exp(-1.0D * lambda * this.c);
/* 232 */     xesum += this.z * this.c * Math.exp(-1.0D * lambda * this.c);
/* 233 */     xxesum += this.z * this.c * this.c * Math.exp(-1.0D * lambda * this.c);
/*     */     
/* 235 */     double ret_f = 1.0D / lambda - xsum / total + xesum / esum;
/* 236 */     double gradient = xesum / esum * (xesum / esum) - xxesum / esum - 
/* 237 */       1.0D / (lambda * lambda);
/*     */     
/* 239 */     return new double[] { ret_f, gradient };
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/evd/Lawless422.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */