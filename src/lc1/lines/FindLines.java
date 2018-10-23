/*     */ package lc1.lines;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.genotype.io.scorable.PhasedIntegerGenotypeData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FindLines
/*     */ {
/*  25 */   static int dist = 500;
/*  26 */   static int num = 2;
/*     */   
/*  28 */   SortedSet<Line> lineset = new TreeSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   List<Line>[] allocation;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   List<Integer> findFree(Line line)
/*     */   {
/*  69 */     List<Integer> res = new ArrayList(this.allocation.length);
/*  70 */     for (int i = 0; i < this.allocation.length; i++) {
/*  71 */       if (this.allocation[i].size() > 0) {
/*  72 */         Line lastLine = (Line)this.allocation[i].get(this.allocation[i].size() - 1);
/*  73 */         if (line.getStart().longValue() > lastLine.getEnd().longValue()) {
/*  74 */           res.add(Integer.valueOf(i));
/*     */         }
/*     */       }
/*     */       else {
/*  78 */         res.add(Integer.valueOf(i));
/*     */       }
/*     */     }
/*  81 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Double[] getMaf(List<PhasedIntegerGenotypeData>[] data, int noSites)
/*     */   {
/*  92 */     throw new Error("Unresolved compilation problem: \n\tCannot cast from Comparable to Object[]\n");
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
/*     */   FindLines(PhasedIntegerGenotypeData[] data, BufferedReader locs)
/*     */     throws Exception
/*     */   {
/* 107 */     Map<Integer, List<Line>> lines = new HashMap();
/* 108 */     String st = locs.readLine();
/* 109 */     for (int i = 0; i < data[0].length(); i++) {
/* 110 */       st = locs.readLine();
/* 111 */       Long locations = Long.valueOf(Long.parseLong(st.split("\\s+")[0]));
/* 112 */       int sum = 0;
/* 113 */       for (int j = 0; j < data.length; j++) {
/* 114 */         sum += ((Integer)data[j].getElement(i)).intValue();
/*     */       }
/* 116 */       List<Line> list = (List)lines.get(Integer.valueOf(sum));
/* 117 */       if (list == null) lines.put(Integer.valueOf(sum), list = new ArrayList());
/* 118 */       Line currentLine = null;
/* 119 */       if (list.size() > 0) {
/* 120 */         currentLine = (Line)list.get(list.size() - 1);
/* 121 */         if (locations.longValue() > currentLine.getEnd().longValue() + dist) {
/* 122 */           if (currentLine.size() < num) {
/* 123 */             list.remove(list.size() - 1);
/*     */           }
/* 125 */           list.add(currentLine = new Line(locations, Integer.valueOf(i)));
/*     */         }
/*     */         else {
/* 128 */           currentLine.add(locations, Integer.valueOf(i));
/*     */         }
/*     */       }
/*     */       else {
/* 132 */         list.add(currentLine = new Line(locations, Integer.valueOf(i)));
/*     */       }
/*     */     }
/*     */     
/* 136 */     if ((st = locs.readLine()) != null) Logger.getAnonymousLogger().warning("did not read all location file");
/* 137 */     for (Iterator<Map.Entry<Integer, List<Line>>> it = lines.entrySet().iterator(); it.hasNext();) {
/* 138 */       Map.Entry<Integer, List<Line>> entry = (Map.Entry)it.next();
/* 139 */       List<Line> list = (List)entry.getValue();
/* 140 */       if (list.size() > 0) {
/* 141 */         Line lastLine = (Line)list.get(list.size() - 1);
/* 142 */         if (lastLine.size() < num) list.remove(list.size() - 1);
/*     */       }
/* 144 */       if (((List)entry.getValue()).size() == 0) { it.remove();
/*     */       } else {
/* 146 */         this.lineset.addAll((Collection)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static Double[] getMaf(List<PhasedIntegerGenotypeData> data)
/*     */   {
/* 153 */     return getMaf(new List[] { data }, ((PhasedIntegerGenotypeData)data.get(0)).length());
/*     */   }
/*     */   
/*     */   public static Double[] getMaf(List<PhasedIntegerGenotypeData> data, Double[] res, Double[] r2) {
/* 157 */     Character[] minorAllele = new Character[res.length];
/* 158 */     for (int i = 0; i < ((PhasedIntegerGenotypeData)data.get(0)).length(); i++) {
/* 159 */       SortedMap<Character, Integer> m = new TreeMap();
/* 160 */       for (int j = 0; j < data.size(); j++) {
/* 161 */         Character element = (Character)((PhasedIntegerGenotypeData)data.get(j)).getElement(i);
/* 162 */         if (!element.equals(Character.valueOf('N'))) {
/* 163 */           Integer val = (Integer)m.get(element);
/* 164 */           m.put(element, Integer.valueOf(val == null ? 1 : val.intValue() + 1));
/*     */         } }
/* 166 */       if (m.keySet().size() > 2) {
/* 167 */         System.err.println("warning !! " + m.keySet());
/*     */       }
/* 169 */       if (m.keySet().size() == 0) {
/* 170 */         res[i] = null;
/*     */       }
/* 172 */       else if (m.keySet().size() == 1) {
/* 173 */         res[i] = Double.valueOf(0.0D);
/*     */       }
/*     */       else {
/* 176 */         int cnt1 = ((Integer)m.get(m.firstKey())).intValue();
/* 177 */         int cnt2 = ((Integer)m.get(m.lastKey())).intValue();
/* 178 */         minorAllele[i] = (cnt1 < cnt2 ? (Character)m.firstKey() : (Character)m.lastKey());
/* 179 */         res[i] = Double.valueOf(Math.min(cnt1, cnt2) / (cnt1 + cnt2));
/* 180 */         if ((i > 0) && (res[(i - 1)] != null) && (res[(i - 1)].doubleValue() != 0.0D)) {
/* 181 */           int total = 0;
/* 182 */           int sum = 0;
/* 183 */           for (int j = 0; j < data.size(); j++) {
/* 184 */             Character element = (Character)((PhasedIntegerGenotypeData)data.get(j)).getElement(i);
/* 185 */             Character element1 = (Character)((PhasedIntegerGenotypeData)data.get(j)).getElement(i - 1);
/* 186 */             if ((!element.equals(Character.valueOf('N'))) && (!element1.equals(Character.valueOf('N')))) {
/* 187 */               total++;
/* 188 */               if ((element.equals(minorAllele[i])) && (element1.equals(minorAllele[(i - 1)]))) {
/* 189 */                 sum++;
/*     */               }
/* 191 */               r2[i] = Double.valueOf(sum / total);
/*     */             }
/*     */           }
/*     */         }
/*     */       } }
/* 196 */     return res;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/lines/FindLines.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */