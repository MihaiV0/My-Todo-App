<script setup lang="ts">
import CustomButton from '@/components/CustomButton.vue';
import CustomInput from '@/components/CustomInput.vue';
import FormTitle from '@/components/FormTitle.vue';
import { ref } from 'vue';
import { registerUserOnServer } from '../data/server';
import router from '@/router';
import PasswordInput from '@/components/PasswordInput.vue';
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue';
import bcrypt from "bcryptjs";
import TermsAndConditionsPopup from '@/components/TermsAndConditionsPopup.vue';

const acceptedTermsAndConditions = ref(false);
const usernameText = ref('');
const emailText = ref('');
const passwordText = ref('');
const confirmPasswordText = ref('');
const errorMessage = ref('');
const showErrorPopup = ref(false);
const showTermsAndConditionsPopup = ref(false);

async function registerUser() {
    let inputOK = true;

    const usernameRegex = /^[a-zA-Z0-9_]+$/;

    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    
    // regexes used for password checking
    const uppercaseRegex = /.*[A-Z].*/;
    const lowercaseRegex = /.*[a-z].*/;
    const numbersRegex = /.*\d.*/;
    const specialCharactersRegex = /.*[&#!_!@*].*/;

    if (!usernameText.value || 
        !emailText.value || 
        !passwordText.value || 
        !confirmPasswordText.value) {

        inputOK = false;
        errorMessage.value = 'All fields are mandatory';
        showErrorPopup.value = true;
    }

    if (inputOK && !acceptedTermsAndConditions.value) {
        inputOK = false;
        errorMessage.value = 'You must accept our terms and conditions';
        showErrorPopup.value = true;
    }

    if (inputOK && !usernameRegex.test(usernameText.value)) {
        inputOK = false;
        errorMessage.value = 'Username must contain only letters, numbers or underscores(_)';
        showErrorPopup.value = true;
    }

    if (inputOK && !emailRegex.test(emailText.value)) {
        inputOK = false;
        errorMessage.value = 'Invalid email address';
        showErrorPopup.value = true;
    }

    if (inputOK && (passwordText.value != confirmPasswordText.value)) {
        inputOK = false;
        errorMessage.value = 'The 2 passwords are not the same!';
        showErrorPopup.value = true;
    }

    if (inputOK && (passwordText.value.length < 8)) {
        inputOK = false;
        errorMessage.value = 'The password must be at least 8 characters long!';
        showErrorPopup.value = true;
    }

    if (inputOK && !uppercaseRegex.test(passwordText.value)) {
        inputOK = false;
        errorMessage.value = 'The password must contain uppercase letters';
        showErrorPopup.value = true;
    }

    if (inputOK && !lowercaseRegex.test(passwordText.value)) {
        inputOK = false;
        errorMessage.value = 'The password must contain lowercase letters';
        showErrorPopup.value = true;
    }

    if (inputOK && !numbersRegex.test(passwordText.value)) {
        inputOK = false;
        errorMessage.value = 'The password must contain numbers';
        showErrorPopup.value = true;
    }

    if (inputOK && !specialCharactersRegex.test(passwordText.value)) {
        inputOK = false;
        errorMessage.value = 'The password must contain special characters(!, @, #, &, *, _)';
        showErrorPopup.value = true;
    }

    if (inputOK) {
        const hashPassword = await bcrypt.hash(passwordText.value, "$2a$10$QkRidA35ea0Fzm/ObrOEgO");

        registerUserOnServer(usernameText.value, emailText.value, hashPassword)
            .then(res => {
                router.push('/register-success');
            })
            .catch(err => {
                errorMessage.value = err.message;
                showErrorPopup.value = true;
            });
    }
}

function changedUsername(newUsername: string) {
    usernameText.value = newUsername;
}

function changedEmail(newEmail: string) {
    emailText.value = newEmail;
}

function changedPassword(newPassword: string) {
    passwordText.value = newPassword;
}

function changedConfirmPassword(newConfirmPassword: string) {
    confirmPasswordText.value = newConfirmPassword;
}

function closeErrorPopup() {
    showErrorPopup.value = false;
}

function closeTermsAndConditionsPopup() {
    showTermsAndConditionsPopup.value = false;
}

function acceptTermsAndConditions() {
    acceptedTermsAndConditions.value = true;
    closeTermsAndConditionsPopup();
}

</script>

<template>
    <div id="container">
        <FormTitle
            text="Register"
        />
        <CustomInput
            input-type="text"
            placeholder-text="Username"
            @change-value="changedUsername"
            :is-password-field="false"
            tooltip-text="The username can only contain letters, numbers or underscores(_)"
        />
        <CustomInput
            input-type="text"
            placeholder-text="Email"
            @change-value="changedEmail"
            :is-password-field="false"
        />
        <PasswordInput
            id="password"
            placeholder="Password"
            @text-changed="changedPassword"
            :has-tooltip="true"
        />
        <PasswordInput
            id="confirm-password"
            placeholder="Confirm password"
            @text-changed="changedConfirmPassword"
            @enter-pressed="registerUser"
            :has-tooltip="true"
        />
        <div>
            <input 
                type="checkbox" 
                id="terms-and-conditions"
                v-model="acceptedTermsAndConditions"
            >
            I've read and accept the 
            <span
                id="terms"
                @click="showTermsAndConditionsPopup = true"
            >
                terms and conditions
            </span>
        </div>
        <CustomButton 
            text="Register"
            @button-click="registerUser"
        />
    </div>
    <Teleport to="body">
        <ErrorMessagePopup
            :error-text="errorMessage"
            :show-popup="showErrorPopup"
            @close="closeErrorPopup"
        />
    </Teleport>
    <Teleport to="body">
        <TermsAndConditionsPopup
            :show-popup="showTermsAndConditionsPopup"
            @close="closeTermsAndConditionsPopup"
            @accept-terms="acceptTermsAndConditions"
        />
    </Teleport>
</template>

<style scoped>
    #terms {
        text-decoration: underline;
    }

    #terms:hover {
        cursor: pointer;
    }

    #container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 3vh;
    }

    #terms-and-conditions {
        accent-color: var(--main-color);
    }
</style>