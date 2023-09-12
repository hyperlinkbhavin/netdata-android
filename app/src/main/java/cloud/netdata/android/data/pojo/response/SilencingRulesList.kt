package cloud.netdata.android.data.pojo.response

import com.google.gson.annotations.SerializedName

data class SilencingRulesList(
    @SerializedName("id"              ) var id             : String?           = null,
    @SerializedName("name"            ) var name           : String?           = null,
    @SerializedName("account_id"      ) var accountId      : String?           = null,
    @SerializedName("integration_ids" ) var integrationIds : ArrayList<String> = arrayListOf(),
    @SerializedName("starts_at"       ) var startsAt       : String?           = null,
    @SerializedName("disabled"        ) var disabled       : Boolean?          = null,
    @SerializedName("lasts_until"     ) var lastsUntil     : String?          = null,
    @SerializedName("read_only"       ) var readOnly       : Boolean?          = null,
    @SerializedName("state"           ) var state          : String?           = null
)
