import {defineStore} from "pinia";

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            authStat: false,
            currUsername: '',
            userGroup: '',
            userRole: '',
        }
    },
    actions: {
        updateAuthState(options: { authStat: boolean; currUsername: string; userGroup: string; userRole: string; }) {
            this.authStat = options.authStat
            this.currUsername = options.currUsername
            this.userGroup = options.userGroup
            this.userRole = options.userRole
        },
        clearAuthState() {
            this.authStat = false
            this.currUsername = ''
            this.userGroup = ''
            this.userRole = ''
        }
    }
})
