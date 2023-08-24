<template>
    <v-alert
        closable
        :title="t('exam_register.submission_title')"
        :text="t('exam_register.submission_text')"
        type="info"
    ></v-alert>
    <v-form :disabled="formDisabled">
        <v-container>
            <v-row>
                <v-col cols="6">
                    <!--  左侧表单  -->
                    <v-card class="form-card">
                        <v-card-title>{{ t('exam_register.form_school_and_major') }}</v-card-title>
                        <v-card-text>
                            <v-select
                                v-model="selectedSchool"
                                :items="schools"
                                item-title="name"
                                item-value="schoolId"
                                :label="t('exam_register.form_school')"
                                variant="outlined"
                                density="comfortable"
                                prepend-icon="mdi-town-hall">
                            </v-select>
                            <v-select
                                v-model="selectedMajor"
                                :items="majors"
                                item-title="name"
                                item-value="id"
                                :label="t('exam_register.form_major')"
                                variant="outlined"
                                density="comfortable"
                                prepend-icon="mdi-school">
                            </v-select>
                        </v-card-text>
                    </v-card>
                    <v-card class="form-card">
                        <v-card-title>{{ t('exam_register.form_education_background') }}</v-card-title>
                        <v-card-text>
                            <v-text-field
                                v-model="userInfo.graduateSchool"
                                :label="t('exam_register.form_graduate_school')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-account-school">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.graduateTime"
                                :label="t('exam_register.form_graduate_time')"
                                variant="outlined"
                                type="month"
                                :hint="t('exam_register.form_graduate_time_hint')"
                                density="comfortable"
                                prepend-icon="mdi-calendar">
                            </v-text-field>
                            <v-radio-group
                                inline
                                v-model="isCurrent"
                                :label="t('exam_register.form_is_current')"
                                prepend-icon="mdi-check-all">
                                <v-radio :label="t('exam_register.form_current')" value="true"></v-radio>
                                <v-radio :label="t('exam_register.form_not_current')" value="false"></v-radio>
                            </v-radio-group>
                            <v-radio-group
                                inline
                                v-model="isScience"
                                :label="t('exam_register.form_is_science')"
                                prepend-icon="mdi-check-all">
                                <v-radio :label="t('exam_register.form_science')" value="true"></v-radio>
                                <v-radio :label="t('exam_register.form_art')" value="false"></v-radio>
                            </v-radio-group>
                            <v-select
                                v-model="userInfo.englishLevel"
                                :items="['CET-4 425+', 'CET-6 425+', 'TEM-6 60+', 'TEM-8 60+', 'IELTS 6.0+', 'TOEFL 80+', '无']"
                                :label="t('exam_register.form_english_level')"
                                variant="outlined"
                                :hint="t('exam_register.form_english_level_hint')"
                                density="comfortable"
                                prepend-icon="mdi-translate">
                            </v-select>
                        </v-card-text>
                    </v-card>
                    <v-banner rounded>
                        <v-banner-text>{{ t('exam_register.link_banner_text') }}</v-banner-text>
                        <v-banner-actions>
                            <v-btn
                                @click="push('/student/dashboard/avatar')"
                                color="primary" prepend-icon="mdi-open-in-new">{{ t('exam_register.open') }}</v-btn>
                        </v-banner-actions>
                    </v-banner>
                </v-col>
                <v-col cols="6">
                    <!--  右侧表单  -->
                    <v-card class="form-card">
                        <v-card-title>{{ t('exam_register.form_basic_info') }}</v-card-title>
                        <v-card-text>
                            <v-text-field
                                v-model="userInfo.realname"
                                :label="t('exam_register.form_real_name')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-account-circle">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.identityId"
                                :label="t('exam_register.form_identity_id')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-id-card">
                            </v-text-field>
                            <v-radio-group
                                inline
                                v-model="userInfo.gender"
                                :label="t('exam_register.form_gender')"
                                prepend-icon="mdi-gender-male-female">
                                <v-radio :label="t('exam_register.form_gender_male')" value="男"></v-radio>
                                <v-radio :label="t('exam_register.form_gender_female')" value="女"></v-radio>
                            </v-radio-group>
                            <v-text-field
                                v-model="userInfo.birthday"
                                :label="t('exam_register.form_birthday')"
                                variant="outlined"
                                type="date"
                                density="comfortable"
                                prepend-icon="mdi-cake-variant">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.nationality"
                                :label="t('exam_register.form_nationality')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-account-group">
                            </v-text-field>
                            <v-select
                                v-model="userInfo.politicalStatus"
                                :items="political_status"
                                item-title="name"
                                item-value="value"
                                :label="t('exam_register.form_political_status')"
                                variant="outlined"
                                density="comfortable"
                                prepend-icon="mdi-hammer-sickle">
                            </v-select>
                            <v-text-field
                                v-model="userInfo.source"
                                :label="t('exam_register.form_source')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-city">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.homeAddress"
                                :label="t('exam_register.form_home_address')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-home">
                            </v-text-field>
                        </v-card-text>
                    </v-card>
                    <v-card class="form-card">
                        <v-card-title>{{ t('exam_register.form_contacts') }}</v-card-title>
                        <v-card-text>
                            <v-text-field
                                v-model="userInfo.receiver"
                                :label="t('exam_register.form_receiver')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-contacts">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.contactAddress"
                                :label="t('exam_register.form_contact_address')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-map-marker">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.zipcode"
                                :label="t('exam_register.form_zipcode')"
                                variant="outlined"
                                type="number"
                                density="comfortable"
                                prepend-icon="mdi-card-account-mail">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.contactNumber"
                                :label="t('exam_register.form_contact_number')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-phone-in-talk">
                            </v-text-field>
                            <v-text-field
                                v-model="userInfo.phone"
                                :label="t('exam_register.form_phone')"
                                variant="outlined"
                                type="input"
                                density="comfortable"
                                prepend-icon="mdi-cellphone">
                            </v-text-field>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
            <v-row class="justify-end" style="margin: 2vh 2vw">
                <v-btn
                    prepend-icon="mdi-cloud-upload"
                    size="large"
                    color="primary"
                    elevation="8"
                    :loading="fabLoading"
                    :disabled="formDisabled"
                    @click="updateUserInfo">
                    {{ t('exam_register.save') }}
                </v-btn>
            </v-row>
        </v-container>
    </v-form>
