/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class SimpleDataCollection
/*     */   extends DataCollection
/*     */ {
/*     */   public SimpleDataCollection() {}
/*     */   
/*     */   protected SimpleDataCollection(DataCollection dat)
/*     */   {
/*  26 */     super(dat);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleDataCollection readAffy(File f)
/*     */     throws Exception
/*     */   {
/*  53 */     throw new Error("Unresolved compilation problem: \n\tThe method put(String, PhasedIntegerGenotypeData) is undefined for the type List<PhasedIntegerGenotypeData>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void selectMale(boolean male)
/*     */   {
/*  61 */     throw new Error("Unresolved compilation problem: \n\tThe method values() is undefined for the type List<PhasedIntegerGenotypeData>\n");
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
/*     */   public void selectMaleTrios()
/*     */   {
/*  78 */     throw new Error("Unresolved compilation problem: \n\tThe method values() is undefined for the type List<PhasedIntegerGenotypeData>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/*  89 */     return this.data.size();
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
/*     */   public void transform(double[] fraction, List<String> keys, String join)
/*     */   {
/* 105 */     throw new Error("Unresolved compilation problems: \n\tped cannot be resolved\n\tType mismatch: cannot convert from boolean to CompoundScorableObject\n\trecSites cannot be resolved or is not a field\n\tThe constructor PhasedIntegerGenotypeData(CompoundScorableObject[], boolean, String) is undefined\n\tped cannot be resolved\n\tped cannot be resolved\n\trecSites cannot be resolved or is not a field\n\tThe method put(String, PhasedIntegerGenotypeData) is undefined for the type List<PhasedIntegerGenotypeData>\n");
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
/*     */ 
/*     */ 
/*     */   public void rename()
/*     */   {
/* 129 */     throw new Error("Unresolved compilation problems: \n\tThe method keySet() is undefined for the type List<PhasedIntegerGenotypeData>\n\tThe method get(int) in the type List<PhasedIntegerGenotypeData> is not applicable for the arguments (String)\n\tThe method setName(String) is undefined for the type PhasedIntegerGenotypeData\n\tType mismatch: cannot convert from Map<String,PhasedIntegerGenotypeData> to List<PhasedIntegerGenotypeData>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void makeTrioData()
/*     */   {
/* 140 */     throw new Error("Unresolved compilation problems: \n\tped cannot be resolved\n\tThe method keySet() is undefined for the type List<PhasedIntegerGenotypeData>\n\tThe method get(int) in the type List<PhasedIntegerGenotypeData> is not applicable for the arguments (String)\n\tThe method hotspot(int) is undefined for the type Constants\n\tThe method recombine(SortedMap<Integer,Integer>, int) is undefined for the type PhasedIntegerGenotypeData\n\tped cannot be resolved\n\tThe method put(String, PhasedIntegerGenotypeData) is undefined for the type List<PhasedIntegerGenotypeData>\n\trecSites cannot be resolved or is not a field\n");
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
/*     */   public String toString()
/*     */   {
/* 192 */     return this.data.toString();
/*     */   }
/*     */   
/* 195 */   public Iterator<PhasedIntegerGenotypeData> iterator() { throw new Error("Unresolved compilation problem: \n\tThe method values() is undefined for the type List<PhasedIntegerGenotypeData>\n"); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleDataCollection(int length) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleDataCollection(List<PhasedIntegerGenotypeData> name) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fillLikelihoodData()
/*     */   {
/* 218 */     throw new Error("Unresolved compilation problems: \n\tThe method getKeys() is undefined for the type SimpleDataCollection\n\tdataL cannot be resolved or is not a field\n");
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
/*     */   public SimpleDataCollection clone()
/*     */   {
/* 239 */     return new SimpleDataCollection(this);
/*     */   }
/*     */   
/*     */   public void restricToAlias(Collection<String> alias) {
/* 243 */     throw new Error("Unresolved compilation problems: \n\tThe method keySet() is undefined for the type List<PhasedIntegerGenotypeData>\n\trecSites cannot be resolved\n\tviterbi cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mix()
/*     */   {
/* 255 */     throw new Error("Unresolved compilation problem: \n\tThe method values() is undefined for the type List<PhasedIntegerGenotypeData>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void modify()
/*     */   {
/* 263 */     throw new Error("Unresolved compilation problems: \n\tThe method values() is undefined for the type List<PhasedIntegerGenotypeData>\n\tcount_null_sites cannot be resolved or is not a field\n\tThe method addMissingData(double, double) in the type CompoundScorableObject is not applicable for the arguments (double)\n\tThe method addError(double) is undefined for the type PhasedIntegerGenotypeData\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void restrictDataSites(int i)
/*     */   {
/* 272 */     throw new Error("Unresolved compilation problems: \n\tThe method values() is undefined for the type List<PhasedIntegerGenotypeData>\n\tThe method restrictSites(int) is undefined for the type PhasedIntegerGenotypeData\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(List<PhasedIntegerGenotypeData> toAdd)
/*     */   {
/* 284 */     throw new Error("Unresolved compilation problem: \n\tThe method put(String, PhasedIntegerGenotypeData) is undefined for the type List<PhasedIntegerGenotypeData>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void convertHemizygousToHomozygous()
/*     */   {
/* 291 */     throw new Error("Unresolved compilation problem: \n\tThe method values() is undefined for the type List<PhasedIntegerGenotypeData>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(List<String> toRemove)
/*     */   {
/* 302 */     for (int i = toRemove.size() - 1; i >= 0; i--) {
/* 303 */       this.data.remove(toRemove.get(i));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/SimpleDataCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */