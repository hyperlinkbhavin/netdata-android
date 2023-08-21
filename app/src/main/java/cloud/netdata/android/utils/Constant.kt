package cloud.netdata.android.utils

import cloud.netdata.android.data.pojo.request.WarRoomsList

object Constant {
    var COOKIE_SI = ""
    var COOKIE_SV = ""
    var TOKEN = ""
    const val TOKEN_PRE_VALUE = "bearer"
    var dynamicResponseUrl = ""

    var isSortBy = false
//    var notificationPriorityList = ArrayList<NotificationPriorityList>()

    const val APP_PREF_IS_LOGIN = "APP_PREF_IS_LOGIN"
    const val APP_PREF_TOKEN = "APP_PREF_TOKEN"
    const val APP_PREF_SPACE_NAME = "APP_PREF_SPACE_NAME"
    const val APP_PREF_SPACE_ID = "APP_PREF_SPACE_ID"
    const val APP_PREF_FROM_NOTIFICATION = "APP_PREF_FROM_NOTIFICATION"
    const val APP_PREF_DAY_NIGHT_MODE = "APP_PREF_DAY_NIGHT_MODE"
    const val APP_PREF_COOKIE_SI = "APP_PREF_COOKIE_SI"
    const val APP_PREF_COOKIE_SV = "APP_PREF_COOKIE_SV"
    const val APP_PREF_SPACE_LIST_MAINTAIN = "APP_PREF_SPACE_LIST_MAINTAIN"
    const val APP_PREF_TEMP_AUDIO = "APP_PREF_TEMP_AUDIO"
    const val APP_PREF_NOTIFICATION_PRIORITY_LIST = "APP_PREF_NOTIFICATION_PRIORITY_LIST"


