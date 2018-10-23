/*    */ package lc1.dp.genotype.io.scorable;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ public class Cluster extends ArrayList<String>
/*    */ {
/*    */   public double overlap(Cluster c)
/*    */   {
/* 10 */     HashSet<String> s = new HashSet(this);
/* 11 */     HashSet<String> s1 = new HashSet(this);
/* 12 */     s.retainAll(c);
/* 13 */     s1.addAll(c);
/* 14 */     return s.size() / s1.size();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/Cluster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */