package com.netdata.app.utils

import com.netdata.app.data.pojo.request.FilterList
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.data.pojo.response.NotificationPriorityList

object Constant {
    var COOKIE_SI = ""
    var COOKIE_SV = ""

    var isSortBy = false
    var notificationPriorityList = ArrayList<NotificationPriorityList>()

    const val APP_PREF_IS_LOGIN = "APP_PREF_IS_LOGIN"
    const val APP_PREF_SPACE_NAME = "APP_PREF_SPACE_NAME"
    const val APP_PREF_SPACE_ID = "APP_PREF_SPACE_ID"
    const val APP_PREF_FROM_NOTIFICATION = "APP_PREF_FROM_NOTIFICATION"
    const val APP_PREF_DAY_NIGHT_MODE = "APP_PREF_DAY_NIGHT_MODE"
    const val APP_PREF_COOKIE_SI = "APP_PREF_COOKIE_SI"
    const val APP_PREF_COOKIE_SV = "APP_PREF_COOKIE_SV"
    const val APP_PREF_SPACE_LIST_MAINTAIN = "APP_PREF_SPACE_LIST_MAINTAIN"
    const val APP_PREF_TEMP_AUDIO = "APP_PREF_TEMP_AUDIO"


    const val BUNDLE_DEEPLINK = "BUNDLE_DEEPLINK"

    const val PUSH_NOTIFICATION = "PUSH_NOTIFICATION"
    const val NOTIFICATION_ICON = "NOTIFICATION_ICON"
    const val CONVERSATION_TITLE = "CONVERSATION_TITLE"

    val sortByTimeList = ArrayList<WarRoomsList>()
    val sortByNotificationPriorityList = ArrayList<WarRoomsList>()
    val sortByCriticalityList = ArrayList<WarRoomsList>()

    fun addData() {
        sortByTimeList.clear()
        sortByNotificationPriorityList.clear()
        sortByCriticalityList.clear()
        sortByTimeList.add(WarRoomsList("New on top"))
        sortByTimeList.add(WarRoomsList("Old on top"))

        sortByNotificationPriorityList.add(WarRoomsList("High to Low"))
        sortByNotificationPriorityList.add(WarRoomsList("Low to High"))

        sortByCriticalityList.add(WarRoomsList("Critical to Clear"))
        sortByCriticalityList.add(WarRoomsList("Clear to Critical"))
    }


