/*    */ package lc1.dp.states;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class State
/*    */   implements Serializable, Comparable
/*    */ {
/*    */   public String name;
/*    */   public final int adv;
/* 23 */   public static int endStateLength = 5;
/*    */   
/* 25 */   private int index = -1;
/*    */   
/*    */   public void setIndex(int i) {
/* 28 */     if ((this.index != -1) && (this.index != i)) throw new RuntimeException("changing index");
/* 29 */     this.index = i;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 33 */     return this.index;
/*    */   }
/*    */   
/*    */   public int compareTo(Object o) {
/* 37 */     State st = (State)o;
/* 38 */     return this.name.compareTo(st.getName());
/*    */   }
/*    */   
/*    */   public abstract Object clone();
/*    */   
/*    */   public String getName()
/*    */   {
/* 45 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getEmissionName() {
/* 49 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract SimpleDistribution adv(State paramState);
/*    */   
/*    */ 
/*    */   public abstract void validate() throws Exception;
/*    */   
/* 58 */   public String toString() { return this.name; }
/*    */   
/*    */   public State(String name1, int adv) {
/* 61 */     if (name1 == "") throw new RuntimeException("need name");
/* 62 */     this.name = name1;
/* 63 */     this.adv = adv;
/*    */   }
/*    */   
/* 66 */   public State(State st1) { this(st1.name, st1.adv); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 74 */     return ((State)o).getName().equals(getName());
/*    */   }
/*    */   
/* 77 */   public int hashCode() { return getName().hashCode(); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/State.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */