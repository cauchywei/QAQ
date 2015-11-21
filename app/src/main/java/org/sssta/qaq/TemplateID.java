package org.sssta.qaq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mac on 15/11/21.
 */
public class TemplateID {
    public static final int template_1 = R.drawable.template1;
    public static final int template_2 = R.drawable.template2;
    public static final int template_3 = R.drawable.template3;
    public static final int template_4 = R.drawable.template4;
    public static final int template_5 = R.drawable.template5;
    public static final int template_6 = R.drawable.template6;

    public static List<Integer> templateIDList = new ArrayList<>(Arrays.asList(template_1,
            template_2,template_3,template_4,template_5,template_6));

    public static List<Integer> templateBmpHeightList = new ArrayList<>(Arrays.asList(
            107,111,402,197,202,87
    ));
    public static List<Double> templateFWidthList = new ArrayList<>(Arrays.asList(
            //0.389,0.313,0.2875,0.266,0.432,0.571
            0.289,0.333,0.2875,0.306,0.432,0.501
    ));
    public static List<Double> templateFHeightList = new ArrayList<>(Arrays.asList(
            //0.364,0.441,0.373,0.183,0.515,0.621
            0.464,0.481,0.373,0.203,0.505,0.521
    ));
    public static List<Double> templateCenterXList = new ArrayList<>(Arrays.asList(
            //0.5,0.313,0.6225,0.487,0.573,0.558
            0.5,0.313,0.6225,0.487,0.503,0.508
    ));
    public static List<Double> templateCenterYList = new ArrayList<>(Arrays.asList(
            //0.449,0.513,0.5,0.447,0.668,0.437
            0.449,0.513,0.5,0.407,0.538,0.437
    ));
    public static List<Float> templateSpinAngleList = new ArrayList<>(Arrays.asList(
            5.0f,0.0f,1.0f,-15.0f,0.0f,0.0f
    ));

}