</template>

<script lang="ts" setup>
import {useLocale} from "vuetify";
import {computed, onMounted, ref, watch} from "vue";
import axios from "axios";
import {useAppStore} from "@/stores/appStore";
import {useRouter} from "vue-router";

const { t } = useLocale()
const { showSnackbar, showDialogue, showLoading, stopLoading } = useAppStore()
const { push } = useRouter()

const formDisabled = ref<boolean>(true)
const REGISTER_STAGE = "register"
const fabLoading = ref<boolean>(false)

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
const schools = ref<School[]>([])
const majors = ref<Major[]>([])
const political_status = computed(() => {
    return [
        { name: t('exam_register.form_party_member'), value: "中共党员" },
        { name: t('exam_register.form_party_member_ready'), value: "中共预备党员" },
        { name: t('exam_register.form_league_member'), value: "共青团员" },
        { name: t('exam_register.form_pioneer'), value: "少先队员" },
        { name: t('exam_register.form_democratic'), value: "民主党派" },
        { name: t('exam_register.form_no_affiliation'), value: "无党派人士" },
        { name: t('exam_register.form_people'), value: "群众" },
        { name: t('exam_register.form_traitor'), value: "叛徒" },
        { name: t('exam_register.form_counter_revolution'), value: "反革命分子" },
    ]
})
const selectedSchool = computed({
    get() {
        return userInfo.value.school.schoolId
    },
    set(val) {
        userInfo.value.school.schoolId = val
    }
})
const selectedMajor = computed({
    get() {
        return userInfo.value.major.id
    },
    set(val) {
        userInfo.value.major.id = val
    }
})
// what the heck of type coercion???
const isCurrent = computed({
    get() {
        return String(userInfo.value.isCurrent)
    },
    set(val: string) {
        userInfo.value.isCurrent = (val === "true")
    }
})
const isScience = computed({
    get() {
        return String(userInfo.value.isScience)
    },
    set(val: string) {
        userInfo.value.isScience = (val === "true")
    }
})

