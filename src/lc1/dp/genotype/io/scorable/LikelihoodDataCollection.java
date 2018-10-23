/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LikelihoodDataCollection
/*     */   extends DataCollection
/*     */ {
/*     */   public static DataCollection read(File f)
/*     */   {
/*     */     try
/*     */     {
/*  33 */       String[] cat = {
/*  34 */         "__", 
/*  35 */         "A_", 
/*  36 */         "B_", 
/*  37 */         "AA", 
/*  38 */         "AB", 
/*  39 */         "BB", 
/*  40 */         "AAA", 
/*  41 */         "AAB", 
/*  42 */         "ABB", 
/*  43 */         "BBB", 
/*  44 */         "AAAA", 
/*  45 */         "AAAB", 
/*  46 */         "AABB", 
/*  47 */         "ABBB", 
/*  48 */         "BBBB" };
/*     */       
/*     */ 
/*     */ 
/*  52 */       return new LikelihoodDataCollection(cat);
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*  56 */       exc.printStackTrace(); }
/*  57 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataCollection clone()
/*     */   {
/*  66 */     return new LikelihoodDataCollection(this);
/*     */   }
/*     */   
/*     */ 
/*     */   protected LikelihoodDataCollection(LikelihoodDataCollection dat) {}
/*     */   
/*     */   public void maximisationStep(double pseudo)
/*     */   {
/*  74 */     throw new Error("Unresolved compilation problem: \n\tdataL cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialisationStep()
/*     */   {
/*  81 */     throw new Error("Unresolved compilation problem: \n\tdataL cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */   static boolean readLine(BufferedReader[] br, List<String>[] str)
/*     */     throws Exception
/*     */   {
/*  88 */     throw new Error("Unresolved compilation problem: \n\tThe method restrict() is undefined for the type Constants\n");
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
/*     */   static List<String> readNames(File id_file)
/*     */   {
/* 104 */     if ((!id_file.exists()) || (id_file.length() == 0L)) return null;
/* 105 */     List<String> l = new ArrayList();
/*     */     try {
/* 107 */       if ((!id_file.exists()) || (id_file.length() == 0L)) throw new RuntimeException("!!!");
/* 108 */       BufferedReader br = new BufferedReader(new FileReader(id_file));
/* 109 */       String st = "";
/* 110 */       while ((st = br.readLine()) != null) {
/* 111 */         l.add(st);
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 115 */       exc.printStackTrace();
/*     */     }
/* 117 */     return l;
/*     */   }
/*     */   
/*     */   public void extractFromTrioData() {
/* 121 */     throw new Error("Unresolved compilation problems: \n\tThe method extractFromTrioData() is undefined for the type DataCollection\n\tdataL cannot be resolved\n\tdataL cannot be resolved or is not a field\n");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LikelihoodDataCollection(String[] categories)
/*     */     throws Exception
/*     */   {}
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseMaf()
/*     */   {
/* 236 */     throw new Error("Unresolved compilation problems: \n\tType mismatch: cannot convert from HaplotypeEmissionState to List<Integer>\n\tlength cannot be resolved\n\temStSp cannot be resolved\n\temStSp cannot be resolved\n\tThe method initialiseCounts() is undefined for the type List<Integer>\n\temStSp cannot be resolved\n\tlength cannot be resolved\n\tmaf.emissions cannot be resolved or is not a field\n\tdataL cannot be resolved or is not a field\n\tThe method getEmiss(int) is undefined for the type EmissionState\n\tThe method transferCountsToProbs(int) is undefined for the type List<Integer>\n\tThe method initialiseCounts() is undefined for the type List<Integer>\n");
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
/*     */   public LikelihoodDataCollection(List<LikelihoodData> name) {}
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
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 291 */     throw new Error("Unresolved compilation problem: \n\tdataL cannot be resolved\n");
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
/*     */   public void restricToAlias(Collection<String> alias)
/*     */   {
/* 309 */     throw new Error("Unresolved compilation problems: \n\tThe method keySet() is undefined for the type List<PhasedIntegerGenotypeData>\n\tdataL cannot be resolved\n\trecSites cannot be resolved\n\tviterbi cannot be resolved\n");
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
/*     */   public int size()
/*     */   {
/* 324 */     throw new Error("Unresolved compilation problem: \n\tdataL cannot be resolved\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/LikelihoodDataCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */