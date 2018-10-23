/*    */ package lc1.dp.states;
/*    */ 
/*    */ import lc1.stats.SimpleDistribution;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DotState
/*    */   extends State
/*    */ {
/*    */   static final long serialVersionUID = 1L;
/*    */   short num;
/*    */   final SimpleDistribution distStart;
/*    */   final SimpleDistribution distEnd;
/*    */   
/* 22 */   public int getClassId() { return 0; }
/*    */   
/*    */   public DotState(String name, SimpleDistribution start, SimpleDistribution end) {
/* 25 */     super(name, 0);
/* 26 */     this.distStart = start;
/* 27 */     this.distEnd = end;
/*    */   }
/*    */   
/*    */   public DotState(DotState st1) {
/* 31 */     super(st1);
/* 32 */     this.distStart = new SimpleDistribution(st1.distStart);
/* 33 */     this.distEnd = new SimpleDistribution(st1.distEnd);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 38 */   public String toString() { return this.name + " " + this.num; }
/*    */   
/*    */   public DotState(String name) {
/* 41 */     super(name, 0);
/* 42 */     this.distStart = SimpleDistribution.noOffset;
/* 43 */     this.distEnd = SimpleDistribution.noOffset;
/*    */   }
/*    */   
/*    */ 
/* 47 */   public Object clone() { return new DotState(this); }
/*    */   
/*    */   public void validate() throws Exception
/*    */   {}
/*    */   
/* 52 */   public DotState(String name, short num) { this(name, SimpleDistribution.noOffset, SimpleDistribution.noOffset);
/* 53 */     this.num = num;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 60 */   public int hashCode() { return this.name.hashCode() + this.num; }
/*    */   
/*    */   public SimpleDistribution adv(State s) {
/* 63 */     if (s == null) return this.distStart;
/* 64 */     return this.distEnd;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/DotState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */