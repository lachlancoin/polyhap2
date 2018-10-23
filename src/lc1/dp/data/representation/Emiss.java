/*     */ package lc1.dp.data.representation;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.emissionspace.SimpleEmissionStateSpace;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class Emiss implements Comparable, Serializable
/*     */ {
/*     */   final int i;
/*  16 */   static Map<String, Emiss> m = new HashMap();
/*     */   final char c;
/*     */   final String shortString;
/*     */   
/*  20 */   public Emiss(char c, int i, String shortString) { this.i = i;
/*  21 */     this.c = c;
/*  22 */     this.shortString = shortString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  27 */   public static Emiss N = new Emiss('_', 0, "_");
/*  28 */   public static Emiss A = new Emiss('A', 2, "A");
/*  29 */   public static Emiss B = new Emiss('B', 3, "B");
/*     */   
/*  31 */   private static Emiss X = new Emiss('X', 5, "AA");
/*  32 */   private static Emiss Y = new Emiss('Y', 6, "AB");
/*  33 */   private static Emiss Z = new Emiss('Z', 7, "BB");
/*     */   
/*  35 */   public static Emiss T = new Emiss('T', 9, "AAA");
/*  36 */   public static Emiss U = new Emiss('U', 10, "AAB");
/*  37 */   public static Emiss V = new Emiss('V', 11, "ABB");
/*  38 */   public static Emiss W = new Emiss('W', 12, "BBB");
/*     */   
/*     */ 
/*  41 */   public static Emiss zero = new Emiss('0', 1, "_");
/*  42 */   public static Emiss one = new Emiss('1', 4, "A");
/*  43 */   public static Emiss two = new Emiss('2', 8, "AA");
/*  44 */   public static Emiss three = new Emiss('3', 13, "AAA");
/*  45 */   public static Emiss four = new Emiss('4', 14, "AAAA");
/*  46 */   public static Emiss five = new Emiss('5', 15, "AAAAA");
/*  47 */   public static Emiss six = new Emiss('6', 16, "AAAAAA");
/*     */   
/*  49 */   static Emiss[] ems = { N, A, B, X, Y, Z, zero, one, two, T, U, V, W, three };
/*     */   
/*  51 */   static { for (int i = 0; i < ems.length; i++) {
/*  52 */       System.err.println("putting " + ems[i].toStringShort() + " : " + ems[i]);
/*  53 */       m.put(ems[i].toStringShort(), ems[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  59 */   static Emiss star = new Emiss('*', 25, "*");
/*     */   
/*  61 */   public static Emiss translate(char c) { if (gaps.indexOf(c) >= 0) return N;
/*  62 */     if (c == '*') return star;
/*  63 */     for (int i = 0; i < ems.length; i++) {
/*  64 */       if (ems[i].c == c) return ems[i];
/*     */     }
/*  66 */     if (c == '0') return A;
/*  67 */     if (c == '1') { return B;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */     throw new RuntimeException(new String(new char[] { c }) + " " + c);
/*     */   }
/*     */   
/*     */   public String toStringShort()
/*     */   {
/*  80 */     return this.shortString;
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
/*  93 */   public static Emiss a() { return A; }
/*  94 */   public static Emiss b() { return B; }
/*  95 */   public static Emiss aa() { return X; }
/*  96 */   public static Emiss ab() { return Y; }
/*  97 */   public static Emiss bb() { return Z; }
/*  98 */   public static Emiss N() { return N; }
/*  99 */   public static Emiss zero() { return zero; }
/* 100 */   public static Emiss one() { return one; }
/* 101 */   public static Emiss two() { return two; }
/*     */   
/*     */   public int noCopies()
/*     */   {
/* 105 */     if ((this == A) || (this == B) || (this == one)) return 1;
/* 106 */     if ((this == N) || (this == zero)) return 0;
/* 107 */     if ((this == X) || (this == Y) || (this == Z) || (this == two)) return 2;
/* 108 */     if (this == four) return 4;
/* 109 */     if (this == five) return 5;
/* 110 */     if (this == six) { return 6;
/*     */     }
/* 112 */     return 3;
/*     */   }
/*     */   
/*     */ 
/* 116 */   public String toStringPrint() { return this.c; }
/*     */   
/*     */   public double noB() {
/* 119 */     if ((this == B) || (this == Y) || (this == U)) return 1.0D;
/* 120 */     if ((this == Z) || (this == V)) return 2.0D;
/* 121 */     if (this == W) return 3.0D;
/* 122 */     if ((this == A) || (this == X) || (this == N) || (this == zero)) return 0.0D;
/* 123 */     return NaN.0D;
/*     */   }
/*     */   
/*     */ 
/* 127 */   static final EmissionStateSpace[][] emissionStateSpace = new EmissionStateSpace[3][Constants.maxNoCopies];
/*     */   
/*     */ 
/* 130 */   private static EmissionStateSpace countSpace = Constants.modelCNP() == 2 ? null : 
/* 131 */     new SimpleEmissionStateSpace(
/*     */     
/*     */ 
/* 134 */     new Emiss[] { zero, one, two, Constants.modelCNP() == 6 ? new Emiss[] { zero, one, two } : Constants.modelCNP() == 3 ? new Emiss[] { zero, one } : three });
/*     */   
/*     */ 
/* 137 */   private static EmissionStateSpace extendedCountSpace = Constants.modelCNP() == 2 ? null : 
/* 138 */     new SimpleEmissionStateSpace(
/*     */     
/*     */ 
/* 141 */     new Emiss[] { zero, one, two, three, four, five, Constants.modelCNP() == 6 ? new Emiss[] { zero, one, two, three, four } : Constants.modelCNP() == 3 ? new Emiss[] { zero, one, two } : six });
/*     */   
/*     */   private static Comparable[] getNumF(int numFounders) {
/* 144 */     Comparable[] founders = new Comparable[numFounders];
/* 145 */     for (int i = 0; i < founders.length; i++) {
/* 146 */       founders[i] = Integer.valueOf(i + 1);
/*     */     }
/* 148 */     return founders;
/*     */   }
/*     */   
/*     */ 
/* 152 */   private static Map<Integer, EmissionStateSpace>[] stateEmissionStateSpace = {
/* 153 */     new HashMap(), new HashMap() };
/*     */   
/*     */   public static EmissionStateSpace getStateEmissionStateSpace(int numF) {
/* 156 */     EmissionStateSpace res = (EmissionStateSpace)stateEmissionStateSpace[0].get(Integer.valueOf(numF));
/* 157 */     if (res == null) {
/* 158 */       stateEmissionStateSpace[0].put(Integer.valueOf(numF), res = new SimpleEmissionStateSpace(getNumF(numF)));
/*     */     }
/* 160 */     return res;
/*     */   }
/*     */   
/*     */   public static EmissionStateSpace getStateEmissionStateSpace(int[] emStSp) {
/* 164 */     int numF = Constants.product(emStSp);
/* 165 */     EmissionStateSpace res = (EmissionStateSpace)stateEmissionStateSpace[1].get(Integer.valueOf(numF));
/* 166 */     if (res == null) {
/* 167 */       EmissionStateSpace[] em = new EmissionStateSpace[emStSp.length];
/* 168 */       for (int i = 0; i < em.length; i++) {
/* 169 */         em[i] = getStateEmissionStateSpace(emStSp[i]);
/*     */       }
/*     */       
/* 172 */       stateEmissionStateSpace[1].put(Integer.valueOf(numF), res = new CompoundEmissionStateSpace(em, emStSp.length));
/*     */     }
/* 174 */     return res;
/*     */   }
/*     */   
/* 177 */   private static EmissionStateSpace alleleSpace = new SimpleEmissionStateSpace(new Emiss[] { A, B });
/*     */   
/*     */ 
/*     */ 
/* 181 */   private static EmissionStateSpace stateSpace = (Constants.onlyCopyNo()) || (Constants.transMode1 == null) ? 
/* 182 */     new SimpleEmissionStateSpace(
/*     */     
/*     */ 
/* 185 */     new Emiss[] { A, B, N, X, Z, Y, T, W, U, Constants.modelCNP() == 6 ? new Emiss[] { A, B, N, X, Z, Y } : Constants.modelCNP() == 3 ? new Emiss[] { A, B, N } : V }) : Constants.modelCNP() == 2 ? new SimpleEmissionStateSpace(new Emiss[] { A, B }) : 
/*     */     
/* 187 */     new lc1.dp.emissionspace.CompoundEmissionStateSpace1(new EmissionStateSpace[] { countSpace, alleleSpace }, 1);
/*     */   
/*     */ 
/* 190 */   public static String gaps = new String(new char[] { '|', '?', '-', 'N', '_' });
/*     */   
/*     */ 
/*     */   public static EmissionStateSpace stateSpace()
/*     */   {
/* 195 */     if (Constants.onlyCopyNo()) return countSpace;
/* 196 */     return stateSpace;
/*     */   }
/*     */   
/* 199 */   public static EmissionStateSpace countSpace() { return countSpace; }
/*     */   
/*     */   public static EmissionStateSpace extendedCountSpace() {
/* 202 */     return extendedCountSpace;
/*     */   }
/*     */   
/* 205 */   public static EmissionStateSpace alleleSpace() { return alleleSpace; }
/*     */   
/*     */   public static CompoundEmissionStateSpace getEmissionStateSpace(int i)
/*     */   {
/* 209 */     return getEmissionStateSpace(i, 
/* 210 */       Constants.onlyCopyNo() ? 0 : 2);
/*     */   }
/*     */   
/*     */   public static CompoundEmissionStateSpace getEmissionStateSpace(int i, int type)
/*     */   {
/* 215 */     return getEmissionStateSpace(i, 
/*     */     
/* 217 */       type == 1 ? alleleSpace : type == 0 ? countSpace : 
/* 218 */       stateSpace, 
/* 219 */       emissionStateSpace[type]);
/*     */   }
/*     */   
/*     */ 
/*     */   public static CompoundEmissionStateSpace getEmissionStateSpace(int i, EmissionStateSpace base, EmissionStateSpace[] store)
/*     */   {
/* 225 */     if (store[i] == null)
/* 226 */       if ((i == 0) || (i == 1) || (i == 2) || (i == 3)) {
/* 227 */         EmissionStateSpace[] sp = new EmissionStateSpace[i + 1];
/* 228 */         Arrays.fill(sp, base);
/* 229 */         store[i] = new CompoundEmissionStateSpace(sp, i + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 244 */         throw new RuntimeException("!!");
/*     */       }
/* 246 */     return (CompoundEmissionStateSpace)store[i];
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 250 */     if (!(obj instanceof Emiss)) return false;
/* 251 */     return this.c == ((Emiss)obj).c;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(Object o)
/*     */   {
/* 257 */     int i1 = ((Emiss)o).i;
/* 258 */     if (this.i == i1) return 0;
/* 259 */     return this.i < i1 ? -1 : 1;
/*     */   }
/*     */   
/* 262 */   public String toString() { return this.c; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Emiss get(String string)
/*     */     throws Exception
/*     */   {
/* 270 */     Emiss res = (Emiss)m.get(string);
/* 271 */     if (res == null) throw new Exception("nothing for " + string);
/* 272 */     return res;
/*     */   }
/*     */   
/*     */   public ComparableArray expand()
/*     */   {
/* 277 */     if ((this == X) || (this == two)) return ComparableArray.make(A, A);
/* 278 */     if (this == Y) return ComparableArray.make(A, B);
/* 279 */     if (this == Z) return ComparableArray.make(B, B);
/* 280 */     return ComparableArray.make(this);
/*     */   }
/*     */   
/*     */   public static int indexOf(Emiss n2) {
/* 284 */     for (int i = 0; i < stateSpace.size(); i++) {
/* 285 */       if (stateSpace.get(i) == n2) {
/* 286 */         return i;
/*     */       }
/*     */     }
/* 289 */     throw new RuntimeException("!");
/*     */   }
/*     */   
/*     */   public static Emiss switchElement(Emiss comp) {
/* 293 */     if (comp == A) return B;
/* 294 */     if (comp == B) return A;
/* 295 */     if (comp == X) return Z;
/* 296 */     if (comp == Z) return X;
/* 297 */     if (comp == Y) return Y;
/* 298 */     if (comp == N) return N;
/* 299 */     throw new RuntimeException("!!");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/Emiss.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */