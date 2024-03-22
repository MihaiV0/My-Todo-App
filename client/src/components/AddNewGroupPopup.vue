<script setup lang="ts">
import { ref } from 'vue';
import CustomButton from './CustomButton.vue';
import CustomInput from './CustomInput.vue';
import PopupBase from './PopupBase.vue';

const props = defineProps({
    showPopup: Boolean,
});

const mValue = ref('');

const emits = defineEmits(['close', 'save-group']);

function updateValue(newValue: string) {
    mValue.value = newValue;
}

</script>

<template>
    <PopupBase 
        :show="showPopup" 
        @close-popup="emits('close')"
    >
        <div class="popup-body">
            <h1>
                Add new group
            </h1>
            <div class="container">
                Group name:
                <CustomInput
                    input-type="text"
                    placeholder-text=""
                    :is-password-field="false"
                    @change-value="updateValue"
                    custom-width="25vw"
                />
            </div>
            <div class="container">
                <CustomButton 
                    text="OK" 
                    @button-click="emits('save-group', mValue)" 
                />
                <button @click="emits('close')">
                    Cancel
                </button>
            </div>
        </div>
    </PopupBase>
</template>

<style scoped>
    .popup-body {
        background-color: #E9E9E9;
        width: 35vw;
        height: 27vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 2vh;
        padding: 10px;
        border-radius: 10px;
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

    .container {
        display: flex;
        gap: 1vw;
        align-items: center;
        font-size: var(--default-font-size);
    }
</style>