onMounted(() => {
    // 检查当前是否开放编辑
    axios.get(`/api/student/validateStage/${REGISTER_STAGE}`)
        .then(res => {
            const r = res.data as R
            if (r.status === 0) {
                formDisabled.value = false
            } else {
                formDisabled.value = true
                showDialogue(`${t('exam_register.beyond_submission_time')}`)
            }
        })
        .catch(err => {
            showSnackbar(`${t('exam_register.rule_fetch_failed')}${err.message}`)
        })
    // 获取当前用户信息
    axios.post("/api/student/user/this")
        .then(res => {
            const r = res.data as R
            if (r.status === 0) {
                userInfo.value = r.data['user-info'] as UserInfo
                // 不知道为什么后端的YearMonth类型被json化之后变成了年月日了，只能前端来改了
                userInfo.value.graduateTime
                    = userInfo.value.graduateTime.slice(0, userInfo.value.graduateTime.lastIndexOf("-"))
            } else if (r.status === 1010) {
                showSnackbar(`${t('exam_register.session_expired')}`)
            } else {
                showSnackbar(`${t('exam_register.user_info_fetch_abnormal')}${r.status}`)
            }
        })
        .catch(err => {
            showSnackbar(`${t('exam_register.user_info_fetch_failed')}${err.message}`)
        })
    // 获取学校列表
    axios.get("/api/student/schools")
        .then(res => {
            const r = res.data as R
            if (r.status === 0) {
                schools.value = r.data.schools as School[]
            } else {
                showSnackbar(`${t('exam_register.user_info_fetch_abnormal')}${r.status}`)
            }
        })
        .catch(err => {
            showSnackbar(`${t('exam_register.school_list_fetch_failed')}${err.message}`)
        })
})

// 当前用户报考学校ID变化时，重新加载学校专业列表
watch(
    () => userInfo.value.school.schoolId,
    (newValue, _oldValue) => {
        axios.post(`/api/student/school2major/${newValue}`)
            .then(res => {
                const r = res.data as R
                if (r.status === 0) {
                    majors.value = r.data.majors as Major[]
                } else {
                    showSnackbar(`${t('exam_register.major_list_fetch_failed')}`)
                }
            })
            .catch(err => {
                showSnackbar(`${t('exam_register.major_list_fetch_failed')}${err.message}`)
            })
    }
)
// 当表单不可编辑状态更新为true时，弹出提示框
watch(formDisabled, (newValue) => {
    if (newValue) {
        showDialogue(`${t('exam_register.beyond_submission_time')}`)
    }
})
// 学校列表更新时，移除ID为100000的学校（这个ID是管理员组）
watch(schools, (newValue: School[]) => {
    newValue.forEach((v, i, a) => {
        if (v.schoolId === 100000) {
            a.splice(i, 1)
        }
    })
})

const updateUserInfo = () => {
    showLoading()
    fabLoading.value = true
    // 这API谁设计的啊，怎么这么傻X？啊，是我呀，那没事了。
    axios.post("/api/student/registerExam", {
        schoolId: userInfo.value.school.schoolId,
        majorId: userInfo.value.major.id,
        gradSchool: userInfo.value.graduateSchool,
        gradTime: userInfo.value.graduateTime,
        isCurrent: userInfo.value.isCurrent,
        isScience: userInfo.value.isScience,
        english: userInfo.value.englishLevel,
        mailName: userInfo.value.receiver,
        mailAddr: userInfo.value.contactAddress,
        zip: userInfo.value.zipcode,
        contact: userInfo.value.contactNumber,
        phone: userInfo.value.phone,
        realName: userInfo.value.realname,
        identity: userInfo.value.identityId,
        gender: userInfo.value.gender,
        nationality: userInfo.value.nationality,
        politics: userInfo.value.politicalStatus,
        source: userInfo.value.source,
        home: userInfo.value.homeAddress,
        birthday: userInfo.value.birthday,
    })
        .then(res => {
            const r = res.data as R
            if ([101, 102, 1008, 1009].includes(r.status)) {
                showSnackbar(r.message)
            } else if (r.status === 0) {
                userInfo.value = r.data['user-info']
                userInfo.value.graduateTime
                    = userInfo.value.graduateTime.slice(0, userInfo.value.graduateTime.lastIndexOf("-"))
                showSnackbar(`${t('exam_register.user_info_update_ok')}`)
            } else {
                showSnackbar(`${t('exam_register.user_info_update_failed')}${r.status}`)
            }
            stopLoading()
            fabLoading.value = false
        })
        .catch(err => {
            stopLoading()
            fabLoading.value = false
            showSnackbar(`${t('exam_register.user_info_update_failed')}${err.message}`)
        })
}
</script>

<style scoped>
.form-card {
    margin-bottom: 3vh;
}
</style>
