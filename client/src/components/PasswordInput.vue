<script setup lang="ts">
import CustomInput from './CustomInput.vue';
import { ref } from 'vue';

const props = defineProps({
    placeholder: String,
    id: String,
    hasTooltip: Boolean,
});

const emits = defineEmits(['text-changed']);

const inputType = ref('password');

function handleChangeValue(newText: string) {
    emits('text-changed', newText);
}

function changeInputType() {
    if (inputType.value == 'password') {
        inputType.value = 'text';
    } else {
        inputType.value = 'password';
    }
}

</script>

<template>
    <CustomInput
        :input-type="inputType"
        :placeholder-text="placeholder"
        @change-value="handleChangeValue"
        :is-password-field="true"
        :tooltip-text="hasTooltip ? 
            `The password must:
            <ul>
                <li>have at least 8 characters</li>
                <li>contain at least one uppercase and one lowercase letter</li>
                <li>contain at least one number</li>
                <li>have at least one special chatacter(!, @, #, &, *, _)</li>
            </ul>` 
            : 
            ''
            "
    >
        <button
            class="borderless-button"
            @click="changeInputType"
            :id="`button-${id}`"
        >
            <span 
                :id="`span-${id}`"
                class="material-symbols-outlined"
            >
                {{ inputType == 'password' ? 'visibility' : 'visibility_off' }}
            </span>
        </button>
    </CustomInput>
</template>

<style scoped>
    .borderless-button {
        border: 0.5px solid transparent;
        background-color: white;
        padding: 5px 4px;
    }

    .borderless-button:hover {
        cursor: pointer;
    }

    .material-symbols-outlined {
        font-size: 17px;
    }
</style>