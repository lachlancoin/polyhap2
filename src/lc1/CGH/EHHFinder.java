/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.sequenced.Convert;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class EHHFinder
/*     */ {
/*     */   Collection<String> set;
/*     */   String base;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  34 */       Constants.parse(args);
/*  35 */       thresh = 0;
/*  36 */       lim = Integer.valueOf(Integer.MAX_VALUE);
/*  37 */       thresh1 = 0.0D;
/*  38 */       File user = new File(System.getProperty("user.dir"));
/*  39 */       Location[] del = Convert.getDeletions(new File(user, "../CGH/deletion_samples.txt"), 
/*  40 */         Integer.valueOf(Integer.parseInt(user.getName())), 
/*  41 */         Constants.mid());
/*  42 */       Collection<String> l = del[0].noObs;
/*  43 */       SimpleDataCollection sdt = 
/*  44 */         SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(user, "phased1.txt"), Emiss.class, Emiss.getEmissionStateSpace(1));
/*  45 */       sdt.removeKeyIfStartsWith("NA");
/*  46 */       PrintWriter pw = new PrintWriter(new java.io.BufferedWriter(new FileWriter(new File(user, "hap_summary.txt"))));
/*  47 */       EHHFinder ehh = new EHHFinder(sdt);
/*     */       
/*  49 */       int st1 = sdt.loc.indexOf(Long.valueOf(del[0].min));
/*  50 */       int end1 = sdt.loc.indexOf(Long.valueOf(del[0].max));
/*  51 */       Set<String> coreHaps = sdt.getAllHaplotypes(st1, end1).keySet();
/*  52 */       for (Iterator<String> it = coreHaps.iterator(); it.hasNext();) {
/*  53 */         String st = (String)it.next();
/*  54 */         pw.println("HAPLOTYPES FOR " + st);
/*  55 */         ehh.base = st;
/*  56 */         System.err.println(st);
/*  57 */         if ((st.startsWith("_")) && (st.endsWith("_"))) { ehh.set = l;
/*     */         }
/*     */         else {
/*  60 */           ehh.set = null;
/*     */         }
/*  62 */         ehh.extend(st, st1, end1, pw);
/*     */         
/*  64 */         pw.println("##############################################");
/*  65 */         pw.flush();
/*     */       }
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*  70 */       exc.printStackTrace();
/*     */     } }
/*     */   
/*  73 */   Comparator comp = new Comparator() {
/*     */     public int compare(String o1, String o2) {
/*  75 */       int l1 = o1.length();
/*  76 */       int l2 = o2.length();
/*  77 */       if (l1 != l2) return l1 > l2 ? -1 : 1;
/*  78 */       return o1.compareTo(o2);
/*     */     }
/*     */   };
/*     */   
/*  82 */   static int thresh = 3;
/*  83 */   static double thresh1 = 0.2D;
/*     */   final SimpleDataCollection sdt;
/*     */   
/*  86 */   public EHHFinder(SimpleDataCollection sdt2) { this.sdt = sdt2; }
/*     */   
/*     */   private Set<String> getIndividuals(List<PIGData> value) {
/*  89 */     Set<String> res = new HashSet();
/*  90 */     for (int i = 0; i < value.size(); i++) {
/*  91 */       res.add(((PIGData)value.get(i)).getName());
/*     */     }
/*  93 */     return res;
/*     */   }
/*     */   
/*     */   public void extend(Aberation ab, PrintWriter pw) {
/*  97 */     String[] nme = ab.name.split("_");
/*  98 */     PIGData haplotype = this.sdt.get(nme[0]).split()[Integer.parseInt(nme[1])];
/*  99 */     String string = haplotype.getStringRep(ab.start, ab.end);
/* 100 */     extend(string, ab.start, ab.end, pw);
/*     */   }
/*     */   
/*     */   public void extend(String string, int start_, int end_, PrintWriter pw)
/*     */   {
/* 105 */     List<PIGData> dat = (List)this.sdt.getAllHaplotypes(start_, end_).get(string);
/*     */     
/* 107 */     if (dat == null) return;
/* 108 */     if (this.set != null) {
/* 109 */       Set<String> hapl_names = new HashSet();
/* 110 */       for (Iterator<PIGData> it = dat.iterator(); it.hasNext();) {
/* 111 */         PIGData dat_ = (PIGData)it.next();
/* 112 */         String name = dat_.getName().split("_")[0].split("\\|")[0];
/* 113 */         if (name.startsWith("NA")) { it.remove();
/*     */         }
/* 115 */         else if (!this.set.contains(name))
/*     */         {
/*     */ 
/* 118 */           pw.println("deletion_samples.txt does not contain " + name + " " + dat_.getStringRep(start_ - 5, end_ + 5));
/*     */         }
/*     */         else
/* 121 */           hapl_names.add(name);
/*     */       }
/* 123 */       for (Iterator<String> it = this.set.iterator(); it.hasNext();) {
/* 124 */         String key = (String)it.next();
/* 125 */         if (!hapl_names.contains(key)) {
/* 126 */           pw.println("phased deletion does not contain " + key);
/*     */         }
/*     */       }
/*     */     }
/* 130 */     this.orig_size = dat.size();
/* 131 */     pw.println("original size is " + this.orig_size);
/* 132 */     SortedMap<Integer, SortedMap<String, Object[]>> map = new TreeMap();
/*     */     
/*     */ 
/* 135 */     extend(start_, end_, string, dat, map, 0, string.length());
/* 136 */     pw.println("aberation " + string + " " + this.sdt.loc.get(start_) + " " + this.sdt.loc.get(end_));
/*     */     
/* 138 */     int cnt = 0;
/* 139 */     for (Iterator<Integer> it = map.keySet().iterator(); it.hasNext(); cnt++) {
/* 140 */       Integer key1 = (Integer)it.next();
/* 141 */       SortedMap<String, Object[]> m1 = (SortedMap)map.get(key1);
/* 142 */       List<PIGData> vals = null;
/* 143 */       for (Iterator<String> it1 = m1.keySet().iterator(); it1.hasNext();) {
/* 144 */         String key = (String)it1.next();
/* 145 */         Object[] val = (Object[])((SortedMap)map.get(key1)).get(key);
/* 146 */         List<PIGData> val0 = (List)val[0];
/*     */         
/* 148 */         if ((vals == null) || (!val0.equals(vals))) {
/* 149 */           vals = val0;
/* 150 */           int start = ((Integer)val[1]).intValue();
/* 151 */           int end = ((Integer)val[2]).intValue();
/*     */           
/* 153 */           PIGData da = lc1.dp.data.representation.SimpleScorableObject.make("", Arrays.asList(new String[] { key }), Emiss.getEmissionStateSpace(1));
/* 154 */           StringWriter sw = new StringWriter();
/* 155 */           PrintWriter pw1 = new PrintWriter(sw);
/* 156 */           da.print(pw1, false, true, true, null);
/* 157 */           String st1 = sw.toString();
/* 158 */           Set<String> indv = getIndividuals(val0);
/*     */           
/* 160 */           pw.println(this.base + " : " + da.length() + "\tr" + val0.size() + " of " + this.orig_size + "\tl " + (((Integer)this.sdt.loc.get(end)).intValue() - ((Integer)this.sdt.loc.get(start)).intValue()) + "\t" + indv);
/* 161 */           pw.println(st1.substring(0, st1.indexOf("\n") - 1) + "|\t\t\t" + this.base + "\t\t\t" + val0.size() / this.orig_size);
/*     */         }
/*     */       }
/*     */     }
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
/*     */ 
/*     */   int orig_size;
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
/*     */   private void print(SortedMap<Integer, SortedMap<String, Object[]>> map, Integer key1, String key) {}
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
/* 203 */   static Integer lim = Integer.valueOf(1);
/*     */   
/*     */   public void extend(int start, int end, String key, List<PIGData> dat, SortedMap<Integer, SortedMap<String, Object[]>> map, int base_index0, int base_index1) {
/* 206 */     if ((dat.size() < thresh) || (dat.size() < thresh1 * this.orig_size)) { return;
/*     */     }
/* 208 */     SortedMap<String, Object[]> map1 = (SortedMap)map.get(Integer.valueOf(dat.size()));
/* 209 */     if (map1 == null) map.put(Integer.valueOf(dat.size()), map1 = new TreeMap(this.comp));
/* 210 */     char[] ch = key.toCharArray();
/* 211 */     Arrays.fill(ch, base_index0, base_index1, '*');
/* 212 */     String key1 = new String(ch);
/* 213 */     if (map1.containsKey(key1)) { return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 218 */     map1.put(key1, new Object[] { dat, Integer.valueOf(start), Integer.valueOf(end) });
/*     */     
/* 220 */     System.err.println(key + " " + dat.size());
/* 221 */     if (end < ((PIGData)dat.get(0)).length() - 1) {
/* 222 */       int k = 0;
/* 223 */       Iterator<Map.Entry<String, List<PIGData>>> it = 
/* 224 */         this.sdt.getHaplotypes(this.sdt.getHaplotypes(start, end + 1, dat)).iterator();
/* 225 */       for (; (it.hasNext()) && (k < lim.intValue()); k++) {
/* 226 */         Map.Entry<String, List<PIGData>> right = (Map.Entry)it.next();
/* 227 */         extend(start, end + 1, (String)right.getKey(), (List)right.getValue(), map, base_index0, base_index1);
/*     */       }
/*     */     }
/* 230 */     if (start - 1 > 0) {
/* 231 */       int k = 0;
/* 232 */       Iterator<Map.Entry<String, List<PIGData>>> it = this.sdt.getHaplotypes(this.sdt.getHaplotypes(start - 1, end, dat)).iterator();
/* 233 */       for (; (it.hasNext()) && (k < lim.intValue()); k++) {
/* 234 */         Map.Entry<String, List<PIGData>> left = (Map.Entry)it.next();
/* 235 */         extend(start - 1, end, (String)left.getKey(), (List)left.getValue(), map, base_index0 + 1, base_index1 + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/EHHFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */