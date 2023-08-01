import { createI18n, useI18n } from 'vue-i18n'
import {translation} from "@/i18n/translation";
import {createVueI18nAdapter} from "vuetify/locale/adapters/vue-i18n";

const i18n = createI18n({
    legacy: false,
    locale: 'zh',
    fallbackLocale: 'en',
    messages: translation
})

const adaptor = createVueI18nAdapter({
    i18n, useI18n
})

export { i18n, adaptor }
