/*     */ package lc1.possel;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.FileHandler;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.CGH.AberationFinder;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.sequenced.Deletion;
/*     */ 
/*     */ public class EHHFinder1 implements Runnable
/*     */ {
/*     */   static final int column = 1;
/*  32 */   static boolean restrict = true;
/*     */   SimpleDataCollection sdt;
/*     */   
/*  35 */   static { try { ConsoleHandler handler = new ConsoleHandler();
/*  36 */       FileHandler handlerF = new FileHandler("stderr_EHHfinder_1_" + restrict, false);
/*  37 */       Logger.global.addHandler(handlerF);
/*  38 */       Formatter formatter = 
/*  39 */         new Formatter() {
/*     */           public String format(LogRecord record) {
/*  41 */             return record.getSourceClassName() + ":\n" + record.getMessage() + "\n";
/*     */           }
/*  43 */         };
/*  44 */         handler.setFormatter(formatter);
/*  45 */         handlerF.setFormatter(formatter);
/*     */       } catch (Exception exc) {
/*  47 */         exc.printStackTrace();
/*  48 */         System.exit(0);
/*     */       }
/*     */     }
/*     */     
/*     */     File cgh;
/*     */     File[] files;
/*     */     public EHHFinder1(File dir, File[] user, String[] args, PrintWriter out, Map<String, Number> sum, String chromosome, Location reg)
/*     */       throws Exception
/*     */     {
/*  57 */       EHHFinder.thresh = 2;
/*  58 */       EHHFinder.lim = Integer.valueOf(Integer.MAX_VALUE);
/*  59 */       EHHFinder.thresh1 = 0.0D;
/*  60 */       this.cgh = dir;
/*     */       
/*     */ 
/*  63 */       this.files = user;
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       try {
/*  68 */         File delF = new File(this.cgh, "deletion_samples.txt");
/*     */         
/*     */ 
/*     */ 
/*  72 */         List<String> probs = new ArrayList();
/*     */         
/*  74 */         File sweepDir = new File("sweep");
/*  75 */         if (!sweepDir.exists())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */           sweepDir.mkdir(); }
/*  82 */         PrintWriter pw_out = new PrintWriter(new BufferedWriter(new FileWriter(new File(sweepDir, "sweep.many"))));
/*  83 */         try { Logger.global.info("files " + Arrays.asList(this.files));
/*  84 */           for (int i = 0; 
/*     */               
/*     */ 
/*  87 */               i < this.files.length; 
/*  88 */               i++) {
/*     */             try {
/*  90 */               String[] del = EHHFinder.getMid(AberationFinder.getBufferedReader(this.files[i], "log_out.txt1"), "mid");
/*  91 */               String[] chr = EHHFinder.getMid(AberationFinder.getBufferedReader(this.files[i], "log_out.txt1"), "chrom");
/*     */               
/*  93 */               Location[] dels = lc1.sequenced.Convert.getDeletions(delF, Integer.valueOf(Integer.parseInt(chr[0])), new int[] { Integer.parseInt(del[0]), Integer.parseInt(del[1]) });
/*  94 */               if (!chr[0].equals("1")) return;
/*  95 */               System.err.println("doing " + i);
/*  96 */               Logger.global.info("file  " + i + " of " + this.files.length);
/*  97 */               SimpleDataCollection sdt = 
/*  98 */                 SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(this.files[i], "phased2.txt_1"), Emiss.class, Emiss.getEmissionStateSpace(1));
/*  99 */               sdt.removeKeyIfStartsWith("NA");
/* 100 */               BufferedReader snpFile = AberationFinder.getBufferedReader(this.files[i], "snp.txt");
/*     */               
/* 102 */               sdt.snpid = new ArrayList();
/* 103 */               List<Integer> snp_orig = new ArrayList();
/* 104 */               SimpleDataCollection.readPosInfo(snpFile, new int[] { 0, 4 }, true, new List[] { sdt.snpid, snp_orig }, new Class[] { String.class, Integer.class });
/* 105 */               snpFile.close();
/* 106 */               Map<Integer, List<Integer>> summary = summary(snp_orig);
/*     */               
/*     */ 
/* 109 */               File outDir = this.files[i].getParentFile();
/* 110 */               if (outDir.getName().endsWith(".gz")) {
/* 111 */                 String nm = outDir.getName();
/* 112 */                 outDir = new File(outDir.getParentFile(), nm.substring(0, nm.indexOf(".tar.gz")));
/* 113 */                 if (!outDir.exists()) { outDir.mkdir();
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 120 */               int st1 = sdt.loc.indexOf(Integer.valueOf((int)dels[0].min));
/* 121 */               int end1 = sdt.loc.indexOf(Integer.valueOf((int)dels[0].max));
/* 122 */               int sub = restrict ? sdt.restrict(st1, end1, 50, 50, true) : 0;
/* 123 */               st1 -= sub;
/* 124 */               end1 -= sub;
/* 125 */               String name = this.files[i].getName();
/* 126 */               if (name.indexOf('.') >= 0) {
/* 127 */                 name = name.substring(0, name.indexOf("."));
/*     */               }
/* 129 */               if (restrict) {
/* 130 */                 Logger.global.info("hwe " + chr[0] + "_" + del[0] + "_" + del[1]);
/* 131 */                 int blocksize = EHHFinder.writeEHH(sdt, sweepDir, chr[0], null, name);
/*     */                 
/* 133 */                 Logger.global.info("block size is " + blocksize + " for " + this.files[i].getName());
/*     */               }
/* 135 */               sdt.calculateMaf(false);
/* 136 */               pw_out.println(name + ".emphase\t" + name + ".snp");
/*     */               
/* 138 */               sdt.split();
/*     */               
/* 140 */               File hapF = new File("hap_1");
/* 141 */               if (!hapF.exists()) { hapF.mkdir();
/*     */               }
/* 143 */               PrintWriter pw = !restrict ? new PrintWriter(new BufferedWriter(new FileWriter(new File(hapF, "hap_summary" + this.files[i].getName() + ".txt")))) : null;
/* 144 */               EHHFinder ehh = new EHHFinder(sdt);
/*     */               
/*     */ 
/* 147 */               if (restrict) {
/* 148 */                 double[] maxR = EHHFinder.calcLD(sdt, st1, end1, this.files[i].getName(), false, 10, true);
/*     */                 
/*     */ 
/* 151 */                 Logger.global.info("maxDprime is " + maxR[0] + " " + maxR[1] + " " + maxR[2] + " for " + this.files[i].getName());
/* 152 */                 maxR = EHHFinder.calcLD(sdt, st1, end1, this.files[i].getName(), false, 1000, false);
/*     */                 
/*     */ 
/* 155 */                 Logger.global.info("maxr2 is " + maxR[0] + " " + maxR[1] + " " + maxR[2] + " for " + this.files[i].getName());
/*     */               }
/*     */               else {
/* 158 */                 Set<String> coreHaps = sdt.getAllHaplotypes(st1, end1).keySet();
/* 159 */                 SortedMap<Integer, SortedMap<String, Object[]>> first = null;
/* 160 */                 for (Iterator<String> it = coreHaps.iterator(); it.hasNext();)
/*     */                 {
/* 162 */                   String st = (String)it.next();
/* 163 */                   Logger.global.info("HAPLOTYPE " + st);
/* 164 */                   pw.println("HAPLOTYPES FOR " + st);
/* 165 */                   ehh.base = st;
/* 166 */                   System.err.println(st);
/* 167 */                   boolean fir = (st.startsWith("_")) && (st.endsWith("_"));
/* 168 */                   if (fir)
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 174 */                     ehh.extend(st, st1, end1, pw);
/* 175 */                     if (fir) {
/* 176 */                       first = ehh.map;
/*     */                     }
/* 178 */                     pw.println("##############################################");
/* 179 */                     pw.flush();
/* 180 */                     System.err.println("done " + i);
/*     */                   }
/*     */                 }
/*     */               }
/* 184 */             } catch (Exception exc) { exc.printStackTrace();
/* 185 */               probs.add(this.files[i].getName());
/* 186 */               System.err.println("error with file " + this.files[i]);
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 192 */           System.err.println("problems " + probs);
/*     */         } catch (Exception exc) {
/* 194 */           exc.printStackTrace();
/*     */         }
/* 196 */         pw_out.close();
/*     */       } catch (Exception exc) {
/* 198 */         exc.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     private Map<Integer, List<Integer>> summary(List<Integer> snp_orig) {
/* 203 */       Map<Integer, List<Integer>> m = new HashMap();
/* 204 */       for (int i = 0; i < snp_orig.size(); i++) {
/* 205 */         Integer val = (Integer)snp_orig.get(i);
/* 206 */         List<Integer> l = (List)m.get(val);
/* 207 */         if (l == null) {
/* 208 */           m.put(val, l = new ArrayList());
/*     */         }
/* 210 */         l.add(Integer.valueOf(i));
/*     */       }
/* 212 */       return m;
/*     */     }
/*     */     
/*     */     private Location getDel(Location[] dels, int mid) {
/* 216 */       for (int i = 0; i < dels.length; i++) {
/* 217 */         if ((int)dels[i].min == mid) return dels[i];
/*     */       }
/* 219 */       return null;
/*     */     }
/*     */     
/*     */     private Deletion findDeletion(Deletion[] dels2, DataCollection dt) {
/* 223 */       List<Deletion> d = new ArrayList();
/* 224 */       int start = ((Integer)dt.loc.get(0)).intValue();
/* 225 */       int end = ((Integer)dt.loc.get(dt.length() - 1)).intValue();
/* 226 */       for (int i = 0; i < dels2.length; i++) {
/* 227 */         if ((dels2[i].start >= start) && (dels2[i].start <= end) && 
/* 228 */           (dels2[i].end >= start) && (dels2[i].end <= end))
/*     */         {
/* 230 */           if (dt.loc.indexOf(Integer.valueOf(dels2[i].start)) >= 0) {
/* 231 */             d.add(dels2[i]);
/*     */           }
/*     */         }
/*     */       }
/* 235 */       if (d.size() != 1)
/* 236 */         throw new RuntimeException("!!WRONG NUMBER" + d);
/* 237 */       return (Deletion)d.get(0);
/*     */     }
/*     */   }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/EHHFinder1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */