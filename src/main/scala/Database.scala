import java.sql.{Connection, DriverManager}

trait data{
  val url = "jdbc:mysql://localhost:3306/mysql"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "root"
  Class.forName(driver)
  var connection: Connection = DriverManager.getConnection(url, username, password)
  val statement = connection.createStatement
}

object Database extends  data{
  def createTable(): Unit = {
    statement.executeUpdate(
      "CREATE TABLE items (id smallint not Null, name VARCHAR(255) not NULL, unit_price decimal(4,2) not NULL, PRIMARY KEY (id))")
  }

  def insertIntoTable (): Unit ={
    statement.executeUpdate("INSERT INTO items VALUES (1 ,'Milo (1kg)', 17.99)")
    statement.executeUpdate("INSERT INTO items VALUES (2 ,'Maggie Kari', 4.55)")
    statement.executeUpdate("INSERT INTO items VALUES (3 ,'Maggi Tomyam', 4.55)")
    statement.executeUpdate("INSERT INTO items VALUES (4 ,'Maggie Asli', 4.10)")
    statement.executeUpdate("INSERT INTO items VALUES (5 ,'Nescafe Kopi Tarik', 2.20)")
    statement.executeUpdate("INSERT INTO items VALUES (6 ,'Nescafe Latte', 2.20)")
    statement.executeUpdate("INSERT INTO items VALUES (7 ,'Nescafe Latte', 2.20)")
    statement.executeUpdate("INSERT INTO items VALUES (8 ,'Gardenia Original Classic', 2.50)")
    statement.executeUpdate("INSERT INTO items VALUES (9 ,'Gardenia Wholemeal', 3.20)")
  }
}