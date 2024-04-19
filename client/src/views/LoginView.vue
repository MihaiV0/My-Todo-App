<script setup lang="ts">
import CustomInput from '@/components/CustomInput.vue';
import PasswordInput from '@/components/PasswordInput.vue';
import FormTitle from '@/components/FormTitle.vue';
import CustomButton from '@/components/CustomButton.vue';
import { ref } from 'vue';
import router from '@/router';
import { saveUserData, loginUserOnServer } from '@/data/server';
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue';
import bcrypt from "bcryptjs";

const usernameOrEmailText = ref('');
const passwordText = ref('');
const showErrorPopup = ref(false);
const errorMessage = ref('');

function usernameOrEmailChanged(newUsernameOrEmail: string) {
    usernameOrEmailText.value = newUsernameOrEmail;
}

function passwordChanged(newPassword: string) {
    passwordText.value = newPassword;
}

async function loginUser() {
    const hashPassword = await bcrypt.hash(passwordText.value, "$2a$10$QkRidA35ea0Fzm/ObrOEgO");

    loginUserOnServer(usernameOrEmailText.value, hashPassword)
        .then(res => {
            saveUserData(res.username, res.email);
            router.push('/my-todos');
        })
        .catch(err => {
            errorMessage.value = err.message;
            showErrorPopup.value = true;
        });
}

function closeErrorPopup() {
    showErrorPopup.value = false;
}

</script>

<template>
    <div id="container">
        <FormTitle
            text="Login"
        />
        <CustomInput
            input-type="text"
            placeholder-text="Username or email"
            :is-password-field="false"
            @change-value="usernameOrEmailChanged"
        />
        <PasswordInput
            placeholder="Password"
            @text-changed="passwordChanged"
            @enter-pressed="loginUser"
            :has-tooltip="false"
        />
        <div>
            Don't have an account? Sign up
            <span
                id="register"
                @click="router.push('/register')"
            >
                here
            </span>
        </div>
        <CustomButton 
            text="Login"
            @button-click="loginUser"
        />
    </div>
    <Teleport to="body">
        <ErrorMessagePopup
            :error-text="errorMessage"
            :show-popup="showErrorPopup"
            @close="closeErrorPopup"
        />
    </Teleport>
</template>

<style scoped>
#container {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    gap: 2vh;
}

#register {
    text-decoration: underline;
}

#register:hover {
    cursor: pointer;
}
</style>