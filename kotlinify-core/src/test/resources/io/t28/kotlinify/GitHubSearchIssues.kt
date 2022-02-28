package io.t28.kotlinify

import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public data class GitHubSearchIssues(
    public val totalCount: Int,
    public val incompleteResults: Boolean,
    public val items: List<Items>
)

public data class Items(
    public val url: String,
    public val repositoryUrl: String,
    public val labelsUrl: String,
    public val commentsUrl: String,
    public val eventsUrl: String,
    public val htmlUrl: String,
    public val id: Int,
    public val nodeId: String,
    public val number: Int,
    public val title: String,
    public val user: User,
    public val labels: List<Labels>,
    public val state: String,
    public val assignee: Any?,
    public val milestone: Milestone,
    public val comments: Int,
    public val createdAt: String,
    public val updatedAt: String,
    public val closedAt: Any?,
    public val pullRequest: PullRequest,
    public val body: String,
    public val score: Int,
    public val locked: Boolean,
    public val authorAssociation: String
)

public data class User(
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
    public val siteAdmin: Boolean
)

public data class Labels(
    public val id: Int,
    public val nodeId: String,
    public val url: String,
    public val name: String,
    public val color: String
)

public data class Milestone(
    public val url: String,
    public val htmlUrl: String,
    public val labelsUrl: String,
    public val id: Int,
    public val nodeId: String,
    public val number: Int,
    public val state: String,
    public val title: String,
    public val description: String,
    public val creator: Creator,
    public val openIssues: Int,
    public val closedIssues: Int,
    public val createdAt: String,
    public val updatedAt: String,
    public val closedAt: String,
    public val dueOn: String
)

public data class Creator(
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
    public val siteAdmin: Boolean
)

public data class PullRequest(
    public val url: String,
    public val htmlUrl: String,
    public val diffUrl: String,
    public val patchUrl: String
)
