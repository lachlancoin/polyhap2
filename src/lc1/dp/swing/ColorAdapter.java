/*     */ package lc1.dp.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lc1.util.Constants;
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
/*     */ public class ColorAdapter
/*     */ {
/*     */   final Color[] colors;
/*  23 */   static int[] cols = new int[5];
/*  24 */   static List<Color> defaultC = new ArrayList();
/*     */   
/*  26 */   public ColorAdapter() { for (int i = 0; i < cols.length; i++) {
/*  27 */       cols[i] = ((int)Math.floor(i * (255.0D / cols.length)));
/*     */     }
/*  29 */     for (int i = cols.length - 1; i >= 0; i--) {
/*  30 */       for (int j = 0; j < cols.length; j++) {
/*  31 */         for (int k = cols.length - 1; k >= 0; k--) {
/*  32 */           if (cols[i] > 230) { if (((cols[j] > 230 ? 1 : 0) & (cols[k] > 230 ? 1 : 0)) != 0) {}
/*  33 */           } else defaultC.add(new Color(cols[i], cols[j], cols[k]));
/*     */         }
/*     */       }
/*     */     }
/*  37 */     defaultC = randomise(defaultC);
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
/*  56 */     this.m = new HashMap();this.colors = ((Color[])defaultC.toArray(new Color[0]));this.mod = Constants.modify(0);
/*     */   }
/*     */   
/*     */   public ColorAdapter(Color[] array)
/*     */   {
/*  26 */     for (int i = 0; i < cols.length; i++) {
/*  27 */       cols[i] = ((int)Math.floor(i * (255.0D / cols.length)));
/*     */     }
/*  29 */     for (int i = cols.length - 1; i >= 0; i--) {
/*  30 */       for (int j = 0; j < cols.length; j++) {
/*  31 */         for (int k = cols.length - 1; k >= 0; k--) {
/*  32 */           if (cols[i] > 230) { if (((cols[j] > 230 ? 1 : 0) & (cols[k] > 230 ? 1 : 0)) != 0) {}
/*  33 */           } else defaultC.add(new Color(cols[i], cols[j], cols[k]));
/*     */         }
/*     */       }
/*     */     }
/*  37 */     defaultC = randomise(defaultC);
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
/*  56 */     this.m = new HashMap();this.colors = array;this.mod = Constants.modify0;
/*     */   }
/*     */   
/*     */   char[] mod;
/*  45 */   static Color[] col = { Color.RED, Color.GREEN, Color.CYAN, Color.BLUE, Color.PINK, Color.YELLOW, Color.MAGENTA, Color.ORANGE };
/*     */   
/*  47 */   private List<Color> randomise(List<Color> defaultC2) { List<Color> ne = new ArrayList();
/*  48 */     for (int i = 0; i < col.length; i++) {
/*  49 */       ne.add(col[i]);
/*     */     }
/*  51 */     while (defaultC2.size() > 0) {
/*  52 */       ne.add((Color)defaultC2.remove(Constants.nextInt(defaultC2.size())));
/*     */     }
/*  54 */     return ne;
/*     */   }
/*     */   
/*     */   Map<String, Color> m;
/*     */   Color[] color2;
/*     */   public Color getColor(int sh) {
/*  60 */     if (this.mod[sh] == '0') return getColor("0");
/*  61 */     if (this.mod[sh] == '2') return getColor("1");
/*  62 */     return getColor(sh);
/*     */   }
/*     */   
/*     */   public Color getColor(String st) {
/*  66 */     String[] str = st.split(":");
/*  67 */     Color[] color = new Color[str.length];
/*  68 */     if (str.length == 1) {
/*  69 */       color[0] = ((Color)this.m.get(str[0]));
/*  70 */       if (color[0] == null) {
/*  71 */         this.m.put(str[0], color[0] = this.colors[this.m.size()]);
/*     */       }
/*     */     }
/*     */     else {
/*  75 */       for (int i = 0; i < str.length; i++) {
/*  76 */         color[i] = getColor(str[i]);
/*     */       }
/*     */     }
/*  79 */     return merge(color);
/*     */   }
/*     */   
/*  82 */   public static Color merge(Color[] color2) { int[] rgb = new int[3];
/*  83 */     Arrays.fill(rgb, 0);
/*  84 */     float sum = 0.0F;
/*     */     
/*  86 */     for (int i = 0; i < color2.length; i++) {
/*  87 */       rgb[0] += color2[i].getRed();
/*  88 */       rgb[1] += color2[i].getGreen();
/*  89 */       rgb[2] += color2[i].getBlue();
/*     */     }
/*     */     
/*  92 */     for (int i = 0; i < rgb.length; i++) {
/*  93 */       rgb[i] = Math.min(255, rgb[i]);
/*     */     }
/*  95 */     return new Color(rgb[0], rgb[1], rgb[2]);
/*     */   }
/*     */   
/*     */ 
/*     */   public Color get(double[] d)
/*     */   {
/* 101 */     if (this.color2 == null)
/* 102 */       this.color2 = new Color[d.length];
/* 103 */     for (int i = 0; i < this.color2.length; i++) {
/* 104 */       this.color2[i] = getColor(i);
/*     */     }
/* 106 */     return merge1(this.color2, d);
/*     */   }
/*     */   
/* 109 */   public static Color merge1(Color[] color2, double[] d) { int[] rgb = new int[3];
/* 110 */     Arrays.fill(rgb, 0);
/* 111 */     double sum = 0.0D;
/*     */     
/* 113 */     for (int i = 0; i < color2.length; i++) {
/* 114 */       int tmp19_18 = 0; int[] tmp19_17 = rgb;tmp19_17[tmp19_18] = ((int)(tmp19_17[tmp19_18] + color2[i].getRed() * d[i])); int 
/* 115 */         tmp40_39 = 1; int[] tmp40_38 = rgb;tmp40_38[tmp40_39] = ((int)(tmp40_38[tmp40_39] + color2[i].getGreen() * d[i])); int 
/* 116 */         tmp61_60 = 2; int[] tmp61_59 = rgb;tmp61_59[tmp61_60] = ((int)(tmp61_59[tmp61_60] + color2[i].getBlue() * d[i]));
/* 117 */       sum += d[i];
/*     */     }
/* 119 */     for (int i = 0; i < rgb.length; i++) {
/* 120 */       rgb[i] = ((int)Math.round(rgb[i] / sum));
/*     */     }
/* 122 */     return new Color(rgb[0], rgb[1], rgb[2]);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/swing/ColorAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */