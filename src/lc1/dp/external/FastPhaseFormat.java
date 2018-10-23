/*     */ package lc1.dp.external;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.collection.DataC;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.model.ExpSiteTrans;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastPhaseFormat
/*     */ {
/*     */   public static void compare(File f, DataC data, DataC origi_with_gaps, PrintWriter pw)
/*     */     throws Exception
/*     */   {
/*  32 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */     DataCollection result = DataCollection.readFastPhaseOutput(br, Emiss.class, Emiss.getEmissionStateSpace(1));
/*  42 */     br.close();
/*  43 */     throw new RuntimeException("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void runFastphase(DataCollection data, DataC orig, DataC orig_with_gaps, int numFounders, int numRestarts, int iter, PrintWriter pw)
/*     */     throws Exception
/*     */   {
/*  53 */     File dir = new File(System.getProperties().getProperty("user.dir"));
/*  54 */     PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(new File(dir, "test.inp"))));
/*  55 */     data.writeFastphase(pw1, false);
/*  56 */     pw1.close();
/*  57 */     String ff = Constants.bin() + "fastPHASE";
/*  58 */     String command = 
/*     */     
/*     */ 
/*  61 */       ff + " -g -T" + numRestarts + " -K" + numFounders + " -C" + iter + " -H" + Constants.noSamples() + " -oMyresults test.inp";
/*  62 */     StringWriter out = new StringWriter();
/*  63 */     System.err.println(command);
/*  64 */     Writer err = new OutputStreamWriter(new SystemErrStream());
/*     */     
/*     */ 
/*  67 */     System.err.println(err.toString());
/*  68 */     System.err.println(out.toString());
/*  69 */     Thread.currentThread();Thread.sleep(100L);
/*     */     
/*  71 */     Logger.getAnonymousLogger().info(out.toString());
/*  72 */     Logger.getAnonymousLogger().info("comparing");
/*  73 */     pw.println("FASTPHASE !!!!!");
/*  74 */     compare(new File(dir, "Myresults_hapguess_switch.out"), orig, 
/*  75 */       orig_with_gaps, pw);
/*  76 */     Logger.getAnonymousLogger().info("fini");
/*  77 */     pw.println("END FASTPHASE !!!!!!!!");
/*     */   }
/*     */   
/*     */   public static double[] readRHat(File f) throws Exception
/*     */   {
/*  82 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  83 */     String[] str = br.readLine().split("\\s+");
/*  84 */     double[] d = new double[str.length];
/*  85 */     for (int i = 0; i < str.length; i++) {
/*  86 */       d[i] = Double.parseDouble(str[i]);
/*     */     }
/*  88 */     br.close();
/*  89 */     return d;
/*     */   }
/*     */   
/*     */   public static List<double[]> readAlpha(File f) throws Exception {
/*  93 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  94 */     List<double[]> l = new ArrayList();
/*  95 */     String st = "";
/*  96 */     while ((st = br.readLine()) != null) {
/*  97 */       String[] str = st.split("\\s+");
/*  98 */       double[] d = new double[str.length];
/*  99 */       for (int i = 0; i < str.length; i++) {
/* 100 */         d[i] = Double.parseDouble(str[i]);
/*     */       }
/* 102 */       l.add(d);
/*     */     }
/* 104 */     br.close();
/* 105 */     return l;
/*     */   }
/*     */   
/* 108 */   public static ExpSiteTrans readHMM(File dir) throws Exception { List<double[]> theta = readAlpha(new File(dir, "thetahat.txt"));
/* 109 */     double[] rhat = readRHat(new File(dir, "rhat.txt"));
/* 110 */     List<double[]> alphas = readAlpha(new File(dir, "alphahat.txt"));
/* 111 */     int numFounders = ((double[])theta.get(0)).length;
/* 112 */     throw new RuntimeException("");
/*     */   }
/*     */   
/*     */ 
/*     */   public static int noNull(Comparable[] c)
/*     */   {
/* 118 */     int num = 0;
/* 119 */     for (int i = 0; i < c.length; i++) {
/* 120 */       if (c[i] == null) num++;
/*     */     }
/* 122 */     return num;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class SystemErrStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void flush()
/*     */     {
/* 140 */       System.err.flush();
/*     */     }
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 144 */       System.err.write(b);
/*     */     }
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException
/*     */     {
/* 149 */       System.err.write(b, off, len);
/*     */     }
/*     */     
/*     */     public void write(int b) throws IOException {
/* 153 */       System.err.write(b);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/external/FastPhaseFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */