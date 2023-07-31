<template>
    <v-row>
        <v-col cols="6">
            <v-card>
                <v-card-title>后端技术栈</v-card-title>
                <v-card-text>
                    <intro-card
                        title="SpringBoot"
                        subtitle="应用程序框架"
                        img-src="https://spring.io/img/logos/spring-initializr.svg"
                        ref-src="https://spring.io/projects/spring-boot">
                        本项目的后端服务器基于SpringBoot和Tomcat开发。SpringBoot是一个开源JavaWeb框架，
                        提供了一系列开箱即用的特性，包括自动配置、快速开发、基于注解的配置等，帮助开发者快速构建
                        可靠、高效的企业级应用程序。
                    </intro-card>

                    <intro-card
                        title="Hibernate"
                        subtitle="对象关系映射框架"
                        img-src="https://hibernate.org/images/hibernate-logo.svg"
                        ref-src="https://hibernate.org/orm/">
                        本项目使用spring-data-jpa，并使用Hibernate作为Jakarta持久化API的实现。
                        Hibernate提供了对象与数据库之间的映射，允许开发者不编写SQL语句就实现对数据库
                        的增删改查操作，能提高开发效率。
                    </intro-card>

                    <intro-card
                        title="MySQL"
                        subtitle="关系型数据库"
                        img-src="https://labs.mysql.com/common/logos/mysql-logo.svg?v2"
                        ref-src="https://www.mysql.com/">
                        本项目的系统数据使用MySQL存储。MySQL是一种流行的开源关系型数据库管理系统，
                        广泛用于Web应用程序和大规模企业级应用。它支持多种操作系统，提供高性能、
                        可靠性和安全性，被广泛应用于数据存储、管理和查询。
                    </intro-card>

                    <intro-card
                        title="Thymeleaf"
                        subtitle="页面模板引擎"
                        img-src="https://www.thymeleaf.org/images/thymeleaf.png"
                        ref-src="https://www.thymeleaf.org/">
                        本项目的服务器端渲染（SSR）部分采用了Thymeleaf和Spring MVC共同实现。
                        Thymeleaf是一种流行的Java模板引擎，用于在Web应用程序中生成动态内容。
                        它采用标准的HTML模板，允许使用表达式和属性来填充数据。Thymeleaf易于使用，
                        与Spring MVC框架配合良好。
                    </intro-card>
                </v-card-text>
            </v-card>
        </v-col>
        <v-col cols="6">
            <v-card>
                <v-card-title>前端技术栈</v-card-title>
                <v-card-text>
                    <intro-card
                        title="Vue.js"
                        subtitle="网页应用程序框架"
                        img-src="https://router.vuejs.org/logo.svg"
                        ref-src="https://vuejs.org/">
                        本项目的单页面应用（SPA）部分使用Vue.js来实现。Vue.js是一个用于创建用户
                        界面的开源MVVM前端渐进式JavaScript框架，也是一个创建单页应用的Web应用框架。
                        它简单易学，提供了响应式数据绑定和组件化开发，使得构建交互式用户界面变得更加 简单和高效。
                    </intro-card>

                    <intro-card
                        title="Vuex/Pinia"
                        subtitle="状态管理模式和库"
                        img-src="https://pinia.vuejs.org/logo.svg"
                        ref-src="https://pinia.vuejs.org/">
                        本项目同时使用Vuex和Pinia作为状态管理实现。Vuex 是一个专为 Vue.js
                        应用程序开发的状态管理模式和库。它采用集中式存储管理应用的所有组件的状态，
                        并以相应的规则保证状态以一种可预测的方式发生变化。Pinia是和Vuex类似的另一个
                        状态管理实现。
                    </intro-card>

                    <intro-card
                        title="Vue Router"
                        subtitle="路由管理"
                        img-src="https://router.vuejs.org/logo.svg"
                        ref-src="https://router.vuejs.org/zh/">
                        本项目的路由能力使用的是Vue Router。Vue Router是Vue.js官方的路由管理器，
                        用于在Vue应用中实现前端路由。它能帮助开发者构建单页应用（SPA）并实现页面之间
                        的无刷新跳转，提供了路由配置、路由跳转、嵌套路由等功能，使得Vue应用的路由管理
                        变得简单而高效。
                    </intro-card>

                    <intro-card
                        title="Vuetify"
                        subtitle="样式/组件库"
                        img-src="https://cdn.vuetifyjs.com/docs/images/logos/vuetify-logo-v3-light.svg"
                        ref-src="https://vuetifyjs.com/en/">
                        本项目使用了Vuetify作为项目的样式和组件库。Vuetify是一个基于Vue.js
                        开发的样式和组件库，提供丰富的可重用组件和预定义样式，帮助开发者快速构建
                        现代化、响应式的Web应用程序。它采用了Material Design风格，使得界面
                        设计简单、美观，并具有良好的可定制性和易用性。
                    </intro-card>

                    <intro-card
                        title="Vue Material Admin"
                        subtitle="中后台模版"
                        :img-src="adminLogo"
                        ref-src="https://github.com/armomu/vue-material-admin">
                        本项目基于Vue Material Admin二次开发。Vue Material Admin 是一个基于 Vuetify 免费开源的纯前端中后台模版。
                    </intro-card>
                </v-card-text>
            </v-card>
        </v-col>
    </v-row>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useStore} from "vuex";
import IntroCard from "@/views/misc/intro-card.vue";
import adminLogo from '@/assets/admin-logo.png'

const registrationInfo = ref<RegistrationData>({
    stageMap: {},
    schoolName: '',
    stages: [],
})
const store = useStore()

onMounted(() => {
    axios.get("/api/student/registration-notice")
        .then(res => {
            if (res.data.status === 0) {
                registrationInfo.value = res.data.data as RegistrationData
            }
        })
        .catch(err => {
            store.commit("showDialogue", `加载学校资讯过程中出现了问题。<br/>Error: ${err.message}`)
        })
})

function openNewTab(addr: string) {
    window.open(addr, '_blank')
}
</script>
<style lang="scss">
.section-container {
    margin: 10px;
}
</style>
