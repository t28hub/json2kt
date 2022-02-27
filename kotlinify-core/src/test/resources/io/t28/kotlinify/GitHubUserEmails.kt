package io.t28.kotlinify

import kotlin.Boolean
import kotlin.String

public data class GitHubUserEmails(
    public val email: String,
    public val primary: Boolean,
    public val verified: Boolean,
    public val visibility: String
)
