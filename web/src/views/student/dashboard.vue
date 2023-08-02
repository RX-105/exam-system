<template>
    <v-card class="container">
        <v-card-title>{{ t('dashboard.school_notice_title') }}</v-card-title>
        <v-card-subtitle>{{ t('dashboard.school_notice_subtitle') }}</v-card-subtitle>
        <v-expansion-panels style="padding: 10px" variant="accordion">
            <v-expansion-panel
                v-for="stage in registrationInfo.stages">
                <v-expansion-panel-title>
                    {{ registrationInfo.stageMap[stage.name] }}
                </v-expansion-panel-title>
                <v-expansion-panel-text>
                    {{ t('dashboard.start_time') }}{{ stage.startTime }}<br>
                    {{ t('dashboard.end_time') }}{{ stage.endTime }}<br>
                    {{ t('dashboard.remarks') }}{{ stage.remark }}
                </v-expansion-panel-text>
            </v-expansion-panel>
        </v-expansion-panels>
    </v-card>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useStore} from "vuex";
import {useLocale} from "vuetify";

const { t } = useLocale()

const registrationInfo = ref<RegistrationData>({
    stageMap: {},
    schoolName: '',
    stages: [],
})
const store = useStore()

onMounted(() => {
    axios.get("/api/student/registration-notice")
        .then(res => {
            if (res.data.status === 0) {
                registrationInfo.value = res.data.data as RegistrationData
            }
        })
        .catch(err => {
            store.commit("showDialogue", `${t('dashboard.failed_fetching_notice_line_1')}<br/>${t('dashboard.failed_fetching_notice_line_2')}${err.message}`)
        })
})
</script>
<style lang="scss">
.container {
    //line-height: 10px;
}
</style>
