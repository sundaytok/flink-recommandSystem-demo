package scala.flink.demo.util

import com.demo.domain.LogEntity

object LogToEntity {
  def getLog(s: String): LogEntity = {
    println(s)
    val values = s.split(",")
    if (values.length < 2) {
      println("Message is not correct")
      null
    }
    val log = LogEntity(Integer.parseInt(values(0)), Integer.parseInt(values(1)), (values(2).toLong), values(3))
    log
  }
}
case class LogEntity(userId: Integer, productId: Integer, time: Long, action: String)

