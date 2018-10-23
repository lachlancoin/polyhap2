/*    */ package lc1.dp.states;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import lc1.dp.core.ProbMultivariate;
/*    */ import lc1.stats.SkewNormal;
/*    */ import lc1.util.Constants;
/*    */ 
/*    */ 
/*    */ 
/*    */ class ProbRManager
/*    */ {
/*    */   Map<List<SkewNormal>, SkewNormal>[] m;
/*    */   
/*    */   ProbRManager()
/*    */   {
/* 19 */     this.m = new Map[Constants.format().length];
/* 20 */     for (int i = 0; i < this.m.length; i++) {
/* 21 */       this.m[i] = new HashMap();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   SkewNormal[] getR(List<EmissionState> dist)
/*    */   {
/* 28 */     SkewNormal[] res = new SkewNormal[this.m.length];
/* 29 */     List[] l = new List[this.m.length];
/* 30 */     for (int ik = 0; ik < l.length; ik++) {
/* 31 */       l[ik] = new ArrayList();
/* 32 */       for (int i = 0; i < dist.size(); i++) {
/* 33 */         l[ik].add(((EmissionState)dist.get(i)).probR()[ik]);
/*    */       }
/*    */       
/*    */ 
/*    */ 
/* 38 */       SkewNormal r = (SkewNormal)this.m[ik].get(l[ik]);
/* 39 */       if (r == null) {
/* 40 */         this.m[ik].put(l[ik], r = ProbMultivariate.getExp((SkewNormal[])l[ik].toArray(new SkewNormal[0])));
/* 41 */         r.appendToName(l[ik].toString());
/*    */       }
/* 43 */       res[ik] = r;
/*    */     }
/* 45 */     return res;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/ProbRManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */