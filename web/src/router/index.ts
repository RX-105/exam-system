import {createRouter, createWebHashHistory} from 'vue-router';
import Layout from '@/layout/layout.vue';
import store from '@/stores/index'

import {checkVersion} from '@/plugins/pwa';

const router = createRouter({
    history: createWebHashHistory(),
    scrollBehavior() {
        return {top: 0};
    },
    routes: [
        {
            path: '/',
            redirect: '/student/dashboard',
            name: 'student-dashboard',
            meta: {
                visible: true,
                title: '考务系统',
                icon: 'mdi-gauge',
                requiredRole: ['student'],
            },
            component: Layout,
            children: [
                {
                    path: '/student/dashboard',
                    name: 'student-dashboard',
                    meta: {
                        title: '首页',
                        icon: 'mdi-home',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/dashboard.vue'),
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
                title: '系统功能',
                icon: 'mdi-gauge',
                requiredRole: ['student'],
            },
            component: Layout,
            children: [
                {
                    path: '/student/system/log',
                    name: 'student-log',
                    meta: {
                        title: '行为日志',
                        icon: 'mdi-account-lock',
                        keepAlive: false,
                        visible: true,
                        requiredRole: ['student'],
                    },
                    component: () => import('@/views/student/system.vue'),
                    children: [],
                },
            ],
        },
        {
            path: '/misc',
            name: 'misc',
            meta: {
                visible: true,
                title: '其他',
                icon: 'mdi-gauge',
                requiredRole: ['logged-any'],
            },
            component: Layout,
            children: [
                {
                    path: '/misc/credits',
                    name: 'misc-credits',
                    meta: {
                        title: '致谢',
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
                title: 'Login',
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
                title: 'login-admin',
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
                title: 'register',
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
                title: 'Not found',
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
                        title: 'Not found',
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

router.beforeEach(async (to, _from, next) => {
    next();
});

router.beforeEach((to, from, next) => {
    const userInfo = store.getters.userInfo
    if (to.path.includes('login') || to.path.includes('register') || to.path.includes('404')) {
        next()
    } else if (to.path.includes('student')) {
        if (userInfo.userRole === 'student') {
            next()
        } else {
            next({
                name: 'login',
                query: {
                    redirect: to.path
                }
            })
        }
    } else if (to.path.includes('admin')) {
        if (userInfo.userRole === 'admin') {
            next()
        } else {
            next({
                name: 'login',
                query: {
                    redirect: to.path
                }
            })
        }
    } else {
        alert('不允许访问这个页面。')
    }
})

router.afterEach(() => {
    checkVersion();
});
export default router;
