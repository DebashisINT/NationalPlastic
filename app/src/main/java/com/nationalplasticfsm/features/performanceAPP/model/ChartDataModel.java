package com.nationalplasticfsm.features.performanceAPP.model;

import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel;
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType;
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels;

public class ChartDataModel {
    public static AAChartModel configurePieChart(Integer attendP,Integer absentP) {
        return new AAChartModel()
                .chartType(AAChartType.Pie)
                .backgroundColor("#ffffff")
                .title("")
                .subtitle("")
                .dataLabelsEnabled(true)//是否直接显示扇形图数据
                .yAxisTitle("")
                .series(new AAPie[] {
                        new AAPie()
                                .name("")
                                .innerSize("60%")
                                .size(90)
                                .dataLabels(new AADataLabels()
                                        .enabled(true)
                                        .useHTML(true)
                                        .distance(10)
                                        //.format("<b>{point.name}</b>: <br> {point.percentage:.1f} %"))
                                        .format("<b></b> {point.percentage:.1f} %"))
                                .data(new Object[][] {
                                {"Present"  ,attendP},
                                {"Not Logged In",absentP}
                        })
                }).colorsTheme(new String[]{"#0019b2","#f5862c"}); // sky,red
    }
}
