package com.hedmer.anibreak.common.error


class InternetConnectivityException: Exception()

class UnknownException: Exception()

class MissingDataException: Exception()

class ResponseError(val errorMessage: String): Exception()