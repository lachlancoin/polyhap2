/*    */ package lc1.dp.profile;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import lc1.dp.genotype.io.scorable.ScorableObject;
/*    */ import pal.alignment.SitePattern;
/*    */ import pal.datatype.DataType;
/*    */ import pal.misc.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScoreableAlignment
/*    */   implements ScorableObject
/*    */ {
/*    */   public SitePattern sp;
/*    */   DataType dt;
/*    */   final int id;
/*    */   
/* 19 */   public Object clone() { return new ScoreableAlignment(this.sp, this.id); }
/*    */   
/*    */   public ScoreableAlignment(SitePattern sp, int id) {
/* 22 */     this.sp = sp;
/* 23 */     this.dt = sp.getDataType();
/*    */     
/* 25 */     this.id = id;
/*    */   }
/*    */   
/* 28 */   public int length() { return this.sp.getSiteCount(); }
/*    */   
/*    */   public Object getElement(int i) {
/* 31 */     if (this.sp.getSequenceCount() != 1) throw new RuntimeException("!!");
/* 32 */     return Integer.valueOf(this.dt.getState(this.sp.getData(0, i)));
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 37 */     return this.sp.getIdentifier(0).getName();
/*    */   }
/*    */   
/* 40 */   public void print(PrintWriter pw) { pw.println(getName()); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/profile/ScoreableAlignment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */