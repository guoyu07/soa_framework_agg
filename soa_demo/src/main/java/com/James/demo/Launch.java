package com.James.demo;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.James.Invoker.Invoker;
import com.James.Kafka_Tools.Kafka_Consumer;
import com.James.Kafka_Tools.Kafka_Producer;
import com.James.Provider.providerInstance;
import com.James.basic.UtilsTools.Parameter;
import com.James.demo.CodeInjection.hot_Injection;
import com.James.demo.Kafka.MsgCosum;
import com.James.kafka_Config.Configuration;

import UtilsTools.JsonConvert;


/**
 * Created by James on 16/7/21.
 */
@SpringBootApplication
public class Launch {

  private static final Logger logger = LogManager.getLogger(Launch.class.getName());

  private static Configuration configuration = null;
  static {
    Properties properties = new Properties();
    properties.put("zookeeper", "192.168.202.16:2181/kafka");
    properties.put("kafka","192.168.202.34:9092,192.168.202.35:9092,192.168.202.36:9092");

    try{
      configuration = Configuration.getInstance().initialization(properties);
    }catch(Exception e){
      e.printStackTrace();
    }


  }

  //代码注入sample
  public void hotInject(){

    logger.info("开始注入");
    hot_Injection injection = new hot_Injection();
    injection.inject();

    try{
      injection.buildString(2);

      logger.info("注入结束,应当能看到执行方法开始时间和总耗时");
    }catch(Exception e){
      e.printStackTrace();
    }

  }

  //服务发现sample
  public void discovery(){

    logger.info("服务发现开始启动");
    //zookeeper地址
    String zkconnect = "192.168.202.16:2181/kafka";

    Properties properties = new Properties();
    properties.setProperty("HttpPort","8080");
    properties.setProperty("zkConnect",zkconnect);

    //服务提供方的服务名称
    providerInstance.getInstance().readConfig(properties).startServer("com.James.demo");
    //调用方
    Invoker demoinvoke = Invoker.create("com.James.demo",zkconnect);

    //取得远端服务的可用节点
    logger.info("start的可用节点为:" + JsonConvert.toJson(demoinvoke.getAvailableProvider("start")));
    logger.info("avrosend的可用节点为"+ JsonConvert.toJson(demoinvoke.getAvailableProvider("avrosend")));

    //调用2个接口
    logger.info("start 返回:" + demoinvoke.call("start", Parameter.create()));
    logger.info("avrosend 返回:" + demoinvoke.call("avrosend", Parameter.create()));
  }

  //kafka收消息sample
  //消息处理在MsgCosum中处理
  public void receiveKafka(){

    logger.info("kafka消费者开始工作");
    Kafka_Consumer kafka_Consumer = new Kafka_Consumer();
    kafka_Consumer.consume(configuration, "soa_group_test", "largest", 2, "soa_test", MsgCosum.class);
  }

  //kafka发送消息sample
  public void sendKafka() throws Exception{

      logger.info("kafka生产者开始工作");
      Kafka_Producer.getInstance().start(configuration);
      int i=0;

      while(i<=1000){
        i++;
        Kafka_Producer.getInstance().send("soa_test","key",String.valueOf(i));

        TimeUnit.SECONDS.sleep(1L);
      }
  }


  public static void main(String[] args) throws Exception {
    Launch launch = new Launch();
    //代码注入
    launch.hotInject();

    SpringApplication.run(Launch.class, args);
    //http服务和avro服务
    launch.discovery();
    //kafka测试
    launch.receiveKafka();
    launch.sendKafka();



      Thread.currentThread().join();
    //System.exit(0);
  }



}
