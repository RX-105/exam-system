import {defineStore} from "pinia";
import axios from "axios";

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            authStat: false,
            currUsername: '',
            userGroup: '',
            userRole: '',
            userAvatar: axios.defaults.baseURL + '/api/student/avatar?refresh=1',
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
        },
        refreshUserAvatar() {
            this.userAvatar = `${this.userAvatar}0`
        },
    }
})
