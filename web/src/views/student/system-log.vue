<template>
    <div class="tables_page">
        <div class="search_bar d-flex jsb" style="margin-bottom: 16px">
            <div class="d-flex jsb search_tool">
                <div class="search_wrap mr-4">
                    <v-text-field
                        rounded
                        class="elevation-0"
                        density="compact"
                        variant="solo"
                        label="Search sample"
                        append-inner-icon="mdi-magnify"
                        single-line
                        hide-details
                        disabled="true"
                    ></v-text-field>
                </div>
                <v-btn class="btn" variant="flat"
                       prepend-icon="mdi-filter-variant"
                       @click="showFilterPanel" :color="filterBtnColor">
                    <span> 过滤</span>
                </v-btn>
            </div>
        </div>
        <v-card>
            <v-table class="table">
                <thead>
                <tr>
                    <th class="text-left">Log ID</th>
                    <th class="text-left">用户名</th>
                    <th class="text-left">用户组</th>
                    <th class="text-left">记录时间</th>
                    <th class="text-left">行为</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="log in logData.content" :key="log.logId">
                    <td>{{log.logId}}</td>
                    <td>{{log.username}}</td>
                    <td>{{log.groupName}}</td>
                    <td>{{log.time}}</td>
                    <td>{{log.extras}}</td>
                </tr>
                </tbody>
            </v-table>
            <div class="d-flex py-2" style="justify-content: center">
                <v-pagination v-model="thisPageReadable"
                              :length="logData.pageInfo.totalPages"
                              total-visible="6"
                              size="small" rounded="circle"></v-pagination>
            </div>
        </v-card>
    </div>
    <v-overlay v-model="overlayState"
               class="align-center justify-center">
        <div style="width: 60vw">
            <v-card>
                <v-card-title>过滤选项</v-card-title>
                <v-card-item>
                    <v-expansion-panels variant="accordion">
                        <v-expansion-panel>
                            <v-expansion-panel-title>
                                <v-row no-gutters>
                                    <v-col cols="6" class="d-flex justify-start">
                                        过滤开始时间和结束时间
                                    </v-col>
                                    <v-col cols="6">
                                        <v-row>
                                            <v-col cols="6" class="d-flex justify-start">
                                                开始时间：{{ logFilter.from || '未设置' }}
                                            </v-col>
                                            <v-col cols="6" class="d-flex justify-start">
                                                结束时间：{{ logFilter.to || '未设置' }}
                                            </v-col>
                                        </v-row>
                                    </v-col>
                                </v-row>
                            </v-expansion-panel-title>
                            <v-expansion-panel-text>
                                <v-row justify="space-around">
                                    <v-col cols="4">
                                        <v-text-field
                                            v-model="logFilter.from"
                                            label="开始时间"
                                            type="date"
                                            :error-messages="logFilter.errorMessage"
                                        ></v-text-field>
                                    </v-col>

                                    <v-col cols="4">
                                        <v-text-field
                                            v-model="logFilter.to"
                                            label="结束时间"
                                            type="date"
                                            :error-messages="logFilter.errorMessage"
                                        ></v-text-field>
                                    </v-col>
                                </v-row>
                            </v-expansion-panel-text>
                        </v-expansion-panel>
                        <v-expansion-panel>
                            <v-expansion-panel-title>
                                <v-row>
                                    <v-col cols="6">过滤行为类型</v-col>
                                </v-row>
                            </v-expansion-panel-title>
                            <v-expansion-panel-text>
                                <v-select v-model="logFilter.action"
                                          :items="actionList"
                                          label="选择行为类型"
                                          item-title="name"
                                          item-value="action">
                                </v-select>
                            </v-expansion-panel-text>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </v-card-item>
                <v-card-actions>
                    <v-btn color="primary" @click="applyFilter" :disabled="logFilter.btnDisabled">
                        完成
                    </v-btn>
                    <v-btn color="primary" @click="clearFilter">重置</v-btn>
                </v-card-actions>
            </v-card>
        </div>
    </v-overlay>
</template>
<script setup lang="ts">
import {ref, onMounted, computed, watch} from 'vue';
import axios from "axios";
import {useStore} from "vuex";

