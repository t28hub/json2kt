package io.t28.kotlinify

import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public data class DuplicatedKeyObject(
    public val `key$$$$$$`: Boolean,
    public val `key$$$$$`: Int,
    public val `key$$$$`: String,
    public val `key$$$`: Any?,
    public val `key$$`: List<Any?>?,
    public val `key$`: `Key$$`,
    public val key: Key
)

public data class `Key$$`(
    public val key: Any?
)

public data class Key(
    public val key: Any?
)
