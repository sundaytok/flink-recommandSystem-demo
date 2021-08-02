package scala.flink.demo.task

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import scala.flink.demo.client.HbaseClient
import scala.flink.demo.util.{LogToEntity, Property}

object LogTask {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = Property.getKafkaProperties("test")
    val dataStream = env.addSource(new FlinkKafkaConsumer[String]("log", new SimpleStringSchema, properties).setStartFromEarliest())
    dataStream.map(record => {
      val log = LogToEntity.getLog(record)
      val rowKey = log.userId + "_" + log.productId + "_" + log.time
      println(rowKey)
      HbaseClient.putData("con", rowKey, "log", "userId", String.valueOf(log.userId))
      HbaseClient.putData("con", rowKey, "log", "productId", String.valueOf(log.productId))
      HbaseClient.putData("con", rowKey, "log", "time", log.time.toString)
      HbaseClient.putData("con", rowKey, "log", "action", log.action)
    })
    env.execute("Log message receive")
  }
}