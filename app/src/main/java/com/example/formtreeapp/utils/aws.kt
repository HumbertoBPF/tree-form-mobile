package com.example.formtreeapp.utils

import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import aws.sdk.kotlin.services.cognitoidentityprovider.initiateAuth
import aws.sdk.kotlin.services.cognitoidentityprovider.model.AuthFlowType
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InitiateAuthResponse
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InvalidParameterException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.NotAuthorizedException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.UserNotFoundException
import com.example.formtreeapp.BuildConfig

suspend fun loginCognitoUserPools(username: String, password: String, onSuccess: suspend (InitiateAuthResponse) -> Unit, onFailure: () -> Unit) {
    CognitoIdentityProviderClient { region = "us-east-1" }.use { identityProviderClient ->
        try {
            // some code
            val response = identityProviderClient.initiateAuth {
                authFlow = AuthFlowType.fromValue("USER_PASSWORD_AUTH")
                clientId = BuildConfig.CLIENT_ID
                authParameters = mapOf(
                    "USERNAME" to username,
                    "PASSWORD" to password
                )
            }
            onSuccess(response)
        } catch (e: UserNotFoundException) {
            onFailure()
        } catch (e: NotAuthorizedException) {
            onFailure()
        } catch (e: InvalidParameterException) {
            onFailure()
        }
    }
}