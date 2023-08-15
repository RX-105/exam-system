<template>
    <v-app :theme="mainStore.theme">
        <v-progress-linear :indeterminate="appStore.loadingState" color="primary"
                           style="position: fixed;z-index: 999">
        </v-progress-linear>
        <router-view />
        <v-snackbar location="bottom right" vertical v-model="mainStore.upVisible" :timeout="-1">
            <p>New content available, click on reload button to update</p>

            <template v-slot:actions>
                <v-btn color="blue" @click="mainStore.onUp"> Reload </v-btn>
                <v-btn color="blue" variant="text" @click="mainStore.onHideUp"> Close </v-btn>
            </template>
        </v-snackbar>
        <!--全局信息提示对话框-->
        <v-dialog
            v-model="appStore.dialogueState"
            width="auto">
            <v-card>
                <v-card-title>{{ t('app.reminder') }}</v-card-title>
                <v-card-text v-html="appStore.dialogueHtml"></v-card-text>
                <v-card-actions>
                    <v-btn color="primary" block @click="appStore.closeDialogue">{{t('app.ok')}}</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <!--全局信息提示snackbar-->
        <v-snackbar v-model="appStore.snackbarState" timeout="2000">
            {{ appStore.snackbarText }}
            <template v-slot:actions>
                <v-btn
                    color="primary"
                    variant="text"
                    @click="appStore.closeSnackbar()">
                    {{t('app.close')}}
                </v-btn>
            </template>
        </v-snackbar>
    </v-app>
</template>
<script setup lang="ts">
import { RouterView } from 'vue-router';
import { useMainStore } from '@/stores/appMain';
import {useLocale} from "vuetify";
import {useAppStore} from "@/stores/appStore";

const { t } = useLocale()

const mainStore = useMainStore();
const appStore = useAppStore()

</script>
<style scoped lang="scss"></style>
