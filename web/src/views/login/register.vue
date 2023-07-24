<template>
    <v-progress-linear :indeterminate="isLoading" color="primary"
                       style="position: fixed;z-index: 999">
    </v-progress-linear>
    <v-card class="login_container">
        <img src="../../assets/Frame-c100fb2f.png" class="frame"/>
        <div class="group">
            <v-card class="form">
                <div class="text-h5">欢迎使用<br>重庆文理学院考务系统</div>
                <div class="mt-4">
                    <v-text-field
                        label="用户名"
                        variant="outlined"
                        density="compact"
                        clearable
                        :rules="[required]"
                        :error-messages="errorMessage.username"
                        v-model="username"
                    ></v-text-field>
                </div>
                <div>
                    <v-text-field
                        label="密码"
                        type="password"
                        variant="outlined"
                        density="compact"
                        clearable
                        v-model="password"
                        :rules="[required]"
                        :error-messages="errorMessage.password"
                    ></v-text-field>
                </div>
                <div>
                    <v-text-field
                        label="确认密码"
                        type="password"
                        variant="outlined"
                        density="compact"
                        clearable
                        v-model="passwordConfirm"
                        :rules="[required]"
                        :error-messages="errorMessage.passwordConfirm"
                    ></v-text-field>
                </div>
                <v-row>
                    <v-col>
                        <v-btn
                            color="primary"
                            prepend-icon="mdi-arrow-left"
                            size="large"
                            type="button"
                            to="/login">
                            返回
                        </v-btn>
                    </v-col>
                    <v-col>
                        <v-btn
                            color="primary"
                            append-icon="mdi-arrow-right"
                            size="large"
                            type="submit"
                            @click="register">
                            注册
                        </v-btn>
                    </v-col>
                </v-row>
            </v-card>
            <v-card class="desc">
                <div class="logo mt-4">
                    <img :src="logo" height="60"/>
                    <div class="text-h5 mt-2">Material UI</div>
                </div>
                <div class="mt-4">
                    vue-material-admin is a free open source mid-backend template based on Vuetify
                </div>
                <div class="mt-4">
                    made with by ❤️
                    <a
                        target="_blank"
                        style="
                            color: rgba(
                                var(--v-theme-on-background),
                                var(--v-high-emphasis-opacity)
                            );
                        "
                        href="https://github.com/jaywoow"
                    >Chen HuaJie</a
                    >
                </div>
            </v-card>
        </div>
    </v-card>
    <SimpleDialogue ref="dialogue"></SimpleDialogue>
</template>
<script lang="ts" setup>
import logo from '@/assets/admin-logo.png';
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import {useStore} from "vuex";
import {useRoute, useRouter} from "vue-router";
import SimpleDialogue from "@/utils/SimpleDialogue.vue";
import {debounce} from "lodash";

const store = useStore()
const router = useRouter()
const route = useRoute()

const username = ref("")
const password = ref("")
const passwordConfirm = ref("")
const dialogue = ref()
const isLoading = ref(false)
const errorMessage = ref({
    username: "",
    password: "",
    passwordConfirm: "",
})

let showDialogue: Function
onMounted(async () => {
    showDialogue = dialogue.value.showDialogue
    username.value = route.query.name as string
})

watch(username, debounce(() => {
    axios.post(`/api/student/checkUser/${username.value}`)
        .then(res => {
            if (res.data.status === 0) {
                errorMessage.value.username = "这个用户名已被占用。"
            } else {
                errorMessage.value.username = ""
            }
        })
}, 500))


watch(passwordConfirm, debounce(() => {
    if (password.value !== passwordConfirm.value) {
        errorMessage.value.passwordConfirm = "密码和确认密码不相符。"
    } else {
        errorMessage.value.passwordConfirm = ""
    }
}, 500))

function register() {
    if (username.value === '' || password.value === ''
        || passwordConfirm.value === '') {
        showDialogue("请填写所有字段。")
        return
    }
    if (errorMessage.value.username !== "" ||
        errorMessage.value.password !== "" ||
        errorMessage.value.passwordConfirm !== "") {
        showDialogue("你的输入内容存在问题，请尝试更正。")
        return;
    }
    isLoading.value = true
    axios.post('/api/student/register', {
        username: username.value,
        password: password.value,
    })
        .then(res => {
            if (res.data.status === 0) {
                store.commit('updateAuthState', {
                    authStat: true,
                    currUsername: username.value,
                    userGroup: 'student',
                    userRole: 'student'
                })
                isLoading.value = false
                const redirect = route.query.redirect as string
                if (redirect)
                    router.push(redirect)
                else
                    router.push("/")
            } else {
                isLoading.value = false
                showDialogue('注册失败。请检查你的用户名或密码。')
            }
        })
        .catch(err => {
            isLoading.value = false
            showDialogue(`发生了一些问题，目前你无法注册，请之后再试一次。<br>Error: ${err.message}`)
        })
}

function required(v: string) {
    return !!v || '需要填写这个字段。'
}

</script>
<style lang="scss" scoped>
.login_container {
    height: 100vh;
    overflow-y: auto;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.5s;
    position: relative;
    overflow: hidden;

    .frame {
        position: absolute;
        left: -5%;
        top: -5%;
        width: 110%;
        height: 110%;
        filter: blur(20px);
    }

    .group {
        display: flex;
        position: relative;
        z-index: 1;
        border-radius: 20px;
        overflow: hidden;

        .form {
            width: 360px;
            margin: 0 auto;
            height: 400px;
            padding: 40px 60px;

            .title {
                font-size: 36px;
                font-weight: 700;
                font-family: Roboto, sans-serif !important;
                margin-bottom: 20px;
            }
        }

        .desc {
            height: 100%;
            margin: 0 auto;
            width: 360px;
            background-image: linear-gradient(to bottom, #d4e5f5, #e1edf3);
            height: 400px;
            padding: 60px;
            text-align: center;

            .logo {
                text-align: center;
            }
        }
    }
}

@media only screen and (max-width: 778px) {
    .login_container {
        .group {
            .form {
                background: transparent;
            }

            .desc {
                display: none;
            }
        }
    }
}
</style>
