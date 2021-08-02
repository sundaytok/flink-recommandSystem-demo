package scala.flink.demo.task

import javassist.Loader.Simple
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import scala.flink.demo.client.{HbaseClient, MysqlClient}
import scala.flink.demo.util.{Entity, LogToEntity, Property}

/**
 * @author 梁锦洪
 * @email 736801943@qq.com
 * @date 2021/8/2 3:57 下午
 */
object ProductPortraitTask {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = Property.getKafkaProperties("ProductPortrait")
    val dataStream=env.addSource(new FlinkKafkaConsumer[String]("con",new SimpleStringSchema,properties))
    dataStream.map(record=>{
      val log = LogToEntity.getLog(record)
      val resultSet = MysqlClient.selectUserById(log.userId)
      if(resultSet !=null){
        while(resultSet.next){
          val productId = String.valueOf(log.productId)
          val sex = resultSet.getString("sex")
          HbaseClient

        }
      }
      resultSet

    })

  }

}
