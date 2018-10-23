/*    */ package lc1.association.io;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.logging.Logger;
/*    */ import pal.datatype.Nucleotides;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Data
/*    */ {
/*    */   String[] breed;
/*    */   Set<String> breed_types;
/* 16 */   boolean charSNP = false;
/* 17 */   Comparator<IntSNP> comp = new Comparator() {
/*    */     public int compare(IntSNP snp1, IntSNP snp2) {
/* 19 */       if (snp1.maf_freq < snp2.maf_freq) return -1;
/* 20 */       if (snp1.maf_freq > snp2.maf_freq) return 1;
/* 21 */       return 0;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/* 26 */   int count1 = 0;
/*    */   
/*    */ 
/* 29 */   int count2 = 0;
/* 30 */   Collection<Integer> del = new ArrayList();
/*    */   
/*    */ 
/* 33 */   Nucleotides dna = new Nucleotides();
/*    */   
/*    */   int num_individuals;
/*    */   
/*    */   Number[] phenotype;
/*    */   
/*    */ 
/*    */   Data() {}
/*    */   
/*    */ 
/*    */   Data(int no_individuals)
/*    */   {
/* 45 */     setIndividuals(no_individuals);
/*    */   }
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
/*    */   abstract Integer currentChromosome()
/*    */     throws Exception;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   Collection<Integer> getIgnored()
/*    */   {
/* 70 */     return this.del;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   abstract boolean hasMore();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   abstract SNP readSnp();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   abstract void seek(short paramShort, String paramString)
/*    */     throws Exception;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   void setIndividuals(int no_individuals)
/*    */   {
/* 96 */     this.phenotype = new Number[no_individuals];
/* 97 */     this.num_individuals = no_individuals;
/* 98 */     this.breed = new String[no_individuals];
/* 99 */     Logger.getAnonymousLogger().info("there are " + no_individuals + " individuals");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/association/io/Data.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */