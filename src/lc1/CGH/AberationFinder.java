/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.SortedSet;
/*     */ import org.apache.commons.cli.Option;
/*     */ import org.apache.commons.cli.Options;
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
/*     */ public class AberationFinder
/*     */ {
/*     */   public static double lowIntens;
/*     */   public static int noOfSnps;
/*     */   public static int[] maf;
/*     */   public static int overlapThresh;
/*     */   public static int requiredNumberOfProbesForFP;
/*     */   public static double overlapFracForTrueMatch;
/*     */   public static double threshold;
/*     */   public static double overlapThresh1;
/*     */   public static int mode;
/*     */   public static double merge;
/*     */   public static boolean includeCN;
/*     */   public static boolean includeName;
/*     */   public static boolean add185KProbes;
/*     */   public static boolean add244KProbes;
/*     */   public static boolean addADM1Probes;
/*     */   public static int[] reg;
/*     */   public static int mult;
/*     */   public static int endEffects;
/*     */   public static boolean includeFrench;
/*     */   public static String[] type;
/*     */   public static boolean includeSingle;
/*     */   public static boolean randomLoc;
/*     */   public static boolean limit;
/*     */   static final Options OPTIONS;
/*     */   public static FileFilter ff_top;
/*     */   static FileFilter ff_mid;
/*     */   static long gap;
/*     */   public static boolean extract;
/*     */   
/*     */   public static double merge()
/*     */   {
/*  86 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*  89 */   public static boolean includeCN() { throw new Error("Unresolved compilation problem: \n"); }
/*     */   
/*     */   public static boolean includeName() {
/*  92 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public static Map<String, List<File>> getDirs1(File user)
/*     */   {
/* 125 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public static File[] getDirs(File user)
/*     */   {
/* 139 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/* 142 */   public static File[] getFiles(File user) { throw new Error("Unresolved compilation problem: \n"); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object[] getChromDir(File user)
/*     */   {
/* 150 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public static Runnable makeAbFinder(File par, File[] user, String[] type, PrintWriter out, Map<String, Number> sum, String chrom, Location region)
/*     */     throws Exception
/*     */   {
/* 164 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 175 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public static List<Integer> readPosInfo(File f, int index, boolean header)
/*     */     throws Exception
/*     */   {
/* 298 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   private static Integer sum(SortedSet<Map.Entry<Integer, Integer>> name)
/*     */   {
/* 320 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public static BufferedReader getBufferedReader(File dir, String name1)
/*     */     throws Exception
/*     */   {
/* 336 */     throw new Error("Unresolved compilation problems: \n\tTarInputStream cannot be resolved to a type\n\tTarInputStream cannot be resolved to a type\n\tTarEntry cannot be resolved to a type\n\tTarInputStream cannot be resolved to a type\n\tTarInputStream cannot be resolved to a type\n\tTarEntry cannot be resolved to a type\n");
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
/*     */   public static void printOptions(PrintWriter pw)
/*     */   {
/* 369 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public static void parse(String[] args)
/*     */     throws Exception
/*     */   {
/* 401 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   private static int[] parse1(Option params)
/*     */   {
/* 459 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] parse1(String[] cop, String def)
/*     */   {
/* 467 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static double[] parse(Option params)
/*     */   {
/* 477 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean limit()
/*     */   {
/* 485 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/AberationFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */