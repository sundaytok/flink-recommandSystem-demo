package scala.flink.demo.util

/**
 * @author 梁锦洪
 * @email 736801943@qq.com
 * @date 2021/8/2 4:02 下午
 */
object LogToEntity {
  def getLog(string: String): Entity = {
    if (null != string) {
      val value = string.split(",")
      val log = Entity(value(0).toInt, value(1).toInt, value(2).toLong, value(3))
      log
    }
  }
}

case class Entity(userId: Int, productId: Int, time: Long, action: String)