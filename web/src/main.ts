import { createApp } from 'vue';
import { createPinia } from 'pinia';
import './styles/index.scss';
import App from './App.vue';
import {router} from "@/router";
import { vuetify } from '@/plugins/vuetify';
import '@/plugins/pwa';
import axios from "axios";
import {i18n} from "@/i18n";

// axios configuration
axios.defaults.baseURL = '//localhost'
axios.defaults.headers.post['Content-Type'] = 'multipart/form-data'
axios.defaults.withCredentials = true

const app = createApp(App);

app.use(createPinia());
app.use(i18n)
app.use(vuetify);
app.use(router);
app.mount('#app');