    const val BUNDLE_DEEPLINK = "BUNDLE_DEEPLINK"
    const val BUNDLE_URL = "BUNDLE_URL"

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
 [
  {
    "data": {
      "@timestamp": "2023-08-09T11:40:59+05:30",
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "name": [
            "ram_space_usage"
          ],
          "role": [
            "sysadmin"
          ],
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "critical"
            ],
            "value": 99.11388616650773,
            "value_string": "99.113886"
          },
          "previous": {
            "status": "clear",
            "value": 50.8822842344764,
            "value_string": "50.882284"
          },
          "class": "Utilization",
          "type": "System",
          "component": "Ram",
          "transition": {
            "id": "c0839dba-e6cc-42a8-863a-14751324e078"
          },
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url":"https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-1/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart=mem.available\u0026alarm=ram_available\u0026transition=f24ca7e0-6158-4097-99b0-612430d6f04b"
          }
        },
        "chart": {
          "id": "disk_space._",
          "family": "/",
          "name": "disk_space._"
        },
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        },
        "room": [
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "context": {
          "name": [
            "disk.space"
          ]
        }
      },
      "user": {
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink",
        "email": "mailto:bhaveshprajapati.hyperlink@gmail.com"
      }
    },
    "createdAt": "2023-08-09T06:11:58.102616Z"
  },
{
    "data": {
      "@timestamp": "2023-08-08T11:43:59+05:30",
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "name": [
            "network_space_usage"
          ],
          "role": [
            "sysadmin"
          ],
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "clear"
            ],
            "value": 50.886333692767984,
            "value_string": "50.886334"
          },
          "previous": {
            "status": "warning",
            "value": 85.1212,
            "value_string": "85.1212"
          },
          "class": "Utilization",
          "type": "System",
          "component": "Network",
          "transition": {
            "id": "7177f08d-e2e3-47c7-a8c8-3cc567f3dee9"
          },
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url":"https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-1/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart=mem.available\u0026alarm=ram_available\u0026transition=f24ca7e0-6158-4097-99b0-612430d6f04b"
          }
        },
        "chart": {
          "id": "disk_space._",
          "family": "/",
          "name": "disk_space._"
        },
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        },
        "room": [
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          },
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          }
        ],
        "context": {
          "name": [
            "disk.space"
          ]
        }
      },
      "user": {
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink",
        "email": "bhaveshprajapati.hyperlink@gmail.com"
      }
    },
    "createdAt": "2023-08-08T06:28:58.117371Z"
  },
{
    "data": {
      "@timestamp": "2023-08-08T09:43:59+05:30",
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "name": [
            "disk_space_usage"
          ],
          "role": [
            "sysadmin"
          ],
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "clear"
            ],
            "value": 50.886333692767984,
            "value_string": "50.886334"
          },
          "previous": {
            "status": "clear",
            "value": 52.2323,
            "value_string": "52.2323"
          },
          "class": "Utilization",
          "type": "System",
          "component": "Disk",
          "transition": {
            "id": "7177f08d-e2e3-47c7-a8c8-3cc567f3dee9"
          },
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url":"https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-1/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart=mem.available\u0026alarm=ram_available\u0026transition=f24ca7e0-6158-4097-99b0-612430d6f04b"
          }
        },
        "chart": {
          "id": "disk_space._",
          "family": "/",
          "name": "disk_space._"
        },
        "space": {
          "id": "97c1bdee-481b-4743-885c-16c5747da6d2",
          "name": "My space 1"
        },
        "room": [
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          }
        ],
        "context": {
          "name": [
            "disk.space"
          ]
        }
      },
      "user": {
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink",
        "email": "bhaveshprajapati.hyperlink@gmail.com"
      }
    },
    "createdAt": "2023-08-08T04:28:58.117371Z"
  },
{
    "data": {
      "@timestamp": "2023-08-09T11:43:59+05:30",
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "name": [
            "disk_space_usage"
          ],
          "role": [
            "sysadmin"
          ],
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "clear"
            ],
            "value": 50.886333692767984,
            "value_string": "50.886334"
          },
          "previous": {
            "status": "critical",
            "value": 99.11577934805341,
            "value_string": "99.115779"
          },
          "class": "Utilization",
          "type": "System",
          "component": "Disk",
          "transition": {
            "id": "7177f08d-e2e3-47c7-a8c8-3cc567f3dee9"
          },
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url":"https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-1/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart=mem.available\u0026alarm=ram_available\u0026transition=f24ca7e0-6158-4097-99b0-612430d6f04b"
          }
        },
        "chart": {
          "id": "disk_space._",
          "family": "/",
          "name": "disk_space._"
        },
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        },
        "room": [
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "context": {
          "name": [
            "disk.space"
          ]
        }
      },
      "user": {
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink",
        "email": "bhaveshprajapati.hyperlink@gmail.com"
      }
    },
    "createdAt": "2023-08-09T06:28:58.117371Z"
  },
{
    "data": {
      "@timestamp": "2023-08-09T11:43:59+05:30",
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "name": [
            "disk_space_usage"
          ],
          "role": [
            "sysadmin"
          ],
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "clear"
            ],
            "value": 50.886333692767984,
            "value_string": "50.886334"
          },
          "previous": {
            "status": "critical",
            "value": 99.11577934805341,
            "value_string": "99.115779"
          },
          "class": "Utilization",
          "type": "System",
          "component": "Disk",
          "transition": {
            "id": "7177f08d-e2e3-47c7-a8c8-3cc567f3dee9"
          },
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url":"https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-1/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart=mem.available\u0026alarm=ram_available\u0026transition=f24ca7e0-6158-4097-99b0-612430d6f04b"
          }
        },
        "chart": {
          "id": "disk_space._",
          "family": "/",
          "name": "disk_space._"
        },
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        },
        "room": [
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          },
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          }
        ],
        "context": {
          "name": [
            "disk.space"
          ]
        }
      },
      "user": {
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink",
        "email": "bhaveshprajapati.hyperlink@gmail.com"
      }
    },
    "createdAt": "2023-08-09T06:28:58.117371Z"
  }
]
"""
}