/*     */ package lc1.possel;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FindAncestral
/*     */ {
/*  26 */   static List<String> data = new ArrayList();
/*  27 */   static File ill = new File("illumina_snps_v1.txt");
/*  28 */   static File ancestral = new File("ancestral_state.txt");
/*     */   Map<String, String> ancestralM;
/*     */   List<Integer> loc;
/*     */   List<String> snpid;
/*     */   PrintWriter out_pw;
/*     */   EmissionState maf;
/*     */   Set<String> rs_id;
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/*  38 */       for (int i = 9; i >= 1; i--) {
/*  39 */         data.add(i);
/*     */       }
/*  41 */       data.add("X");
/*  42 */       for (int i = 0; i < data.size(); i++) {
/*  43 */         FindAncestral fa = new FindAncestral((String)data.get(i));
/*  44 */         fa.readIll((String)data.get(i));
/*     */       }
/*     */     } catch (Exception exc) {
/*  47 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   FindAncestral(String chr)
/*     */     throws Exception
/*     */   {
/*  33 */     File localFile1 = new File(System.getProperty("user.dir"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */     this.rs_id = new HashSet();
/*     */     
/*     */ 
/*  60 */     File dir = new File(System.getProperty("user.dir"));
/*  61 */     File inp = 
/*  62 */       new File(dir, "data");
/*  63 */     File out = new File(inp, chr + "_anc.txt");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */     this.ancestralM = map(ancestral, Integer.MAX_VALUE, chr);
/*  74 */     this.out_pw = new PrintWriter(new BufferedWriter(new FileWriter(out)));
/*     */   }
/*     */   
/*     */   public String map(String st) {
/*  78 */     if (st.equals("T")) return "A";
/*  79 */     if (st.equals("G")) return "C";
/*  80 */     return st;
/*     */   }
/*     */   
/*  83 */   public void readIll(String chr) throws Exception { BufferedReader br = new BufferedReader(new FileReader(ill));
/*  84 */     String st = "";
/*  85 */     st = br.readLine();
/*  86 */     int a_ind = 0;int b_ind = 1;
/*  87 */     if (this.snpid != null) {
/*  88 */       a_ind = this.maf.getEmissionStateSpace().get(Emiss.a()).intValue();
/*  89 */       b_ind = this.maf.getEmissionStateSpace().get(Emiss.b()).intValue();
/*     */     }
/*  91 */     Object[] toPrint = new Object[5];
/*  92 */     String fs = "%7s %7s %7s %7s %5.3g";
/*  93 */     while ((st = br.readLine()) != null) {
/*  94 */       String[] str = st.split("\\s+");
/*  95 */       if (str[2].equals(chr)) {
/*  96 */         double mf = -1.0D;
/*  97 */         if (this.snpid != null) {
/*  98 */           int i = this.snpid.indexOf(str[1]);
/*  99 */           int loc1 = ((Integer)this.loc.get(i)).intValue();
/* 100 */           int loc2 = 
/* 101 */             Integer.parseInt(str[3]);
/*     */           
/* 103 */           Integer best_index = this.maf.getFixedInteger(i);
/* 104 */           if (best_index != null) {
/* 105 */             if (best_index.intValue() == a_ind) { mf = 1.0D;
/* 106 */             } else if (best_index.intValue() == b_ind) mf = 0.0D; else
/* 107 */               throw new RuntimeException("!!");
/*     */           } else
/* 109 */             mf = this.maf.getEmiss(i)[a_ind];
/*     */         }
/* 111 */         toPrint[0] = str[1];toPrint[1] = str[2];toPrint[2] = str[3];
/*     */         
/*     */ 
/* 114 */         if (this.ancestralM.containsKey(str[1])) {
/* 115 */           String anc = (String)this.ancestralM.get(str[1]);
/* 116 */           String a = map(str[4]);
/* 117 */           String b = map(str[5]);
/* 118 */           if (anc.equals(a)) {
/* 119 */             toPrint[3] = "A";toPrint[4] = Double.valueOf(mf);
/*     */ 
/*     */ 
/*     */           }
/* 123 */           else if (anc.equals(b)) {
/* 124 */             toPrint[3] = "B";toPrint[4] = Double.valueOf(1.0D - mf);
/*     */           }
/*     */           else
/*     */           {
/* 128 */             toPrint[3] = "mism";toPrint[4] = Integer.valueOf(-1);
/* 129 */             this.out_pw.print("mismatch\t-");
/*     */           }
/* 131 */           this.out_pw.println(Format.sprintf(fs, toPrint));
/* 132 */           this.out_pw.flush();
/*     */         }
/*     */         else {
/* 135 */           toPrint[3] = "null";toPrint[4] = Integer.valueOf(-1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 141 */     this.out_pw.close();
/*     */   }
/*     */   
/* 144 */   public Map<String, String> map(File f, int lim, String chr) throws Exception { Map<String, String> m = new HashMap();
/* 145 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 146 */     br.readLine();
/* 147 */     String st = "";
/* 148 */     for (int i = 0; ((st = br.readLine()) != null) && (i < lim); i++) {
/* 149 */       String[] str = st.trim().split("\\s+");
/* 150 */       if (str[1].equals(chr))
/*     */       {
/* 152 */         m.put(str[0], map(str[3]));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 158 */     return m;
/*     */   }
/*     */   
/*     */   static class RS { String rs_id;
/*     */     int loc;
/*     */     String chr;
/*     */     FindAncestral.MAF MAF_ceu;
/*     */     
/* 166 */     public String toString() { return this.chr + " " + this.rs_id + " " + this.loc + " " + this.MAF_ceu.toString(); }
/*     */     
/*     */     public boolean get(String anc) {
/* 169 */       if (this.MAF_ceu.A.equals(anc)) return true;
/* 170 */       if (this.MAF_ceu.B.equals(anc)) return false;
/* 171 */       throw new RuntimeException("!! " + anc + " " + this.MAF_ceu.A + " " + this.MAF_ceu.B + " " + toString());
/*     */     }
/*     */     
/*     */     RS(String[] str) {
/* 175 */       this.rs_id = str[0];
/* 176 */       this.loc = Integer.parseInt(str[2]);
/* 177 */       this.chr = str[1].substring(3);
/* 178 */       String[] st2 = str[(str.length - 1)].split(";");
/* 179 */       for (int index = 0; 
/* 180 */           index < st2.length; index++) {
/* 181 */         if (st2[index].indexOf("CEU") >= 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       try {
/* 186 */         this.MAF_ceu = new FindAncestral.MAF(st2[index], "CEU");
/*     */       } catch (Exception exc) {
/* 188 */         for (int i = 0; i < st2.length;) {
/*     */           try {
/* 190 */             this.MAF_ceu = new FindAncestral.MAF(st2[i], "CEU");
/*     */           }
/*     */           catch (Exception localException1) {}
/*     */         }
/*     */         
/*     */ 
/* 196 */         if (this.MAF_ceu == null) {
/* 197 */           System.err.println("prob with " + Arrays.asList(st2));
/* 198 */           System.err.println(st2[0]);
/* 199 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class MAF {
/*     */     String A;
/*     */     String B;
/*     */     double mafA;
/*     */     
/* 210 */     public String toString() { return "MAF " + this.A + " " + this.B + " " + this.mafA; }
/*     */     
/*     */     MAF(String st, String pop)
/*     */     {
/* 214 */       String[] st1 = st.split(":");
/*     */       
/*     */ 
/* 217 */       String[] st2 = st1[2].split(",");
/* 218 */       String[] stA = st2[0].split("\\s+");
/* 219 */       String[] stB = st2[1].split("\\s+");
/* 220 */       this.A = stA[0];
/* 221 */       this.B = stB[0];
/* 222 */       this.mafA = Double.parseDouble(stA[1]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/FindAncestral.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */