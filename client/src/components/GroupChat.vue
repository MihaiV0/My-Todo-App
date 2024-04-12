<script setup lang="ts">
import { computed, ref } from 'vue';

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

const mMessageText = ref('');

function replaceUnderscoresWithSpaces(nameWithUnderscores: String) {
    const nameParts = nameWithUnderscores.split('_');
    let nameWithSpaces = nameParts[0];
    for (let i = 1; i < nameParts.length; i++) {
        nameWithSpaces += ' ';
        nameWithSpaces += nameParts[i];
    }
    return nameWithSpaces;
}

const mEmits = defineEmits(['send-message'])

const mCurrentUser = computed(() => replaceUnderscoresWithSpaces(localStorage.getItem('username') || ''))

function sendMessage() {
    if (mMessageText.value) {
        mEmits('send-message', mMessageText.value, mCurrentUser.value)
        mMessageText.value = ''
    }
}

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
                <div 
                    style="text-align: start;"
                    v-html="message.message"
                >
                </div>
                <div
                    style="font-size: 14px;"
                >
                    {{ message.dateTime }}
                </div>
            </div>
        </div>
        <div id="input-container">
            <input 
                type="text"
                placeholder="Message"
                v-model="mMessageText"
                @keydown.enter="sendMessage"
            >
            <span 
                class="material-symbols-outlined"
                id="send-btn"
                @click="sendMessage"
            >
                send
            </span>
        </div>
    </div>
</template>

<style scoped>
    #chat-container {
        width: calc(60vw - 2 * var(--todo-panel-border-right-width));
        background-color: white;
        display: flex;
        flex-direction: column;
        gap: 1vh;
    }
    
    #send-btn {
        background-color: var(--main-color);
        color: white;
        padding: 10px;
        border-radius: 10px;
    }

    #send-btn:hover {
        cursor: pointer;
    }

    #message-container {
        display: flex;
        flex-direction: column;
        gap: 1vh;
        overflow: auto;
        height: 90%;
        margin: 1vh 0;
    }

    #input-container {
        height: 10%;
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 0.5vw;
        background-color: var(--grey-secondary-color);
    }

    input {
        outline: none;
        border: none;
        border-radius: 30px;
        padding: 10px 15px;
        font-size: var(--default-font-size);
        width: 80%;
        border: 1px solid transparent;
        color: var(--main-color);
    }

    input:focus {
        border-color: var(--main-color);
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