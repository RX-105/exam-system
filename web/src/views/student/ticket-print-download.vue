<template>
    <div class="background" v-if="!loadIncomplete" ref="printArea">
        <v-row>
            <v-col cols="6">
                <table class="table-left"><!--共21行-->
                    <tbody>
                    <tr>
                        <th colspan="12" rowspan="1" class="no-border-bottom">{{user.school.recruitYears}}年{{user.school.name}}{{user.school.examName}}考试准考证</th>
                    </tr>
                    <tr>
                        <td colspan="12" rowspan="1" class="no-border-start">
                            <v-img :src="baseURL+'/api/student/idBarcode'"
                            max-width="100%"
                            max-height="40px"></v-img>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">考生编号</td>
                        <td colspan="6" rowspan="1">{{ user.userId }}</td>
                        <td colspan="3" rowspan="4" style="padding: 0">
                            <v-img :src="baseURL+'/api/student/avatar?refresh=false'"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">考生姓名/性别</td>
                        <td colspan="6" rowspan="1">{{ user.name }}/{{ user.gender }}</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">证件类型/号码</td>
                        <td colspan="6" rowspan="1">居民身份证/{{ user.identityId }}</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">学习工作单位</td>
                        <td colspan="6" rowspan="1">{{ user.graduateSchool }}</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">报考单位（代码）</td>
                        <td colspan="9" rowspan="1">{{ user.school.name }}（{{ user.school.schoolId }}）</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">报考专业（代码）</td>
                        <td colspan="9" rowspan="1">{{ user.major.name }}（{{ user.major.id }}）</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">考试时间</td>
                        <td colspan="5" rowspan="1">{{ timeRange(exams[0]) }}</td>
                        <td colspan="4" rowspan="1">{{ timeRange(exams[1]) }}</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">{{ exams[0].startTime.split(' ')[0] }}</td>
                        <td colspan="5" rowspan="1">{{ exams[0].name }}</td>
                        <td colspan="4" rowspan="1">{{ exams[1].name }}</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">{{ exams[2].startTime.split(' ')[0] }}</td>
                        <td colspan="5" rowspan="1">{{ exams[2].name }}</td>
                        <td colspan="4" rowspan="1">{{ exams[3].name }}</td>
                    </tr>
                    <tr>
                        <td colspan="3" rowspan="1">报考点名称（代码）</td>
                        <td colspan="5" rowspan="1">{{ user.school.name }}（{{ user.school.schoolId }}）</td>
                        <td colspan="2" rowspan="1">考生报名号</td>
                        <td colspan="2" rowspan="1">{{ user.userId }}</td>
                    </tr>
                    <tr>
                        <td colspan="3" class="no-border-bottom" style="vertical-align: bottom">考试地点</td>
                        <td colspan="9" rowspan="2">{{ user.school.name }}第{{ user.roomId }}考场，座位号{{ user.seatId }}</td>
                    </tr>
                    <tr>
                        <td colspan="3"></td>
                    </tr>
                    <tr>
                        <td colspan="3" class="no-border-bottom" style="vertical-align: bottom">报考点说明</td>
                        <td colspan="9" rowspan="2" style="text-align: left">
                            1.本考点实行智能安检门和考场两次安检，考生须按要求配合做好违规物
                            品检查并有序入场。2.本考点统一配备标准文具（签字笔、三角板、2B铅
                            笔、橡皮），除招生单位特殊要求外，不得携带其它文具及违禁物品进场
                            。3.更多要求请及时关注重庆市教育考试院和{{ user.school.name }}网站
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3"></td>
                    </tr>
                    <tr>
                        <td colspan="3" class="no-border-bottom" style="vertical-align: bottom">招生单位说明</td>
                        <td colspan="9" rowspan="2" style="text-align: left">
                            1.所有自命题科目均无须使用计算器等特殊工具；<br/>
                            2.初试成绩查询届时请关注我校研究生处网站通知。
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3"></td>
                    </tr>
                    </tbody>
                </table>
            </v-col>
            <v-col cols="6">
                <table class="table-right" style="width: 90%">
                    <tbody>
                    <tr>
                        <th colspan="1" rowspan="1">考&nbsp;&nbsp生&nbsp;&nbsp须&nbsp;&nbsp知</th>
                    </tr>
                    <tr>
                        <td colspan="1" rowspan="1" style="text-align: left;">
                            1.考生应在规定时间内登录本网站自行下载并使用A4幅面白纸打印《准考证》。
                            《准考证》正、反两面在使用期间均不得涂改或书写。<br/>
                            2.考生需认真查阅并遵守报考点及报考点所在地省级教育招生考试机构考试要求。<br/>
                            3.考生须凭本人《准考证》和有效居民身份证按规定时间和地点参加考试。<br/>
                            4.考试时间以北京时间为准。每科开考15分钟后不得入场。交卷出场时间不得早于当
                            科考试结束前30分钟，具体出场时间由考点所在省级教育招生考试机构规定。交卷出场后
                            不得再进场续考，也不得在考试机构规定的区域逗留或交谈。<br/>
                            5.考试地点由报考点指定。考生应在考前了解考试地点及考场有关注意事项。《准考
                            证》上未打印出考试地点的考生，考前要注意查询报考点公布的有关信息。<br/>
                            6.考生须严格遵守考场规则。应主动接受身份验证核查、安全检查和随身物品检查等
                            ，进入考场后对号入座。入座后将《准考证》、有效居民身份证放在桌面左上角，以便核
                            验。不得将手机带入考点（考试封闭管理区域）。<br/>
                            7.考生应按规定在指定位置准确清楚填（涂）姓名、考生编号等信息，并按要求在答
                            题卡（纸）指定位置粘贴条形码。凡因漏填（涂）、错填（涂）或字迹不清以及漏贴条形
                            码而影响评卷结果的，责任由考生自负。考生应在答题卡（纸）规定的区域内按题号顺序
                            答题，写在草稿纸或者规定区域以外的答案一律无效。<br/>
                            8.考生应按考点所在省级教育招生考试机构有关规定携带并使用文具。自命题科目按
                            招生单位说明携带文具。<br/>
                            9.考生不遵守考场规则，不服从考务工作人员管理，有违纪、作弊等行为的，将按照
                            《中华人民共和国教育法》以及《国家教育考试违规处理办法》进行处理，并将记入国家
                            教育考试考生诚信档案；涉嫌违法的，移送司法机关，依照《中华人民共和国刑法》等追
                            究法律责任。<br/>
                            10.其他未尽事宜以考点及所在省级教育招生考试机构发布的公告为准。<br/>
                            <p style="text-align: center;font-weight: bold">----------- 特 别 提 醒 ------------</p>
                            依据《中华人民共和国刑法修正案（九）》，“在法律规定的国家考试中，组织作弊
                            的，处三年以下有期徒刑或者拘役，并处或者单处罚金；情节严重的，处三年以上七年以
                            下有期徒刑，并处罚金。”<br/>
                            “为他人实施前款犯罪提供作弊器材或者其他帮助的，依照前款的规定处罚。”<br/>
                            “为实施考试作弊行为，向他人非法出售或者提供第一款规定的考试的试题、答案的
                            ，依照第一款的规定处罚。”<br/>
                            “代替他人或者让他人代替自己参加第一款规定的考试的，处拘役或者管制，并处或
                            者单处罚金。”<br/>
                            《最高人民法院、最高人民检察院关于办理组织考试作弊等刑事案件适用法律若干问
                            题的解释》已于2019年9月4日起施行。
                        </td>
                    </tr>
                    </tbody>
                </table>
            </v-col>
        </v-row>
    </div>

    <v-snackbar v-model="errorSnackbar">{{ errorSnackbarText }}
        <template v-slot:actions>
            <v-btn
                color="pink"
                variant="text"
                @click="errorSnackbar = false">
                {{ t("ticket_download.close") }}
            </v-btn>
        </template>
    </v-snackbar>
    <v-snackbar v-model="getTrue" class="no-print">{{ t("ticket_download.print_prompt") }}
        <template v-slot:actions>
            <v-btn
                color="pink"
                variant="text"
                @click="saveToPdf">
                {{ t("ticket_download.print_btn") }}
            </v-btn>
        </template>
    </v-snackbar>
    <v-overlay
        v-model="loadIncomplete"
        class="align-center justify-center"
    >
        <v-progress-circular
            color="primary"
            size="64"
            indeterminate
        ></v-progress-circular>
    </v-overlay>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, toRaw} from "vue";
