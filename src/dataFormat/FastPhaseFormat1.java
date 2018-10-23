/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ 
/*     */ public class FastPhaseFormat1
/*     */ {
/*  24 */   int no_copies = 2;
/*  25 */   static int[] mid = { 34135863, 40527829 };
/*     */   Map<String, List<Character[]>> rawData;
/*     */   List<String> phasedIndID;
/*     */   
/*  29 */   public static List<String> getIndiv(ZipFile f, String entryName, Integer column) throws Exception { BufferedReader nxt = 
/*  30 */       new BufferedReader(new InputStreamReader(
/*  31 */       f.getInputStream(f.getEntry(entryName))));
/*  32 */     List<String> indiv = new ArrayList();
/*  33 */     String st = "";
/*  34 */     while ((st = nxt.readLine()) != null) {
/*  35 */       indiv.add(st.split("\t")[column.intValue()]);
/*     */     }
/*  37 */     nxt.close();
/*  38 */     return indiv;
/*     */   }
/*     */   
/*     */   public static List<String> getSNP(ZipFile f, String entryName) throws Exception {
/*  42 */     BufferedReader nxt = 
/*  43 */       new BufferedReader(new InputStreamReader(
/*  44 */       f.getInputStream(f.getEntry(entryName))));
/*  45 */     List<String> snp = new ArrayList();
/*  46 */     String st = "";
/*  47 */     while ((st = nxt.readLine()) != null) {
/*  48 */       String[] str = st.split("\t");
/*  49 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1]))
/*  50 */         snp.add(str[3]);
/*     */     }
/*  52 */     nxt.close();
/*  53 */     return snp;
/*     */   }
/*     */   
/*     */ 
/*     */   public void getRawData(File f)
/*     */     throws Exception
/*     */   {
/*  60 */     ZipFile zf = new ZipFile(f);
/*  61 */     this.rawData = new HashMap();
/*  62 */     List<String> indiv = new ArrayList();
/*  63 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/*  64 */     List<String> snp = new ArrayList();
/*  65 */     snp = getSNP(zf, "Snps");
/*  66 */     String st = "";
/*  67 */     for (int i = 0; i < snp.size(); i++) {
/*  68 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/*  69 */       if (this.rawData.isEmpty()) {
/*  70 */         for (int s = 0; s < indiv.size(); s++) {
/*  71 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  72 */           Character[] temp = new Character[this.no_copies];
/*  73 */           for (int k = 0; k < this.no_copies; k++) {
/*  74 */             temp[k] = Character.valueOf(st.charAt(k));
/*     */           }
/*  76 */           List<Character[]> temp1 = new ArrayList();
/*  77 */           temp1.add(temp);
/*  78 */           this.rawData.put((String)indiv.get(s), temp1);
/*     */         }
/*  80 */         if ((st = nxt.readLine()) != null) throw new RuntimeException("!!");
/*     */       }
/*     */       else {
/*  83 */         for (int s = 0; s < indiv.size(); s++) {
/*  84 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  85 */           Character[] temp = new Character[this.no_copies];
/*  86 */           for (int k = 0; k < this.no_copies; k++) {
/*  87 */             temp[k] = Character.valueOf(st.charAt(k));
/*     */           }
/*  89 */           ((List)this.rawData.get(indiv.get(s))).add(temp);
/*     */         }
/*  91 */         if ((st = nxt.readLine()) != null) { throw new RuntimeException("!!");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void getPhasedIndID(File f)
/*     */     throws Exception
/*     */   {
/* 100 */     this.phasedIndID = new ArrayList();
/* 101 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 102 */     String st = "";
/* 103 */     while ((st = br.readLine()) != null) {
/* 104 */       if (st.startsWith("# id")) {
/* 105 */         String[] str = st.split("\\s+");
/* 106 */         this.phasedIndID.add(str[2]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Integer> getRandomIndex(int totalSNPs, double missingRate) {
/* 112 */     List<Integer> randomIndex = new ArrayList();
/* 113 */     SortedMap<Double, Integer> temp = new TreeMap();
/* 114 */     Random ran = new Random();
/* 115 */     int k = 0;
/* 116 */     while (k < totalSNPs) {
/* 117 */       Double tp = Double.valueOf(ran.nextDouble());
/* 118 */       if (!temp.containsKey(tp)) {
/* 119 */         temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(k));
/* 120 */         k++;
/*     */       }
/*     */     }
/* 123 */     int numMissingGenotype = (int)Math.round(missingRate * totalSNPs);
/* 124 */     int c = 0;
/* 125 */     if (numMissingGenotype != 0)
/* 126 */       for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/* 127 */         Double nt = (Double)it.next();
/* 128 */         randomIndex.add((Integer)temp.get(nt));
/* 129 */         c++;
/* 130 */         if (c == numMissingGenotype)
/*     */           break;
/*     */       }
/* 133 */     return randomIndex;
/*     */   }
/*     */   
/*     */   public void print(PrintStream ps) {
/* 137 */     int noSNP = ((List)this.rawData.get(this.phasedIndID.get(0))).size();
/* 138 */     int noInd = this.phasedIndID.size();
/* 139 */     List<Integer> randomIndex = getRandomIndex(noSNP * noInd, 0.0D);
/* 140 */     ps.println(noInd);
/* 141 */     ps.println(noSNP);
/* 142 */     for (int i = 0; i < noInd; i++) {
/* 143 */       ps.println("# " + (String)this.phasedIndID.get(i));
/* 144 */       for (int m = 0; m < noSNP; m++) {
/* 145 */         if (randomIndex.contains(Integer.valueOf(m + i * noSNP))) {
/* 146 */           ps.print("?");
/*     */         }
/*     */         else {
/* 149 */           ps.print(((Character[])((List)this.rawData.get(this.phasedIndID.get(i))).get(m))[0]);
/*     */         }
/*     */       }
/* 152 */       ps.println();
/* 153 */       for (int m = 0; m < noSNP; m++) {
/* 154 */         if (randomIndex.contains(Integer.valueOf(m + i * noSNP))) {
/* 155 */           ps.print("?");
/*     */         }
/*     */         else {
/* 158 */           ps.print(((Character[])((List)this.rawData.get(this.phasedIndID.get(i))).get(m))[1]);
/*     */         }
/*     */       }
/* 161 */       ps.println();
/*     */     }
/*     */   }
/*     */   
/*     */   public void print1(PrintStream ps)
/*     */   {
/* 167 */     int noSNP = ((List)this.rawData.get(this.phasedIndID.get(0))).size();
/* 168 */     int noInd = this.phasedIndID.size();
/* 169 */     ps.println(noInd);
/* 170 */     ps.println(noSNP);
/* 171 */     for (int i = 0; i < noInd; i++) {
/* 172 */       ps.println("# " + (String)this.phasedIndID.get(i));
/* 173 */       for (int m = 0; m < noSNP; m++) {
/* 174 */         ps.print(((Character[])((List)this.rawData.get(this.phasedIndID.get(i))).get(m))[0]);
/*     */       }
/* 176 */       ps.println();
/* 177 */       for (int m = 0; m < noSNP; m++) {
/* 178 */         ps.print(((Character[])((List)this.rawData.get(this.phasedIndID.get(i))).get(m))[1]);
/*     */       }
/* 180 */       ps.println();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try {
/* 187 */       FastPhaseFormat1 ph = new FastPhaseFormat1();
/* 188 */       for (int i = 0; i < 1; i++) {
/* 189 */         ph.getRawData(new File(args[0], "X_" + i + ".zip"));
/* 190 */         ph.getPhasedIndID(new File(args[0], "phased2_" + i + "_" + args[1]));
/* 191 */         PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(args[0] + "fastphase_" + i + "_" + args[1] + ".inp"))));
/* 192 */         ph.print1(ps);
/* 193 */         ps.close();
/*     */       }
/*     */     } catch (Exception exc) {
/* 196 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/FastPhaseFormat1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */