package com.plastickarma.githubapikt.http

import com.github.kittinunf.fuel.core.FoldableRequestInterceptor
import com.github.kittinunf.fuel.core.RequestTransformer
import java.net.URL

/**
 * GitHub API does not work well with encodes '+' in search query. This encoder
 * replaces the encoded characters back to it's original value.
 */
object SearchQueryEncoder : FoldableRequestInterceptor {
    override fun invoke(next: RequestTransformer): RequestTransformer {
        return { request ->
            next(
                request.apply {
                    url = URL(url.toExternalForm().replace("%2B", "+"))
                }
            )
        }
    }
}
