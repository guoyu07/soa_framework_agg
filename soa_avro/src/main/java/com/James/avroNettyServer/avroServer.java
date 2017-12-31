package com.James.avroNettyServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.James.avroProto.Message;
import com.James.avroProto.avrpRequestProto;
import com.James.avroServiceRegist.avroRequestHandleRegister;
import com.James.basic.Enum.BasicCode;
import com.James.basic.UtilsTools.CommonConfig;
import com.James.basic.UtilsTools.JsonConvert;
import com.James.basic.UtilsTools.Return;


/**
 * Created by James on 16/6/26.
 * AVRO服务端
 *
 */

public class avroServer {

  private static final Log logger = LogFactory.getLog(avroServer.class.getName());

  public static class avrpRequestProtoImpl implements avrpRequestProto {
    // in this simple example just return details of the message
    public Utf8 send(Message message) {

      if(message.getRequestName()==null||message.getRequestName().length()<=0){
        logger.error("RequestName参数不正确");
        Return ret = Return.FAIL(BasicCode.parameters_incorrect.code, BasicCode.parameters_incorrect.name());
        return new Utf8(ret.toJson());
      }

      try{
        Map<String,String> par = JsonConvert.toObject(message.getParam().toString(), HashMap.class);
        //TrackingChain tc = ThreadLocalCache.getCallchain().get();
//        if(tc==null){
//          tc=new TrackingChain(par.get(CommonConfig.s_trackingID));
//        }
//        tc.setClientID(CommonConfig.clientID);

        //ThreadLocalCache.setCallchain(tc);
      }catch(Exception e){
        e.printStackTrace();
        Return ret = Return.FAIL(BasicCode.parameters_incorrect.code, BasicCode.parameters_incorrect.name());
        return new Utf8(ret.toJson());
      }

      String response ="";
      Class avrpRequestProto = avroRequestHandleRegister.INSTANCE.getRequestHandle(
          message.getRequestName().toString());
//      avrpRequestProto avrpRequestProto =  avroRequestHandleRegister.INSTANCE.getRequestHandle(
//          message.getRequestName().toString());
      if(avrpRequestProto==null){
        Return ret = Return.FAIL(BasicCode.service_not_found.code, BasicCode.service_not_found.name());
        return new Utf8(ret.toJson());
      }
      try{
        avrpRequestProto avrpRequestProtoImpl = (avrpRequestProto) avrpRequestProto.newInstance();
        response = avrpRequestProtoImpl.send(message).toString();
      }catch(Exception e){
        e.printStackTrace();
        logger.error("转发"+message.getRequestName() + "服务异常",e);
        Return ret = Return.FAIL(BasicCode.error.code, BasicCode.error.name());
        return new Utf8(ret.toJson());
      }
      Return ret = Return.SUCCESS(BasicCode.success.code, BasicCode.success.name()).put("data",response);
      return new Utf8(ret.toJson());
    }
  }

  private static Server server;

  public static void startServer() throws IOException {
    startServer(Integer.valueOf(CommonConfig.defaultAvroPort));

  }

  public static void startServer(int port) throws IOException {
    server = new NettyServer(new SpecificResponder(avrpRequestProto.class, new avrpRequestProtoImpl()), new InetSocketAddress(port));
    logger.info("avro Netty Server Started @ " + port);
  }


}