import axios from "axios";
import {useLocale} from "vuetify";
import html2pdf from "html2pdf.js";

const {t} = useLocale()

const user = ref<UserInfo>({
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
const exams = ref<ExamInfo[]>([])
const errorSnackbar = ref<boolean>(false)
const errorSnackbarText = ref<string>()
const loadIncomplete = ref<boolean>(true)
const printArea = ref()

onMounted(() => {
    axios.post('/api/student/user/this')
        .then(resp => {
            const code = resp.data.status
            if ([0].includes(code)) {
                user.value = resp.data.data['user-info']
                exams.value = resp.data.data['exam-info']
                loadIncomplete.value = false
            } else {
                errorSnackbarText.value = resp.data.message
                errorSnackbar.value = true
            }
        })
    // setTimeout(saveToPdf, 1000)
})

const baseURL = computed(() => {
    return axios.defaults.baseURL
})

const timeRange = (exam: ExamInfo) => {
    let str = ''
    if(parseInt(exam.endTime.split(':')[0]) < 12) str += '上午'
    else str += '下午'
    str += `${exam.startTime.split(' ')[1]}-${exam.endTime.split(' ')[1]}`
    return str
}

const getTrue = () => true;

const saveToPdf = () => {
    const fileName = `${new Date().getFullYear()}年${user.value.school.name}报名信息表（${user.value.realname}）.pdf`
    const opt = {
        margin: 0,
        filename: fileName,
        image: { type: 'jpeg', quality: 1 },
        html2canvas: { scale: 2,useCORS: true },
        jsPDF: { unit: 'in', format: 'a4', orientation: 'l' }
    }
    // html2pdf().from(printArea.value).toPdf().save(fileName)
    html2pdf().set(opt).from(printArea.value).save()
}
</script>
<style scoped>
.background {
    /*background-color: white;*/
    width: 100%;
    height: 100%;
}
/*.table-left > tr > th,td {*/
/*    height: 30px;*/
/*}*/
.table-left{
    width: 100%;
}
.barcode-img {
    object-fit: cover;
    width: 50%;
    height: 30%;
}
table {
    border-collapse: collapse;
    border: 2px solid black;
    margin: 20px;
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
.no-border-bottom {
    border-bottom: 1px solid white;
}
.no-border-start{
    border-top: 1px solid white;
}
@media print
{
    .no-print, .no-print *
    {
        display: none !important;
    }
}
</style>
