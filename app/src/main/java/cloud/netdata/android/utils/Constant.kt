package cloud.netdata.android.utils

import cloud.netdata.android.data.pojo.request.WarRoomsList

object Constant {
    var COOKIE_SI = ""
    var COOKIE_SV = ""
    var TOKEN = ""
    var dynamicResponseUrl = ""
    var isDarkMode = false

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
    const val APP_PREF_SORTING_BY_TIME = "APP_PREF_SORTING_BY_TIME"
    const val APP_PREF_SORTING_BY_PRIORITY = "APP_PREF_SORTING_BY_PRIORITY"
    const val APP_PREF_SORTING_BY_CRITICALITY = "APP_PREF_SORTING_BY_CRITICALITY"


    const val BUNDLE_DEEPLINK = "BUNDLE_DEEPLINK"
    const val BUNDLE_URL = "BUNDLE_URL"

    const val PUSH_NOTIFICATION = "PUSH_NOTIFICATION"
    const val NOTIFICATION_ICON = "NOTIFICATION_ICON"
    const val CONVERSATION_TITLE = "CONVERSATION_TITLE"
    const val MY_NOTIFICATION_ACTION = "MY_NOTIFICATION_ACTION"
    var MY_NOTIFICATION_MESSAGE = "Space"

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
    "createdAt": "2023-08-24T07:09:00.506753Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003d58a9bc46-7e99-4bcf-a361-6cb94e10845e"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "critical"
            ],
            "value": 98.97937579989586
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "warning",
            "value": 97.44567383894699
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "58a9bc46-7e99-4bcf-a361-6cb94e10845e"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 23,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-24T07:08:27.164364Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003de7cfe7f8-6b9b-4fa0-9b10-8bb2780acc80"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "warning"
            ],
            "value": 91.20386353275973
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "clear",
            "value": 89.90304928407532
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "e7cfe7f8-6b9b-4fa0-9b10-8bb2780acc80"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 24,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-24T06:55:30.197724Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003d41a86d00-7cb9-4998-858e-f0ea85841b9f"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "critical"
            ],
            "value": 98.17599902343564
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "warning",
            "value": 97.95665351229384
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "41a86d00-7cb9-4998-858e-f0ea85841b9f"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 22,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-24T06:49:00.173972Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003d3c131b1e-3b11-457d-85cf-ae9e311d5960"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "warning"
            ],
            "value": 91.2542176327088
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "clear",
            "value": 87.98158260647317
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "3c131b1e-3b11-457d-85cf-ae9e311d5960"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 21,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T14:15:09.102229Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003d8415905e-4cdc-442b-8add-9f9d4601dd09"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "clear"
            ],
            "value": 79.96726983503311
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "warning",
            "value": 80.05271921676486
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "8415905e-4cdc-442b-8add-9f9d4601dd09"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 16,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T14:01:19.132436Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "",
            "conf_source": "35@/usr/lib/netdata/conf.d/health.d/ram.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
            "edit_line": "35",
            "info": "number of out of memory kills in the last 30 minutes",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/mem.oom_kill.oom_kill:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dmem.oom_kill\u0026alarm\u003doom_kill\u0026transition\u003d58330fb5-13bd-4ec5-886d-5bfb717216c7"
          },
          "config_hash": "ec009ef7-23c5-0fc5-8d28-79937977a4df",
          "current": {
            "status": [
              "clear"
            ],
            "value": 0.0
          },
          "name": [
            "oom_kill"
          ],
          "previous": {
            "status": "warning",
            "value": 5.0
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "58330fb5-13bd-4ec5-886d-5bfb717216c7"
          }
        },
        "chart": {
          "id": "mem.oom_kill",
          "name": "mem.oom_kill"
        },
        "context": {
          "name": [
            "mem.oom_kill"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 17,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T13:51:11.720613Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
            "conf_source": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
            "edit_command": "sudo  /etc/netdata/edit-config health.d/ram.conf",
            "edit_line": "20",
            "info": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dmem.available\u0026alarm\u003dram_available\u0026transition\u003ddb70eaa5-fbc5-40c5-97f9-359b815dbcfa"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "478f8e15-d04e-1e88-102a-159e3cab5974",
          "current": {
            "status": [
              "warning"
            ],
            "value": 8.124783992360571
          },
          "name": [
            "ram_available"
          ],
          "previous": {
            "status": "clear",
            "value": 22.22026885834094
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "db70eaa5-fbc5-40c5-97f9-359b815dbcfa"
          },
          "type": "System"
        },
        "chart": {
          "id": "mem.available",
          "name": "mem.available"
        },
        "context": {
          "name": [
            "mem.available"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 18,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T13:35:19.087475Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
            "conf_source": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
            "edit_line": "20",
            "info": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dmem.available\u0026alarm\u003dram_available\u0026transition\u003d0bc52e43-6a7f-47e2-af08-47cbc13e2032"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "478f8e15-d04e-1e88-102a-159e3cab5974",
          "current": {
            "status": [
              "clear"
            ],
            "value": 80.08817440165468
          },
          "name": [
            "ram_available"
          ],
          "previous": {
            "status": "warning",
            "value": 1.3581095344356964
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "0bc52e43-6a7f-47e2-af08-47cbc13e2032"
          },
          "type": "System"
        },
        "chart": {
          "id": "mem.available",
          "name": "mem.available"
        },
        "context": {
          "name": [
            "mem.available"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 19,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T13:35:19.048298Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "used * 100 / (used + cached + free + buffers)",
            "conf_source": "4@/usr/lib/netdata/conf.d/health.d/ram.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
            "edit_line": "4",
            "info": "system memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.ram.ram_in_use:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.ram\u0026alarm\u003dram_in_use\u0026transition\u003d10d4e53b-4d04-4c71-b256-1a89f9592c54"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "0625fad1-394d-0474-6a2c-a18c6838c6df",
          "current": {
            "status": [
              "clear"
            ],
            "value": 17.230400823609244
          },
          "name": [
            "ram_in_use"
          ],
          "previous": {
            "status": "warning",
            "value": 95.99412792180507
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "10d4e53b-4d04-4c71-b256-1a89f9592c54"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.ram",
          "name": "system.ram"
        },
        "context": {
          "name": [
            "system.ram"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 20,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T13:15:20.851813Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "avail * 100 / (system.ram.used + system.ram.cached + system.ram.free + system.ram.buffers)",
            "conf_source": "20@/usr/lib/netdata/conf.d/health.d/ram.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
            "edit_line": "20",
            "info": "percentage of estimated amount of RAM available for userspace processes, without causing swapping",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/mem.available.ram_available:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dmem.available\u0026alarm\u003dram_available\u0026transition\u003dcea35cbc-c1aa-47ac-a9d3-dbad83601944"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "478f8e15-d04e-1e88-102a-159e3cab5974",
          "current": {
            "status": [
              "warning"
            ],
            "value": 8.470629287273013
          },
          "name": [
            "ram_available"
          ],
          "previous": {
            "status": "clear",
            "value": 11.003221052645598
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "cea35cbc-c1aa-47ac-a9d3-dbad83601944"
          },
          "type": "System"
        },
        "chart": {
          "id": "mem.available",
          "name": "mem.available"
        },
        "context": {
          "name": [
            "mem.available"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 14,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T11:12:11.209553Z",
    "data": {
      "host": [
        {
          "id": "993ca47b-8456-4028-87a2-2aba81175287",
          "isSelected": false,
          "name": "shivam-raval"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/mem.swap.used_swap:::993ca47b-8456-4028-87a2-2aba81175287?chart\u003dmem.swap\u0026alarm\u003dused_swap\u0026transition\u003d08fdb3d5-2659-4fa3-b672-1c7a0fab013e"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "2074c546-02b0-b787-763d-dc55f98a88f4",
          "current": {
            "status": [
              "critical"
            ],
            "value": 100.0
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "warning",
            "value": 91.003782279553
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "08fdb3d5-2659-4fa3-b672-1c7a0fab013e"
          },
          "type": "System"
        },
        "chart": {
          "id": "mem.swap",
          "name": "mem.swap"
        },
        "context": {
          "name": [
            "mem.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 15,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T09:23:05.105522Z",
    "data": {
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "isSelected": false,
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "used * 100 / (used + cached + free + buffers)",
            "conf_source": "4@/usr/lib/netdata/conf.d/health.d/ram.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/ram.conf",
            "edit_line": "4",
            "info": "system memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-2/alerts/system.ram.ram_in_use:::62a1da9c-5f61-41d0-8a7d-2e971cf300e3?chart\u003dsystem.ram\u0026alarm\u003dram_in_use\u0026transition\u003d5723d0f1-de8f-488b-8926-7f893642e1ae"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "0625fad1-394d-0474-6a2c-a18c6838c6df",
          "current": {
            "status": [
              "warning"
            ],
            "value": 90.40006896248734
          },
          "name": [
            "ram_in_use"
          ],
          "previous": {
            "status": "clear",
            "value": 89.66794162951169
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "5723d0f1-de8f-488b-8926-7f893642e1ae"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.ram",
          "name": "system.ram"
        },
        "context": {
          "name": [
            "system.ram"
          ]
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
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 13,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T08:14:59.386181Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_ swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003dfb80adde-2d7c-459f-a8f2-a0a0918d4c6e"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "warning"
            ],
            "value": 86.13736369583835
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "critical",
            "value": 99.99141691478141
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "fb80adde-2d7c-459f-a8f2-a0a0918d4c6e"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 11,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T06:36:12.132719Z",
    "data": {
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "isSelected": false,
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-2/alerts/disk_space._.disk_space_usage:::62a1da9c-5f61-41d0-8a7d-2e971cf300e3?chart\u003ddisk_space._\u0026alarm\u003ddisk_space_usage\u0026transition\u003d14a1aa69-59ca-4de6-89a4-cb2d7f8291af"
          },
          "class": "Utilization",
          "component": "Disk",
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "clear"
            ],
            "value": 50.95456182147541
          },
          "name": [
            "disk_space_usage"
          ],
          "previous": {
            "status": "critical",
            "value": 99.1844361563528
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "14a1aa69-59ca-4de6-89a4-cb2d7f8291af"
          },
          "type": "System"
        },
        "chart": {
          "id": "disk_space._",
          "name": "disk_space._"
        },
        "context": {
          "name": [
            "disk.space"
          ]
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
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 12,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T05:33:39.104598Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003d95d039fa-3bd1-46b3-a4d4-7ed31f9c8d42"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "critical"
            ],
            "value": 99.42664990739804
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "warning",
            "value": 96.73671099989129
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "95d039fa-3bd1-46b3-a4d4-7ed31f9c8d42"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 10,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-23T04:26:10.186325Z",
    "data": {
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "isSelected": false,
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-2/alerts/disk_space._.disk_space_usage:::62a1da9c-5f61-41d0-8a7d-2e971cf300e3?chart\u003ddisk_space._\u0026alarm\u003ddisk_space_usage\u0026transition\u003d38994401-f738-4acc-a4bf-9aadc97be167"
          },
          "class": "Utilization",
          "component": "Disk",
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "critical"
            ],
            "value": 99.1585645150551
          },
          "name": [
            "disk_space_usage"
          ],
          "previous": {
            "status": "clear",
            "value": 50.92182210757323
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "38994401-f738-4acc-a4bf-9aadc97be167"
          },
          "type": "System"
        },
        "chart": {
          "id": "disk_space._",
          "name": "disk_space._"
        },
        "context": {
          "name": [
            "disk.space"
          ]
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
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 9,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-22T12:56:03.06151Z",
    "data": {
      "host": [
        {
          "id": "62a1da9c-5f61-41d0-8a7d-2e971cf300e3",
          "isSelected": false,
          "name": "2021u39"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/war-room-2/alerts/disk_space._.disk_space_usage:::62a1da9c-5f61-41d0-8a7d-2e971cf300e3?chart\u003ddisk_space._\u0026alarm\u003ddisk_space_usage\u0026transition\u003dc1ca13db-dec8-4742-9735-c3df7c78c9aa"
          },
          "class": "Utilization",
          "component": "Disk",
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "clear"
            ],
            "value": 50.92707849209801
          },
          "name": [
            "disk_space_usage"
          ],
          "previous": {
            "status": "critical",
            "value": 99.16035834596573
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "c1ca13db-dec8-4742-9735-c3df7c78c9aa"
          },
          "type": "System"
        },
        "chart": {
          "id": "disk_space._",
          "name": "disk_space._"
        },
        "context": {
          "name": [
            "disk.space"
          ]
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
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 8,
    "isNotificationRead": false,
    "isRead": false,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-21T08:30:08.199914Z",
    "data": {
      "host": [
        {
          "id": "57a1d42f-815e-4922-9cd6-23a36f0502c3",
          "isSelected": false,
          "name": "2021U26"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "((used + free) \u003e 0) ? (used * 100 / (used + free)) : 0",
            "conf_source": "21@/usr/lib/netdata/conf.d/health.d/swap.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/swap.conf",
            "edit_line": "21",
            "info": "swap memory utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/system.swap.used_swap:::57a1d42f-815e-4922-9cd6-23a36f0502c3?chart\u003dsystem.swap\u0026alarm\u003dused_swap\u0026transition\u003d9942a7a8-3a86-43f7-9de4-854c13f93aa1"
          },
          "class": "Utilization",
          "component": "Memory",
          "config_hash": "f4b7b2d0-de8d-2711-b3e0-f0f479e18c68",
          "current": {
            "status": [
              "critical"
            ],
            "value": 98.25439120176544
          },
          "name": [
            "used_swap"
          ],
          "previous": {
            "status": "clear",
            "value": 88.16793092333016
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "9942a7a8-3a86-43f7-9de4-854c13f93aa1"
          },
          "type": "System"
        },
        "chart": {
          "id": "system.swap",
          "name": "system.swap"
        },
        "context": {
          "name": [
            "system.swap"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          },
          {
            "id": "ebd6b50d-d4a6-45f2-a54c-2e461b9bb2fb",
            "name": "War Room 2"
          },
          {
            "id": "6b7a2b89-80c4-401d-9bf5-01e5d9c68e58",
            "name": "War Room 1"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 7,
    "isNotificationRead": true,
    "isRead": true,
    "isSpaceRead": false,
    "priority": "High"
  },
  {
    "createdAt": "2023-08-21T08:14:35.174532Z",
    "data": {
      "host": [
        {
          "id": "993ca47b-8456-4028-87a2-2aba81175287",
          "isSelected": false,
          "name": "shivam-raval"
        }
      ],
      "Netdata": {
        "alert": {
          "annotations": {
            "calc_expr": "used * 100 / (avail + used)",
            "conf_source": "12@/usr/lib/netdata/conf.d/health.d/disks.conf",
            "edit_command": "sudo /etc/netdata/edit-config health.d/disks.conf",
            "edit_line": "12",
            "info": "disk / space utilization",
            "url": "https://app.netdata.cloud/spaces/bhaveshprajapa-space/rooms/all-nodes/alerts/disk_space._.disk_space_usage:::993ca47b-8456-4028-87a2-2aba81175287?chart\u003ddisk_space._\u0026alarm\u003ddisk_space_usage\u0026transition\u003d92622f9f-3162-45db-8686-fff1d25eb15f"
          },
          "class": "Utilization",
          "component": "Disk",
          "config_hash": "52f85de1-4543-57c0-d8f0-2194144e65da",
          "current": {
            "status": [
              "clear"
            ],
            "value": 54.013405969931824
          },
          "name": [
            "disk_space_usage"
          ],
          "previous": {
            "status": "warning",
            "value": 95.49110205593445
          },
          "role": [
            "sysadmin"
          ],
          "transition": {
            "id": "92622f9f-3162-45db-8686-fff1d25eb15f"
          },
          "type": "System"
        },
        "chart": {
          "id": "disk_space._",
          "name ": "disk_space._"
        },
        "context": {
          "name": [
            "disk.space"
          ]
        },
        "room": [
          {
            "id": "897a964f-55aa-4f3b-bda6-ce89704dff5d",
            "name": "All nodes"
          }
        ],
        "space": {
          "id": "22628414-b404-497e-a1a8-48f5356a53c9",
          "name": "Bhaveshprajapa space"
        }
      },
      "user": {
        "email": "bhaveshprajapati.hyperlink@gmail.com",
        "id": "ceec6556-321f-4494-bfd7-c165d4818651",
        "name": "bhaveshprajapati.hyperlink"
      }
    },
    "id": 6,
    "isNotificationRead": true,
    "isRead": true,
    "isSpaceRead": false,
    "priority": "High"
  }
]
"""
}