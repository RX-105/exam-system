<template>
    <v-app :theme="mainStore.theme">
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
            v-model="store.getters.dialogueState"
            width="auto">
            <v-card>
                <v-card-text v-html="store.getters.dialogueHtml"></v-card-text>
                <v-card-actions>
                    <v-btn color="primary" block @click="store.commit('closeDialogue')">好</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <!--全局信息提示snackbar-->
        <v-snackbar v-model="store.getters.snackbarState">
            {{ store.getters.snackbarText }}
            <template v-slot:actions>
                <v-btn
                    color="primary"
                    variant="text"
                    @click="store.commit('closeSnackbar')">
                    关闭
                </v-btn>
            </template>
        </v-snackbar>
    </v-app>
</template>
<script setup lang="ts">
import { RouterView } from 'vue-router';
import { useMainStore } from '@/stores/appMain';
import {useStore} from "vuex";

const mainStore = useMainStore();
const store = useStore()

</script>
<style scoped lang="scss"></style>
