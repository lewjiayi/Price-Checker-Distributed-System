import java.net.Socket
import java.io.{DataInputStream, DataOutputStream}
import scala.util.control.Breaks._
import scala.util.{Try, Success, Failure}

object Client extends App {
    var client = new Socket("localhost", 64000 )
    val is = new DataInputStream(client.getInputStream() )
    val os = new DataOutputStream( client.getOutputStream() )
    var line: String = is.readLine();
    val item_count: Int = line.toInt
    println(s"\nItem catalog\n")
    for (i <- 1 to item_count ){
        line = is.readLine();
        println(s"$line")
    }

    breakable{
        while(true){
            var input: String = scala.io.StdIn.readLine("\nEnter N to end program \nEnter item number to check for unit price: ")
            Try(input.toInt) match {
                case Success(_) => 
                    val id: Int = input.toInt
                    if (id > item_count || id <= 0 ){
                        println("Invalid Input")
                    }
                    else{
                        os.writeBytes(s"${id}\n")
                        line = is.readLine();
                        println(s"Price is RM $line")
                    }
                case Failure(_) => 
                    input = input.toUpperCase()
                    if (input == "N") {
                        os.writeBytes(s"${input}\n")
                        break
                    }
                    else println(s"Invalid input\n") 
            }
        }
    }
    client.close()
}
