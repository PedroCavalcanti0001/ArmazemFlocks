package me.zkingofkill.armazem.data

import com.google.gson.Gson
import me.zkingofkill.armazem.Main
import me.zkingofkill.armazem.Main.Companion.singleton
import me.zkingofkill.armazem.models.User
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class Mysql {
    private var table: String = singleton.config.getString("mysql.table")
    private var con: Connection? = openCon()

    private fun openCon(): Connection? {
        if (con != null && !con!!.isClosed) return con
        try {
            val password = singleton.config.getString("mysql.password")
            val user = singleton.config.getString("mysql.user")
            val host = singleton.config.getString("mysql.host")
            val port = singleton.config.getInt("mysql.port")
            val database = singleton.config.getString("mysql.database")
            val type = "jdbc:mysql://"
            val url = "$type$host:$port/$database"
            return DriverManager.getConnection(url, user, password)

        } catch (e: Exception) {
            println("  ")
            println("  ")
            println("[ArmazemFlocks] O plugin não se conectou ao mysql por favor verifique sua configuração.")
            println("  ")
            println("  ")
            singleton.pluginLoader.disablePlugin(Main.singleton)
        }

        return null
    }

    fun init() {
        con = openCon()
        val createTable: PreparedStatement
        try {
            createTable =
                con!!.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS $table" +
                            "(`username` VARCHAR(25), `user` LONGTEXT NOT NULL, PRIMARY KEY (`username`));"
                )


            createTable.execute()
            con!!.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }


    fun getUser(playername: String): User {
        val connection = openCon()
        val ps =
            connection!!.prepareStatement("SELECT * FROM $table WHERE username = ?")
        ps?.setString(1, playername)
        val rs = ps.executeQuery()
        return if (rs.next()) Gson().fromJson(rs?.getString("user"), User::class.java) else User(playername)
    }

    fun upsertUser(user: User) {
        try {
            con = openCon()
            val insert =
                con!!.prepareStatement(
                    "INSERT INTO $table(username,user) VALUES (?,?) " +
                            "ON DUPLICATE KEY UPDATE username = ?, user = ?;"
                )
            insert.setString(1, user.player)
            insert.setString(2, Gson().toJson(user))
            insert.setString(3, user.player)
            insert.setString(4, Gson().toJson(user))
            insert.execute()
            con!!.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}