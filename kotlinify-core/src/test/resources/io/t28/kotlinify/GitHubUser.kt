package io.t28.kotlinify

import kotlin.Boolean
import kotlin.Int
import kotlin.String

public data class GitHubUser(
    public val login: String,
    public val id: Int,
    public val node_id: String,
    public val avatar_url: String,
    public val gravatar_id: String,
    public val url: String,
    public val html_url: String,
    public val followers_url: String,
    public val following_url: String,
    public val gists_url: String,
    public val starred_url: String,
    public val subscriptions_url: String,
    public val organizations_url: String,
    public val repos_url: String,
    public val events_url: String,
    public val received_events_url: String,
    public val type: String,
    public val site_admin: Boolean,
    public val name: String,
    public val company: String,
    public val blog: String,
    public val location: String,
    public val email: String,
    public val hireable: Boolean,
    public val bio: String,
    public val twitter_username: String,
    public val public_repos: Int,
    public val public_gists: Int,
    public val followers: Int,
    public val following: Int,
    public val created_at: String,
    public val updated_at: String,
    public val plan: Plan
)

public data class Plan(
    public val name: String,
    public val space: Int,
    public val collaborators: Int,
    public val private_repos: Int
)
