/*    */ package lc1.dp;
/*    */ 
/*    */ import java.io.Serializable;
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
/* 22 */   public static int endStateLength = 5;
/*    */   
/* 24 */   public int index = -1;
/*    */   
/* 26 */   public int getIndex() { if (this.index == -1) throw new NullPointerException("index is null");
/* 27 */     return this.index;
/*    */   }
/*    */   
/*    */   public int compareTo(Object o) {
/* 31 */     State st = (State)o;
/* 32 */     return this.name.compareTo(st.getName());
/*    */   }
/*    */   
/*    */   public abstract Object clone(State paramState);
/*    */   
/*    */   public String getName()
/*    */   {
/* 39 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getEmissionName() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract SimpleDistribution adv(State paramState);
/*    */   
/*    */ 
/*    */   public abstract void validate() throws Exception;
/*    */   
/* 52 */   public String toString() { return this.name; }
/*    */   
/*    */   State(String name1, int adv) {
/* 55 */     this.name = name1;
/* 56 */     this.adv = adv;
/*    */   }
/*    */   
/* 59 */   public State(State st1) { this(st1.name, st1.adv); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 67 */     return ((State)o).getName().equals(getName());
/*    */   }
/*    */   
/* 70 */   public int hashCode() { return getName().hashCode(); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/State.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */