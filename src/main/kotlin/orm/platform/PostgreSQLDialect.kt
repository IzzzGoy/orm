package orm.platform

import orm.table.Table

class PostgreSQLDialect: Dialect {
    override fun createTable(table: Table, ifNotExists: Boolean): String {
        return """
            CREATE TABLE ${if(ifNotExists) "IF NOT EXISTS" else ""} ${table.name}(
                
            );
        """.trimIndent()
    }
}