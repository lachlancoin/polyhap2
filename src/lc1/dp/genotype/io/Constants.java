/*     */ package lc1.dp.genotype.io;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import org.apache.commons.cli.CommandLine;
/*     */ import org.apache.commons.cli.OptionBuilder;
/*     */ import org.apache.commons.cli.Options;
/*     */ import org.apache.commons.cli.Parser;
/*     */ import org.apache.commons.cli.PosixParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Constants
/*     */ {
/*  16 */   static final Options OPTIONS = new Options() {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final boolean CHECK = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final boolean CHECK1 = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  31 */   private static boolean fast = true;
/*  32 */   public static Random rand = new Random(System.currentTimeMillis());
/*     */   
/*     */   static void setSimulationParams() {
/*  35 */     addMissing = 0.014D;
/*  36 */     convertHemizygousToHomozygous = true;
/*     */     
/*  38 */     maxIndiv = 100;
/*     */   }
/*     */   
/*  41 */   private static double addMissing = 0.0D;
/*  42 */   private static boolean convertHemizygousToHomozygous = false;
/*  43 */   private static double addError = 0.0D;
/*  44 */   private static boolean averageNullModel = false;
/*  45 */   private static boolean fillGaps = true;
/*  46 */   private static boolean correctErrors = false;
/*  47 */   private static int maxIndiv = Integer.MAX_VALUE;
/*  48 */   private static boolean modelNull = false;
/*  49 */   private static double[] noCopies = { 1.0D, 0.0D, 0.0D };
/*  50 */   private static int numFounders = 4;
/*  51 */   private static int numItExp = 25;
/*  52 */   private static int numItFree = 0;
/*  53 */   private static int numRep = 1;
/*  54 */   private static double[] thresh = { 0.0D, 0.5D, 0.6D, 0.7D, 0.8D, 0.9D, 0.95D };
/*  55 */   private static boolean trainBackgroundNullModel = true;
/*  56 */   private static boolean trainEmissions = true;
/*  57 */   private static boolean trainTransitions = true;
/*  58 */   private static double u_global = 2.0D;
/*  59 */   private static double probHomozygousIsHemizygous = 0.5D;
/*  60 */   private static double probMiscall = 0.05D;
/*  61 */   private static double probGapIsNotGap = 0.05D;
/*  62 */   private static double probNonGapIsGap = 0.05D;
/*  63 */   private static int noSamplesFromHMM = 100;
/*  64 */   static double frac = 1.0D;
/*  65 */   static double start = 0.01D;
/*  66 */   static double stateProbOfNull = 0.05D;
/*  67 */   static double modify = 0.9D;
/*     */   
/*  69 */   public static void setPhasingOptions() { addMissing = 0.0D;
/*  70 */     addError = 0.0D;
/*  71 */     stateProbOfNull = 0.05D;
/*  72 */     averageNullModel = false;
/*  73 */     correctErrors = false;
/*  74 */     fillGaps = false;
/*  75 */     maxIndiv = Integer.MAX_VALUE;
/*  76 */     modelNull = false;
/*  77 */     noCopies = new double[] { 0.0D, 1.0D, 0.0D };
/*  78 */     numFounders = 2;
/*  79 */     frac = 0.25D;
/*  80 */     start = 40.0D;
/*  81 */     numItExp = 25;
/*  82 */     numItFree = 5;
/*  83 */     numRep = 1;
/*  84 */     thresh = new double[] { 0.0D };
/*  85 */     trainBackgroundNullModel = false;
/*  86 */     trainEmissions = false;
/*  87 */     trainTransitions = true;
/*  88 */     u_global = 2.0D;
/*  89 */     probHomozygousIsHemizygous = 0.0D;
/*  90 */     probMiscall = 0.0D;
/*  91 */     probGapIsNotGap = 0.0D;
/*  92 */     modify = -1.0D;
/*     */   }
/*     */   
/*     */   public static void setMousePhaseOptions(int numF) {
/*  96 */     averageNullModel = false;
/*  97 */     fillGaps = false;
/*  98 */     correctErrors = false;
/*  99 */     maxIndiv = 1500;
/* 100 */     modelNull = false;
/* 101 */     stateProbOfNull = 0.05D;
/* 102 */     numFounders = numF;
/*     */     
/* 104 */     numItExp = 50;
/* 105 */     numItFree = 0;
/* 106 */     numRep = 1;
/* 107 */     trainBackgroundNullModel = false;
/* 108 */     trainEmissions = true;
/* 109 */     trainTransitions = true;
/* 110 */     frac = trainEmissions ? 0.9D : 1.0D;
/* 111 */     start = trainEmissions ? 2.0D : 0.01D;
/* 112 */     u_global = 2.0D;
/* 113 */     probHomozygousIsHemizygous = 0.0D;
/* 114 */     probMiscall = 0.0D;
/* 115 */     probGapIsNotGap = 1.0D;
/* 116 */     noSamplesFromHMM = 100;
/* 117 */     modify = -1.0D;
/*     */   }
/*     */   
/* 120 */   private static double[] parse(CommandLine params, String opt, double[] def) { if (params.hasOption(opt)) {
/* 121 */       String[] cop = params.getOptionValues(opt);
/* 122 */       double[] noCopies = new double[cop.length];
/* 123 */       for (int i = 0; i < cop.length; i++) {
/* 124 */         noCopies[i] = Double.parseDouble(cop[i]);
/*     */       }
/* 126 */       return noCopies;
/*     */     }
/* 128 */     return def;
/*     */   }
/*     */   
/*     */   public static void parse(String[] args) throws Exception {
/* 132 */     Parser parser = new PosixParser();
/* 133 */     CommandLine params = parser.parse(OPTIONS, args);
/* 134 */     if (params.hasOption("type")) {
/* 135 */       if (params.getOptionValue("type").equals("phase")) setPhasingOptions();
/*     */     }
/*     */     else
/*     */     {
/* 139 */       addMissing = Double.parseDouble(params.getOptionValue("addMissing", addMissing));
/* 140 */       noCopies = parse(params, "noCopies", noCopies);
/* 141 */       numFounders = Integer.parseInt(params.getOptionValue("numF", numFounders));
/* 142 */       numRep = Integer.parseInt(params.getOptionValue("numRep", numRep));
/* 143 */       numItExp = Integer.parseInt(params.getOptionValue("numIt", numItExp));
/*     */     }
/*     */   }
/*     */   
/*     */   public static double probGapIsNotGap() {
/* 148 */     return probGapIsNotGap;
/*     */   }
/*     */   
/* 151 */   public static double probNonGapIsGap() { return probNonGapIsGap; }
/*     */   
/*     */   public static double addMissing() {
/* 154 */     return addMissing;
/*     */   }
/*     */   
/* 157 */   public static boolean averageNullModel() { return averageNullModel; }
/*     */   
/*     */ 
/*     */   public static boolean fast()
/*     */   {
/* 162 */     return fast;
/*     */   }
/*     */   
/* 165 */   public static boolean fillGaps() { return fillGaps; }
/*     */   
/*     */   public static boolean correctErrors()
/*     */   {
/* 169 */     return correctErrors;
/*     */   }
/*     */   
/*     */   public static boolean convertHemizygousToHomozygous() {
/* 173 */     return convertHemizygousToHomozygous;
/*     */   }
/*     */   
/*     */   public static int getMax(double[] d) {
/* 177 */     double max = d[0];
/* 178 */     int max_id = 0;
/* 179 */     for (int i = 0; i < d.length; i++) {
/* 180 */       if (d[i] > max) {
/* 181 */         max_id = i;
/* 182 */         max = d[i];
/*     */       }
/*     */     }
/* 185 */     return max_id;
/*     */   }
/*     */   
/* 188 */   public static double[] getThresh() { return thresh; }
/*     */   
/*     */   public static int maxIndiv() {
/* 191 */     return maxIndiv;
/*     */   }
/*     */   
/* 194 */   public static boolean modelNull() { return modelNull; }
/*     */   
/*     */   public static int nextInt(int tot)
/*     */   {
/* 198 */     return rand.nextInt(tot);
/*     */   }
/*     */   
/*     */   public static double[] noCopies()
/*     */   {
/* 203 */     return noCopies;
/*     */   }
/*     */   
/* 206 */   public static int numF() { return numFounders; }
/*     */   
/*     */   public static int numItExp() {
/* 209 */     return numItExp;
/*     */   }
/*     */   
/*     */   public static int numItFree() {
/* 213 */     return numItFree;
/*     */   }
/*     */   
/* 216 */   public static int numRep() { return numRep; }
/*     */   
/*     */ 
/*     */   public static int sample(double[] d)
/*     */   {
/* 221 */     return sample(d, 1.0D);
/*     */   }
/*     */   
/*     */   public static int sample(double[] d, double sum) {
/* 225 */     double ra = rand.nextDouble() * sum;
/* 226 */     double cum = 0.0D;
/* 227 */     for (int i = 0; i < d.length; i++) {
/* 228 */       cum += d[i];
/* 229 */       if (ra < cum) return i;
/*     */     }
/* 231 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public static boolean trainAverageNullModel() {
/* 235 */     return trainBackgroundNullModel;
/*     */   }
/*     */   
/* 238 */   public static boolean trainEmissions() { return trainEmissions; }
/*     */   
/*     */   public static boolean trainTransitions() {
/* 241 */     return trainTransitions;
/*     */   }
/*     */   
/* 244 */   public static double u_global() { return u_global; }
/*     */   
/*     */   public static double modelError() {
/* 247 */     return probMiscall;
/*     */   }
/*     */   
/* 250 */   public static double addError() { return addError; }
/*     */   
/*     */   public static double modelHemizygous() {
/* 253 */     return probHomozygousIsHemizygous;
/*     */   }
/*     */   
/*     */   public static boolean writeHMM()
/*     */   {
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   public static int noSamples() {
/* 262 */     return noSamplesFromHMM;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Iterator<Double> pseudoIterator(int noIndiv)
/*     */   {
/* 268 */     new Iterator() {
/* 269 */       double current = Constants.start;
/*     */       
/* 271 */       public boolean hasNext() { return true; }
/*     */       
/*     */       public Double next()
/*     */       {
/* 275 */         this.current *= Constants.frac;
/* 276 */         return Double.valueOf(this.current);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void remove() {}
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static double stateProbOfNull()
/*     */   {
/* 289 */     return stateProbOfNull;
/*     */   }
/*     */   
/* 292 */   public static double modify() { return modify; }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/Constants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */