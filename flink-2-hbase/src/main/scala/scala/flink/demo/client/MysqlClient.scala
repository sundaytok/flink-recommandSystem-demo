package scala.flink.demo.client

import java.sql.{DriverManager, ResultSet, Statement}
import scala.flink.demo.util.Property

/**
 * @author 梁锦洪
 * @email 736801943@qq.com
 * @date 2021/8/2 4:09 下午
 */
object MysqlClient {
  val URL: String = Property.getStrValue("mysql.url")
  val NAME: String = Property.getStrValue("mysql.name")
  val PASS: String = Property.getStrValue("mysql.pass")
  var stmt: Statement = _

  try {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection(URL, NAME, PASS)
    stmt = conn.createStatement
  } catch {
    case e: Exception => e.printStackTrace()
  }

  def selectById(id: Int): ResultSet = {
    val sql = String.format("select  * from product where product_id = %s", id.toString)
    val resultSet = stmt.executeQuery(sql)
    resultSet
  }

  def selectUserById(id: Int): ResultSet = {
    val sql = String.format("select  * from user where user_id = %s", id.toString)
    val resultSet = stmt.executeQuery(sql)
    resultSet
  }

  def main(args: Array[String]): Unit = {
    val resultSet = MysqlClient.selectById(1)
    while (resultSet.next) {
      println(resultSet.getString(2))
    }
  }

}
