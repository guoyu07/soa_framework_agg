package com.James.demo.providerRegister;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.util.Utf8;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.James.avroProto.Message;
import com.James.avroProto.avrpRequestProto;
import com.James.basic.Annotation.InputParamAnnotation;
import com.James.basic.Annotation.OutputParamAnnotation;
import com.James.basic.Annotation.descriptionAnnotation;
import com.James.basic.Annotation.mockFilterAnnotation;
import com.James.basic.UtilsTools.Return;


/**
 * Created by James on 16/5/30.
 * 测试用的方法
 */
@RestController
public class RemoteMethod implements avrpRequestProto {

  //avro方法
  @descriptionAnnotation(author = "james",name="avrosend",submit_mode="",protocol="avro",desc="")
  @InputParamAnnotation(name ="param1",describe = "参数1")
  @InputParamAnnotation(name ="param2",describe = "参数2")
  @OutputParamAnnotation(name ="param2",describe = "参数2")
  @Override
  public Utf8 send(Message message)
      throws AvroRemoteException {
    System.out.println("avro取得的参数为:" + message.getParam() );
    return new Utf8("avrosend");
  }

  //通过http 方式调用 ,需要自行嵌入spring等容器
  @RequestMapping(value = "/start", method = RequestMethod.GET)
  @descriptionAnnotation(author = "james",name="start",submit_mode= "GET",protocol="http" ,desc="",version = "1.0")
  @InputParamAnnotation(name ="param1",describe = "参数1")
  @InputParamAnnotation(name ="param2",describe = "参数2")
  @OutputParamAnnotation(name ="param2",describe = "参数2",type="String")
  public Return start(){
      System.out.println("调用到start");
      return Return.SUCCESS(200,"调用start接口返回成功");
  }

  //通过http 方式调用 ,需要自行嵌入spring等容器
  @RequestMapping(value = "/mockCallReturn", method = RequestMethod.GET)
  @descriptionAnnotation(author = "james",name="mockCallReturn",submit_mode= "GET",protocol="http" ,desc="",version = "1.0")
  @InputParamAnnotation(name ="param1",describe = "参数1")
  @InputParamAnnotation(name ="param2",describe = "参数2")
  @OutputParamAnnotation(name ="param2",describe = "参数2",type="String")
  @mockFilterAnnotation(name="mockCallReturn_policy",policy = mockFilterAnnotation.Policy.Call_RETURN,code=400,note="mockCallReturn",allowFailTimes = 1)
  public Return mockCallReturn(){
    System.out.println("调用到mockCallReturn");
    return Return.FAIL(300, "调用mockCallReturn返回非200");
  }

  //通过http 方式调用 ,需要自行嵌入spring等容器
  @RequestMapping(value = "/mocFailReturn", method = RequestMethod.GET)
  @descriptionAnnotation(author = "james",name="mocFailReturn",submit_mode= "GET",protocol="http" ,desc="",version = "1.0")
  @InputParamAnnotation(name ="param1",describe = "参数1")
  @InputParamAnnotation(name ="param2",describe = "参数2")
  @OutputParamAnnotation(name ="param2",describe = "参数2",type="String")
  @mockFilterAnnotation(name="mocFailReturn_policy",policy = mockFilterAnnotation.Policy.Fail_RETURN)
  public Return mocFailReturn(){
    System.out.println("调用到mocFailReturn");
    return Return.FAIL(300, "调用mocFailReturn返回非200");
  }

  //重复的name会有报错
  @descriptionAnnotation(author = "james",name="start",submit_mode= "GET",protocol="http" ,desc="",version = "1.0")
  public void duplicatestart(){
    System.out.println("duplicatestart");
  }

}
