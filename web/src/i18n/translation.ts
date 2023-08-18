import { en, zhHans } from 'vuetify/locale'
import system_log_en from "@/i18n/translations/en/SystemLog_en.json";
import system_log_zh from '@/i18n/translations/zh/SystemLog_zh.json'
import app_en from "@/i18n/translations/en/App_en.json";
import app_zh from "@/i18n/translations/zh/App_zh.json";
import routes_en from '@/i18n/translations/en/Routes_en.json'
import routes_zh from '@/i18n/translations/zh/Routes_zh.json'
import dashboard_en from '@/i18n/translations/en/Dashboard_en.json'
import dashboard_zh from '@/i18n/translations/zh/Dashboard_zh.json'
import exam_register_en from '@/i18n/translations/en/ExamRegister_en.json'
import exam_register_zh from '@/i18n/translations/zh/ExamRegister_zh.json'
import upload_avatar_en from '@/i18n/translations/en/UploadAvatar_en.json'
import upload_avatar_zh from '@/i18n/translations/zh/UploadAvatar_zh.json'

const translation = {
    en: {
        $vuetify: en,
        system_log: system_log_en,
        app: app_en,
        routes: routes_en,
        dashboard: dashboard_en,
        exam_register: exam_register_en,
        upload_avatar: upload_avatar_en,
    },
    zh: {
        $vuetify: zhHans,
        system_log: system_log_zh,
        app: app_zh,
        routes: routes_zh,
        dashboard: dashboard_zh,
        exam_register: exam_register_zh,
        upload_avatar: upload_avatar_zh,
    }
}

export { translation }
