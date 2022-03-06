package io.t28.kotlinify.kotlinx

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GitHubUser(
    public val login: String,
    public val id: Int,
    @SerialName(value = "node_id")
    public val nodeId: String,
    @SerialName(value = "avatar_url")
    public val avatarUrl: String,
    @SerialName(value = "gravatar_id")
    public val gravatarId: String,
    public val url: String,
    @SerialName(value = "html_url")
    public val htmlUrl: String,
    @SerialName(value = "followers_url")
    public val followersUrl: String,
    @SerialName(value = "following_url")
    public val followingUrl: String,
    @SerialName(value = "gists_url")
    public val gistsUrl: String,
    @SerialName(value = "starred_url")
    public val starredUrl: String,
    @SerialName(value = "subscriptions_url")
    public val subscriptionsUrl: String,
    @SerialName(value = "organizations_url")
    public val organizationsUrl: String,
    @SerialName(value = "repos_url")
    public val reposUrl: String,
    @SerialName(value = "events_url")
    public val eventsUrl: String,
    @SerialName(value = "received_events_url")
    public val receivedEventsUrl: String,
    public val type: String,
    @SerialName(value = "site_admin")
    public val siteAdmin: Boolean,
    public val name: String,
    public val company: String,
    public val blog: String,
    public val location: String,
    public val email: String,
    public val hireable: Boolean,
    public val bio: String,
    @SerialName(value = "twitter_username")
    public val twitterUsername: String,
    @SerialName(value = "public_repos")
    public val publicRepos: Int,
    @SerialName(value = "public_gists")
    public val publicGists: Int,
    public val followers: Int,
    public val following: Int,
    @SerialName(value = "created_at")
    public val createdAt: String,
    @SerialName(value = "updated_at")
    public val updatedAt: String,
    public val plan: Plan
)

@Serializable
public data class Plan(
    public val name: String,
    public val space: Int,
    public val collaborators: Int,
    @SerialName(value = "private_repos")
    public val privateRepos: Int
)
