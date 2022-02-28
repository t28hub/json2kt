package io.t28.kotlinify

import kotlin.Boolean
import kotlin.Int
import kotlin.String

public data class GitHubUser(
    public val login: String,
    public val id: Int,
    public val nodeId: String,
    public val avatarUrl: String,
    public val gravatarId: String,
    public val url: String,
    public val htmlUrl: String,
    public val followersUrl: String,
    public val followingUrl: String,
    public val gistsUrl: String,
    public val starredUrl: String,
    public val subscriptionsUrl: String,
    public val organizationsUrl: String,
    public val reposUrl: String,
    public val eventsUrl: String,
    public val receivedEventsUrl: String,
    public val type: String,
    public val siteAdmin: Boolean,
    public val name: String,
    public val company: String,
    public val blog: String,
    public val location: String,
    public val email: String,
    public val hireable: Boolean,
    public val bio: String,
    public val twitterUsername: String,
    public val publicRepos: Int,
    public val publicGists: Int,
    public val followers: Int,
    public val following: Int,
    public val createdAt: String,
    public val updatedAt: String,
    public val plan: Plan
)

public data class Plan(
    public val name: String,
    public val space: Int,
    public val collaborators: Int,
    public val privateRepos: Int
)
