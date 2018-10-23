/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SATformat
/*     */ {
/*  20 */   List<String[]> potato = new ArrayList();
/*     */   
/*  22 */   public void getPotato(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  24 */     while ((st = br.readLine()) != null) { String st;
/*  25 */       String[] str = st.split("\\s+");
/*  26 */       this.potato.add(str);
/*     */     }
/*     */   }
/*     */   
/*     */   public void printSATformat(PrintStream ps) {
/*  31 */     ps.print("name\t");
/*  32 */     for (int m = 0; m < 12; m++) {
/*  33 */       ps.print(m + "\t");
/*     */     }
/*  35 */     ps.println();
/*  36 */     for (int i = 0; i < this.potato.size() / 4; i++) {
/*  37 */       ps.print(i + "\t");
/*  38 */       for (int m = 0; m < 12; m++) {
/*  39 */         for (int s = 0; s < 4; s++) {
/*  40 */           ps.print(((String[])this.potato.get(s + i * 4))[m]);
/*     */         }
/*  42 */         ps.print("\t");
/*     */       }
/*  44 */       ps.println();
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(ZipFile f, String entryName, Integer column, int maxInd) throws Exception
/*     */   {
/*  50 */     BufferedReader nxt = 
/*  51 */       new BufferedReader(new InputStreamReader(
/*  52 */       f.getInputStream(f.getEntry(entryName))));
/*  53 */     List<String> indiv = new ArrayList();
/*  54 */     String st = "";
/*  55 */     for (int k = 0; ((st = nxt.readLine()) != null) && (k < maxInd); k++) {
/*  56 */       indiv.add(st.split("\\s+")[column.intValue()]);
/*     */     }
/*  58 */     nxt.close();
/*  59 */     return indiv;
/*     */   }
/*     */   
/*     */   public List<String> getSNP(ZipFile f, String entryName, Integer column, int from, int to) throws Exception {
/*  63 */     BufferedReader nxt = 
/*  64 */       new BufferedReader(new InputStreamReader(
/*  65 */       f.getInputStream(f.getEntry(entryName))));
/*  66 */     List<String> snp = new ArrayList();
/*  67 */     String st = "";
/*  68 */     for (int k = 0; (st = nxt.readLine()) != null; k++) {
/*  69 */       String[] str = st.split("\\s+");
/*  70 */       if ((Integer.parseInt(str[1]) >= from) && (Integer.parseInt(str[1]) <= to)) {
/*  71 */         snp.add(st.split("\\s+")[column.intValue()]);
/*     */       }
/*     */     }
/*  74 */     nxt.close();
/*  75 */     return snp;
/*     */   }
/*     */   
/*     */ 
/*     */   Map<String, List<String>> rawData;
/*     */   
/*  81 */   List<String> sampleID = new ArrayList();
/*     */   
/*  83 */   public void getRawData(File f, int maxInd, FileWriter pw) throws Exception { ZipFile zf = new ZipFile(f);
/*  84 */     this.rawData = new HashMap();
/*  85 */     this.sampleID = getIndiv(zf, "Samples", Integer.valueOf(0), maxInd);
/*  86 */     List<String> snpInfo = getSNP(zf, "Snps", Integer.valueOf(3), 34135863, 34414474);
/*  87 */     String st = "";
/*  88 */     for (int i = 0; i < snpInfo.size(); i++)
/*     */     {
/*  90 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snpInfo.get(i)))));
/*  91 */       List<String> temp = new ArrayList();
/*  92 */       for (int s = 0; s < this.sampleID.size(); s++) {
/*  93 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  94 */         temp.add(st.split("\\s+")[0]);
/*     */       }
/*  96 */       this.rawData.put((String)snpInfo.get(i), temp);
/*     */     }
/*     */     
/*     */ 
/* 100 */     pw.append("name");pw.append(',');
/* 101 */     for (int i = 0; i < snpInfo.size() - 1; i++) {
/* 102 */       pw.append((CharSequence)snpInfo.get(i));pw.append(',');
/*     */     }
/* 104 */     pw.append((CharSequence)snpInfo.get(snpInfo.size() - 1));
/* 105 */     pw.append('\n');
/* 106 */     for (int s = 0; s < this.sampleID.size(); s++) {
/* 107 */       pw.append((CharSequence)this.sampleID.get(s));pw.append(',');
/* 108 */       for (int i = 0; i < snpInfo.size() - 1; i++) {
/* 109 */         pw.append((CharSequence)((List)this.rawData.get(snpInfo.get(i))).get(s));pw.append(',');
/*     */       }
/* 111 */       pw.append((CharSequence)((List)this.rawData.get(snpInfo.get(snpInfo.size() - 1))).get(s));
/* 112 */       pw.append('\n');
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 118 */   List<String[]> potatoSAT = new ArrayList();
/*     */   
/* 120 */   public void getPotatoSAT(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 122 */     while ((st = br.readLine()) != null) { String st;
/* 123 */       String str = st.replaceAll(" ", "");
/* 124 */       if (str.startsWith("<string>")) this.potatoSAT.add(str.split("<string>"));
/*     */     }
/*     */   }
/*     */   
/*     */   public void printpolyHapOutformat(PrintStream ps) {
/* 129 */     String aIndex = "AGTTGCCCCTGT";
/* 130 */     for (int i = 0; i < this.potatoSAT.size(); i++) {
/* 131 */       if (((String[])this.potatoSAT.get(i))[1].length() < 3) { ps.println("# id ind" + ((String[])this.potatoSAT.get(i))[1]);
/*     */       } else {
/* 133 */         String out = ((String[])this.potatoSAT.get(i))[1];
/* 134 */         for (int m = 0; m < aIndex.length(); m++) {
/* 135 */           if (out.length() != aIndex.length()) throw new RuntimeException("!!");
/* 136 */           if (out.charAt(m) == aIndex.charAt(m)) ps.print("A"); else
/* 137 */             ps.print("B");
/*     */         }
/* 139 */         ps.println();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try {
/* 147 */       SATformat sat = new SATformat();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */       FileWriter pw = new FileWriter(new File("satInputX_0.csv"));
/* 162 */       sat.getRawData(new File("X_0.zip"), 300, pw);
/* 163 */       pw.flush();
/* 164 */       pw.close();
/*     */     } catch (Exception exc) {
/* 166 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/SATformat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */