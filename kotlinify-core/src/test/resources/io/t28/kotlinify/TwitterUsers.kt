package io.t28.kotlinify

import kotlin.String
import kotlin.collections.List

public data class TwitterUsers(
    public val `data`: List<Data>,
    public val includes: Includes
)

public data class Data(
    public val createdAt: String,
    public val username: String,
    public val pinnedTweetId: String,
    public val id: String,
    public val name: String
)

public data class Includes(
    public val tweets: List<Tweets>
)

public data class Tweets(
    public val createdAt: String,
    public val text: String,
    public val id: String
)
