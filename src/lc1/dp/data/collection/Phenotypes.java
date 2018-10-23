/*    */ package lc1.dp.data.collection;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import lc1.stats.ProbabilityDistribution;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Phenotypes
/*    */ {
/*    */   Map<String, Integer>[] phenVals;
/*    */   public ProbabilityDistribution[] phenotypeDistribution;
/* 16 */   public List<String> phen = new ArrayList();
/*    */   
/*    */   public Phenotypes() {}
/*    */   
/*    */   public Phenotypes(File incl) throws Exception {
/* 21 */     DataCollection.readPosInfo(incl, new int[1], false, new List[] { this.phen }, new Class[] { String.class });
/* 22 */     this.phenVals = new Map[this.phen.size()];
/*    */   }
/*    */   
/* 25 */   public int size() { return this.phen.size(); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/Phenotypes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */