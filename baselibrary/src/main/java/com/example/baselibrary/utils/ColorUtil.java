package com.example.baselibrary.utils;

/**
 * Created by fangsf on 2018/7/17.
 * Useful:
 */
public class ColorUtil {

    /**
     * HSB转RGB
     * @param hsb
     * @return
     */
    public static float[] hsb2rgb(float[] hsb) {
        float[] rgb = new float[3];
        //先令饱和度和亮度为100%，调节色相h
        for (int offset = 240, i = 0; i < 3; i++, offset -= 120) {
            //算出色相h的值和三个区域中心点(即0°，120°和240°)相差多少，然后根据坐标图按分段函数算出rgb。但因为色环展开后，红色区域的中心点是0°同时也是360°，不好算，索性将三个区域的中心点都向右平移到240°再计算比较方便
            float x = Math.abs((hsb[0] + offset) % 360 - 240);
            //如果相差小于60°则为255
            if (x <= 60) rgb[i] = 255;
                //如果相差在60°和120°之间，
            else if (60 < x && x < 120) rgb[i] = ((1 - (x - 60) / 60) * 255);
                //如果相差大于120°则为0
            else rgb[i] = 0;
        }
        //在调节饱和度s
        for (int i = 0; i < 3; i++)
            rgb[i] += (255 - rgb[i]) * (1 - hsb[1]);
        //最后调节亮度b
        for (int i = 0; i < 3; i++)
            rgb[i] *= hsb[2];
        return rgb;
    }
}
