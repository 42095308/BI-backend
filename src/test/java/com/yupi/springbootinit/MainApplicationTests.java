package com.yupi.springbootinit;

import com.yupi.springbootinit.config.WxOpenConfig;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * 主类测试
 *

 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }


    @Test
    void name() {
        String s = "【【【【【\n" + "{\n" + "    title: {\n" + "        text: '网站用户增长情况',\n" + "        subtext: ''\n" + "        },\n" + "    tooltip: {\n" + "        trigger: 'axis',\n" + "        axisPointer: {\n" + "            type: 'shadow'\n" + "            }\n" + "    },\n" + "    legend: {\n" + "        data: ['用户数']\n" + "        },\n" + "    xAxis: {\n" + "        data: ['1号', '2号', '3号']\n" + "    },\n" + "    yAxis: {},\n" + "    series: [{\n" + "        name: '用户数',\n" + "        type: 'line',\n" + "        data: [10, 20, 30]\n" + "    }]\n" + "}\n" + "【【【【【\n" + "根据数据分析可得，该网站用户数量逐日增长，时间越长，用户数量增长越多。";
        String[] split = s.split("【【【【【");
        System.out.println(split.length);
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
            System.out.println("----------------------");
        }
    }
}
