package com.plastickarma.githubapikt.base.auth

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.RequestTransformer

object Noop : RequestTransformer {
    override fun invoke(request: Request) = request
}
