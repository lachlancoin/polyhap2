/*      */ package lc1.util;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Logger;
/*      */ import org.apache.commons.cli.CommandLine;
/*      */ import org.apache.commons.cli.Option;
/*      */ import org.apache.commons.cli.OptionBuilder;
/*      */ import org.apache.commons.cli.Options;
/*      */ import org.apache.commons.cli.Parser;
/*      */ import org.apache.commons.cli.PosixParser;
/*      */ 
/*      */ public class Constants
/*      */ {
/*   29 */   public static double samplePermute = 0.0D;
/*      */   
/*      */   public static double samplePermute() {
/*   32 */     return samplePermute;
/*      */   }
/*      */   
/*   35 */   public static String[] inputFiles = { "" };
/*   36 */   public static double missingRate = 0.0D;
/*   37 */   public static int phasingType = 0;
/*   38 */   public static String inputGenotype = "";
/*   39 */   public static String inputSample = "";
/*      */   
/*   41 */   public static int distanceLD = 0;
/*   42 */   public static String[] mid = { "" };
/*   43 */   public static int offset = 0;
/*   44 */   public static int[] restrict = { 2147483637 };
/*   45 */   public static int[] maxIndiv = { Integer.MAX_VALUE };
/*   46 */   public static int[] restrictKb = { 250 };
/*   47 */   public static int[] numIt = { 15 };
/*   48 */   public static int numRep = 1;
/*   49 */   public static int numIteForHap = 0;
/*      */   
/*      */   public static final boolean CHECK = false;
/*      */   
/*   53 */   public static boolean realRandom = true;
/*   54 */   public static Random rand = new Random(realRandom ? System.currentTimeMillis() : 2553436L);
/*   55 */   private static boolean fast = true;
/*   56 */   private static boolean cache = true;
/*   57 */   private static boolean trainWithGenotypes = true;
/*   58 */   public static String baseDir = ".";
/*   59 */   public static int modelCNP = 6;
/*   60 */   public static int modelCNPInternal = 3;
/*   61 */   public static int modelCNPnonInternal = 6;
/*      */   
/*   63 */   public static int[] mid() { int[] mid_ = new int[2];
/*   64 */     mid_[0] = Integer.parseInt(mid[1]);
/*   65 */     mid_[1] = (mid.length == 2 ? mid_[0] : Integer.parseInt(mid[2]));
/*   66 */     return mid_; }
/*      */   
/*   68 */   public static String[] build = { "build35.txt" };
/*      */   
/*      */ 
/*      */ 
/*   72 */   public static String build(int i) { return build[i]; }
/*      */   
/*   74 */   public static boolean countDataType = false;
/*      */   
/*   76 */   public static boolean countDataType() { return countDataType; }
/*      */   
/*   78 */   public static double probCrossOverBetweenBP = 1.1E-8D;
/*   79 */   private static int index = 1;
/*      */   
/*      */ 
/*   82 */   public static int maxNoCopies = 4;
/*   83 */   public static int[] noCopies = { 2 };
/*   84 */   public static char[] modify0 = null;
/*   85 */   public static char[] modify0nonInternal = null;
/*   86 */   public static char[] modify0Internal = null;
/*   87 */   public static char[] modify1 = null;
/*   88 */   public static String[] modifyFrac0 = null;
/*   89 */   public static String[] modifyFrac1 = null;
/*      */   
/*      */ 
/*   92 */   private static Boolean male = null;
/*      */   
/*   94 */   public static boolean keepSamples = false;
/*      */   
/*   96 */   public static boolean keepSamples() { return keepSamples; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  104 */   public static int[] transMode0 = { 4 };
/*  105 */   public static int[] transMode1 = null;
/*  106 */   public static int[] transMode0nonInternal = { 4 };
/*  107 */   public static int[] transMode0Internal = { 4 };
/*      */   
/*      */ 
/*  110 */   public static double[] u_global = { 10.0D, 10.0D, 10.0D, 100.0D };
/*  111 */   public static double[] u_global1 = { 10.0D, 10.0D, 10.0D, 100.0D };
/*  112 */   public static double[] frac = { 1.0D, 1.0D, 1.0D, 1.0D, 1.0D };
/*  113 */   public static double[] start = { 1.0E-5D, 1.0E-5D, 1.0E-5D, 50.0D, 1.0E-5D };
/*  114 */   public static double[] limit = { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
/*  115 */   public static double[] index_to_start = { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
/*  116 */   public static int[] freq_update = { 0001 };
/*      */   
/*  118 */   public static double[] frac1 = { 1.0D, 1.0D, 1.0D, 1.0D, 1.0D };
/*  119 */   public static double[] start1 = { 1.0E-5D, 1.0E-5D, 1.0E-5D, 1.0E-5D, 1.0E-5D };
/*  120 */   public static double[] limit1 = { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
/*  121 */   public static double[] index_to_start1 = { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
/*  122 */   public static int[] freq_update1 = { 0001 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  127 */   private static boolean initialise = false;
/*      */   
/*  129 */   public static boolean fillGaps = true;
/*  130 */   public static int noSamplesFromHMM = 1;
/*  131 */   private static boolean unwrapForSampling = false;
/*      */   
/*  133 */   private static boolean sample = true;
/*      */   
/*  135 */   private static boolean sampleWithPedigree = false;
/*  136 */   private static boolean trainWithPedigree = false;
/*      */   
/*  138 */   public static String[] format = { "hapmap" };
/*      */   
/*      */ 
/*      */ 
/*  142 */   public static String out = "summary80.txt";
/*  143 */   public static String type = "";
/*      */   
/*      */   public static String dir;
/*  146 */   public static boolean runFastPhase = false;
/*      */   
/*  148 */   private static String bin = "";
/*      */   
/*  150 */   public static int writeHMM = 1;
/*  151 */   private static double[] hotspot = { 100.0D, 1.0D, 1.0D };
/*      */   
/*      */ 
/*      */ 
/*  155 */   public static boolean onlyCopyNo = false;
/*  156 */   public static boolean resample = false;
/*  157 */   private static boolean readPedigree = false;
/*  158 */   public static double[] expModelIntHotSpot = { 100.0D, 100.0D, 100.0D };
/*      */   
/*      */ 
/*  161 */   private static boolean annotate = false;
/*  162 */   private static double sampleThresh = 0.5D;
/*  163 */   public static double precision = 0.1D;
/*      */   private static int indexToTrainSWHMM;
/*  165 */   private static double u_exp = 0.001D;
/*  166 */   private static double[] initExpTrans = null;
/*      */   
/*  168 */   public static String[] inputDir = { "./" };
/*  169 */   public static boolean keepBest = false;
/*      */   
/*  171 */   public static int prime = 0;
/*      */   
/*  173 */   private static boolean xchrom = false;
/*  174 */   private static double pseudoCountWeightClumping = 0.0D;
/*      */   
/*  176 */   public static double initialConcentration = 0.0D;
/*  177 */   public static double exclude = 0.0D;
/*  178 */   public static int modifyWithData = 0;
/*      */   
/*  180 */   public static int end = Integer.MAX_VALUE;
/*      */   
/*      */ 
/*  183 */   public static double[] cgh_x = { 0.0D, 0.5D, 1.0D, 1.5D, 2.0D, 3.0D, 4.0D };
/*      */   
/*  185 */   public static double[] cgh_mean = { -2.5D, -1.0D, 0.0D, 0.58D, 1.0D, 1.58D, 2.0D };
/*      */   
/*      */ 
/*  188 */   public static double[] cgh_var = { 0.3D, 0.3D, 0.3D, 0.3D, 0.3D, 0.3D, 0.3D };
/*      */   
/*  190 */   public static double[] cgh_skew = { -0.01D, -0.01D, 0.0D, 0.0D, 0.01D, 0.01D, 0.01D };
/*      */   
/*      */ 
/*  193 */   public static double[] r_x = { 0.0D, 0.5D, 1.0D, 1.5D, 2.0D, 3.0D, 4.0D };
/*      */   
/*  195 */   public static double[] r_var = { 0.3D, 0.3D, 0.3D, 0.3D, 0.3D, 0.3D, 0.3D };
/*      */   
/*      */ 
/*  198 */   public static double[] r_skew = { -0.02D, -0.01D, 0.0D, 0.01D, 0.02D, 3.0D, 4.0D };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  203 */   public static double[] r_mean = { -1.0D, -0.53D, 0.0D, 0.35D, 0.54D, 1.09D, 1.38D };
/*      */   
/*  205 */   public static double[] b_mean = { 0.0D, 0.25D, 0.3333333333333333D, 0.5D, 0.6666666666666666D, 0.75D, 1.0D };
/*      */   
/*  207 */   public static double[] b_var = { 0.03D, 0.03D, 0.03D, 0.05D, 0.03D, 0.03D, 0.03D };
/*      */   
/*  209 */   public static double[] b_skew = { 1.0E10D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, -1.0E10D };
/*      */   
/*  211 */   public static double r_train = 1.0D;
/*  212 */   public static double b_train = 1.0D;
/*  213 */   public static boolean trainCGH = false;
/*  214 */   public static double[] meanvarskewprior = { 1.0D, 10.0D, 0.1D };
/*  215 */   public static double[] r_prior = { 1.0D };
/*      */   
/*  217 */   public static double[] r_x() { return r_x; }
/*      */   
/*      */   public static double[] r_var() {
/*  220 */     return r_var;
/*      */   }
/*      */   
/*  223 */   public static double[] r_mean() { return r_mean; }
/*      */   
/*      */   public static double[] r_skew() {
/*  226 */     return r_skew;
/*      */   }
/*      */   
/*  229 */   public static double r_train() { return r_train; }
/*      */   
/*      */   public static double b_train() {
/*  232 */     return b_train;
/*      */   }
/*      */   
/*  235 */   public static double[] meanvarskewprior() { return meanvarskewprior; }
/*      */   
/*      */   static {
/*      */     try {
/*  239 */       dir = new File(System.getProperties().getProperty("user.dir")).getAbsolutePath();
/*      */     } catch (Exception exc) {
/*  241 */       exc.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean onlyCopyNo() {
/*  246 */     return onlyCopyNo;
/*      */   }
/*      */   
/*      */   public static boolean resample()
/*      */   {
/*  251 */     return resample;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean readPedigree()
/*      */   {
/*  259 */     return readPedigree;
/*      */   }
/*      */   
/*      */ 
/*      */   public static double expModelIntHotSpot(int i)
/*      */   {
/*  265 */     return expModelIntHotSpot[i];
/*      */   }
/*      */   
/*      */ 
/*      */   private static double[] parse(Option params)
/*      */   {
/*  271 */     String[] cop = params.getValues();
/*  272 */     Logger.global.info("parsing " + Arrays.asList(cop));
/*  273 */     double[] noCopies = new double[cop.length];
/*  274 */     for (int i = 0; i < cop.length; i++) {
/*  275 */       int ind = cop[i].indexOf('/');
/*  276 */       if (ind >= 0) {
/*  277 */         double d1 = Double.parseDouble(cop[i].substring(0, ind));
/*  278 */         double d2 = Double.parseDouble(cop[i].substring(ind + 1));
/*  279 */         noCopies[i] = (d1 / d2);
/*      */       }
/*      */       else {
/*  282 */         noCopies[i] = Double.parseDouble(cop[i].startsWith("^") ? cop[i].substring(1) : cop[i]);
/*      */       } }
/*  284 */     return noCopies;
/*      */   }
/*      */   
/*  287 */   private static int[] parse1(Option params) { String[] cop = params.getValues();
/*  288 */     int[] noCopies = new int[cop.length];
/*  289 */     for (int i = 0; i < cop.length; i++) {
/*  290 */       noCopies[i] = Integer.parseInt(cop[i]);
/*      */     }
/*  292 */     return noCopies;
/*      */   }
/*      */   
/*  295 */   private static char[] parse2(Option params) { String[] cop = params.getValues();
/*  296 */     char[] noCopies = new char[cop.length];
/*  297 */     for (int i = 0; i < cop.length; i++) {
/*  298 */       noCopies[i] = cop[i].charAt(0);
/*      */     }
/*  300 */     return noCopies;
/*      */   }
/*      */   
/*      */   private static boolean[] parse3(Option params) {
/*  304 */     String[] cop = params.getValues();
/*  305 */     boolean[] noCopies = new boolean[cop.length];
/*  306 */     for (int i = 0; i < cop.length; i++) {
/*  307 */       noCopies[i] = (cop[i].toLowerCase().charAt(0) == 't' ? 1 : false);
/*      */     }
/*  309 */     return noCopies;
/*      */   }
/*      */   
/*      */   private static int[] parse1(String[] cop, String def)
/*      */   {
/*  314 */     if (cop == null) cop = def.split(":");
/*  315 */     if (cop[0] == "null") return null;
/*  316 */     int[] noCopies = new int[cop.length];
/*  317 */     for (int i = 0; i < cop.length; i++) {
/*  318 */       noCopies[i] = Integer.parseInt(cop[i]);
/*      */     }
/*  320 */     return noCopies;
/*      */   }
/*      */   
/*  323 */   public static boolean sample() { return sample; }
/*      */   
/*      */   public static boolean initialise() {
/*  326 */     return initialise;
/*      */   }
/*      */   
/*  329 */   public static CommandLine parse(String[] args, Integer column) throws Exception { return parse(args, OPTIONS, column); }
/*      */   
/*      */   public static CommandLine parse(String[] args) throws Exception {
/*  332 */     return parse(args, OPTIONS, Integer.valueOf(1));
/*      */   }
/*      */   
/*      */   public static CommandLine parseSimple(String[] args) throws Exception {
/*  336 */     Parser parser = new PosixParser();
/*  337 */     return parser.parse(OPTIONS, args, false); }
/*      */   
/*  339 */   public static String paramFile = "log.txt";
/*  340 */   public static int column = 1;
/*      */   
/*  342 */   public static CommandLine parse(String[] args, Options opti, Integer column) throws Exception { Parser parser = new PosixParser();
/*  343 */     CommandLine params = parser.parse(opti, args, false);
/*  344 */     if (params.hasOption("paramFile")) {
/*  345 */       if (params.getOptionValue("paramFile").equals("many")) return null;
/*      */       File user;
/*  347 */       File user; if ((params.hasOption("dir")) && (!params.getOptionValue("dir").equals("."))) {
/*  348 */         user = new File(params.getOptionValue("dir"));
/*      */       } else
/*  350 */         user = new File(System.getProperty("user.dir"));
/*  351 */       String[] args1 = read(new File(user, params.getOptionValue("paramFile")), column.intValue());
/*  352 */       column = column.intValue();
/*  353 */       return parse(args1, opti, null);
/*      */     }
/*  355 */     if (params.hasOption("type")) {
/*  356 */       type = params.getOptionValue("type");
/*      */     }
/*  358 */     Option[] options = params.getOptions();
/*  359 */     for (int i = 0; i < options.length; i++)
/*      */     {
/*  361 */       String argName = options[i].getLongOpt();
/*  362 */       Field field = Constants.class.getField(argName);
/*  363 */       Class type = field.getType();
/*  364 */       if (type.equals(Double.TYPE)) {
/*  365 */         String st = options[i].getValue();
/*  366 */         if (st.startsWith("^")) {
/*  367 */           st = st.substring(1);
/*      */         }
/*  369 */         field.set(null, Double.valueOf(Double.parseDouble(st)));
/*      */       }
/*  371 */       else if (type.equals(double[].class)) {
/*  372 */         field.set(null, parse(options[i]));
/*      */       }
/*  374 */       else if (type.equals(String[].class)) {
/*  375 */         field.set(null, options[i].getValues());
/*      */       }
/*  377 */       else if (type.equals(char[].class)) {
/*  378 */         field.set(null, parse2(options[i]));
/*      */       }
/*  380 */       else if (type.equals(boolean[].class)) {
/*  381 */         field.set(null, parse3(options[i]));
/*      */       }
/*  383 */       else if (type.equals(int[].class)) {
/*  384 */         field.set(null, parse1(options[i]));
/*      */       }
/*  386 */       else if (type.equals(Integer.TYPE)) {
/*  387 */         field.set(null, Integer.valueOf(Integer.parseInt(options[i].getValue())));
/*      */       }
/*  389 */       else if (type.equals(String.class)) {
/*  390 */         String val = options[i].getValue();
/*  391 */         if (field.getName().startsWith("dir")) {
/*  392 */           val = val.replace(';', ':').replace('%', ' ');
/*      */         }
/*      */         
/*  395 */         field.set(null, val);
/*      */       }
/*  397 */       else if (type.equals(Boolean.TYPE)) {
/*  398 */         if (!field.getName().equals("CHECK")) {
/*  399 */           field.set(null, Boolean.valueOf(Boolean.parseBoolean(options[i].getValue())));
/*      */         }
/*  401 */       } else if (type.equals(Boolean.class)) {
/*  402 */         field.set(null, new Boolean(Boolean.parseBoolean(options[i].getValue())));
/*      */       }
/*  404 */       else if (type.equals(URL.class)) {
/*  405 */         String val = options[i].getValue();
/*  406 */         if (argName.equals("dir")) {
/*  407 */           String[] str = options[i].getValues();
/*  408 */           StringBuffer sb = new StringBuffer();
/*  409 */           for (int ik = 0; ik < str.length; ik++) {
/*  410 */             sb.append(str[ik]);
/*  411 */             if (ik < str.length - 1) sb.append(":");
/*      */           }
/*  413 */           val = sb.toString();
/*  414 */           if ((!val.startsWith("file")) && (!val.startsWith("http"))) {
/*  415 */             File f = new File(val);
/*  416 */             field.set(null, f.toURL());
/*      */           }
/*      */           else {
/*  419 */             field.set(null, new URL(val));
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  424 */         throw new RuntimeException("unknown type " + argName + " " + type);
/*      */       }
/*  426 */       System.err.println("Set " + field.getName() + " : " + field.get(null));
/*      */     }
/*      */     
/*  429 */     return params;
/*      */   }
/*      */   
/*      */   private static String[] read(File file, int col) throws Exception
/*      */   {
/*  434 */     List<String> l = new ArrayList();
/*  435 */     BufferedReader br = new BufferedReader(new FileReader(file));
/*  436 */     String st = "";
/*  437 */     while ((st = br.readLine()) != null) {
/*  438 */       st = st.trim();
/*  439 */       if (!st.startsWith("#")) {
/*  440 */         String[] str = st.trim().split("\\s+");
/*      */         
/*      */ 
/*  443 */         if (str.length >= 2) {
/*  444 */           l.add(str[0].trim());
/*      */           
/*  446 */           if (st.startsWith("--dir")) {
/*  447 */             StringBuffer toA = new StringBuffer();
/*  448 */             for (int i = 1; i < str.length - 1; i++) {
/*  449 */               toA.append(str[i]);
/*  450 */               toA.append("%");
/*      */             }
/*  452 */             toA.append(str[(str.length - 1)]);
/*  453 */             String s = toA.toString();
/*  454 */             s = s.replace(':', ';');
/*  455 */             l.add(s);
/*      */           }
/*      */           else {
/*  458 */             int ind = str.length == 2 ? 1 : col;
/*  459 */             l.add(str[ind].trim());
/*      */           }
/*      */         }
/*      */       } }
/*  463 */     br.close();
/*  464 */     return (String[])l.toArray(new String[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean fast()
/*      */   {
/*  472 */     return fast;
/*      */   }
/*      */   
/*  475 */   public static boolean fillGaps() { return fillGaps; }
/*      */   
/*      */ 
/*      */   public static int getMax(Double[] d)
/*      */   {
/*  480 */     double max = d[0].doubleValue();
/*  481 */     int max_id = 0;
/*  482 */     for (int i = 0; i < d.length; i++) {
/*  483 */       if (d[i].doubleValue() > max) {
/*  484 */         max_id = i;
/*  485 */         max = d[i].doubleValue();
/*      */       }
/*      */     }
/*  488 */     return max_id;
/*      */   }
/*      */   
/*      */   public static int getMax(Integer[] d) {
/*  492 */     int max = d[0].intValue();
/*  493 */     int max_id = 0;
/*  494 */     for (int i = 0; i < d.length; i++) {
/*  495 */       if (d[i].intValue() > max) {
/*  496 */         max_id = i;
/*  497 */         max = d[i].intValue();
/*      */       }
/*      */     }
/*  500 */     return max_id;
/*      */   }
/*      */   
/*  503 */   public static int getMax(double[] d) { double max = d[0];
/*  504 */     int max_id = 0;
/*  505 */     for (int i = 0; i < d.length; i++) {
/*  506 */       if (d[i] > max) {
/*  507 */         max_id = i;
/*  508 */         max = d[i];
/*      */       }
/*      */     }
/*  511 */     return max_id;
/*      */   }
/*      */   
/*      */   public static int getMax(int[] d) {
/*  515 */     int max = d[0];
/*  516 */     int max_id = 0;
/*  517 */     for (int i = 0; i < d.length; i++) {
/*  518 */       if (d[i] > max) {
/*  519 */         max_id = i;
/*  520 */         max = d[i];
/*      */       }
/*      */     }
/*  523 */     return max_id;
/*      */   }
/*      */   
/*  526 */   public static int getMin(double[] d) { double min = d[0];
/*  527 */     int min_id = 0;
/*  528 */     for (int i = 0; i < d.length; i++) {
/*  529 */       if (d[i] < min) {
/*  530 */         min_id = i;
/*  531 */         min = d[i];
/*      */       }
/*      */     }
/*  534 */     return min_id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  540 */   public static int maxIndiv(int i) { return maxIndiv[i]; }
/*      */   
/*  542 */   public static String[] phenToPlot = new String[0];
/*      */   
/*  544 */   public static String[] phenToPlot() { return phenToPlot; }
/*      */   
/*  546 */   public static int segments = 4;
/*      */   
/*      */   public static int nextInt(int tot) {
/*  549 */     return rand.nextInt(tot);
/*      */   }
/*      */   
/*      */   public static int[] noCopies()
/*      */   {
/*  554 */     if (noCopies.length < format.length)
/*  555 */       return extend(noCopies, format.length);
/*  556 */     return noCopies;
/*      */   }
/*      */   
/*  559 */   public static int numF(int i) { return modify(i).length; }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int[] transMode(int i)
/*      */   {
/*  565 */     if (i == 0) {
/*  566 */       if (phasingType == 0) {
/*  567 */         return transMode0nonInternal;
/*      */       }
/*  569 */       return transMode0Internal;
/*      */     }
/*  571 */     return transMode1;
/*      */   }
/*      */   
/*      */   public static int[] numIt()
/*      */   {
/*  576 */     return numIt;
/*      */   }
/*      */   
/*  579 */   public static int numRep() { return numRep; }
/*      */   
/*      */   public static double switchU()
/*      */   {
/*  583 */     return 1.0E10D;
/*      */   }
/*      */   
/*  586 */   public static int sample(double[] d) { return sample(d, 1.0D); }
/*      */   
/*      */   public static int sample(double[] d, double sum)
/*      */   {
/*  590 */     double ra = rand.nextDouble() * sum;
/*  591 */     double cum = 0.0D;
/*  592 */     for (int i = 0; i < d.length; i++) {
/*  593 */       cum += d[i];
/*  594 */       if (ra <= cum) return i;
/*      */     }
/*  596 */     throw new RuntimeException("!!");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static double[] u_global(int i)
/*      */   {
/*  603 */     if (i == 0) return u_global;
/*  604 */     return u_global1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int writeHMM()
/*      */   {
/*  613 */     return writeHMM;
/*      */   }
/*      */   
/*  616 */   public static int[] restrict() { return restrict; }
/*      */   
/*      */   public static int noSamples() {
/*  619 */     return noSamplesFromHMM;
/*      */   }
/*      */   
/*      */   public static Iterator<double[]> pseudoIterator(final int noIndiv, int ph)
/*      */   {
/*  624 */     double[] start = ph == 0 ? start : start1;
/*  625 */     final double[] limit = ph == 0 ? limit : limit1;
/*  626 */     final int[] freq_update = ph == 0 ? freq_update : freq_update1;
/*  627 */     final double[] frac = ph == 0 ? frac : frac1;
/*  628 */     final double[] index_to_start = ph == 0 ? index_to_start : index_to_start1;
/*      */     
/*  630 */     new Iterator() {
/*  631 */       int index = 0;
/*      */       
/*  633 */       double[] current = new double[Constants.this.length];
/*  634 */       double[] res = new double[Constants.this.length];
/*      */       
/*  636 */       public boolean hasNext() { return true; }
/*      */       
/*      */ 
/*      */       public double[] next()
/*      */       {
/*  641 */         for (int i = 0; i < this.current.length; i++) {
/*  642 */           if (this.index == index_to_start[i]) {
/*  643 */             if (frac[i] < 0.0D) {
/*  644 */               this.current[i] = (Constants.this[i] * noIndiv);
/*      */             }
/*      */             else {
/*  647 */               this.current[i] = Constants.this[i];
/*      */             }
/*      */           }
/*  650 */           else if (this.index > index_to_start[i]) this.current[i] = Math.abs(this.current[i] * frac[i]); else
/*  651 */             this.current[i] = 1.0E10D;
/*  652 */           if (this.current[i] < limit[i]) { this.current[i] = limit[i];
/*      */           }
/*  654 */           if (Math.abs(Math.IEEEremainder(this.index, freq_update[i])) > 1.0E-4D) {
/*  655 */             this.res[i] = 1000000.0D;
/*      */           } else
/*  657 */             this.res[i] = this.current[i];
/*      */         }
/*  659 */         this.index += 1;
/*  660 */         return this.res;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public void remove() {}
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static char[] modify(int i)
/*      */   {
/*  676 */     if (i == 0) {
/*  677 */       if (phasingType == 0) {
/*  678 */         return modify0nonInternal;
/*      */       }
/*  680 */       return modify0Internal;
/*      */     }
/*  682 */     return modify1;
/*      */   }
/*      */   
/*      */   public static double[] extend(double[] d, int len)
/*      */   {
/*  687 */     double[] res = new double[len];
/*  688 */     System.arraycopy(d, 0, res, 0, d.length);
/*  689 */     for (int i = d.length; i < res.length; i++) {
/*  690 */       res[i] = d[(d.length - 1)];
/*      */     }
/*  692 */     return res;
/*      */   }
/*      */   
/*  695 */   public static int[] extend(int[] d, int len) { int[] res = new int[len];
/*  696 */     System.arraycopy(d, 0, res, 0, d.length);
/*  697 */     for (int i = d.length; i < res.length; i++) {
/*  698 */       res[i] = d[(d.length - 1)];
/*      */     }
/*  700 */     return res;
/*      */   }
/*      */   
/*  703 */   public static double[] r_prior() { return extend(r_prior, modify(0).length); }
/*      */   
/*      */   public static double[] modifyFrac(int i)
/*      */   {
/*  707 */     double[] res = new double[modify(i).length];
/*  708 */     double sum = 0.0D;
/*  709 */     String[] mod = i == 0 ? modifyFrac0 : modifyFrac1;
/*  710 */     for (int k = 0; 
/*  711 */         k < mod.length; k++)
/*      */     {
/*  713 */       res[k] = Double.parseDouble(mod[k]);
/*  714 */       sum += res[k];
/*      */     }
/*  716 */     if (k < res.length) {
/*  717 */       double rem = (1.0D - sum) / (res.length - k);
/*  718 */       for (; k < res.length; k++) {
/*  719 */         res[k] = rem;
/*      */       }
/*      */     }
/*  722 */     return res;
/*      */   }
/*      */   
/*      */ 
/*  726 */   public static double[] r_state_mean = new double[0];
/*  727 */   public static double[] r_state_var = new double[0];
/*  728 */   public static double[] r_state_skew = new double[0];
/*      */   
/*      */ 
/*      */ 
/*      */   public static double[][] meanvarskew(int k)
/*      */   {
/*  734 */     double[][] res = new double[format().length][3];
/*      */     
/*      */ 
/*      */ 
/*  738 */     if (k < r_state_mean.length) {
/*  739 */       for (int j = 0; j < res.length; j++) {
/*  740 */         res[j][0] = r_state_mean[k];
/*  741 */         res[j][1] = r_state_var[k];
/*  742 */         res[j][2] = r_state_skew[k];
/*      */       }
/*      */     }
/*      */     else {
/*  746 */       int k1 = r_state_mean.length - 1;
/*  747 */       for (int j = 0; j < res.length; j++) {
/*  748 */         res[j][0] = r_state_mean[k1];
/*  749 */         res[j][1] = r_state_var[k1];
/*  750 */         res[j][2] = r_state_skew[k1];
/*      */       }
/*      */     }
/*  753 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean sampleWithPedigree()
/*      */   {
/*  760 */     return sampleWithPedigree;
/*      */   }
/*      */   
/*  763 */   public static boolean trainWithPedigree() { return trainWithPedigree; }
/*      */   
/*      */   public static boolean unwrapForSampling()
/*      */   {
/*  767 */     return unwrapForSampling;
/*      */   }
/*      */   
/*      */ 
/*  771 */   public static final Options OPTIONS = new Options() {};
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDir()
/*      */   {
/*  786 */     return dir;
/*      */   }
/*      */   
/*      */   public static void printOptions(PrintWriter pw, String end) {
/*  790 */     printOptions(pw, end, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void printOptions(PrintWriter pw, String end, CommandLine options)
/*      */   {
/*      */     try
/*      */     {
/*  799 */       Field[] f = Constants.class.getFields();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  807 */       for (int i = 0; i < f.length; i++) {
/*  808 */         if (((options == null) || (options.hasOption(f[i].getName()))) && 
/*  809 */           (!Modifier.isFinal(f[i].getModifiers())) && 
/*  810 */           (Modifier.isStatic(f[i].getModifiers())) && 
/*  811 */           (!f[i].getType().equals(Random.class))) {
/*  812 */           if (f[i].getType().equals(double[].class)) {
/*  813 */             double[] val = (double[])f[i].get(null);
/*  814 */             if (val != null) {
/*  815 */               Double[] d = new Double[val.length];
/*  816 */               StringBuffer sb = new StringBuffer();
/*  817 */               for (int ik = 0; ik < d.length; ik++) {
/*  818 */                 d[ik] = Double.valueOf(val[ik]);
/*  819 */                 if (d[ik].doubleValue() < 0.0D) sb.append("^");
/*  820 */                 sb.append(d[ik]);
/*  821 */                 sb.append(ik < d.length - 1 ? ":" : "");
/*      */               }
/*  823 */               pw.print(" --" + f[i].getName() + " " + sb.toString() + end);
/*      */             }
/*      */           }
/*  826 */           else if (f[i].getType().equals(int[].class)) {
/*  827 */             int[] val = (int[])f[i].get(null);
/*  828 */             if (val != null) {
/*  829 */               Integer[] d = new Integer[val.length];
/*  830 */               StringBuffer sb = new StringBuffer();
/*  831 */               for (int ik = 0; ik < d.length; ik++) {
/*  832 */                 d[ik] = Integer.valueOf(val[ik]);
/*  833 */                 sb.append(d[ik] + (ik < d.length - 1 ? ":" : ""));
/*      */               }
/*  835 */               pw.print(" --" + f[i].getName() + " " + sb.toString() + end);
/*      */             }
/*      */           }
/*  838 */           else if (f[i].getType().equals(char[].class)) {
/*  839 */             char[] val = (char[])f[i].get(null);
/*  840 */             if (val != null) {
/*  841 */               StringBuffer sb = new StringBuffer();
/*  842 */               for (int ik = 0; ik < val.length; ik++) {
/*  843 */                 sb.append(val[ik] + (ik < val.length - 1 ? ":" : ""));
/*      */               }
/*  845 */               pw.print(" --" + f[i].getName() + " " + sb.toString() + end);
/*      */             }
/*      */           }
/*  848 */           else if (f[i].getType().equals(boolean[].class)) {
/*  849 */             boolean[] val = (boolean[])f[i].get(null);
/*  850 */             if (val != null) {
/*  851 */               StringBuffer sb = new StringBuffer();
/*  852 */               for (int ik = 0; ik < val.length; ik++) {
/*  853 */                 sb.append(val[ik] + (ik < val.length - 1 ? ":" : ""));
/*      */               }
/*  855 */               pw.print(" --" + f[i].getName() + " " + sb.toString() + end);
/*      */             }
/*      */           }
/*  858 */           else if (f[i].getType().equals(String[].class)) {
/*  859 */             String[] val = (String[])f[i].get(null);
/*  860 */             if (val != null) {
/*  861 */               StringBuffer sb = new StringBuffer();
/*  862 */               for (int ik = 0; ik < val.length; ik++) {
/*  863 */                 sb.append(val[ik] + (ik < val.length - 1 ? ":" : ""));
/*      */               }
/*  865 */               pw.print(" --" + f[i].getName() + " " + sb.toString() + end);
/*      */             }
/*      */           }
/*  868 */           else if ((f[i].getType().equals(Double.TYPE)) || (f[i].getType().equals(Double.class))) {
/*  869 */             double val = ((Double)f[i].get(null)).doubleValue();
/*  870 */             pw.print(" --" + f[i].getName() + " ");
/*  871 */             if (val < 0.0D) pw.print("^" + val); else
/*  872 */               pw.print(val);
/*  873 */             pw.print(end);
/*      */           }
/*      */           else {
/*  876 */             pw.print(" --" + f[i].getName() + " " + f[i].get(null) + end);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception exc) {
/*  882 */       exc.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean runFastPhase() {
/*  887 */     return runFastPhase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Boolean male()
/*      */   {
/*  895 */     return male;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double hotspot(int index)
/*      */   {
/*  911 */     return hotspot[index];
/*      */   }
/*      */   
/*      */   public static String inputFiles(int index) {
/*  915 */     return inputFiles[index];
/*      */   }
/*      */   
/*      */ 
/*      */   public static String[] inputFile()
/*      */   {
/*  921 */     String[] res = new String[inputDir.length];
/*  922 */     for (int i = 0; i < res.length; i++)
/*      */     {
/*      */ 
/*  925 */       res[i] = (baseDir + "/" + inputDir[i] + "/" + chrom0() + ".zip");
/*      */     }
/*  927 */     return res;
/*      */   }
/*      */   
/*      */   public static String[] inputFile1() {
/*  931 */     String[] res = new String[inputFiles.length];
/*  932 */     for (int i = 0; i < res.length; i++)
/*      */     {
/*      */ 
/*  935 */       res[i] = (baseDir + "/" + inputDir[0] + "/" + inputFiles(i) + ".zip");
/*      */     }
/*  937 */     return res;
/*      */   }
/*      */   
/*      */   public static boolean keepBest()
/*      */   {
/*  942 */     return keepBest;
/*      */   }
/*      */   
/*      */   public static int modelCNP()
/*      */   {
/*  947 */     if (phasingType == 0) {
/*  948 */       return modelCNPnonInternal;
/*      */     }
/*  950 */     return modelCNPInternal;
/*      */   }
/*      */   
/*  953 */   public static String[] getFormat() { return format; }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int offset()
/*      */   {
/*  959 */     return offset;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int prime()
/*      */   {
/*  965 */     return prime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double initExpTrans(int index)
/*      */   {
/*  975 */     return initExpTrans[index];
/*      */   }
/*      */   
/*      */   public static boolean cache()
/*      */   {
/*  980 */     return cache;
/*      */   }
/*      */   
/*      */   public static double sum(double[] counts) {
/*  984 */     double sum = 0.0D;
/*  985 */     for (int i = 0; i < counts.length; i++) {
/*  986 */       sum += counts[i];
/*      */     }
/*  988 */     return sum;
/*      */   }
/*      */   
/*      */   public static boolean xchrom()
/*      */   {
/*  993 */     return xchrom;
/*      */   }
/*      */   
/*      */   public static double u_exp() {
/*  997 */     return u_exp;
/*      */   }
/*      */   
/*      */   public static int indexToTrainSWHMM() {
/* 1001 */     return indexToTrainSWHMM;
/*      */   }
/*      */   
/*      */   public static double precision() {
/* 1005 */     return precision;
/*      */   }
/*      */   
/*      */   public static double sampleThresh() {
/* 1009 */     return sampleThresh;
/*      */   }
/*      */   
/* 1012 */   public static double pseudoCountWeightClumping() { return pseudoCountWeightClumping; }
/*      */   
/*      */   public static Double initialConcentration()
/*      */   {
/* 1016 */     return Double.valueOf(initialConcentration);
/*      */   }
/*      */   
/*      */   public static String getDirFile() {
/* 1020 */     return dir;
/*      */   }
/*      */   
/*      */   public static boolean annotate()
/*      */   {
/* 1025 */     return annotate;
/*      */   }
/*      */   
/*      */   public static double exclude() {
/* 1029 */     return exclude;
/*      */   }
/*      */   
/*      */   public static boolean trainWithGenotypes() {
/* 1033 */     return trainWithGenotypes;
/*      */   }
/*      */   
/*      */   public static int numItSum() {
/* 1037 */     int sum = numIt[0];
/* 1038 */     for (int i = 1; i < numIt.length; i++) {
/* 1039 */       sum += numIt[i];
/*      */     }
/* 1041 */     return sum;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int modifyWithData()
/*      */   {
/* 1052 */     return modifyWithData;
/*      */   }
/*      */   
/* 1055 */   public static String bin() { return bin; }
/*      */   
/*      */   public static int index()
/*      */   {
/* 1059 */     return index;
/*      */   }
/*      */   
/* 1062 */   public static boolean trans1 = true;
/*      */   
/*      */   public static boolean trans1() {
/* 1065 */     return trans1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] format()
/*      */   {
/* 1076 */     return format;
/*      */   }
/*      */   
/*      */   public static String chrom0() {
/* 1080 */     return mid[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Integer end()
/*      */   {
/* 1088 */     return Integer.valueOf(end);
/*      */   }
/*      */   
/*      */   public static int sum(int[] no_copies)
/*      */   {
/* 1093 */     int sm = 0;
/* 1094 */     for (int i = 0; i < no_copies.length; i++) {
/* 1095 */       sm += no_copies[i];
/*      */     }
/* 1097 */     return sm;
/*      */   }
/*      */   
/*      */   public static int product(int[] emStSp)
/*      */   {
/* 1102 */     int res = emStSp[0];
/* 1103 */     for (int i = 1; i < emStSp.length; i++) {
/* 1104 */       res *= emStSp[i];
/*      */     }
/* 1106 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1117 */   public static double bg0 = 1.0D;
/*      */   
/* 1119 */   public static double bg1 = 1.0D;
/*      */   
/*      */   public static double[] bg(int i) {
/* 1122 */     double bg_ = i == 0 ? bg0 : bg1;
/* 1123 */     double[] res = new double[5];
/* 1124 */     Arrays.fill(res, (1.0D - bg_) / res.length);
/* 1125 */     res[2] += bg_;
/* 1126 */     return res;
/*      */   }
/*      */   
/* 1129 */   private static double fixedThresh = 1.0D;
/*      */   
/* 1131 */   public static double fixedThresh() { return fixedThresh; }
/*      */   
/* 1133 */   public static boolean useFree = true;
/*      */   
/*      */ 
/* 1136 */   public static boolean useFree() { return useFree; }
/*      */   
/*      */   public static int[] getMax2(double[] emiss) {
/* 1139 */     int i = getMax(emiss);
/* 1140 */     double max = Double.NEGATIVE_INFINITY;
/* 1141 */     int r = -1;
/* 1142 */     for (int j = 0; j < emiss.length; j++) {
/* 1143 */       if ((j != i) && 
/* 1144 */         (emiss[j] > max)) {
/* 1145 */         max = emiss[j];
/* 1146 */         r = j;
/*      */       }
/*      */     }
/* 1149 */     return new int[] { i, r }; }
/*      */   
/* 1151 */   public static boolean illBgAvgOfFg = true;
/*      */   
/*      */ 
/* 1154 */   public static boolean illuminaBgIsAverageOfFg() { return illBgAvgOfFg; }
/*      */   
/* 1156 */   private static double trainThresh = 0.0D;
/*      */   
/*      */ 
/*      */ 
/* 1160 */   public static double trainThresh() { return trainThresh; }
/*      */   
/* 1162 */   private static double countThresh = 0.0D;
/*      */   
/*      */   public static double countThresh() {
/* 1165 */     return countThresh;
/*      */   }
/*      */   
/* 1168 */   public static int radix() { return 32; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1173 */   public static int[] restrictKb() { return restrictKb; }
/*      */   
/* 1175 */   public static boolean reverse = false;
/*      */   
/* 1177 */   public static boolean reverse() { return reverse; }
/*      */   
/* 1179 */   public static boolean drop = false;
/*      */   
/*      */   public static boolean drop() {
/* 1182 */     return drop;
/*      */   }
/*      */   
/* 1185 */   public static boolean trainCGH() { return trainCGH; }
/*      */   
/* 1187 */   private static double bwThresh = 0.0D;
/*      */   
/*      */ 
/* 1190 */   public static double bwThresh() { return bwThresh; }
/*      */   
/* 1192 */   public static boolean summarise = false;
/*      */   
/* 1194 */   public static boolean summarise() { return summarise; }
/*      */   
/*      */ 
/* 1197 */   public static boolean noHMM() { return false; }
/*      */   
/* 1199 */   public static boolean run = true;
/*      */   
/*      */   public static boolean run() {
/* 1202 */     return run;
/*      */   }
/*      */   
/* 1205 */   public static String column() { return "_" + column; }
/*      */   
/*      */   public static String[] getCols(String[] args) throws Exception {
/* 1208 */     Parser parser = new PosixParser();
/*      */     
/* 1210 */     CommandLine params = parser.parse(OPTIONS, args, false);
/* 1211 */     String[] cols = { "1" };
/* 1212 */     if (params.hasOption("column")) {
/* 1213 */       cols = params.getOptionValues("column");
/*      */     }
/* 1215 */     return cols; }
/*      */   
/* 1217 */   public static double softenHapMap = 0.3D;
/*      */   
/*      */ 
/* 1220 */   public static double softenHapMap() { return softenHapMap; }
/*      */   
/* 1222 */   public static double cn_ratio = 1.1D;
/*      */   
/* 1224 */   public static double cn_ratio() { return cn_ratio; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1232 */   public static String[] codesToRestrict = new String[0];
/*      */   
/* 1234 */   public static Collection<Character> codesToRestrict() { String[] codes = codesToRestrict;
/* 1235 */     Set<Character> res = new java.util.HashSet();
/* 1236 */     for (int i = 0; i < codes.length; i++) {
/* 1237 */       res.add(Character.valueOf(codes[i].charAt(0)));
/*      */     }
/* 1239 */     return res;
/*      */   }
/*      */   
/* 1242 */   public static void delete(File submissions) { if (submissions.isDirectory()) {
/* 1243 */       File[] f = submissions.listFiles();
/* 1244 */       for (int i = 0; i < f.length; i++) {
/* 1245 */         delete(f[i]);
/*      */       }
/* 1247 */       submissions.delete();
/*      */     }
/*      */     else {
/* 1250 */       submissions.delete();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/* 1255 */   public static int backgroundCount = 2;
/*      */   
/*      */ 
/* 1258 */   public static int backgroundCount() { return backgroundCount; }
/*      */   
/* 1260 */   public static boolean trainData = false;
/*      */   
/*      */ 
/* 1263 */   public static boolean trainData() { return trainData; }
/*      */   
/* 1265 */   public static boolean saveStatePath = true;
/*      */   
/* 1267 */   public static boolean saveStates() { return saveStatePath; }
/*      */   
/* 1269 */   public static boolean allowCloning = false;
/*      */   
/*      */ 
/* 1272 */   public static boolean allowCloning() { return allowCloning; }
/*      */   
/* 1274 */   public static double caseControlWeight = 1.0D;
/*      */   
/* 1276 */   public static double caseControlWeight() { return caseControlWeight; }
/*      */   
/* 1278 */   public static boolean topBottom = true;
/*      */   
/*      */ 
/* 1281 */   public static boolean topBottom() { return topBottom; }
/*      */   
/* 1283 */   public static double[] weights = { 1.0D };
/*      */   
/*      */   public static double[] weights() {
/* 1286 */     return weights;
/*      */   }
/*      */   
/* 1289 */   public static double round() { return 1000.0D; }
/*      */   
/* 1291 */   public static int plot = 0;
/*      */   
/* 1293 */   public static int plot() { return plot; }
/*      */   
/* 1295 */   public static int trainEnsemble = 2;
/*      */   
/* 1297 */   public static int trainEnsemble() { return trainEnsemble; }
/*      */   
/*      */ 
/* 1300 */   public static double[] r_state_var_mod = { 1.0D };
/* 1301 */   public static int[] cnStatesInCommonClass = { 1 };
/*      */   
/* 1303 */   public static int[] cnStatesInCommonClass() { return cnStatesInCommonClass; }
/*      */   
/* 1305 */   public static double base = 2.0D;
/*      */   
/* 1307 */   public static double base() { return base; }
/*      */   
/* 1309 */   public static int numThreads = 2;
/*      */   
/* 1311 */   public static int numThreads() { return numThreads; }
/*      */   
/* 1313 */   public static String[] toDel = new String[0];
/*      */   
/*      */   public static String[] toDel() {
/* 1316 */     return toDel;
/*      */   }
/*      */   
/* 1319 */   public static boolean supressR = false;
/*      */   
/* 1321 */   public static boolean suppressR() { return supressR; }
/*      */   
/* 1323 */   public static boolean supressB = false;
/*      */   
/* 1325 */   public static boolean suppressB() { return supressB; }
/*      */   
/* 1327 */   public static boolean logplot = true;
/*      */   
/* 1329 */   public static boolean logplot() { return logplot; }
/*      */   
/* 1331 */   public static int[] indexToRestrict = new int[0];
/*      */   
/* 1333 */   public static int[] indexToRestrict() { return indexToRestrict; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1338 */   public static boolean[] loess = { true };
/* 1339 */   public static boolean[] median_correction = { true };
/*      */   
/*      */   public static boolean loess(int i) {
/* 1342 */     if (i >= loess.length) return loess[(loess.length - 1)];
/* 1343 */     return loess[i];
/*      */   }
/*      */   
/* 1346 */   public static boolean median_correction(int i) { if (i >= median_correction.length) return median_correction[(median_correction.length - 1)];
/* 1347 */     return median_correction[i];
/*      */   }
/*      */   
/*      */ 
/* 1351 */   public static int segments() { return segments; }
/*      */   
/* 1353 */   public static boolean scoreDT = false;
/*      */   
/*      */   public static boolean scoreDT() {
/* 1356 */     return scoreDT;
/*      */   }
/*      */   
/*      */   static void setGapParams() {}
/*      */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/util/Constants.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */