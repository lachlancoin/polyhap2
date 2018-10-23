/*    */ package lc1.stats;
/*    */ 
/*    */ public class Contigency {
/*  4 */   ChiSq chisq = new ChiSq();
/*    */   
/*    */   int deg;
/*    */   double[][] contig;
/*    */   double csum;
/*    */   int rows;
/*    */   int cols;
/*    */   double[] crow;
/*    */   double[] ccol;
/*    */   double[][] expectation;
/*    */   
/*    */   final double calcchiSquare()
/*    */   {
/* 17 */     double chi = 0.0D;
/*    */     
/* 19 */     for (int i = 0; i < this.rows; i++)
/*    */     {
/* 21 */       for (int j = 0; j < this.cols; j++) {
/* 22 */         if (this.contig[i][j] > 0.0D) {
/* 23 */           double E = this.expectation[i][j];
/* 24 */           chi += ((float)this.contig[i][j] - E) * ((float)this.contig[i][j] - E) / E;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 29 */     return chi;
/*    */   }
/*    */   
/*    */   public final double getSig() {
/* 33 */     double chi2 = calcchiSquare();
/* 34 */     return ChiSq.chi2prob(this.deg, chi2);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setMatrix(double[][] tcontig)
/*    */   {
/* 45 */     this.rows = tcontig.length;
/* 46 */     this.cols = tcontig[0].length;
/* 47 */     this.deg = ((this.rows - 1) * (this.cols - 1));
/* 48 */     this.contig = tcontig;
/* 49 */     this.csum = 0.0D;
/* 50 */     this.crow = new double[this.rows];
/* 51 */     this.ccol = new double[this.cols];
/* 52 */     for (int i = 0; i < this.rows; i++) {
/* 53 */       for (int j = 0; j < this.cols; j++) {
/* 54 */         this.csum += this.contig[i][j];
/* 55 */         this.crow[i] += this.contig[i][j];
/* 56 */         this.ccol[j] += this.contig[i][j];
/*    */       }
/*    */     }
/* 59 */     this.expectation = new double[this.rows][this.cols];
/* 60 */     for (int i = 0; i < this.rows; i++) {
/* 61 */       for (int j = 0; j < this.cols; j++)
/*    */       {
/*    */ 
/* 64 */         this.expectation[i][j] = (this.ccol[j] / this.csum * this.crow[i]);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/Contigency.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */