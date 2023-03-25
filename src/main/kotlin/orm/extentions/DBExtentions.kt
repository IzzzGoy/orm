package orm.extentions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import orm.Database
import orm.table.Table
import java.lang.Exception
import java.sql.Connection
import kotlin.coroutines.CoroutineContext

suspend fun Database.createTable(table: Table, exceptionHandler: (Throwable) -> Unit) = transactionScope.launch {
    runCatching {

        transaction {
            prepareStatement(
                dialect.createTable(table).also {
                    logger.info("Create Table: ${table.name}\n SQL: $it")
                }
            ).execute()
        }
    }.onFailure { exception ->
        exceptionHandler(exception)
    }
}

suspend inline fun<reified T: Any> Database.transaction(isReadOnly: Boolean = false, crossinline statement: suspend Connection.() -> T): T {
    return connection.use { conn ->
        conn.isReadOnly = isReadOnly
        statement(conn).also {
            conn.commit()
            conn.isReadOnly = false
        }
    }
}
