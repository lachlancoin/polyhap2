/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChiamoCompress
/*     */ {
/*     */   List[] snpInfo;
/*     */   
/*     */   public List<String> getIndiv(File f)
/*     */     throws Exception
/*     */   {
/*  33 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  35 */     String start = null;
/*  36 */     List<String> indivID = new ArrayList();
/*  37 */     String st; while ((st = br.readLine()) != null) { String st;
/*  38 */       String[] str = st.split("\\s+");
/*  39 */       if (start == null) {
/*  40 */         start = str[0];
/*     */       } else
/*  42 */         if (!st.startsWith(start)) break;
/*  43 */       indivID.add(str[1]);
/*     */     }
/*  45 */     br.close();
/*  46 */     return indivID;
/*     */   }
/*     */   
/*     */   public void getSNPinfo(File f) throws Exception
/*     */   {
/*  51 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  52 */     ArrayList<Integer> loc = new ArrayList();
/*  53 */     ArrayList<String> snp = new ArrayList();
/*  54 */     ArrayList<String> A_allele = new ArrayList();
/*  55 */     this.snpInfo = new List[] { snp, loc, A_allele };
/*     */     String st;
/*  57 */     while ((st = br.readLine()) != null) { String st;
/*  58 */       String[] str = st.split("\\s+");
/*  59 */       snp.add(str[0]);
/*  60 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/*  61 */       A_allele.add(str[4]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  67 */   Map<String, List<String>> rawData = new HashMap();
/*     */   
/*  69 */   public void ChiamoDataRaw(File bc, double cutpoint) throws Exception { BufferedReader br1 = new BufferedReader(new FileReader(bc));
/*  70 */     List<String> indivBC = getIndiv(bc);
/*  71 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/*  72 */       String al = this.snpInfo[2].get(i).toString();
/*  73 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/*  74 */       for (int k = 0; k < indivBC.size(); k++) {
/*  75 */         String[] str1 = br1.readLine().split("\\s+");
/*  76 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/*  77 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/*  78 */         if (!this.rawData.containsKey(str1[1])) {
/*  79 */           List<String> oneIndSNPs = new ArrayList();
/*  80 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/*  82 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/*  83 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else {
/*  85 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   Map<String, Double> maf;
/*     */   public Map<String, Double> calculateMaf()
/*     */   {
/*  95 */     this.maf = new HashMap();
/*  96 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/*  97 */       int count = 0;
/*  98 */       int ind = 0;
/*  99 */       Character a = null;
/* 100 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 101 */         String s = (String)is.next();
/* 102 */         ind++;
/* 103 */         if (a == null) {
/* 104 */           a = Character.valueOf(((String)((List)this.rawData.get(s)).get(i)).charAt(0));
/* 105 */           count++;
/* 106 */           if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */         }
/*     */         else {
/* 109 */           if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(0)) count++;
/* 110 */           if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */         }
/*     */       }
/* 113 */       double aFre = count / (ind * 2);
/* 114 */       if (aFre > 0.5D) this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(1.0D - aFre)); else
/* 115 */         this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(aFre));
/*     */     }
/* 117 */     return this.maf;
/*     */   }
/*     */   
/*     */   public void printMAF(PrintStream ps) {
/* 121 */     for (int i = 0; i < this.maf.size(); i++) {
/* 122 */       for (Iterator<String> is = this.maf.keySet().iterator(); is.hasNext();) {
/* 123 */         String s = (String)is.next();
/* 124 */         ps.println(s + " " + this.maf.get(s));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 131 */   Map<String, String> AalleleHapmap = null;
/*     */   
/* 133 */   public void getAalleleHapmap(File f) throws Exception { this.AalleleHapmap = new HashMap();
/* 134 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 135 */     String st = br.readLine();
/* 136 */     while ((st = br.readLine()) != null) {
/* 137 */       String[] str = st.split("\\s+");
/* 138 */       this.AalleleHapmap.put(str[0], str[2] + str[3]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 143 */   List<String> maskSNPs = null;
/*     */   
/* 145 */   public void getMaskSNPs(File f) throws Exception { this.maskSNPs = new ArrayList();
/* 146 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 148 */     while ((st = br.readLine()) != null) { String st;
/* 149 */       String[] str = st.split("\\s+");
/* 150 */       this.maskSNPs.add(str[3]);
/*     */     }
/*     */   }
/*     */   
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*     */   public void ChiamoDataCollection(File bc, int numChr, String chr, String maskRate) throws Exception
/*     */   {
/* 159 */     BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 160 */     List<String> indivBC = getIndiv(bc);
/* 161 */     this.dest = new FileOutputStream(chr + "_" + maskRate + ".zip");
/* 162 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 163 */     this.osw = new OutputStreamWriter(this.outS);
/* 164 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 165 */       if (((Double)this.maf.get(this.snpInfo[0].get(i).toString())).doubleValue() <= 0.01D) {
/* 166 */         for (int k = 0; k < indivBC.size(); k++) {
/* 167 */           br1.readLine();
/*     */         }
/*     */       }
/* 170 */       else if (this.maskSNPs.contains(this.snpInfo[0].get(i).toString())) {
/* 171 */         for (int k = 0; k < indivBC.size(); k++) {
/* 172 */           br1.readLine();
/*     */         }
/*     */       }
/*     */       else {
/* 176 */         List<Character> snpGeno = new ArrayList();
/* 177 */         List<Double> snpCall = new ArrayList();
/* 178 */         String al = this.snpInfo[2].get(i).toString();
/* 179 */         if (al.length() != 1) { throw new RuntimeException("!!" + al);
/*     */         }
/* 181 */         List<Character> checkGeno = null;
/* 182 */         char a; char a; if ((this.AalleleHapmap != null) && (this.AalleleHapmap.containsKey(this.snpInfo[0].get(i)))) {
/* 183 */           checkGeno = new ArrayList();
/* 184 */           String g = (String)this.AalleleHapmap.get(this.snpInfo[0].get(i));
/* 185 */           checkGeno.add(Character.valueOf(g.charAt(0)));checkGeno.add(Character.valueOf(g.charAt(1)));
/* 186 */           a = g.charAt(0);
/*     */         } else {
/* 188 */           a = al.charAt(0); }
/* 189 */         for (int k = 0; k < indivBC.size(); k++) {
/* 190 */           String[] str1 = br1.readLine().split("\\s+");
/* 191 */           if (checkGeno != null) {
/* 192 */             if (!checkGeno.contains(Character.valueOf(str1[2].charAt(0)))) throw new RuntimeException("!!");
/* 193 */             if (!checkGeno.contains(Character.valueOf(str1[2].charAt(1)))) throw new RuntimeException("!!");
/*     */           }
/* 195 */           if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 196 */           if (!str1[0].equals(this.snpInfo[0].get(i))) { throw new RuntimeException("!!");
/*     */           }
/* 198 */           if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 199 */             snpGeno.add(Character.valueOf('B'));
/* 200 */           if (str1[2].charAt(1) == a) snpGeno.add(Character.valueOf('A')); else
/* 201 */             snpGeno.add(Character.valueOf('B'));
/* 202 */           snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */         }
/*     */         
/* 205 */         ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 206 */         this.outS.putNextEntry(headings);
/* 207 */         for (int m = 0; m < snpGeno.size() / numChr; m++) {
/* 208 */           for (int c = 0; c < numChr; c++) {
/* 209 */             this.osw.write(((Character)snpGeno.get(m * numChr + c)).charValue());
/*     */           }
/* 211 */           this.osw.write("\t" + snpCall.get(m) + "\n");
/*     */         }
/* 213 */         this.osw.flush();
/* 214 */         this.outS.closeEntry();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 219 */     ZipEntry headings = new ZipEntry("Samples");
/* 220 */     this.outS.putNextEntry(headings);
/* 221 */     for (int i = 0; i < indivBC.size(); i++) {
/* 222 */       this.osw.write((String)indivBC.get(i) + "\n");
/*     */     }
/* 224 */     this.osw.flush();
/* 225 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(String chr)
/*     */     throws Exception
/*     */   {
/* 231 */     ZipEntry headings = new ZipEntry("Snps");
/* 232 */     this.outS.putNextEntry(headings);
/* 233 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 234 */       if ((((Double)this.maf.get(this.snpInfo[0].get(i).toString())).doubleValue() > 0.01D) && (!this.maskSNPs.contains(this.snpInfo[0].get(i).toString())))
/*     */       {
/* 236 */         this.osw.write("chr" + chr + "\t");this.osw.write(this.snpInfo[1].get(i).toString() + "\t");
/* 237 */         this.osw.write(Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40 + "\t");
/* 238 */         this.osw.write(this.snpInfo[0].get(i).toString());
/* 239 */         this.osw.write("\n");
/*     */       }
/*     */     }
/* 242 */     this.osw.flush();
/* 243 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 247 */     ZipEntry headings = new ZipEntry("Name");
/* 248 */     this.outS.putNextEntry(headings);
/* 249 */     this.osw.write("genotype callingScore\n");
/* 250 */     this.osw.write("chr start end snpID\n");
/* 251 */     this.osw.write("sampleID");
/* 252 */     this.osw.flush();
/* 253 */     this.outS.closeEntry();
/* 254 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 261 */       ChiamoCompress cc = new ChiamoCompress();
/* 262 */       cc.getSNPinfo(new File("22_snp.txt"));
/* 263 */       cc.ChiamoDataRaw(new File("Affx_20070205fs1_gt_58C_Chiamo_22.txt"), 0.0D);
/* 264 */       cc.calculateMaf();
/* 265 */       cc.getAalleleHapmap(new File("genotypes_chr22_CEU_r22_nr.b36_fwd_legend.txt"));
/* 266 */       cc.getMaskSNPs(new File("marked_" + args[0]));
/* 267 */       cc.ChiamoDataCollection(new File("Affx_20070205fs1_gt_58C_Chiamo_22.txt"), 2, "22", args[0]);
/* 268 */       cc.writeSnps("22");
/* 269 */       cc.writeName();
/*     */     }
/*     */     catch (Exception exc) {
/* 272 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/ChiamoCompress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */