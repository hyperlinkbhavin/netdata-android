package com.netdata.app.utils

import com.netdata.app.data.pojo.request.WarRoomsList

object Constant {
    var COOKIE_SI = ""
    var COOKIE_SV = ""

    var isSortBy = false
    var isFilterBy = false
    var sortByTimeItemPosition = -1
    var sortByNotificationPriorityItemPosition = -1
    var sortByCriticalityItemPosition = -1

    const val APP_PREF_IS_LOGIN = "APP_PREF_IS_LOGIN"
    const val APP_PREF_SPACE_NAME = "APP_PREF_SPACE_NAME"
    const val APP_PREF_SPACE_ID = "APP_PREF_SPACE_ID"
    const val APP_PREF_FROM_NOTIFICATION = "APP_PREF_FROM_NOTIFICATION"
    const val APP_PREF_DAY_NIGHT_MODE = "APP_PREF_DAY_NIGHT_MODE"
    const val APP_PREF_COOKIE_SI = "APP_PREF_COOKIE_SI"
    const val APP_PREF_COOKIE_SV = "APP_PREF_COOKIE_SV"


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
                "name": "10min_dbengine_global_io_errors",
                "role": "sysadmin",
                "when": "2023-07-14T16:21:04+05:30",
                "chart": "netdata.dbengine_global_errors",
                "family": "dbengine io",
                "status": "clear",
                "details": "number of IO errors in the last 10 minutes (CRC errors, out of space, bad disk, etc)",
                "duration": 720000000000,
                "calc_expr": "",
                "conf_file": "19@/usr/lib/netdata/conf.d/health.d/dbengine.conf",
                "edit_line": 19,
                "raised_by": "2021U39",
                "prev_status": "critical",
                "edit_command": "sudo /etc/netdata/edit-config health.d/dbengine.conf",
                "chart_context": "netdata.dbengine_global_errors",
                "transition_id": "38ac5e08-6fb5-48c4-b0a9-07d0f900482f",
                "warning_count": 0,
                "classification": "Errors",
                "critical_count": 0,
                "value_with_units": "0 errors",
                "non_clear_duration": 720000000000
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
            "channel_disabled": null
        },
        "createdAt": "2023-07-27T05:30:31.196000Z"
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
                "name": "disk_space_usage",
                "role": "sysadmin",
                "when": "2023-07-14T17:00:44+05:30",
                "chart": "disk_space._",
                "family": "/",
                "status": "clear",
                "details": "disk / space utilization",
                "duration": 1020000000000,
                "calc_expr": "used * 100 / (avail + used)",
                "conf_file": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
                "edit_line": 12,
                "raised_by": "2021U39",
                "prev_status": "critical",
                "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
                "chart_context": "disk.space",
                "transition_id": "542d7e25-645e-43a0-997e-7547231c6335",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "50.51 %",
                "non_clear_duration": 1320000000000
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
            "channel_disabled": null
        },
        "createdAt": "2023-07-27T09:32:31.196000Z"
    },{
        "data": {
            "node": {
                "id": "ea37ca88-56f4-4075-9f44-a578ec658d7f",
                "hostname": "2022U122",
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
                "email": "mailto:bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "disk_space_usage",
                "role": "sysadmin",
                "when": "2023-07-14T17:34:20+05:30",
                "chart": "disk_space._",
                "family": "/",
                "status": "warning",
                "details": "disk / space utilization",
                "duration": 540000000000,
                "calc_expr": "used * 100 / (avail + used)",
                "conf_file": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
                "edit_line": 12,
                "raised_by": "2022U122",
                "prev_status": "clear",
                "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
                "chart_context": "disk.space",
                "transition_id": "fa67cdd7-2e4d-42da-a529-c3758e199bc2",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "96.76 %",
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
            "channel_disabled": null
        },
        "createdAt": "2023-07-26T12:35:31.196000Z"
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
                "email": "mailto:bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [
                    {
                        "when": "2023-07-14T17:21:37+05:30",
                        "status": "warning",
                        "alarm_name": "ram_in_use"
                    },
                    {
                        "when": "2023-07-14T17:21:37+05:30",
                        "status": "critical",
                        "alarm_name": "used_swap"
                    }
                ],
                "name": "ram_available",
                "role": "sysadmin",
                "when": "2023-07-14T17:21:37+05:30",
                "chart": "mem.available",
                "family": "system",
                "status": "critical",
                "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
                "duration": 0,
                "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
                "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 20,
                "raised_by": "2021U26",
                "prev_status": "STATUS_UNKNOWN",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "mem.available",
                "transition_id": "dc2a7233-d0d1-4af1-a183-bb8278fab24c",
                "warning_count": 1,
                "classification": "Utilization",
                "critical_count": 1,
                "value_with_units": "99 %",
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
            "channel_disabled": null
        },
        "createdAt": "2023-07-26T11:32:31.196000Z"
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
                "email": "mailto:bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [
                    {
                        "when": "2023-07-14T17:21:37+05:30",
                        "status": "critical",
                        "alarm_name": "used_swap"
                    }
                ],
                "name": "ram_in_use",
                "role": "sysadmin",
                "when": "2023-07-14T17:21:37+05:30",
                "chart": "system.ram",
                "family": "ram",
                "status": "warning",
                "details": "system memory utilization",
                "duration": 0,
                "calc_expr": "used * 100 / (used + cached + free + buffers)",
                "conf_file": "4@/usr/lib/netdata/conf.d/health.d/ram.conf",
                "edit_line": 4,
                "raised_by": "2021U26",
                "prev_status": "STATUS_UNKNOWN",
                "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
                "chart_context": "system.ram",
                "transition_id": "a3c0d8fa-6034-46ae-9040-88da40d51abb",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 1,
                "value_with_units": "96.95 %",
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
            "channel_disabled": null
        },
        "createdAt": "2023-07-25T12:32:31.196000Z"
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
                "email": "mailto:bhaveshprajapati.hyperlink@gmail.com",
                "MobileAppToken": "6ef3a0ee-c2e4-4f25-a9cc-1619948f7236"
            },
            "alarm": {
                "log": [],
                "name": "used_swap",
                "role": "sysadmin",
                "when": "2023-07-14T17:21:37+05:30",
                "chart": "system.swap",
                "family": "swap",
                "status": "critical",
                "details": "swap memory utilization",
                "duration": 0,
                "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
                "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
                "edit_line": 21,
                "raised_by": "2021U26",
                "prev_status": "STATUS_UNKNOWN",
                "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
                "chart_context": "system.swap",
                "transition_id": "321b4766-f9e1-41a8-8e4f-3f02aab9cb75",
                "warning_count": 0,
                "classification": "Utilization",
                "critical_count": 0,
                "value_with_units": "100 %",
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
            "channel_disabled": null
        },
        "createdAt": "2023-07-22T12:32:31.196000Z"
    },
    {
    "data": {
      "node": {
        "id": "ea37ca88-56f4-4075-9f44-a578ec658d7f",
        "hostname": "2022U122",
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
          
        ],
        "name": "disk_space_usage",
        "role": "sysadmin",
        "when": "2023-07-14T17:38:20+05:30",
        "chart": "disk_space._",
        "family": "/",
        "status": "clear",
        "details": "disk / space utilization",
        "duration": 240000000000,
        "calc_expr": "used * 100 / (avail + used)",
        "conf_file": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
        "edit_line": 12,
        "raised_by": "2022U122",
        "prev_status": "warning",
        "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
        "chart_context": "disk.space",
        "transition_id": "3ac08735-4b99-4863-894f-36017f5b6bc9",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "46.12 %",
        "non_clear_duration": 240000000000
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-21T12:23:20.516249Z"
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
          
        ],
        "name": "ram_available",
        "role": "sysadmin",
        "when": "2023-07-14T17:25:07+05:30",
        "chart": "mem.available",
        "family": "system",
        "status": "warning",
        "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
        "duration": 210000000000,
        "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
        "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
        "edit_line": 20,
        "raised_by": "2021U26",
        "prev_status": "critical",
        "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
        "chart_context": "mem.available",
        "transition_id": "cbcb4ae5-9c6b-49ea-a65c-63fa97c15c11",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "84.83 %",
        "non_clear_duration": 210000000000
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-21T12:10:07.566794Z"
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
          
        ],
        "name": "ram_in_use",
        "role": "sysadmin",
        "when": "2023-07-14T17:25:07+05:30",
        "chart": "system.ram",
        "family": "ram",
        "status": "critical",
        "details": "system memory utilization",
        "duration": 210000000000,
        "calc_expr": "used * 100 / (used + cached + free + buffers)",
        "conf_file": "4@/usr/lib/netdata/conf.d/health.d/ram.conf",
        "edit_line": 4,
        "raised_by": "2021U26",
        "prev_status": "warning",
        "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
        "chart_context": "system.ram",
        "transition_id": "3a85990d-c469-4d35-a958-c22590de5018",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "12.71 %",
        "non_clear_duration": 210000000000
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-21T12:10:07.533416Z"
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
          
        ],
        "name": "used_swap",
        "role": "sysadmin",
        "when": "2023-07-14T17:25:07+05:30",
        "chart": "system.swap",
        "family": "swap",
        "status": "warning",
        "details": "swap memory utilization",
        "duration": 210000000000,
        "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
        "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
        "edit_line": 21,
        "raised_by": "2021U26",
        "prev_status": "critical",
        "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
        "chart_context": "system.swap",
        "transition_id": "9cfc8558-a863-454d-b539-bd1600db900e",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "40.86 %",
        "non_clear_duration": 210000000000
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-21T12:10:07.503419Z"
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
          
        ],
        "name": "ram_available",
        "role": "sysadmin",
        "when": "2023-07-14T20:30:43+05:30",
        "chart": "mem.available",
        "family": "system",
        "status": "warning",
        "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
        "duration": 970000000000,
        "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
        "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
        "edit_line": 20,
        "raised_by": "2021U26",
        "prev_status": "warning",
        "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
        "chart_context": "mem.available",
        "transition_id": "eb1b2539-336b-4d03-b85a-decc9b494e99",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "27.71 %",
        "non_clear_duration": 970000000000
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-20T15:15:43.512655Z"
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
          
        ],
        "name": "ram_available",
        "role": "sysadmin",
        "when": "2023-07-14T20:14:33+05:30",
        "chart": "mem.available",
        "family": "system",
        "status": "warning",
        "details": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
        "duration": 10120000000000,
        "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
        "conf_file": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
        "edit_line": 20,
        "raised_by": "2021U26",
        "prev_status": "clear",
        "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
        "chart_context": "mem.available",
        "transition_id": "18cd4a12-7cf7-4851-b0e4-b627337889e7",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "9.88 %",
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-20T14:44:35.034823Z"
  },
  {
    "data": {
      "node": {
        "id": "ea37ca88-56f4-4075-9f44-a578ec658d7f",
        "hostname": "2022U122",
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
          
        ],
        "name": "used_swap",
        "role": "sysadmin",
        "when": "2023-07-14T18:26:50+05:30",
        "chart": "system.swap",
        "family": "swap",
        "status": "critical",
        "details": "swap memory utilization",
        "duration": 220000000000,
        "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
        "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
        "edit_line": 21,
        "raised_by": "2022U122",
        "prev_status": "warning",
        "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
        "chart_context": "system.swap",
        "transition_id": "72387e08-148c-45d2-a10f-acddcfc4293a",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "75.68 %",
        "non_clear_duration": 220000000000
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-18T13:11:50.492271Z"
  },
  {
    "data": {
      "node": {
        "id": "ea37ca88-56f4-4075-9f44-a578ec658d7f",
        "hostname": "2022U122",
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
          
        ],
        "name": "used_swap",
        "role": "sysadmin",
        "when": "2023-07-14T18:23:10+05:30",
        "chart": "system.swap",
        "family": "swap",
        "status": "warning",
        "details": "swap memory utilization",
        "duration": 3470000000000,
        "calc_expr": "((used + free) > 0) ? (used * 100 / (used + free)) : 0",
        "conf_file": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
        "edit_line": 21,
        "raised_by": "2022U122",
        "prev_status": "clear",
        "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
        "chart_context": "system.swap",
        "transition_id": "f2a5b444-eade-472b-939f-95bb2d2780a7",
        "warning_count": 0,
        "classification": "Utilization",
        "critical_count": 0,
        "value_with_units": "90.4 %",
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
      "channel_disabled": null
    },
    "createdAt": "2023-07-14T12:53:40.493561Z"
  }
]
"""
}