package io.t28.kotlinify

import kotlin.Int
import kotlin.String
import kotlin.collections.List

public data class TwitterTweetsSearch(
    public val `data`: List<Data>,
    public val includes: Includes,
    public val meta: Meta
)

public data class Data(
    public val text: String,
    public val authorId: String,
    public val id: String,
    public val lang: String,
    public val conversationId: String,
    public val createdAt: String
)

public data class Includes(
    public val users: List<Users>
)

public data class Users(
    public val id: String,
    public val entities: Entities,
    public val createdAt: String,
    public val username: String,
    public val name: String
)

public data class Entities(
    public val url: Url,
    public val description: Description
)

public data class Url(
    public val urls: List<Urls>
)

public data class Urls(
    public val start: Int,
    public val end: Int,
    public val url: String,
    public val expandedUrl: String,
    public val displayUrl: String
)

public data class Description(
    public val hashtags: List<Hashtags>
)

public data class Hashtags(
    public val start: Int,
    public val end: Int,
    public val tag: String
)

public data class Meta(
    public val newestId: String,
    public val oldestId: String,
    public val resultCount: Int,
    public val nextToken: String
)
