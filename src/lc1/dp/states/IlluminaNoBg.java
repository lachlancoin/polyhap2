/*    */ package lc1.dp.states;
/*    */ 
/*    */ import lc1.dp.emissionspace.EmissionStateSpace;
/*    */ import lc1.dp.illumina.IlluminaProbB;
/*    */ import lc1.dp.illumina.IlluminaProbR;
/*    */ import lc1.stats.IlluminaDistribution;
/*    */ import lc1.stats.PseudoDistribution;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IlluminaNoBg
/*    */   extends HaplotypeEmissionState
/*    */ {
/*    */   IlluminaProbR probR;
/*    */   IlluminaProbB probB;
/*    */   
/*    */   public int getParamIndex()
/*    */   {
/* 19 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IlluminaNoBg(String name, EmissionStateSpace emStSp, IlluminaProbR r, IlluminaProbB b, int noSnps, short index)
/*    */   {
/* 29 */     super(name, noSnps, emStSp, index);
/* 30 */     this.probB = b;
/* 31 */     this.probR = r;
/* 32 */     for (int i = 0; i < this.noSnps; i++) {
/* 33 */       this.emissions[i] = new IlluminaDistribution(index);
/*    */     }
/*    */   }
/*    */   
/*    */   public IlluminaNoBg(IlluminaNoBg st)
/*    */   {
/* 39 */     super(st);
/* 40 */     for (int i = 0; i < this.noSnps; i++) {
/* 41 */       this.emissions[i] = st.emissions[i].clone();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setB(int i, double b2)
/*    */   {
/* 53 */     ((IlluminaDistribution)this.emissions[i]).setB(b2);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setR(int i, double r2)
/*    */   {
/* 59 */     ((IlluminaDistribution)this.emissions[i]).setR(r2);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 70 */   public EmissionStateSpace getEmissionStateSpace() { return this.emStSp; }
/*    */   
/*    */   public void set(int i, double sampleR, double sampleB) {
/* 73 */     setR(i, sampleR);
/* 74 */     setB(i, sampleB);
/*    */   }
/*    */   
/*    */ 
/*    */   public double[] getEmiss(int i)
/*    */   {
/* 80 */     return this.emissions[i].calcDistribution(this.probR, this.probB, this.distribution, this.emStSp);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/IlluminaNoBg.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */