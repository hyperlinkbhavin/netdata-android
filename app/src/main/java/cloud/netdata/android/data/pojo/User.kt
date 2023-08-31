package cloud.netdata.android.data.pojo

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id"            ) var id            : String?           = null,
    @SerializedName("email"         ) var email         : String?           = null,
    @SerializedName("name"          ) var name          : String?           = null,
    @SerializedName("avatarURL"     ) var avatarURL     : String?           = null,
    @SerializedName("createdAt"     ) var createdAt     : String?           = null,
    @SerializedName("settings"      ) var settings      : Settings?         = Settings(),
    @SerializedName("termsAccepted" ) var termsAccepted : Boolean?          = null,
    @SerializedName("auth_type"     ) var authType      : String?           = null,
    @SerializedName("permissions"   ) var permissions   : ArrayList<String> = arrayListOf()
) {

    data class Settings (
        @SerializedName("alertsGrouping"      ) var alertsGrouping      : String?  = null,
        @SerializedName("isLoaded"            ) var isLoaded            : Boolean? = null,
        @SerializedName("spacePanelCollapsed" ) var spacePanelCollapsed : Boolean? = null,
        @SerializedName("theme"               ) var theme               : String?  = null

    )
}