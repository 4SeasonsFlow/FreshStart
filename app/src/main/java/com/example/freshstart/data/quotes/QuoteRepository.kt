package com.example.freshstart.data.quotes

interface QuoteRepository {
    suspend fun getRandomQuote(): List<FreshQuote>
}

class NetworkQuoteRepository(
    private val quoteApiService: QuoteApiService,
) : QuoteRepository {
    override suspend fun getRandomQuote(): List<FreshQuote> = quoteApiService.getRandomQuote()
}