    const val dummyData = """
 [   {
        "data": {
            "node": {
                "id": "ea37ca88-56f4-4075-9f44-a578ec658d7f",
                "hostname": "shivam-raval",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "used_swap",
                "role": "sysadmin",
                "when": "2023-07-20T16:57:38+05:30",
                "chart": "system.swap",
                "family": "swap",
                "status": "warning",
                "details": "swap memory utilization",
                "duration": 9090000000000,
                "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
                "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
                "edit_line": 21,
                "raised_by": "shivam-raval",
                "prev_status": "clear",
                "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
                "chart_context": "system.swap",
                "transition_id": "ee7026e8-6a06-4524-8440-740c921bf1d1",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "92.02 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                },    {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca4d",
                    "name": "test room 3",
                    "slug": "test-room-3"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-08-01T11:28:08.50188Z"
    },{
        "data": {
            "node": {
                "id": "ea37ca88-56f4-4075-9f44-a578ec658d7f",
                "hostname": "shivam-raval",
                "reachable": false
            },
            "room": {
                "id": "fd09bea5-664b-40a1-ac8a-10cc102f4b66",
               "name": "test room 1",
                "slug": "test-room-1"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "used_swap",
                "role": "sysadmin",
                "when": "2023-07-20T16:57:38+05:30",
                "chart": "system.swap",
                "family": "swap",
                "status": "warning",
                "details": "swap memory utilization",
                "duration": 9090000000000,
                "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
                "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
                "edit_line": 21,
                "raised_by": "shivam-raval",
                "prev_status": "clear",
                "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
                "chart_context": "system.swap",
                "transition_id": "ee7026e8-6a06-4524-8440-740c921bf1d1",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "92.02 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                },    {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca4d",
                    "name": "test room 3",
                    "slug": "test-room-3"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-08-01T11:28:08.50188Z"
    },
    {
        "data": {
            "node": {
                "id": "d08b47a9-bb74-411e-9392-df54c6f5b3ed",
                "hostname": "2021U26",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [
                    {
                        "when": "2023-07-20T16:40:19+05:30",
                        "status": "warning",
                        "alarm_name": "ram_available"
                    },
                    {
                        "when": "2023-07-20T16:20:09+05:30",
                        "status": "critical",
                        "alarm_name": "used_swap"
                    }
                ],
                "name": "ram_in_use",
                "role": "sysadmin",
                "when": "2023-07-20T16:40:49+05:30",
                "chart": "system.ram",
                "family": "ram",
                "status": "warning",
                "details": "system memory utilization",
                "duration": 19150000000000,
                "calc_expr": "used * 100 / (used + cached + free + buffers)",
                "conf_file": "4@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 4,
                "raised_by": "2021U26",
                "prev_status": "clear",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "system.ram",
                "transition_id": "d2cf124e-7c16-4f8f-ba1f-9a4a9b8a3489",
                "warning_count": 1,
                "classification": "Utilization",
                "critical_count": 1,
                "value_with_units": "90.28 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-08-02T11:10:49.919713Z"
    },
    {
        "data": {
            "node": {
                "id": "d08b47a9-bb74-411e-9392-df54c6f5b3ed",
                "hostname": "2021U26",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [
                    {
                        "when": "2023-07-29T16:20:09+05:30",
                        "status": "critical",
                        "alarm_name": "used_swap"
                    }
                ],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-20T16:40:19+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "warning",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 19120000000000,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U26",
                "prev_status": "clear",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "71ecd3d8-df5c-4fa1-9ae6-91af5d5a391e",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 1,
                "value_with_units": "9.13 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-20T11:10:20.953019Z"
    },
    {
        "data": {
            "node": {
                "id": "ff064169-c563-4b8c-9a06-48aafe986c43",
                "hostname": "2021U30",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "used_swap",
                "role": "sysadmin",
                "when": "2023-07-20T16:26:37+05:30",
                "chart": "system.swap",
                "family": "swap",
                "status": "clear",
                "details": "swap memory utilization",
                "duration": 0,
                "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
                "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
                "edit_line": 21,
                "raised_by": "2021U30",
                "prev_status": "critical",
                "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
                "chart_context": "system.swap",
                "transition_id": "777be8c2-d4b6-40cf-aaf3-b115995880d2",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "0 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-20T11:01:38.116631Z"
    },
    {
        "data": {
            "node": {
                "id": "ff064169-c563-4b8c-9a06-48aafe986c43",
                "hostname": "2021U30",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-20T16:26:37+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "clear",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 0,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U30",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "1ea7a905-236d-4531-987d-4c317c64e128",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "82.95 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-20T11:01:38.107206Z"
    },
    {
        "data": {
            "node": {
                "id": "ff064169-c563-4b8c-9a06-48aafe986c43",
                "hostname": "2021U30",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [
                    {
                        "when": "2023-07-20T15:51:31+05:30",
                        "status": "warning",
                        "alarm_name": "ram_available"
                    }
                ],
                "name": "ram_in_use",
                "role": "sysadmin",
                "when": "2023-07-20T16:26:37+05:30",
                "chart": "system.ram",
                "family": "ram",
                "status": "clear",
                "details": "system memory utilization",
                "duration": 0,
                "calc_expr": "used * 100 / (used + cached + free + buffers)",
                "conf_file": "4@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 4,
                "raised_by": "2021U30",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "system.ram",
                "transition_id": "514afe89-25f5-4835-8a72-1f72d613269e",
                "warning_count": 1,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "14.79 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-20T11:01:38.09433Z"
    },
    {
        "data": {
            "node": {
                "id": "d08b47a9-bb74-411e-9392-df54c6f5b3ed",
                "hostname": "2021U26",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "used_swap",
                "role": "sysadmin",
                "when": "2023-07-20T16:20:09+05:30",
                "chart": "system.swap",
                "family": "swap",
                "status": "critical",
                "details": "swap memory utilization",
                "duration": 160000000000,
                "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
                "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
                "edit_line": 21,
                "raised_by": "2021U26",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
                "chart_context": "system.swap",
                "transition_id": "3dce451e-d423-47f7-8350-ece2508c48ec",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "98.02 %",
                "non_clear_duration": 160000000000
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-20T10:50:39.496405Z"
    },
    {
        "data": {
            "node": {
                "id": "d08b47a9-bb74-411e-9392-df54c6f5b3ed",
                "hostname": "2021U26",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "used_swap",
                "role": "sysadmin",
                "when": "2023-07-20T16:17:29+05:30",
                "chart": "system.swap",
                "family": "swap",
                "status": "warning",
                "details": "swap memory utilization",
                "duration": 17750000000000,
                "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
                "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
                "edit_line": 21,
                "raised_by": "2021U26",
                "prev_status": "clear",
                "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
                "chart_context": "system.swap",
                "transition_id": "24bbf022-d526-4f18-bf87-89da1410146a",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "91.4 %",
                "non_clear_duration": 0
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-20T10:47:59.521796Z"
    },
    {
        "data": {
            "node": {
                "id": "1f4ac1ba-69ea-4a35-86c6-80cf872ce29e",
                "hostname": "2021U39",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-20T15:44:00+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "clear",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 5320000000000,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U39",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "ab3cd395-c53f-4773-ab60-11f9ee7aaa9c",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "70.42 %",
                "non_clear_duration": 5320000000000
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-20T10:29:00.495216Z"
    },{
        "data": {
            "node": {
                "id": "1f4ac1ba-69ea-4a35-86c6-80cf872ce29e",
                "hostname": "2021U39",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-20T15:44:00+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "critical",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 5320000000000,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U39",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "ab3cd395-c53f-4773-ab60-11f9ee7aaa9c",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "70.42 %",
                "non_clear_duration": 5320000000000
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-27T10:29:00.495216Z"
    },{
        "data": {
            "node": {
                "id": "1f4ac1ba-69ea-4a35-86c6-80cf872ce29e",
                "hostname": "2021U39",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-20T15:44:00+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "clear",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 5320000000000,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U39",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "ab3cd395-c53f-4773-ab60-11f9ee7aaa9c",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "70.42 %",
                "non_clear_duration": 5320000000000
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-27T10:29:00.495216Z"
    },{
        "data": {
            "node": {
                "id": "1f4ac1ba-69ea-4a35-86c6-80cf872ce29e",
                "hostname": "2021U39",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-20T15:44:00+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "clear",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 5320000000000,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U39",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "ab3cd395-c53f-4773-ab60-11f9ee7aaa9c",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "70.42 %",
                "non_clear_duration": 5320000000000
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-05-27T10:29:00.495216Z"
    },{
        "data": {
            "node": {
                "id": "1f4ac1ba-69ea-4a35-86c6-80cf872ce29e",
                "hostname": "2021U39",
                "reachable": false
            },
            "room": {
                "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                "name": "All nodes",
                "slug": "all-nodes"
            },
            "user": {
                "id": "f51767d6-5c03-4783-991c-9403ea9df4ac",
                "name": "bhaveshprajapati.hyperlink",
                "email": "bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-20T15:44:00+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "clear",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 5320000000000,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U39",
                "prev_status": "warning",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "ab3cd395-c53f-4773-ab60-11f9ee7aaa9c",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "70.42 %",
                "non_clear_duration": 5320000000000
            },
            "rooms": [
                {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "All nodes",
                    "slug": "all-nodes"
                },  {
                    "id": "29ffc3cb-174a-430e-9f55-2d3c4c0fca3d",
                    "name": "Test Room 1",
                    "slug": "Test-Room-1"
                }
            ],
            "space": {
                "id": "8a571b80-fd97-4ded-a166-9aacc2e84acb",
                "name": "Bhaveshprajapa space",
                "slug": "bhaveshprajapa-space"
            },
            "issuer": null,
            "rate_limit": {
                "window": 0,
                "threshold": 0
            },
            "passwordless": null,
            "channel_disabled": null,
            "file_attachments": null,
            "cloud_install_summary": null
        },
        "createdAt": "2023-07-28T10:29:00.495216Z"
    }
]
"""
}