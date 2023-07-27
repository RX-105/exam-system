<template>
    <v-card class="container">
        <v-card-title>学校公告</v-card-title>
        <v-card-subtitle>查看学校最新报考资讯</v-card-subtitle>
        <v-expansion-panels style="padding: 10px">
            <v-expansion-panel
                v-for="stage in registrationInfo.stages">
                <v-expansion-panel-title>
                    {{ registrationInfo.stageMap[stage.name] }}
                </v-expansion-panel-title>
                <v-expansion-panel-text>
                    开始时间：{{stage.startTime}}<br>
                    结束时间：{{stage.endTime}}<br>
                    备注：{{stage.remark}}
                </v-expansion-panel-text>
            </v-expansion-panel>
        </v-expansion-panels>
    </v-card>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useStore} from "vuex";

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
            store.commit("showDialogue", `加载学校资讯过程中出现了问题。<br/>Error: ${err.message}`)
        })
})
</script>
<style lang="scss">
.container {
    //line-height: 10px;
}
</style>
