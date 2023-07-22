<template>
    <v-dialog
        v-model="dialogueState"
        width="auto">
        <v-card>
            <v-card-text>
                {{ dialogueText }}
            </v-card-text>
            <v-card-actions>
                <v-btn color="primary" block @click="resetDialogue">好</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script lang="ts" setup>

import {ref} from "vue";

const dialogueState = ref(false)
const dialogueText = ref('')
function showDialogue(s: string) {
    dialogueText.value = s
    dialogueState.value = true
}
function resetDialogue() {
    dialogueState.value = false
    setTimeout(() => {
        dialogueText.value = ''
    }, 500)
}

defineExpose({
    showDialogue
})
</script>

<style scoped>

</style>
