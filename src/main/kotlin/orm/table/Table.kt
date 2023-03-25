package orm.table

import orm.platform.Dialect
import orm.table.column.Column

open class Table(
    name: String?,
) {

    val name: String = name ?: "t_${this::class.simpleName.orEmpty()}"

}