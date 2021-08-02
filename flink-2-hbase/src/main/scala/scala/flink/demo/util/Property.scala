package scala.flink.demo.util

import java.io.{IOException, InputStream, InputStreamReader}
import java.util.Properties

object Property {
  val CONF_NAME = "config.properties"
  private val in: InputStream = Thread.currentThread.getContextClassLoader.getResourceAsStream(CONF_NAME)
  private val contextProperties = new Properties
  try {
    val inputStreamReader = new InputStreamReader(in)
    contextProperties.load(inputStreamReader)
  } catch {
    case e: Exception => {
      println(">>>flink-2-hbase<<<资源文件加载失败!") // 打印到标准err
      e.printStackTrace // 打印到标准err
    }
      System.out.println(">>>flink-2-hbase<<<资源文件加载成功")
  }

  def getStrValue(key: String): String = {
    contextProperties.getProperty(key)
  }

  def getIntValue(key: String): Unit = {
    val strValue = getStrValue(key)
    Integer.parseInt(strValue)
  }

  def getKafkaProperties(groupId: String): Properties = {
    val properties = new Properties
    properties.setProperty("bootstrap.servers", getStrValue("kafka.bootstrap.servers"))
//    properties.setProperty("zookeeper.connect", getStrValue("kafka.zookeeper.connect"))
    properties.setProperty("group.id", groupId)

    properties
  }
}
