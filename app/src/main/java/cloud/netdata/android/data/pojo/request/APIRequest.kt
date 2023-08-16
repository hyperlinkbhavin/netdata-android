package cloud.netdata.android.data.pojo.request

import com.google.gson.annotations.SerializedName

data class APIRequest(
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("redirectURI")
    var redirectURI: String? = null,

    @SerializedName("registerURI")
    var registerURI: String? = null,

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("account_id")
    var accountId: String? = null,

    @SerializedName("integration_ids")
    var integrationIds: ArrayList<String>? = null,

    @SerializedName("starts_at")
    var startsAt: String? = null,

    @SerializedName("silencingRuleID")
    var noRule: ArrayList<String>? = null,
)