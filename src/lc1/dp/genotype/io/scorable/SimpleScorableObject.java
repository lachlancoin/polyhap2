/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ 
/*     */ 
/*     */ public abstract class SimpleScorableObject
/*     */   implements ScorableObject
/*     */ {
/*     */   List<Comparable> l1;
/*     */   final String id;
/*     */   final Class clazz;
/*     */   
/*  19 */   public String getName() { return this.id; }
/*     */   
/*     */   public abstract ScorableObject clone();
/*     */   
/*  23 */   public final void remove(int i) { this.l1.remove(i); }
/*     */   
/*     */   public String getStringRep(int i) {
/*  26 */     return getElement(i).toString();
/*     */   }
/*     */   
/*     */   public void restrictTo(Integer integer, Integer integer2) {
/*  30 */     this.l1 = this.l1.subList(integer.intValue(), integer2.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleScorableObject(String id, int noSites, Class clazz)
/*     */   {
/*  36 */     this.l1 = new ArrayList(noSites);
/*  37 */     this.id = id;
/*  38 */     this.clazz = clazz;
/*     */   }
/*     */   
/*  41 */   public SimpleScorableObject(SimpleScorableObject data) { this(data.id, data.l1.size(), data.clazz);
/*  42 */     for (Iterator<Comparable> it = data.l1.iterator(); it.hasNext();)
/*  43 */       this.l1.add(copyElement((Comparable)it.next()));
/*     */   }
/*     */   
/*     */   public abstract Comparable copyElement(Comparable paramComparable);
/*     */   
/*     */   public void addMissingData(double perc, double error) {
/*  49 */     for (int i = 0; i < length(); i++) {
/*  50 */       if (Constants.rand.nextDouble() < perc) {
/*  51 */         set(i, null);
/*     */       }
/*  53 */       else if (Constants.rand.nextDouble() < perc) {
/*  54 */         throw new RuntimeException();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean allNull()
/*     */   {
/*  62 */     for (int i = 0; i < this.l1.size(); i++) {
/*  63 */       if (this.l1.get(i) != null) return false;
/*     */     }
/*  65 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int length()
/*     */   {
/*  72 */     return this.l1.size();
/*     */   }
/*     */   
/*     */   public final Comparable getElement(int i) {
/*  76 */     return (Comparable)this.l1.get(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void addPoint(Comparable i1)
/*     */   {
/*  83 */     this.l1.add(i1);
/*     */   }
/*     */   
/*     */   public void set(int i, Comparable obj) {
/*  87 */     this.l1.set(i, obj);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String toString()
/*     */   {
/*  99 */     StringWriter sw = new StringWriter();
/* 100 */     PrintWriter pw = new PrintWriter(sw);
/* 101 */     print(pw);
/* 102 */     return sw.getBuffer().toString();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/SimpleScorableObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */