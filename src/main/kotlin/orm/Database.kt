package orm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import orm.platform.Dialect
import java.sql.Connection
import java.sql.DriverManager
import java.util.logging.Logger

class Database private constructor(
    val transactionScope: CoroutineScope,
    val connection: Connection,
    val dialect: Dialect,
    val logger: Logger = Logger.getLogger("DATABASE")
) {

    init {
        Runtime.getRuntime().addShutdownHook(
            Thread {
                connection.close()
            }
        )
    }

    companion object {
        lateinit var defaultDB: Database

        fun connect(
            url: String,
            user: String,
            password: String,
            dbConfig: DatabaseConfig,
            transactionScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ): Result<Database> {
            return runCatching {
                Class.forName(dbConfig.driver)
                Database(
                    connection = DriverManager.getConnection(url, user, password).also { it.autoCommit = false },
                    dialect = dbConfig.dialect,
                    transactionScope = transactionScope
                ).also {
                    defaultDB = it
                }
            }
        }
    }
}


interface DatabaseConfig {
    val driver: String
    val dialect: Dialect
}