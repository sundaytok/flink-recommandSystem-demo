package scala.flink.demo.client

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Get, Put, Table}
import org.apache.hadoop.hbase.util.Bytes

import scala.flink.demo.util.Property

object HbaseClient {
  var conn: Connection = _
  var admin: Admin = _

  private val conf: Configuration = HBaseConfiguration.create
  conf.set("hbase.rootdir", Property.getStrValue("hbase.rootdir"))
  conf.set("hbase.zookeeper.quorum", Property.getStrValue("hbase.zookeeper.quorum"))
  conf.set("hbase.client.scanner.timeout.period", Property.getStrValue("hbase.client.scanner.timeout.period"))
  conf.set("hbase.rpc.timeout", Property.getStrValue("hbase.rpc.timeout"))
  try {
    conn = ConnectionFactory.createConnection(conf)
    admin = conn.getAdmin
  } catch {
    case e: Exception => e.printStackTrace()
  }

  def createTable(tableName: String, columnFamilies: String*): Unit = {
    val table_name = TableName.valueOf(tableName)
    if (admin.tableExists(table_name)) System.out.println("Table Exists")
    else {
      System.out.println("Start create table")
      val tableDescriptor = new HTableDescriptor(table_name)
      for (columnFamily <- columnFamilies) {
        val column = tableDescriptor.addFamily(new HColumnDescriptor(columnFamily))
      }
      admin.createTable(tableDescriptor)
      System.out.println("Create Table success")
    }
  }

  /**
   * 向对应列添加数据
   *
   * @param tableName  表名
   * @param rowKey     行号
   * @param familyName 列族名
   * @param column     列名
   * @param data       数据
   * @throws Exception
   */
  @throws[Exception]
  def putData(tableName: String, rowKey: String, familyName: String, column: String, data: String): Unit = {
    val table = conn.getTable(TableName.valueOf(tableName))
    val put = new Put(rowKey.getBytes)
    put.addColumn(familyName.getBytes, column.getBytes, data.getBytes)
    table.put(put)
  }

  def incrementColumn(tableName: String, rowKey: String, familyName: String, column: String): Unit = {
    val str = getData(tableName, rowKey, familyName, column)
    var res = 1;
    if (str != null) {
      str.toInt += res
    }

  }

  def getData(tableName: String, rowKey: String, familyName: String, column: String): String = {
    val table = conn.getTable(TableName.valueOf(tableName))
    val row = Bytes.toBytes(rowKey)
    val get = new Get(row)
    val result = table.get(get)
    val resultValue = result.getValue(familyName.getBytes, column.getBytes)
    if (null == resultValue) {
      return null
    }
    new String(resultValue)
  }

  def putData(tableName: String, rowKey: String, familyName: String, column: String, data: String): Unit = {
    val table = conn.getTable(TableName.valueOf(tableName))
    val put = new Put(rowKey.getBytes)
    put.addColumn(familyName.getBytes, column.getBytes, data.getBytes)
    table.put(put)
  }
}
