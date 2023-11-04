package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.YuPiChart;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.utils.ExcelUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private ChartService chartService;
    @Resource
    private YuPiChart yuPiChart;

    final long modelID = 1659171950288818178L;

    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.MY_DIRECT_QUEUE}, ackMode = "MANUAL")
    public void receiveMsg(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMsg：{}", msg);
        if (StringUtils.isBlank(msg)) {
            // 拒接消息，不在放入队列
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "消息为空");
        }
        Chart chart = chartService.getById(Long.parseLong(msg));

        if (chart == null) {
            // 拒接消息，不在放入队列
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图表不存在");
        }


        // 将状态设置为running
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            // 拒接消息，不在放入队列
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表状态失败");
            return;
        }
        // ai处理后的结果
        String result = yuPiChart.doChart(modelID, buildUserInput(chart));

        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            // 拒接消息，不在放入队列
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "ai 生成失败");
            return;
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        // 对结果进行封装，将数据插入到数据库
        updateChart.setGenChart(genChart);
        updateChart.setGenResult(genResult);
        updateChart.setStatus("success");
        boolean b1 = chartService.updateById(updateChart);
        if (!b1) {
            // 拒接消息，不在放入队列
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }

        channel.basicAck(deliveryTag, false);
    }


    private String buildUserInput(Chart chart) {
        //从数据库中取出
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }


    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("fail");
        updateChartResult.setExecMessage(execMessage);
        boolean b = chartService.updateById(updateChartResult);
        if (!b) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }

    }
}
