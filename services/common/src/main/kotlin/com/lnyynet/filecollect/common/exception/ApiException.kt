package com.lnyynet.filecollect.common.exception

class ApiException(
    val errorCode: Int,
    override val message: String
) : RuntimeException(message) 