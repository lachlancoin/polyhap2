/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import calc.LDCalculator;
/*     */ import java.io.File;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import lc1.CGH.Aberation;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.ensj.PedigreeDataCollection;
/*     */ import lc1.stats.ProbabilityDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ public abstract class DataC
/*     */ {
/*  28 */   public String name = "-";
/*     */   public Integer length;
/*  30 */   public List<String> snpid = new ArrayList();
/*  31 */   public List<Integer> loc = new ArrayList();
/*  32 */   public List<Character> majorAllele = new ArrayList();
/*  33 */   public List<Character> minorAllele = new ArrayList();
/*  34 */   public List<Double> recesiveGenoFreq = new ArrayList();
/*     */   
/*     */   public EmissionState maf;
/*     */   
/*     */ 
/*     */   public abstract int noCopies(String paramString);
/*     */   
/*     */   public abstract void fillLikelihoodData(double paramDouble, int[] paramArrayOfInt);
/*     */   
/*     */   public abstract void writeSNPFile(File paramFile, String paramString, boolean paramBoolean, Collection<Integer> paramCollection)
/*     */     throws Exception;
/*     */   
/*     */   public Double scoreChi(int i)
/*     */   {
/*  48 */     return Double.valueOf(1.0D);
/*     */   }
/*     */   
/*     */   public EmissionStateSpace[] getNoCopies() {
/*  52 */     SortedMap<Integer, EmissionStateSpace> ss = new TreeMap();
/*     */     
/*  54 */     for (Iterator<PIGData> it = iterator(); it.hasNext();) {
/*  55 */       PIGData data = (PIGData)it.next();
/*  56 */       int noCop = data.noCopies();
/*  57 */       if (!ss.containsKey(Integer.valueOf(noCop - 1))) {
/*  58 */         String name = data.getName();
/*  59 */         EmissionState stat = getL(name);
/*  60 */         EmissionStateSpace emstsp = stat == null ? 
/*  61 */           Emiss.getEmissionStateSpace(noCop - 1) : stat.getEmissionStateSpace();
/*     */         
/*  63 */         ss.put(Integer.valueOf(noCop - 1), emstsp);
/*     */       }
/*     */     }
/*  66 */     EmissionStateSpace[] res = new EmissionStateSpace[((Integer)ss.lastKey()).intValue() + 1];
/*  67 */     for (int i = 0; i < res.length; i++) {
/*  68 */       res[i] = ((EmissionStateSpace)ss.get(Integer.valueOf(i)));
/*     */     }
/*  70 */     return res;
/*     */   }
/*     */   
/*     */   public Double[] scoreChi1(int i, boolean armitage, boolean useEmSt, int phenIndex) {
/*  74 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int count(int[] paramArrayOfInt, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract Double[] scoreChi(double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract String getInfo(String paramString1, String paramString2, int paramInt, boolean paramBoolean)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void reverse();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int[][] getSourcePositions();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void mix();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract EmissionState makeMafState(EmissionStateSpace paramEmissionStateSpace);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract DataC clone();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int noAllelles();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void extractFromTrioData();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void calculateMaf(boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract EmissionState calculateMaf1();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void printTrioData(PrintWriter paramPrintWriter);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract PIGData get(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int countSwitches();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract List<Integer> getBlockBoundaries();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void getBlockBoundaries(String paramString, Set<Integer> paramSet);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void restricToAlias(Collection<String> paramCollection);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract EmissionState getL(String paramString);
/*     */   
/*     */ 
/*     */   public abstract int getFirstIndexAbove(long paramLong);
/*     */   
/*     */ 
/*     */   public abstract int getIndex(Integer paramInteger);
/*     */   
/*     */ 
/*     */   public abstract int length();
/*     */   
/*     */ 
/*     */   public abstract void printHapMapFormat(File paramFile, String paramString)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract void printHapMapFormat(PrintWriter paramPrintWriter, Collection<Integer> paramCollection, boolean paramBoolean)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract void printHapMapFormat(PrintWriter paramPrintWriter, Collection<Integer> paramCollection, boolean paramBoolean, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract int size();
/*     */   
/*     */ 
/*     */   public abstract void writeDickFormat(File paramFile, boolean paramBoolean, Collection<String> paramCollection, Collection<Integer> paramCollection1)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract void getDeletedPositions(double paramDouble, SortedSet<Integer> paramSortedSet1, SortedSet<Integer> paramSortedSet2);
/*     */   
/*     */ 
/*     */   public abstract void printLocations(PrintWriter paramPrintWriter);
/*     */   
/*     */ 
/*     */   public abstract List<Aberation> getDeletedPositions();
/*     */   
/*     */ 
/*     */   public abstract void printDeletedPositions(PrintWriter paramPrintWriter);
/*     */   
/*     */ 
/*     */   public abstract void writeLocation(PrintWriter paramPrintWriter, Collection<Integer> paramCollection);
/*     */   
/*     */ 
/*     */   public abstract void writeFastphase(Map<String, PIGData> paramMap, Map<String, double[]> paramMap1, PrintWriter paramPrintWriter, boolean paramBoolean1, boolean paramBoolean2, Collection<Integer> paramCollection)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract void writeFastphase(Map<String, PIGData> paramMap, Map<String, double[]> paramMap1, PrintWriter paramPrintWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Collection<Integer> paramCollection)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract void writeFastphase1(PrintWriter paramPrintWriter, Map<String, double[]> paramMap)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract List<String> getNames();
/*     */   
/*     */ 
/*     */   public abstract void trim(int paramInt);
/*     */   
/*     */ 
/*     */   public abstract List<Integer> getLocations();
/*     */   
/*     */ 
/*     */   public abstract void replace(Map<String, PIGData> paramMap);
/*     */   
/*     */ 
/*     */   public abstract void arrangeDataAccordingToPedigree();
/*     */   
/*     */ 
/*     */   public abstract void replaceL(Map<String, EmissionState> paramMap);
/*     */   
/*     */ 
/*     */   public abstract Map<String, PIGData> arrangeDataAccordingToPedigree(Map<String, PIGData> paramMap);
/*     */   
/*     */ 
/*     */   public abstract Iterator<PIGData> iterator();
/*     */   
/*     */ 
/*     */   public abstract void set(int paramInt, PIGData paramPIGData);
/*     */   
/*     */ 
/*     */   public abstract void setPedigree(PedigreeDataCollection paramPedigreeDataCollection);
/*     */   
/*     */ 
/*     */   public abstract Set<String> getKeys();
/*     */   
/*     */ 
/*     */   public abstract double[][] checkConsistency();
/*     */   
/*     */ 
/*     */   public abstract double[] checkConsistency(Map<String, PIGData> paramMap);
/*     */   
/*     */ 
/*     */   public abstract boolean containsKey(String paramString);
/*     */   
/*     */ 
/*     */   public abstract void summarise();
/*     */   
/*     */ 
/*     */   public abstract double[] calcLDAverage();
/*     */   
/*     */ 
/*     */   public abstract double[][] calcLD();
/*     */   
/*     */ 
/*     */   public abstract double[][] calcLD(String paramString1, String paramString2);
/*     */   
/*     */ 
/*     */   public abstract void split();
/*     */   
/*     */ 
/*     */   public abstract void calcLD(LDCalculator paramLDCalculator1, LDCalculator paramLDCalculator2, PIGData paramPIGData, double[][] paramArrayOfDouble);
/*     */   
/*     */ 
/*     */   public abstract List<Double> calcLD(LDCalculator paramLDCalculator, PIGData paramPIGData, boolean paramBoolean, int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */   public abstract double[][] getLD(int paramInt1, int paramInt2, String[] paramArrayOfString1, String[] paramArrayOfString2);
/*     */   
/*     */ 
/*     */   public abstract int countHaplotypes(int[] paramArrayOfInt1, int[] paramArrayOfInt2, String[] paramArrayOfString);
/*     */   
/*     */ 
/*     */   public abstract void printIndiv(PrintWriter paramPrintWriter);
/*     */   
/*     */ 
/*     */   public abstract double[] uncertaintyPhase(String paramString);
/*     */   
/*     */ 
/*     */   public abstract double[] uncertaintyState(String paramString);
/*     */   
/*     */ 
/*     */   public abstract double[] uncertaintyVitPhase(String paramString);
/*     */   
/*     */ 
/*     */   public abstract void setData(String paramString, PIGData paramPIGData);
/*     */   
/*     */ 
/*     */   public abstract void setViterbi(String paramString, PIGData paramPIGData);
/*     */   
/*     */ 
/*     */   public abstract void setRecSites(String paramString, SortedMap<Integer, Integer>[] paramArrayOfSortedMap);
/*     */   
/*     */ 
/*     */   public abstract void clearViterbi();
/*     */   
/*     */ 
/*     */   public abstract Iterator<EmissionState> dataLvalues();
/*     */   
/*     */ 
/*     */   public abstract void clearData();
/*     */   
/*     */ 
/*     */   public abstract SortedMap<Integer, Integer>[] recSites(String paramString);
/*     */   
/*     */ 
/*     */   public abstract void writeFastphase(PrintWriter paramPrintWriter, boolean paramBoolean)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   public abstract EmissionState maf();
/*     */   
/*     */ 
/*     */   public abstract Object ped();
/*     */   
/*     */ 
/*     */   public abstract EmissionState dataL(String paramString);
/*     */   
/*     */ 
/*     */   public abstract EmissionState getState(String paramString, EmissionStateSpace paramEmissionStateSpace);
/*     */   
/*     */ 
/*     */   public double weight(String key)
/*     */   {
/* 330 */     double[] weights = Constants.weights();
/* 331 */     List<Short> dataIndices = ((HaplotypeEmissionState)dataL(key)).getDataIndices();
/* 332 */     double max = 0.0D;
/* 333 */     for (int i = 0; i < dataIndices.size(); i++) {
/* 334 */       double wt = weights[((Short)dataIndices.get(i)).shortValue()];
/* 335 */       if (wt > max) max = wt;
/*     */     }
/* 337 */     return max;
/*     */   }
/*     */   
/*     */   public abstract void writeDickFormat1(File paramFile, boolean paramBoolean) throws Exception;
/*     */   
/*     */   public String[] getUnderlyingDataSets() {
/* 343 */     return new String[] { this.name };
/*     */   }
/*     */   
/*     */   public abstract ProbabilityDistribution[] numLevels();
/*     */   
/*     */   public abstract Phenotypes pheno();
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/DataC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */