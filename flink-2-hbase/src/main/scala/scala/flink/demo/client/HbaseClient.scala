package scala.flink.demo.client

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Put, Table}

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
    case e: Exception => e.printStackTrace
  }

  def createTable(tableName:String,columnFamilies:String*)={
    val tablename = TableName.valueOf(tableName)
    if (admin.tableExists(tablename)) System.out.println("Table Exists")
    else {
      System.out.println("Start create table")
      val tableDescriptor = new HTableDescriptor(tablename)
      for (columnFamliy <- columnFamilies) {
        val column = tableDescriptor.addFamily(new HColumnDescriptor(columnFamliy))
      }
      admin.createTable(tableDescriptor)
      System.out.println("Create Table success")
    }
  }

  /**
   * 向对应列添加数据
   *
   * @param tablename  表名
   * @param rowkey     行号
   * @param famliyname 列族名
   * @param column     列名
   * @param data       数据
   * @throws Exception
   */
  @throws[Exception]
  def putData(tablename: String, rowkey: String, famliyname: String, column: String, data: String): Unit = {
    val table = conn.getTable(TableName.valueOf(tablename))
    val put = new Put(rowkey.getBytes)
    put.addColumn(famliyname.getBytes, column.getBytes, data.getBytes)
    table.put(put)
  }
}
