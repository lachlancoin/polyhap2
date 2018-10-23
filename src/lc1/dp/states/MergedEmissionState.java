/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.stats.SkewNormal;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MergedEmissionState
/*     */   extends HaplotypeEmissionState
/*     */ {
/*     */   public EmissionState[] em;
/*     */   private List<int[]> transformation;
/*     */   
/*  19 */   public void reverse() { throw new RuntimeException("!!"); }
/*     */   
/*     */   static String getName(EmissionState[] em) {
/*  22 */     StringBuffer sb = new StringBuffer();
/*  23 */     for (int i = 0; i < em.length; i++) {
/*  24 */       sb.append(em[i].getName());
/*     */     }
/*  26 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void getTransformation(List<Integer>[] locs, List<String>[] snp_id, List<int[]> transformation, List<Integer> overallLocs, int main)
/*     */   {
/*  33 */     transformation.clear();
/*  34 */     overallLocs.clear();
/*  35 */     SortedSet<PosTrans> l = new TreeSet();
/*  36 */     for (int i = 0; i < locs.length; i++) {
/*  37 */       for (int j = 0; j < locs[i].size(); j++) {
/*  38 */         PosTrans pt = new PosTrans(((Integer)locs[i].get(j)).intValue(), i, j, (String)snp_id[i].get(j));
/*  39 */         if (l.contains(pt)) {
/*  40 */           if (i == main) {
/*  41 */             PosTrans first = (PosTrans)l.tailSet(pt).first();
/*  42 */             first.replace(pt);
/*     */           }
/*     */         }
/*     */         else {
/*  46 */           l.add(pt);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  51 */     for (Iterator<PosTrans> it = l.iterator(); it.hasNext();) {
/*  52 */       PosTrans nxt = (PosTrans)it.next();
/*  53 */       transformation.add(nxt.ind_loc);
/*  54 */       overallLocs.add(nxt.pos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void adjustCoords(List<Integer>[] locs, List<String>[] snp_id)
/*     */   {
/*  61 */     SortedSet<PosTrans1> l = new TreeSet();
/*  62 */     for (int i = 0; i < locs.length; i++) {
/*  63 */       for (int j = 0; j < locs[i].size(); j++) {
/*  64 */         PosTrans1 pt = new PosTrans1(((Integer)locs[i].get(j)).intValue(), i, j, (String)snp_id[i].get(j));
/*  65 */         if (l.contains(pt)) {
/*  66 */           PosTrans1 first = (PosTrans1)l.tailSet(pt).first();
/*  67 */           if (!first.snp_id.equals(pt.snp_id)) {
/*  68 */             int incr = ((Integer)locs[i].get(j)).intValue() + 1;
/*  69 */             if ((locs[i].size() > j + 1) && (((Integer)locs[i].get(j + 1)).intValue() == incr)) throw new RuntimeException("!!");
/*  70 */             locs[i].set(j, Integer.valueOf(incr));
/*  71 */             pt = new PosTrans1(((Integer)locs[i].get(j)).intValue(), i, j, (String)snp_id[i].get(j));
/*  72 */             if (l.contains(pt)) throw new RuntimeException("!!");
/*  73 */             l.add(pt);
/*     */           }
/*     */         }
/*     */         else {
/*  77 */           l.add(pt);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public SkewNormal[] probR()
/*     */   {
/*  85 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public IlluminaProbB[] probB()
/*     */   {
/*  90 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public MergedEmissionState(HaplotypeEmissionState[] em, List<int[]> transformation) {
/*  94 */     super(em[0].getName(), transformation.size(), em[0].getEmissionStateSpace(), (short)-1);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */     this.em = em;
/* 102 */     this.transformation = transformation;
/* 103 */     for (int i = 0; i < this.noSnps; i++) {
/* 104 */       int[] inde = (int[])transformation.get(i);
/* 105 */       this.emissions[i] = em[inde[0]].emissions[inde[1]];
/*     */     }
/*     */   }
/*     */   
/*     */   public MergedEmissionState(MergedEmissionState state) {
/* 110 */     super(state);
/* 111 */     this.em = new EmissionState[state.em.length];
/* 112 */     for (int i = 0; i < this.em.length; i++) {
/* 113 */       this.em[i] = ((EmissionState)state.em[i].clone());
/*     */     }
/* 115 */     this.transformation = new ArrayList(state.transformation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 137 */     return new MergedEmissionState(this);
/*     */   }
/*     */   
/* 140 */   int paramIndex = 1;
/*     */   
/*     */   public int getParamIndex() {
/* 143 */     return this.paramIndex;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/MergedEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */