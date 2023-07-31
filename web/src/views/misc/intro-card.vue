<template>
    <v-card>
        <v-card-title>{{title}}</v-card-title>
        <v-card-subtitle>{{subtitle}}</v-card-subtitle>
        <v-card-text>
            <v-row>
                <v-col cols="8">
                    <slot></slot>
                </v-col>
                <v-col cols="4">
                    <v-img :src="imgSrc"></v-img>
                </v-col>
            </v-row>
        </v-card-text>
        <v-card-actions>
            <v-btn color="primary"
                   @click="openNewTab">
                查看
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script lang="ts" setup>
const props = defineProps({
    title: String,
    subtitle: String,
    imgSrc: String,
    refSrc: String
})

function openNewTab() {
    window.open(props.refSrc, '_blank')
}
</script>

<style scoped>

</style>
