package io.t28.kotlinify.jackson

import com.fasterxml.jackson.`annotation`.JsonIgnoreProperties
import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.Boolean
import kotlin.Int
import kotlin.String

@JsonIgnoreProperties(ignoreUnknown = true)
public data class GitHubUser(
    @JsonProperty
    public val login: String,
    @JsonProperty
    public val id: Int,
    @JsonProperty(value = "node_id")
    public val nodeId: String,
    @JsonProperty(value = "avatar_url")
    public val avatarUrl: String,
    @JsonProperty(value = "gravatar_id")
    public val gravatarId: String,
    @JsonProperty
    public val url: String,
    @JsonProperty(value = "html_url")
    public val htmlUrl: String,
    @JsonProperty(value = "followers_url")
    public val followersUrl: String,
    @JsonProperty(value = "following_url")
    public val followingUrl: String,
    @JsonProperty(value = "gists_url")
    public val gistsUrl: String,
    @JsonProperty(value = "starred_url")
    public val starredUrl: String,
    @JsonProperty(value = "subscriptions_url")
    public val subscriptionsUrl: String,
    @JsonProperty(value = "organizations_url")
    public val organizationsUrl: String,
    @JsonProperty(value = "repos_url")
    public val reposUrl: String,
    @JsonProperty(value = "events_url")
    public val eventsUrl: String,
    @JsonProperty(value = "received_events_url")
    public val receivedEventsUrl: String,
    @JsonProperty
    public val type: String,
    @JsonProperty(value = "site_admin")
    public val siteAdmin: Boolean,
    @JsonProperty
    public val name: String,
    @JsonProperty
    public val company: String,
    @JsonProperty
    public val blog: String,
    @JsonProperty
    public val location: String,
    @JsonProperty
    public val email: String,
    @JsonProperty
    public val hireable: Boolean,
    @JsonProperty
    public val bio: String,
    @JsonProperty(value = "twitter_username")
    public val twitterUsername: String,
    @JsonProperty(value = "public_repos")
    public val publicRepos: Int,
    @JsonProperty(value = "public_gists")
    public val publicGists: Int,
    @JsonProperty
    public val followers: Int,
    @JsonProperty
    public val following: Int,
    @JsonProperty(value = "created_at")
    public val createdAt: String,
    @JsonProperty(value = "updated_at")
    public val updatedAt: String,
    @JsonProperty
    public val plan: Plan
)

@JsonIgnoreProperties(ignoreUnknown = true)
public data class Plan(
    @JsonProperty
    public val name: String,
    @JsonProperty
    public val space: Int,
    @JsonProperty
    public val collaborators: Int,
    @JsonProperty(value = "private_repos")
    public val privateRepos: Int
)
