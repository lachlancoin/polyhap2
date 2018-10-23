/*    */ package lc1.dp;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DotState
/*    */   extends State
/*    */ {
/*    */   static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */   short num;
/*    */   
/*    */   final SimpleDistribution distStart;
/*    */   
/*    */   final SimpleDistribution distEnd;
/*    */   
/*    */ 
/* 20 */   public int getClassId() { return 0; }
/*    */   
/*    */   public DotState(String name, SimpleDistribution start, SimpleDistribution end) {
/* 23 */     super(name, 0);
/* 24 */     this.distStart = start;
/* 25 */     this.distEnd = end;
/*    */   }
/*    */   
/*    */   public DotState(DotState st1) {
/* 29 */     super(st1);
/* 30 */     this.distStart = new SimpleDistribution(st1.distStart);
/* 31 */     this.distEnd = new SimpleDistribution(st1.distEnd);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 36 */   public String toString() { return this.name + " " + this.num; }
/*    */   
/*    */   public DotState(String name) {
/* 39 */     super(name, 0);
/* 40 */     this.distStart = SimpleDistribution.noOffset;
/* 41 */     this.distEnd = SimpleDistribution.noOffset;
/*    */   }
/*    */   
/*    */ 
/* 45 */   public Object clone(State pseudo) { return new DotState(this); }
/*    */   
/*    */   public void validate() throws Exception
/*    */   {}
/*    */   
/* 50 */   public DotState(String name, short num) { this(name, SimpleDistribution.noOffset, SimpleDistribution.noOffset);
/* 51 */     this.num = num;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 58 */   public int hashCode() { return this.name.hashCode() + this.num; }
/*    */   
/*    */   public SimpleDistribution adv(State s) {
/* 61 */     if (s == null) return this.distStart;
/* 62 */     return this.distEnd;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/DotState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */