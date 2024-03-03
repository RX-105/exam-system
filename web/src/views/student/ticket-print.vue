<template>
    <v-banner icon="mdi-alert-decagram" style="margin-bottom: 3vh" rounded>
        <v-banner-text>点击跳转到新页面打印准考证。</v-banner-text>
        <v-banner-actions>
            <v-btn color="primary" prepend-icon="mdi-file-download" @click="router.push({name: 'ticket-download'})">
                转到
            </v-btn>
        </v-banner-actions>
    </v-banner>
</template>

<script setup lang="ts">
import {useLocale} from "vuetify";
import {computed, onMounted, ref} from "vue";
import axios from "axios";
import {useAppStore} from "@/stores/appStore";
import {useUserStore} from "@/stores/userStore";
import * as html2pdf from 'html2pdf.js'
import {useRouter} from "vue-router";

const { t } = useLocale()
const { showSnackbar } = useAppStore()
useUserStore();
const router = useRouter()
const printArea = ref()

const userInfo = ref<UserInfo>({
    userId: 0,
    tableId: null,
    name: '',
    realname: '',
    identityId: '',
    gender: '',
    politicalStatus: '',
    phone: 0,
    source: '',
    graduateSchool: '',
    graduateTime: '',
    isCurrent: false,
    isScience: false,
    school: {
        schoolId: 0,
        code: '',
        name: '',
        address: '',
        zipcode: 0,
        contactNumber: '',
        examName: '',
        recruitYears: '',
    },
    major: {
        id: 0,
        name: '',
        schoolId: 0,
        applicantCount: 0,
        enrollmentCount: 0,
        acceptScore: 0.0,
        admissionCount: 0,
    },
    englishLevel: '',
    homeAddress: '',
    nationality: '',
    birthday: '',
    avatarName: '',
    roomId: 0,
    seatId: 0,
    admissionId: null,
    isConfirmed: false,
    receiver: '',
    contactAddress: '',
    zipcode: 0,
    contactNumber: '',
})

const isScience = computed(() => {
    return userInfo.value.isScience ? "理科" : "文科"
})
const isCurrent = computed(() => {
    return userInfo.value.isCurrent ? "应届" : "往届"
})

onMounted(() => {
    axios.get("/api/student/regFormInfo")
        .then(res => {
            const r = res.data as R
            if (r.status === 0) {
                userInfo.value = r.data.user as UserInfo
            } else {
                showSnackbar(`用户信息获取异常。Error: ${r.status} ${r.message}`)
            }
        })
        .catch(err => {
            showSnackbar(`用户信息获取异常。Error: ${err.message}`)
        })
})

const saveToPdf = () => {
    const fileName = `${new Date().getFullYear()}年${userInfo.value.school.name}报名信息表（${userInfo.value.realname}）.pdf`
    const opt = {
        margin: 0,
        filename: fileName,
        image: { type: 'jpeg', quality: 1 },
        html2canvas: { scale: 2,useCORS: true },
        jsPDF: { unit: 'in', format: 'a4', orientation: 'p' }
    }
    // html2pdf().from(printArea.value).toPdf().save(fileName)
    html2pdf().set(opt).from(printArea.value).save()
}
</script>

<style scoped>
.printing {
    font-family: 宋体,fangsong;
}
.printing-bold {
    font-weight: bold;
}
table {
    border-collapse: collapse;
    border: 2px solid black;
    margin-left: auto;
    margin-right: auto;
    width: 700px;
    table-layout: fixed;
    user-select: none;
}
th, td {
    padding-left: 15px;
    padding-right: 15px;
    border: 1px solid black;
    font-family: fangsong;
    text-align: center;
    height: 40px;
}
.table-title > td {
    font-weight: bold;
}

.print-area {
    background-color: white;
    color: black;
    padding: 5vh 2vw;
    margin: 0 5vw;
}
</style>
