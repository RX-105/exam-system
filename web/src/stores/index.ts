import {createStore} from "vuex";

const store = createStore({
    state () {
        return {
            authStat: false,
            currUsername: ''
        }
    },
    mutations: {
        updateAuthState(state, options) {
            state.authStat = options.authStat
            state.currUsername = options.currUsername
        }
    },
    getters: {
        authStat(state) {
            return state.authStat
        }
    }
})

export default store
