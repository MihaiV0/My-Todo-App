<script setup lang="ts">
import { transform } from 'typescript';
import { computed } from 'vue';

interface Message {
    message: string,
    username: string,
    dateTime: string,
}

const mProps = defineProps({
    messages: {
        required: true,
        type: Array<Message>
    }
})

function replaceUnderscoresWithSpaces(nameWithUnderscores: String) {
    const nameParts = nameWithUnderscores.split('_');
    let nameWithSpaces = nameParts[0];
    for (let i = 1; i < nameParts.length; i++) {
        nameWithSpaces += ' ';
        nameWithSpaces += nameParts[i];
    }
    return nameWithSpaces;
}

const mCurrentUser = computed(() => replaceUnderscoresWithSpaces(localStorage.getItem('username') || ''))
</script>

<template>
    <div id="chat-container">
        <div id="message-container">
            <div 
                class="message" 
                v-for="message in messages" 
                :style="{ backgroundColor: mCurrentUser == message.username ? 'var(--main-color)' : '',
                        color: mCurrentUser == message.username ? 'white' : '',
                        transform: mCurrentUser == message.username ? 'translateX(95%)' : '' }">
                <div
                    style="display: flex; align-items: center;"
                >
                    <span class="material-symbols-outlined">
                        face
                    </span>
                    {{ message.username }}
                </div>
                <div style="text-align: start;">
                    {{ message.message }}
                </div>
                <div
                    style="font-size: 14px;"
                >
                    {{ message.dateTime }}
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    #chat-container {
        width: calc(60vw - 2 * var(--todo-panel-border-right-width));
        background-color: white;
    }

    #message-container {
        display: flex;
        flex-direction: column;
        gap: 1vh;
        overflow: auto;
        transform: translateY(4%);
    }

    .message {
        display: flex;
        flex-direction: column;
        background-color: var(--green-secondary-color);
        color: var(--main-color);
        transform: translateX(1%);
        width: 50%;
        padding: 5px;
        border-radius: 10px;
        gap: 0.5vh;
        text-align: end;
    }
</style>