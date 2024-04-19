<script setup lang="ts">
import CustomInput from '@/components/CustomInput.vue'
import TodoField from '@/components/TodoField.vue'
import FixedSizeSpan from '@/components/FixedSizeSpan.vue'
import { ref, watch, onMounted } from 'vue'
import CustomButton from '@/components/CustomButton.vue'
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue'
import { updateUser } from '@/data/server'

const mEmail = ref('')
const mUsername = ref('')
const mErrorMessage = ref('')
const mShowErrorPopup = ref(false)
const mShowSaveToaster = ref(false)

function emailChanged(newEmail: string) {
    mEmail.value = newEmail
}

function usernameChanged(newUsername: string) {
    mUsername.value = newUsername
}

function closeErrorPopup() {
    mShowErrorPopup.value = false;
}

onMounted(() => {
    reoadUserData()
})

async function save() {
    let inputOK = true

    const usernameRegex = /^[a-zA-Z0-9_]+$/

    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/

    if (!mUsername.value ||
        !mEmail.value) {

        inputOK = false
        mErrorMessage.value = 'All fields are mandatory'
        mShowErrorPopup.value = true
    }

    if (inputOK && !usernameRegex.test(mUsername.value)) {
        inputOK = false
        mErrorMessage.value = 'Username must contain only letters, numbers or underscores(_)'
        mShowErrorPopup.value = true
    }

    if (inputOK && !emailRegex.test(mEmail.value)) {
        inputOK = false
        mErrorMessage.value = 'Invalid email address'
        mShowErrorPopup.value = true
    }

    if (inputOK) {
        updateUser(localStorage.getItem('username') || '', mUsername.value, mEmail.value)
            .then(res => {
                localStorage.setItem('username', mUsername.value)
                localStorage.setItem('email', mEmail.value)
                mShowSaveToaster.value = true
                setTimeout(() => {
                    mShowSaveToaster.value = false
                }, 2000)
            })
            .catch(err => {
                if (err.message.includes('Email')) {
                    mErrorMessage.value = `Email ${mEmail.value} is already used`
                } else if (err.message.includes('Username')) {
                    mErrorMessage.value = `Username ${mUsername.value} is already used`
                } else {
                    mErrorMessage.value = err.message
                }
                mShowErrorPopup.value = true
            });
    }
}

function reoadUserData() {
    mEmail.value = localStorage.getItem('email') || ''
    mUsername.value = localStorage.getItem('username') || ''
}
</script>

<template>
    <div id="my-profile">
        <div id="fields">
            <h2>
                Change profile
            </h2>
            <TodoField>
                <FixedSizeSpan>
                    Username:
                </FixedSizeSpan>
                <CustomInput
                    input-type="text"
                    placeholder-text="Username"
                    :is-password-field="false"
                    @change-value="usernameChanged"
                    @enter-pressed="save"
                    :initial-value="mUsername"
                />
            </TodoField>
            <TodoField>
                <FixedSizeSpan>
                    Email:
                </FixedSizeSpan>
                <CustomInput
                    input-type="text"
                    placeholder-text="Email"
                    :is-password-field="false"
                    @change-value="emailChanged"
                    @enter-pressed="save"
                    :initial-value="mEmail"
                />
            </TodoField>
            <div id="buttons">
                <CustomButton 
                    text="Save" 
                    @button-click="save" 
                />
                <button @click="reoadUserData">
                    Cancel
                </button>
            </div>
        </div>
    </div>
    <Teleport to="body">
        <ErrorMessagePopup
            :error-text="mErrorMessage"
            :show-popup="mShowErrorPopup"
            @close="closeErrorPopup"
        />
        <div 
            v-show="mShowSaveToaster"
            id="save-toaster"
        >
            <span class="material-symbols-outlined">
                info
            </span>
            Profile data saved
        </div>
    </Teleport>
</template>

<style scoped>
    #fields {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 1vh;
    }

    #save-toaster {
        position: absolute;
        bottom: 5vh;
        left: 43vw;
        padding: 10px;
        background-color: var(--main-color);
        color: white;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 7px;
        box-shadow: inset;
        gap: 0.2vw;
    }

    #buttons {
        display: flex;
        justify-content: center;
        gap: 2vw;
    }

    button {
        padding: 7px 14px;
        background-color: var(--green-secondary-color);
        color: white;
        border-color: var(--green-secondary-color);
        font-size: var(--default-font-size);
    }

    button:hover {
        border-color: currentColor;
        cursor: pointer;
    }
</style>