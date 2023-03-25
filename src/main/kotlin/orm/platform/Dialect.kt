package orm.platform

import orm.table.Table

interface Dialect {
    fun createTable(table: Table, ifNotExists: Boolean = true): String
}