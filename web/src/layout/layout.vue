<template>
    <v-layout
        :class="{
            isMini: navState.isMini,
            isMobile: mainStore.isMobile,
        }"
    >
        <!--左侧导航栏-->
        <v-navigation-drawer
            class="my-4 layout_navigation"
            :rail="navState.rail"
            expand-on-hover
            rail-width="77"
            @update:rail="navigationRail"
            :permanent="permanent"
            v-model="navState.menuVisible"
            style="position: fixed"
        >
            <!--logo and title-->
            <v-list class="py-4 mx-2 logo" nav>
                <v-list-item :prepend-avatar="logo" class="mx-1">
                    <v-list-item-title class="title">Material UI</v-list-item-title>
                    <v-list-item-subtitle>vue-material-admin</v-list-item-subtitle>
                </v-list-item>
            </v-list>
            <!--logo and title-->
            <v-divider></v-divider>
            <template v-for="(item, key) in navState.routes" :key="key">
                <v-list nav class="mx-2"
                        v-if="routeLoggedAccessible(item) && item.meta.visible"
                        style="padding: 10px">
                    <v-list-item-subtitle style="padding-top: 10px;padding-bottom: 10px">{{t(item.meta.title)}}</v-list-item-subtitle>
                    <v-list-item
                        v-for="(i1, k1) in item.children" :key="k1"
                        :prepend-icon="i1.meta.icon"
                        :title="t(i1.meta.title)"
                        :to="{ name: i1.name }"
                        class="mx-1"
                        active-class="nav_active">
                    </v-list-item>
                </v-list>
            </template>
        </v-navigation-drawer>
        <!--左侧导航栏-->
        <main class="app_main">
            <header class="header">
                <Breadcrumbs v-if="!mainStore.isMobile" />
                <div v-if="!mainStore.isMobile" class="mt-3 ml-9 gamepad" @click="changeRail">
                    <v-icon v-if="navState.rail" icon="mdi-sort-variant-lock-open" />
                    <v-icon v-else icon="mdi-sort-variant" />
                </div>
                <div v-if="mainStore.isMobile" class="head_logo ml-4 mr-1">
                    <img :src="logo" height="40" />
                </div>
                <v-btn
                    v-if="mainStore.isMobile"
                    variant="text"
                    icon="mdi-menu"
                    @click="navState.menuVisible = !navState.menuVisible"
                >
                    <v-icon size="small"></v-icon>
                </v-btn>
                <v-spacer></v-spacer>
                <div v-if="!mainStore.isMobile" style="width: 220px" class="search_ip mr-2">
                    <!-- <div id="docsearch"></div> -->
                    <v-text-field
                        rounded
                        density="compact"
                        variant="outlined"
                        label="Search here"
                        prepend-inner-icon="mdi-magnify"
                        single-line
                        hide-details
                        clearable
                    ></v-text-field>
                </div>
                <div class="tool_btns">
                    <v-btn
                        @click="mainStore.onTheme"
                        variant="text"
                        :icon="
                            mainStore.theme === 'light' ? 'mdi-weather-sunny' : 'mdi-weather-night'
                        "
                    />
                    <v-btn variant="text" prepend-icon="mdi-earth"
                           append-icon="mdi-chevron-down" class="mr-2">
                        语言
                        <v-menu activator="parent">
                            <v-list>
                                <v-list-item
                                    v-for="locale in locales"
                                    :key="locale.locale"
                                    :value="locale.locale"
                                    @click="updateLocale(locale.locale)"
                                >
                                    <v-list-item-title>{{ locale.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-btn>
                    <v-btn variant="text" append-icon="mdi-chevron-down" class="mr-2">
                        <v-avatar size="x-small" class="avatar mr-2">
                            <v-img :src="wxtx" alt="陈咩啊"></v-img>
                        </v-avatar>
                        <span v-if="!mainStore.isMobile">{{ userStore.currUsername }}</span>
                        <v-menu activator="parent">
                            <v-list nav class="h_a_menu">
                                <v-list-item
                                    title="Github"
                                    prepend-icon="mdi-github"
                                    @click="toGithub"
                                />
                                <v-list-item
                                    title="Email"
                                    prepend-icon="mdi-email"
                                    @click="toEmail"
                                />
                                <v-list-item
                                    title="退出登录"
                                    prepend-icon="mdi-login"
                                    @click="signOut"
                                />
                            </v-list>
                        </v-menu>
                    </v-btn>
                </div>
                <div style="position: fixed; right: 20px; bottom: 100px; z-index: 99999">
                    <v-btn icon="mdi-cog" />
                </div>
            </header>
            <div class="router"><RouterView /></div>
        </main>
    </v-layout>
</template>
<script setup lang="ts">
import logo from '@/assets/admin-logo.png';
import wxtx from '@/assets/wx.png';
import {RouterView, useRouter} from 'vue-router';
import Breadcrumbs from '@/components/breadcrumbs/breadcrumbs.vue';
import {reactive, computed, watch, ref} from 'vue';
import { useMainStore } from '@/stores/appMain';
import axios from "axios";
import {routeLoggedAccessible} from "@/router";
import {useLocale} from "vuetify";
import {useUserStore} from "@/stores/userStore";

const {current, t} = useLocale()

const mainStore = useMainStore();
const userStore = useUserStore()
const router = useRouter();
const navState = reactive({
    menuVisible: true,
    rail: mainStore.isMobile,
    isMini: mainStore.isMobile,
    routes: router.options.routes,
});
const permanent = computed(() => {
    return !mainStore.isMobile;
});
const locales = ref([
    {name: 'English', locale: 'en'},
    {name: '简体中文', locale: 'zh'},
])

watch(permanent, () => {
    navState.menuVisible = true;
    changeRail();
});
const navigationRail = (e: boolean) => {
    if (!navState.rail) return;
    navState.isMini = e;
};

const changeRail = () => {
    navState.rail = !navState.rail;
    navState.isMini = navState.rail;
};

const toGithub = () => {
    window.open('https://github.com/jaywoow/vue-material-admin', '_blank');
};
const toEmail = () => {
    window.open('mailto:894620576@qq.com', '_blank');
};

const signOut = () => {
    axios.post("/api/logout")
        .then(res => {
            if (res.data.status === 0) {
                router.push("/login")
            }
        })
    userStore.clearAuthState()
}

const updateLocale = (locale: string) => {
    current.value = locale
}
</script>
<style scoped lang="scss"></style>
