<template>
    <v-container>
        <v-card>
            <v-card-title>{{ t('upload_avatar.upload_avatar_title') }}</v-card-title>
            <v-card-subtitle>{{ t('upload_avatar.upload_avatar_subtitle') }}</v-card-subtitle>
            <v-card-text>
                <v-row style="margin: 5px">
                    <div>
                        <div class="text-item">{{ t('upload_avatar.upload_avatar_text_1') }}</div>
                        <div class="text-item">{{ t('upload_avatar.upload_avatar_text_2') }}</div>
                        <div class="text-item">{{ t('upload_avatar.upload_avatar_text_3') }}</div>
                        <div class="text-item">{{ t('upload_avatar.upload_avatar_text_4') }}</div>
                    </div>
                </v-row>
                <v-row>
                    <v-col cols="4"></v-col>
                    <v-col cols="4">
                        <v-img
                            :src="userStore.userAvatar"
                            aspect-ratio="5:7"
                            height="200"/>
                    </v-col>
                    <v-col cols="4"></v-col>
                </v-row>
                <v-row>
                    <v-col cols="3"></v-col>
                    <v-col cols="6">
                        <v-file-input
                            v-model="avatarFile"
                            :label="t('upload_avatar.label_upload_avatar')"
                            accept="image/*"
                            counter
                            show-size
                            :rules="[ruleCheckAvatarSize]"
                            prepend-icon="mdi-camera"/>
                    </v-col>
                    <v-col cols="3"></v-col>
                </v-row>
            </v-card-text>
            <v-card-actions>
                <v-btn color="primary" @click="uploadAvatar">{{ t('upload_avatar.upload') }}</v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>
<script setup lang="ts">
import {useLocale} from "vuetify";
import {ref} from "vue";
import axios from "axios";
import {useAppStore} from "@/stores/appStore";
import {useUserStore} from "@/stores/userStore";

const { t } = useLocale()
const { showDialogue, showSnackbar } = useAppStore()
const userStore = useUserStore()

const avatarFile = ref<File[]>([])

const uploadAvatar = () => {
    if (avatarFile.value.length === 0) {
        showDialogue(t('upload_avatar.file_is_empty'))
        return
    } else {
        if (avatarFile.value[0].size > 5000000) {
            showDialogue(
                `${t('upload_avatar.file_size_exceeded_1')}
                ${(avatarFile.value[0].size/(1000*1000)).toFixed(1)}
                ${t('upload_avatar.file_size_exceeded_2')}`
            )
            return
        }
        axios.post("/api/student/avatar", {
            file: avatarFile.value[0]
        })
            .then(res => {
                const r = res.data as R
                if (r.status === 0) {
                    userStore.refreshUserAvatar()
                    showSnackbar(t('upload_avatar.upload_complete'))
                } else {
                    showSnackbar(`${t('upload_avatar.bad_response')}${r.status}`)
                }
            })
            .catch(err => {
                showSnackbar(`${t('upload_avatar.request_error')}${err.message}`)
            })
    }
}

const ruleCheckAvatarSize = (value: any) => {
    return !value || !value.length || value[0].size < 5000000 || t('upload_avatar.prompt_file_size_limit')
}

</script>
<style lang="scss">
.text-item {
    margin: 1vh 0;
    text-indent: -1.5em;
    padding: 0 0 0 1.5em;
}
</style>
