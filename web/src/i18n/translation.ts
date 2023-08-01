import { en, zhHans } from 'vuetify/locale'
import systemLog_en from "@/i18n/translations/en/SystemLog_en.json";
import systemLog_zh from '@/i18n/translations/zh/SystemLog_zh.json'
import app_en from "@/i18n/translations/en/App_en.json";
import app_zh from "@/i18n/translations/zh/App_zh.json";
import routes_en from '@/i18n/translations/en/Routes_en.json'
import routes_zh from '@/i18n/translations/zh/Routes_zh.json'

const translation = {
    en: {
        $vuetify: en,
        systemLog: systemLog_en,
        app: app_en,
        routes: routes_en
    },
    zh: {
        $vuetify: zhHans,
        systemLog: systemLog_zh,
        app: app_zh,
        routes: routes_zh
    }
}

export { translation }
