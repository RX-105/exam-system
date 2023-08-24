<template>
    <v-banner icon="mdi-alert-decagram" style="margin-bottom: 3vh" rounded>
        <v-banner-text>{{ t('register_form_print.banner_text') }}</v-banner-text>
        <v-banner-actions>
            <v-btn color="primary" prepend-icon="mdi-file-download" @click="saveToPdf">
                {{ t('register_form_print.banner_action_download_as_pdf') }}
            </v-btn>
        </v-banner-actions>
    </v-banner>
    <v-card>
        <v-card-title>{{ t('register_form_print.register_form_preview') }}</v-card-title>
        <v-card-text>
            <div class="print-area" ref="printArea">
                <h1 class="text-center printing printing-bold">{{ userInfo.school.name }}考试报名信息表</h1>
                <table style="border-bottom: 1px solid black;">
                    <tbody>
                    <tr class="table-title">
                        <td colspan="1" rowspan="1">姓名</td>
                        <td colspan="1" rowspan="1">性别</td>
                        <td colspan="1" rowspan="1">民族</td>
                        <td colspan="1" rowspan="1">籍贯</td>
                        <td colspan="1" rowspan="4" style="padding: 0">
                            <img :src="userStore.userAvatar" style="width: 138px;height: 160px;">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1">{{ userInfo.realname }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.gender }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.nationality }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.homeAddress }}</td>
                    </tr>
                    <tr class="table-title">
                        <td colspan="1" rowspan="1">出生年月</td>
                        <td colspan="1" rowspan="1">政治面貌</td>
                        <td colspan="1" rowspan="1">应届状态</td>
                        <td colspan="1" rowspan="1">文理分科</td>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1">{{ userInfo.birthday }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.politicalStatus }}</td>
                        <td colspan="1" rowspan="1">{{ isCurrent }}</td>
                        <td colspan="1" rowspan="1">{{ isScience }}</td>
                    </tr>
                    </tbody>
                </table>
                <table style="border-top: 1px solid black;border-bottom: 1px solid black">
                    <tbody>
                    <tr class="table-title">
                        <td colspan="5" rowspan="1">报考信息</td>
                    </tr>
                    <tr class="table-title">
                        <td colspan="1" rowspan="1">报考学校</td>
                        <td colspan="1" rowspan="1">报考专业</td>
                        <td colspan="1" rowspan="1">毕业学校</td>
                        <td colspan="2" rowspan="1">毕业时间</td>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1">{{ userInfo.school.name }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.major.name }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.graduateSchool }}</td>
                        <td colspan="2" rowspan="1">{{ userInfo.graduateTime.slice(0, userInfo.graduateTime.lastIndexOf('-')) }}</td>
                    </tr>
                    </tbody>
                </table>
                <table style="border-top: 1px solid black;border-bottom: 1px solid black">
                    <tbody>
                    <tr class="table-title">
                        <td colspan="4" rowspan="1">个人信息</td>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1" style="font-weight: bold">证件号码</td>
                        <td colspan="3" rowspan="1">{{ userInfo.identityId }}</td>
                    </tr>
                    </tbody>
                </table>
                <table style="border-top: 1px solid black;border-bottom: 2px solid black">
                    <tbody>
                    <tr class="table-title">
                        <td colspan="4" rowspan="1">通讯信息</td>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1" style="font-weight: bold">家庭住址</td>
                        <td colspan="3" rowspan="1">{{ userInfo.homeAddress }}</td>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1" style="font-weight: bold">邮寄地址</td>
                        <td colspan="3" rowspan="1">{{ userInfo.contactAddress }}</td>
                    </tr>
                    <tr class="table-title">
                        <td colspan="1" rowspan="1">收件人</td>
                        <td colspan="1" rowspan="1">邮政编码</td>
                        <td colspan="1" rowspan="1">联系电话</td>
                        <td colspan="1" rowspan="1">移动电话</td>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1">{{ userInfo.receiver }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.zipcode }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.contactNumber }}</td>
                        <td colspan="1" rowspan="1">{{ userInfo.phone }}</td>
                    </tr>
                    </tbody>
                </table>
                <table style="border-top: 0" id="print-hidden">
                    <tbody>
                    <tr>
                        <td colspan="1" rowspan="2" style="font-weight: bold;border: 0">考生<br>承诺</td>
                        <td colspan="3" rowspan="1" style="text-align: left;height: 60px;border-bottom: 0;border-top: 0;border-right: 0;">
                            我承诺上述填写的信息准确无误，照片为本人照片。如果存在虚假信息，一切后果均由我本人承担。
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1" style="border-top: 0;text-align: right;border-right: 0;border-bottom: 0">
                            考生签字：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
                            年&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;日
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </v-card-text>
    </v-card>
</template>

<script setup lang="ts">
import {useLocale} from "vuetify";
import {computed, onMounted, ref} from "vue";
import axios from "axios";
import {useAppStore} from "@/stores/appStore";
import {useUserStore} from "@/stores/userStore";
import * as html2pdf from 'html2pdf.js'

const { t } = useLocale()
const { showSnackbar } = useAppStore()
const userStore = useUserStore()
const printArea = ref(null)

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
                showSnackbar(`${t('register_form_print.error_fetch_user_info')}${r.status} ${r.message}`)
            }
        })
        .catch(err => {
            showSnackbar(`${t('register_form_print.error_fetch_user_info')}${err.message}`)
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
    // FIXME: 使用html2pdf将dom转换成pdf时会导致dom内的头像重新加载，但是非axios的请求不携带JSESSIONID等信息，导致重新加载后得到的头像是默认图像
    // FIXME: 目前想到的解决方法：1.新开一个页面专用于打印；2.考虑其他的dom转pdf工具
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
