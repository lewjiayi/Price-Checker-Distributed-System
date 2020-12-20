import java.net.{ServerSocket, Socket}
import java.io.{DataInputStream, DataOutputStream}
import java.sql.ResultSet
import scala.util.control.Breaks._
import scala.util.{Try, Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import Server.server

object Server extends App with data {
    val exist = statement.executeQuery(s"SHOW TABLES LIKE 'items'").next()
    if (exist == false){
        Database.createTable()
        Database.insertIntoTable()
    }

    val serverSocket = new ServerSocket(64000)
    val incomingSocket: Future[Socket] = Future {serverSocket.accept()}

    def processSocket(x: Socket){
        val incomingSocket2: Future[Socket] = Future {serverSocket.accept()}
            incomingSocket2.foreach(x => {
                processSocket(x)
            })
        
        val is = x.getInputStream()
        val out = x.getOutputStream()

        val dis = new DataInputStream(is)
        val dos = new DataOutputStream(out)

        val items = statement.executeQuery(s"SELECT * FROM items")
        var item_list: String = ""
        var item_count: Int = 0
        while(items.next()){
            val id = items.getInt("id")
            val name = items.getString("name")
            item_list += s"$id. $name\n"
            item_count += 1
        }
        dos.writeBytes(item_count.toString() + "\n")
        dos.writeBytes(item_list)

        breakable{
            while(true){
                var line: String = dis.readLine()
                Try(line.toInt) match {
                    case Success(_) =>
                        println(s"Checking price for item $line")
                        val result : ResultSet = statement.executeQuery(s"SELECT unit_price FROM items WHERE id LIKE '%$line%'")
                        val results = Iterator.from(0).takeWhile(_ => result.next()).map(_ => result.getDouble("unit_price")).toList
                        dos.writeBytes(s"${results(0)}\n") 
                    case Failure(_) => break
                }
            }
        }
    }

    incomingSocket.foreach(x => {
        processSocket(x)
    })
    scala.io.StdIn.readLine("Press any key to terminate server")
}