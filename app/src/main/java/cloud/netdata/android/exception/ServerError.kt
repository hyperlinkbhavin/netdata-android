package cloud.netdata.android.exception

import okio.IOException

class ServerError : IOException {

    val errorBody: String


    constructor(message: String) : super(message) {
        errorBody = ""
    }

    constructor(message: String, errorBody: String) : super(message) {
        this.errorBody = errorBody
    }
}
