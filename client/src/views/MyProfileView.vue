<script setup lang="ts">
import CustomInput from '@/components/CustomInput.vue'
import TodoField from '@/components/TodoField.vue'
import FixedSizeSpan from '@/components/FixedSizeSpan.vue'
import { ref, onMounted } from 'vue'
import CustomButton from '@/components/CustomButton.vue'
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue'
import { updateUser } from '@/data/server'
import PopupBase from '@/components/PopupBase.vue'
import router from '@/router'
import { clearUserData } from '@/data/server'

const mEmail = ref('')
const mUsername = ref('')
const mErrorMessage = ref('')
const mShowErrorPopup = ref(false)
const mShowSaveConfirmationPopup = ref(false)

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
                mShowSaveConfirmationPopup.value = true
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

function saveDataAndLogout() {
    clearUserData();
    router.push('/login');
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
        <PopupBase
            :show="mShowSaveConfirmationPopup" 
        >
            <div id="popup-body">
                <span class="material-symbols-outlined">
                    info
                </span>
                In order to update your data, you need to logout and login again.
                <CustomButton 
                    text="OK" 
                    @button-click="saveDataAndLogout" 
                />
            </div>
        </PopupBase>
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

    #popup-body {
        width: 35vw;
        height: 20vh;
        background-color: white;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        gap: 1vh;
        font-size: var(--default-font-size);
        border-radius: 10px;
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