package com.breezefieldnationalplastic.features.leaderboard.api

/**
 * Created by Puja on 12-04-2024.
 */
object LeaderboardRepoProvider {
    fun provideLeaderboardbranchRepository(): LeaderboardRepo {
        return LeaderboardRepo(LeaderboardApi.createWithoutMultipart())
    }
}