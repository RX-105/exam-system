import {defineStore} from "pinia";

export const useAppStore = defineStore('app', {
    state: () => {
        return {
            dialogueState: false,
            dialogueHtml: '',
            snackbarState: false,
            snackbarText: '',
            loadingState: false,
        }
    },
    actions: {
        showDialogue(contentHtml: string) {
            this.dialogueState = true
            this.dialogueHtml = contentHtml
        },
        closeDialogue() {
            this.dialogueState = false
            setTimeout(() => {
                this.dialogueHtml = ''
            }, 500)
        },
        showSnackbar(contentText: string) {
            this.snackbarState = true
            this.snackbarText = contentText
        },
        closeSnackbar() {
            this.snackbarState = false
            setTimeout(() => {
                this.snackbarText = ''
            }, 500)
        },
        showLoading() {
            this.loadingState = true
        },
        stopLoading() {
            this.loadingState = false
        }
    }
})
