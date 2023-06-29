package com.netdata.app.exception

import okio.IOException

class CookiesHandlerError(message: String, val map: HashMap<String, String>) : IOException(message) {

}
