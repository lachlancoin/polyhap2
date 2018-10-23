/*    */ package lc1.dp.genotype.io;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.TreeSet;
/*    */ import lc1.dp.CopyEnumerator;
/*    */ 
/*    */ public class EmissionStateSpace extends ArrayList<Comparable>
/*    */ {
/*    */   public final int noCopies;
/*    */   
/*    */   public EmissionStateSpace(Collection set, int noCopies)
/*    */   {
/* 17 */     super(set.size());
/* 18 */     this.noCopies = noCopies;
/* 19 */     for (Iterator<Comparable> it = set.iterator(); it.hasNext();) {
/* 20 */       add((Comparable)it.next());
/*    */     }
/* 22 */     initialise();
/*    */   }
/*    */   
/*    */ 
/* 26 */   public EmissionStateSpace(List<Comparable>[] stateSpaces, int noCop) { this(initStateSpace(stateSpaces), noCop); }
/*    */   
/*    */   private static Collection<Comparable> initStateSpace(final List<Comparable>[] stateSpaces) {
/* 29 */     final Collection<Comparable> set = new TreeSet();
/* 30 */     CopyEnumerator posm = new CopyEnumerator(stateSpaces.length) {
/*    */       public Iterator<Comparable> getPossibilities(int depth) {
/* 32 */         return stateSpaces[depth].iterator();
/*    */       }
/*    */       
/*    */       public void doInner(Comparable[] list) {
/* 36 */         set.add(new ComparableArray(java.util.Arrays.asList(list)));
/*    */       }
/*    */       
/*    */       public boolean exclude(Object obj, Object previous) {
/* 40 */         return false;
/*    */       }
/* 42 */     };
/* 43 */     posm.run();
/*    */     
/* 45 */     return set;
/*    */   }
/*    */   
/* 48 */   protected final Map<Comparable, Integer> stateSpaceToIndex = new java.util.TreeMap();
/*    */   
/*    */   public void initialise()
/*    */   {
/* 52 */     for (int i = 0; i < size(); i++) {
/* 53 */       this.stateSpaceToIndex.put((Comparable)get(i), Integer.valueOf(i));
/*    */     }
/*    */   }
/*    */   
/*    */   public Integer get(Object comp)
/*    */   {
/* 59 */     return (Integer)this.stateSpaceToIndex.get(comp);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/EmissionStateSpace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */