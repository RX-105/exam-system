import {createStore} from "vuex";

const store = createStore({
    state () {
        return {
            authStat: false,
            currUsername: '',
            userGroup: '',
            userRole: '',

            dialogueState: false,
            dialogueHtml: '',

            snackbarState: false,
            snackbarText: '',
        }
    },
    mutations: {
        updateAuthState(state, options) {
            state.authStat = options.authStat
            state.currUsername = options.currUsername
            state.userGroup = options.userGroup
            state.userRole = options.userRole
        },
        clearAuthState(state) {
            state.authStat = false
            state.currUsername = ''
            state.userGroup = ''
            state.userRole = ''
        },
        showDialogue(state, contentHtml) {
            state.dialogueState = true
            state.dialogueHtml = contentHtml
        },
        closeDialogue(state) {
            state.dialogueState = false
            setTimeout(() => {
                state.dialogueHtml = ''
            }, 500)
        },
        showSnackbar(state, contentText) {
            state.snackbarState = true
            state.snackbarText = contentText
        },
        closeSnackbar(state) {
            state.snackbarState = false
            setTimeout(() => {
                state.snackbarText = ''
            }, 500)
        }
    },
    getters: {
        authStat(state) {
            return state.authStat
        },
        userInfo(state) {
            return {
                currUsername: state.currUsername,
                userGroup: state.userGroup,
                userRole: state.userRole,
            }
        },
        dialogueHtml(state) {
            return state.dialogueHtml
        },
        dialogueState(state) {
            return state.dialogueState
        },
        snackbarText(state) {
            return state.snackbarText
        },
        snackbarState(state) {
            return state.snackbarState
        }
    }
})

export default store
