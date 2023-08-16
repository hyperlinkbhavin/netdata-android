package cloud.netdata.android.data.pojo.response

import com.google.gson.annotations.SerializedName

data class SpaceList(
    @SerializedName("id"             ) var id             : String?           = null,
    @SerializedName("slug"           ) var slug           : String?           = null,
    @SerializedName("name"           ) var name           : String?           = null,
    @SerializedName("description"    ) var description    : String?           = null,
    @SerializedName("iconURL"        ) var iconURL        : String?           = null,
    @SerializedName("createdAt"      ) var createdAt      : String?           = null,
    @SerializedName("permissions"    ) var permissions    : ArrayList<String> = arrayListOf(),
    @SerializedName("plan"           ) var plan           : String?           = null,
    @SerializedName("planDefinition" ) var planDefinition : PlanDefinition?   = PlanDefinition(),
    var isSelected: Boolean = false,
    var isForever: Boolean = false,
    var isUntil: Boolean = false,
    var untilDate: String? = null,
    var count: Int = 0
){
    data class PlanDefinition (
        @SerializedName("feed"         ) var feed         : Feed?         = Feed(),
        @SerializedName("invitations"  ) var invitations  : Invitations?  = Invitations(),
        @SerializedName("integrations" ) var integrations : Integrations? = Integrations()
    ){
        data class Feed (
            @SerializedName("search_timeframe_seconds" ) var searchTimeframeSeconds : Int? = null
        )

        data class Invitations (
            @SerializedName("max_space_members_with_claimed_nodes"          ) var maxSpaceMembersWithClaimedNodes         : Int? = null,
            @SerializedName("max_space_members_with_no_claimed_nodes"       ) var maxSpaceMembersWithNoClaimedNodes       : Int? = null,
            @SerializedName("max_pending_invitations_with_claimed_nodes"    ) var maxPendingInvitationsWithClaimedNodes   : Int? = null,
            @SerializedName("max_pending_invitations_with_no_claimed_nodes" ) var maxPendingInvitationsWithNoClaimedNodes : Int? = null
        )

        data class Integrations (
            @SerializedName("all"       ) var all       : All?              = All(),
            @SerializedName("available" ) var available : ArrayList<String> = arrayListOf()
        ){
            data class All (
                @SerializedName("Slack"      ) var Slack      : String? = null,
                @SerializedName("Discord"    ) var Discord    : String? = null,
                @SerializedName("Webhook"    ) var Webhook    : String? = null,
                @SerializedName("Opsgenie"   ) var Opsgenie   : String? = null,
                @SerializedName("SendGrid"   ) var SendGrid   : String? = null,
                @SerializedName("PagerDuty"  ) var PagerDuty  : String? = null,
                @SerializedName("Mattermost" ) var Mattermost : String? = null,
                @SerializedName("RocketChat" ) var RocketChat : String? = null
            )
        }
    }
}