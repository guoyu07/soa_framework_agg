package com.James.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.James.Filter.Annotation.degradeAnnotation;
import com.James.Filter.Annotation.ratelimitAnnotation;
import com.James.Filter.Filter;
import com.James.Filter.degrade.degradeCountDown;
import com.James.Filter.rateLimit.ratelimitCountDown;
import com.James.basic.Annotation.InputParamAnnotation;
import com.James.basic.Annotation.OutputParamAnnotation;
import com.James.basic.Annotation.descriptionAnnotation;
import com.James.basic.Enum.Code;
import com.James.basic.Model.inputParam;
import com.James.basic.Model.outputParam;
import com.James.basic.Model.sharedNode;
import com.James.basic.UtilsTools.CommonConfig;
import com.James.soa_agent.HotInjecter;
import com.James.soa_agent.event_handle.ScanAnnotationClass_Handle;


/**
 * Created by James on 16/9/28.
 * 服务扫描
 * 通过注解扫描
 */
public class providerScanImpl implements ScanAnnotationClass_Handle {
  private static final Log LOGGER = LogFactory.getLog(providerScanImpl.class.getName());

  @Override
  public Object attach_annotation(Set<Class<?>> Classes,Class<? extends Annotation> annotationClass) {

    Set<Class<?>> classes = new LinkedHashSet();
    Set<Class<?>> classset = HotInjecter.getInstance().getClasses();

    classset.forEach(clazz ->{
      Method[] methods = clazz.getMethods();
      for(Method method : methods){
        Annotation pAnnotation = method.getAnnotation(annotationClass);
        if(pAnnotation!=null){
          classes.add(clazz);
        }
      }
    });

    return classes;
  }


  //扫描指定的class
  //读取desc信息
  //读取方法上的入参和出参
  //读取Filter信息
  public static List<sharedNode> readClasses(Class<?> clazz,String serverName){

    List<sharedNode> sharedNodes =new ArrayList<>();

    Method[] methods = clazz.getMethods();
    for(Method method : methods){
      descriptionAnnotation descriptionAnno = method.getAnnotation(descriptionAnnotation.class);
      InputParamAnnotation[] inParams = method.getAnnotationsByType(InputParamAnnotation.class);
      OutputParamAnnotation[] outParams = method.getAnnotationsByType(OutputParamAnnotation.class);

      sharedNode sharedNode = new sharedNode(serverName);
      //读取desc信息
      sharedNode = getDescribe(sharedNode,method);

      //降级 filter
      degradeAnnotation DegradeAnnotation = method.getAnnotation(degradeAnnotation.class);
      if(DegradeAnnotation!=null){
        if(descriptionAnno!=null){

          int code = DegradeAnnotation.code() >0 ? DegradeAnnotation.code(): Code.service_degrade.code;
          String note = DegradeAnnotation.note().length()>0 ? DegradeAnnotation.note(): Code.service_degrade.note;

          degradeCountDown DegradeCountDown = new degradeCountDown(DegradeAnnotation.name(),
                                                        DegradeAnnotation.allowPeriod(),
                                                        DegradeAnnotation.allowTimes(),
                                                        DegradeAnnotation.freezingTime(),
                                                        code,
                                                        note
                                                        );

          Filter.getInstance().addDegradeConfig(DegradeCountDown);
          sharedNode.addDegradeFilter(DegradeCountDown.getName());
        }else{
          LOGGER.warn("检测到"+method.getName()+"配置了降级策略,但是没有配置description");
        }
      }

      //限流 filter
      ratelimitAnnotation RatelimitAnnotation = method.getAnnotation(ratelimitAnnotation.class);
      if(RatelimitAnnotation!=null){
        if(descriptionAnno!=null){

          int code = RatelimitAnnotation.code() >0 ? RatelimitAnnotation.code(): Code.over_limit.code;
          String note = RatelimitAnnotation.note().length()>0 ? RatelimitAnnotation.note(): Code.over_limit.note;

          ratelimitCountDown RatelimitCountDown = new ratelimitCountDown(RatelimitAnnotation.name(),
                                                              RatelimitAnnotation.allowPeriod(),
                                                              RatelimitAnnotation.allowTimes(),
                                                              RatelimitAnnotation.freezingTime(),
                                                              code,
                                                              note
          );

          Filter.getInstance().addLimitConfig(RatelimitCountDown);
          sharedNode.addRatelimitFilter(RatelimitCountDown.getName());
        }else{
          LOGGER.warn("检测到"+method.getName()+"配置了限流策略,但是没有配置description");
        }
      }

      //Inparam,Outparam
      for(InputParamAnnotation inParam: inParams){
        inputParam InputParam = new inputParam();
        InputParam.setName(inParam.name());
        InputParam.setType(inParam.type());
        InputParam.setDescribe(inParam.describe());
        InputParam.setRequired(inParam.Required());
        InputParam.setDefault_value(inParam.default_value());

        sharedNode.addInputParam(InputParam);

      }

      for(OutputParamAnnotation outParam: outParams){
        outputParam OutputParam = new outputParam();
        OutputParam.setName(outParam.name());
        OutputParam.setType(outParam.type());
        OutputParam.setDescribe(outParam.describe());
        OutputParam.setRequired(outParam.Required());
        OutputParam.setDefault_value(outParam.default_value());

        sharedNode.addOutputParam(OutputParam);
      }

      sharedNodes.add(sharedNode);
    }

    return sharedNodes;

  }

  //生成shareNode的描述
  public static sharedNode getDescribe(sharedNode sharedNode,Method method) {

    descriptionAnnotation describe = method.getAnnotation(descriptionAnnotation.class);

    if (describe != null) {
      LOGGER.info("开始扫描" + method.getDeclaringClass().getName() + "类下的" + method.getName() + "方法");
      sharedNode.setAuthor(describe.author());

      sharedNode.setDescribe(describe.desc());
      sharedNode.setVersion(describe.version());

      sharedNode.setProtocol(CommonConfig.PROTOCOL.valueOf(describe.protocol()));

      sharedNode.setSubmit_mode(describe.submit_mode());

      if(describe.protocol().equals(CommonConfig.PROTOCOL.avro.name())){
        sharedNode.setMethod_name(describe.name());
        //avro需要全限定名
        sharedNode.setDeclaringClass_name(method.getDeclaringClass().getName());
        sharedNode.setRpc_port(providerInstance.getInstance().getDefaultAvroPort());
      }

      if(describe.protocol().equals(CommonConfig.PROTOCOL.http.name())){
        sharedNode.setMethod_name(describe.name());
        sharedNode.setHttp_port(providerInstance.getInstance().getDefaultHttpPort());
        sharedNode.setHttp_context(providerInstance.getInstance().getDefaultHttpContext());
      }

      if(!sharedNode.isAvailable()) {
        LOGGER.error(sharedNode.getMethod_name()+"不可用");
      }
    }

    return sharedNode;
  }
}
