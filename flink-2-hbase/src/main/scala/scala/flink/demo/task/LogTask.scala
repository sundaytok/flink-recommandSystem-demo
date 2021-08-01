package scala.flink.demo.task

import com.demo.map.LogMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import scala.flink.demo.client.HbaseClient
import scala.flink.demo.util
import scala.flink.demo.util.{LogToEntity, Property}

object LogTask {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = Property.getKafkaProperties("log")
    val dataStream: DataStreamSource[String] = env.addSource(new FlinkKafkaConsumer("con", new SimpleStringSchema, properties))
    dataStream.map(new LogMapFunction)
    env.execute("Log message receive")
  }

}

case class LogEntity(userId: Integer, productId: Integer, time: Long, action: String)
//s =>{
//  println(s)
//  val log: util.LogEntity = LogToEntity.getLog(s)
//  if (null != log) {
//  val rowKey = log.userId + "_" + log.productId + "_" + log.time
//  HbaseClient.putData("con", rowKey, "log", "userid", String.valueOf(log.userId))
//  HbaseClient.putData("con", rowKey, "log", "productid", String.valueOf(log.productId))
//  HbaseClient.putData("con", rowKey, "log", "time", log.time.toString)
//  HbaseClient.putData("con", rowKey, "log", "action", log.action)
//}
//}