// 日志加载逻辑
const store = useStore()
const logData = ref<LogData>({
    pageInfo: {
        totalPages: 0,
        currentElements: 0,
        totalElements: 0,
        thisPage: 0,
    },
    content: [],
})
const thisPageReadable = computed({
    get() { return logData.value.pageInfo.thisPage + 1 },
    set(newVal) { logData.value.pageInfo.thisPage = newVal - 1 }
})
watch(thisPageReadable, async (newVal, oldVal) => {
    if (logData.value.pageInfo.thisPage >= logData.value.pageInfo.totalPages
        || logData.value.pageInfo.thisPage < 0) {
        store.commit('showDialogue', '这样做毫无意义。')
        logData.value.pageInfo.thisPage = oldVal - 1
    } else {
        await loadLogs()
    }
})
onMounted(() => {
    loadLogs()
})
function loadLogs() {
    axios.get("/api/student/all-logs", {
        params: {
            pageNum: logData.value.pageInfo.thisPage,
            pageSize: 10,
            action: logFilter.value.action,
            from: logFilter.value.from,
            to: logFilter.value.to
        }
    })
        .then(res => {
            if (res.data.status == 0) {
                logData.value = res.data.data as LogData
            } else if (res.data.status == 1010) {
                store.commit('showSnackbar', '登录过期，请尝试重新登录。')
            } else {
                store.commit('showSnackbar', `内容加载失败。Error: ${res.data.message}`)
            }
        })
        .catch(err => {
            store.commit('showSnackbar', `内容加载失败。Error: ${err.message}`)
        })
}

// 过滤器逻辑部分
const logFilter = ref({
    from: '',
    to: '',
    action: '',
    errorMessage: '',
    btnDisabled: false,
})
const overlayState = ref(false)
const actionList = ref([
    { name: '主动登录', action: 'login' },
    { name: '注册', action: 'register' },
    { name: '更新头像', action: 'update-avatar' },
])
const filterBtnColor = ref('default')
watch(logFilter, () => {
    if(logFilter.value.from !== '' || logFilter.value.to !== '') {
        if (new Date(logFilter.value.to) < new Date(logFilter.value.from)) {
            logFilter.value.errorMessage = '时间段不合适。'
            logFilter.value.btnDisabled = true
        } else {
            logFilter.value.errorMessage = ''
            logFilter.value.btnDisabled = false
        }
    }
}, { deep: true })
function showFilterPanel() {
    overlayState.value = true
}
function applyFilter() {
    overlayState.value = false
    if (logFilter.value.from === '' || logFilter.value.to === '' ||
        logFilter.value.action === '' || logFilter.value.errorMessage !== '') {
        store.commit('showSnackbar', '过滤选项不完整，过滤搜索将不会生效。')
    } else {
        filterBtnColor.value = 'primary'
        loadLogs()
    }
}
function clearFilter() {
    logFilter.value.from = ''
    logFilter.value.to = ''
    logFilter.value.action = ''
    filterBtnColor.value = 'default'
}

</script>
<style lang="scss">
.tables_page {
    .tjjj {
        .tj_label {
            padding: 4px 8px;
            border-radius: 15px;
            background: rgba(244, 67, 54, 0.2);
            color: #f44336;
        }
        .v-chip {
            margin-inline-end: 0 !important;
        }
    }
    .search_bar {
        .search_tool {
            .search_wrap {
                flex: 0 0 260px;
            }
            .btn {
                height: 40px;
            }
        }
        .v-field--variant-solo {
            box-shadow: none;
        }
    }
    .table {
        .td1 {
            .name {
                font-weight: 700;
                height: 21px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
            .sub_title {
                font-size: 12px;
            }
            .v-icon {
                font-size: 35px;
            }
        }
    }
}
.isMobile {
    .tables_page {
        .search_bar {
            display: block !important;
            .search_tool {
                margin-bottom: 16px;
                .search_wrap {
                    flex: 1;
                }
            }
        }
        .table {
            width: calc(100vw - 32px);
            overflow: hidden;
        }
    }
}
</style>
