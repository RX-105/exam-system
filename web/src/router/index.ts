import {createRouter, createWebHashHistory} from 'vue-router';
import Layout from '@/layout/layout.vue';
import type {RouteLocationNormalized} from "vue-router";

import {checkVersion} from '@/plugins/pwa';
import axios from "axios";
import {useUserStore} from "@/stores/userStore";

const router = createRouter({
    history: createWebHashHistory(),
    scrollBehavior() {
        return {top: 0};
    },
    routes: [
        {
          path: "/",
          redirect: "/student/dashboard",
          name: "root-route",
          meta: {
              visible: false,
              title: 'routes.exam_system',
              icon: 'mdi-gauge',
              requiredRole: ['any'],
          }
        },
        {
            path: '/student/dashboard',
            redirect: '/student/dashboard/info',
            name: 'student-dashboard',
            meta: {
                visible: true,
                title: 'routes.exam_system',
                icon: 'mdi-gauge',
                requiredRole: ['student'],
            },
            component: Layout,
            children: [
                {
                    path: '/student/dashboard/info',
                    name: 'student-info',
                    meta: {
                        title: 'routes.dashboard',
                        icon: 'mdi-home',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/dashboard.vue'),
                    children: [],
                },
                {
                    path: '/student/dashboard/register',
                    name: 'student-exam-register',
                    meta: {
                        title: 'routes.exam_register',
                        icon: 'mdi-file-document-plus',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/exam-register.vue'),
                    children: [],
                },
                {
                    path: '/student/dashboard/avatar',
                    name: 'student-upload-avatar',
                    meta: {
                        title: 'routes.upload_avatar',
                        icon: 'mdi-emoticon',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/upload-avatar.vue'),
                    children: [],
                },
                {
                    path: '/student/dashboard/register-form',
                    name: 'student-register-form-print',
                    meta: {
                        title: 'routes.register_form_print',
                        icon: 'mdi-format-list-numbered',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/register-form-print.vue'),
                    children: [],
                },
                {
                    path: '/student/dashboard/ticket-print',
                    name: 'student-ticket-print',
                    meta: {
                        title: 'routes.ticket_print',
                        icon: 'mdi-ticket-account',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/ticket-print.vue'),
                    children: [],
                },
            ],
        },
        {
            path: '/student/system',
            name: 'student-system',
            redirect: '/student/system/log',
            meta: {
                visible: true,
                title: 'routes.system_functionalities',
                icon: 'mdi-gauge',
                requiredRole: ['student'],
            },
            component: Layout,
            children: [
                {
                    path: '/student/system/log',
                    name: 'student-log',
                    meta: {
                        title: 'routes.action_log',
                        icon: 'mdi-account-lock',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/system-log.vue'),
                    children: [],
                },
            ],
        },
        {
            path: '/misc',
            name: 'misc',
            meta: {
                visible: true,
                title: 'routes.misc',
                icon: 'mdi-gauge',
                requiredRole: ['logged-any'],
            },
            component: Layout,
            children: [
                {
                    path: '/misc/credits',
                    name: 'misc-credits',
                    meta: {
                        title: 'routes.credits',
                        icon: 'mdi-star-face',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['logged-any'],
                    },
                    component: () => import('@/views/misc/credits.vue'),
                    children: [],
                },
            ],
        },
        {
            path: '/componets',
            name: 'componets',
            meta: {
                visible: true,
                title: 'Componets',
                icon: 'mdi-cube-scan',
            },
            component: Layout,
            children: [
                {
                    path: 'samples',
                    name: 'Samples',
                    meta: {
                        title: 'Samples',
                        icon: 'mdi-alpha-s',
                        keepAlive: false,
                        visible: true,
                    },
                    component: () => import('@/views/componets/sample.vue'),
                    children: [],
                },
                {
                    path: 'tables',
                    name: 'tables',
                    meta: {
                        title: 'Tables',
                        icon: 'mdi-alpha-t',
                        keepAlive: false,
                        visible: true,
                    },
                    component: () => import('@/views/componets/table.vue'),
                    children: [],
                },
            ],
        },
        {
            path: '/graphics',
            name: 'graphics',
            meta: {
                visible: true,
                title: 'Graphics',
                icon: 'mdi-image',
            },
            component: Layout,
            children: [
                {
                    path: 'three-js',
                    name: 'three.js',
                    meta: {
                        keepAlive: false,
                        title: 'Three.js',
                        icon: 'mdi-alpha-t',
                        visible: true,
                    },
                    component: () => import('@/views/graphics/threeJs.vue'),
                },
                {
                    path: 'babylonjs',
                    name: 'Babylon.js',
                    meta: {
                        keepAlive: false,
                        title: 'Babylon.js',
                        icon: 'mdi-alpha-b',
                        visible: true,
                    },
                    component: () => import('@/views/graphics/babylonjs.vue'),
                },
            ],
        },
        {
            path: '/low-code',
            name: 'LowCode',
            meta: {
                visible: true,
                title: 'Low Code',
                icon: 'mdi-view-module',
            },
            component: Layout,
            children: [
                {
                    path: 'layer-edit-example',
                    name: 'layerEdit',
                    meta: {
                        title: 'Layer Edit Example',
                        icon: 'mdi-alpha-l',
                        keepAlive: false,
                        visible: true,
                    },
                    component: () => import('@/views/low-code/layer-edit.vue'),
                    children: [],
                },
            ],
        },
        {
            path: '/map',
            name: 'map',
            meta: {
                visible: true,
                title: 'Map',
                icon: 'mdi-compass',
            },
            component: Layout,
            children: [
                {
                    path: 'amap',
                    name: 'amap',
                    meta: {
                        title: 'Amap',
                        icon: 'mdi-alpha-a',
                        keepAlive: false,
                        visible: true,
                    },
                    component: () => import('@/views/map/amap.vue'),
                    children: [],
                },
            ],
        },
        {
            path: '/login',
            name: 'login',
            meta: {
                title: 'routes.login',
                icon: 'mdi-shield-account',
                visible: false,
                requiredRole: ['any'],
            },
            component: () => import('@/views/login/login.vue'),
        },
        {
            path: '/login-admin',
            name: 'login-admin',
            meta: {
                title: 'routes.login_admin',
                icon: 'mdi-shield-account',
                visible: false,
                requiredRole: ['any'],
            },
            component: () => import('@/views/login/login-admin.vue')
        },
        {
            path: "/register",
            name: "register",
            meta: {
                title: 'routes.register',
                icon: 'mdi-shield-account',
                visible: false,
                requiredRole: ['any'],
            },
            component: () => import('@/views/login/register.vue')
        },
        {
            path: '/:pathMatch(.*)',
            name: 'Match',
            meta: {
                keepAlive: false,
                requiredRole: ['any'],
            },
            redirect: '/404'
        },
        {
            path: '/404',
            name: '404',
            meta: {
                keepAlive: false,
                title: 'routes.four_oh_four',
                icon: 'mdi-paw-off',
                visible: false,
                requiredRole: ['any'],
            },
            component: Layout,
            children: [
                {
                    path: '',
                    name: 'd404',
                    meta: {
                        title: 'routes.four_oh_four',
                        visible: false,
                        requiredRole: ['any'],
                    },
                    component: () => import('@/views/feedback/no.vue'),
                    children: [],
                },
            ],
        },
    ],
});


router.beforeEach((to, _from, next) => {
    const userStore = useUserStore()
    let authStat = userStore.authStat
    // 总是排除any权限的路由
    if (routeAlwaysAccessible(to)) {
        next()
    } else if (authStat) {
        // 当前已经登录，鉴权
        if (routeLoggedAccessible(to)) {
            next()
        } else {
            alert('不允许访问这个页面。')
        }
    } else {
        axios.get("/api/credential", )
            .then(res => {
                if (res.data.status === 0) {
                    // 从服务器获取到登录状态，保存到状态管理
                    userStore.updateAuthState({
                        authStat: true,
                        currUsername: res.data.data.username,
                        userGroup: res.data.data.userGroup,
                        userRole: res.data.data.userRole,
                    })
                    if (routeLoggedAccessible(to)) {
                        next()
                    } else {
                        alert('不允许访问这个页面。')
                    }
                } else {
                    // 仍然没有登录状态
                    if (routeAlwaysAccessible(to)) {
                        next()
                    } else {
                        next({
                            name: 'login',
                            query: {
                                redirect: to.path
                            }
                        })
                    }
                }
            })
            .catch(err => {
                console.error(`failed verifying identity: ${err.message}`)
            })
    }
})

router.afterEach(() => {
    checkVersion();
});

// 判断当前路由是否是登录状态下可访问的公开路由，或者是
// 当前权限下可以访问的路由。需要验证登陆状态
const routeLoggedAccessible = (item: RouteLocationNormalized | any) => {
    const userStore = useUserStore()
    return item.meta.requiredRole
        && (item.meta.requiredRole.includes(userStore.userRole)
            || item.meta.requiredRole.includes('logged-any')
            || item.meta.requiredRole.includes('any')
        )
}

// 判断当前路由是否是不需要任何验证就可以访问的路由
// 不验证登陆状态
const routeAlwaysAccessible = (item: RouteLocationNormalized | any) => {
    return item.meta.requiredRole
        && item.meta.requiredRole.includes('any')
}

export {router, routeLoggedAccessible};
