package orm.table.column

import orm.platform.Dialect
import java.sql.Types

interface Column<T: Any> {

    val type: Types
    val defaultValue: T?

    val isPrimaryKey: Boolean
    val isUnique: Boolean
    val isIndex: Boolean
    val isNotNull: Boolean

    fun toSQLType(t: T): String

    fun genCreationString(dialect: Dialect): String
}