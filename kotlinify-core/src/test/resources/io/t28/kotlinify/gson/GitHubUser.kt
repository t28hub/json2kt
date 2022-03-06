package io.t28.kotlinify.gson

import com.google.gson.annotations.SerializedName
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public data class GitHubUser(
    public val login: String,
    public val id: Int,
    @SerializedName(value = "node_id")
    public val nodeId: String,
    @SerializedName(value = "avatar_url")
    public val avatarUrl: String,
    @SerializedName(value = "gravatar_id")
    public val gravatarId: String,
    public val url: String,
    @SerializedName(value = "html_url")
    public val htmlUrl: String,
    @SerializedName(value = "followers_url")
    public val followersUrl: String,
    @SerializedName(value = "following_url")
    public val followingUrl: String,
    @SerializedName(value = "gists_url")
    public val gistsUrl: String,
    @SerializedName(value = "starred_url")
    public val starredUrl: String,
    @SerializedName(value = "subscriptions_url")
    public val subscriptionsUrl: String,
    @SerializedName(value = "organizations_url")
    public val organizationsUrl: String,
    @SerializedName(value = "repos_url")
    public val reposUrl: String,
    @SerializedName(value = "events_url")
    public val eventsUrl: String,
    @SerializedName(value = "received_events_url")
    public val receivedEventsUrl: String,
    public val type: String,
    @SerializedName(value = "site_admin")
    public val siteAdmin: Boolean,
    public val name: String,
    public val company: String,
    public val blog: String,
    public val location: String,
    public val email: String,
    public val hireable: Boolean,
    public val bio: String,
    @SerializedName(value = "twitter_username")
    public val twitterUsername: String,
    @SerializedName(value = "public_repos")
    public val publicRepos: Int,
    @SerializedName(value = "public_gists")
    public val publicGists: Int,
    public val followers: Int,
    public val following: Int,
    @SerializedName(value = "created_at")
    public val createdAt: String,
    @SerializedName(value = "updated_at")
    public val updatedAt: String,
    public val plan: Plan
)

public data class Plan(
    public val name: String,
    public val space: Int,
    public val collaborators: Int,
    @SerializedName(value = "private_repos")
    public val privateRepos: Int
)
