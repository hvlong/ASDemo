package com.challenge.aspire.data.source.remote.repositorys

import com.challenge.aspire.data.model.Token
import com.challenge.aspire.data.source.local.sharedprf.SharedPrefsApi
import com.challenge.aspire.data.source.local.sharedprf.SharedPrefsKey
import com.challenge.aspire.utils.extension.notNull
import javax.inject.Inject

interface TokenRepository {

    fun getToken(): Token?

    fun saveToken(token: Token)

    fun clearToken()

    fun isHasLogIn(): Boolean

    fun doLogout()

}

class TokenRepositoryImpl
@Inject constructor(private val sharedPrefsApi: SharedPrefsApi) : TokenRepository {

    private var tokenCache: Token? = null

    override fun getToken(): Token? {
        tokenCache.notNull {
            return it
        }

        val token = sharedPrefsApi.get(SharedPrefsKey.KEY_TOKEN, Token::class.java)
        token.notNull {
            tokenCache = it
            return it
        }

        return null
    }

    override fun saveToken(token: Token) {
        tokenCache = token
        sharedPrefsApi.put(SharedPrefsKey.KEY_TOKEN, tokenCache)
    }

    override fun clearToken() {
        tokenCache = null
        sharedPrefsApi.clear()
    }


    override fun isHasLogIn(): Boolean {
        return !getToken()?.accessToken.isNullOrEmpty()
    }

    override fun doLogout() {
        clearToken()
    }
}